package fi.mazhar.dhikr

/**
 * Fully serializable app state (everything that must survive process death).
 * The canonical dhikr catalog / routines are *not* stored here — they are
 * bundled content. Only progress, shared goals, target overrides, and
 * settings are persisted.
 */
data class SavedState(
    val settings: Settings = Settings(),
    val progress: List<DhikrProgress> = emptyList(),
    val goals: List<SharedDailyGoal> = emptyList(),
    /** Per-entry repetition-target personalization, keyed by [RoutineKey.toString]. */
    val targetOverrides: Map<String, Int> = emptyMap(),
    /** Most recently opened routine, for the "resume" card. */
    val lastOpenedRoutine: RoutineId? = null,
)

/**
 * Persistence boundary. Implementations: [InMemoryDhikrStore] (tests) and an
 * offline file-backed store on Android. No network is ever required.
 */
interface DhikrStore {
    fun load(): SavedState?
    fun save(state: SavedState)
}

/** In-memory store used by the JVM test suite and by other platforms. */
class InMemoryDhikrStore(initial: SavedState? = null) : DhikrStore {
    private var state: SavedState? = initial

    override fun load(): SavedState? = state

    override fun save(state: SavedState) {
        this.state = state
    }
}

/**
 * Versioned, line-based codec for [SavedState]. Deliberately dependency-free
 * so it works identically on every KMP target and in the JVM test suite.
 *
 * Persisted fields are constrained (day keys, enum names, slug ids, numbers),
 * so no escaping is required; unknown or malformed lines are skipped for
 * forward compatibility.
 */
object SavedStateCodec {

    private const val HEADER = "DHIKR_STATE_V1"
    private const val SEP = "|"

    fun encode(state: SavedState): String = buildString {
        appendLine(HEADER)
        appendLine(
            listOf(
                "S",
                state.settings.theme.name,
                state.settings.arabicFontScale.toString(),
                state.settings.showTransliteration,
                state.settings.showTranslation,
                state.settings.translationLanguage.name,
                state.settings.haptics,
                state.settings.autoAdvance,
                state.settings.sharedGoalEnabled,
                state.settings.laIlaTarget100,
                state.lastOpenedRoutine?.name ?: "",
            ).joinToString(SEP),
        )
        for (entry in state.targetOverrides.entries.sortedBy { it.key }) {
            appendLine(listOf("T", entry.key, entry.value).joinToString(SEP))
        }
        for (p in state.progress) {
            appendLine(
                listOf(
                    "P",
                    p.dayKey,
                    p.routineKey.toString(),
                    p.dhikrId,
                    p.completedCount,
                    p.completedAt?.toString() ?: "",
                ).joinToString(SEP),
            )
        }
        for (g in state.goals) {
            appendLine(
                listOf(
                    "G",
                    g.goalId,
                    g.dayKey,
                    g.target,
                    g.completedCount,
                    g.satisfiedBy?.toString() ?: "",
                    g.contributions.entries
                        .sortedBy { it.key }
                        .joinToString(",") { "${it.key}:${it.value}" },
                    g.applicableRoutineIds.joinToString(",") { it.name },
                ).joinToString(SEP),
            )
        }
    }

    fun decode(text: String): SavedState? {
        val lines = text.lines().map { it.trimEnd('\r') }.filter { it.isNotBlank() }
        if (lines.isEmpty() || lines[0] != HEADER) return null

        var settings = Settings()
        var lastOpened: RoutineId? = null
        val overrides = LinkedHashMap<String, Int>()
        val progress = ArrayList<DhikrProgress>()
        val goals = ArrayList<SharedDailyGoal>()

        for (line in lines.drop(1)) {
            val f = line.split(SEP)
            when (f[0]) {
                "S" -> if (f.size >= 11) runCatching {
                    settings = Settings(
                        theme = AppTheme.valueOf(f[1]),
                        arabicFontScale = f[2].toFloat(),
                        showTransliteration = f[3].toBoolean(),
                        showTranslation = f[4].toBoolean(),
                        translationLanguage = TranslationLanguage.valueOf(f[5]),
                        haptics = f[6].toBoolean(),
                        autoAdvance = f[7].toBoolean(),
                        sharedGoalEnabled = f[8].toBoolean(),
                        laIlaTarget100 = f[9].toBoolean(),
                    )
                    lastOpened = if (f[10].isEmpty()) null else RoutineId.valueOf(f[10])
                }
                "T" -> if (f.size >= 3) runCatching {
                    overrides[f[1]] = f[2].toInt()
                }
                "P" -> if (f.size >= 5) runCatching {
                    val (routineId, order) = parseRoutineKey(f[2]) ?: return@runCatching
                    progress.add(
                        DhikrProgress(
                            dayKey = f[1],
                            routineKey = RoutineKey(routineId, order),
                            dhikrId = f[3],
                            completedCount = f[4].toInt(),
                            completedAt = f.getOrNull(5)?.takeIf { it.isNotEmpty() }?.toLong(),
                        ),
                    )
                }
                "G" -> if (f.size >= 7) runCatching {
                    val contributions = LinkedHashMap<String, Int>()
                    if (f[6].isNotEmpty()) {
                        for (pair in f[6].split(",")) {
                            val kv = pair.split(":", limit = 2)
                            if (kv.size == 2) contributions[kv[0]] = kv[1].toInt()
                        }
                    }
                    val satisfied = f[5].takeIf { it.isNotEmpty() }?.let { parseRoutineKey(it) }
                    val applicable = f.getOrNull(7)?.takeIf { it.isNotEmpty() }
                        ?.split(",")?.mapNotNull { name ->
                            runCatching { RoutineId.valueOf(name) }.getOrNull()
                        } ?: emptyList()
                    goals.add(
                        SharedDailyGoal(
                            goalId = f[1],
                            dayKey = f[2],
                            target = f[3].toInt(),
                            completedCount = f[4].toInt(),
                            applicableRoutineIds = applicable,
                            satisfiedBy = satisfied?.let { RoutineKey(it.first, it.second) },
                            contributions = contributions,
                        ),
                    )
                }
            }
        }
        return SavedState(
            settings = settings,
            progress = progress,
            goals = goals,
            targetOverrides = overrides,
            lastOpenedRoutine = lastOpened,
        )
    }

    private fun parseRoutineKey(s: String): Pair<RoutineId, Int>? {
        val idx = s.lastIndexOf('-')
        if (idx <= 0 || idx == s.length - 1) return null
        return try {
            RoutineId.valueOf(s.substring(0, idx)) to s.substring(idx + 1).toInt()
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}

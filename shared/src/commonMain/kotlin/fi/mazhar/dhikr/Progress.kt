package fi.mazhar.dhikr

/**
 * Completed progress for a single routine entry on a specific day.
 * Keyed by [dayKey] + [RoutineKey] so one routine's counts never merge with
 * another routine's, and history is retained across days.
 */
data class DhikrProgress(
    val dayKey: String,
    val routineKey: RoutineKey,
    val dhikrId: String,
    val completedCount: Int,
    val completedAt: Long? = null,
)

/**
 * Legacy shared daily goal kept for decoding older saves and backups.
 */
data class SharedDailyGoal(
    val goalId: String,
    val dayKey: String,
    val target: Int,
    val completedCount: Int = 0,
    val applicableRoutineIds: List<RoutineId> = emptyList(),
    val satisfiedBy: RoutineKey? = null,
    /**
     * Repetitions contributed per routine key before counts were separated.
     */
    val contributions: Map<String, Int> = emptyMap(),
)

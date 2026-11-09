package fi.mazhar.dhikr

/** Stable identifier for a built-in routine or a personal list. */
class RoutineId private constructor(val name: String) {
    val isPersonal: Boolean get() = name.startsWith("PERSONAL_")

    override fun equals(other: Any?): Boolean = other is RoutineId && name == other.name
    override fun hashCode(): Int = name.hashCode()
    override fun toString(): String = name

    companion object {
        val FAJR = RoutineId("FAJR")
        val DHUHR = RoutineId("DHUHR")
        val ASR = RoutineId("ASR")
        val MAGHRIB = RoutineId("MAGHRIB")
        val ISHA = RoutineId("ISHA")
        val BEFORE_SLEEP = RoutineId("BEFORE_SLEEP")

        private val builtIn = listOf(FAJR, DHUHR, ASR, MAGHRIB, ISHA, BEFORE_SLEEP).associateBy { it.name }
        private val personalPattern = Regex("PERSONAL_[0-9a-f]+")

        fun valueOf(name: String): RoutineId = builtIn[name]
            ?: if (personalPattern.matches(name)) RoutineId(name)
            else throw IllegalArgumentException("Unknown routine ID")
    }
}

/** Ordered, stable identifier for a slot within a routine. */
data class RoutineKey(
    val routineId: RoutineId,
    val order: Int,
) {
    override fun toString(): String = "${routineId.name}-$order"
}

/**
 * Stable identifier for a routine's appearance of a dhikr.
 * A dhikr's *content* lives on [Dhikr]; the *target/position* in a specific
 * routine lives here. Progress is keyed by this to prevent cross-routine
 * merging of counts.
 */
data class RoutineEntry(
    val key: RoutineKey,
    val dhikrId: String,
    val target: Int,
    val enabled: Boolean = true,
    /** When set, this entry is satisfied via a shared daily goal. */
    val sharedGoalId: String? = null,
    /** For the shared-goal case: only add to progress if goal not met. */
    val conditionalOnGoal: Boolean = false,
)

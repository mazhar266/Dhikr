package fi.mazhar.dhikr

/** A daily routine (Fajr, Dhuhr, Asr, Maghrib, Isha, Before Sleep). */
enum class RoutineId {
    FAJR,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA,
    BEFORE_SLEEP,
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

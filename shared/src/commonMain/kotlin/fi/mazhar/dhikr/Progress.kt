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
 * A shared daily goal: a single repetition target that can be satisfied by
 * recitations in one or more routines (e.g. the "La Ilaha Illallah" 10/100
 * goal shared between Fajr and Asr).
 *
 * [satisfiedBy] records which routineKey first fulfilled the goal on this day,
 * so we can report an Asr entry as *satisfied via the shared goal* rather than
 * as independently recited in Asr.
 */
data class SharedDailyGoal(
    val goalId: String,
    val dayKey: String,
    val target: Int,
    val completedCount: Int = 0,
    val applicableRoutineIds: List<RoutineId> = emptyList(),
    val satisfiedBy: RoutineKey? = null,
    /**
     * Repetitions contributed per routine key, so undo in one routine can
     * never roll back taps made from another routine.
     */
    val contributions: Map<String, Int> = emptyMap(),
)

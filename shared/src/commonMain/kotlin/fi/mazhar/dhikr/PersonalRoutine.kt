package fi.mazhar.dhikr

import fi.mazhar.dhikr.content.RoutineDefinition

data class PersonalRoutine(
    val id: RoutineId,
    val title: String,
    val entries: List<PersonalDhikr> = emptyList(),
) {
    internal fun definition() = RoutineDefinition(
        id = id,
        title = title,
        titleBn = title,
        arabicTitle = title,
        description = "",
        descriptionBn = "",
        entries = entries.map { RoutineEntry(RoutineKey(id, it.order), it.id, it.target) },
    )
}

data class PersonalDhikr(
    val order: Int,
    val arabic: String,
    val pronunciation: String,
    val meaning: String,
    val target: Int,
) {
    internal val id: String get() = "personal-dhikr-$order"

    internal fun asDhikr() = Dhikr(
        id = id,
        title = sequenceOf(arabic, pronunciation, meaning).first { it.isNotBlank() }.lineSequence().first(),
        transliteration = pronunciation,
        arabic = arabic,
        translation = meaning,
        category = DhikrCategory.CUSTOM,
        kind = DhikrKind.REPEATED,
        verification = VerificationStatus.USER_PROVIDED,
    )
}

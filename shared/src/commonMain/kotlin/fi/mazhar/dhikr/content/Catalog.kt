package fi.mazhar.dhikr.content

import fi.mazhar.dhikr.Dhikr
import fi.mazhar.dhikr.DhikrCategory
import fi.mazhar.dhikr.DhikrKind
import fi.mazhar.dhikr.SourceRef
import fi.mazhar.dhikr.SourceType
import fi.mazhar.dhikr.VerificationStatus

/**
 * Canonical dhikr catalog. Qur'anic passages are generated verbatim from the
 * Quran.com uthmani dataset (see [QuranTexts]); supplication wordings follow
 * the cited hadith collections. Anything not fully verified is flagged
 * [VerificationStatus.NEEDS_VERIFICATION] and listed in
 * DHIKR_CONTENT_REVIEW.md — do not upgrade it silently.
 *
 * Repetition counts and prayer timings are kept separate from these content
 * definitions: the user's personal routine (which entries, how many times)
 * lives in [Routines] and is documented as user-configured unless the cited
 * hadith establishes the count.
 */
object DhikrCatalog {

    private val items: List<Dhikr> = listOf(
        // ------------------------------------------------------------- Tasbih
        Dhikr(
            id = "allahu-akbar",
            title = "Allahu Akbar",
            arabicTitle = "اللَّهُ أَكْبَرُ",
            transliteration = "Allāhu akbar",
            arabic = "اللَّهُ أَكْبَرُ",
            translation = "Allah is the Greatest.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 843; Sahih Muslim 596a (post-prayer tasbih)"),
            ),
            notes = "Opening takbir and the 33/34-count tasbih share this wording.",
        ),
        Dhikr(
            id = "astaghfirullah",
            title = "Astaghfirullah",
            arabicTitle = "أَسْتَغْفِرُ اللَّهَ",
            transliteration = "Astaghfirullāh",
            arabic = "أَسْتَغْفِرُ اللَّهَ",
            translation = "I seek Allah's forgiveness.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih Muslim 591 (said three times after the salam)"),
            ),
        ),
        Dhikr(
            id = "takbir-tahlil-after-salam",
            title = "Takbir al-Tashriq",
            arabicTitle = "التَّكْبِيرُ وَالتَّهْلِيلُ",
            transliteration = "Allāhu akbar, Allāhu akbar. Lā ilāha illallāh, wallāhu akbar, Allāhu akbar, wa lillāhil-ḥamd.",
            arabic = "اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، لَا إِلَٰهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، وَلِلَّهِ الْحَمْدُ",
            translation = "Allah is the Greatest, Allah is the Greatest. There is no god but Allah. Allah is the Greatest, Allah is the Greatest, and all praise belongs to Allah.",
            category = DhikrCategory.CUSTOM,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.OTHER, "Musannaf Ibn Abi Shaybah, Book of the Eid Prayers, chapter on takbir on the Day of Arafah"),
            ),
            notes = "Reported as the takbir of Ibn Mas'ud during the days of Tashriq. Daily placement after Fajr and Asr is a personal routine.",
        ),
        Dhikr(
            id = "antas-salam",
            title = "Allahumma Antas-Salam",
            arabicTitle = "اللَّهُمَّ أَنْتَ السَّلَامُ",
            transliteration = "Allāhumma antas-salām wa minkas-salām, tabārakta yā dhal-jalāli wal-ikrām",
            arabic = "اللَّهُمَّ أَنْتَ السَّلَامُ، وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
            translation = "O Allah, You are Peace and from You comes peace. Blessed are You, O Possessor of majesty and honour.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih Muslim 592a", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "ajirni-minan-nar",
            title = "Allahumma Ajirni Minan-Nar",
            arabicTitle = "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ",
            transliteration = "Allāhumma ajirnī minan-nār",
            arabic = "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ",
            translation = "O Allah, protect me from the Fire.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.NEEDS_VERIFICATION,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 5079",
                    grading = "Da’if per al-Albani",
                ),
            ),
            notes = "The weak report mentions seven times after Maghrib and Fajr. The app uses the personal Fajr and Asr schedule.",
        ),
        Dhikr(
            id = "subhanallah",
            title = "Subhanallah",
            arabicTitle = "سُبْحَانَ اللَّهِ",
            transliteration = "Subḥānallāh",
            arabic = "سُبْحَانَ اللَّهِ",
            translation = "Glory be to Allah.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 843; Sahih Muslim 595", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "alhamdulillah",
            title = "Alhamdulillah",
            arabicTitle = "الْحَمْدُ لِلَّهِ",
            transliteration = "Alḥamdulillāh",
            arabic = "الْحَمْدُ لِلَّهِ",
            translation = "All praise is for Allah.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 843; Sahih Muslim 595", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "salawat-ibrahimiyyah",
            title = "Salawat upon the Prophet ﷺ",
            arabicTitle = "الصَّلَاةُ الْإِبْرَاهِيمِيَّةُ",
            transliteration = "Allāhumma ṣalli ʿalā Muḥammadin wa ʿalā āli Muḥammadin kamā ṣallayta ʿalā Ibrāhīma wa ʿalā āli Ibrāhīma innaka ḥamīdum majīd. Allāhumma bārik ʿalā Muḥammadin wa ʿalā āli Muḥammadin kamā bārakta ʿalā Ibrāhīma wa ʿalā āli Ibrāhīma innaka ḥamīdum majīd",
            arabic = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ، وَعَلَىٰ آلِ مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ، وَعَلَىٰ آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ. اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ، وَعَلَىٰ آلِ مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ، وَعَلَىٰ آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ",
            translation = "O Allah, send prayers upon Muhammad and the family of Muhammad as You sent prayers upon Ibrahim and the family of Ibrahim. You are indeed Praiseworthy, Most Honourable. O Allah, send blessings upon Muhammad and the family of Muhammad as You sent blessings upon Ibrahim and the family of Ibrahim. You are indeed Praiseworthy, Most Honourable.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 3370", grading = "Sahih"),
            ),
            notes = "The wording is the verified Ibrahimiyyah formula; a tenfold repetition after prayer is the user's personal routine.",
        ),
        Dhikr(
            id = "raditu-billahi",
            title = "Raditu Billahi Rabban",
            arabicTitle = "رَضِيتُ بِاللَّهِ رَبًّا",
            transliteration = "Raḍītu billāhi rabban, wa bil-islāmi dīnan, wa bi-Muḥammadin nabiyyan",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ نَبِيًّا",
            translation = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad ﷺ as my Prophet.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Jami’ at-Tirmidhi 3389",
                    grading = "Hasan gharib per at-Tirmidhi; evening wording",
                ),
            ),
            notes = "Three repetitions after Fajr and Asr follow the personal routine.",
        ),
        Dhikr(
            id = "hasbiyallahu",
            title = "Hasbiyallahu La Ilaha Illa Huwa",
            arabicTitle = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ",
            transliteration = "Ḥasbiyallāhu lā ilāha illā huwa, ʿalayhi tawakkaltu, wa huwa rabbul-ʿarshil-ʿaẓīm",
            arabic = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ، عَلَيْهِ تَوَكَّلْتُ، وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
            translation = "Allah is sufficient for me; there is no god but Him. In Him I trust, and He is the Lord of the Mighty Throne.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Tawbah 9:129 (closing words)"),
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 5081",
                    grading = "Abu al-Darda’ report; graded mawdu’ by al-Albani",
                ),
            ),
            notes = "The wording is Qur’anic. Seven repetitions after Fajr and Asr follow the personal routine.",
        ),

        // ------------------------------------------- A'udhu bi kalimatillah…
        Dhikr(
            id = "audhu-kalimat-min-sharri",
            title = "A'udhu bi Kalimatillah (evil of creation)",
            arabicTitle = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ",
            transliteration = "Aʿūdhu bikalimātillāhit-tāmmāti min sharri mā khalaq",
            arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            translation = "I seek refuge in the perfect words of Allah from the evil of what He has created.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih Muslim 2708, 2710", grading = "Sahih"),
            ),
            notes = "Sahih wordings are tied to stopping at a place (Muslim 2708) and to evening protection (Muslim 2710); the after-prayer placement is the user's routine.",
        ),
        Dhikr(
            id = "audhu-kalimat-shaytan",
            title = "A'udhu bi Kalimatillah (devils and the evil eye)",
            arabicTitle = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّةِ",
            transliteration = "Aʿūdhu bikalimātillāhit-tāmmati min kulli shayṭānin wa hāmmah, wa min kulli ʿaynin lāmmah",
            arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّةِ مِنْ كُلِّ شَيْطَانٍ وَهَامَّةٍ، وَمِنْ كُلِّ عَيْنٍ لَامَّةٍ",
            translation = "I seek refuge in the perfect words of Allah from every devil and poisonous creature, and from every envious, evil eye.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 3371", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "audhu-kalimat-ghadab",
            title = "A'udhu bi Kalimatillah (anger, punishment, devils)",
            arabicTitle = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّةِ",
            transliteration = "Aʿūdhu bikalimātillāhit-tāmmati min ghaḍabihi wa ʿiqābihi, wa sharri ʿibādihi, wa min hamazātish-shayāṭīni wa an yaḥḍurūn",
            arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّةِ مِنْ غَضَبِهِ وَعِقَابِهِ، وَشَرِّ عِبَادِهِ، وَمِنْ هَمَزَاتِ الشَّيَاطِينِ وَأَنْ يَحْضُرُونِ",
            translation = "I seek refuge in the perfect words of Allah from His anger and His punishment, from the evil of His slaves, and from the whisperings of devils and their presence.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.NEEDS_VERIFICATION,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Jami’ at-Tirmidhi 3528 (alarm during sleep)",
                    grading = "Hasan gharib per at-Tirmidhi; da’if per Darussalam",
                ),
            ),
        ),
        Dhikr(
            id = "bismillah-la-yadurru",
            title = "Bismillahil-Ladhi La Yadurru",
            arabicTitle = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ",
            transliteration = "Bismillāhil-ladhī lā yaḍurru maʿasmihi shayʾun fil-arḍi wa lā fis-samāʾi wa huwas-samīʿul-ʿalīm",
            arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ، وَهُوَ السَّمِيعُ الْعَلِيمُ",
            translation = "In the name of Allah, with whose name nothing on earth or in heaven can cause harm; and He is the All-Hearing, the All-Knowing.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 5088; Jami' at-Tirmidhi 3601",
                    grading = "Sahih per al-Albani; hadith specifies three times morning and evening",
                ),
            ),
        ),

        // ------------------------------------------------------- Istighfar du'as
        Dhikr(
            id = "sayyidul-istighfar",
            title = "Sayyid al-Istighfar",
            arabicTitle = "سَيِّدُ الِاسْتِغْفَارِ",
            transliteration = "Allāhumma anta rabbī lā ilāha illā anta, khalaqtanī wa ana ʿabduka, wa ana ʿalā ʿahdika wa waʿdika mas-taṭaʿtu, aʿūdhu bika min sharri mā ṣanaʿtu, abūʾu laka biniʿmatika ʿalayya, wa abūʾu bidhambī, faghfir lī fa-innahū lā yaghfirudh-dhunūba illā anta",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي، فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            translation = "O Allah, You are my Lord; there is no god but You. You created me and I am Your slave. I keep Your covenant and promise as much as I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favour upon me, and I acknowledge my sin, so forgive me; for none forgives sins except You.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 6306", grading = "Sahih; morning and evening"),
            ),
        ),

        // ------------------------------------------------------ Health & wellbeing
        Dhikr(
            id = "afini-badani",
            title = "Allahumma 'Afini (wellbeing and protection)",
            arabicTitle = "اللَّهُمَّ عَافِنِي فِي بَدَنِي",
            transliteration = "Allāhumma ʿāfinī fī badanī, Allāhumma ʿāfinī fī samʿī, Allāhumma ʿāfinī fī baṣarī, lā ilāha illā anta.\n\nAllāhumma innī aʿūdhu bika minal-kufr wal-faqr. Allāhumma innī aʿūdhu bika min ʿadhābil-qabr, lā ilāha illā anta",
            arabic = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَٰهَ إِلَّا أَنْتَ.\n\nاللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، لَا إِلَٰهَ إِلَّا أَنْتَ",
            translation = "O Allah, grant my body health; O Allah, grant my hearing health; O Allah, grant my sight health. There is no god but You.\n\nO Allah, I seek refuge in You from disbelief and poverty. O Allah, I seek refuge in You from the torment of the grave. There is no god but You.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sunan Abi Dawud 5090 (both supplications, three times each morning and evening)", grading = "Hasan in chain per al-Albani"),
            ),
            notes = "Each app repetition covers both supplications.",
        ),

        // -------------------------------------------------------- La ilaha illallah
        Dhikr(
            id = "la-ilaha-illallah-goal",
            title = "La Ilaha Illallah (daily tasbih)",
            arabicTitle = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ",
            transliteration = "Lā ilāha illallāhu waḥdahu lā sharīka lah, lahul-mulku wa lahul-ḥamdu, wa huwa ʿalā kulli shayʾin qadīr",
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            translation = "There is no god but Allah alone, without partner. To Him belong the dominion and all praise, and He is over all things competent.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sahih Muslim 2693 (ten times); Sahih Muslim 2691 and Sahih al-Bukhari 6403 (one hundred times)",
                    grading = "Sahih",
                ),
            ),
            notes = "10× or 100× per the user's daily-goal setting; the Fajr/Asr sharing of this goal is a user-configured arrangement.",
        ),
        Dhikr(
            id = "subhanallahi-wabihamdihi-100",
            title = "Subhanallahi wa Bihamdihi — one hundred times",
            arabicTitle = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            transliteration = "Subḥānallāhi wa biḥamdih, subḥānallāhil-ʿaẓīm",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            translation = "Glory be to Allah and praise Him; glory be to Allah the Almighty.",
            category = DhikrCategory.CUSTOM,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sahih al-Bukhari 6406 (the paired phrases); Sahih al-Bukhari 6405 (the first phrase alone 100 times)",
                    grading = "Sahih components; the exact combined hundredfold form is a personal routine",
                ),
            ),
        ),

        // ------------------------------------------------------------- Morning du'as
        Dhikr(
            id = "hammi-wal-hazan",
            title = "Refuge from worry and grief",
            arabicTitle = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ",
            transliteration = "Allāhumma innī aʿūdhu bika minal-hammi wal-ḥazan, wal-ʿajzi wal-kasal, wal-bukhli wal-jubn, wa ḍalaʿid-dayni wa ghalabatir-rijāl",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ",
            translation = "O Allah, I seek refuge in You from worry and grief, from incapacity and laziness, from miserliness and cowardice, from the burden of debt and from being overpowered by men.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 6369", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "ya-hayyu-ya-qayyum",
            title = "Ya Hayyu Ya Qayyum",
            arabicTitle = "يَا حَيُّ يَا قَيُّومُ",
            transliteration = "Yā ḥayyu yā qayyūm, biraḥmatika astaghīth, aṣliḥ lī shaʾnī kullah, wa lā takilnī ilā nafsī ṭarfata ʿayn",
            arabic = "يَا حَيُّ يَا قَيُّومُ، بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَىٰ نَفْسِي طَرْفَةَ عَيْنٍ",
            translation = "O Ever-Living, O Sustainer of all, by Your mercy I seek help. Set right all my affairs, and do not leave me to myself for the blink of an eye.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Al-Nasa’i, al-Sunan al-Kubra 10405 (morning and evening)"),
            ),
        ),
        Dhikr(
            id = "ilman-nafian",
            title = "Beneficial knowledge and pure provision",
            arabicTitle = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا",
            transliteration = "Allāhumma innī asʾaluka ʿilman nāfiʿā, wa rizqan ṭayyibā, wa ʿamalan mutaqabbalā",
            arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلًا مُتَقَبَّلًا",
            translation = "O Allah, I ask You for beneficial knowledge, wholesome provision, and deeds that are accepted.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Ibn Majah 925",
                    grading = "Sahih per al-Albani; hadith ties it to after the Subh (Fajr) prayer",
                ),
            ),
        ),

        // ------------------------------------------------------------------- Qur'an
        Dhikr(
            id = "quran-ayatul-kursi",
            title = "Ayat al-Kursi (Al-Baqarah 2:255)",
            arabicTitle = "آيَةُ الْكُرْسِيِّ",
            transliteration = "Allāhu lā ilāha illā huwal-ḥayyul-qayyūm. Lā taʾkhudhuhū sinatun wa lā nawm. Lahū mā fis-samāwāti wa mā fil-arḍ. Man dhal-ladhī yashfaʿu ʿindahū illā bi-idhnih. Yaʿlamu mā bayna aydīhim wa mā khalfahum, wa lā yuḥīṭūna bishayʾin min ʿilmihī illā bimā shāʾ. Wasiʿa kursiyyuhus-samāwāti wal-arḍ, wa lā yaʾūduhū ḥifẓuhumā, wa huwal-ʿaliyyul-ʿaẓīm.",
            arabic = QuranTexts.AYATUL_KURSI,
            translation = "Allah, there is no god but Him, the Ever-Living, the Sustainer of all. Neither drowsiness nor sleep overtakes Him. To Him belongs everything in the heavens and on earth. Who can intercede with Him except by His permission? He knows what lies before them and what lies behind them, and they grasp nothing of His knowledge except what He wills. His Kursi extends over the heavens and the earth. Preserving them does not tire Him. He is the Most High, the Magnificent.",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Baqarah 2:255"),
                SourceRef(SourceType.HADITH, "Al-Nasa’i, ‘Amal al-Yawm wal-Laylah 100 (after every obligatory prayer); Sahih al-Bukhari 2311 (before sleep)", grading = "Sahih per al-Albani"),
            ),
        ),
        Dhikr(
            id = "quran-hashr-59-22-24",
            title = "Surah Al-Hashr, last verses (59:22–24)",
            arabicTitle = "سُورَةُ الْحَشْرِ",
            transliteration = "Huwallāhul-ladhī lā ilāha illā huw, ʿālimul-ghaybi wash-shahādah, huwar-raḥmānur-raḥīm. Huwallāhul-ladhī lā ilāha illā huwal-malikul-quddūsus-salāmul-muʾminul-muhayminul-ʿazīzul-jabbārul-mutakabbir, subḥānallāhi ʿammā yushrikūn. Huwallāhul-khāliqul-bāriʾul-muṣawwir, lahul-asmāʾul-ḥusnā, yusabbiḥu lahū mā fis-samāwāti wal-arḍ, wa huwal-ʿazīzul-ḥakīm.",
            arabic = QuranTexts.HASHR_22_24,
            translation = "He is Allah, there is no god but Him, the Knower of the unseen and the seen. He is the Most Compassionate, the Most Merciful.\n\nHe is Allah, there is no god but Him, the Sovereign, the Holy, the Source of Peace, the Giver of Security, the Guardian, the Almighty, the Compeller, the Supremely Great. Glory be to Allah above what they associate with Him.\n\nHe is Allah, the Creator, the Originator, the Fashioner. To Him belong the most beautiful names. Everything in the heavens and on earth glorifies Him. He is the Almighty, the All-Wise.",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Hashr 59:22–24"),
            ),
            notes = "The recitation is Qur'an; no verified hadith ties these verses to after Fajr/Asr, so that timing is the user's routine.",
        ),
        Dhikr(
            id = "quran-baqarah-285-286",
            title = "Surah Al-Baqarah, last verses (2:285–286)",
            arabicTitle = "سُورَةُ الْبَقَرَةِ",
            transliteration = "Āmanar-rasūlu bimā unzila ilayhi mir-rabbihī wal-muʾminūn. Kullun āmana billāhi wa malāʾikatihī wa kutubihī wa rusulih. Lā nufarriqu bayna aḥadim mir-rusulih. Wa qālū samiʿnā wa aṭaʿnā, ghufrānaka rabbanā wa ilaykal-maṣīr. Lā yukallifullāhu nafsan illā wusʿahā. Lahā mā kasabat wa ʿalayhā maktasabat. Rabbanā lā tuʾākhidhnā in nasīnā aw akhṭaʾnā. Rabbanā wa lā taḥmil ʿalaynā iṣran kamā ḥamaltahū ʿalal-ladhīna min qablinā. Rabbanā wa lā tuḥammilnā mā lā ṭāqata lanā bih. Waʿfu ʿannā, waghfir lanā, warḥamnā. Anta mawlānā fanṣurnā ʿalal-qawmil-kāfirīn.",
            arabic = QuranTexts.BAQARAH_285_286,
            translation = "The Messenger believes in what was sent down to him from his Lord, and so do the believers. Each believes in Allah, His angels, His books, and His messengers. They say, “We make no distinction between any of His messengers.” And they say, “We hear and obey. Grant us Your forgiveness, our Lord. To You is the return.”\n\nAllah does not burden any soul beyond its capacity. It receives the good it earns and bears the evil it earns. Our Lord, do not hold us to account if we forget or make a mistake. Our Lord, do not lay on us a burden like the one You laid on those before us. Our Lord, do not burden us with what we have no strength to bear. Pardon us, forgive us, and have mercy on us. You are our Protector, so grant us victory over the disbelieving people.",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Baqarah 2:285–286"),
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 5009; Sahih Muslim 808", grading = "Sahih; reciting the last two verses of Al-Baqarah in a night suffices one"),
            ),
        ),
        Dhikr(
            id = "quran-kafirun",
            title = "Surah Al-Kafirun (109)",
            arabicTitle = "سُورَةُ الْكَافِرُونَ",
            transliteration = "Qul yā-ayyuhal-kāfirūn. Lā aʿbudu mā taʿbudūn. Wa lā antum ʿābidūna mā aʿbud. Wa lā ana ʿābidun mā ʿabattum. Wa lā antum ʿābidūna mā aʿbud. Lakum dīnukum wa liya dīn.",
            arabic = QuranTexts.AL_KAFIRUN,
            translation = "Say: O disbelievers, I do not worship what you worship, nor do you worship what I worship. I will not worship what you have worshipped, nor will you worship what I worship. For you is your religion, and for me is mine.",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Kafirun 109:1–6"),
                SourceRef(SourceType.HADITH, "Sunan Abi Dawud 5055 (recite Al-Kafirun before sleeping)", grading = "Sahih per al-Albani"),
            ),
        ),
        Dhikr(
            id = "quran-ikhlas",
            title = "Surah Al-Ikhlas (112)",
            arabicTitle = "سُورَةُ الْإِخْلَاصِ",
            transliteration = "Qul huwallāhu aḥad. Allāhus-ṣamad. Lam yalid wa lam yūlad. Wa lam yakul lahū kufuwan aḥad.",
            arabic = QuranTexts.AL_IKHLAS,
            translation = "Say: He is Allah, the One. Allah is the One on whom all depend. He neither begets nor was He begotten, and no one is equal to Him.",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Ikhlas 112:1–4"),
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 5017 (the three Quls before sleep)", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "quran-falaq",
            title = "Surah Al-Falaq (113)",
            arabicTitle = "سُورَةُ الْفَلَقِ",
            transliteration = "Qul aʿūdhu birabbil-falaq. Min sharri mā khalaq. Wa min sharri ghāsiqin idhā waqab. Wa min sharrin-naffāthāti fil-ʿuqad. Wa min sharri ḥāsidin idhā ḥasad.",
            arabic = QuranTexts.AL_FALAQ,
            translation = "Say: I seek refuge in the Lord of daybreak, from the evil of what He created, from the evil of darkness when it settles, from the evil of those who blow on knots, and from the evil of an envier when they envy.",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Falaq 113:1–5"),
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 5017 (the three Quls before sleep)", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "quran-nas",
            title = "Surah An-Nas (114)",
            arabicTitle = "سُورَةُ النَّاسِ",
            transliteration = "Qul aʿūdhu birabbin-nās. Malikin-nās. Ilāhin-nās. Min sharril-waswāsil-khannās. Alladhī yuwaswisu fī ṣudūrin-nās. Minal-jinnati wan-nās.",
            arabic = QuranTexts.AN_NAS,
            translation = "Say: I seek refuge in the Lord of mankind, the King of mankind, the God of mankind, from the evil of the retreating whisperer who whispers into people’s hearts, from among jinn and mankind.",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "An-Nas 114:1–6"),
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 5017 (the three Quls before sleep)", grading = "Sahih"),
            ),
        ),
    ).map { it.withBangla() }

    private val byId: Map<String, Dhikr> = items.associateBy { it.id }

    val all: List<Dhikr> get() = items

    fun byId(id: String): Dhikr? = byId[id]
}

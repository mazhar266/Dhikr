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
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 843; Sahih Muslim 595 (post-prayer tasbih)"),
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
            title = "Takbir and Tahlil after the Salam",
            arabicTitle = "التَّكْبِيرُ وَالتَّهْلِيلُ",
            transliteration = "Allāhu akbar, Allāhu akbar, lā ilāha illallāhu waḥdahu lā sharīka lah, lahul-mulku wa lahul-ḥamdu wa huwa ʿalā kulli shayʾin qadīr",
            arabic = "اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            translation = "Allah is the Greatest, Allah is the Greatest. There is no god but Allah alone, without partner. To Him belong the dominion and all praise, and He is over all things competent.",
            category = DhikrCategory.CUSTOM,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.NEEDS_VERIFICATION,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Tahlil wording: Sahih Muslim 596–597 (completion of the post-prayer hundred)",
                    grading = "Tahlil verified; the doubled opening takbir before it has no single verified hadith",
                ),
            ),
            notes = "The user's supplied wording was truncated ('Allahu Akbar, Allahu Akbar, La ilaha illallah…'). The completed text above joins the takbir formula to the well-attested tahlil of Muslim 596–597. No single hadith with this exact composite was located; see DHIKR_CONTENT_REVIEW.md for the candidates.",
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
                SourceRef(SourceType.HADITH, "Sahih Muslim 591", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "ajirni-minan-nar",
            title = "Allahumma Ajirni Minan-Nar",
            arabicTitle = "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ",
            transliteration = "Allāhumma ajirnī minan-nār",
            arabic = "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ",
            translation = "O Allah, protect me from the Fire.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 5079",
                    grading = "Hasan sahih per al-Albani; some scholars weakened one isnad",
                ),
            ),
            notes = "Hadith specifies seven times after Maghrib and Fajr before speaking to anyone.",
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
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 3370; Sahih Muslim 406", grading = "Sahih"),
            ),
            notes = "The wording is the verified Ibrahimiyyah formula; a tenfold repetition after prayer is the user's personal routine.",
        ),
        Dhikr(
            id = "raditu-billahi",
            title = "Raditu Billahi Rabban",
            arabicTitle = "رَضِيتُ بِاللَّهِ رَبًّا",
            transliteration = "Raḍītu billāhi rabbā, wa bil-islāmi dīnā, wa bi-Muḥammadin ṣallallāhu ʿalayhi wa sallama nabiyyā",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            translation = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad ﷺ as my Prophet.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 1529",
                    grading = "Sahih per al-Albani; hadith ties it to hearing the adhan",
                ),
            ),
            notes = "The user recites it after the prayers; the verified timing in the hadith is after the adhan.",
        ),
        Dhikr(
            id = "hasbiyallahu",
            title = "Hasbiyallahu La Ilaha Illa Huwa",
            arabicTitle = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ",
            transliteration = "Ḥasbiyallāhu lā ilāha illā huwa, ʿalayhi tawakkaltu, wa huwa rabbul-ʿarshil-ʿaẓīm",
            arabic = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ، عَلَيْهِ تَوَكَّلْتُ، وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
            translation = "Allah is sufficient for me; there is no god but Him. In Him I trust, and He is the Lord of the Mighty Throne.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 5081",
                    grading = "Hasan sahih per al-Albani; hadith ties it to seven times morning and evening",
                ),
            ),
            notes = "The user recites it after Fajr/Asr; the verified timing in the hadith is morning and evening.",
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
            arabicTitle = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ",
            transliteration = "Aʿūdhu bikalimātillāhit-tāmmāti min kulli shayṭānin wa hāmmah, wa min kulli ʿaynin lāmmah",
            arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ كُلِّ شَيْطَانٍ وَهَامَّةٍ، وَمِنْ كُلِّ عَيْنٍ لَامَّةٍ",
            translation = "I seek refuge in the perfect words of Allah from every devil and poisonous creature, and from every envious, evil eye.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih Muslim 2709", grading = "Sahih"),
            ),
        ),
        Dhikr(
            id = "audhu-kalimat-ghadab",
            title = "A'udhu bi Kalimatillah (anger, punishment, devils)",
            arabicTitle = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ",
            transliteration = "Aʿūdhu bikalimātillāhit-tāmmāti min ghaḍabihi wa ʿiqābihi, wa sharri ʿibādihi, wa min hamazātish-shayāṭīni wa an yaḥḍurūn",
            arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ غَضَبِهِ وَعِقَابِهِ، وَشَرِّ عِبَادِهِ، وَمِنْ هَمَزَاتِ الشَّيَاطِينِ وَأَنْ يَحْضُرُونِ",
            translation = "I seek refuge in the perfect words of Allah from His anger and His punishment, from the evil of His slaves, and from the whisperings of devils and their presence.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 5082",
                    grading = "Sahih per al-Albani; hadith ties it to morning and evening",
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
        Dhikr(
            id = "istighfar-100",
            title = "Istighfar — one hundred times",
            arabicTitle = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullāha wa atūbu ilayh",
            arabic = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            translation = "I seek Allah's forgiveness and turn to Him in repentance.",
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.HADITH, "Sahih Muslim 2702; Sunan Abi Dawud 1516", grading = "Sahih; the Prophet ﷺ sought forgiveness about one hundred times a day"),
            ),
        ),

        // ------------------------------------------------------ Health & wellbeing
        Dhikr(
            id = "afini-badani",
            title = "Allahumma 'Afini (wellbeing and protection)",
            arabicTitle = "اللَّهُمَّ عَافِنِي فِي بَدَنِي",
            transliteration = "Allāhumma ʿāfinī fī badanī, Allāhumma ʿāfinī fī samʿī, Allāhumma ʿāfinī fī baṣarī, lā ilāha illā anta. Allāhumma innī aʿūdhu bika minal-kufr wal-faqr, wa aʿūdhu bika min ʿadhābil-qabr, lā ilāha illā anta",
            arabic = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَٰهَ إِلَّا أَنْتَ. اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، وَأَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، لَا إِلَٰهَ إِلَّا أَنْتَ",
            translation = "O Allah, grant my body health; O Allah, grant my hearing health; O Allah, grant my sight health. There is no god but You. O Allah, I seek refuge in You from disbelief and poverty, and I seek refuge in You from the torment of the grave. There is no god but You.",
            category = DhikrCategory.DU_A,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sunan Abi Dawud 5090 ('Afini fi badani…', three times morning and evening)",
                    grading = "Sahih per al-Albani",
                ),
                SourceRef(
                    SourceType.HADITH,
                    "Sunan an-Nasa'i 5465 (refuge from disbelief, poverty, and the punishment of the grave)",
                    grading = "Sahih per al-Albani",
                ),
            ),
            notes = "The user combined two verified supplications into one threefold entry; each component is separately attested.",
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
                    "Sahih Muslim 2691 (ten times and one hundred times); Sahih al-Bukhari 6403 (one hundred times)",
                    grading = "Sahih; counts are tied to morning and evening in the hadith",
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
            category = DhikrCategory.SUNNAH,
            kind = DhikrKind.REPEATED,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(
                    SourceType.HADITH,
                    "Sahih al-Bukhari 6405 (the two phrases); Sahih Muslim 2691 (hundredfold tasbih)",
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
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 6369", grading = "Sahih; morning and evening"),
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
                SourceRef(SourceType.HADITH, "Mustadrak al-Hakim 1/499", grading = "Sahih per al-Albani; morning and evening"),
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
            transliteration = "Allāhu lā ilāha illā huwal-ḥayyul-qayyūm…",
            arabic = QuranTexts.AYATUL_KURSI,
            translation = "Allah — there is no deity except Him, the Ever-Living, the Sustainer of existence. (2:255)",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "Al-Baqarah 2:255"),
                SourceRef(SourceType.HADITH, "Sunan an-Nasa'i 100 (after every obligatory prayer); Sahih al-Bukhari 2311 (before sleep)", grading = "Sahih per al-Albani"),
            ),
        ),
        Dhikr(
            id = "quran-hashr-59-22-24",
            title = "Surah Al-Hashr, last verses (59:22–24)",
            arabicTitle = "سُورَةُ الْحَشْرِ",
            transliteration = "Huwallāhul-ladhī lā ilāha illā huwa…",
            arabic = QuranTexts.HASHR_22_24,
            translation = "He is Allah, other than whom there is no deity, Knower of the unseen and the witnessed… (59:22–24)",
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
            transliteration = "Āmanar-rasūlu bimā unzila ilayhi mir-rabbihī…",
            arabic = QuranTexts.BAQARAH_285_286,
            translation = "The Messenger has believed in what was revealed to him from his Lord… (2:285–286)",
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
            transliteration = "Qul yā-ayyuhal-kāfirūn…",
            arabic = QuranTexts.AL_KAFIRUN,
            translation = "Say: O disbelievers, I do not worship what you worship… (109:1–6)",
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
            transliteration = "Qul huwallāhu aḥad…",
            arabic = QuranTexts.AL_IKHLAS,
            translation = "Say: He is Allah, the One… (112:1–4)",
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
            transliteration = "Qul aʿūdhu birabbil-falaq…",
            arabic = QuranTexts.AL_FALAQ,
            translation = "Say: I seek refuge in the Lord of the daybreak… (113:1–5)",
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
            transliteration = "Qul aʿūdhu birabbin-nās…",
            arabic = QuranTexts.AN_NAS,
            translation = "Say: I seek refuge in the Lord of mankind… (114:1–6)",
            category = DhikrCategory.QURAN,
            kind = DhikrKind.RECITE_ONCE,
            verification = VerificationStatus.VERIFIED,
            sourceRefs = listOf(
                SourceRef(SourceType.QURAN, "An-Nas 114:1–6"),
                SourceRef(SourceType.HADITH, "Sahih al-Bukhari 5017 (the three Quls before sleep)", grading = "Sahih"),
            ),
        ),
    )

    private val byId: Map<String, Dhikr> = items.associateBy { it.id }

    val all: List<Dhikr> get() = items

    fun byId(id: String): Dhikr? = byId[id]
}

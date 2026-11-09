# Dhikr Content Review

Status legend:

- **Verified (wording)** — the Arabic wording follows a cited, well-established source (Qur'an dataset or a canonical hadith collection).
- **Verified (count)** — the cited hadith itself establishes the repetition count.
- **User-configured** — the placement (which prayer / when) or count comes from the user's personal routine, not from an established hadith. Preserved exactly as requested; not presented as Sunnah.
- **Needs verification** — incomplete or ambiguous source; flagged in the app UI; **do not ship as authenticated content without scholarly review.**

Qur'anic passages were **not** written from memory: they are generated verbatim from the [quran.com API v4](https://api.quran.com) `text_uthmani` field (Tanzil-verified Uthmani script) by `tools/gen_quran_kt.py`, which also emits integrity fingerprints asserted in `ContentTest`.

Supplication wordings follow the cited collections; hadith numbers use standard sunnah.com numbering. Where scholars differ on grading, the uncertainty is stated rather than resolved silently.

---

## Qur'anic entries

| Catalog id | Passage | Text source | Placement in app |
|---|---|---|---|
| `quran-ayatul-kursi` | Al-Baqarah 2:255 | quran.com uthmani (verified dataset) | After fard prayers (Al-Nasa’i, ‘Amal al-Yawm wal-Laylah 100, sahih per al-Albani) and before sleep (Bukhari 2311) — both placements verified |
| `quran-hashr-59-22-24` | Al-Hashr 59:22–24 | quran.com uthmani (verified dataset) | **User-configured** — no verified hadith ties these verses to after Fajr/Asr |
| `quran-baqarah-285-286` | Al-Baqarah 2:285–286 | quran.com uthmani (verified dataset) | Before sleep verified: "whoever recites the last two verses of Al-Baqarah in a night, that suffices him" (Bukhari 5009; Muslim 808) |
| `quran-kafirun` | Al-Kafirun 109:1–6 | quran.com uthmani (verified dataset) | Before sleep: Abu Dawud 5055, sahih per al-Albani (some scholars differ) |
| `quran-ikhlas`, `quran-falaq`, `quran-nas` | The three Quls | quran.com uthmani (verified dataset) | Before sleep, three times each with blowing/wiping: Bukhari 5017 — verified |

Verse numbering and diacritics are exactly as returned by the dataset; any accidental edit is caught by the `ContentTest` fingerprint assertions.

## Supplications & tasbih entries

| Catalog id | Wording status | Wording source | Count status | Timing status |
|---|---|---|---|---|
| `allahu-akbar` | Verified | Standard formula | 1× (opener) user-configured; 33×/34× verified | After prayer: verified (Bukhari 843; Muslim 595–596) |
| `astaghfirullah` | Verified | Muslim 591 | 3× after salam and 100× in the personal routine | The 100× entry uses the same Astaghfirullah wording |
| `takbir-tahlil-after-salam` | Attested wording | Musannaf Ibn Abi Shaybah, Book of the Eid Prayers, chapter on takbir on the Day of Arafah | 1× personal | Daily Fajr/Asr placement is personal |
| `antas-salam` | Verified | Muslim 592a | Once | After salam |
| `ajirni-minan-nar` | Reported wording | Abu Dawud 5079, graded da’if by al-Albani | 7× personal | The weak report mentions Fajr and Maghrib. The app uses Fajr and Asr |
| `subhanallah` | Verified | Bukhari 843; Muslim 595 | 33× verified | After prayer: verified |
| `alhamdulillah` | Verified | Bukhari 843; Muslim 595 | 33× verified | After prayer: verified |
| `salawat-ibrahimiyyah` | Verified (Ibrahimiyyah formula) | Bukhari 3370 | 10× user-configured | After prayer: user-configured (tashahhud placement is the verified one) |
| `raditu-billahi` | Attested wording | Tirmidhi 3389 (hasan gharib per at-Tirmidhi) | 3× personal | The cited narration gives the evening wording |
| `hasbiyallahu` | Verified Qur’anic wording | Closing words of Al-Tawbah 9:129 | 7× personal | Abu Dawud 5081 attributes the morning/evening count to Abu al-Darda’. Al-Albani graded that report mawdu’ |
| `audhu-kalimat-min-sharri` | Verified | Muslim 2708, 2710 | 3× user-configured | Verified contexts: stopping at a place (Muslim 2708), evening (Muslim 2710); after-prayer use user-configured |
| `audhu-kalimat-shaytan` | Verified | Bukhari 3371 | 1× personal | The narration describes seeking protection for Al-Hasan and Al-Husain |
| `audhu-kalimat-ghadab` | Reported wording | Tirmidhi 3528, hasan gharib per at-Tirmidhi and da’if per Darussalam | 1× personal | The narration gives this wording for alarm during sleep |
| `bismillah-la-yadurru` | Verified | Abu Dawud 5088; Tirmidhi 3601 (sahih) | 3× verified | Morning/evening: verified; after-prayer user-configured |
| `sayyidul-istighfar` | Verified | Bukhari 6306 | 1× verified | Morning/evening: verified; after-prayer user-configured |
| `afini-badani` | Both supplications attested | Abu Dawud 5090, hasan in chain per al-Albani | 3× each | The report describes morning and evening recitation. One app repetition covers both supplications |
| `la-ilaha-illallah-goal` | Verified | Muslim 2693 (10×); Muslim 2691 and Bukhari 6403 (100×) | Both counts are reported | Fajr/Asr placement and sharing are personal |
| `subhanallahi-wabihamdihi-100` | Verified paired wording | Bukhari 6406 for the pair and Bukhari 6405 for the first phrase alone 100× | 100× of the pair is personal | Fajr/Asr placement is personal |
| `hammi-wal-hazan` | Verified | Bukhari 6369 | 1× personal | Fajr/Asr placement is personal |
| `ya-hayyu-ya-qayyum` | Verified | Al-Nasa’i, al-Sunan al-Kubra 10405 | 1× verified | Morning/evening: verified; after-prayer user-configured |
| `ilman-nafian` | Verified | Ibn Majah 925 (sahih per al-Albani) | 1× verified | **After the Fajr (Subh) prayer**: verified |

## Takbir al-Tashriq

The wording supplied for Fajr/Asr item 3 matches the formula reported from Ibn Mas’ud
for the days of Tashriq. The app uses that formula in the personal daily routine.
[Musannaf Ibn Abi Shaybah](https://islamicurdubooks.com/hadith/book.php?bc_id=1435&page=8)

## Routine-level notes

- **Fajr (29 items) / Asr (28 items)**: Asr is the same inventory minus item 29
  (`ilman-nafian`, Fajr-only) — item 24 in Asr is the *conditional mirror* of the shared daily goal.
  All after-prayer placements follow the user's personal routine (documented above item by item).
- **Shared daily goal (`la-ilaha-illallah-goal`)**: off by default; when enabled in Settings,
  Fajr and Asr share one daily goal (10× or 100×). An Asr entry satisfied only via Fajr's recitation
  is displayed as complete. Asr can add up to ten repetitions toward an unfinished goal. Each routine can undo only its own contributions.
- **Dhuhr / Maghrib / Isha (5 items each)** and **Before Sleep (5 items)**: placements verified as
  noted in the tables above.
- **Bangla**: every catalog entry has a Bengali title, a Bengali-script pronunciation
  (উচ্চারণ), and a meaning that follows the Arabic already in the catalog. Latin pronunciation
  is the full transliteration (Qur'an passages are no longer ellipsized). The Settings language
  switch selects English or বাংলা for the app chrome, the meaning, and the pronunciation.
  Bangla text is a reading aid for the cited Arabic; it is not an independent isnad.

## Intentional simplifications (non-content)

- **Overcounting** is always prevented (the counter disables at target); the brief's optional
  "explicitly enabled" overcount mode is not offered.
- **Daily reset time** is fixed at local midnight (no configurable hour). The engine re-derives
  "today" from the device calendar on every operation, so timezone/DST changes behave correctly.
- **Reminders/notifications** are not implemented (settings-free minimal design); everything else in
  the Settings brief is functional.

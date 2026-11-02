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
| `quran-ayatul-kursi` | Al-Baqarah 2:255 | quran.com uthmani (verified dataset) | After fard prayers (an-Nasa'i 100, sahih per al-Albani) and before sleep (Bukhari 2311) — both placements verified |
| `quran-hashr-59-22-24` | Al-Hashr 59:22–24 | quran.com uthmani (verified dataset) | **User-configured** — no verified hadith ties these verses to after Fajr/Asr |
| `quran-baqarah-285-286` | Al-Baqarah 2:285–286 | quran.com uthmani (verified dataset) | Before sleep verified: "whoever recites the last two verses of Al-Baqarah in a night, that suffices him" (Bukhari 5009; Muslim 808) |
| `quran-kafirun` | Al-Kafirun 109:1–6 | quran.com uthmani (verified dataset) | Before sleep: Abu Dawud 5055, sahih per al-Albani (some scholars differ) |
| `quran-ikhlas`, `quran-falaq`, `quran-nas` | The three Quls | quran.com uthmani (verified dataset) | Before sleep, three times each with blowing/wiping: Bukhari 5017 — verified |

Verse numbering and diacritics are exactly as returned by the dataset; any accidental edit is caught by the `ContentTest` fingerprint assertions.

## Supplications & tasbih entries

| Catalog id | Wording status | Wording source | Count status | Timing status |
|---|---|---|---|---|
| `allahu-akbar` | Verified | Standard formula | 1× (opener) user-configured; 33×/34× verified | After prayer: verified (Bukhari 843; Muslim 595–596) |
| `astaghfirullah` | Verified | Muslim 591 | 3× verified | After salam: verified (Muslim 591) |
| `takbir-tahlil-after-salam` | **NEEDS VERIFICATION** | see below | 1× user-configured | After salam: user-configured |
| `antas-salam` | Verified | Muslim 591 | once, verified | After salam: verified |
| `ajirni-minan-nar` | Verified | Abu Dawud 5079 | 7× verified | Verified for after Maghrib & Fajr, *before speaking to anyone* — the app does not enforce that condition; graded hasan sahih by al-Albani, some scholars weakened one isnad (uncertainty documented) |
| `subhanallah` | Verified | Bukhari 843; Muslim 595 | 33× verified | After prayer: verified |
| `alhamdulillah` | Verified | Bukhari 843; Muslim 595 | 33× verified | After prayer: verified |
| `salawat-ibrahimiyyah` | Verified (Ibrahimiyyah formula) | Bukhari 3370; Muslim 406 | 10× user-configured | After prayer: user-configured (tashahhud placement is the verified one) |
| `raditu-billahi` | Verified | Abu Dawud 1529 (sahih per al-Albani) | 3× user-configured | Hadith ties it to **hearing the adhan**; after-prayer use is user-configured |
| `hasbiyallahu` | Verified | Abu Dawud 5081 (hasan sahih per al-Albani) | 7× verified | Hadith ties it to **morning and evening**; after-Fajr/Asr use is user-configured |
| `audhu-kalimat-min-sharri` | Verified | Muslim 2708, 2710 | 3× user-configured | Verified contexts: stopping at a place (Muslim 2708), evening (Muslim 2710); after-prayer use user-configured |
| `audhu-kalimat-shaytan` | Verified | Muslim 2709 | 1× user-configured | Morning/evening: verified; after-prayer user-configured |
| `audhu-kalimat-ghadab` | Verified | Abu Dawud 5082 (sahih per al-Albani) | 1× user-configured | Morning/evening: verified; after-prayer user-configured |
| `bismillah-la-yadurru` | Verified | Abu Dawud 5088; Tirmidhi 3601 (sahih) | 3× verified | Morning/evening: verified; after-prayer user-configured |
| `sayyidul-istighfar` | Verified | Bukhari 6306 | 1× verified | Morning/evening: verified; after-prayer user-configured |
| `afini-badani` | Verified (two components) | Abu Dawud 5090 ('Afini fi badani/sam'i/basari) + an-Nasa'i 5465 (refuge from kufr, poverty, punishment of grave) | 3× verified for component 1; 3× user-configured for the combination | Morning/evening: verified |
| `la-ilaha-illallah-goal` | Verified | Bukhari 6403; Muslim 2691 | 10× and 100× both verified (Muslim 2691) | Morning/evening: verified; after-prayer placement and the Fajr↔Asr shared goal are user-configured |
| `subhanallahi-wabihamdihi-100` | Verified components | Bukhari 6405 (the two phrases); Muslim 2691 (hundredfold tasbih) | 100×: components verified, exact combined form user-configured | Morning/evening: verified |
| `istighfar-100` | Verified | Muslim 2702; Abu Dawud 1516 | ~100× verified | The Prophet ﷺ sought forgiveness ~100×/day |
| `hammi-wal-hazan` | Verified | Bukhari 6369 | 1× verified | Morning/evening: verified; after-prayer user-configured |
| `ya-hayyu-ya-qayyum` | Verified | al-Hakim 1/499 (sahih per al-Albani) | 1× verified | Morning/evening: verified; after-prayer user-configured |
| `ilman-nafian` | Verified | Ibn Majah 925 (sahih per al-Albani) | 1× verified | **After the Fajr (Subh) prayer**: verified |

## Needs verification — unresolved items

### 1. `takbir-tahlil-after-salam` (Fajr/Asr item 3)

The brief supplied: *"1× Allahu Akbar, Allahu Akbar, La ilaha illallah…"* (truncated). No single
verified hadith was located for a composite beginning with a **doubled takbir** followed by the
tahlil. Candidates identified:

- **Sahih Muslim 596–597** — completing the post-prayer hundred with *"La ilaha illallahu wahdahu la
  sharika lah…"* (no opening takbirs). The app's completion of the truncated text follows this wording.
- **Tirmidhi 3474 / Abu Dawud 1512** — *"Allahu Akbar kabiran, wal-hamdu lillahi kathiran, wa
  subhanallahi bukratan wa asila"* 10× after Fajr/Maghrib (different wording).

**Action required:** a human reviewer should confirm the intended wording. Until then the entry is
stored `needs_verification`, carries a visible "needs scholarly verification" badge in the reader,
and must not be presented as authenticated Sunnah.

## Routine-level notes

- **Fajr (29 items) / Asr (28 items)**: Asr is the same inventory minus item 29
  (`ilman-nafian`, Fajr-only) — item 24 in Asr is the *conditional mirror* of the shared daily goal.
  All after-prayer placements follow the user's personal routine (documented above item by item).
- **Shared daily goal (`la-ilaha-illallah-goal`)**: off by default; when enabled in Settings,
  Fajr and Asr share one daily goal (10× or 100×). An Asr entry satisfied only via Fajr's recitation
  is displayed as *satisfied via the shared goal* and its count cannot be undone from Asr.
- **Dhuhr / Maghrib / Isha (5 items each)** and **Before Sleep (5 items)**: placements verified as
  noted in the tables above.
- **Bangla**: every catalog entry has a Bengali title, a Bengali-script pronunciation
  (উচ্চারণ), and a meaning that follows the Arabic already in the catalog. Latin pronunciation
  is the full transliteration (Qur'an passages are no longer ellipsized). The Settings language
  switch selects English or বাংলা for the app chrome, the meaning, and the pronunciation.
  Bangla text is a reading aid for the cited Arabic; it is not an independent isnad.

## Corrections requiring human scholarly review

1. Confirm the intended wording of `takbir-tahlil-after-salam` (see above).
2. Optionally review the gradings quoted from al-Albani for Abu Dawud 5079 (`ajirni-minan-nar`) and
   Abu Dawud 5055 (`quran-kafirun` placement), where scholars differ.
3. Bangla meanings and pronunciations follow the catalog Arabic. A reviewer who wants a
   published Bengali tafsir wording can still compare them; they are not presented as a
   separate authenticated source.

## Intentional simplifications (non-content)

- **Overcounting** is always prevented (the counter disables at target); the brief's optional
  "explicitly enabled" overcount mode is not offered.
- **Daily reset time** is fixed at local midnight (no configurable hour). The engine re-derives
  "today" from the device calendar on every operation, so timezone/DST changes behave correctly.
- **Reminders/notifications** are not implemented (settings-free minimal design); everything else in
  the Settings brief is functional.

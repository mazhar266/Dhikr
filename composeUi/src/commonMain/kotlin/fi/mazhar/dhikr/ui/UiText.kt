package fi.mazhar.dhikr.ui

import fi.mazhar.dhikr.TranslationLanguage

/** Every user-facing string. [TranslationLanguage] selects English or Bangla. */
interface UiText {
    val today: String
    val summary: String
    val settings: String
    val home: String
    val back: String
    val dailySummary: String
    val greeting: String
    val todaysAdhkar: String
    val complete: String
    val unknownRoutine: String
    val unfinishedOnly: String
    val recited: String
    val once: String
    val sharedGoal: String
    val done: String
    val entryNotFound: String
    val prev: String
    val next: String
    val changeTarget: String
    val undo: String
    val markAsRecited: String
    val tapToCount: String
    val repetitionTarget: String
    val decrease: String
    val increase: String
    val save: String
    val cancel: String
    val needsVerification: String
    val pronunciation: String
    val meaning: String
    val dayStreak: String
    val repetitionsToday: String
    val routines: String
    val unfinished: String
    val last14Days: String
    val filledBlock: String
    val meritNote: String
    val appearance: String
    val theme: String
    val system: String
    val light: String
    val sofiaPaper: String
    val dark: String
    val oledDark: String
    val reading: String
    val showPronunciation: String
    val showTranslation: String
    val language: String
    val english: String
    val bangla: String
    val hapticFeedback: String
    val autoAdvance: String
    val dailyGoal: String
    val shareGoal: String
    val target: String
    val dailyReset: String
    val resetExplain: String
    val resetToday: String
    val resetTitle: String
    val resetBody: String
    val reset: String
    val progressReset: String
    val backup: String
    val backupBody: String
    val export: String
    val import: String
    val backupSaved: String
    val backupRestored: String
    val backupInvalid: String
    val about: String
    val aboutBody: String
    val developer: String
    val developerBio: String
    val aboutFoot: String

    fun arabicFontSize(percent: Int): String
    fun homeProgress(completed: Int, total: Int, repetitions: Int, streak: Int): String
    fun continueRoutine(title: String): String
    fun entriesDone(completed: Int, total: Int): String
    fun entryCount(completed: Int, total: Int, percent: String): String
    fun entryPosition(position: Int): String
    fun remaining(title: String, count: Int): String
    fun goalShared(on: Boolean): String
    fun backupFailed(detail: String?): String
    fun restoreFailed(detail: String?): String
}

fun uiText(language: TranslationLanguage): UiText =
    if (language == TranslationLanguage.BENGALI) BanglaUi else EnglishUi

private object EnglishUi : UiText {
    override val today = "Today"
    override val summary = "Summary"
    override val settings = "Settings"
    override val home = "Home"
    override val back = "Back"
    override val dailySummary = "Daily Summary"
    override val greeting = "As-salamu alaykum"
    override val todaysAdhkar = "Today's adhkar"
    override val complete = "Complete"
    override val unknownRoutine = "Unknown routine"
    override val unfinishedOnly = "Unfinished only"
    override val recited = "Recited"
    override val once = "Once"
    override val sharedGoal = "shared goal"
    override val done = "Done"
    override val entryNotFound = "Entry not found"
    override val prev = " Prev"
    override val next = "Next "
    override val changeTarget = "Change target"
    override val undo = "Undo"
    override val markAsRecited = "Mark as Recited"
    override val tapToCount = "tap to count"
    override val repetitionTarget = "Repetition target"
    override val decrease = "Decrease"
    override val increase = "Increase"
    override val save = "Save"
    override val cancel = "Cancel"
    override val needsVerification = "Wording needs scholarly verification"
    override val pronunciation = "Pronunciation"
    override val meaning = "Meaning"
    override val dayStreak = "day streak"
    override val repetitionsToday = "repetitions today"
    override val routines = "Routines"
    override val unfinished = "Unfinished"
    override val last14Days = "Last 14 days"
    override val filledBlock = "A filled block is a day with at least one completed routine"
    override val meritNote = "Counts are a tracking aid, not a measure of religious merit."
    override val appearance = "Appearance"
    override val theme = "Theme"
    override val system = "System"
    override val light = "Light"
    override val sofiaPaper = "Sofia Paper"
    override val dark = "Dark"
    override val oledDark = "OLED Dark"
    override val reading = "Reading"
    override val showPronunciation = "Show pronunciation"
    override val showTranslation = "Show translation"
    override val language = "Language"
    override val english = "English"
    override val bangla = "বাংলা"
    override val hapticFeedback = "Haptic feedback"
    override val autoAdvance = "Auto-advance after completing an entry"
    override val dailyGoal = "La ilaha illallah daily goal"
    override val shareGoal = "Share between Fajr and Asr"
    override val target = "Target"
    override val dailyReset = "Daily reset"
    override val resetExplain = "Progress resets automatically at local midnight. Previous days are kept as history."
    override val resetToday = "Reset today's progress"
    override val resetTitle = "Reset today's progress?"
    override val resetBody = "Today's counts will be cleared. Other days are kept."
    override val reset = "Reset"
    override val progressReset = "Today's progress reset"
    override val backup = "Backup & restore"
    override val backupBody = "Backups are plain files you choose where to keep. Restoring replaces current progress and settings."
    override val export = "Export…"
    override val import = "Import…"
    override val backupSaved = "Backup saved"
    override val backupRestored = "Backup restored"
    override val backupInvalid = "Not a valid Dhikr backup"
    override val about = "About"
    override val aboutBody = "A quiet companion for the daily adhkar."
    override val developer = "Developer"
    override val developerBio = "Mazhar Ahmed\nBA in Islamic Studies at IOU\nDawra-e-Hadith from Qawmi Madrasa, Bangladesh"
    override val aboutFoot = "Offline only · No account needed\nQur'anic text: Tanzil-verified uthmani script via quran.com"

    override fun arabicFontSize(percent: Int) = "Arabic font size — $percent%"
    override fun homeProgress(completed: Int, total: Int, repetitions: Int, streak: Int): String {
        val base = "$completed of $total entries · $repetitions repetitions"
        return if (streak > 0) "$base · $streak-day streak" else base
    }
    override fun continueRoutine(title: String) = "Continue $title"
    override fun entriesDone(completed: Int, total: Int) = "$completed of $total entries done"
    override fun entryCount(completed: Int, total: Int, percent: String) = "$completed / $total entries · $percent"
    override fun entryPosition(position: Int) = "Entry $position"
    override fun remaining(title: String, count: Int) = "$title — $count remaining"
    override fun goalShared(on: Boolean) = if (on) {
        "One shared daily goal: completing it in either prayer satisfies both entries."
    } else {
        "Independent: Fajr and Asr count their own repetitions."
    }
    override fun backupFailed(detail: String?) = "Backup failed: $detail"
    override fun restoreFailed(detail: String?) = "Restore failed: $detail"
}

private object BanglaUi : UiText {
    override val today = "আজ"
    override val summary = "সারাংশ"
    override val settings = "সেটিংস"
    override val home = "হোম"
    override val back = "পেছনে"
    override val dailySummary = "দিনের সারাংশ"
    override val greeting = "আসসালামু আলাইকুম"
    override val todaysAdhkar = "আজকের আযকার"
    override val complete = "সম্পন্ন"
    override val unknownRoutine = "রুটিন পাওয়া যায়নি"
    override val unfinishedOnly = "শুধু বাকি"
    override val recited = "পড়েছি"
    override val once = "একবার"
    override val sharedGoal = "একত্র লক্ষ্য"
    override val done = "শেষ"
    override val entryNotFound = "এন্ট্রি পাওয়া যায়নি"
    override val prev = " আগে"
    override val next = "পরে "
    override val changeTarget = "লক্ষ্য বদলান"
    override val undo = "আগেরটা বাতিল"
    override val markAsRecited = "পড়া হয়েছে"
    override val tapToCount = "গুণতে চাপুন"
    override val repetitionTarget = "পুনরাবৃত্তির লক্ষ্য"
    override val decrease = "কমান"
    override val increase = "বাড়ান"
    override val save = "সংরক্ষণ"
    override val cancel = "বাতিল"
    override val needsVerification = "শব্দচয়ন আলেমদের যাচাইয়ের অপেক্ষায়"
    override val pronunciation = "উচ্চারণ"
    override val meaning = "অর্থ"
    override val dayStreak = "দিনের ধারা"
    override val repetitionsToday = "আজকের পুনরাবৃত্তি"
    override val routines = "রুটিন"
    override val unfinished = "বাকি"
    override val last14Days = "গত ১৪ দিন"
    override val filledBlock = "রঙিন ঘর মানে সেদিন অন্তত একটি রুটিন শেষ হয়েছে"
    override val meritNote = "গণনা হিসাব রাখার সহায়ক, আমলের মানদণ্ড নয়।"
    override val appearance = "চেহারা"
    override val theme = "থিম"
    override val system = "সিস্টেম"
    override val light = "আলো"
    override val sofiaPaper = "সফিয়া পেপার"
    override val dark = "অন্ধকার"
    override val oledDark = "ওএলইডি অন্ধকার"
    override val reading = "পঠন"
    override val showPronunciation = "উচ্চারণ দেখান"
    override val showTranslation = "অর্থ দেখান"
    override val language = "ভাষা"
    override val english = "English"
    override val bangla = "বাংলা"
    override val hapticFeedback = "কম্পন"
    override val autoAdvance = "একটি শেষ হলে পরেরটিতে যান"
    override val dailyGoal = "লা ইলাহা ইল্লাল্লাহ — দৈনিক লক্ষ্য"
    override val shareGoal = "ফজর ও আসরের মধ্যে ভাগ করুন"
    override val target = "লক্ষ্য"
    override val dailyReset = "দৈনিক রিসেট"
    override val resetExplain = "স্থানীয় মধ্যরাতে অগ্রগতি নিজে থেকে মুছে যায়। আগের দিনগুলো ইতিহাস হিসেবে থাকে।"
    override val resetToday = "আজকের অগ্রগতি মুছুন"
    override val resetTitle = "আজকের অগ্রগতি মুছবেন?"
    override val resetBody = "আজকের গণনা মুছে যাবে। অন্য দিনগুলো থাকবে।"
    override val reset = "মুছুন"
    override val progressReset = "আজকের অগ্রগতি মুছে ফেলা হয়েছে"
    override val backup = "ব্যাকআপ ও পুনরুদ্ধার"
    override val backupBody = "ব্যাকআপ একটি সাধারণ ফাইল, যেখানে চান সেখানে রাখুন। পুনরুদ্ধার করলে বর্তমান অগ্রগতি ও সেটিংস বদলে যায়।"
    override val export = "রপ্তানি…"
    override val import = "আমদানি…"
    override val backupSaved = "ব্যাকআপ সংরক্ষিত"
    override val backupRestored = "ব্যাকআপ ফিরিয়ে আনা হয়েছে"
    override val backupInvalid = "এটি বৈধ Dhikr ব্যাকআপ নয়"
    override val about = "পরিচিতি"
    override val aboutBody = "দৈনিক আযকারের একটি শান্ত সঙ্গী।"
    override val developer = "ডেভেলপার"
    override val developerBio = "মাজহার আহমেদ\nআইওইউ-তে ইসলামিক স্টাডিজে স্নাতক\nবাংলাদেশের কওমি মাদরাসা থেকে দাওরায়ে হাদীস"
    override val aboutFoot = "শুধু অফলাইন · কোনো অ্যাকাউন্ট লাগে না\nকুরআনের পাঠ: কুরআন.কম-এর মাধ্যমে তানযীল-যাচাইকৃত উসমানী লিপি"

    override fun arabicFontSize(percent: Int) = "আরবি হরফের আকার — $percent%"
    override fun homeProgress(completed: Int, total: Int, repetitions: Int, streak: Int): String {
        val base = "$total টির মধ্যে $completed টি · $repetitions বার"
        return if (streak > 0) "$base · $streak দিনের ধারা" else base
    }
    override fun continueRoutine(title: String) = "$title চালিয়ে যান"
    override fun entriesDone(completed: Int, total: Int) = "$total টির মধ্যে $completed টি শেষ"
    override fun entryCount(completed: Int, total: Int, percent: String) = "$completed / $total টি · $percent"
    override fun entryPosition(position: Int) = "এন্ট্রি $position"
    override fun remaining(title: String, count: Int) = "$title — $count টি বাকি"
    override fun goalShared(on: Boolean) = if (on) {
        "একটি ভাগাভাগি লক্ষ্য: যেকোনো এক ওয়াক্তে শেষ করলে দুটি এন্ট্রিই পূর্ণ হয়।"
    } else {
        "আলাদা: ফজর ও আসর নিজ নিজ গণনা রাখে।"
    }
    override fun backupFailed(detail: String?) = "ব্যাকআপ ব্যর্থ: $detail"
    override fun restoreFailed(detail: String?) = "পুনরুদ্ধার ব্যর্থ: $detail"
}

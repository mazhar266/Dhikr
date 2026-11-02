# Generates QuranTexts.kt from quran.com API JSON files in /tmp/quran.
# Run: python tools/gen_quran_kt.py
import io
import json
import os

SRC = os.environ.get("QURAN_JSON_DIR", "/tmp/quran")
OUT = os.path.join(os.path.dirname(__file__), "..", "shared", "src", "commonMain",
                   "kotlin", "com", "hikmah", "dhikr", "content", "QuranTexts.kt")


def load(fn):
    with io.open(os.path.join(SRC, fn), encoding="utf-8") as f:
        return json.load(f)


single = {}
for key, fn in [("2:255", "v2_255.json"), ("2:285", "v2_285.json"), ("2:286", "v2_286.json"),
                ("59:22", "v59_22.json"), ("59:23", "v59_23.json"), ("59:24", "v59_24.json")]:
    if fn is None:
        continue
    single[key] = load(fn)["verse"]["text_uthmani"].strip()

chapters = {}
for c in (109, 112, 113, 114):
    verses = load("ch%d.json" % c)["verses"]
    chapters[c] = [(v["verse_key"], v["text_uthmani"].strip()) for v in verses]


def esc(s):
    return s.replace("\\", "\\\\").replace('"', '\\"').replace("\n", "\\n")


def fnv1a_signed(s):
    """FNV-1a 64-bit over UTF-8 bytes, returned as a signed 64-bit int."""
    h = 0xCBF29CE484222325
    for b in s.encode("utf-8"):
        h ^= b
        h = (h * 0x100000001B3) & 0xFFFFFFFFFFFFFFFF
    return h - 0x10000000000000000 if h >= 0x8000000000000000 else h


def join_range(keys):
    return " ".join(single[k] for k in keys)


entries = [
    ("AYATUL_KURSI", join_range(["2:255"]), "Al-Baqarah 2:255"),
    ("BAQARAH_285_286", join_range(["2:285", "2:286"]), "Al-Baqarah 2:285-286"),
    ("HASHR_22_24", join_range(["59:22", "59:23", "59:24"]), "Al-Hashr 59:22-24"),
]
for c in (109, 112, 113, 114):
    name = {109: "AL_KAFIRUN", 112: "AL_IKHLAS", 113: "AL_FALAQ", 114: "AN_NAS"}[c]
    ref = {109: "Al-Kafirun 109:1-6", 112: "Al-Ikhlas 112:1-4",
           113: "Al-Falaq 113:1-5", 114: "An-Nas 114:1-6"}[c]
    entries.append((name, " ".join(t for _, t in chapters[c]), ref))

out = io.StringIO()
w = out.write
w("// Generated from the Quran.com API v4 (uthmani script, Tanzil-verified text).\n")
w("// Source: https://api.quran.com/api/v4/... (text_uthmani field)\n")
w("// Do not hand-edit verse text; regenerate from the verified dataset instead.\n")
w("package com.hikmah.dhikr.content\n\n")
w("internal object QuranTexts {\n")
for const, text, ref in entries:
    w("    /** %s. */\n" % ref)
    w('    const val %s: String = "%s"\n\n' % (const, esc(text)))
w("}\n\n")
w("/** Integrity fingerprints (FNV-1a over UTF-8 bytes) guarding the verse text. */\n")
w("internal object QuranChecksums {\n".replace("internal ", ""))
for const, text, _ in entries:
    w("    const val %s: Long = %dL\n" % (const, fnv1a_signed(text)))
w("}\n")

os.makedirs(os.path.dirname(OUT), exist_ok=True)
with io.open(OUT, "w", encoding="utf-8", newline="\n") as f:
    f.write(out.getvalue())
print("written %s (%d chars)" % (OUT, len(out.getvalue())))
for const, text, ref in entries:
    print("%s: %d chars | %s | starts: %s" % (const, len(text), ref, text[:36]))

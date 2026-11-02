"""Generate the Android launcher icons and desktop icons from the master artwork.

Usage: python tools/gen_icons.py [path/to/icon.png]   (default: icon.png at the repo root)

The master is a rounded-square app icon with transparent corners. Outputs:
  * Android legacy icons (API 24-25): the cleaned rounded square, 48dp.
  * Android adaptive icon (API 26+): the artwork with its rim trimmed and its
    edge feathered into a solid background color, scaled so the arch, beads and
    tassel sit inside the 66dp safe zone of the 108dp layer.
  * Desktop: a 256px window icon and a multi-size .ico for Dhikr.exe.
Requires Pillow.
"""

import os
import statistics
import sys

from PIL import Image, ImageChops, ImageDraw, ImageFilter

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
RES = os.path.join(ROOT, "androidApp", "src", "main", "res")
DESKTOP = os.path.join(ROOT, "desktopApp")

DENSITIES = {"mdpi": 1.0, "hdpi": 1.5, "xhdpi": 2.0, "xxhdpi": 3.0, "xxxhdpi": 4.0}

# Trim this many source pixels inside the opaque edge to drop the bevelled rim,
# then feather the cut over FEATHER pixels.
RIM = 28
FEATHER = 8
# Radius (source px, from the artwork's centre) that must fit the 33dp safe zone.
CONTENT_RADIUS = 550


def clean_master(path):
    """Opaque rounded square without the stray pixels around it."""
    im = Image.open(path).convert("RGBA")
    alpha = im.getchannel("A")
    left, top, right, bottom = alpha.point(lambda v: 255 if v > 250 else 0).getbbox()
    pad = 4
    box = (max(left - pad, 0), max(top - pad, 0), min(right + pad, im.width), min(bottom + pad, im.height))
    keep = Image.new("L", im.size, 0)
    keep.paste(255, box)
    # The artwork is ~99% opaque throughout; make the inside fully opaque.
    alpha = ImageChops.multiply(alpha.point(lambda v: 255 if v > 245 else v), keep)
    im.putalpha(alpha)
    return im.crop(box)


def background_color(im):
    """Median green of a band just inside the rim, away from the gold arch."""
    px = im.load()
    w, h = im.size
    samples = []
    for t in range(RIM + 4, RIM + 40, 4):
        for s in range(w // 2 - 60, w // 2 + 60, 6):
            samples.append(px[s, h - 1 - t])  # bottom edge, below the tassel
        for s in range(h // 2 - 150, h // 2 + 150, 6):
            samples.append(px[t, s])          # left edge, outside the arch
            samples.append(px[w - 1 - t, s])  # right edge
    return tuple(int(statistics.median(c[i] for c in samples)) for i in range(3))


def adaptive_foreground(im):
    """Square 108dp layer at source resolution: feathered artwork on transparency."""
    mask = im.getchannel("A").point(lambda v: 255 if v > 250 else 0)
    mask = mask.filter(ImageFilter.MinFilter(2 * RIM + 1)).filter(ImageFilter.GaussianBlur(FEATHER))
    art = im.copy()
    art.putalpha(mask)
    canvas_size = round(CONTENT_RADIUS * 108 / 33)
    canvas = Image.new("RGBA", (canvas_size, canvas_size), (0, 0, 0, 0))
    canvas.alpha_composite(art, ((canvas_size - im.width) // 2, (canvas_size - im.height) // 2))
    return canvas


def save(img, path, size):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.resize((size, size), Image.LANCZOS).save(path, optimize=True)


def main():
    src = sys.argv[1] if len(sys.argv) > 1 else os.path.join(ROOT, "icon.png")
    master = clean_master(src)
    bg = background_color(master)
    foreground = adaptive_foreground(master)

    full = Image.new("RGBA", foreground.size, bg + (255,))
    full.alpha_composite(foreground)
    # Legacy round icon: the adaptive composite seen through a circular mask.
    inset = foreground.width // 6
    round_icon = full.crop((inset, inset, foreground.width - inset, foreground.width - inset))
    circle = Image.new("L", (round_icon.width * 4, round_icon.height * 4), 0)
    ImageDraw.Draw(circle).ellipse((0, 0, circle.width - 1, circle.height - 1), fill=255)
    round_icon.putalpha(circle.resize(round_icon.size, Image.LANCZOS))

    for name, scale in DENSITIES.items():
        folder = os.path.join(RES, f"mipmap-{name}")
        save(master, os.path.join(folder, "ic_launcher.png"), round(48 * scale))
        save(round_icon, os.path.join(folder, "ic_launcher_round.png"), round(48 * scale))
        save(foreground, os.path.join(folder, "ic_launcher_foreground.png"), round(108 * scale))

    values = os.path.join(RES, "values")
    os.makedirs(values, exist_ok=True)
    with open(os.path.join(values, "ic_launcher_background.xml"), "w", encoding="utf-8", newline="\n") as f:
        f.write(
            '<?xml version="1.0" encoding="utf-8"?>\n<resources>\n'
            f'    <color name="ic_launcher_background">#{bg[0]:02X}{bg[1]:02X}{bg[2]:02X}</color>\n'
            "</resources>\n"
        )

    save(master, os.path.join(DESKTOP, "src", "desktopMain", "resources", "icon.png"), 256)
    ico_path = os.path.join(DESKTOP, "icons", "dhikr.ico")
    os.makedirs(os.path.dirname(ico_path), exist_ok=True)
    master.resize((256, 256), Image.LANCZOS).save(
        ico_path, sizes=[(16, 16), (24, 24), (32, 32), (48, 48), (64, 64), (128, 128), (256, 256)],
    )
    print(f"background #{bg[0]:02X}{bg[1]:02X}{bg[2]:02X}; wrote icons to {RES} and {DESKTOP}")


if __name__ == "__main__":
    main()

package io.github.keyboardfx;

public record Key(
        String character, // character visible on the key
        String output, // character(s) outputted by the key
        String upperCase, // character visible on the key when shift is pressed
        String altGr // character visible on the key when alt gr is pressed
) {

    public static final Key BACKSPACE = new Key(null, null, null, null);
    public static final Key SPACE = new Key(null, " ", null, null);
    public static final Key ENTER = new Key(null, "\n", null, null);
    public static final Key SHIFT = new Key(null, null, null, null);
    public static final Key CAPS = new Key(null, null, null, null);
    public static final Key CTRL = new Key(null, null, null, null);
    public static final Key ALT = new Key(null, null, null, null);
    public static final Key OS = new Key(null, null, null, null);
    public static final Key MENU = new Key(null, null, null, null);
    public static final Key ALT_GR = new Key(null, null, null, null);
    public static final Key ESC = new Key(null, null, null, null);
    public static final Key TAB = new Key(null, "\t", null, null);
    public static final Key SWAP = new Key(null, null, null, null);
}

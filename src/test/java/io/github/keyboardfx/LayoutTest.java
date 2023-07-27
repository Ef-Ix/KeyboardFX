package io.github.keyboardfx;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LayoutTest {

    @Test
    public void layoutCreation() {
        Key key1 = new Key("a", "a", "A", null);
        Key key2 = new Key("b", "b", "B", null);
        Layout layout = new Layout("Test", Map.of(1, key1, 2, key2));
        assertEquals(key1, layout.getKeys().get(1));
        assertEquals(key2, layout.getKeys().get(2));
        assertEquals("Test", layout.name());
    }

    @Test
    public void layoutCreationBase() {
        Key a = new Key("a", "a", "A", null);
        Key b = new Key("b", "b", "B", null);
        Layout base = new Layout("Base", Map.of(1, a));
        Layout layout = new Layout(base, "Layout", Map.of(2, b));

        assertEquals("Layout", layout.name());
        assertEquals(Map.of(1, a, 2, b), layout.getKeys());

    }
}

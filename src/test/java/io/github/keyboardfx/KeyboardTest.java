package io.github.keyboardfx;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class KeyboardTest extends ApplicationTest {

    private Keyboard keyboard;

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        Layout layout = new Layout("Test", new HashMap<>());
        this.keyboard = new Keyboard(layout);

        stage.setScene(new Scene(keyboard.getNode()));
        stage.show();
    }

    @Test
    public void layoutUpdate() {
        Layout layout = new Layout("Test2", Map.of(1, new Key("0", "0", ")", null)));
        keyboard.setLayout(layout).update();

        waitForFxEvents();

        assertEquals("0", lookup("#BUTTON1").queryButton().getText());
    }

    @Test
    public void loadFromJson() throws URISyntaxException {
        Layout layout = Layout.loadFromFile(Path.of(Objects.requireNonNull(KeyboardTest.class.getResource("keyboard/qwerty.json")).toURI()));
        keyboard.setLayout(layout).update();

        waitForFxEvents();

        assertEquals("q", lookup("#BUTTON13").queryButton().getText());
        assertEquals("w", lookup("#BUTTON14").queryButton().getText());
        assertEquals("e", lookup("#BUTTON15").queryButton().getText());
        assertEquals("r", lookup("#BUTTON16").queryButton().getText());
        assertEquals("t", lookup("#BUTTON17").queryButton().getText());
        assertEquals("y", lookup("#BUTTON18").queryButton().getText());
    }

    @Test
    public void write() {
        List<String> written = new ArrayList<>();
        List<Key> pressed = new ArrayList<>();

        Key e = new Key("e", "e", "E", "€");
        Key a = new Key("a", "b", "A", null);

        Layout layout = new Layout("Test3", Map.of(1, e, 2, a));
        keyboard.setLayout(layout).update();
        keyboard.registerTextConsumer(written::add);
        keyboard.registerPressConsumer(pressed::add);

        waitForFxEvents();

        clickOn("e");
        clickOn("#LEFT_SHIFT");

        assertTrue(lookup("#SHIFT_SELECTED").queryAs(CheckBox.class).isSelected());

        clickOn("A");

        assertFalse(lookup("#SHIFT_SELECTED").queryAs(CheckBox.class).isSelected());

        assertEquals(List.of("e", "A"), written);
        assertEquals(List.of(e, Key.SHIFT, a, Key.SHIFT), pressed);

        written.clear();
        pressed.clear();

        clickOn("#LEFT_CTRL_CMD");
        clickOn("#ALT");

        assertTrue(lookup("#ALT_SELECTED").queryAs(CheckBox.class).isSelected());
        assertTrue(lookup("#CTRL_SELECTED").queryAs(CheckBox.class).isSelected());

        clickOn("e");
        clickOn("a");

        assertEquals(List.of("€", "b"), written);


    }
}

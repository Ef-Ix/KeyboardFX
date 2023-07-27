package io.github.keyboardfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public class Keyboard {

    private final Set<Consumer<String>> textConsumers;
    private final Set<Consumer<Key>> pressConsumers;

    @FXML
    CheckBox SHIFT_SELECTED;
    @FXML
    CheckBox CTRL_SELECTED;
    @FXML
    CheckBox ALT_SELECTED;
    @FXML
    CheckBox CAPS_SELECTED;
    @FXML
    Button BACKSPACE;

    private Parent node;
    private Layout layout;

    /**
     * Creates a new keyboard with the given layout.
     *
     * @param layout the layout of the keyboard
     * @throws RuntimeException if the fxml file could not be loaded
     */
    public Keyboard(Layout layout) {
        this.layout = layout;
        this.textConsumers = new HashSet<>();
        this.pressConsumers = new HashSet<>();
        try {
            load();
            update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the keyboard from the fxml file.
     *
     * @throws IOException if the fxml file could not be loaded
     */
    private void load() throws IOException {
        FXMLLoader loader = new FXMLLoader(Keyboard.class.getResource("fxml/keyboard.fxml"));
        loader.setControllerFactory((c) -> this);
        this.node = loader.load();
    }

    /**
     * Sets the layout of the keyboard.
     *
     * @param layout the layout to set
     * @return the keyboard
     */
    public Keyboard setLayout(Layout layout) {
        this.layout = layout;
        return this;
    }

    /**
     * Updates the keyboard layout.
     * Should be called after the layout has been changed.
     *
     * @return the keyboard
     */
    public Keyboard update() {
        layout.getKeys().forEach(this::setButtonKey);
        return this;
    }

    /**
     * Sets the text of the button with the given id to the given character.
     *
     * @param id  the id of the key button
     * @param key the key to set
     */
    private void setButtonKey(int id, Key key) {
        Button button = (Button) this.node.lookup("#BUTTON" + id);
        if (button != null) Platform.runLater(() -> button.setText((SHIFT_SELECTED.isSelected() || CAPS_SELECTED.isSelected()) && key.upperCase() != null ? key.upperCase() : key.character()));
    }

    /**
     * Returns the root node of the keyboard which can be displayed.
     *
     * @return the root node of the keyboard.
     */
    public Parent getNode() {
        return this.node;
    }

    @FXML
    void onKeyPressed(MouseEvent event) {
        if (event.getSource() instanceof Button button) {
            switch (button.getId()) {
                case "ESC" -> esc();
                case "TAB" -> tab();
                case "CAPS" -> caps();
                case "LEFT_SHIFT", "RIGHT_SHIFT" -> shift();
                case "LEFT_CTRL_CMD", "RIGHT_CTRL_CMD" -> ctrl();
                case "ALT" -> alt();
                case "SPACE" -> space();
                case "ENTER" -> enter();
                case "BACKSPACE" -> backspace();
                case "LEFT_OS", "RIGHT_OS" -> os();
                case "MENU" -> menu();
                case "ALT_GR" -> altGr();
                case "SWAP" -> swap();
                default -> {
                    String num = button.getId().replace("BUTTON", "");
                    if (isInt(num)) {
                        int id = Integer.parseInt(num);
                        Key key = layout.getKeys().get(id);
                        press(key);
                        write(key);
                    }
                    if (SHIFT_SELECTED.isSelected()) shift();
                }
            }
        }
    }

    private void altGr() {
        press(Key.ALT_GR);
        if (CAPS_SELECTED.isSelected() && SHIFT_SELECTED.isSelected()) {
            CAPS_SELECTED.setSelected(false);
            SHIFT_SELECTED.setSelected(false);
        } else {
            CAPS_SELECTED.setSelected(true);
            SHIFT_SELECTED.setSelected(true);
        }
    }

    private void esc() {
        press(Key.ESC);
    }

    private void tab() {
        press(Key.TAB);
        write(Key.TAB);
    }

    private void caps() {
        press(Key.CAPS);
        CAPS_SELECTED.setSelected(!CAPS_SELECTED.isSelected());
        SHIFT_SELECTED.setSelected(CAPS_SELECTED.isSelected());
        update();
    }

    private void shift() {
        press(Key.SHIFT);
        SHIFT_SELECTED.setSelected(!SHIFT_SELECTED.isSelected());
        if (SHIFT_SELECTED.isSelected()) CAPS_SELECTED.setSelected(false);
        update();
    }

    private void ctrl() {
        press(Key.CTRL);
        CTRL_SELECTED.setSelected(!CTRL_SELECTED.isSelected());
        if (CTRL_SELECTED.isSelected()) {
            SHIFT_SELECTED.setSelected(false);
            CAPS_SELECTED.setSelected(false);
        }
    }

    private void alt() {
        press(Key.ALT);
        ALT_SELECTED.setSelected(!ALT_SELECTED.isSelected());
        if (ALT_SELECTED.isSelected()) {
            SHIFT_SELECTED.setSelected(false);
            CAPS_SELECTED.setSelected(false);
        }
    }

    private void space() {
        press(Key.SPACE);
        write(Key.SPACE);
    }

    private void enter() {
        press(Key.ENTER);
    }

    private void backspace() {
        press(Key.BACKSPACE);
    }

    private void os() {
        press(Key.OS);
    }

    private void swap() {
        press(Key.SWAP);
    }

    private void menu() {
        press(Key.MENU);
    }

    private boolean isInt(String possibleInt) {
        try {
            Integer.parseInt(possibleInt);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void write(Key key) {
        String text = key.output();

        if (text == null) text = key.character();
        if ((SHIFT_SELECTED.isSelected() || CAPS_SELECTED.isSelected()) && (key.upperCase() != null)) text = key.upperCase();
        if (CTRL_SELECTED.isSelected() && ALT_SELECTED.isSelected() && key.altGr() != null) text = key.altGr();

        String finalText = text;
        textConsumers.forEach(consumer -> consumer.accept(finalText));
    }

    private void press(Key key) {
        pressConsumers.forEach(consumer -> consumer.accept(key));
    }

    /**
     * Registers a consumer which will be called some text is written.
     *
     * @param consumer the consumer to register
     * @return the keyboard
     */
    public Keyboard registerTextConsumer(Consumer<String> consumer) {
        this.textConsumers.add(consumer);
        return this;
    }

    /**
     * Registers a consumer which will be called some key is pressed.
     *
     * @param consumer the consumer to register
     * @return the keyboard
     */
    public Keyboard registerPressConsumer(Consumer<Key> consumer) {
        this.pressConsumers.add(consumer);
        return this;
    }

}

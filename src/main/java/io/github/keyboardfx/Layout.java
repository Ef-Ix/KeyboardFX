package io.github.keyboardfx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Layout {

    public static final List<Layout> LOADED_LAYOUTS = new ArrayList<>();

    private final String name;
    private final HashMap<Integer, Key> keys;

    /**
     * Creates a new layout.
     * The outputs default to the layout if not overwritten. You only have to specify the keys that are different.
     *
     * @param layout The layout of the keyboard (sets characters for all the keys).
     */
    public Layout(String name, Map<Integer, Key> layout) {
        this("", name, layout);
    }

    /**
     * Creates a new layout.
     * You cannot use the same name for the base and the layout.
     *
     * @param baseName The name of the base layout to use.
     * @param name     The name of the layout.
     * @param layout   The layout of the keyboard (sets characters for all the keys).
     */
    public Layout(@JsonProperty("base") String baseName, @JsonProperty("name") String name, @JsonProperty("keys") Map<Integer, Key> layout) {
        this(LOADED_LAYOUTS.stream().filter(l -> l.name().equals(baseName) && !l.name().equals(name)).findFirst().orElse(null), name, layout);
    }

    /**
     * Creates a new layout.
     * All values default to the base layout if not overwritten. You only have to specify the keys that are different.
     * The outputs default to the layout if not overwritten. You only have to specify the keys that are different.
     *
     * @param base   The base layout to use.
     * @param layout The layout of the keyboard (sets characters for all the keys).
     */
    public Layout(Layout base, String name, Map<Integer, Key> layout) {
        this.keys = base == null ? new HashMap<>() : base.getKeys();
        this.name = name;
        this.keys.putAll(layout);
    }

    /**
     * Parses a JSON string to a layout.
     *
     * @param json The JSON string to parse.
     * @return The parsed layout.
     */
    public static Layout fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Layout.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Layout("Empty", Map.of());
        }
    }

    /**
     * Loads a layout from a file.
     *
     * @param path The path to the file.
     * @return The loaded layout.
     */
    public static Layout loadFromFile(Path path) {
        try {
            return fromJson(Files.readString(path));
        } catch (Exception e) {
            e.printStackTrace();
            return new Layout("Empty", Map.of());
        }
    }

    /**
     * Parses a layout to a JSON string.
     *
     * @param layout The layout to parse.
     * @return The parsed JSON string.
     */
    public static String toJson(Layout layout) {
        try {
            return new ObjectMapper().writeValueAsString(layout);
        } catch (JsonProcessingException e) {
            System.err.println("Could not parse JSON: " + layout);
            return "{}";
        }
    }

    /**
     * Registers a layout to be selected when pressing swap.
     *
     * @param layout The layout to register.
     */
    public static void registerLayout(Layout layout) {
        LOADED_LAYOUTS.add(layout);
    }

    public HashMap<Integer, Key> getKeys() {
        return keys;
    }

    public String name() {
        return this.name;
    }


}

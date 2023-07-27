# KeyboardFX
A customizable keyboard for javafx applications based on [JavaFX](https://github.com/openjfx) and [Jackson](https://github.com/FasterXML/jackson).

## JSON based
Create a json file for your keyboard or use the existing one which is based on qwerty.

```json
{
  "name": "myawesomelayout",
  "base": "qwerty",
  "keys": {
    "1": {
      "character": "e",
      "output": "x",
      "upperCase": "E",
      "altGr": "€"
    }
  }
}
```
JSON files can be loaded using `Layout#loadFromFile(Path path)`. This will result in a layout based on qwerty with the first customizable key being an "e" instead. Since the output is overidden, it will print an "x" when pressed. When shift/caps is pressed, "E" will be used instead. Using CTRL and ALT, an "€" will be printed.

## How to create a keyboard?
Create or load a layout and use the Keyboard constructor to create a new Keyboard. `Keyboard#getNode()` will return a Parent which represents the keyboard window.

In order to use the keyboard, register a listener (consumer) to either the key event or the write event. These will be triggered when a button is pressed.

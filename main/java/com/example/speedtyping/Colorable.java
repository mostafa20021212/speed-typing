package com.example.speedtyping;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public interface Colorable {
    String ORIGINAL_COLOR = "transparent";
    String NEW_COLOR_TRUE = "#3F3E3FFF";
    String NEW_COLOR_FALSE = "#CA4754FF";
    void changeColor(KeyEvent actionEvent,boolean isCorrect);
    void returnColor();
}

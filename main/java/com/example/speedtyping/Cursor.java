package com.example.speedtyping;

import javafx.scene.shape.Line;

public abstract class Cursor extends Line {


    public Cursor(int i, int i1, int i2, int i3) {
        super(i,i1,i2,i3);

        this.setStrokeWidth(2);
        this.setLayoutY(150);
        this.setTranslateX(110);
    }

    public abstract void moveRight(Word word);
    public abstract void moveLeft(Word word);
    public abstract void moveDown();

    public abstract void moveUp();
    public abstract void reset();
    public abstract void resetAll();



}

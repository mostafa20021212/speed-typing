package com.example.speedtyping;

import javafx.scene.text.Text;

public class Word extends Text {

    public Word(String s){
        super(s);
    }
    public double minX(){
        return this.getBoundsInParent().getMinX();
    }
    public double sizeWord(){
        return this.getBoundsInParent().getMinX() +  this.getBoundsInParent().getWidth();
    }



}
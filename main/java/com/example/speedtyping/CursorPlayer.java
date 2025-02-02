package com.example.speedtyping;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.security.PublicKey;
import java.util.Stack;

public class CursorPlayer extends Cursor{
    Stack<Pair<Double,Double>>oldPlace;


    public CursorPlayer(){
        super(10, 50, 10, 80);
        this.setStroke(Color.YELLOW);
        setAnimation();
        oldPlace = new Stack<>();


    }

    private void setAnimation() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(400), new KeyValue(this.opacityProperty(), 0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void moveRight(Word word) {

        this.setLayoutX(word.sizeWord());
    }

    @Override
    public void moveLeft(Word word) {
        this.setLayoutX(word.minX());
    }

    @Override
    public void moveDown() {
       oldPlace.push(new Pair<>(this.getLayoutX(),this.getLayoutY()));
        this.setLayoutY(this.getLayoutY() + 33);
        this.setLayoutX(0.0);
    }

    @Override
    public void moveUp() {
        this.setLayoutX(oldPlace.peek().getKey());
        this.setLayoutY(oldPlace.peek().getValue());
        oldPlace.pop();
    }

    @Override
    public void reset() {
        oldPlace.push(new Pair<>(this.getLayoutX(),this.getLayoutY() -33));
        this.setLayoutX(0.0);
    }
    @Override
    public void resetAll() {
        this.setLayoutX(0.0);
        this.setLayoutY(150.0);
    }


}
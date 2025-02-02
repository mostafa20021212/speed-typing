package com.example.speedtyping;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class CursorAi extends Cursor{

    public  int row = 0;
    int[] sp  =  {10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
    double targetX = 0.0;
    private int speed;
    private  VBox board;
    private Timeline timeline;

    public CursorAi( VBox board) {
        super(10, 50, 10, 80);
        this.board = board;
        targetX = 800;
        this.speed = sp[(int) (Math.random() * sp.length-1)];
        System.out.println(speed);
        this.setStroke(Color.rgb(50, 52, 55));
        moveAi();
    }
    private void moveAi(){
        this.setStroke(Color.rgb(50, 52, 55));

        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(15), event -> {
            double currentX = this.getLayoutX();
            if (currentX < targetX) {
                this.setLayoutX(currentX + 1); // Adjust the speed by changing the increment value (1 in this case)
            }
            if(currentX == targetX) {
                if(row == 2)timeline.stop();
                else
                    moveDown();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    @Override
    public void moveRight(Word word) {

    }

    @Override
    public void moveLeft(Word word) {

    }

    @Override
    public void moveDown() {
        System.out.println("down");
        row++;
        this.setLayoutY(this.getLayoutY() + 33);
        this.setLayoutX(0.0);
    }

    @Override
    public void moveUp() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void resetAll() {
        this.setLayoutX(0.0);
        this.setLayoutY(150.0);
        timeline.stop();
    }
}

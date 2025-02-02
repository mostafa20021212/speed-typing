package com.example.speedtyping;

import javafx.application.Platform;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Timer implements Runnable {
    private AtomicInteger time;


    public static boolean endTheGame = false;
    private Text stage;

    public Timer(int time, Text stage) {
        this.time = new AtomicInteger(time);
        this.stage = stage;
    }

    @Override
    public void run() {
        try {
            while (!endTheGame && !Thread.currentThread().isInterrupted() && this.time.get() >= 0) {
                Platform.runLater(() -> stage.setText(String.valueOf(this.time.getAndDecrement())));
                System.out.println("time");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {

            return;
        }

        endTheGame = true;
    }
}
package com.example.speedtyping;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGame extends Application {
    TypingGame game;
    @Override
    public void start(Stage stage) throws IOException {
        try{

            FXMLLoader fxmlLoader = new FXMLLoader(MainGame.class.getResource("main-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            stage.setTitle("Typing Game by Ahmed & Mostafa");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(e->{
                TypingGame.isClose = true;
                Timer.endTheGame = true;
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
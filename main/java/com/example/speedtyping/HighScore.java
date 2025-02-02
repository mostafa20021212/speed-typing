package com.example.speedtyping;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public  class  HighScore  {

    public static void updateHighScore(int second, Button high, PlayerList playerList)
    {
        if(!playerList.listOfPlayers.isEmpty()) {
            if(playerList.getHighScore(second) == 0)
                high.setVisible(false);
            else
            {
                high.setText("highScore : " + playerList.getHighScore(second));
            }

        }else {
            high.setVisible(false);
        }

    }

}
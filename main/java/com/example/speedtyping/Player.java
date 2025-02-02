package com.example.speedtyping;

import java.io.Serializable;
import java.util.Map;

public class Player implements Serializable {

    private String name;
    private Score score;



    public Player(String name) {
        this.name = name;
        this.score = new Score();
    }

    public Score getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setScore(int newScore, int second,int newAcc){
        if(second == 15 && newScore > this.score.getMax15()){
            this.score.setMax15(newScore);
            this.score.setAcc15(newAcc);
        }
        else if(second == 30 && newScore > this.score.getMax30()){
            this.score.setMax30(newScore);
            this.score.setAcc30(newAcc);
        }
        else if (second == 60 && newScore > this.score.getMax60()){
            this.score.setMax60(newScore);
            this.score.setAcc60(newAcc);
        }
    }
    public void setWorstCharacter(Map<Character,Integer>storage,int time){
        score.updateWorstCh(storage,time);
    }
    @Override
    public String toString(){
        return name;
    }
}

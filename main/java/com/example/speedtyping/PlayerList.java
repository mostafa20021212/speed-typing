package com.example.speedtyping;

import java.io.Serializable;
import java.util.*;


public class PlayerList implements Serializable {
    List<Player> listOfPlayers;
    Map<Integer,List<Player>>map = new LinkedHashMap<>();;

    public PlayerList() {

        this.listOfPlayers = new ArrayList<>();
    }

    public Player createPlayer(String namePlayer){
        if(!isFound(namePlayer)){
            System.out.println("not found player");
            this.listOfPlayers.add(new Player(namePlayer));
            return this.listOfPlayers.get(listOfPlayers.size()-1);
        }
        return this.listOfPlayers.stream().filter(e->e.getName().equals(namePlayer)).findAny().get();
    }


    private boolean isFound(String namePlayer){
        return this.listOfPlayers.stream().anyMatch(e->e.getName().equals(namePlayer));
    }

    public int getHighScore(int second) {
        if(second == 15)
            return this.listOfPlayers.stream().max(Comparator.comparingInt(x -> x.getScore().getMax15())).get().getScore().getMax15();
        else if(second == 30)
            return this.listOfPlayers.stream().max(Comparator.comparingInt(x -> x.getScore().getMax30())).get().getScore().getMax30();
        else
            return this.listOfPlayers.stream().max(Comparator.comparingInt(x -> x.getScore().getMax60())).get().getScore().getMax60();
    }
    public void updateScore15(){

        this.listOfPlayers.sort((x,y)-> {

            if(y.getScore().getMax15() - x.getScore().getMax15() ==0){
               return y.getScore().getAcc15() - x.getScore().getAcc15();
            }
            return y.getScore().getMax15() - x.getScore().getMax15();
        });

    }

    public void updateScore30(){
        this.listOfPlayers.sort((x,y)-> {
            if(y.getScore().getMax30() - x.getScore().getMax30() ==0){
                return y.getScore().getAcc30() - x.getScore().getAcc30();
            }
            return y.getScore().getMax30() - x.getScore().getMax30();
        });

    }
    public void updateScore60(){
        this.listOfPlayers.sort((x,y)-> {
            if(y.getScore().getMax60() - x.getScore().getMax60() ==0){
                return y.getScore().getMax60() - x.getScore().getMax60();
            }
            return y.getScore().getMax60() - x.getScore().getMax60();
        });

    }
}

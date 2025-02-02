package com.example.speedtyping;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class TypingGame    {

    @FXML
    public Button enter;
    @FXML
    public Label logo;
    Stage stage3 = null;
    @FXML
    public Label king;
    private boolean takeVS;
    public TextField textArea;
    @FXML
    public Button high;

    private boolean isCorrect = true;
    private int allWords , correctWords;
    private Map<Character,Integer>worstChar;
    @FXML
    private Text wpm;
    @FXML
    private Text acc;
    @FXML
    private HBox modeTimeBtn;

    private int correctChar = 0 ,unCorrectChar = 0;
    private  List<Character> ch = List.of('=', '-', ';', '.', ',', '\'', '[', ']');


    private CursorAi corsorAi = null;
    private int mode =1 ;
    private boolean isTakeTime ;
    static String name;
    private Keyboard keyboard;
    private int whatTime;
    private Timer timer;
    private Cursor player;
    private Player user;

    private ListWord listWord;
    private int row = 0,col =-1;
    private int counter;
    private  Thread thread;
    private Map<Integer,Word>mapListWord;
    private Stack<Boolean>backCorrectWord;

    private String filePath = "user-data.txt";

    private PlayerList playerList = new PlayerList();
    @FXML
    private  Pane pane;
    @FXML
    private VBox board;
    @FXML
    private Text time;

    @FXML
    private Button spaceBtn;
    private Boolean isStart ;
    public static boolean isClose = false;

    @FXML
    private  Pane pane2;
    @FXML
    private  Pane pane3;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                acc.setVisible(false);
                wpm.setVisible(false);
                modeTimeBtn.setVisible(true);
                king.setVisible(true);
                acc.setText("");
                wpm.setText("");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };



    public void start(){
        loadList();
        listWord = new ListWord(board);
        player = new CursorPlayer();
        pane.getChildren().add(player);
        this.mapListWord = listWord.getMapOfWord();
        isStart = true;
        worstChar = new LinkedHashMap<>();
        backCorrectWord = new Stack<>();
        keyboard = new Keyboard(pane3,pane2);;
    }



    private void loadList() {
        File f = new File(filePath);

        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            playerList = (PlayerList) ois.readObject();


            ois.close();


            //FileOutputStream fos = new FileOutputStream(f, false);
            //fos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
      //  user = playerList.createPlayer(name);
    }
    private void startRun() {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                while ( !isClose &&!Thread.currentThread().isInterrupted() && !Timer.endTheGame){
                    System.out.println("start");
                }
                resetAndSave();
            }
        }).start();

    }

    public void checkPlayerRegister(ActionEvent et) throws IOException
    {




        name = textArea.getText();
        name = name.trim();

        if(name.isEmpty())return;

        Stage stage = (Stage) enter.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainGame.class.getResource("hello-view.fxml"));
        Scene scene ;
        try {
            scene = new Scene(fxmlLoader.load(), 1000, 610);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TypingGame game;
        stage.setTitle("Typing Game by Ahmed & Mostafa");
        stage.setScene(scene);
        stage.setResizable(false);
        game = fxmlLoader.getController();
        game.start();
        scene.setOnKeyPressed(e->{
           if( e.getCode().equals(KeyCode.ESCAPE)){
               TypingGame.isClose = true;
               Timer.endTheGame = true;
               stage.close();
           }
        });
        stage.centerOnScreen();
        stage.show();


    }

    public void startGame(KeyEvent e) {
        if(stage3 != null){
            stage3.close();
        }
        pane.requestFocus();
        btnsVisble();

        if(isStart){
            if(takeVS){

                corsorAi = new CursorAi(board);
                pane.getChildren().add(corsorAi);
                corsorAi.setVisible(true);
                takeVS = false;
            }
            startRun();
            timer = new Timer(whatTime,time);
            thread = new Thread(timer);
            thread.start();
            isStart = false;
        }

        if(e.isControlDown()
                && e.getCode().equals(KeyCode.BACK_SPACE)
                && counter != 0
                && col >=0
        ){

            Word wordDelete  = mapListWord.get(counter-1);

            if(wordDelete.getText().equals(" ")){
                player.moveLeft(wordDelete);
                if(col== -1 && row != 0){
                    row--;
                    col = ((HBox)board.getChildren().get(row)).getChildren().size()-1;
                    player.moveUp();
                }
                counter--;
                col--;
                resetAccAndWpm();
                return;
            }
            while (counter != 0 && !wordDelete.getText().equals(" ")){
                wordDelete.setFill(Color.rgb(219, 216, 182));
                wordDelete = mapListWord.get(--counter);
                if(col== -1 && row != 0){
                    row--;
                    col = ((HBox)board.getChildren().get(row)).getChildren().size()-1;
                    player.moveUp();
                }
                col--;


            }

            wordDelete.setFill(Color.rgb(219, 216, 182));

            resetAccAndWpm();
            if(col == -2 && row == 0){
                wordDelete = mapListWord.get(counter+1);
                player.moveLeft(wordDelete);
                shrinkWords(wordDelete.getText(),true);
                col = -1;
                return;
            }else {
                player.moveLeft(wordDelete);
            }
            shrinkWords(wordDelete.getText(),false);
            return;
        }

        Word charClick = mapListWord.get(counter);


        if(e.isControlDown() && e.getCode().equals(KeyCode.ENTER)){
            resetAll();
            return;
        }

        String click = e.getText();

        click = mode == 3 || mode == 2 ?  click.toUpperCase() : click.toLowerCase();


        boolean isSpace = e.getCode().equals(KeyCode.SPACE);
        boolean isBackSpace = e.getCode().equals(KeyCode.BACK_SPACE);
        boolean isLetter = false;

        if(!click.isEmpty())
            isLetter =  click.length() ==1 && Character.isLetter(click.charAt(0))  || Character.isDigit(click.charAt(0)) || ch.contains(click.charAt(0));

        if(isLetter || isSpace){
            if(isCorrect && charClick.getText().equals(" ")) {
                correctWords++;
            }
            if(charClick.getText().equals(" "))
            {
                backCorrectWord.push(isCorrect);
                isCorrect = true;
                allWords++;
            }
            if(charClick.getText().equals(click) || (charClick.getText().equals(" ") && isSpace)){
                charClick.setFill(Color.rgb(63, 62, 63));
                keyboard.changeColor(e,true);
            }
            else{
                charClick.setFill(Color.rgb(202,71,84));
                keyboard.changeColor(e,false);
                isCorrect = false;
                char ch = charClick.getText().charAt(0);
                if(!worstChar.containsKey(ch)){
                    worstChar.put(ch,1);
                }else worstChar.put(ch,1+worstChar.get(ch));
            }
            player.moveRight(charClick);

            counter++;
            col++;
        }

        if(col == ((HBox)board.getChildren().get(row)).getChildren().size()-1 && charClick.getText().equals(" ")){
            row++;
            if(row == 2){
                player.reset();
                listWord.removeLineOfWord();
                col = -1;
                row--;
                return;
            }
            col = -1;
            player.moveDown();
        }
        if(isBackSpace){

            if(col== -1 && row != 0){
                shrinkWords(charClick.getText(),true);
                row--;
                col = ((HBox)board.getChildren().get(row)).getChildren().size()-1;
                player.moveUp();
            }else if (col >= 0){

                charClick = mapListWord.get(--counter);
                shrinkWords(charClick.getText(),false);
                charClick.setFill(Color.rgb(219, 216, 182));
                player.moveLeft(charClick);
                col--;
            }

        }
        resetAccAndWpm();


    }

    private void resetAccAndWpm() {
        acc.setText("Acc : "+Math.round(((correctWords*1.0)/allWords)*100));
        if(!time.getText().isEmpty()){
            long num = Math.round((allWords) / ((whatTime - Double.parseDouble(time.getText()))/60.0));
            if(num >= 500)
                num = 50;
            wpm.setText("Wpm : "+ num );
        }
    }

    private void btnsVisble() {
        king.setVisible(false);
        high.setVisible(false);
        acc.setVisible(true);
        wpm.setVisible(true);
        modeTimeBtn.setVisible(false);
    }

    private void shrinkWords(String ch,boolean f){
        if(ch.equals(" ") || f){
            if(!backCorrectWord.empty()){
                allWords--;
                if(backCorrectWord.pop())correctWords--;
            }
        }
    }

    private void resetAll() {
        pane.setOnKeyPressed(this::shrinkPointer);
      if(thread != null && thread.isAlive())
           thread.interrupt();
        //endTime.interrupt();
        row = 0;
        col = -1;
        if(takeVS ||( corsorAi != null && corsorAi.isVisible())){
            takeVS = true;
            corsorAi.resetAll();
            pane.getChildren().remove(corsorAi);
        }

        listWord.resetBoard();
        listWord.addWordsToBoard(mode);
        mapListWord = listWord.getMapOfWord();
        counter = 0;
        isStart = true;
        time.setText("");
        player.resetAll();
        isClose = false;
        Timer.endTheGame = false;
        isTakeTime = false;
        allWords = 0;
        correctWords = 0;
        isCorrect = true;
        backCorrectWord.clear();
        unCorrectChar = 0;
        correctChar = 0;
        Thread wpmAccReset = new Thread(r);
        wpmAccReset.start();
        resetTime();

    }

    private void shrinkPointer(KeyEvent e){

    }
    public void isSpaceClick(KeyEvent keyEvent) {
        if(isTakeTime && keyEvent.getCode().equals(KeyCode.SPACE))
            startGame(keyEvent);
    }
    void resetTime(){
        isTakeTime = false;
        for(Node it : pane.getChildren()){
            if(it instanceof HBox hBox){
                for(Node i : hBox.getChildren()){
                    if(i instanceof Button click){
                        if(click.getText().equals("30") || click.getText().equals("60")|| click.getText().equals("15")){
                            ((Button)i).setStyle("-fx-border-color: transparent; -fx-font-size: 14px; -fx-text-fill: #646669; -fx-background-color:transparent" );
                        }
                    }
                }
            }
        }
    }

    public void changeTime(MouseEvent mouseEvent) {
        high.setVisible(true);
        isTakeTime = true;
        whatTime =Integer.parseInt( ((Button) mouseEvent.getSource()).getText());
        HBox btnHbox = (HBox) ((Button) mouseEvent.getSource()).getParent();
        HighScore.updateHighScore(whatTime,high,playerList);

        for(Node it :btnHbox.getChildren() ){
            if(it instanceof Button click){

                if(click.getText().equals("30") || click.getText().equals("60")|| click.getText().equals("15")){
                    ((Button)it).setStyle("-fx-border-color: transparent; -fx-font-size: 14px; -fx-text-fill: #646669; -fx-background-color:transparent" );
                }
            }
        }
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color:#D5AD17");
        spaceBtn.requestFocus();
        if(isTakeTime){
            pane.setOnKeyPressed(this::startGame);
        }
    }

    public void changeMode(MouseEvent mouseEvent) {

        mode = Integer.parseInt(((Button)mouseEvent.getSource()).getId());
        HBox btnHbox = (HBox) ((Button) mouseEvent.getSource()).getParent();

        for(Node it :btnHbox.getChildren() ){
            if(it instanceof Button click){

                if(click.getId() != null && (click.getId().equals("1") || click.getId().equals("2")|| click.getId().equals("3")||click.getId().equals("4"))  ){
                    ((Button)it).setStyle("-fx-border-color: transparent; -fx-font-size: 14px; -fx-text-fill: #646669; -fx-background-color:transparent" );
                }
            }
        }

        listWord.addWordsToBoard(mode);
        keyboard.changeLayout(mode);
        ((Button) mouseEvent.getSource()).setStyle("-fx-background-color:#D5AD17");
        spaceBtn.requestFocus();



    }
    public  void resetAndSave(){



        File f = new File(filePath);

        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            int ACC =(int) Math.round((correctWords * 1.0) / allWords * 100);
            if(!isClose && ACC >= 30){
                user = playerList.createPlayer(name);
                user.setScore((int) Math.round(allWords / (whatTime / 60.0)), whatTime,ACC);
                user.setWorstCharacter(worstChar,whatTime);
            }
            oos.writeObject(playerList);
            oos.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        try{

            Platform.runLater(this::resetAll);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public void releaseKey() {
        keyboard.returnColor();
    }


    public void changeToScore(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainGame.class.getResource("Score-panel.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 826, 400);
            stage3 = new Stage();
            stage3.setTitle("Typing Game by Ahmed & Mostafa");
            stage3.setScene(scene);
            stage3.show();
            stage3.setResizable(true);
            scene.setOnKeyPressed(e->{
                if(e.getCode().equals(KeyCode.ESCAPE))stage3.close();
            });

            Pane pane1 = (Pane) root.lookup("#pane15");
            Pane pane2 = (Pane) root.lookup("#pane30");
            Pane pane3 = (Pane) root.lookup("#pane60");


            playerList.updateScore15();
            for(Player p: playerList.listOfPlayers)
            {
                System.out.println(p.getName());
                System.out.println(p.getScore().getMax15());
                if(p.getScore().getMax15() == 0)continue;
                Button stats = new Button(p.getName() + " Wpm: " + p.getScore().getMax15()+" Acc: "+p.getScore().getAcc15() + " WorstChar: "+p.getScore().toStringTime(15));
                stats.setStyle("-fx-background-color: transparent; -fx-border-color: #D5AD17; -fx-border-width: 0px 0px 2px 0px; -fx-text-fill: white;-fx-padding: 0 10px;  -fx-margin-top: 105px;");
                stats.setMinWidth(282);
                stats.setMinHeight(40);

                pane1.getChildren().addAll(stats);
            }
            playerList.updateScore30();
            for(Player p: playerList.listOfPlayers)
            {
                if(p.getScore().getMax30() == 0)continue;
                Button stats = new Button(p.getName() + " Wpm: " + p.getScore().getMax30()+" Acc: "+p.getScore().getAcc30() + " WorstChar: "+p.getScore().toStringTime(30));
                stats.setStyle("-fx-background-color: transparent; -fx-border-color: #D5AD17; -fx-border-width: 0px 0px 2px 0px; -fx-text-fill: white;-fx-padding: 0 10px;  -fx-margin-top: 105px;");
                stats.setMinWidth(282);
                stats.setMinHeight(40);
                pane2.getChildren().addAll(stats);
            }
            playerList.updateScore60();
            for(Player p: playerList.listOfPlayers)
            {
                if(p.getScore().getMax60() == 0)continue;
                Button stats = new Button(p.getName() + " Wpm: " + p.getScore().getMax60()+" Acc: "+p.getScore().getAcc60() + " WorstChar: "+p.getScore().toStringTime(60));
                stats.setStyle("-fx-background-color: transparent; -fx-border-color: #D5AD17; -fx-border-width: 0px 0px 2px 0px; -fx-text-fill: white;-fx-padding: 0 10px;  -fx-margin-top: 105px;");
                stats.setMinWidth(282);
                stats.setMinHeight(40);
                pane3.getChildren().addAll(stats);
            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void AiChallenge(MouseEvent mouseEvent) {
        if(takeVS || corsorAi != null){
            ((Button)mouseEvent.getSource()).setStyle("-fx-background-color:transparent");
            takeVS = false;
            if(corsorAi != null)
                corsorAi.setVisible(false);
        }else{
            takeVS = true;
            ((Button)mouseEvent.getSource()).setStyle("-fx-background-color:#fba700");
        }
        spaceBtn.requestFocus();
    }

    public void deleteFile(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete File");
        alert.setHeaderText("click on delete if you want to delete all data");
        ButtonType okButton = new ButtonType("Delete");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(okButton, cancelButton);
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        if (result == okButton)
        {
            File f = new File("user-data.txt");
            f.delete();
        }


    }

    public void firstPage(MouseEvent mouseEvent) throws IOException {
        TypingGame.isClose = true;
        Timer.endTheGame = true;
        Stage s = (Stage) pane.getScene().getWindow();
        s.close();
        FXMLLoader fxmlLoader = new FXMLLoader(MainGame.class.getResource("main-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        Stage stage = new Stage();
        stage.setTitle("Typing Game by Ahmed & Mostafa");
        stage.setScene(scene);
        stage.show();

    }
}
package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game extends Main{
    private int rowsNum = 0;
    private int columnsNum = 0;

    private int maxMoves = -1;

    private Player player1;
    private Player player2;

    // initialize variables that will be needed for the loading function
    private Player currentPlayer;
    private int counter = 0;
    private boolean counterController = true;

    public Game(){
        getRowsGetColumnsGetMaxMoves();
        createPlayer(1);
        createPlayer(2);
    }

    public Game(int rowsNum, int columnsNum){
        this.rowsNum = rowsNum;
        this.columnsNum = columnsNum;
    }

    public Game(int rowsNum, int columnsNum, int maxMoves){
        this.rowsNum = rowsNum;
        this.columnsNum = columnsNum;
        this.maxMoves = maxMoves;
    }

    public void init(){
        player1.initField(this.rowsNum, this.columnsNum);
        player2.initField(this.rowsNum, this.columnsNum);
    }

    public void placeShips(){
        player1.placeShips(player2.getField());
        player2.placeShips(player1.getField());
    }

    public void start(){
        while (this.maxMoves != counter){
            while (true){
                try {
                    System.out.println(currentPlayer.getName() + "'s Field");
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(currentPlayer.getField().toString());
                    
                    currentPlayer.selectMove();
                    break;
                } catch (MoveIsCommandException e){
                    commandController(e);
                } catch (InvalidLocationException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            if(currentPlayer == player1) currentPlayer = player2;
            else currentPlayer = player1;

            // check if a player has won
            if(currentPlayer.hasWon()) {
                showResult(currentPlayer);
                break;
            }

            if(!counterController) this.counter ++;
            this.counterController = !this.counterController;
        }
        if(counter == this.maxMoves) showResult();
    }

    public void showResult(){
        System.out.println("Max Moves reached");
        showFieldsWithShips();
        showScores();
    }

    public void showResult(Player player){
        System.out.println(player.getName() + " won!!!");
        showFieldsWithShips();
        showScores();
    }

    /*
    *
    * Helper Functions
    *
    * */
    public void showFields(){
        System.out.println(this.player1.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player1.field.toString());

        System.out.println(this.player2.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player2.field.toString());

    }
    
    public void showFieldsWithShips(){
        System.out.println(this.player1.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player1.field.toStringWithShips());

        System.out.println(this.player2.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player2.field.toStringWithShips());

    }

    public void showScores(){
        System.out.println(this.player1.getName() + "'s Score: " + this.player1.getScore() + "\n");
        System.out.println(this.player2.getName() + "'s Score: " + this.player2.getScore());

    }

    private void createPlayer(int playerNum){
        Scanner sc = new Scanner(System.in);
        String answer;
        while (true){
            System.out.println("Do you want player " + playerNum + " to be a computer?");
            answer = sc.nextLine().toLowerCase();
            if(answer.equals("y") || answer.equals("yes")){
                if(playerNum == 1) setPlayer1(new ComputerPlayer());
                else setPlayer2(new ComputerPlayer());
                return;
            } else if(answer.equals("n") || answer.equals("no")){
                break;
            }
        }

        String tempName;
        while(true){
            System.out.println("What do you want player's " + playerNum + " name to be?");
            tempName = sc.nextLine();
            System.out.println("Is '" + tempName + "' correct?");

            answer = sc.nextLine().toLowerCase();
            if(answer.equals("y") || answer.equals("yes")) break;
        }

        if(playerNum == 1) setPlayer1(new HumanPlayer(tempName));
        else setPlayer2(new HumanPlayer(tempName));
    }

    private void getRowsGetColumnsGetMaxMoves() {
        while(true){
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Number of rows: ");
                this.rowsNum = sc.nextInt();
                System.out.print("Number of columns: ");
                this.columnsNum = sc.nextInt();

                if(rowsNum > 9 && rowsNum < 16 && columnsNum > 9 && columnsNum < 16){
                    break;
                } else {
                    System.out.println("Rows and Columns have to be between the number 10 and 15");
                }

            } catch (InputMismatchException e) {
                System.out.println("Please provide integers for number of Rows and Columns!");
            }
        }
        while (true){
            try {
                Scanner sc = new Scanner(System.in);

                System.out.println("Provide the maximum moves that are going to be played(leave blank for playing until someone wins!)");
                String tempMoves = sc.nextLine();
                if("".equals(tempMoves)) {
                    this.maxMoves = -1;
                    break;
                }

                if(Integer.parseInt(tempMoves) > 0) {
                    this.maxMoves = Integer.parseInt(tempMoves);
                    break;
                }
                System.out.println("Max Moves have to be a number more than zero!");
            } catch (NumberFormatException e){
                System.out.println("Please provide an integer for number of maxMoves!");
            }
        }
    }

    public Player getPlayerFromNum(int playerNum) throws InvalidPlayerException {
        switch (playerNum){
            case 1:
                return this.player1;
            case 2:
                return this.player2;
            default:
                throw new InvalidPlayerException();
        }
    }


    /*
    *
    * Setters - Getters
    *
    * */
    public void setPlayer1(Player player1){
        this.player1 = player1;
    }
    public Player getPlayer1(){
        return this.player1;
    }

    public void setPlayer2(Player player2){
        this.player2 = player2;
    }
    public Player getPlayer2(){
        return this.player2;
    }

    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    public void setCounter(int counter){
        this.counter = counter;
    }
    public int getCounter(){
        return this.counter;
    }

    public void setCounterController(boolean counterController){
        this.counterController = counterController;
    }
    public boolean getCounterController(){
        return this.counterController;
    }

    public void setRowsNum(int rowsNum){
        this.rowsNum = rowsNum;
    }
    public int getRowsNum(){
        return this.rowsNum;
    }

    public void setColumnsNum(int columnsNum){
        this.columnsNum = columnsNum;
    }
    public int getColumnsNum(){
        return this.columnsNum;
    }

    public void setMaxMoves(int maxMoves){
        this.maxMoves = maxMoves;
    }
    public int getMaxMoves(){
        return this.maxMoves;
    }
}

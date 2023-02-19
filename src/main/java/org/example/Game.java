package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private int rowsNum = 0;
    private int columnsNum = 0;

    private int maxMoves = -1;

    private Player player1;
    private Player player2;

    public Game(){
        getRowsAndColumns();
        createPlayer(1);
        createPlayer(2);
    }

    public Game(Player player1, Player player2){
        getRowsAndColumns();
        this.player1 = player1;
        this.player2 = player2;
    }

    public Game(int rowsNum, int columnsNum, int maxMoves, Player player1, Player player2){
        this.rowsNum = rowsNum;
        this.columnsNum = columnsNum;
        this.maxMoves = maxMoves;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void init(){
        player1.initField(this.rowsNum, this.columnsNum);
        player2.initField(this.rowsNum, this.columnsNum);
    }

    public void placeShips(){
        player1.placeShips(player2.getField());

        player2.placeShips(player1.getField());

    }

    public void play(){
        System.out.println("Player 1 Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player1.getField().toStringWithShips());

        System.out.println("Player 2 Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player2.getField().toStringWithShips());

        Player currentPlayer = player1;
        while (true){
            try {
                currentPlayer.selectMove();
                if(player1.hasWon()) {
                    showResult(player1);
                    break;
                }
                if(player2.hasWon()){
                    showResult(player2);
                    break;
                }

            } catch (InvalidLocationException e) {
                System.out.println(e.getMessage());
            }
            if(currentPlayer == player1) currentPlayer = player2;
            else currentPlayer = player1;
        }
    }
    public void showResult(Player player){
        System.out.println(player.getName() + " won: " + player.hasWon());
    }

    /*
    *
    * Helper Functions
    *
    * */

    private void createPlayer(int playerNum){
        System.out.println("Do you want player " + playerNum + " to be a computer?");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine().toLowerCase();

        if(answer.equals("y") || answer.equals("yes")){
            if(playerNum == 1) setPlayer1(new ComputerPlayer());
            else setPlayer2(new ComputerPlayer());
            return;
        }

        System.out.println("What do you want player's " + playerNum + " name to be?");
        String tempName;
        while(true){
            tempName = sc.nextLine();
            System.out.println("Is '" + tempName + "' correct?");

            answer = sc.nextLine().toLowerCase();
            if(answer.equals("y") || answer.equals("yes")) break;
        }

        if(playerNum == 1) setPlayer1(new HumanPlayer(tempName));
        else setPlayer2(new HumanPlayer(tempName));
    }

    private void getRowsAndColumns() {
        while(true){
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Number of rows: ");
                rowsNum = sc.nextInt();
                System.out.print("Number of columns: ");
                columnsNum = sc.nextInt();

                if(rowsNum > 9 && rowsNum < 16 && columnsNum > 9 && columnsNum < 16){
                    break;
                } else {
                    System.out.println("Rows and Columns have to be between the number 10 and 15");
                }

            } catch (InputMismatchException e) {
                System.out.println("Please provide numbers for number of Rows and Columns");
            }
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
    public void setPlayer2(Player player2){
        this.player2 = player2;
    }
}

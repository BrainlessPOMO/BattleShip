package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private int rowsNum = 0;
    private int columnsNum = 0;

    private int maxMoves;
    private int player1Moves;
    private int player2Moves;

    private Player player1;
    private Player player2;

    public Game(int rowsNum, int columnsNum, int maxMoves, Player player1, Player player2){
        this.rowsNum = rowsNum;
        this.columnsNum = columnsNum;
        this.maxMoves = maxMoves;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void init(){
        System.out.println("Please tell me your name");
        getRowsAndColumns();
    }

    public void placeShips(){

    }

    public void showResult(){

    }

    /*
    *
    * Helper Functions
    *
    * */

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
}

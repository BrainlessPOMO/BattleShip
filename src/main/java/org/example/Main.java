package org.example;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static int rowsNum = 0;
    static int columnsNum = 0;

    public static void main(String[] args) throws IOException {
        getRowsAndColumns();
        playerTest();
        HumanPlayer player3 = new HumanPlayer("kati");
        player3.initField(rowsNum, columnsNum);
        while (true){
            try {
                player3.selectMove();
            } catch (InvalidLocationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void playerTest(){
        HumanPlayer player1 = new HumanPlayer("Aggelos");
        player1.initField(rowsNum, columnsNum);

        HumanPlayer player2 = new HumanPlayer("Kati");
        player2.initField(rowsNum, columnsNum);

        player1.placeShips(player2.getField());
        player2.placeShips(player1.getField());

        System.out.println("Player 1 Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player1.getField().toStringWithShips());

        System.out.println("Player 2 Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player2.getField().toStringWithShips());

//        hitAllShips(player1);
        hitAllShips(player2);

        System.out.println("Player 1 Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player1.getField().toString());

        System.out.println("Player 2 Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player2.getField().toString());

        System.out.println("player1 won: " + player1.hasWon());
        System.out.println("player2 won: " + player2.hasWon());
    }

    private static void hitAllShips(Player player1){
        try {
            player1.getField().processValidMove(player1.getField().getLocation("A0"));
            player1.getField().processValidMove(player1.getField().getLocation("A1"));
            player1.getField().processValidMove(player1.getField().getLocation("A2"));
            player1.getField().processValidMove(player1.getField().getLocation("A3"));
            player1.getField().processValidMove(player1.getField().getLocation("A4"));

            player1.getField().processValidMove(player1.getField().getLocation("B0"));
            player1.getField().processValidMove(player1.getField().getLocation("B1"));
            player1.getField().processValidMove(player1.getField().getLocation("B2"));
            player1.getField().processValidMove(player1.getField().getLocation("B3"));
            player1.getField().processValidMove(player1.getField().getLocation("B4"));

            player1.getField().processValidMove(player1.getField().getLocation("C0"));
            player1.getField().processValidMove(player1.getField().getLocation("C1"));
            player1.getField().processValidMove(player1.getField().getLocation("C2"));

            player1.getField().processValidMove(player1.getField().getLocation("D0"));
            player1.getField().processValidMove(player1.getField().getLocation("D1"));
            player1.getField().processValidMove(player1.getField().getLocation("D2"));

            player1.getField().processValidMove(player1.getField().getLocation("E0"));
            player1.getField().processValidMove(player1.getField().getLocation("E1"));
            player1.getField().processValidMove(player1.getField().getLocation("E2"));

            player1.getField().processValidMove(player1.getField().getLocation("F0"));
            player1.getField().processValidMove(player1.getField().getLocation("G0"));
        } catch (InvalidLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getRowsAndColumns() {
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
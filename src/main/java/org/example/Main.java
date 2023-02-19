package org.example;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static int rowsNum = 0;
    static int columnsNum = 0;

    public static void main(String[] args) throws IOException {
//        getRowsAndColumns();
//        playerTest();

//        HumanPlayer player1 = new HumanPlayer("kati");
//        ComputerPlayer player2 = new ComputerPlayer();
        Game game = new Game();

        game.init();
        game.placeShips();

        game.play(game.getPlayer1());

    }

//    private static void hitAllShips(Player player1){
//        try {
//            player1.getField().processValidMove(player1.getField().getLocation("A0"));
//            player1.getField().processValidMove(player1.getField().getLocation("A1"));
//            player1.getField().processValidMove(player1.getField().getLocation("A2"));
//            player1.getField().processValidMove(player1.getField().getLocation("A3"));
//            player1.getField().processValidMove(player1.getField().getLocation("A4"));
//
//            player1.getField().processValidMove(player1.getField().getLocation("B0"));
//            player1.getField().processValidMove(player1.getField().getLocation("B1"));
//            player1.getField().processValidMove(player1.getField().getLocation("B2"));
//            player1.getField().processValidMove(player1.getField().getLocation("B3"));
//            player1.getField().processValidMove(player1.getField().getLocation("B4"));
//
//            player1.getField().processValidMove(player1.getField().getLocation("C0"));
//            player1.getField().processValidMove(player1.getField().getLocation("C1"));
//            player1.getField().processValidMove(player1.getField().getLocation("C2"));
//
//            player1.getField().processValidMove(player1.getField().getLocation("D0"));
//            player1.getField().processValidMove(player1.getField().getLocation("D1"));
//            player1.getField().processValidMove(player1.getField().getLocation("D2"));
//
//            player1.getField().processValidMove(player1.getField().getLocation("E0"));
//            player1.getField().processValidMove(player1.getField().getLocation("E1"));
//            player1.getField().processValidMove(player1.getField().getLocation("E2"));
//
//            player1.getField().processValidMove(player1.getField().getLocation("F0"));
//            player1.getField().processValidMove(player1.getField().getLocation("G0"));
//        } catch (InvalidLocationException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
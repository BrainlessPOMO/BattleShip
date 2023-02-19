package org.example;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static Game game;

    public static void main(String[] args){

        game = new Game();
        game.init();
        game.placeShips();

        game.play(game.getPlayer1());

//        Game game = new Game(15, 15, 15, new HumanPlayer("kati"), new ComputerPlayer());
//        game.init();

//        HumanPlayer player1 = new HumanPlayer("name");
//        HumanPlayer player2 = new HumanPlayer("name");
//        game = new Game(11, 11, 11, player1, player2);
//        game.init();
//
//        AircraftCarrier ac = new AircraftCarrier(player1.getField());
//        ac.setDir(ShipDirection.fromString("v"));
//        ac.setStartingLocation(player1.getField().getLocation(row, column));
//        if(ShipDirection.fromString("v") == ShipDirection.VERTICAL){
//            for(int i=0; i<ac.getLength(); i++) {
//                player1.getField().getLocation(row + i, col).setShip(ac);
//            }
//        }
//        else {
//            for(int i=0; i<ac.getLength(); i++) {
//                player1.getField().getLocation(row, col + i).setShip(ac);
//            }
//        }
    }

    protected void commandController(MoveIsCommandException e) {
        switch (e.getCommand()){
            case EXIT :
                if(exitCommandController()) System.exit(-1);
                break;
            case LOAD:
                loadCommandController();
                break;
            case SAVE:
                saveCommandController();
                break;
            default:
                helpCommandController();
        }
    }

    private boolean exitCommandController(){
        System.out.println("Are you sure you want to exit the program? (ex: y=yes, n=no) \nAny game that is not saved will be lost!");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine().toLowerCase();

        if(answer.equals("y") || answer.equals("yes")){
            return true;
        }
        return false;
    }

    private void saveCommandController(){
        System.out.println("Inside saveCommandController");
    }
    private void loadCommandController(){
        System.out.println("Inside loadCommandController");
    }

    private void helpCommandController(){
        System.out.println("-----------------------------------------");
        for(Command cmd : Command.values()){
            System.out.println(cmd.commandString + " : " + cmd.helpText);
        }
        System.out.println("-----------------------------------------");
        System.out.println();
    }
}
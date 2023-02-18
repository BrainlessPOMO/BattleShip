package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class HumanPlayer extends Player{
    public HumanPlayer(String name) {
        super(name);
    }

    public void selectMove() throws InvalidLocationException{
        System.out.println("Please give a command to be executed, or make a move");
        Scanner sc = new Scanner(System.in);

        String decision = sc.nextLine();
        Boolean isCommand = true;
        Boolean isMove = true;

        try{
            Command command = Command.fromString(decision);
            System.out.println("Chose: " + command);
            return;
        } catch (InputMismatchException e){
            isCommand = false;
        }

        try{
            Location newLoc = field.getLocation(decision);
            System.out.println("Chose move: " + newLoc.getRow() + "   " + newLoc.getCol());
            return;
        }catch (InvalidLocationException e){
            isMove = false;
        }
        
        if(!isCommand && !isMove){
            throw new InvalidLocationException("No known commands or moves! Please provide a command or a move, or type help to see all the available commands");
        }
    }

    public void placeShips(Field otherField){
        if(wantRandomized()){
            super.placeShips(otherField);
            return;
        }

        setNewShip(otherField, "Aircraft Carrier");
        setNewShip(otherField, "Aircraft Carrier");
        setNewShip(otherField, "Destroyer");
        setNewShip(otherField, "Destroyer");
        setNewShip(otherField, "Destroyer");
        setNewShip(otherField, "Submarine");
        setNewShip(otherField, "Submarine");

    }

    /*
     *
     * Helper Functions
     *
     * */
    private boolean wantRandomized() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want your ships to be placed randomly?(yes, no): ");

        String random = sc.nextLine().toLowerCase();
        switch (random){
            case "y", "yes":
                return true;
            default:
                return false;
        }
    }

    private void setNewShip(Field otherField, String typeOfShip){
        Scanner sc = new Scanner(System.in);

        try {
            otherField.placeShip(new AircraftCarrier(otherField, ShipDirection.HORIZONTAL, otherField.getLocation("A0")), false);
            otherField.placeShip(new AircraftCarrier(otherField, ShipDirection.HORIZONTAL, otherField.getLocation("B0")), false);
            otherField.placeShip(new Destroyer(otherField, ShipDirection.HORIZONTAL, otherField.getLocation("C0")), false);
            otherField.placeShip(new Destroyer(otherField, ShipDirection.HORIZONTAL, otherField.getLocation("D0")), false);
            otherField.placeShip(new Destroyer(otherField, ShipDirection.HORIZONTAL, otherField.getLocation("E0")), false);
            otherField.placeShip(new Submarine(otherField, ShipDirection.HORIZONTAL, otherField.getLocation("F0")), false);
            otherField.placeShip(new Submarine(otherField, ShipDirection.HORIZONTAL, otherField.getLocation("G0")), false);
        } catch (InvalidLocationException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e){
            System.out.println(e.getMessage());
        }

//        while(true){
//            System.out.println("Please where you want to put your "+ typeOfShip +"(ex: A3): ");
//            String tempLoc = sc.nextLine();
//            System.out.println("Please tell me the direction of your your first AircraftCarrier(ex: h=horizontal, v=vertical): ");
//            String tempDir = sc.nextLine();
//
//            try {
//                switch (typeOfShip){
//                    case "Aircraft Carrier":
//                        otherField.placeShip(new AircraftCarrier(otherField, ShipDirection.fromString(tempDir), otherField.getLocation(tempLoc)), false);
//                        break;
//                    case "Destroyer":
//                        otherField.placeShip(new Destroyer(otherField, ShipDirection.fromString(tempDir), otherField.getLocation(tempLoc)), false);
//                        break;
//                    default:
//                        otherField.placeShip(new Submarine(otherField, ShipDirection.fromString(tempDir), otherField.getLocation(tempLoc)), false);
//                        break;
//                }
//            } catch (InvalidLocationException e){
//                System.out.println(e.getMessage());
//            } catch (InputMismatchException e){
//                System.out.println(e.getMessage());
//            } catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//
//            break;
//        }
    }
}

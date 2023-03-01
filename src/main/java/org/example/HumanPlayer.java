package org.example;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HumanPlayer extends Player{
    public HumanPlayer(String name) {
        super(name);
    }

    public void selectMove() throws InvalidLocationException, Exception{
        System.out.println(this.getName() + ", please give a command to be executed, or make a move");
        Scanner sc = new Scanner(System.in);

        String decision = sc.nextLine();
        Boolean isMove = true;

        try{
            Command command = Command.fromString(decision);
            throw new MoveIsCommandException(command);
        } catch (InputMismatchException e){ }

        try{
            if(this.field.getLocation(decision).isMarked()) {
                throw new Exception("This location is already marked!");
            }
            if(this.field.processValidMove(field.getLocation(decision))){
                super.addToScore(this.field.getLocation(decision).getShip().getPoints());
            }
            return;
        } catch (InvalidLocationException e){
            isMove = false;
        }

        if(!isMove) throw new InvalidLocationException("No known commands or moves! Please provide a command or a move, or type 'help' to see all the available commands");

    }

    @Override
    public void placeShips(Field otherField){
        if(wantRandomizedShipPlaces()){
            super.placeShips(otherField);
            return;
        }

        ArrayList<String> totalShips = new ArrayList<>() {
            {
                add("Aircraft Carrier");
                add("Aircraft Carrier");
                add("Destroyer");
                add("Destroyer");
                add("Destroyer");
                add("Submarine");
                add("Submarine");
            }
        };

        for(String ship : totalShips){
            placeNewShip(otherField, ship);
            System.out.println(otherField.toStringWithShips());
        }

    }

    /*
     *
     * Helper Functions
     *
     * */
    private boolean wantRandomizedShipPlaces() {
        Scanner sc = new Scanner(System.in);
        System.out.println( this.getName() + ", do you want your ships to be placed randomly?(yes, no): ");

        String random = sc.nextLine().toLowerCase();
        switch (random){
            case "y", "yes":
                return true;
            default:
                return false;
        }
    }

    private void placeNewShip(Field otherField, String typeOfShip){
        Scanner sc = new Scanner(System.in);
        boolean isplaced = false;

        while(!isplaced){
            System.out.println(this.getName() + ", please tell me where you want to put your "+ typeOfShip +"(ex: A3): ");
            String tempLoc = sc.nextLine();
            System.out.println(this.getName() + ", please tell me the direction of your your first AircraftCarrier(ex: h=horizontal, v=vertical): ");
            String tempDir = sc.nextLine();

            try {
                switch (typeOfShip){
                    case "Aircraft Carrier":
                        if(otherField.placeShip(new AircraftCarrier(otherField, ShipDirection.fromString(tempDir), otherField.getLocation(tempLoc)), false)){
                            isplaced = true;
                            break;
                        }
                        else throw new InvalidLocationException("You cannot place your ship there");
                    case "Destroyer":
                        if(otherField.placeShip(new Destroyer(otherField, ShipDirection.fromString(tempDir), otherField.getLocation(tempLoc)), false)){
                            isplaced = true;
                            break;
                        }
                        else throw new InvalidLocationException("You cannot place your ship there");
                    default:
                        if(otherField.placeShip(new Submarine(otherField, ShipDirection.fromString(tempDir), otherField.getLocation(tempLoc)), false)){
                            isplaced = true;
                            break;
                        }
                        else throw new InvalidLocationException("You cannot place your ship there");
                }
            } catch (InvalidLocationException e){
                System.out.println(e.getMessage());
            } catch (InputMismatchException e){
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void placeShipsInSpecificPlaces(Field otherField){
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
    }

}

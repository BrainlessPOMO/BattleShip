package org.example;

import java.util.ArrayList;
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
            commandController(command);
            return;
        } catch (InputMismatchException e){
            isCommand = false;
        }

        try{
            if(this.field.getLocation(decision).isMarked()) {
                System.out.println("This location is already marked!");
                return;
            }
            this.field.processValidMove(field.getLocation(decision));

            System.out.println(this.field.toString());
            return;
        }catch (InvalidLocationException e){
            isMove = false;
        }
        
        if(!isCommand && !isMove){
            throw new InvalidLocationException("No known commands or moves! Please provide a command or a move, or type help to see all the available commands");
        }
    }

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
        System.out.println("Do you want your ships to be placed randomly?(yes, no): ");

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
            System.out.println("Please tell me where you want to put your "+ typeOfShip +"(ex: A3): ");
            String tempLoc = sc.nextLine();
            System.out.println("Please tell me the direction of your your first AircraftCarrier(ex: h=horizontal, v=vertical): ");
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

    private void commandController(Command command){
        switch (command){
            case EXIT :
                System.out.println("Are you sure you want to exit the program? (ex: y=yes, n=no) \nAny game that is not saved will be lost!");
                Scanner sc = new Scanner(System.in);
                String answer = sc.nextLine().toLowerCase();

                if(answer.equals("y") || answer.equals("yes")){
                    super.exitCommandController();
                }
                break;
            case LOAD:
                super.loadCommandController();
                break;
            case SAVE:
                super.saveCommandController();
                break;
            default:
                System.out.println("-----------------------------------------");
                for(Command cmd : Command.values()){
                    System.out.println(cmd.commandString + " : " + cmd.helpText);
                }
                System.out.println("-----------------------------------------");
                System.out.println();
        }
    }

}

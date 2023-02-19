package org.example;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Player {
    private String name;
    private int score;
    protected Field field;
    private ArrayList<Ship> ships = new ArrayList<>();

    public Player(String name){
        this.name = name;
    }

    public void initField(int row, int column){
        this.field = new Field(row, column);
    }

    public void placeShips(Field otherField) {
        // this func places Ships randomly on the field
        otherField.placeShipRandomly(new AircraftCarrier(otherField), 10, false);
        otherField.placeShipRandomly(new AircraftCarrier(otherField), 10, false);
        otherField.placeShipRandomly(new Destroyer(otherField), 10, false);
        otherField.placeShipRandomly(new Destroyer(otherField), 10, false);
        otherField.placeShipRandomly(new Destroyer(otherField), 10, false);
        otherField.placeShipRandomly(new Submarine(otherField), 10, false);
        otherField.placeShipRandomly(new Submarine(otherField), 10, false);
    }

    public boolean hasWon(){
        for(ArrayList<Location> row : this.field.getLocations() ){
            for(Location loc : row){
                if(loc.getShip() != null){
                    if(!loc.getShip().isSinking()) return false;
                }
            }
        }

        return true;
    }

    public void selectMove() throws InvalidLocationException{
        // TODO
        // SELIDA 5
    }

    /*
    *
    * Helper Functions
    *
    * */
    protected void exitCommandController(){
        System.exit(-1);
    }

    protected void loadCommandController(){
        System.out.println("Inside loadCommandController");
    }

    protected void saveCommandController(){
        System.out.println("Inside saveCommandController");
    }

    /*
    *
    * Setters - Getters
    *
    * */
    public Field getField(){
        return this.field;
    }

    public String getName(){ return this.name; }
}

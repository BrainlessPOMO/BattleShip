package org.example;

import java.util.ArrayList;
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
        otherField.placeShipRandomly(new AircraftCarrier(otherField), 0, false);
        otherField.placeShipRandomly(new AircraftCarrier(otherField), 0, false);
        otherField.placeShipRandomly(new Destroyer(otherField), 0, false);
        otherField.placeShipRandomly(new Destroyer(otherField), 0, false);
        otherField.placeShipRandomly(new Destroyer(otherField), 0, false);
        otherField.placeShipRandomly(new Submarine(otherField), 0, false);
        otherField.placeShipRandomly(new Submarine(otherField), 0, false);
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

    public void selectMove() throws InvalidLocationException, Exception{ }

    public void addToScore(int points) {
        setScore(getScore() + points);
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

    public int getScore(){ return this.score; }

    public void setScore(int score){ this.score = score; }

}

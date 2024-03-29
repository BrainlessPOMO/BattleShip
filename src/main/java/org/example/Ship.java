package org.example;

public class Ship {
    private final int length;
    private final int points;
    private final char letter;
    private final Field field;
    private Location start;
    private ShipDirection dir;
    private int hitCounter = 0;

    public Ship(int length, int points, char letter, Field field) {
        this.length = length;
        this.points = points;
        this.letter = letter;
        this.field = field;
    }

    public Ship(int length, int points, char letter, Field field, Location start, ShipDirection dir) {
        this.length = length;
        this.points = points;
        this.letter = letter;
        this.field = field;
        this.start = start;
        this.dir = dir;
    }

    @Override
    public String toString(){
        return (
                "Length:" + this.getLength() +
                "\tPoints:" + this.getPoints() +
                "\tLetter:" + this.getLetter() +
                "\tCol:" + this.getStartingLocation().getCol() +
                "\tRow:" + this.getStartingLocation().getRow() +
                "\tDir:" + this.getDir()
        );
    }

    public void hit(){
        if(this.hitCounter < this.length){
            this.hitCounter ++;
            System.out.println("A ship was hit");
        }
    }

    public Boolean isHit(){
        return this.hitCounter > 0;
    }
    public Boolean isSinking(){
        // if ship is sinking then his whole length will be hit
        // so our hitCounter will be the same as the length of the ship
        return this.hitCounter == this.length;
    }
    public String getHitMessage(){
        if(this.isHit()){
         return "A Ship is hit";
        }
        return "Ship is not hit";
    }
    public String getSinkMessage(String typeOfShip){
        if(isSinking()){
            return typeOfShip + " is sinking";
        }
        return typeOfShip + " is not sinking";
    }

    public Boolean threaten(){
        return false;
    }
    /*
    *
    * Setters - Getters
    *
    * */
    public int getLength() {
        return this.length;
    }
    public int getPoints() {
        return this.points;
    }
    public char getLetter() {
        return this.letter;
    }
    public Field getField() {
        return this.field;
    }
    public Location getStartingLocation(){
        return this.start;
    }
    public void setStartingLocation(Location start){
        this.start = start;
    }
    public ShipDirection getDir(){
        return this.dir;
    }
    public void setDir(ShipDirection dir){
        this.dir = dir;
    }

    // function for saving type of Ship on a file
    public String getClassString(){ return "ship"; }
}

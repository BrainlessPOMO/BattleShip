package org.example;

public class Location {
    private int row;
    private int col;
    private Ship ship;
    private boolean marked;

    Location(int row, int col, Ship ship, boolean marked){
        this.row = row;
        this.col = col;
        this.ship = ship;
        this.marked = marked;
    }

    Location(int row, int col, boolean marked){
        this.row = row;
        this.col = col;
        this.marked = marked;
    }

    public void mark(){
        this.setMarked(true);
        if(this.ship != null) this.ship.hit();
    }

    public Boolean isEmpty(){
        if(ship == null) return true;
        return false;
    }
    public Boolean isHit(){
        if(this.ship.isHit()) return true;
        return false;
    }

    /*
    *
    * Helper Functions
    * */

    public char convertRow(int row){
        switch (row){
            case 1:
                return 'B';
            case 2:
                return 'C';
            case 3:
                return 'D';
            case 4:
                return 'E';
            case 5:
                return 'F';
            case 6:
                return 'G';
            case 7:
                return 'H';
            case 8:
                return 'I';
            case 9:
                return 'J';
            case 10:
                return 'K';
            case 11:
                return 'L';
            case 12:
                return 'M';
            case 13:
                return 'N';
            case 14:
                return 'O';
            default:
                return 'A';
        }
    }
    /*
    *
    * Setters - Getters
    *
    * */
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public Ship getShip() {
        return ship;
    }
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public String printLoc(){
        if(this.isMarked()){
            return "col: " + this.col + "  row: " + this.row + "   x";
        }
        return "col: " + this.col + "  row: " + this.row + "   .";
    }

    public String toString(){
        if(!this.isMarked()){
            // location is not marked
            return ".";
        }
        if(this.ship == null){
            // location is marked and has no ship in it
            return "o";
        }
        if(!this.ship.isSinking()){
            // location is marked and has ship in it and the ship is not sinking
            return "x";
        }
        
        // location is marked and has ship in it and the ship is sinking
        return "x" + this.ship.getLetter();
    }

    public String toStringWithShip(){
        
        if(!this.isMarked()){
            // location is not marked
            if (this.ship != null){
                // location is not marked and has a ship
                return String.valueOf(this.ship.getLetter());
            }
            
            // location is not marked and has no ship in it
            return ".";
        }
        if(this.ship == null){
            // location is marked and has no ship in it
            return "o";
        }

        // location is marked and has ship in it
        return "x" + this.ship.getLetter();
    }
}

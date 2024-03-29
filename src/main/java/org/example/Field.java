package org.example;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Field {
    private int numRows;
    private int numCols;
    private ArrayList<ArrayList<Location>> locations = new ArrayList<>();

    private ArrayList<Ship> ships = new ArrayList<>();

    public Field(int numRows, int numCols){
        this.numRows = numRows;
        this.numCols = numCols;

        // initialize 2-D ArrayList
        for (int i=0; i<numRows; i++) locations.add(new ArrayList<>());

        // add locations on the ArrayList
        for(int i=0; i<numRows; i++){
            for (int j=0; j<numCols; j++){
                locations.get(i).add(new Location(i, j, false));
            }
        }
    }

    public Location getLocation(int row, int column) {
        return locations.get(row).get(column);
    }

    // get the location of a picked tile
    public Location getLocation(String locString) throws InvalidLocationException{
        Pattern pattern = Pattern.compile("^([A-Za-z][0-9]{1,2})$");
        Matcher matcher = pattern.matcher(locString);
        if(!matcher.matches()){
            throw new InvalidLocationException("The position you requested is not correct");
        }

        int row = convertRow(locString.charAt(0));
        locString = locString.replaceAll("[^\\d.]", "");
        int column = Integer.parseInt(locString);
        if(row > numRows-1 || row < 0){
            throw new InvalidLocationException("Invalid row");
        }
        if(column > numCols-1 || column < 0){
            throw new InvalidLocationException("Invalid column");
        }

        return locations.get(row).get(column);
    }

    public boolean placeShipRandomly(Ship s, int maxTries, boolean checkMarked){
        Random rand = new Random();
        int tries;

        if(maxTries == 0) tries = 1;
        else tries = 0;

        ArrayList<Location> shipOldLocations = new ArrayList<>();
        // if checkMarked is true then we should check also for the past Locations of the ship
        if(checkMarked){
            shipOldLocations = getOldLocationsOfAShip(s);
        }

        while(tries != maxTries){
            int tempRow = rand.nextInt(numRows-1);
            int tempCol = rand.nextInt(numCols-1);
            ShipDirection tempDirection = getDirection(rand.nextInt(2));

            // check if temporary locations are occupied
            boolean isOccupied = isOccupied(s, tempDirection, tempRow, tempCol);

            // check if temporary locations are in the old locations
            boolean isChecked = false;
            if(checkMarked) isChecked = isOccupied(s, tempDirection, tempRow, tempCol, shipOldLocations);

            if(!isOccupied && !isChecked){
                if(checkMarked) removeShip(s);
                setShipOnField(s, tempDirection, tempRow, tempCol, s.getLength());
                return true;
            }
            tries ++;
        }
        return false;
    }

    public boolean placeShip(Ship s, boolean checkMarked){
        boolean isOccupied = isOccupied(s, s.getDir(), s.getStartingLocation().getRow(), s.getStartingLocation().getCol());
        boolean isChecked = false;

        if(checkMarked){
            isChecked = isOccupied(s, s.getDir(), s.getStartingLocation().getRow(), s.getStartingLocation().getCol(), getOldLocationsOfAShip(s));
        }

        // check if the locations are occupied
        if(!isOccupied && !isChecked){
            setShipOnField(s, s.getDir(), s.getStartingLocation().getRow(), s.getStartingLocation().getCol(), s.getLength());
            return true;
        }
        return false;
    }

    public void removeShip(Ship s){
        setShipOnField(null, s.getDir(), s.getStartingLocation().getRow(), s.getStartingLocation().getCol(), s.getLength());
    }

    // this function returns true if a ship was sunk so that it's score can be added to player's score
    public boolean processValidMove(Location moveLoc){
        moveLoc.setMarked(true);
        if(moveLoc.getShip() != null) {
            moveLoc.getShip().hit();
            if(moveLoc.getShip().isSinking()){
                System.out.println("A Ship sunk");
                return true;
            }
        }
        for(int i=-4; i<=4; i++){
            try{
                if(i != 0) shipThreat(locations.get(moveLoc.getRow() + i).get(moveLoc.getCol()), moveLoc);
            } catch (Exception e) {  }
        }
        for(int i=-4; i<=4; i++){
            try{
                if(i != 0) shipThreat(locations.get(moveLoc.getRow()).get(moveLoc.getCol() + i), moveLoc);
            } catch (Exception e) { }
        }
        return false;
    }

    @Override
    public String toString(){
        StringBuilder tempString = new StringBuilder();
        // print 2-D ArrayList
        tempString.append("    ");
        // print the columns numbers
        for(int i=0; i<locations.get(0).size(); i++){
            tempString.append(getNums(i));
        }
        tempString.append("\n    ");
        for (Location loc : locations.get(0)) {
            tempString.append("   _");
        }
        
        tempString.append("\n");
        // print the rows letters
        for(int i=0; i<locations.size(); i++){
            tempString.append(convertRow(i) + "  |");
            for(int j=0; j<locations.get(i).size(); j++){
                if(locations.get(i).get(j).toString().length() == 1) tempString.append("   " + locations.get(i).get(j).toString());
                if(locations.get(i).get(j).toString().length() == 2) tempString.append("  " + locations.get(i).get(j).toString());
            }
            tempString.append("\n");
        }
        return tempString.toString();
    }

    public String toStringWithShips(){
        StringBuilder tempString = new StringBuilder();
        // print 2-D ArrayList
        tempString.append("    ");
        // print the columns numbers
        for(int i=0; i<locations.get(0).size(); i++){
            tempString.append(getNums(i));
        }
        tempString.append("\n    ");
        for(int i=0; i<locations.get(0).size(); i++){
            tempString.append("   _");
        }
        
        tempString.append("\n");
        // print the rows letters
        for(int i=0; i<locations.size(); i++){
            tempString.append(convertRow(i) + "  |");
            for(int j=0; j<locations.get(i).size(); j++){
                tempString.append("   " + locations.get(i).get(j).toStringWithShip());
            }
            tempString.append("\n");
        }
        return tempString.toString();
    }

    /*
    *
    * HELPER FUNCTIONS
    *
    * */
    private ArrayList<Location> getOldLocationsOfAShip(Ship s){
        ArrayList<Location> shipOldLocations = new ArrayList<>();
        if(s.getDir() == ShipDirection.VERTICAL){
            for(int i=0; i<s.getLength(); i++) {
                shipOldLocations.add(getLocation(s.getStartingLocation().getRow() + i, s.getStartingLocation().getCol()));
            }
        } else {
            for (int i = 0; i < s.getLength(); i++) {
                shipOldLocations.add(getLocation(s.getStartingLocation().getRow(), s.getStartingLocation().getCol() + i));
            }
        }
        return shipOldLocations;
    }

    public void setShipOnField(Ship s, ShipDirection dir, int row, int col, int length){
        if(s != null){
            s.setDir(dir);
            s.setStartingLocation(getLocation(row, col));
        }
        if(dir == ShipDirection.VERTICAL){
            for(int i=0; i<length; i++) {
                getLocation(row + i, col).setShip(s);
            }

            //
            // initialize or update new ship in ships ArrayList
            //
            if(ships.indexOf(s) != -1){
                ships.set(ships.indexOf(s), s);
            }
            else ships.add(s);
        }
        else {
            for(int i=0; i<length; i++) {
                getLocation(row, col + i).setShip(s);
            }
            //
            // initialize or update new ship in ships ArrayList
            //
            if(ships.indexOf(s) != -1){
                ships.set(ships.indexOf(s), s);
            }
            else ships.add(s);
        }
    }

    // function to check if ship's temporary location is occupied
    private boolean isOccupied(Ship s, ShipDirection dir, int row, int column) {
        if (dir == ShipDirection.VERTICAL){
            for(int i=0; i<s.getLength(); i++){
                if(row + i > numRows-1) {
                    return true;
                }else{
                    if(!getLocation(row + i, column).isEmpty()) return true;
                    if(getLocation(row + i, column).isMarked()) return true;
                }
            }
        } else {
            for(int i=0; i<s.getLength(); i++){
                if(column + i > numCols-1){
                    return true;
                } else {
                    if(!getLocation(row,column + i).isEmpty()) return true;
                    if(getLocation(row,column + i).isMarked()) return true;
                }
            }
        }
        return false;
    }

    // function to check if ship's temporary location was occupied, before
    private boolean isOccupied(Ship s, ShipDirection dir, int row, int column, ArrayList<Location> oldShipLocations) {
        if (dir == ShipDirection.VERTICAL){
            for(int i=0; i<s.getLength(); i++){
                if(row + i > numRows-1) {
                    return true;
                }else{
                    for(int j=0; j<oldShipLocations.size(); j++){
                        if(oldShipLocations.get(j).getRow() == getLocation(row + i, column).getRow() && oldShipLocations.get(j).getCol() == getLocation(row + i, column).getCol()) return true;
                    }
                }
            }
        } else {
            for(int i=0; i<s.getLength(); i++){
                if(column + i > numCols-1){
                    return true;
                } else {
                    for(int j=0; j<oldShipLocations.size(); j++){
                        if(oldShipLocations.get(j).getRow() == getLocation(row, column + i).getRow() && oldShipLocations.get(j).getCol() == getLocation(row, column + i).getCol()) return true;
                    }
                }
            }
        }
        return false;
    }

    public ShipDirection getDirection(int num){
        if(num == 0) return ShipDirection.HORIZONTAL;

        return ShipDirection.VERTICAL;
    }

    // function to get correct spaces for the printing of the Field
    public String getNums(int number) {
        //if(number%10 > 10) return
        return switch (number) {
            case 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 -> "  " + number;
            default -> "   " + number;
        };
    }
    public char convertRow(int number) {
        switch (number){
            case 0:
                return 'A';
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
                return '0';
        }
    }
    public int convertRow(char row)  {
        switch (row){
            case 'A', 'a':
                return 0;
            case 'B', 'b':
                return 1;
            case 'C', 'c':
                return 2;
            case 'D', 'd':
                return 3;
            case 'E', 'e':
                return 4;
            case 'F', 'f':
                return 5;
            case 'G', 'g':
                return 6;
            case 'H', 'h':
                return 7;
            case 'I', 'i':
                return 8;
            case 'J', 'j':
                return 9;
            case 'K', 'k':
                return 10;
            case 'L', 'l':
                return 11;
            case 'M', 'm':
                return 12;
            case 'N', 'n':
                return 13;
            case 'O', 'o':
                return 14;
            default:
                return -1;
        }
    }

    private void shipThreat(Location threatingLocation, Location hitLocation){

        //
        // if the threatened location is marked 
        // or does not have a ship 
        // or has the same ship as the one hit
        // or has already been hit
        //
        // Then we won't try to change its position
        //

        if(threatingLocation.isMarked() || threatingLocation.getShip() == null || threatingLocation.getShip() == hitLocation.getShip()) return;
        if(threatingLocation.getShip().isHit()) return;
        

        if(threatingLocation.getShip().getClass() == AircraftCarrier.class) return;

        if(threatingLocation.getShip().getClass() == Destroyer.class) {
            if(placeShipRandomly(threatingLocation.getShip(), 1, true)) System.out.println("A Destroyer changed position!");
            else System.out.println("A Destroyer tried to change it's position but did not manage");
        }

        if(threatingLocation.getShip().getClass() == Submarine.class){
            if(placeShipRandomly(threatingLocation.getShip(), 0, true)) System.out.println("A Sub changed position!");
        }

    }
    /*
    *
    * Setters - Getters
    *
    * */

    public ArrayList<ArrayList<Location>> getLocations(){
        return this.locations;
    }

    public int getNumRows(){
        return this.numRows;
    }

    public int getNumCols(){
        return this.numCols;
    }

    public ArrayList<Ship> getShips(){
        return this.ships;
    }
}

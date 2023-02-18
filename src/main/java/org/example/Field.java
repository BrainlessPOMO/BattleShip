package org.example;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Field {
    private int numRows;
    private int numCols;
    private ArrayList<ArrayList<Location>> locations = new ArrayList<>();
    Player player;

    public Field(int numRows, int numCols){
        this.numRows = numRows;
        this.numCols = numCols;

        // initialize 2-D ArrayList
        for (int i=0; i<numRows; i++) locations.add(new ArrayList<Location>());

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
        int tries = 0;

        ArrayList<Location> shipOldLocations = new ArrayList<>();
        // if checkMarked is true then we should check also for the past Locations of the ship
        if(checkMarked){
            shipOldLocations = getOldLocationsOfAShip(s);
        }

        while(tries < maxTries){
            int tempRow = rand.nextInt(numRows-1);
            int tempCol = rand.nextInt(numCols-1);
            ShipDirection tempDirection = getDirection(rand.nextInt(2));

            // check if temporary locations are occupied
            boolean isOccupied = isOccupied(s, tempDirection, tempRow, tempCol);

            // check if temporary locations are int the old locations
            boolean isChecked = false;
            if(checkMarked) isChecked = isOccupied(s, tempDirection, tempRow, tempCol, shipOldLocations);

            if(!isOccupied && !isChecked){
                setShipOnField(s, tempDirection, tempRow, tempCol, s.getLength());
                System.out.println("Ship places successfully");
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
            System.out.println("Ship successfully placed!");
            return true;
        }
        return false;
    }

    public void removeShip(Ship s){
        setShipOnField(null, s.getDir(), s.getStartingLocation().getRow(), s.getStartingLocation().getCol(), s.getLength());
    }

    public void processValidMove(Location moveLoc){
        moveLoc.setMarked(true);
        if(moveLoc.getShip() != null) {
            moveLoc.getShip().hit();
            if(moveLoc.getShip().isSinking()){
                System.out.println("A Ship sunk");
            }
        }
    }

    public String toString(){
        StringBuilder tempString = new StringBuilder();
        // print 2-D ArrayList
        tempString.append("    ");
        for(int i=0; i<locations.get(0).size(); i++){
            tempString.append(getNums(i));
        }
        tempString.append("\n    ");
        for(int i=0; i<locations.get(0).size(); i++){
            tempString.append("   _");
        }
        tempString.append("\n");
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
        for(int i=0; i<locations.get(0).size(); i++){
            tempString.append(getNums(i));
        }
        tempString.append("\n    ");
        for(int i=0; i<locations.get(0).size(); i++){
            tempString.append("   _");
        }
        tempString.append("\n");
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

    private void setShipOnField(Ship s, ShipDirection dir, int row, int col, int length){
        if(s != null){
            s.setDir(dir);
            s.setStartingocation(getLocation(row, col));
        }
        if(dir == ShipDirection.VERTICAL){
            for(int i=0; i<length; i++) {
                getLocation(row + i, col).setShip(s);
            }
        }
        else {
            for(int i=0; i<length; i++) {
                getLocation(row, col + i).setShip(s);
            }
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
        switch (number){
            case 10,11,12,13,14,15,16,17,18,19:
                return "  " + number;
            default:
                return "   " + number;
        }
    }
    public char convertRow(int number) {
        switch (number){
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
    public int convertRow(char row)  {
        switch (row){
            case 'B':
                return 1;
            case 'C':
                return 2;
            case 'D':
                return 3;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 6;
            case 'H':
                return 7;
            case 'I':
                return 8;
            case 'J':
                return 9;
            case 'K':
                return 10;
            case 'L':
                return 11;
            case 'M':
                return 12;
            case 'N':
                return 13;
            case 'O':
                return 14;
            default:
                return 0;
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
}

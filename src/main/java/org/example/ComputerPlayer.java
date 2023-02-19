package org.example;

import java.util.Random;

public class ComputerPlayer extends Player{
    public ComputerPlayer() {
        super("Computer");
    }

    public void selectMove(){
        Random rd = new Random();

        int targetedRow = rd.nextInt(field.getNumRows()-1);
        int targetedCol = rd.nextInt(field.getNumCols()-1);

        Location choseLoc = field.getLocation(targetedRow, targetedCol);
        System.out.println("Chose move: " + choseLoc.getRow() + "   " + choseLoc.getCol());
    }

    public void placeShips(Field otherField){
        super.placeShips(otherField);
    }

    /*
    *
    * Helper Functions
    *
    * */

    public ShipDirection getDirection(int num){
        if(num == 0) return ShipDirection.HORIZONTAL;
        return ShipDirection.VERTICAL;
    }
}

package org.example;

import java.util.Random;

public class ComputerPlayer extends Player{

    public ComputerPlayer() {
        super("Computer");
    }

    @Override
    public void selectMove(){
        while(true){
            Random rd = new Random();

            int targetedRow = rd.nextInt(field.getNumRows()-1);
            int targetedCol = rd.nextInt(field.getNumCols()-1);

            // check if the random location has already been picked
            if(!this.field.getLocation(targetedRow, targetedCol).isMarked()){
                if(this.field.processValidMove(field.getLocation(targetedRow, targetedCol))){
                    super.addToScore(this.field.getLocation(targetedRow, targetedCol).getShip().getPoints());
                }
                break;
            }

        }
    }

    @Override
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

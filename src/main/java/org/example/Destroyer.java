package org.example;

public class Destroyer extends Ship{
    public Destroyer(Field field){
        super(3, 2, 'D', field);
    }
    public Destroyer(Field field, ShipDirection dir, Location start) {
        super(3, 2, 'D', field, start, dir);
    }

    public String getSinkMessage(){
        return super.getSinkMessage("Destroyer");
    }

    // function for saving type of Ship on a file
    @Override
    public String getClassString(){ return "d"; }

}

package org.example;

public class Submarine extends Ship{
    public Submarine(Field field){
        super(1, 3, 'S', field);
    }

    public Submarine(Field field, ShipDirection dir, Location start) {
        super(1, 3, 'S', field, start, dir);
    }

    public String getSinkMessage(){
        return super.getSinkMessage("Submarine");
    }

    // function for saving type of Ship on a file
    public String getClassString(){ return "s"; }
}

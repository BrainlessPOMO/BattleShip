package org.example;

public class AircraftCarrier extends Ship{

    private int hitCounter = 0;

    public AircraftCarrier(Field field) {
        super(5, 5, 'A', field);
    }

    public AircraftCarrier(Field field, ShipDirection dir, Location start) {
        super(5, 5, 'A', field, start, dir);
    }

    public String getSinkMessage(){
        return super.getSinkMessage("Aircraft Carrier");
    }
}

package org.example;

import java.util.InputMismatchException;

public enum ShipDirection {
    HORIZONTAL, VERTICAL;

    public static ShipDirection fromString(String dirString) throws InputMismatchException {
        switch (dirString) {
            case "v", "vertical", "V", "Vertical":
                return ShipDirection.VERTICAL;
            case "h", "horizontal", "H", "Horizontal":
                return ShipDirection.HORIZONTAL;
            default:
                throw new InputMismatchException("The direction you requested does not exist!");
        }
    }


}

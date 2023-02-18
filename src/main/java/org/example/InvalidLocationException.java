package org.example;

public class InvalidLocationException extends Exception {
    public InvalidLocationException(){
        super("Incorrect position selected");
    }
    public InvalidLocationException(String errorMessage){
        super(errorMessage);
    }
}

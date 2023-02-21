package org.example;

public class InvalidPlayerException extends Exception{

    InvalidPlayerException(){
        super("There is no such player");
    }
    InvalidPlayerException(String message){
        super(message);
    }
}

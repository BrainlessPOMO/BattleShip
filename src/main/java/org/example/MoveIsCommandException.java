package org.example;

public class MoveIsCommandException extends InvalidLocationException{
    private Command command;

    MoveIsCommandException(Command command){
        this.command = command;
    }

    public Command getCommand(){ return this.command; }
}

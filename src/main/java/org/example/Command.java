package org.example;

import java.util.InputMismatchException;

public enum Command {
    HELP ("It shows all the available commands", "help"),
    SAVE ("Saves the current state of the game", "save"),
    LOAD("Loads a previous played game", "load"),
    EXIT("Exits the program", "exit");

    public final String helpText;
    public final String commandString;
    Command(String helpText, String commandString) {
        this.helpText = helpText;
        this.commandString = commandString;
    }

    public static Command fromString(String commandString) throws InputMismatchException{
        switch (commandString){
            case "help", "h":
                return Command.HELP;
            case "save", "s":
                return Command.SAVE;
            case "load", "l":
                return Command.LOAD;
            case "exit", "e":
                return Command.EXIT;
            default:
                throw new InputMismatchException("The command " + commandString + " does not exist. Please type 'help' to see all the available commands");
        }
    }
}

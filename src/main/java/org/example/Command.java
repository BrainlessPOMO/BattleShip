package org.example;

import java.util.InputMismatchException;

public enum Command {
    HELP ("It shows all the available commands", "help, h"),
    SAVE ("Saves the current state of the game", "save, s"),
    LOAD("Loads a previous played game", "load, l"),
    EXIT("Exits the program", "exit, e"),
    PRINT("Prints the state of both fields", "print, p"),
    SHIPRINT("Prints the state of both fields with visible ships", "prints, ps");

    public final String helpText;
    public final String commandString;
    Command(String helpText, String commandString) {
        this.helpText = helpText;
        this.commandString = commandString;
    }

    public static Command fromString(String commandString) throws InputMismatchException{
        switch (commandString.toLowerCase()){
            case "help", "h":
                return Command.HELP;
            case "save", "s":
                return Command.SAVE;
            case "load", "l":
                return Command.LOAD;
            case "exit", "e":
                return Command.EXIT;
            case "print", "p":
                return Command.PRINT;
            case "prints", "ps":
                return Command.SHIPRINT;
            default:
                throw new InputMismatchException("The command " + commandString + " does not exist. Please type 'help' to see all the available commands");
        }
    }
}

package org.example;

public class LoadFailException extends Exception{
    public LoadFailException(){
        super("This file is corrupted. We cannot load this game!");
    }
}

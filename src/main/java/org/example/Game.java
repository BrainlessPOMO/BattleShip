package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private int rowsNum = 0;
    private int columnsNum = 0;

    private int maxMoves = -1;

    private Player player1;
    private Player player2;

    public Game(){
        getRowsGetColumnsGetMaxMoves();
        createPlayer(1);
        createPlayer(2);
    }

    public Game(Player player1, Player player2){
        getRowsGetColumnsGetMaxMoves();
        this.player1 = player1;
        this.player2 = player2;
    }

    public Game(int rowsNum, int columnsNum, int maxMoves, Player player1, Player player2){
        this.rowsNum = rowsNum;
        this.columnsNum = columnsNum;
        this.maxMoves = maxMoves;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void init(){
        player1.initField(this.rowsNum, this.columnsNum);
        player2.initField(this.rowsNum, this.columnsNum);
    }

    public void placeShips(){
        player1.placeShips(player2.getField());
        player2.placeShips(player1.getField());
    }

    public void play(Player currentPlayer){
        System.out.println(this.player1.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player1.getField().toStringWithShips());

        System.out.println(this.player2.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player2.getField().toStringWithShips());

        int counter = 0;
        boolean counterController = true;
        while (this.maxMoves != counter){
            while (true){
                try {
                    currentPlayer.selectMove();
                    break;
                } catch (MoveIsCommandException e){
                    commandController(e);
                } catch (InvalidLocationException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            if(currentPlayer == player1) currentPlayer = player2;
            else currentPlayer = player1;

            // check if a player has won
            if(player1.hasWon()) {
                showResult(player1);
                break;
            }
            if(player2.hasWon()){
                showResult(player2);
                break;
            }

            if(!counterController) counter ++;
            counterController = !counterController;
        }
        showResult();
    }

    public void showResult(){
        System.out.println("Max Moves reached");
        showFields();
        showScores();
    }

    public void showResult(Player player){
        System.out.println(player.getName() + " won!!!");
        showFields();
    }

    /*
    *
    * Helper Functions
    *
    * */
    public void showFields(){
        System.out.println(this.player1.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player1.field.toStringWithShips());

        System.out.println(this.player2.getName() + "'s Field");
        System.out.println("----------------------------------------------------------------");
        System.out.println(player2.field.toStringWithShips());

    }

    public void showScores(){
        System.out.println(this.player1.getName() + "'s Score: " + this.player1.getScore() + "\n");
        System.out.println(this.player2.getName() + "'s Score: " + this.player2.getScore());

    }

    private void createPlayer(int playerNum){
        Scanner sc = new Scanner(System.in);
        String answer;
        while (true){
            System.out.println("Do you want player " + playerNum + " to be a computer?");
            answer = sc.nextLine().toLowerCase();
            if(answer.equals("y") || answer.equals("yes")){
                if(playerNum == 1) setPlayer1(new ComputerPlayer());
                else setPlayer2(new ComputerPlayer());
                return;
            } else if(answer.equals("n") || answer.equals("no")){
                break;
            }
        }

        String tempName;
        while(true){
            System.out.println("What do you want player's " + playerNum + " name to be?");
            tempName = sc.nextLine();
            System.out.println("Is '" + tempName + "' correct?");

            answer = sc.nextLine().toLowerCase();
            if(answer.equals("y") || answer.equals("yes")) break;
        }

        if(playerNum == 1) setPlayer1(new HumanPlayer(tempName));
        else setPlayer2(new HumanPlayer(tempName));
    }

    private void getRowsGetColumnsGetMaxMoves() {
        while(true){
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Number of rows: ");
                this.rowsNum = sc.nextInt();
                System.out.print("Number of columns: ");
                this.columnsNum = sc.nextInt();

                if(rowsNum > 9 && rowsNum < 16 && columnsNum > 9 && columnsNum < 16){
                    break;
                } else {
                    System.out.println("Rows and Columns have to be between the number 10 and 15");
                }

            } catch (InputMismatchException e) {
                System.out.println("Please provide integers for number of Rows and Columns!");
            }
        }
        while (true){
            try {
                Scanner sc = new Scanner(System.in);

                System.out.println("Provide the maximum moves that are going to be played(leave blank for playing until someone wins!)");
                String tempMoves = sc.nextLine();
                if(tempMoves == "") {
                    this.maxMoves = -1;
                    break;
                }

                if(Integer.parseInt(tempMoves) > 0) {
                    this.maxMoves = Integer.parseInt(tempMoves);
                    break;
                }
                System.out.println("Max Moves have to be a number more than zero!");
            } catch (NumberFormatException e){
                System.out.println("Please provide an integer for number of maxMoves!");
            }
        }
    }

    public void commandController(MoveIsCommandException e) {
        switch (e.getCommand()){
            case EXIT :
                System.out.println("Are you sure you want to exit the program? (ex: y=yes, n=no) \nAny game that is not saved will be lost!");
                Scanner sc = new Scanner(System.in);
                String answer = sc.nextLine().toLowerCase();

                if(answer.equals("y") || answer.equals("yes")){
                    System.exit(-1);
                }
                break;
            case LOAD:
                System.out.println("Inside loadCommandController");
                break;
            case SAVE:
                System.out.println("Inside saveCommandController");
                break;
            default:
                System.out.println("-----------------------------------------");
                for(Command cmd : Command.values()){
                    System.out.println(cmd.commandString + " : " + cmd.helpText);
                }
                System.out.println("-----------------------------------------");
                System.out.println();
        }
    }

    /*
    *
    * Setters - Getters
    *
    * */
    public void setPlayer1(Player player1){
        this.player1 = player1;
    }
    public Player getPlayer1(){
        return this.player1;
    }

    public void setPlayer2(Player player2){
        this.player2 = player2;
    }
    public Player getPlayer2(){
        return this.player2;
    }
}

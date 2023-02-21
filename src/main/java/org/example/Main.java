package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static Game game;



    public static void main(String[] args) {

        game = new Game();
        game.init();
        game.placeShips();

        game.setCurrentPlayer(game.getPlayer1());
        game.play();

    }

    protected void commandController(MoveIsCommandException e) {
        switch (e.getCommand()){
            case EXIT :
                if(exitCommandController()) System.exit(-1);
                break;
            case LOAD:
                try {
                    loadCommandController();
                } catch (LoadFailException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            case SAVE:
                saveCommandController();
                break;
            default:
                helpCommandController();
        }
    }

    private boolean exitCommandController(){
        System.out.println("Are you sure you want to exit the program? (ex: y=yes, n=no) \nAny game that is not saved will be lost!");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine().toLowerCase();

        if(answer.equals("y") || answer.equals("yes")){
            return true;
        }
        return false;
    }

    /*
    *
    * Controllers
    *
    * */
    private void saveCommandController(){
        JSONObject output = new JSONObject();
        output.put("rowsNum", game.getRowsNum());
        output.put("columnsNum", game.getColumnsNum());
        output.put("maxMoves", game.getMaxMoves());

        if(game.getCurrentPlayer() == game.getPlayer1()) output.put("playing", "player1");
        else output.put("playing", "player2");

        output.put("counter", game.getCounter());
        output.put("counterController", game.getCounterController());

        JSONArray outputPlayers = new JSONArray();
        JSONObject outputPlayer1 = new JSONObject();

        if(game.getPlayer1().getClass() == HumanPlayer.class){
            outputPlayer1.put("type", "humanPlayer");
            outputPlayer1.put("name", game.getPlayer1().getName());
        }
        else {
            outputPlayer1.put("type", "computerPlayer");
            outputPlayer1.put("name", "");
        }

        JSONArray player1Ships = new JSONArray();
        JSONObject shipObject = new JSONObject();
        for( Ship ship : game.getPlayer1().getField().getShips()){

        }


        System.out.println("Inside saveCommandController");
    }
    private void loadCommandController() throws LoadFailException {
        int currentPlayer = readFromFileAndParseInfo();
        System.out.println("\n\n------------------------------\nFile is parsed! Game begins\n------------------------------\n\n");
        if(currentPlayer == 1){
            game.setCurrentPlayer(game.getPlayer1());
            game.play();
        }
        else if (currentPlayer == 2){
            game.setCurrentPlayer(game.getPlayer2());
            game.play();
        }
        else {
            throw new LoadFailException();
        }
    }

    private void helpCommandController(){
        System.out.println("-----------------------------------------");
        for(Command cmd : Command.values()){
            System.out.println(cmd.commandString + " : " + cmd.helpText);
        }
        System.out.println("-----------------------------------------");
        System.out.println();
    }

    /*
    *
    * Loader Functions
    *
    * */
    public void setLoadedShipsOnField(Player player, JSONArray shipsToLoad) throws LoadFailException {
        for(int i=0; i<shipsToLoad.length(); i++){
            JSONObject tempShip = shipsToLoad.getJSONObject(i);
            boolean placed;
            switch (tempShip.getString("type")){
                case "ac":
                    placed = player.getField().placeShip(new AircraftCarrier(player.getField(), ShipDirection.fromString(tempShip.getString("shipDirection")), player.getField().getLocation(tempShip.getInt("row"), tempShip.getInt("column"))), false);
                    if(!placed) throw new LoadFailException();
                    break;
                case "d":
                    placed = player.getField().placeShip(new Destroyer(player.getField(), ShipDirection.fromString(tempShip.getString("shipDirection")), player.getField().getLocation(tempShip.getInt("row"), tempShip.getInt("column"))), false);
                    if(!placed) throw new LoadFailException();
                    break;
                case "s":
                    placed = player.getField().placeShip(new Submarine(player.getField(), ShipDirection.fromString(tempShip.getString("shipDirection")), player.getField().getLocation(tempShip.getInt("row"), tempShip.getInt("column"))), false);
                    if(!placed) throw new LoadFailException();
                    break;
                default:
                    throw new LoadFailException();
            }
        }
    }

    public void loadMarkedLocations(Player player, JSONArray markedLocations){
        for(int i=0; i<markedLocations.length(); i++){
            player.getField().getLocation(markedLocations.getJSONObject(i).getInt("row"), markedLocations.getJSONObject(i).getInt("column")).mark();
        }
    }

    public void setLoadedPlayer(String playerType, String name, int playerNum){
        if(playerType.equals("humanPlayer")){
            if(playerNum == 0){
                game.setPlayer1(new HumanPlayer(name));
            } else {
                game.setPlayer2(new HumanPlayer(name));
            }
        } else if (playerType.equals("computerPlayer")){
            if(playerNum == 0){
                game.setPlayer1(new ComputerPlayer());
            } else {
                game.setPlayer2(new ComputerPlayer());
            }
        }
    }


    /*
    *
    * Helper Functions
    *
    * */
    public int readFromFileAndParseInfo(){
        String pathToFile = "src\\main\\saves\\" + getFileName() + ".json";

        int currentPlayer = -1;
        try {
            String contents = new String((Files.readAllBytes(Paths.get(pathToFile))));

            // get game information from jsonObject
            JSONObject gameInfo = new JSONObject(contents);

            if(gameInfo.getInt("maxMoves") == -1){
                game = new Game(gameInfo.getInt("rowsNum"), gameInfo.getInt("columnsNum"));
            } else {
                game = new Game(gameInfo.getInt("rowsNum"), gameInfo.getInt("columnsNum"), gameInfo.getInt("maxMoves"));
                game.setCounter(gameInfo.getInt("counter"));
                game.setCounterController(gameInfo.getBoolean("counterController"));
            }

            // get players information from jsonObject
            JSONArray players = gameInfo.getJSONArray("players");
            JSONObject player1 = players.getJSONObject(0);
            JSONObject player2 = players.getJSONObject(1);

            setLoadedPlayer(player1.getString("type"), player1.getString("name"), 0);
            setLoadedPlayer(player2.getString("type"), player2.getString("name"), 1);
            game.init();

            // load everything for player 1
            setLoadedShipsOnField(game.getPlayer1(), player1.getJSONArray("ships"));
            loadMarkedLocations(game.getPlayer1(), player1.getJSONArray("fieldMarkedLocations"));

            // load everything for player 2
            setLoadedShipsOnField(game.getPlayer2(), player2.getJSONArray("ships"));
            loadMarkedLocations(game.getPlayer2(), player2.getJSONArray("fieldMarkedLocations"));

            //get the player that plays the next move
            if(gameInfo.getString("playing").equals("player2")) currentPlayer = 2;
            else currentPlayer = 1;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (LoadFailException e) {
            System.out.println(e.getMessage());
        }
        return currentPlayer;
    }

    public String getFileName(){
        String tempFileName;
        while (true){
            System.out.println("Which game file do you want to be loaded?");
            Scanner scanner = new Scanner(System.in);

            tempFileName = scanner.nextLine();
            System.out.println("Is '" + tempFileName + "' the correct file name?");
            String answer = scanner.nextLine().toLowerCase();
            if((answer.equals("y") || answer.equals("yes")) && !tempFileName.equals("") && tempFileName != null) break;
        }
        return tempFileName;
    }
}
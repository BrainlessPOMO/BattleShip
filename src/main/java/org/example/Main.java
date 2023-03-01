package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static Game game;

    public final String savesFolder = "src\\main\\java\\saves";

    public static void main(String[] args) {

        game = new Game();
        game.init();
        game.placeShips();

        game.setCurrentPlayer(game.getPlayer1());
        game.start();

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
            case PRINT:
                
                System.out.println(game.getPlayer1().getName() + "'s Field");
                System.out.println("----------------------------------------------------------------");
                System.out.println(game.getPlayer1().getField().toString());

                System.out.println(game.getPlayer2().getName() + "'s Field");
                System.out.println("----------------------------------------------------------------");
                System.out.println(game.getPlayer2().getField().toString());    
                break;
            
            case SHIPRINT:
                try{
                    System.out.println(game.getPlayerFromNum(1).getName() + "'s Field");
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(game.getPlayerFromNum(1).getField().toStringWithShips());
                    
                    System.out.println(game.getPlayerFromNum(2).getName() + "'s Field");
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(game.getPlayerFromNum(2).getField().toStringWithShips());    
                } catch(InvalidPlayerException ex){
                    System.out.println("There is no such player");
                }
                break;
            default:
                helpCommandController();
        }
    }

    private boolean exitCommandController(){
        System.out.println("Are you sure you want to exit the program? (ex: y=yes, n=no) \nAny game that is not saved will be lost!");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine().toLowerCase();

        return answer.equals("y") || answer.equals("yes");
    }

    /*
    *
    * Controllers
    *
    * */
    private void saveCommandController(){
        JSONObject outputJSONObject = new JSONObject();
        outputJSONObject.put("rowsNum", game.getRowsNum());
        outputJSONObject.put("columnsNum", game.getColumnsNum());
        outputJSONObject.put("maxMoves", game.getMaxMoves());

        if(game.getCurrentPlayer() == game.getPlayer1()) outputJSONObject.put("playing", "player1");
        else outputJSONObject.put("playing", "player2");

        outputJSONObject.put("counter", game.getCounter());
        outputJSONObject.put("counterController", game.getCounterController());

        JSONArray playersJSONArray = new JSONArray();

        try {
            // make player1 and player 2 JSON objects and put them inside players JSON Array
            playersJSONArray.put(playersToJSONObject(1));
            playersJSONArray.put(playersToJSONObject(2));

        } catch (InvalidPlayerException e) {
            System.out.println(e.getMessage() + "\n" + "This game could not be saved. Sorry for the inconvenience");
        }

        outputJSONObject.put("players", playersJSONArray);

        System.out.println("How would you want the save to be called?");
        String fileName = getFileName();
        String fileDir = this.savesFolder + fileName + ".json";
        try{
            File file = new File(fileDir);
            if(file.exists()) {
                System.out.println("A file with name " + fileName + " already exists. Do you want to override it?");

                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();
                if (!(answer.equals("y") || answer.equals("yes"))) {
                    System.out.println("File was not saved.");
                    return;
                }
            }

            FileWriter outputFile = new FileWriter(fileDir);
            outputFile.write(outputJSONObject.toString(1));

            outputFile.close();
        } catch (IOException e){ }

        System.out.println("Game Successfully Saved");
    }
    private void loadCommandController() throws LoadFailException {
        int currentPlayer = readFromFileAndParseInfo();
        switch (currentPlayer) {
            case 1:
                game.setCurrentPlayer(game.getPlayer1());
                game.start();
                System.out.println("\n\n------------------------------\nFile is parsed! Game begins\n------------------------------\n\n");
                break;
            case 2:
                game.setCurrentPlayer(game.getPlayer2());
                game.start();
                System.out.println("\n\n------------------------------\nFile is parsed! Game begins\n------------------------------\n\n");
                break;
            default:
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
                    if(!placed) throw new LoadFailException("cannot place AC");
                    break;
                case "d":
                    placed = player.getField().placeShip(new Destroyer(player.getField(), ShipDirection.fromString(tempShip.getString("shipDirection")), player.getField().getLocation(tempShip.getInt("row"), tempShip.getInt("column"))), false);
                    if(!placed) throw new LoadFailException("cannot place destroyer");
                    break;
                case "s":
                    placed = player.getField().placeShip(new Submarine(player.getField(), ShipDirection.fromString(tempShip.getString("shipDirection")), player.getField().getLocation(tempShip.getInt("row"), tempShip.getInt("column"))), false);
                    if(!placed) throw new LoadFailException("cannot place sub");
                    break;
                default:
                    throw new LoadFailException("\n\nnot a ship\n\n");
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
     * Saver Functions
     *
     * */
    public JSONObject shipToJSONObject(Ship ship) {
        JSONObject tempShipObject = new JSONObject();

        tempShipObject.put("type", ship.getClassString());
        tempShipObject.put("shipDirection", ShipDirection.getShipDirection(ship.getDir()));
        tempShipObject.put("row", ship.getStartingLocation().getRow());
        tempShipObject.put("column", ship.getStartingLocation().getCol());

        return tempShipObject;
    }

    public JSONObject locationToJSONObject(Location loc){
        JSONObject tempLocationObject = new JSONObject();

        tempLocationObject.put("row", loc.getRow());
        tempLocationObject.put("column", loc.getCol());
        return tempLocationObject;
    }

    public JSONObject playersToJSONObject(int playerNum) throws InvalidPlayerException {
        JSONObject tempPlayerJSONObject = new JSONObject();


        if(game.getPlayerFromNum(playerNum).getClass() == HumanPlayer.class){
            tempPlayerJSONObject.put("type", "humanPlayer");
            tempPlayerJSONObject.put("name", game.getPlayerFromNum(playerNum).getName());
        }
        else {
            tempPlayerJSONObject.put("type", "computerPlayer");
            tempPlayerJSONObject.put("name", "");
        }

        // create a JSONArray containing all the information about ships on player's 1 field
        JSONArray playerShipsJSONArray = new JSONArray();

        for(Ship ship : game.getPlayerFromNum(playerNum).getField().getShips()){
            if(ship != null){
                JSONObject shipObject = shipToJSONObject(ship);
                playerShipsJSONArray.put(shipObject);
            }
        }


        // create a JSONArray containing all the markedLocations on player's 1 field
        JSONArray playerMarkedLocationsJSONArray = new JSONArray();
        for(ArrayList<Location> row : game.getPlayerFromNum(playerNum).getField().getLocations()){
            for(Location loc : row){
                if(loc.isMarked()) playerMarkedLocationsJSONArray.put(locationToJSONObject(loc));
            }
        }

        tempPlayerJSONObject.put("ships", playerShipsJSONArray);
        tempPlayerJSONObject.put("fieldMarkedLocations", playerMarkedLocationsJSONArray);

        return tempPlayerJSONObject;
    }


    /*
    *
    * Helper Functions
    *
    * */
    public int readFromFileAndParseInfo() throws LoadFailException{
        System.out.println("Which game file do you want to be loaded?");

        String fileName = getFileName();
        String pathToFile = this.savesFolder + fileName + ".json";

        int currentPlayer = -1;
        
        // check if the file exists
        File file = new File(pathToFile);
        if(!file.exists()) {
            throw new LoadFailException("File with name '" + fileName + "' does not exist!");
        }
        
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

        } catch (IOException | LoadFailException e) {
            System.out.println(e.getMessage());
        }
        return currentPlayer;
    }

    public String getFileName(){
        String tempFileName;
        while (true){
            Scanner scanner = new Scanner(System.in);

            tempFileName = scanner.nextLine();
            System.out.println("Is '" + tempFileName + "' the correct file name?");
            String answer = scanner.nextLine().toLowerCase();
            if((answer.equals("y") || answer.equals("yes")) && !tempFileName.equals("") && tempFileName != null) break;
        }
        return tempFileName;
    }
}
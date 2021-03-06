package it.polimi.ingsw.model;

import it.polimi.ingsw.model.State.*;
import it.polimi.ingsw.utility.Coordinate;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * The player is used by the controller to change the model between a Strategy pattern's approach:
 * is responsible of all changes in the grid and
 * all the actions that could be done by its workers are described in this class.
 * The class is abstract because it needs an implementation depending on which God the user decides to use during the game.
 * @author CG51
 * @version 1.1
 */

public abstract class Player implements Serializable{


    private static final long serialVersionUID = 13L;
    private List<Worker> workers;
    private String playerID;
    private List<Coordinate> validCoordinate;  //is always calculated in the previous action
    private boolean power=false;

    Model model;

    public boolean isGameOver() {
        return gameOver;
    }

    boolean gameOver = false;


    /**
     * Class constructor to set the Player name and workers
     * @param playerID
     * @param model
     */
    public Player (String playerID, Model model) {

        this.playerID = playerID;
        this.workers= new ArrayList<>();
        this.model = model;
    }

    /**
     * Adds exactly 2 worker
     * @return
     */
    public Worker addWorker(){
        if(workers.size()<2){
        Worker worker=new Worker(this,workers.size());
        this.workers.add(worker);
        return worker;
        }
        else return null;
    }

    /**
     * Getter method to access the playerID
     * @return the string which holds playerID
     */
    public String getPlayerID() {
        return playerID;
    }

    /**
     * Getter method to access the worker
     * @param num index of worker to be accessed
     * @return specified worker
     */
    public Worker getWorker(int num) {
        return workers.get(num);
    }

    /**
     * Sets the value of coordinate from an object Checks if the passed parameter is in the set of valid coordinates
     * @param checks to be verified
     */
    public final void setValidCoordinate(Checks checks) {
        this.validCoordinate= checks.getResult();
        if(this.validCoordinate.isEmpty()) {
            defeatHandler();

        }
    }

    /**
     * Checks if the game is over
     * @return
     */
    protected boolean checkGameOver(){
        boolean noAction = false;
        List<Coordinate> result;
        for (int i = 0; i < Grid.N_ROWS; i++)
            for (int j = 0; j < Grid.N_COLS; j++) {
                if (model.getGrid().getTile(i, j).getWorker() != null && model.getGrid().getTile(i, j).getWorker().getPlayer().getPlayerID().compareTo(this.playerID) == 0) {
                    result = new Checks(model, new Coordinate(i, j)).isNotWorker().isNotDome().isRisible().getResult();
                    if (!noAction) {
                        if (result.isEmpty()) noAction = true;
                        else return false;
                    } else {
                        if (result.isEmpty()) {
                            defeatHandler();
                            return true;
                        } else return false;
                    }
                }
            }
        return false;
    }

    /**
     * Winning handler
     */
    protected void defeatHandler(){
        String winner = null;
        int k = 0;
        this.gameOver=true;
        model.getGrid().removeWorkersOfPlayer(this);
        for(int j=0; j < model.getNumOfPlayers(); j++) {
            if(!model.getPlayer(j).gameOver) {
                k++;
                winner = model.getPlayer(j).getPlayerID();
            }
        }
        if(k==1) {
            model.setCurrentState(new Win(winner));
            model.setWinner(winner);
            model.notify(model.updateState());
        }
    }

    /**
     * Method locates the worker in the working area (game board) if there is no worker at chosen point
     * @param destination
     * @return
     */
    public boolean positionWorker(Coordinate destination) {
        if (!model.getGrid().getTile(destination).isWorker()) {
            model.getGrid().getTile(destination).setWorker(addWorker());
            if(workers.size() == 2 && model.getPlayer(model.getNumOfPlayers() - 1) == this) model.setCurrentState(new Select());
            else if(workers.size() == 2) model.setCurrentState(new PositionWorkers());
            return true;
        }
        else return false;
    }

    /**
     * To select the worker
     * @param selection
     * @return
     */
    public boolean makeSelection(Coordinate selection) {
        Worker workerTmp = model.getGrid().getTile(selection).getWorker();

        if (workerTmp!=null &&  workerTmp.getPlayer().equals(this)) {
            model.setCurrentWorker(selection);
            nextPhase();
            return true;

        } else return false;


    }

    /**
     * Make the condition to check if the movement is available and call the move worker function
     * @param destination The input choice
     * @return
     */
    public boolean makeMovement(Coordinate destination) {
        Coordinate from = model.getCurrentWorker();
        setValidCoordinate(new Checks(model,from).isNotWorker().isNotDome().isRisible());
        if (validCoordinate.contains(destination)) {

            moveWorker(destination);
            if (winCondition(from, destination)) model.setCurrentState(new Win(this.playerID));
            else {
                nextPhase();

            }
            return true;
        } else return false;


    }

    /**
     * moves worker to destination coordinate
     * @param destination
     */
    protected void moveWorker(Coordinate destination){

        Worker wrkTmp = model.getGrid().getTile(model.getCurrentWorker()).getWorker();
        model.getGrid().getTile(destination).setWorker(wrkTmp);
        model.getGrid().getTile(model.getCurrentWorker()).noneWorker();
        model.setCurrentWorker(destination);

    }

    /**
     * Builds typeblock at destination point if condition is met
     * @param destination
     * @return true or false
     */
    public boolean makeBuild(Coordinate destination) {
        setValidCoordinate(new Checks(model,model.getCurrentWorker()).isNotWorker().isNotDome());
        if (validCoordinate.contains(destination)) {
            model.getGrid().getTile(destination).levelUp();
            nextPhase();
            return true;
        } else return false;
    }

    /**
     * Checks the win condition
     * @param from
     * @param destination
     * @return true or false
     */
    public boolean winCondition(Coordinate from, Coordinate destination) {
        Tile tileFrom = model.getGrid().getTile(from);
        Tile tileDestination = model.getGrid().getTile(destination);

        return tileFrom.getHeight().equals(TypeBlock.SECOND) && tileDestination.getHeight().equals(TypeBlock.THIRD);
    }

    /**
     * is used to check if the power is active or not
     * @return true or false depending on power's activation
     */
    public boolean isActive(){
        return power;
    }

    /**
     * Switches the power
     */
    public void togglePower(){
        power= !power;
    }


    /**
     * This function implement the FSM structure which describe game's round for each kind of god.
     * For each state, the function had to decide the next state, depends also if the power is active or not.
     */
    public void nextPhase(){
        State currentState = model.getCurrentState();
        State nextState = null;
        if (currentState instanceof Select)
            nextState = new Move();

        else if (currentState instanceof Move)
            nextState = new Build();

        else if (currentState instanceof Build){
            nextState = new End();

        }

        model.setCurrentState(nextState);
    }

    public abstract boolean makePower(Coordinate destination);

    public final boolean containsInValidCoordinate(Coordinate coordinate){
        return validCoordinate.contains(coordinate);
    }

    /**
     * Overridden equals method to compare the player object
     * @param obj to be compared with player
     * @return current player if passed parameter is player instance
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) return false;
        Player that = (Player) obj;
        return this.getPlayerID().equals(that.getPlayerID());

    }
}

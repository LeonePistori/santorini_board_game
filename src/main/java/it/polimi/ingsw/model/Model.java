package it.polimi.ingsw.model;

import it.polimi.ingsw.model.State.GameStart;
import it.polimi.ingsw.model.State.State;
import it.polimi.ingsw.utility.Subject;
import it.polimi.ingsw.utility.Coordinate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Model extends Subject<Model> implements Cloneable {

    private Grid grid;
    private Coordinate currentWorker;
    private State currentState;
    private List<PlayerWithGroundEffect>groundEffects;
    private List<Player> players;
    private Player currentPlayer;

    public Model clone(){
        Model model = new Model();
        model.grid=this.grid;
        return model;
    }

    public Model(){
        this.grid = new Grid();
        this.groundEffects=new ArrayList<>();
        this.players=new ArrayList<>();
        //this.currentState = new GameStart();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    protected Grid getGrid() {
        return grid;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Coordinate getCurrentWorker() {
        return currentWorker;
    }

    protected void setCurrentWorker(Coordinate worker) {
        this.currentWorker = worker;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    protected List<PlayerWithGroundEffect> getGroundEffects() {
        return groundEffects;
    }

    public Player getPlayer(int index){
        return players.get(index);
    }

    // THE GOD NAME NEEDS TO BE THE SAME AS THE CLASS
    public void createPlayer(String god, String id){
        String godStandard = god.substring(0, 1).toUpperCase() + god.substring(1).toLowerCase();
        try {
            Class [] classes = {String.class};
            Player new_player = (Player)Class.forName("it.polimi.ingsw.model." + godStandard).getDeclaredConstructor(classes).newInstance(id);
            players.add(new_player);
            for(int i=0; i<2; i++){
                new_player.addWorker();
            }
            System.out.println("PLAYER CREATED");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void nextPlayer(){
        int index= players.indexOf(currentPlayer);
        if(index==players.size()-1)index=0;
        this.currentPlayer=players.get(index);
    }
}



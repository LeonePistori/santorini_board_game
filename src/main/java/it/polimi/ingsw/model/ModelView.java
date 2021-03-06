package it.polimi.ingsw.model;

import it.polimi.ingsw.model.State.Win;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class describes the ModelView functionality
 * @author CG51
 * @version 1.1
 */
public class ModelView implements Serializable {
    /**
     * Class attributes
     */
    private static final long serialVersionUID = 30L;
    private Grid grid;
    private String currentPlayer;
    private List<Player> players;
    private String state;
    private Map<String, String> godsPlayer;
    String winner;
    private String message;


    public ModelView(Model model) {
        Model clone = model.clone();
        this.grid = clone.getGrid();
        if(!(clone.getPlayers().isEmpty()))
            this.currentPlayer = clone.getCurrentPlayer().getPlayerID();
        this.players = clone.getPlayers();
        this.state = clone.getCurrentState().getClass().getSimpleName().toLowerCase();
        this.godsPlayer = clone.getGodsPlayer();
        if(clone.getCurrentState() instanceof Win)
            this.winner = ((Win)clone.getCurrentState()).getWinner();
    }

    public Map<String, String> getGodsPlayer() {
        return godsPlayer;
    }


    public ModelView(Model model, String message) {
        new ModelView(model).setMessage(message);
    }


    public int getNumOfPlayers() {
        return players.size();
    }

    public String getPlayer(int index) {
        return players.get(index).getPlayerID();
    }

    public int sizePlayers() {
        return players.size();
    }

    public String getWinner(){
        return winner;
    }

    public Grid getGrid() {
        return grid;
    }

    public String getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public ModelView setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}

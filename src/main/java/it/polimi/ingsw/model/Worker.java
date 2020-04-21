package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.Coordinate;

/**
 * The class Worker represents workers of the game
 * @author CG51
 * @version 0.1
 */



public class Worker {

    /**
     * class attribute player, i.e. who owns the worker
     */
    private Player player;
    private int num;

    public Worker(){
        //ONLY FOR TEST
    }

    public Worker(Player player, int num) {
        this.player = player;
        this.num = num;
    }

    /**
     * setter method to set the current player
     * @param player
     */
    public void setPlayer(Player player){
        this.player = player;

    }

    /**
     * getter method to access the player
     * @return current player
     */
    public Player getPlayer() {

        return player;
    }


}

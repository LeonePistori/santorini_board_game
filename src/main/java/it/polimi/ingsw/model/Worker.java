package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.Coordinate;

import java.io.Serializable;

/**
 * The class Worker represents workers of the game
 * @author CG51
 * @version 1.1
 */



public class Worker implements Serializable {

    /**
     * class attribute player, i.e. who owns the worker
     */
    private static final long serialVersionUID = 4L;
    private Player player;
    private int num;

    public Worker(){
        //ONLY FOR TEST
    }

    /**
     * Getter method to get the index of worker
     * @return index of worker
     */
    public int getNum() {
        return num;
    }

    /**
     * Class constructor to assign the worker with the index to the player
     * @param player
     * @param num
     */
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

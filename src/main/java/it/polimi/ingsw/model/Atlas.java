package it.polimi.ingsw.model;

import it.polimi.ingsw.model.State.*;
import it.polimi.ingsw.utility.Coordinate;

import java.util.List;

/**
 * Class describes the behaviour of the Player's turn who has the power of Atlas.
 * Atlas is the God of Titan Shouldering the Heavens. It is power based on the Build process.
 * Player owning the Atlas may build a DOME at ANY LEVEL, during his/her build action
 * @author CG51
 * @version 0.1
 */
public class Atlas extends Player {

    /**
     * Constructor Atlas to keep the Player's ID, which is received from the super class
     * @param playerID of type String (Player's name)
     * @param model
     */
    public Atlas(String playerID, Model model) {
        super(playerID, model);
    }

    /**
     * Method describes the main skeleton of the turn, Power state is where player may use Atlas' power
     */
    @Override
    public void nextPhase() {
        State currentState = model.getCurrentState();
        State nextState = null;
        if (currentState instanceof Select)
            nextState = new Move();
        else if (currentState instanceof Move)
            nextState = new Power();
        else if (currentState instanceof Build)
            nextState = new End();
        model.setCurrentState(nextState);

    }

    /**
     * Method defines how the power of Atlas is exploited
     * @param destination where to use the power
     * @return true or false
     */
    @Override
    public boolean makePower(Coordinate destination) {
        if (isActive()) {
            model.setCurrentState(new Build());
            setValidCoordinate(new Checks(model, model.getCurrentWorker()).isNotWorker().isNotDome());
            if (containsInValidCoordinate(destination)) {
                Tile t=model.getGrid().getTile(destination);
                while(t.getHeight().ordinal()<TypeBlock.DOME.ordinal())
                    t.levelUp();
                nextPhase();
                return true;
            } else {
                model.setCurrentState(new Power());
                togglePower();
                return false;
            }
        }
        else{
            model.setCurrentState(new Build());
            boolean result= super.makeBuild(destination);
            if(!result){
                model.setCurrentState(new Power());
            }
            return result;

        }

    }
}

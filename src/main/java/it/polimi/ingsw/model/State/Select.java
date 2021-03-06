package it.polimi.ingsw.model.State;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.utility.Coordinate;

import java.io.Serializable;

/**
 * Class describes the Select state of the FSM model. It has the following methods:
 * @author CG51
 * @version 1.1
 */
public class Select implements State, Serializable {

    private static final long serialVersionUID = 11L;

    @Override
    public boolean handle(Coordinate choice, Model model) {
        return model.getCurrentPlayer().makeSelection(choice);

    }

    @Override
    public String questionMessage() {
        return null;
    }
}
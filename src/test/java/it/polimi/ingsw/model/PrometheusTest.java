package it.polimi.ingsw.model;

import it.polimi.ingsw.model.State.End;
import it.polimi.ingsw.model.State.Power;
import it.polimi.ingsw.model.State.Select;
import it.polimi.ingsw.utility.Coordinate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrometheusTest {
    Model model;
    Player prometheus;

    @Before
    public void setupGridTest(){

        model = new Model();
        prometheus = new Prometheus("playerTest", model);
        prometheus.addWorker();

        model.getGrid().getTile(new Coordinate(1, 0)).levelUp().setWorker(prometheus.getWorker(0));
        model.getGrid().getTile(new Coordinate(1,1)).levelUp();
    }
    @Test
    public void firstTest(){
        model.setCurrentState(new Select());
        model.setCurrentPlayer(prometheus);
        prometheus.makeSelection(new Coordinate(1,0));
        assertTrue(model.getCurrentState() instanceof Power);
        prometheus.togglePower();
        int height= model.getGrid().getTile(1,1).getHeight().ordinal();
        prometheus.makePower(new Coordinate(1,1));
        assertEquals(model.getGrid().getTile(1,1).getHeight().ordinal(),height+1);
        assertFalse(prometheus.makeMovement(new Coordinate(1,1)));
        prometheus.makeMovement(new Coordinate(2,1));
        prometheus.makeBuild(new Coordinate(1,1));
        assertTrue(model.getCurrentState() instanceof End);

    }
    @Test
    public void secondTest(){
        model.setCurrentState(new Select());
        model.setCurrentPlayer(prometheus);
        model.getGrid().getTile(new Coordinate(1,1)).levelUp();

        prometheus.makeSelection(new Coordinate(1,0));
        assertTrue(model.getCurrentState() instanceof Power);
        prometheus.makePower(new Coordinate(1,1));
        assertEquals(model.getGrid().getTile(1,1).getWorker(),prometheus.getWorker(0));
        prometheus.makeBuild(new Coordinate(2,1));
        assertTrue(model.getCurrentState() instanceof End);

    }

}

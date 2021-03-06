package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.State.PositionWorkers;
import it.polimi.ingsw.model.playerChoice.PlayerChoice;
import it.polimi.ingsw.model.playerChoice.SetUpChoice;
import it.polimi.ingsw.view.Event;
import it.polimi.ingsw.view.RemoteView;


import java.util.ArrayList;
import java.util.List;

/**
 * Class handles the God selection phase of each player through the following methods:
 * @author CG51
 * @version 1.1
 */
public class SetUpController implements Controller {

    /**
     * Class attributes
     */
    Model model;
    List<String> players;
    int current_player;
    List<String> gods = new ArrayList<>();
    boolean init;
    Controller nextController;
    int numOfPlayerToCreate;
    List<RemoteView> views;

    /**
     * Constructor initializes the players, to each player different (unique) RemoteView is opted.
     * @param model
     * @param players
     * @param views
     */
    public SetUpController(Model model, List<String> players, List<RemoteView> views){
        this.views=views;
        for(RemoteView v : views){
            v.addObserver(this);
        }
        this.model = model;
        this.players = players;
        this.current_player = 0;
        this.init=true;
        this.numOfPlayerToCreate=players.size();
        System.out.println("STARTING PLAYER: " + players.get(current_player) + " " + current_player);
        try {
            views.get(current_player).showEvent(Event.SETUP);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Deprecated
    public void addPlayer(String player){
        players.add(player);
        numOfPlayerToCreate++;
    }

    /**
     * used to communicate with the player in selecting the God. After having selected the Game Gods successfully currentController of the specific Remote View (=player) switches to GameController
     * @param message
     * @throws Error if unexpected behaviour is done
     */
    public void handle(PlayerChoice message) throws Error{

            if (init){
                if(((SetUpChoice)message).getInputs().length == players.size()){
                    for(int i=0; i < ((SetUpChoice)message).getInputs().length; i++){
                        gods.add(i,((SetUpChoice)message).getInputs()[i]);
                    }
                    init=false;
                    
                }
                else{
                    message.getView().showError("Need to select Gods");
                    throw new Error();
                }
            }
            else {
                if(model.getNumOfPlayers() != 0){
                    for(int i = 0; i < model.getNumOfPlayers(); i++){
                        if(((SetUpChoice)message).getInputs()[0].compareTo(model.getPlayer(i).getClass().getSimpleName()) == 0){
                            message.getView().showError("God already selected");
                            throw new Error();
                        }
                    }
                }
                if(gods.contains(((SetUpChoice)message).getInputs()[0])){
                    model.createPlayer(((SetUpChoice)message).getInputs()[0], players.get(current_player));
                    if(nextController == null)
                        this.nextController= new GameController(model);
                    gods.remove(((SetUpChoice)message).getInputs()[0]);
                    message.getView().removeObserver(this);
                    message.getView().addObserver(nextController);
                    numOfPlayerToCreate--;


                }
                else{
                    message.getView().showError("God needs to be in Gods list");
                    throw new Error();
                }
            }
            if(current_player == players.size() - 1){
                current_player = 0;
            }
            else current_player++;


            if(numOfPlayerToCreate==0){
                model.setCurrentState(new PositionWorkers());
                model.notify(model.updateState().setMessage("Welcome in Santorini board\n"));
                //views.get(current_player).showMessage(model.getCurrentState().questionMessage());
            }
            else{
                views.get(current_player).showEvent(Event.GODCHOICE);

                    views.get(current_player).showMessage(gods.toArray(new String[0]));

            }

            System.out.println("CURRENT PLAYER: " + players.get(current_player) + " " + current_player);

    }

    /**
     * Method is used to notify player, while current player gets the messages according to his/her step,
     * other players who try to do something get the message while it is not their turn
     * @param message
     */
    @Override
    public void update(PlayerChoice message){
        if(message.getPlayer().compareTo(players.get(current_player)) == 0){
            try{
                handle(message);
            }
            catch (Error e){

            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        else {
            message.getView().showError("Not your turn");
        }
    }
}

package domain;

import enums.Colour;
import networking.*;
import panel.GameScreen;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {

    // this represents a player in a game
    // there may be some overlap between this and the networking.User class, but I thought it would be a good idea to keep them separated for now

    // game-specific info
    private Town curTown;
    private Set<Town> townsVisited = new HashSet<>();
    private Colour colour; 
    private int score; // The score of a player
    
    // info for connecting to LS and multiplayer
    // TODO: decide where to initialize this field
    private User associated;
    private String ip;
    
    private Hand aHand;//The Hand of this Player, including hand of CardUnit and hand of CounterUnit

    // TODO: implement the constructor
    public Player(Colour pColour, GameScreen pScreen)
    {
        this.curTown = GameMap.getInstance().getTownByName("Elvenhold");
        this.colour = pColour;
    }
    
    // TODO: add more constructors or update existing to initialize the networking fields (associated, ip)

    public String getCurrentTownName() {
        return curTown.getName();
    }

    // called by ElfBoot when it moves to a new town
    public void setCurrentTown(Town curTown) {
        this.curTown = curTown;
        if (!townsVisited.contains(curTown)) {
            townsVisited.add(curTown);
            score++;
        }
    }

    public Town getCurrentTown() {
        return curTown;
    }

    public int getScore() {
    	return score;
    }

    public Colour getColour() {
        return this.colour;
    }
}

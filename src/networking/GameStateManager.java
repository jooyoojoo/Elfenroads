package networking;

import domain.*;
import enums.RoundPhaseType;
import utils.GameRuleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameStateManager {

    private final static Logger LOGGER = Logger.getLogger("game state");

    private GameState gameState;

    private Road selectedRoad;
    private TransportationCounter selectedCounter;
    private List<TravelCard> selectedCards = new ArrayList<>();
    private Town selectedTown;
    boolean obstacleSelected = false;

    private GameStateManager() {}

    public GameStateManager getInstance() {
        return this;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /*
    Selection handling:
    - counter unit + road + PLANROUTES phase -> place a counter unit on the road
    - card + town + MOVE phase -> requested player move to a town
    */

    public Road getSelectedRoad() {
        return selectedRoad;
    }

    public void setSelectedRoad(Road selectedRoad) {
        LOGGER.info("Road on " + selectedRoad.getRegionType() + " selected");
        this.selectedRoad = selectedRoad;

        if (gameState.getCurrentPhase() == RoundPhaseType.PLANROUTES) {
            // Player intends to place an obstacle
            if (obstacleSelected) {
                if (!selectedRoad.placeObstacle()) {
                    //TODO: display failure message and deselect obstacle in UI
                }
            }
            // Player intends to place a transportation counter
            if (!selectedRoad.setTransportationCounter(selectedCounter)) {
                //TODO: display failure message and deselect counter in UI
            }
        }
    }

    public void obstacleSelected() {
        obstacleSelected = true;
        selectedCounter = null;
    }

    public TransportationCounter getSelectedCounter() {
        return selectedCounter;
    }

    public void setSelectedCounter(TransportationCounter selectedCounter) {
        LOGGER.info("Counter " + selectedCounter.getType() + " selected");
        this.selectedCounter = selectedCounter;
    }

    public List<TravelCard> getSelectedCards() {
        return selectedCards;
    }

    public void addSelectedCard(TravelCard selectedCard) {
        this.selectedCards.add(selectedCard);
    }

    public void setSelectedCard(List<TravelCard> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public Town getSelectedTown() {
        return selectedTown;
    }

    public void setSelectedTown(Town selectedTown) {
        LOGGER.info("Town " + selectedTown.getName() + " selected");
        this.selectedTown = selectedTown;

        if (gameState.getCurrentPhase() == RoundPhaseType.MOVE && !selectedCards.isEmpty()) {
            if (!GameRuleUtils.validateMove(GameMap.getInstance(null), gameState.getCurrentPlayer().getCurrentTown(), selectedTown, selectedCards)) {
                gameState.getCurrentPlayer().setCurrentTown(selectedTown);
            } else {
                //TODO: in UI
                // - deselect all currently selected cards in UI
                // - display message that prompts Player for re-selection
                this.selectedTown = null;
                this.selectedCards.clear();
            }
        }
    }

    /**
     * Clears all selection states.
     * Whenever a new selection state is added to GameState, remember to clear it here.
     */
    private void clearSelection() {
        selectedRoad = null;
        selectedCounter = null;
        selectedCards.clear();
        selectedTown = null;
        obstacleSelected = false;
    }

    /*
    Advance game progress
     */

    // totalRounds Round <--in-- numOfRoundPhaseType Phases <--in-- numOfPlayer Turns
    public void endTurn() {
        clearSelection();

        // all players have passed their turn in the current phase
        if (gameState.getCurrentPlayerIdx() + 1 == gameState.getNumOfPlayers()) {
            int nextOrdinal = gameState.getCurrentPhase().ordinal() + 1;
            if (nextOrdinal == RoundPhaseType.values().length) {
                // all phases are done, go to the next round
                endRound();
            } else {
                // go to the next phase within the same round
                gameState.setCurrentPhase(RoundPhaseType.values()[nextOrdinal]);
            }
            return;
        }

        // within the same phase, next player will take action
        gameState.setToNextPlayer();
    }

    private void endRound() {
        gameState.setToFirstPlayer();
        gameState.incrementCurrentRound();
        if (gameState.getCurrentRound() == gameState.getTotalRounds()) {
            endGame();
            return;
        }
        //TODO: update round card in UI
    }

    private void endGame() {
        //TODO: finishes game ending
    }
}

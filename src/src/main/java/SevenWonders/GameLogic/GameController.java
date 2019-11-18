package SevenWonders.GameLogic;

import SevenWonders.AssetManager;
import SevenWonders.GameLogic.Enums.CARD_COLOR_TYPE;

import SevenWonders.GameLogic.Enums.WONDER_TYPE;
import javafx.util.Pair;
import java.util.Vector;

public class GameController {
    private final int MAX_NO_OF_PLAYERS = 7;

    private PlayerController[] playerControllers;
    private DiscardPileController discardPileController;
    private DeckController deckController;
    private GameModel model;
    private int playerCount;
    public GameController(GameModel model){

        this.model = model;

        this.playerControllers = new PlayerController[MAX_NO_OF_PLAYERS];
        playerCount = 0;

        this.discardPileController = new DiscardPileController( model.getDiscardPile());
        this.deckController = new DeckController(( model.getDeck()));
    }

    public boolean checkMoveIsValid(MoveModel move){
        int id = move.getPlayerID();

        Pair<PlayerController, PlayerController> neighbors = new Pair<>(playerControllers[(id+1)%7], playerControllers[(id+8)%7]);

        return MoveController.getInstance().playerCanMakeMove(move, playerControllers[id], neighbors);
    }

    public void updateCurrentMove(int playerID, MoveModel move){
        playerControllers[playerID].updateCurrentMove(move);
    }

    public void playTurn(){
        makeMoves();
        if( model.getCurrentTurn() < 7){

            if (model.getCurrentTurn() != 6)
                shiftCards();

            model.incrementCurrentTurn();
        }
        else{
            model.incrementCurrentAge();
            playEndOfAge();
        }
    }

    private void playEndOfAge(){
        for(int i = 0; i < playerControllers.length; i++){
            //TODO WarPoints system is due to change, change this also
            int winPoint = 0;
            int totalPoints = 0;
            PlayerController myPlayerController = playerControllers[i];
            PlayerController leftPlayerController = playerControllers[(i + 6) % 7];
            PlayerController rightPlayerController = playerControllers[(i + 1) % 7];

            switch(model.getCurrentAge()){
                case 1:
                    winPoint = 1;
                    break;
                case 2:
                    winPoint = 3;
                    break;
                case 3:
                    winPoint = 5;
                    break;
            }

            if( myPlayerController.getShields() > leftPlayerController.getShields()){ //Player defeats left
                totalPoints += winPoint;
            }
            if( myPlayerController.getShields() > rightPlayerController.getShields()){ //Player defeats right
                totalPoints += winPoint;
            }
            if( myPlayerController.getShields() < leftPlayerController.getShields()){ //Left defeats player
                totalPoints -= 1;
            }
            if( myPlayerController.getShields() < rightPlayerController.getShields()){ //Right defeats player
                totalPoints -= 1;
            }

            //TODO myPlayerController.setWarPoints( myPlayerController.getWarPoints() + totalPoints);
        }
    }

    private void makeMoves()
    {
        for (PlayerController playerController : playerControllers) {
            playerController.makeMove();
        }

        for(int i = 0; i < playerControllers.length; i++){
            //Check for possible updates in gold and shields of the player because of the played card
            //Done using the move of the player, therefore the move is not deleted until the updates are performed
            int cardCount = 0;
            PlayerController myPlayerController = playerControllers[i];
            PlayerController leftPlayerController = playerControllers[(i + 6) % 7];
            PlayerController rightPlayerController = playerControllers[(i + 1) % 7];

            CardEffect effect = deckController.getCardByID(
                    myPlayerController.getCurrentMove().getSelectedCardID()).getCardEffect();

            switch (effect.getEffectType()){
                case GRANT_SHIELDS:
                    myPlayerController.setShields(effect.getShields() + myPlayerController.getShields());
                    break;
                case GET_MONEY:
                    myPlayerController.setGold(effect.getGold() + myPlayerController.getGold());
                    break;
                case GET_MONEY_FOR_BROWN_CARD:
                    cardCount = 0;
                    for (Card card : myPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.BROWN) {
                            cardCount++;
                        }
                    }
                    for (Card card : leftPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.BROWN) {
                            cardCount++;
                        }
                    }
                    for (Card card : rightPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.BROWN) {
                            cardCount++;
                        }
                    }
                    myPlayerController.setGold(cardCount + myPlayerController.getGold());
                    break;
                case GET_MONEY_FOR_GRAY_CARD:
                    cardCount = 0;

                    for (Card card : myPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.GRAY) {
                            cardCount++;
                        }
                    }
                    for (Card card : leftPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.GRAY) {
                            cardCount++;
                        }
                    }
                    for (Card card : rightPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.GRAY) {
                            cardCount++;
                        }
                    }
                    myPlayerController.setGold( cardCount * 2 + myPlayerController.getGold());
                    break;
                case GET_MONEY_AND_VP_PER_BROWN:
                    cardCount = 0;

                    for (Card card : myPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.BROWN) {
                            cardCount++;
                        }
                    }

                    myPlayerController.setGold(cardCount + myPlayerController.getGold());
                case GET_MONEY_AND_VP_PER_GRAY:
                    cardCount = 0;

                    for (Card card : myPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.GRAY) {
                            cardCount++;
                        }
                    }
                    myPlayerController.setGold( cardCount * 2 + myPlayerController.getGold());
                    break;
                case GET_MONEY_AND_VP_PER_WONDER:
                    int goldToAdd = (myPlayerController.getWonder().getCurrentStageIndex() + 1) * 3;

                    myPlayerController.setGold( goldToAdd + myPlayerController.getGold() );
                    break;
                case GET_MONEY_AND_VP_PER_YELLOW:
                    cardCount = 0;

                    for (Card card : myPlayerController.getConstructionZone().getConstructedCards()) {
                        if (card.getColor() == CARD_COLOR_TYPE.YELLOW) {
                            cardCount++;
                        }
                    }
                    myPlayerController.setGold( cardCount + myPlayerController.getGold());
                    break;
            }
            myPlayerController.setReady(false);
            myPlayerController.updateCurrentMove(null); //Clear the players move
        }
    }

    private void shiftCards()
    {
        Vector<Card> tmp, tmp1; //Used for shifting purposes
        tmp = playerControllers[0].getHand();

        if (model.getCurrentAge() == 2 ) //If Age is 2, shift cards right
        {
            for( int i = 0; i < playerControllers.length; i++){
                PlayerController rightPlayerController = playerControllers[(i + 1) % 7];

                tmp1 = rightPlayerController.getHand();
                rightPlayerController.setHand(tmp);
                tmp = tmp1;
            }
        }
        else{ //If Age is not 2, shift cards left
            for( int i = 0; i < playerControllers.length; i++){
                PlayerController myPlayerController = playerControllers[i];
                PlayerController rightPlayerController = playerControllers[(i + 1) % 7];

                myPlayerController.setHand(rightPlayerController.getHand());
            }
            playerControllers[playerControllers.length-1].setHand(tmp);
        }
    }

    public void discardCard(Card card) {
        discardPileController.discardCard(card);
    }

    public boolean isEveryoneReady(){
        for(PlayerController controller: playerControllers){
            if (!controller.isReady())
                return false;
        }
        return true;
    }

    public void makePlayerReady(int id){ playerControllers[id].setReady(true); }

    public int addPlayer(String name, WONDER_TYPE wonderType){
    /*
        TODO finish when wonder is complete
        Wonder wonder = new Wonder();
        PlayerModel model = new PlayerModel(playerCount, name, wonderType); //playerCount corresponds to the id

        model.addPlayer(model);
        playerControllers[playerCount] = new PlayerController(model, this);

        playerCount++; */
        return playerCount - 1; //Return the id of the added player
    }
}

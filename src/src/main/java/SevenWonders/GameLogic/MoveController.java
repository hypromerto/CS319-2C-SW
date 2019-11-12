package SevenWonders.GameLogic;

import SevenWonders.GameLogic.Enums.CARD_EFFECT_TYPE;
import SevenWonders.GameLogic.Enums.RESOURCE_TYPE;
import javafx.util.Pair;

import java.util.Map;

public class MoveController {
    //variables
    private static MoveController moveControllerInstance = null;

    //methods
    public static MoveController getInstance() {
        if (moveControllerInstance == null) {
            moveControllerInstance = new MoveController();
        }
        return moveControllerInstance;
    }


    public boolean playerCanMakeMove(MoveModel moveModel, PlayerModel currentPlayer, Pair<PlayerModel, PlayerModel> neighbours) {
        Card selectedCard = fromIDToCard(moveModel.getSelectedCardID());
    }

    private boolean playerCanMakeTheTrade( MoveModel move, PlayerModel currentPlayer, Pair<PlayerModel, PlayerModel> neighbours) {

    }

    private boolean playerHasEnoughCoins(MoveModel moveModel, PlayerModel currentPlayer) {

    }

    private boolean playerHasEnoughResources(MoveModel moveModel, PlayerModel currentPlayer) {
        Map<RESOURCE_TYPE,Integer> requiredResources = fromIDToCard(moveModel.getSelectedCardID()).getResourceCost();
        Map<RESOURCE_TYPE,Integer> clonedResourceMap;
        for (Map.Entry<RESOURCE_TYPE, Integer> entry : requiredResources.entrySet()) {
            clonedResourceMap.put()
        }

        for (Card builtCard : currentPlayer.getConstructionZone().getConstructedCards()) {
            CardEffect effect = builtCard.getCardEffect();
            CARD_EFFECT_TYPE effect_type =  effect.getEffectType();
            switch (effect_type) {
                case PRODUCE_RAW_MATERIAL:
                    for (Map.Entry<RESOURCE_TYPE, Integer> entry : effect.getResources().entrySet()) {

                    }
            }
        }
    }

    private boolean playerCanBuild(MoveModel moveModel, PlayerModel currentPlayer) {

    }

    private boolean playerCanPlayBuildCard(MoveModel moveModel, PlayerModel currentPlayer) {

    }

    private boolean playerCanDiscardCard(MoveModel moveModel, PlayerModel currentPlayer) {

    }

    private boolean playerCanBuildWonder(MoveModel moveModel, PlayerModel currentPlayer,Pair<PlayerModel, PlayerModel> neighbours) {
        WonderStage currentStage = currentPlayer.getWonder().getCurrentStage(); //current stage of the wonder
        Card selectedCard = fromIDToCard(moveModel.getSelectedCardID());
        ConstructionZone currentCZ = currentPlayer.getConstructionZone();
        Vector<Card> constructedCards = currentCZ.getConstructedCards();

    }

    /* TO DO
    This method is for taking an ID and returning a Card object, maybe initialized somewhere else later.
     */
    public Card fromIDToCard(int cardID) {
        return null;
    }
}

package SevenWonders.AI;

import SevenWonders.AssetManager;
import SevenWonders.GameLogic.Deck.Card.Card;
import SevenWonders.GameLogic.Enums.*;
import SevenWonders.GameLogic.Game.GameModel;
import SevenWonders.GameLogic.Move.MoveController;
import SevenWonders.GameLogic.Move.MoveModel;
import SevenWonders.GameLogic.Move.TradeAction;
import SevenWonders.GameLogic.Player.PlayerModel;
import SevenWonders.GameLogic.ScoreController;
import javafx.util.Pair;

import java.util.Vector;

public class AIMoveGenerator {

    private static double upgradeWonderScore(MoveModel move, PlayerModel me, GameModel game) {

        if(move.getAction() != ACTION_TYPE.UPGRADE_WONDER) {return 0;}

        int currentStageIndex = me.getWonder().getCurrentStageIndex();
        if(currentStageIndex == 3) {return 0;}

        int currentAge = game.getCurrentAge();

        if(currentAge == 1) {
            return 6.0 - (currentStageIndex * 2.5);
        }

        else if(currentAge == 2) {
            return 7.5 - (currentStageIndex * 1.25);
        }
        else {
            return 7.0;
        }


    }
    private static double discardScore(MoveModel move) {
        if(move.getAction() == ACTION_TYPE.DISCARD_CARD) {
            return 1.0;
        }
        return 0.0;
    }

    private static double commercialScore(MoveModel move, PlayerModel me) {
        Card card = AssetManager.getInstance().getCardByID(move.getSelectedCardID());


        if(card.getColor() != CARD_COLOR_TYPE.YELLOW) {return 0;}
        if(move.getAction() != ACTION_TYPE.BUILD_CARD) {return 0;}

        double points = 0;

        if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.GET_MONEY) {
            points += 2.0;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.LEFT_RAW_MATERIAL_TRADE_DISCOUNT) {
            points += 2.0;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.RIGHT_RAW_MATERIAL_TRADE_DISCOUNT) {
            points += 2.0;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.MANUFACTURED_GOODS_TRADE_DISCOUNT) {
            points += 4.0;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.GET_MONEY_FOR_BROWN_CARD) {
            points += 3.5;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.GET_MONEY_FOR_GRAY_CARD) {
            points += 3.5;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.ONE_OF_EACH_RAW_MATERIAL) {
            points += 4.5;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.ONE_OF_EACH_MANUFACTURED_GOODS) {
            points += 4.5;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.GET_MONEY_AND_VP_PER_WONDER) {
            points += 6.0;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.GET_MONEY_AND_VP_PER_BROWN) {
            points += 6.0;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.GET_MONEY_AND_VP_PER_GRAY) {
            points += 6.0;
        }
        else if(card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.GET_MONEY_AND_VP_PER_YELLOW) {
            points += 6.0;
        }

        for (Card card1 : me.getConstructionZone().getConstructedCards()) {
            if (card1.getColor() == CARD_COLOR_TYPE.YELLOW){points -= 1.5;}
        }

        if (me.getGold() < 3) {
            points += 2.0;
        }

        return points;
    }


    private static double civilianScore(MoveModel move, PlayerModel me, GameModel game) {
        Card card = AssetManager.getInstance().getCardByID(move.getSelectedCardID());
        if(card.getColor() != CARD_COLOR_TYPE.BLUE) {return 0;}
        if(move.getAction() != ACTION_TYPE.BUILD_CARD) {return 0;}

        double points = card.getCardEffect().getVictoryPoints();

        int playedBlues = 0;
        for (PlayerModel player : game.getPlayerList()) {
            if (player.getId() != me.getId()) {
                for (Card card1 : player.getConstructionZone().getConstructedCards()) {
                    if (card1.getColor() == CARD_COLOR_TYPE.BLUE)
                        playedBlues++;
                }
            }
            else {
                for (Card card1 : player.getConstructionZone().getConstructedCards()) {
                    if (card1.getColor() == CARD_COLOR_TYPE.BLUE)
                        playedBlues--;
                }
            }
        }

        points -= (0.20 * playedBlues);

        return points;

    }

    private static double militaryScore(MoveModel move, PlayerModel me, GameModel game) {

        Card card = AssetManager.getInstance().getCardByID(move.getSelectedCardID());
        if(card.getColor() != CARD_COLOR_TYPE.RED) {return 0;}
        if(move.getAction() != ACTION_TYPE.BUILD_CARD) {return 0;}

        double points = 0.0;
        int shields = me.getShields();
        int shieldsToGain = card.getCardEffect().getShields();
        int currentAge = game.getCurrentAge();
        int rightNeighborShields = game.getRightPlayer(me.getId()).getShields();
        int leftNeighborShields = game.getLeftPlayer(me.getId()).getShields();


        // LEFT

        if(leftNeighborShields > shields && (shields + shieldsToGain) > leftNeighborShields) {
            points += (2 *(currentAge));
        }

        else if(leftNeighborShields == shields) {
            points += ((2 *(currentAge)) - 1);
        }

        else if(leftNeighborShields < shields) {
            points += shieldsToGain;
        }

        // RIGHT

        if(rightNeighborShields > shields && (shields + shieldsToGain) > rightNeighborShields) {
            points += (2 * (currentAge));
        }

        else if(rightNeighborShields == shields) {
            points += ((2 *(currentAge)) - 1);
        }

        else if(rightNeighborShields < shields) {
            points += shieldsToGain;
        }

        return points;
    }

    private static double resourceScore(MoveModel move, PlayerModel me, GameModel game) {
        double score = 1.0;

        Card card = AssetManager.getInstance().getCardByID(move.getSelectedCardID());
        if (move.getAction() == ACTION_TYPE.BUILD_CARD && (card.getColor() != CARD_COLOR_TYPE.BROWN && card.getColor() != CARD_COLOR_TYPE.GRAY)) return 0.0;
        if (move.getAction() != ACTION_TYPE.BUILD_CARD) return 0.0;


        if (card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.PRODUCE_ONE_OF_TWO) {
            if (MoveController.getInstance().playerHasEnoughResources(card.getCardEffect().getResources(), me, new Vector<TradeAction>()).getKey()) {
                score += 2.0;
            } else {
                score += 3.5;
            }
        }

        if (card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.PRODUCE_MANUFACTURED_GOODS || card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.PRODUCE_RAW_MATERIAL) {
            if (MoveController.getInstance().playerHasEnoughResources(card.getCardEffect().getResources(), me, new Vector<TradeAction>()).getKey()) {
                score -= 1.0;
            } else {
                score += 2.5;
            }
        }

        score += (6 - game.getCurrentAge() * 3) - game.getCurrentTurn() / 2.0;

        return score;
    }

    private static double scienceScore(MoveModel move, PlayerModel me, GameModel game) {
        double score = 0.0;
        Card card = AssetManager.getInstance().getCardByID(move.getSelectedCardID());

        if (!(move.getAction() == ACTION_TYPE.UPGRADE_WONDER &&
                me.getWonder().getCurrentStage().getWonderEffect().getEffectType() == WONDER_EFFECT_TYPE.CHOOSE_ONE_SCIENCE) &&
                !(card.getColor() == CARD_COLOR_TYPE.GREEN && move.getAction() == ACTION_TYPE.BUILD_CARD)) {
            return 0;
        }

        int playedGreens = -1;
        for (Card card1 : me.getHand()) {
            if (card1.getColor() == CARD_COLOR_TYPE.GREEN)
                playedGreens++;
        }

        for (PlayerModel player : game.getPlayerList()) {
            if (player.getId() != me.getId()) {
                for (Card card1 : player.getConstructionZone().getConstructedCards()) {
                    if (card1.getColor() == CARD_COLOR_TYPE.GREEN)
                        playedGreens++;
                }
            }
        }

        score -= playedGreens / 2.0;
        score -= ScoreController.calculateScore(me.getId(), game);

        if (move.getAction() == ACTION_TYPE.UPGRADE_WONDER) {
            game.getPlayerList()[me.getId()].getWonder().upgradeStage();
            score += ScoreController.calculateScore(me.getId(), game);
            game.getPlayerList()[me.getId()].getWonder().downgradeStage();
        } else {
            game.getPlayerList()[me.getId()].getConstructionZone().getConstructedCards().add(card);
            score += ScoreController.calculateScore(me.getId(), game);
            game.getPlayerList()[me.getId()].getConstructionZone().getConstructedCards().remove(card);
        }

        return 5 + score * 2.4;
    }

    public static double guildScore(MoveModel move, PlayerModel me, GameModel game) {
        double score = 2.0;
        Card card = AssetManager.getInstance().getCardByID(move.getSelectedCardID());

        if (move.getAction() != ACTION_TYPE.BUILD_CARD) return 0;
        if (card.getColor() != CARD_COLOR_TYPE.PURPLE) return 0;

        score -= ScoreController.calculateScore(me.getId(), game);
        game.getPlayerList()[me.getId()].getConstructionZone().getConstructedCards().add(card);
        score += ScoreController.calculateScore(me.getId(), game);
        game.getPlayerList()[me.getId()].getConstructionZone().getConstructedCards().remove(card);


        return score;
    }

    public static double evaluateMove(MoveModel move, PlayerModel me, GameModel game) {
        double score = 0.0;

        score += scienceScore(move, me, game);
        score += militaryScore(move, me, game);
        score += civilianScore(move, me, game);
        score += commercialScore(move, me);
        score += resourceScore(move, me, game);
        score += guildScore(move, me, game);
        score += discardScore(move);
        score += upgradeWonderScore(move, me, game);

        return score;
    }

    private static Vector<MoveModel> generateBestMoveListSorted(PlayerModel me, GameModel game) {
        Vector<MoveModel> moves = new Vector<MoveModel>();
        for (Card card : me.getHand()) {
            for (ACTION_TYPE at : ACTION_TYPE.values()) {
                MoveModel move = new MoveModel(me.getId(), card.getId(), at);
                Pair<PlayerModel, PlayerModel> neighbors = new Pair<>(game.getLeftPlayer(me.getId()), game.getRightPlayer(me.getId()));
                var x = MoveController.getInstance().playerCanMakeMove(move, me, neighbors, true);
                if (x.getKey()) {
                    if (x.getValue() != null) {
                        for (TradeAction t : x.getValue()) {
                            move.addTrade(t);
                        }
                    }
                    moves.add(move);
                }
            }
        }

        moves.sort((moveModel, t1) -> {
            return (int) (-100.0 * evaluateMove(moveModel, me, game) + 100.0 * evaluateMove(t1, me, game));
        });
        return moves;
    }

    private static double map(double value, double begin, double end) {
        return (value - begin) / (end - begin);
    }

    private static double getAccuracyIndex(AI_DIFFICULTY difficulty) {
        if (difficulty == AI_DIFFICULTY.CHEATER)
            return 0;
        double x = Math.random();
        double l = difficulty.ordinal() + 1;
        return map(l * Math.exp(l * x), l, l * Math.exp(l));
    }

    public static MoveModel generateMove(PlayerModel me, GameModel game, AI_DIFFICULTY difficulty) {
        Vector<MoveModel> moves = generateBestMoveListSorted(me, game);
        for (MoveModel move : moves) {
            System.out.println(me.getName() + " : " + move.getSelectedCardID() + " " + move.getAction().toString());
        }
        if (moves.size() == 0) {
            return new MoveModel(me.getId(), me.getHand().firstElement().getId(), ACTION_TYPE.DISCARD_CARD);
        }
        return moves.get((int) getAccuracyIndex(difficulty) * (moves.size() - 1));
    }
}

package SevenWonders.UserInterface;

import SevenWonders.AssetManager;
import SevenWonders.SceneManager;
import SevenWonders.SoundManager;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AnimationController {

    public static void endOfAgeAnimation(GameplayController gameplayController, StackPane stackPane) {
        stackPane.setVisible(true);
        ImageView shieldImage = new ImageView(AssetManager.getInstance().getImage("shield.png"));
        stackPane.getChildren().add(shieldImage);
        StackPane.setAlignment(shieldImage, Pos.CENTER);
        StackPane.setMargin(shieldImage, new Insets(0,0,50,0));
        ImageView leftSword = new ImageView(AssetManager.getInstance().getImage("sword_left.png"));
        ImageView rightSword = new ImageView(AssetManager.getInstance().getImage("sword_right.png"));

        FadeTransition shield = new FadeTransition(Duration.millis(2000), shieldImage);
        shield.setFromValue(0.0);
        shield.setToValue(1.0);
        shield.play();

        TranslateTransition swordLeft = new TranslateTransition(Duration.millis(500), leftSword);
        swordLeft.setFromX(-500);
        swordLeft.setToX(-50);
        swordLeft.setFromY(-500);
        swordLeft.setToY(-100);
        swordLeft.play();

        TranslateTransition swordRight = new TranslateTransition(Duration.millis(500), rightSword);
        swordRight.setFromX(500);
        swordRight.setToX(50);
        swordRight.setFromY(-500);
        swordRight.setToY(-100);
        swordRight.play();

        shield.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FadeTransition pane = new FadeTransition(Duration.millis(2500), stackPane);
                pane.setFromValue(1.0);
                pane.setToValue(0.0);
                pane.play();
                pane.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        stackPane.setVisible(false);
                        FadeTransition pane = new FadeTransition(Duration.millis(10), stackPane);
                        pane.setFromValue(0.0);
                        pane.setToValue(1.0);
                        pane.play();
                        stackPane.getChildren().clear();
                    }
                });
            }
        });

        stackPane.getChildren().add(leftSword);
        stackPane.getChildren().add(rightSword);

        ImageView shieldRight, shieldLeft;
        if(gameplayController.getPlayer().getShields() < gameplayController.getRightPlayer().getShields()){
            shieldRight = new ImageView(AssetManager.getInstance().getImage("warpoint_minus1.png"));
            shieldRight.setScaleX(1.5);
            shieldRight.setScaleY(1.5);
        }
        else if(gameplayController.getPlayer().getShields() == gameplayController.getRightPlayer().getShields()){
            shieldRight = null;
        }
        else{
            if(gameplayController.gameModel.getCurrentAge() == 1){
                shieldRight = new ImageView(AssetManager.getInstance().getImage("warpoint_1.png"));
            }
            else if(gameplayController.gameModel.getCurrentAge() == 2){
                shieldRight = new ImageView(AssetManager.getInstance().getImage("warpoint_3.png"));
            }
            else if(gameplayController.gameModel.getCurrentAge() == 3){
                shieldRight = new ImageView(AssetManager.getInstance().getImage("warpoint_5.png"));
            }
            else {
                shieldRight = null;
            }

            if (shieldRight != null){
                shieldRight.setScaleY(0.8);
                shieldRight.setScaleX(0.8);
            }
        }

        if(gameplayController.getPlayer().getShields() < gameplayController.getLeftPlayer().getShields()){
            shieldLeft = new ImageView(AssetManager.getInstance().getImage("warpoint_minus1.png"));
            shieldLeft.setScaleY(1.5);
            shieldLeft.setScaleX(1.5);
        }
        else if(gameplayController.getPlayer().getShields() == gameplayController.getLeftPlayer().getShields()){
            shieldLeft = null;
        }
        else{
            if(gameplayController.gameModel.getCurrentAge() == 1){
                shieldLeft = new ImageView(AssetManager.getInstance().getImage("warpoint_1.png"));
            }
            else if(gameplayController.gameModel.getCurrentAge() == 2){
                shieldLeft = new ImageView(AssetManager.getInstance().getImage("warpoint_3.png"));
            }
            else if(gameplayController.gameModel.getCurrentAge() == 3){
                shieldLeft = new ImageView(AssetManager.getInstance().getImage("warpoint_5.png"));
            }
            else{

                shieldLeft = null;
            }

            if (shieldLeft != null){
                shieldLeft.setScaleY(0.8);
                shieldLeft.setScaleX(0.8);
            }
        }


        TranslateTransition leftShield  = new TranslateTransition(Duration.millis(1500), shieldLeft);
        leftShield.setFromX(-700);
        leftShield.setToX(-50);
        leftShield.setFromY(-100);
        leftShield.setToY(-100);
        leftShield.play();

        TranslateTransition rightShield = new TranslateTransition(Duration.millis(1500), shieldRight);
        rightShield.setFromX(700);
        rightShield.setToX(50);
        rightShield.setFromY(-100);
        rightShield.setToY(-100);
        rightShield.play();

        if(shieldLeft != null){
            stackPane.getChildren().add(shieldLeft);
        }
        if(shieldRight != null){
            stackPane.getChildren().add(shieldRight);
        }

    }

    public static void startOfAgeAnimation(GameplayController gameplayController, StackPane stackPane){
        Text ageText = new Text();
        if(gameplayController.gameModel.getCurrentAge() == 3){
            ageText.setText("GAME OVER   ");
        }
        else{
            ageText.setText("AGE " + (gameplayController.gameModel.getCurrentAge() + 1) + "   ");
        }
        ageText.setStyle("-fx-font-family: 'Assassin$';" + "-fx-font-size: 80px;" + "-fx-fill: white;" + "-fx-stroke: black;" + "-fx-stroke-width: 1px;");

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        int depth1 = 120;
        borderGlow.setWidth(depth1);
        borderGlow.setHeight(depth1);
        ageText.setEffect(borderGlow);

        TranslateTransition textAnim = new TranslateTransition(Duration.millis(2500), ageText);
        textAnim.setFromX(50);
        textAnim.setToX(50);
        textAnim.setFromY(-500);
        textAnim.setToY(-200);
        textAnim.play();
        textAnim.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FadeTransition ageFade = new FadeTransition(Duration.millis(500), ageText);
                ageFade.setFromValue(1.0);
                ageFade.setToValue(0.0);
                ageFade.play();
                if(gameplayController.gameModel.getGameFinished()) {
                    SoundManager.getInstance().stopAgeThreeMusic();
                    SoundManager.getInstance().playEndMusic();

                    var sceneAndController = AssetManager.getInstance().getSceneAndController("ScoreView.fxml");
                    ScoreViewController scoreViewController = (ScoreViewController) sceneAndController.getValue();
                    scoreViewController.gameplayController = gameplayController;
                    Parent scene = sceneAndController.getKey();

                    SceneManager.getInstance().changeScene(scene);
                }
            }
        });
        stackPane.getChildren().add(ageText);
        StackPane.setAlignment(ageText, Pos.CENTER);
    }
}
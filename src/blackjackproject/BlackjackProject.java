/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackproject;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class BlackjackProject extends Application{

    private Deck deck = new Deck();
    private Hand dealer, player;
    private Text message = new Text();

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox playerCards = new HBox(20);

     private Parent createContent() {
        dealer = new Hand(dealerCards.getChildren());
        player = new Hand(playerCards.getChildren());
        
        //font
        Font font1;
        Color darkgreen = Color.rgb(22,97,23);  
        Color lightgreen = Color.rgb(96,158,101);  
        Color darkgold = Color.rgb(52, 20, 8);
        Color lightgold = Color.rgb(108, 27, 8);
        Stop[] stops = new Stop[] { new Stop(0, darkgreen), new Stop(1, lightgreen)};
        Stop[] stops2 = new Stop[] { new Stop(0, darkgold), new Stop(1, lightgold)};
        LinearGradient lg1 = new LinearGradient(55, 0, 400, 0, false, CycleMethod.NO_CYCLE, stops2);
        LinearGradient lg2 = new LinearGradient(0, 125, 0, 500, false, CycleMethod.NO_CYCLE, stops);
        
        Image image = new Image(Card.class.getResourceAsStream("images/table.png"));
        ImagePattern imagePattern = new ImagePattern(image);

        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Region background = new Region();
        background.setPrefSize(800, 600);
        background.setStyle("-fx-background-color: rgba(101, 74, 52, 1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        Rectangle leftBG = new Rectangle(550, 560);
        leftBG.setArcWidth(30);
        leftBG.setArcHeight(30);
        leftBG.setFill(imagePattern);
        Rectangle rightBG = new Rectangle(230, 560);
        rightBG.setArcWidth(30);
        rightBG.setArcHeight(30);
        rightBG.setFill(lg1);

        // LEFT
        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text dealerScore = new Text("Dealer: ");
        Text playerScore = new Text("Player: ");
        playerScore.setStyle("-fx-font-family: Cambria; -fx-font-size: 20;");
        dealerScore.setStyle("-fx-font-family: Cambria; -fx-font-size: 20;");
        playerScore.setFill(Color.WHITE);
        dealerScore.setFill(Color.WHITE);

        leftVBox.getChildren().addAll(dealerScore, dealerCards, message, playerCards, playerScore);

        // RIGHT

        VBox rightVBox = new VBox(20);
        rightVBox.setAlignment(Pos.CENTER);

        //final TextField bet = new TextField("BET");
        //bet.setDisable(false);
        //bet.setMaxWidth(50);
        Text money = new Text("Take Your Action !");
        money.setStyle("-fx-font-family: Cambria; -fx-font-size: 25;");
        money.setFill(Color.WHITE);

        Button btnPlay = new Button("PLAY");
        Button btnHit = new Button("HIT");
        Button btnStand = new Button("STAND");

        btnPlay.setStyle("-fx-border-radius: 20; -fx-background-radius: 20;-fx-text-fill: #000000;  -fx-background-color:#e3c664; -fx-effect:dropshadow(one-pass-box,black,8,0.0,2,0);");
        btnHit.setStyle("-fx-border-radius: 20; -fx-background-radius: 20;-fx-text-fill: #000000;  -fx-background-color:#e3c664;  -fx-effect:dropshadow(one-pass-box,black,8,0.0,2,0);");
        btnStand.setStyle("-fx-border-radius: 20; -fx-background-radius: 20;-fx-text-fill: #000000; -fx-background-color:#e3c664;  -fx-effect:dropshadow(one-pass-box,black,8,0.0,2,0);");
        HBox buttonsHBox = new HBox(15, btnHit, btnStand);
        buttonsHBox.setAlignment(Pos.CENTER);

        rightVBox.getChildren().addAll( btnPlay, money, buttonsHBox);

        // ADD BOTH STACKS TO ROOT LAYOUT

        rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(rightBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        // BIND PROPERTIES

        btnPlay.disableProperty().bind(playable);
        btnHit.disableProperty().bind(playable.not());
        btnStand.disableProperty().bind(playable.not());
        
        
        
        playerScore.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.valueProperty().asString()));
        dealerScore.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.valueProperty().asString()));

        player.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

        dealer.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

        // INIT BUTTONS

        btnPlay.setOnAction(event -> {
            startNewGame();
        });

        btnHit.setOnAction(event -> {
            player.takeCard(deck.drawCard());
        });

        btnStand.setOnAction(event -> {
            while (dealer.valueProperty().get() < 17) {
                dealer.takeCard(deck.drawCard());
            }

            endGame();
        });

        return root;
    }

    private void startNewGame() {
        playable.set(true);
        message.setText("");

        deck.refill();

        dealer.reset();
        player.reset();

        dealer.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
    }

    private void endGame() {
        playable.set(false);

        int dealerValue = dealer.valueProperty().get();
        int playerValue = player.valueProperty().get();
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue;

        // the order of checking is important
        if (dealerValue == 21 || playerValue > 21 || dealerValue == playerValue
                || (dealerValue < 21 && dealerValue > playerValue)) {
            winner = "DEALER";
        }
        else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
            winner = "PLAYER";
        }

        message.setText(winner + " WON");
        message.setStyle("-fx-font-family: Cambria; -fx-font-size: 20;");
        message.setFill(Color.WHITE);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        scene.getStylesheets().add("blackjackproject/mainStyle.css");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}

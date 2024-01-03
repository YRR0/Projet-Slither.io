package fr.uparis.informatique.cpoo5.projet.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class MenuPane extends StackPane {
    private Stage primaryStage;
    private Runnable onStartGame;

    public MenuPane(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeMenu();
    }

    public void setOnStartGame(Runnable onStartGame) {
        this.onStartGame = onStartGame;
    }

    private void initializeMenu() {

        setStyle("-fx-background-color: #1c1c1c;");
        //setAlignment(Pos.CENTER);

        Button simplePlayerButton = createMenuButton("Simple Player");
        Button twoPlayerButton = createMenuButton("Two Player");
        Button onlineButton = createMenuButton("Online");

        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(simplePlayerButton, twoPlayerButton, onlineButton);

        getChildren().add(buttonBox);

        simplePlayerButton.setOnAction(e -> handleButtonClick("Simple Player"));
        twoPlayerButton.setOnAction(e -> handleButtonClick("Two Player"));
        onlineButton.setOnAction(e -> handleButtonClick("Online"));

        Scene scene = new Scene(this, 1000, 400);
        primaryStage.setScene(scene);

        primaryStage.centerOnScreen();
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 24px;");
        button.setMinSize(300, 80);
        return button;
    }

    private void handleButtonClick(String option) {
        if ("Simple Player".equals(option) && onStartGame != null) {
            onStartGame.run();
        }
    }
}

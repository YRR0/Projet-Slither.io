package fr.uparis.informatique.cpoo5.projet;
import fr.uparis.informatique.cpoo5.projet.reseau.*;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.controller.SnakeController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import fr.uparis.informatique.cpoo5.projet.reseau.SnakeGameClient;
public class SnakeGame extends Application {
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        //Game game = new Game();
        Game game = ServerLauncher.game;
        GamePane gamePane = new GamePane(game);
        SnakeController gameController = new SnakeController(game, gamePane);

        // Créer le client réseau
        SnakeGameClient gameClient = new SnakeGameClient(game);

        initializeSnakeGame(primaryStage,WIDTH,HEIGHT,gamePane,gameController);

        startGameLoop(game, gameController, gameClient);
    }

    public static void initializeSnakeGame(Stage primaryStage, int width, int height,GamePane gamePane, SnakeController gameController) {
        Scene scene = new Scene(gamePane, width, height);

        scene.setOnMouseMoved(gameController::handleMouseMove);
        scene.setOnKeyPressed(gameController::handleKeyPress);
        scene.setOnKeyReleased(gameController::handleKeyRelease);

        // Configurer la fenêtre principale
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGameLoop(Game game, SnakeController gameController, SnakeGameClient gameClient) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (game.isPaused()) {
                    gameController.update();
                    //gameClient.sendPlayerInfo();
                }
            }
        }.start();
    }

}




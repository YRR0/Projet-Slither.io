package fr.uparis.informatique.cpoo5.projet;

import fr.uparis.informatique.cpoo5.projet.view.GamePane;
import fr.uparis.informatique.cpoo5.projet.view.MenuPane;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.controller.SnakeController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SnakeGame extends Application {

    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
    private Stage primaryStage;

    public static void main(String[] args) {
        System.out.println("Launching application...");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Starting application...");
            this.primaryStage = primaryStage;
            showMenu(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMenu(Stage stage) {
        System.out.println("Showing menu...");

        MenuPane menuPane = new MenuPane(stage);
        menuPane.setOnStartGame(() -> startSimplePlayerGame());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        System.out.println("Menu displayed.");
    }

    private void startSimplePlayerGame() {
        // Initialiser le jeu et le panneau de jeu
        Game game = new Game();
        GamePane gamePane = new GamePane(game);
        SnakeController gameController = new SnakeController(game, gamePane);

        Stage gameStage = new Stage();
        gameStage.initStyle(StageStyle.UNDECORATED);
        Scene gameScene = new Scene(gamePane, WIDTH, HEIGHT);
        // Gérer les mouvements de la souris pour changer la direction du serpent
        gameScene.setOnMouseMoved(gameController::handleMouseMove);
        gameScene.setOnKeyPressed(gameController::handleKeyPress);
        gameScene.setOnKeyReleased(gameController::handleKeyRelease);

        gameStage.setScene(gameScene);

        primaryStage.close();

        // Démarrer la boucle de jeu
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (game.isPaused()) {
                    // Mettre à jour l'état du jeu et rendre le panneau de jeu
                    gameController.update();
                }
            }
        }.start();

        // Afficher le nouveau stage pour le jeu
        gameStage.show();
    }
}

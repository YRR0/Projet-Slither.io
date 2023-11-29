package fr.uparis.informatique.cpoo5.projet;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;
import fr.uparis.informatique.cpoo5.projet.controller.SnakeController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import fr.uparis.informatique.cpoo5.projet.model.Game;

// La classe principale de l'application, étendant la classe Application de JavaFX
public class SnakeGame extends Application {

    // Définir la largeur et la hauteur de la fenêtre du jeu
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    // La méthode principale qui lance l'application
    public static void main(String[] args) {
        launch(args);
    }

    // La méthode start() est appelée au lancement de l'application
    @Override
    public void start(Stage primaryStage) {
        // Initialiser le jeu et le panneau de jeu
        Game game = new Game();
        GamePane gamePane = new GamePane(game);
        SnakeController gameController = new SnakeController(game, gamePane);


        // Créer une scène avec le panneau de jeu
        Scene scene = new Scene(gamePane, WIDTH, HEIGHT);

        // Gérer les mouvements de la souris pour changer la direction du serpent
        scene.setOnMouseMoved(gamePane::handleMouseMove);

        // Configurer la fenêtre principale
        primaryStage.initStyle(StageStyle.UNDECORATED); // Masquer les décorations de la fenêtre
        primaryStage.setScene(scene);
        primaryStage.show();

        // Démarrer la boucle de jeu
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Mettre à jour l'état du jeu et rendre le panneau de jeu
                game.update();
                gamePane.render();
            }
        }.start();
    }
}




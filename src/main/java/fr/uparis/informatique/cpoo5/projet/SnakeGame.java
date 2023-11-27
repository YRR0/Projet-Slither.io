package fr.uparis.informatique.cpoo5.projet;
import fr.uparis.informatique.cpoo5.projet.view.SnakeView;
import fr.uparis.informatique.cpoo5.projet.controller.SnakeController;
import fr.uparis.informatique.cpoo5.projet.model.SnakeModel;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SnakeGame extends Application {

    private final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    @Override
    public void start(Stage primaryStage) throws Exception {
        SnakeModel model = new SnakeModel();
        SnakeView view = new SnakeView(primaryStage, model);
        SnakeController controller = new SnakeController(model, view);

        primaryStage.show();

        // Set up the game loop
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(130), e -> controller.run()));
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
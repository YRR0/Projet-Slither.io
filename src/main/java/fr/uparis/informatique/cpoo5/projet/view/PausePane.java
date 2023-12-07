package fr.uparis.informatique.cpoo5.projet.view;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;

public class PausePane extends StackPane {
    private Label pauseLabel;
    public PausePane() {
        // Créer une étiquette pour afficher le message de pause
        Button boutonScene1 = new Button("Reprendre");
        pauseLabel = new Label("Pause");
        pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));


        // Ajouter l'étiquette à la pile
        getChildren().add(pauseLabel);

        // Masquer l'écran de pause au départ
        setVisible(true);
    }

    // Méthode pour afficher ou masquer l'écran de pause
    public void setPause(boolean isPaused) {
        setVisible(isPaused);
    }
}

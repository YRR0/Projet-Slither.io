package fr.uparis.informatique.cpoo5.projet.view;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PausePane extends StackPane {
    private Label pauseLabel;
    public PausePane() {
        // Créer une étiquette pour afficher le message de pause
        pauseLabel = new Label("Pause");
        pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        // Ajouter l'étiquette à la pile
        getChildren().add(pauseLabel);

        // Masquer l'écran de pause au départ
        setVisible(false);
    }

    // Méthode pour afficher ou masquer l'écran de pause
    public void setPause(boolean isPaused) {
        setVisible(isPaused);
    }
}

package pdp.scrabble.view.Component;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class ActionHistory extends StackPane {
  private final VBox messageContainer;

  public ActionHistory() {
    // Conteneur pour les messages
    messageContainer = new VBox();
    messageContainer.setSpacing(5); // Espacement entre les messages
    messageContainer.setStyle("-fx-padding: 10;"); // Ajout de marges internes

    // ScrollPane pour permettre le défilement
    ScrollPane scrollPane = new ScrollPane(messageContainer);
    scrollPane.setHbarPolicy(
        ScrollPane.ScrollBarPolicy
            .AS_NEEDED); // Activer la barre de défilement horizontale si nécessaire
    scrollPane.setVbarPolicy(
        ScrollPane.ScrollBarPolicy
            .AS_NEEDED); // Activer la barre de défilement verticale si nécessaire
    scrollPane.setStyle("-fx-background: lightgray; -fx-border-color: black;");

    // Configuration de la taille du StackPane
    this.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
    this.setPrefSize(200, 400); // Taille du rectangle
    this.getChildren().add(scrollPane);
  }

  /**
   * Ajoute un message à l'historique.
   *
   * @param message Le message à afficher dans l'historique.
   */
  public void addMessage(String message) {
    // Ajouter un séparateur uniquement si ce n'est pas le premier message
    if (!messageContainer.getChildren().isEmpty()) {
      Line separator = new Line(0, 0, 180, 0); // Ligne horizontale
      separator.setStyle("-fx-stroke: gray; -fx-stroke-width: 1;");
      messageContainer.getChildren().add(separator);
    }

    // Créer un label pour le message
    Label messageLabel = new Label(message);
    messageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: normal;"); // Style des messages

    // Ajouter le message au conteneur
    messageContainer.getChildren().add(messageLabel);

    // Scroll automatiquement vers le bas pour afficher le dernier message
    ScrollPane scrollPane = (ScrollPane) this.getChildren().get(0);
    scrollPane.layout(); // Assure que la mise en page est à jour
    scrollPane.setVvalue(1.0); // Positionne le curseur tout en bas
  }

  /** Efface tout l'historique. */
  public void clearHistory() {
    messageContainer.getChildren().clear();
  }
}

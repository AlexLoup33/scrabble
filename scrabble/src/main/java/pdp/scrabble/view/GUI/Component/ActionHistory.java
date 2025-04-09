package pdp.scrabble.view.GUI.Component;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class ActionHistory extends StackPane {
  private final VBox messageContainer;

  public ActionHistory() {
    // Containers for messages
    messageContainer = new VBox();
    messageContainer.setSpacing(5); // Space between messages
    messageContainer.setStyle("-fx-padding: 10;"); //

    // ScrollPane to scroll bar
    ScrollPane scrollPane = new ScrollPane(messageContainer);
    scrollPane.setHbarPolicy(
        ScrollPane.ScrollBarPolicy.AS_NEEDED); // Activate vertical scroll bar if needed
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Same horizontal scroll bar
    scrollPane.setStyle("-fx-background: lightgray; -fx-border-color: black;");

    // Configuration size of StackPane
    this.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
    this.setPrefSize(200, 400);
    this.getChildren().add(scrollPane);
  }

  /**
   * add a message to the history
   *
   * @param message the message readed
   */
  public void addMessage(String message) {
    // Add unique separator if not first message
    if (!messageContainer.getChildren().isEmpty()) {
      Line separator = new Line(0, 0, 180, 0); // horizontale ligne
      separator.setStyle("-fx-stroke: gray; -fx-stroke-width: 1;");
      messageContainer.getChildren().add(separator);
    }

    // Create a label for a message
    Label messageLabel = new Label(message);
    messageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: normal;"); // Style of messages

    // Add a message to container
    messageContainer.getChildren().add(messageLabel);

    // Scroll down automatically to display the latest message
    ScrollPane scrollPane = (ScrollPane) this.getChildren().get(0);
    scrollPane.layout(); // Make sure the layout is up to date
    scrollPane.setVvalue(1.0); // Put the cursor down
  }

  /** Clear the history */
  public void clearHistory() {
    messageContainer.getChildren().clear();
  }
}

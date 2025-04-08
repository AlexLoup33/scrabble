package pdp.scrabble.view.GUI.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pdp.scrabble.game.Player;

public class ShowScore extends VBox {
  private final int boxWidth = 200;
  private final int boxHeight = 50;

  private final Map<Player, StackPane> playerBoxes = new HashMap<>();

  public ShowScore() {
    this.setSpacing(10);
    this.setStyle("-fx-alignment: center;");
  }

  /**
   * The `updateScores` function clears the existing player boxes and updates the scores for each
   * player in the provided list.
   *
   * @param players A list of Player objects that contains the players whose scores need to be
   *     updated.
   */
  public void updateScores(List<Player> players) {
    this.getChildren().clear();
    playerBoxes.clear();
    for (Player player : players) {
      updateScore(player);
    }
  }

  /**
   * The `updateScore` function updates the score displayed for a player in a GUI by either creating
   * a new player box or updating the existing player's score label.
   *
   * @param player The `player` parameter in the `updateScore` method represents an instance of the
   *     `Player` class. It is used to update the score displayed for a specific player in a user
   *     interface.
   */
  public void updateScore(Player player) {
    StackPane playerBox = playerBoxes.get(player);

    if (playerBox == null) {
      playerBox = createPlayerBox(player);
      playerBoxes.put(player, playerBox);
      this.getChildren().add(playerBox);
    } else {
      Label playerLabel = (Label) playerBox.getChildren().get(0);
      playerLabel.setText(player.getName() + ": " + player.getScore());
    }
  }

  /**
   * The function creates a StackPane containing a Label with the player's name and score displayed
   * in a specified style.
   *
   * @param player The `createPlayerBox` method you provided creates a visual representation of a
   *     player in a game. It creates a `StackPane` with a light gray background and a black border,
   *     displaying the player's name and score in a bold label at the center of the pane.
   * @return The method `createPlayerBox(Player player)` returns a `StackPane` object that
   *     represents a graphical box displaying the player's name and score.
   */
  private StackPane createPlayerBox(Player player) {
    StackPane playerBox = new StackPane();
    playerBox.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
    playerBox.setPrefSize(boxWidth, boxHeight);

    String playerInfo = player.getName() + ": " + player.getScore();
    Label playerLabel = new Label(playerInfo);
    playerLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
    playerLabel.setAlignment(Pos.CENTER);

    playerBox.getChildren().add(playerLabel);

    return playerBox;
  }
}

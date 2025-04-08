package pdp.scrabble.view.GUI.Component;

import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.Player;

public class DisplayTurn extends HBox {
  private final Label turnLabel;
  private final Label playerLabel;
  private final ResourceBundle langPack;

  public DisplayTurn(Lang lang) {
    langPack = lang.getViewGUI_langPack();
    turnLabel = new Label(langPack.getString("turn"));
    playerLabel = new Label(langPack.getString("player"));

    turnLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    playerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    this.getChildren().addAll(turnLabel, playerLabel);
    this.setSpacing(10);
    this.setStyle("-fx-alignment: center;");
  }

  public void updateTurn(int turn, Player currentPlayer) {
    this.getChildren().clear();
    turnLabel.setText(langPack.getString("turn") + " " + turn + " |");
    playerLabel.setText(langPack.getString("player") + " " + currentPlayer.getName());
    this.getChildren().addAll(turnLabel, playerLabel);
  }
}

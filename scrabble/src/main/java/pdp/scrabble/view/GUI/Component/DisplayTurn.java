package pdp.scrabble.view.GUI.Component;

import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import pdp.scrabble.game.Lang;
import pdp.scrabble.game.Player;

public class DisplayTurn extends HBox {
  private final Label turnLabel;
  private final Label playerLabel;
  private final Label drawerLabel;
  private final ResourceBundle langPack;

  public DisplayTurn(Lang lang) {
    langPack = lang.getViewGUILangPack();
    turnLabel = new Label(langPack.getString("turn"));
    playerLabel = new Label(langPack.getString("player"));
    drawerLabel = new Label(langPack.getString("drawer"));

    turnLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    playerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    drawerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    this.getChildren().addAll(turnLabel, playerLabel, drawerLabel);
    this.setSpacing(10);
    this.setStyle("-fx-alignment: center;");
  }

  public void updateTurn(int turn, Player currentPlayer, int drawerSize) {
    this.getChildren().clear();
    turnLabel.setText(langPack.getString("turn") + " " + turn + " |");
    playerLabel.setText(langPack.getString("player") + " " + currentPlayer.getName() + " |");
    drawerLabel.setText(langPack.getString("drawer") + " " + drawerSize);
    this.getChildren().addAll(turnLabel, playerLabel, drawerLabel);
  }
}

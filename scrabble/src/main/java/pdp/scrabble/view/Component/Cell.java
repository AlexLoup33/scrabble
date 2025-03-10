package pdp.scrabble.view.Component;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import pdp.scrabble.game.ScoreMultiplier;

public class Cell extends Pane {
  private final int x;
  private final int y;
  private final ScoreMultiplier scoreMultiplier;

  private String letter;
  private boolean isOccupied;

  private int score;

  public Cell(int x, int y, ScoreMultiplier scoreMultiplier, boolean isOccupied) {
    this.x = x;
    this.y = y;
    this.scoreMultiplier = scoreMultiplier;
    this.isOccupied = isOccupied;
    this.letter = "";
    this.score = 0;

    Text lett = new Text(letter);
    lett.setStyle("-fx-font-size: 20px;");
    this.getChildren().add(lett);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public ScoreMultiplier getScoreMultiplier() {
    return scoreMultiplier;
  }

  public String getLetter() {
    return letter;
  }

  public void setLetter(String letter) {
    this.getChildren().clear();
    this.letter = letter;
    Text lett = new Text(letter);
    lett.setStyle("-fx-font-size: 20px;");
    this.getChildren().add(lett);
  }

  public boolean isOccupied() {
    return isOccupied;
  }

  public void setOccupied(boolean occupied) {
    isOccupied = occupied;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return "Cell{"
        + "x="
        + x
        + ", y="
        + y
        + ", scoreMultiplier="
        + scoreMultiplier
        + ", letter='"
        + letter
        + '\''
        + ", isOccupied="
        + isOccupied
        + ", score="
        + score
        + '}';
  }
}

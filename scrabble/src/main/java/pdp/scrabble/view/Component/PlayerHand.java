package pdp.scrabble.view.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter; 
import pdp.scrabble.dictionary.LettersPoints;
import pdp.scrabble.game.Lang;

public class PlayerHand extends HBox {
    private final LettersPoints pointsPerLetter;
    private final int squareSize = 70;

    public PlayerHand(Lang lang) {
        this.setSpacing(10);
        this.setStyle("-fx-alignment: center;"); // Center the HBox
        this.pointsPerLetter = lang.getLettersPoints();
        
    }

    public void updateLetters(LettersCounter letters) {
        this.getChildren().clear();
        for (Letter letter : letters.getLettersSet()) {
            for (int i = 0; i < letters.getLetterCount(letter); i++) {
                // Create the square (tile)
                StackPane letterBox = new StackPane();
                letterBox.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
                letterBox.setPrefSize(squareSize, squareSize); // Square size for each tile

                // Display the letter at the center of the square
                Label letterLabel = new Label(letter.toString());
                letterLabel.setStyle("-fx-font-size: " + (squareSize / 2) + "; -fx-font-weight: bold;");
                letterLabel.setAlignment(Pos.CENTER); // Center the letter in the StackPane

                // Display the score of the letter at the bottom right
                Label scoreLabel = new Label(String.valueOf(this.pointsPerLetter.getLetterCount(letter)));
                scoreLabel.setStyle("-fx-font-size: " + (squareSize / 4) + ";");

                // Add the score at the bottom right
                StackPane.setAlignment(scoreLabel, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(scoreLabel, new Insets(0, 2, 0, 0)); // Offset the score slightly from the edge

                // Add the letter and score to the StackPane
                letterBox.getChildren().addAll(letterLabel, scoreLabel);

                // Add the tile to the HBox representing the player's hand
                this.getChildren().add(letterBox);
            }
        }
    }
}

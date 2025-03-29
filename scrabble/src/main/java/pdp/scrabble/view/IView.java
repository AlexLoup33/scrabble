package pdp.scrabble.view;

import java.util.List;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.LettersDrawer;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.actions.GameAction;

public interface IView {
  /**
   * This function asks a player for their action during a specific turn in a game.
   *
   * @param player The player parameter represents the player who is being asked to take a game
   *     action.
   * @param turn The `turn` parameter represents the current turn number in the game. It is an
   *     Integer value that indicates which turn the game is currently on.
   * @return The method "askAction" is likely returning a GameAction object, which represents the
   *     action chosen by the player for a specific turn in the game.
   */
  public GameAction askAction(Player player, Integer turn);

  /**
   * This Java function displays a grid, possibly for a game of Super Scrabble.
   *
   * @param grid The `grid` parameter is likely an object representing a grid or game board. It
   *     could be a data structure that holds information about the layout of the grid, such as the
   *     positions of tiles or cells.
   * @param isSuperScrabble The parameter `isSuperScrabble` is a boolean value that indicates
   *     whether the game being played is a Super Scrabble game or not. If `isSuperScrabble` is
   *     true, it means the game is Super Scrabble; if it is false, it means the game is regular
   */
  public void displayGrid(Grid grid, Boolean isSuperScrabble);

  /**
   * The function displayPlayerLetters takes a LettersCounter object as a parameter and displays the
   * player's letters.
   *
   * @param letters The `displayPlayerLetters` method takes a `LettersCounter` object as a parameter
   *     named `letters`. This object likely contains information about the letters that a player
   *     has in a game or some other context. You can use the `letters` object within the method to
   *     display or manipulate the player's letters
   */
  public void displayPlayerLetters(LettersCounter letters);

  /**
   * The function `getFromLangPack` in Java retrieves a string value from a language pack based on
   * the provided identifier.
   *
   * @param identifiant The `identifiant` parameter is a unique identifier used to retrieve a
   *     specific string or value from a language pack or resource bundle. This identifier is
   *     typically associated with a particular text or message in the language pack, allowing the
   *     `getFromLangPack` method to fetch the corresponding translation or content for that
   * @return A String value from a language pack corresponding to the given identifier.
   */
  public String getFromLangPack(String identifiant);

  /**
   * This Java function is designed to display the score of a specific player.
   *
   * @param player The parameter "player" in the method "showScore" is of type Player. This means
   *     that the method expects an object of the Player class to be passed as an argument when it
   *     is called.
   */
  public void showScore(Player player);

  /**
   * This function displays the scores of a list of players.
   *
   * @param players A list of Player objects representing the players whose scores you want to
   *     display.
   */
  public void showScore(List<Player> players);

  /**
   * This Java function is named displayDraw and takes an object of type LettersDrawer as a
   * parameter.
   *
   * @param draw The parameter `draw` is an object of type `LettersDrawer`. This object likely
   *     contains methods and properties related to drawing letters or text on a display or screen.
   *     You can use this object to draw letters or text in a specific way as needed by the
   *     `displayDraw` method.
   */
  public void displayDraw(LettersDrawer draw);

  /**
   * This function announces the turn of a player in a game.
   *
   * @param currentPlayer The current player who is taking their turn.
   * @param turn The `turn` parameter in the `announceTurn` method represents the current turn
   *     number in the game. It is an integer value that indicates which turn is currently being
   *     played.
   */
  public void announceTurn(Player currentPlayer, int turn);

  public void showRemainingTimes(List<Player> players);

  /**
   * This Java function showError takes a CustomError object as a parameter to handle and display
   * errors.
   *
   * @param e The parameter "e" in the method "showError" is of type CustomError. This parameter is
   *     used to pass an instance of the CustomError class to the method for handling and displaying
   *     error information.
   */
  public void showError(CustomError e);

  /**
   * This function is used to display an error message along with the details of the exception that
   * occurred.
   *
   * @param e The parameter "e" in the method "showError" is of type Throwable, which means it can
   *     hold an object that represents an error or exception that occurred during the program
   *     execution.
   */
  public void showError(Throwable e);

  // public void showAbout(); //FIXME: J'ai pas compris alors help me

  public void initialize(Configuration conf);
}

package pdp.scrabble.game;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.History.History;
import pdp.scrabble.game.actions.*;
import pdp.scrabble.game.serializer.Serializer;

public class GameModel {
  public final RandomAPI rngGenerator;
  public final Grid grid;
  public final Lang lang;
  public final List<Player> players;
  public final LettersDrawer draw; // draw = pioche en fr
  public final IDictionary dictionary;
  public final Configuration config;
  private Player currentPlayer;
  private History<GameAction> history;
  private int gameTurn;
  private int everyonePassedTurnStreak;
  public int nextPlayerID;

  public GameModel(Configuration config) {
    this.config = (Configuration) config;
    this.rngGenerator = new RandomAPI(this.config.getSeed());
    this.history = new History<>();

    this.nextPlayerID = 0;
    this.lang = Lang.getLang(this.config.getLanguage());
    this.draw = new LettersDrawer(this.lang, this.rngGenerator, this.config.getSuperScrabble());
    this.players = new ArrayList<>(this.config.getPlayerCount());

    for (int i = 0; i < this.config.getPlayerCount(); i++) {
      this.players.add(new Player(nextPlayerID, this.config.getBlitzTime()));
      drawLetters(this.players.get(i));
      this.nextPlayerID++;
    }

    if (this.config.getDictFile() != null) {
      this.lang.set_customDictPath(this.config.getDictFile());
    }

    this.dictionary = new Dictionary();
    this.dictionary.loadFromFile(this.lang.getDictionaryPath());

    this.grid = new Grid(this, (config.getSuperScrabble() ? 21 : 15));
    this.currentPlayer = this.players.get(0);
    this.gameTurn = 1;
    this.everyonePassedTurnStreak = 0;
  }

  public void reset() {
    this.rngGenerator.reset(); // way safer to keep this line the first !!
    this.grid.reset();
    this.draw.reset();
    for (Player player : this.players) {
      player.reset();
      drawLetters(player);
    }
    this.currentPlayer = this.players.get(0);
    this.gameTurn = 1;
    this.everyonePassedTurnStreak = 0;
    // TODO: replace manual reset by undo all
    this.history.clear();
  }

  private void undoOne() {
    GameAction toUndo = this.history.undoOne();
    // undo the played time
    toUndo.player.undoTurnPlayedTime(toUndo);
    toUndo.undo(this); // double dispatch
  }

  private void redoOne() {
    GameAction toRedo = this.history.redoOne();
    // no needs to redo the played time here
    this.applyGameActionDispatch(toRedo, toRedo.duration);
  }

  /**
   * dispatch the game action to the correct methode of applyGameAction
   *
   * @param played the game action to apply
   * @param duration the time taken by the player to decide its action
   * @return whether the turn is finished
   */
  public void applyGameActionDispatch(GameAction played, long duration) {
    played.player.addTurnPlayedTime(duration);
    played.duration = duration;
    played.applyGameAction(this); // use double dispatch
    // add nothing here !
  }

  public void applyGameAction(RenamePlayerAction renamePlayer) {
    renamePlayer.player.rename(renamePlayer.newName);
    history.addPlayedAction(renamePlayer);
  }

  // F33
  public void applyGameAction(PassTurnAction passTurn) {
    passTurn.player.playerPassedTurn();
    history.addPlayedAction(passTurn);
    this.endPlayerTurn();
  }

  // F34
  public void applyGameAction(ExchangeLetterAction exchLtrs) {
    exchLtrs.assertHasLettersToPlace();
    exchLtrs.rngStateBefore = rngGenerator.getCurrentState();
    exchLtrs.exchangeLetters(this.draw);
    history.addPlayedAction(exchLtrs);
    this.endPlayerTurn();
  }

  // F36
  public void applyGameAction(ForfeitGameAction ffGame) {
    ffGame.player.forfeit();
    history.addPlayedAction(ffGame);
    this.endPlayerTurn();
  }

  public void applyGameAction(PlaceWordAction placeWord) {
    placeWord.assertHasLettersToPlace();
    List<OwnedWord> scoredWords = this.grid.placeLetters(placeWord);
    placeWord.removeLettersPlaced();
    placeWord.rngStateBefore = rngGenerator.getCurrentState();
    placeWord.drawLetters(this.draw);
    for (OwnedWord ownedWord : scoredWords) {
      int wordScore = this.grid.getScore(ownedWord);
      Utilities.verbose(
          " -> placed the word: " + ownedWord.word.toString() + " (" + wordScore + " pts)\n");
      placeWord.player.addScore(wordScore);
    }
    {
      int bonus = this.grid.getBonusScore(placeWord.toPlace);
      if (bonus != 0) {
        placeWord.player.addScore(bonus);
        Utilities.verbose(" -> scored a scrable: +" + bonus + " pts)\n");
      }
    }
    this.grid.useMultiplicators(scoredWords);
    history.addPlayedAction(placeWord);
    this.endPlayerTurn();
  }

  // F41
  public void applyGameAction(RestartGameAction restart) {
    this.reset();
  }

  public void applyGameAction(QuitAction quit) {
    if (quit.isSaveWanted) {
      try {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String saveLocation = "./saves/" + now.toString() + ".txt";
        Serializer.saveCurrentToFile(Paths.get(saveLocation), this);
        Utilities.debug('m', "%s was saved");
      } catch (IOException e) {
        throw new CustomError(ErrorType.IO, "save directory is not accessible", e.getMessage());
      }
    }
    System.exit(0);
  }

  public void applyGameAction(UndoAction undoAction) {
    this.undoOne();
  }

  public void applyGameAction(RedoAction redoAction) {
    this.redoOne();
  }

  /**
   * This function returns a list of active players by filtering out players who have forfeited.
   *
   * @return The method `getActivePlayers` returns a list of active players, which are players who
   *     have not forfeited the game.
   */
  public List<Player> getActivePlayers() {
    List<Player> activePlayers = new ArrayList<>();
    for (Player player : players) {
      if (!player.getHasForfeited()) {
        activePlayers.add(player);
      }
    }
    return activePlayers;
  }

  public void endPlayerTurn() {
    if (this.getCurrentPlayer().getRemainingTime() <= 0 && this.config.blitz()) {
      Utilities.verbose("Player " + this.getCurrentPlayer().getName() + " has no time left !\n");
      this.getCurrentPlayer().forfeit();
    }

    if (this.getActivePlayers().isEmpty()) {
      this.gameTurn += 1;
      return; // avoid infinite loop
    }

    // => safe to loop (there is at least an active player to select)
    // select the next player to play and update the game turn
    do {
      int currentPlayerIndex = this.players.indexOf(this.currentPlayer);
      int nextPlayerIndex = (currentPlayerIndex + 1) % this.players.size();
      if (nextPlayerIndex == 0) {
        // update the game turn (can't loop twice)
        this.endGameTurn();
      }
      this.currentPlayer = this.players.get(nextPlayerIndex);
    } while (this.currentPlayer.getHasForfeited());
  }

  /** to be called when all players finished there turn */
  private void endGameTurn() {
    if (this.getActivePlayers().stream().allMatch(Player::hasPassed)) {
      // => every remaining players has passed
      this.everyonePassedTurnStreak += 1;
    } else {
      // => one of the remaining players has played
      this.everyonePassedTurnStreak = 0;
    }

    for (Player player : this.players) {
      player.endGameTurn();
    }
    this.gameTurn += 1;
  }

  public void drawLetters(Player p) {
    if (this.draw.getCurrentSize() < 7) {
      throw new Error("[BUG] draw letter count is strictly under 7");
    }
    for (int i = p.bag.size(); i < 7; i++) {
      p.bag.addLetter(this.draw.popLetter());
    }
  }

  public int getTurn() {
    return this.gameTurn;
  }

  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  public int nbEveryonePassedTurn() {
    return this.everyonePassedTurnStreak;
  }

  public String getSerializedHistory() {
    return this.history.toSerializedString();
  }

  public List<GameAction> getHistory() {
    return this.history.getCurrentActionsList();
  }

  public void setHistory(History<GameAction> history) {
    this.history = history;
  }

  // *** all the dispatched undo ***

  public void undoGameAction(ExchangeLetterAction exchangeLetterAction) {
    throw new UnsupportedOperationException("Unimplemented method 'undoGameAction'");
    // exchangeLetterAction.lettersDrawed
  }

  public void undoGameAction(ForfeitGameAction forfeitGameAction) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'undoGameAction'");
  }

  public void undoGameAction(PassTurnAction passTurnAction) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'undoGameAction'");
  }

  public void undoGameAction(PlaceWordAction placeWordAction) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'undoGameAction'");
  }

  public void undoGameAction(RenamePlayerAction renamePlayerAction) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'undoGameAction'");
  }
}

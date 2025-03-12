package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.game.actions.*;

public class GameModel {
  public final Grid grid;
  public final Lang lang;
  public final List<Player> players;
  public final LettersDrawer draw; // draw = pioche en fr
  public final IDictionary dictionary;
  public final Configuration config;
  public int currentPlayer;

  public GameModel(IConfiguration config) {
    this.config = (Configuration) config;

    this.players = new ArrayList<>(this.config.getPlayerCount());
    for (int i = 0; i < this.config.getPlayerCount(); i++) {
      this.players.add(new Player());
    }
    this.dictionary = new Dictionary();
    this.dictionary.loadFromFile(config.getDictFile());

    this.lang = Lang.getLang(this.config.getLanguage());
    this.draw = new LettersDrawer(this.lang);

    if (!config.getSuperScrabble()) {
      this.grid = new Grid(this, 15);
    } else {
      this.grid = new Grid(this, 21);
    }
    this.currentPlayer = 0;
  }

  public void reset() {
    this.grid.reset();
    this.draw.reset();
    for (Player player : this.players) {
      player.reset();
    }
  }

  /**
   * dispatch the game action to the correct methode of applyGameAction
   *
   * @param played
   * @return whether the turn is finished
   */
  public void applyGameActionDispatch(GameAction played) {
    played.applyGameAction(this); // use double dispatch
  }

  public void applyGameAction(RenamePlayerAction rpa) {
    rpa.player.rename(rpa.newName);
  }

  // F33
  public void applyGameAction(PassTurnAction pta) {
    this.currentPlayer = getNextPlayerIndex();
  }

  public void applyGameAction(ExchangeLetterAction exchangeLetterAction) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'applyGameAction'");
  }

  // F36
  public void applyGameAction(ForfeitGameAction forfeitGameAction) {
    forfeitGameAction.player.forfeit();
    this.currentPlayer = getNextPlayerIndex();
  }

  public void applyGameAction(PlaceWordAction pwa) {
    pwa.assertHasLettersToPlace();
    this.grid.placeLetters(pwa);
    pwa.removeLettersPlaced();
    this.currentPlayer = getNextPlayerIndex();
  }

  // F41
  public void applyGameAction(RestartGameAction restartGameAction) {
    this.reset();
    this.currentPlayer = getNextPlayerIndex();
  }

  /**
   * This function returns a list of active players by filtering out players who have forfeited.
   *
   * <p><<<<<<< HEAD
   *
   * @return The method `getActivePlayers` returns a list of active players, which are players who
   *     have not forfeited the game. =======
   * @return The method `getActivePlayers` returns a list of active players, which are players who
   *     have not forfeited the game. >>>>>>> 3751731ee445fa8332134eb82ab31f8e1424469a
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

  public Player getCurrentActivePlayer() {
    Player next = this.players.get(currentPlayer);
    while (next.getHasForfeited()) {
      this.currentPlayer = getNextPlayerIndex();
      next = this.players.get(currentPlayer);
    }
    return next;
  }

  public int getNextPlayerIndex() {
    return (this.currentPlayer + 1) % (config.getPlayerCount());
  }
}

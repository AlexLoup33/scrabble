package pdp.scrabble.game;

import static pdp.scrabble.errorsHandling.ErrorType.PlayerDoesntHaveLettersInIsBag;

import java.util.ArrayList;
import java.util.List;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.dictionary.LettersCounter;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.actions.*;

public class GameModel {
  public final Grid grid;
  public final Lang lang;
  public final List<Player> players;
  public final LettersDrawer draw; // draw = pioche en fr
  public final IDictionary dictionary;
  public final Configuration config;

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
  public boolean applyGameActionDispatch(GameAction played) {
    return played.applyGameAction(this); // use double dispatch
  }

  public boolean applyGameAction(RenamePlayerAction rpa) {
    rpa.player.rename(rpa.newName);
    return false;
  }

  public boolean applyGameAction(PassTurnAction pta) {
    return true;
  }

  public boolean applyGameAction(ExchangeLetterAction exchangeLetterAction) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'applyGameAction'");
  }

  public boolean applyGameAction(ForfeitGameAction forfeitGameAction) {
    forfeitGameAction.player.forfeit();
    return true;
  }

  public boolean applyGameAction(PlaceWordAction pwa) {
    pwa.assertHasLettersToPlace();
    this.grid.placeLetters(pwa);
    pwa.removeLettersPlaced();
    return true;
  }

  public boolean applyGameAction(RestartGameAction restartGameAction) {
    this.reset();
    return true;
  }

  /**
   * This function returns a list of active players by filtering out players who
   * have forfeited.
   *
   * @return The method `getActivePlayers` returns a list of active players, which
   *         are players who have not forfeited the game.
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
}

package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;
import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.configuration.IConfiguration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.game.actions.GameAction;
import pdp.scrabble.game.actions.PassTurnAction;
import pdp.scrabble.game.actions.PlaceWordAction;
import pdp.scrabble.game.actions.RenamePlayerAction;

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

  public boolean applyGameAction(GameAction played) {
    if (played instanceof PlaceWordAction) {
      try {
        grid.placeLetters((PlaceWordAction) played);
        return true;
      } catch (CustomError e) {
        System.out.println(e);
        // view.showError(e.toUserFormatedText()); NOT IMPLEMENTED
        return false;
      }
    }
    if (played instanceof RenamePlayerAction) {
      try {
        RenamePlayerAction renaming = (RenamePlayerAction) played;
        renaming.player.rename(renaming.newName);
        return true;
      } catch (CustomError e) {
        System.out.println(e);
        // view.showError(e.toUserFormatedText()); NOT IMPLEMENTED
        return false;
      }
    }
    Utilities.debug('l', "%s is playing a non implemented action\n", played.player);
    return false;
  }

  public boolean applyGameAction(RenamePlayerAction rpa) {
    rpa.player.rename(rpa.newName);
    return false;
  }

  public boolean applyGameAction(PassTurnAction pta) {
    // rien a faire normalement (vérifiez svp)
    // puis change de joueur (si il n'y a pas eut de problèmes)
    return true;
  }

  //    public void applyGameAction(ForfeitAction fa) {
  //        // forfeit le joueur (avec la méthode de joueur)
  //        // puis change de joueur (si il n'y a pas eut de problèmes)
  //    }

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
}

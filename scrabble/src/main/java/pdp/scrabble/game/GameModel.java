package pdp.scrabble.game;

import java.util.ArrayList;
import java.util.List;

import pdp.scrabble.Utilities;
import pdp.scrabble.configuration.Configuration;
import pdp.scrabble.dictionary.Dictionary;
import pdp.scrabble.dictionary.IDictionary;
import pdp.scrabble.game.actions.*;

public class GameModel {
    private final RandomAPI rngGenerator;
    public final Grid grid;
    public final Lang lang;
    public final List<Player> players;
    public final LettersDrawer draw; // draw = pioche en fr
    public final IDictionary dictionary;
    public final Configuration config;
    private Player currentPlayer;
    private int gameTurn;
    private int everyonePassedTurnStreak;
    public int nextPlayerID;

    public GameModel(Configuration config) {
        this.config = (Configuration) config;
        this.rngGenerator = new RandomAPI(this.config.getSeed());

        this.nextPlayerID = 0;
        this.players = new ArrayList<>(this.config.getPlayerCount());
        for (int i = 0; i < this.config.getPlayerCount(); i++) {
            this.players.add(new Player(nextPlayerID));
            this.nextPlayerID++;
        }

        this.lang = Lang.getLang(this.config.getLanguage());
        if (this.config.getDictFile() != null) {
            this.lang.set_customDictPath(this.config.getDictFile());
        }

        this.dictionary = new Dictionary();
        this.dictionary.loadFromFile(this.lang.getDictionaryPath());

        this.draw = new LettersDrawer(this.lang, this.rngGenerator, this.config.getSuperScrabble());
        this.grid = new Grid(this, (config.getSuperScrabble() ? 21 : 15));
        this.currentPlayer = this.players.get(0);
        this.gameTurn = 1;
        this.everyonePassedTurnStreak = 0;
    }

    public void reset() {
        this.rngGenerator.reset(); // way safer to keep this line the first !!
        this.grid.reset();
        this.draw.reset(this.config.getSuperScrabble());
        for (Player player : this.players) {
            player.reset();
        }
        this.currentPlayer = this.players.get(0);
        this.gameTurn = 1;
        this.everyonePassedTurnStreak = 0;
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
        pta.player.playerPassedTurn();
        this.endPlayerTurn();
    }

    // F34
    public void applyGameAction(ExchangeLetterAction exchangeLetterAction) {
        exchangeLetterAction.assertHasLettersToPlace();
        exchangeLetterAction.exchangeLetters(this.draw);
        this.endPlayerTurn();
    }

    // F36
    public void applyGameAction(ForfeitGameAction forfeitGameAction) {
        forfeitGameAction.player.forfeit();
        this.endPlayerTurn();
    }

    public void applyGameAction(PlaceWordAction pwa) {
        pwa.assertHasLettersToPlace();
        List<OwnedWord> scoredWords = this.grid.placeLetters(pwa);
        pwa.removeLettersPlaced();
        for (OwnedWord ownedWord : scoredWords) {
            int wordScore = this.grid.getScore(ownedWord);
            Utilities.verbose(
                    " -> placed the word: " + ownedWord.word.toString() + " (" + wordScore + " pts)\n");
            pwa.player.addScore(wordScore);
        }
        this.grid.useMultiplicators(scoredWords);
        this.endPlayerTurn();
    }

    // F41
    public void applyGameAction(RestartGameAction restartGameAction) {
        this.reset();
        // this.endPlayerTurn();
        this.currentPlayer = this.players.get(0);
    }

    /**
     * This function returns a list of active players by filtering out players who have forfeited.
     *
     * @return The method `getActivePlayers` returns a list of active players, which are players who
     * have not forfeited the game.
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

    private void endPlayerTurn() {
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

    /**
     * to be called when all players finished there turn
     */
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

    public int getTurn() {
        return this.gameTurn;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int nbEveryonePassedTurn() {
        return this.everyonePassedTurnStreak;
    }
}

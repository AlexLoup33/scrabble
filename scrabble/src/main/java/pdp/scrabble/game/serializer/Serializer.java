package pdp.scrabble.game.serializer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import pdp.scrabble.dictionary.Letter;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.Grid;
import pdp.scrabble.game.GridPos;
import pdp.scrabble.game.serializer.parser.CharInputStream;
import pdp.scrabble.game.serializer.parser.Parser;
import pdp.scrabble.game.serializer.parser.TokenInputStream;

public class Serializer {

  /**
   * Serializes the passed model to the given path argument
   *
   * @param src Path object pointing to the target destination
   *        The given extension will be applied if possible
   * @param toSave game model object to serialize
   */
  public static void saveCurrentToFile(Path filePath, GameModel toSave) throws IOException {
    BufferedWriter writer = Files.newBufferedWriter(filePath);

    String name = filePath.getFileName().toString().split("\\.")[0];
    writer.write("# [" + name + "]\n");
    writer.write("<<<<<<<<<<<<<<<<<<<< GAME\n");
    writer.write("Verbose=\"" + toSave.config.getVerbose() + "\"\n");
    writer.write(
        "Debug=\""
            + (toSave.config.getDebug() == null ? "false" : toSave.config.getDebug())
            + "\"\n");
    writer.write("Blitz=\"" + toSave.config.getBlitz() + "\"\n");
    writer.write("SuperScrabble=\"" + toSave.config.getSuperScrabble() + "\"\n");
    writer.write("Gui=\"" + toSave.config.getGui() + "\"\n");

    writer.write("Language=\"" + toSave.config.getLanguage() + "\"\n");
    writer.write("PlayerCount=" + toSave.config.getPlayerCount() + "\n");
    writer.write("BlitzTime=" + toSave.config.getBlitzTime() + "\n");
    writer.write("Seed=" + toSave.rngGenerator.getSeed() + "\n");

    StringBuilder aiPlayers = new StringBuilder();
    for (int i = 0; i < toSave.config.getAiPlayers().length; i++) {
      if (toSave.config.getAiPlayers()[i]) aiPlayers.append(String.valueOf(i + 1) + ",");
    }
    if (aiPlayers.length() > 0) aiPlayers.deleteCharAt(aiPlayers.length() - 1);
    writer.write("AiPlayers=[" + aiPlayers.toString() + "]\n");
    writer.write("AiMode=\"" + toSave.config.getAiMode() + "\"\n");
    writer.write("AiDepth=" + toSave.config.getAiDepth() + "\n");
    writer.write("AiTime=" + toSave.config.getAiTime() + "\n");

    writer.write("HistoryFile=\"" + toSave.config.getHistoryFile() + "\"\n");
    writer.write("DictFile=\"" + toSave.config.getDictFile() + "\"\n");
    writer.write("ContestFile=\"" + toSave.config.getContestFile() + "\"\n");
    writer.write(">>>>>>>>>>>>>>>>>>>>\n");

    writer.write(displayGrid(toSave.grid, toSave.config.getSuperScrabble()));

    writer.write("<<<<<<<<<<<<<<<<<<<< HISTORY\n");
    writer.write(toSave.getSerializedHistory());
    writer.write("\n>>>>>>>>>>>>>>>>>>>>\n");
    writer.close();
  }

  /**
   * Loads a model from a saveFile
   *
   * @param src Path object pointing to the saveFile
   * @return the loaded GameModel 
   */
  public static GameModel loadModelFromFile(Path filePath) {
    Parser parser = new Parser(new TokenInputStream(new CharInputStream(filePath)));
    return parser.getLoadedModel();
  }

  private static String displayGrid(Grid grid, Boolean isSuperScrabble) {
    // Display the first row of the grid with the column numbers
    StringBuilder sb = new StringBuilder();
    sb.append("#   ");
    for (int i = 0; i < grid.size - 1; i++) {
      if (i < 8) sb.append(i + 1 + "  ");
      else sb.append(i + 1 + " ");
    }
    sb.append(grid.size + "\n");

    // Display each row
    for (int i = 0; i < grid.size; i++) {
      sb.append("# " + (char) (i + 97) + " ");
      for (int j = 0; j < grid.size; j++) {
        GridPos pos = new GridPos(i, j);

        Letter letter = grid.getLetter(i, j);
        if (letter == null) {
          if (j == grid.size - 1) sb.append("_");
          else sb.append("_" + "  ");
        } else {
          if (j == grid.size - 1) sb.append(letter);
          else sb.append(letter + "  ");
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}

package pdp.scrabble.game.serializer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import pdp.scrabble.game.GameModel;
import pdp.scrabble.game.serializer.parser.CharInputStream;
import pdp.scrabble.game.serializer.parser.Parser;
import pdp.scrabble.game.serializer.parser.TokenInputStream;

public class Serializer {

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

    writer.write("<<<<<<<<<<<<<<<<<<<< HISTORY\n");
    writer.write(toSave.getHistory().toSerializedString());
    writer.write("\n>>>>>>>>>>>>>>>>>>>>\n");
    writer.close();
  }

  public static GameModel loadModelFromFile(Path filePath) {
    Parser parser = new Parser(new TokenInputStream(new CharInputStream(filePath)));
    return parser.getLoadedModel();
  }
}

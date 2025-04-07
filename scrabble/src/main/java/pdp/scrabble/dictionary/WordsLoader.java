package pdp.scrabble.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class WordsLoader {

  /** TODO comment */
  public static List<Word> loadWordsFromFile(Path filePath) {
    LinkedList<Word> words = new LinkedList<>();
    String solvedPath = WordsLoader.resolveFilePath(filePath);
    try (FileReader fileReader = new FileReader(solvedPath);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        words.add(new Word(line));
      }
    } catch (IOException e) {
      throw new CustomError(
          ErrorType.IO,
          "failed to read the dict",
          "Error reading file (" + solvedPath + "): " + e.getMessage());
    }

    return words;
  }

  /** TODO comment */
  private static String resolveFilePath(Path filePath) {
    if (!Files.exists(filePath)) {
      throw new CustomError(
          ErrorType.IO, "dict file don't exist", "the file at: " + filePath + " don't exist");
    }
    return filePath.toString();
  }
}

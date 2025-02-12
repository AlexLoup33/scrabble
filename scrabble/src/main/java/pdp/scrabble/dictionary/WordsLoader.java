package pdp.scrabble.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class WordsLoader {

  /** TODO comment */
  public static List<Word> loadWordsFromFile(Path filePath) {
    LinkedList<Word> words = new LinkedList<>();
    String solvedPath = WordsLoader.resolveFilePath(filePath);
//    System.out.println(solvedPath);
    try (FileReader fileReader = new FileReader(solvedPath);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        words.add(new Word(line));
      }
    } catch (IOException e) {
      // TODO: to redo when we have a good error handler implemented
      String errorMessage = "Error reading file (" + solvedPath + "): " + e.getMessage();
      System.err.println(errorMessage);
      throw new Error(errorMessage);
    }

    return words;
  }

  /** TODO comment */
  private static String resolveFilePath(Path filePath) {
    if (!Files.exists(filePath)) {
      throw new IllegalArgumentException("the file at: " + filePath + " don't exist");
    }
    try {
      return filePath.toString();
      // dont work when toUri() as:
      // return filePath.toUri().toString();
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("couldn't load the resource at: " + filePath);
    }
  }
}

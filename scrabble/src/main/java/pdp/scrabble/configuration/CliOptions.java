package pdp.scrabble.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import pdp.scrabble.Utilities;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class CliOptions {

  private Options options;
  private CommandLineParser parser;
  private CommandLine line;

  private Properties props;

  /**
   * Builds a cli Options from args passed as parameters to the java executable
   *
   * @param args command line arguments
   * @throws CustomError if user specify a non existent CliOption
   */
  public CliOptions(String[] args) throws CustomError {
    this.options = new Options();
    this.parser = new DefaultParser();

    // Stops execution if found
    this.options.addOption("h", "help", false, "print this message");
    this.options.addOption("V", "version", false, "print the version");

    // FLAGS
    this.options.addOption(
        Option.builder("v").longOpt("verbose").desc("display additional informations").build());
    // Debug can have an argument because of debug flags
    this.options.addOption(
        Option.builder("d")
            .longOpt("debug")
            .desc(
                "display debugging informations, only messages matching given flags will be printed")
            .hasArg()
            .optionalArg(true)
            .argName("flag(s)")
            .build());
    this.options.addOption(
        Option.builder("b").longOpt("blitz").desc("launch game in blitz mode").build());
    this.options.addOption(
        Option.builder("s").longOpt("super").desc("launch game in super scrabble mode").build());
    this.options.addOption(
        Option.builder("g").longOpt("gui").desc("launch game with a graphical interface").build());

    this.options.addOption(
        Option.builder("t")
            .longOpt("time")
            .desc("time for each player in blitz mode")
            .hasArg()
            .argName("minutes")
            .build());
    this.options.addOption(
        Option.builder("p")
            .longOpt("player")
            .desc("number of players(2-4)")
            .hasArg()
            .argName("player")
            .build());
    this.options.addOption(
        Option.builder("l")
            .longOpt("language")
            .desc("language for the interface of the game")
            .hasArg()
            .argName("language")
            .build());
    this.options.addOption(
        Option.builder("a")
            .longOpt("ai")
            .desc("designated players will be played by an AI")
            .hasArg()
            .argName("list")
            .build());
    this.options.addOption(
        Option.builder("am")
            .longOpt("ai-mode")
            .desc("algorithm used for AI players(mcts or minimax)")
            .hasArg()
            .argName("algo")
            .build());
    this.options.addOption(
        Option.builder("ad")
            .longOpt("ai-depth")
            .desc("exploration depth of AI research tree")
            .hasArg()
            .argName("depth")
            .build());
    this.options.addOption(
        Option.builder("at")
            .longOpt("ai-time")
            .desc("time limit for AI turn")
            .hasArg()
            .argName("minutes")
            .build());

    // Paths/Filenames
    this.options.addOption(
        Option.builder("D")
            .longOpt("dictionary")
            .desc("use a provided dictionary file")
            .hasArg()
            .argName("file")
            .build());
    this.options.addOption(
        Option.builder("c")
            .longOpt("contest")
            .desc("launch game in contest mode, from file")
            .hasArg()
            .argName("file")
            .build());
    this.options.addOption(
        Option.builder("r")
            .longOpt("resume")
            .desc("resume game from history file")
            .hasArg()
            .argName("file")
            .build());
    this.options.addOption(
        Option.builder("zz")
            .longOpt("seed")
            .desc("seed to start the game")
            .hasArg()
            .argName("seed")
            .build());

    parseCliOptions(args); // this can throw
  }

  /**
   * Fills the props attribute, intended to be used by Configuration builder to create a
   * Configuration instance with CLI args Warning: some flags will exit the program (e.g help)
   *
   * @param args command line arguments
   * @throws CustomError If user specify a non existent CliOption
   */
  private void parseCliOptions(String[] args) throws CustomError {
    try {
      this.line = parser.parse(options, args);
    } catch (ParseException e) {
      throw new CustomError(
          ErrorType.InvalidCliArgs,
          "Some cli options were incorrect, -h for help.",
          e.getMessage());
    }

    this.props = new Properties();

    if (line.hasOption("help")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("scrabble", options);
      System.exit(0);
    }

    if (line.hasOption("version")) {
      try {
        System.out.println(Utilities.getProjetVersion());
      } catch (CustomError e) {
        System.out.println(e.toUserFormatedText());
        System.exit(1);
      }
      System.exit(0);
    }

    Map<String, String> mappings = getArgMapping();

    for (Option option : this.line.getOptions()) {
      String key = mappings.get(option.getOpt());
      boolean isFlag = (!option.hasArg() || option.getValue() == null);
      if (key != null) {
        if (!isFlag) {
          this.props.setProperty(key, option.getValue());
        } else {
          // CLI option that matches the mapping with no arg is
          // 100% a flag, so set the corresponding bool to true
          this.props.setProperty(key, "true");
        }
      }
    }

    // Initialize the debugs flags with the string passed
    // to the debug option, if any
    Utilities.debugInit(line.getOptionValue("d"));
  }

  /**
   * Returns a mapping argName <-> scrabblerc key
   *
   * @see ConfigurationPropertiesEnum
   */
  private Map<String, String> getArgMapping() {
    Map<String, String> mappings = new HashMap<>();
    mappings.put("ad", "aiDepth");
    mappings.put("am", "aiMode");
    mappings.put("a", "aiPlayers");
    mappings.put("at", "aiTime");
    mappings.put("b", "blitz");
    mappings.put("t", "blitzTime");
    mappings.put("c", "contestFile");
    mappings.put("d", "debug");
    mappings.put("D", "dictFile");
    mappings.put("g", "gui");
    mappings.put("r", "historyFile");
    mappings.put("l", "language");
    mappings.put("p", "playerCount");
    mappings.put("s", "superScrabble");
    mappings.put("v", "verbose");

    return mappings;
  }

  public Properties getProps() {
    return this.props;
  }
}

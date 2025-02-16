package pdp.scrabble.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;

public class ConfigurationBuilder {

  private Map<ConfigurationPropertiesEnum, Boolean> completeness;
  private ConfigurationTemplate confStub;

  /**
   * Returns a ConfigurationBuilder from a path object. The given path object must point to a
   * scrabblerc file. The scrabble rc file is in INI format, and values are defined using key=value
   * format. This scrabblerc file must define all the properties of a Configuration object,
   * otherwise, this constructor will throw. The keys are listed in ConfigurationPropertiesEnum.
   *
   * @param src Path object pointing to a scrabblerc file
   * @throws CustomError if src is not a regular file or absent on disk
   * @throws CustomError if one of the values of the scrabblerc is not valid for key
   * @throws CustomError if the scrabblerc file is missing a configuration property
   * @see ConfigurationPropertiesEnum for the properties keys.
   */
  public ConfigurationBuilder(Path src) throws CustomError {
    Properties props = new Properties();
    try {
      // FileReader location is relative to user working directory at runtime
      BufferedReader scrabblerc = Files.newBufferedReader(src);
      props.load(scrabblerc);
    } catch (Exception e) {
      throw new CustomError(
          ErrorType.FailedConfigFileParsing, "Could not access .scrabblerc", e.getMessage());
    }

    this.completeness = new HashMap<>();
    for (ConfigurationPropertiesEnum instance : ConfigurationPropertiesEnum.values()) {
      this.completeness.put(instance, false);
    }

    this.confStub = new ConfigurationTemplate();
    convertPropsToTemplate(props, this.confStub, false);

    // completeness keeps track of properties that have been set
    // .scrabblerc must define all of the properties, otherwise stop execution
    if (this.completeness.containsValue(false)) {
      StringBuilder completenessValues = new StringBuilder("completeness values : \n");
      completeness.forEach(
          (ConfigurationPropertiesEnum e, Boolean b) ->
              completenessValues.append(
                  "[key: " + e.toString() + " / value: " + b.toString() + "]\n"));
      throw new CustomError(
          ErrorType.FailedConfigFileParsing,
          "Some properties are missing from .scrabblerc",
          completenessValues.toString());
    }
  }

  /**
   * Returns a ConfigurationBuilder from cli args passed to the java executable. These args are
   * parsed using the Commons-cli apache library in the CliOptions class. Unlike the path
   * constructor, we don't expect that cli args define all properties, this is because args
   * configuration properties are only meant to override scrabblerc values, so all configuration
   * values should already be defined. This constructor is the entry point of our commons-cli
   * parsing, which means it is required for parsing of cli args unrelated to configuration (eg.
   * help and version)
   *
   * @param args command line arguments
   * @throws CustomError if one of the args keys was unexpected
   * @throws CustomError if one of the args has incorrect value for key
   * @see CliOptions
   */
  public ConfigurationBuilder(String[] args) throws CustomError {
    CliOptions options = new CliOptions(args); // this can throw
    this.completeness = new HashMap<>();
    for (ConfigurationPropertiesEnum instance : ConfigurationPropertiesEnum.values()) {
      this.completeness.put(instance, false);
    }
    this.confStub = new ConfigurationTemplate();
    convertPropsToTemplate(options.getProps(), this.confStub, true);
  }

  /**
   * This method takes a properties object and tries to set a target configuration template with
   * it's values. It sets the completeness bitmap values at the same time, which is used to know if
   * the template has had all it's values set.
   *
   * @param props the properties object containing the values to set the template with
   * @param target the ConfigurationTemplate to be completed
   * @param fromCli determines if the properties target is being completed from cli values or from
   *     scrabblerc, to display different error messages
   * @throws CustomError if a properties value does not match the type of template attribute
   */
  private void convertPropsToTemplate(
      Properties props, ConfigurationTemplate target, boolean fromCli) throws CustomError {
    for (String key : props.stringPropertyNames()) {
      Class cls = target.getClass();
      ConfigurationPropertiesEnum confEnum = ConfigurationPropertiesEnum.get(key);
      // If key is not a known scrabblerc key, ignore it
      if (confEnum == null) {
        continue;
      }
      String errHeader = fromCli ? "" : "[.scrabblerc] ";
      ErrorType errType = fromCli ? ErrorType.InvalidCliArgs : ErrorType.ConfigParsingException;
      switch (confEnum) {
        // FLAGS
        case VERBOSE:
        case DEBUG:
        case BLITZ:
        case SUPERSCRABBLE:
        case GUI:
          {
            // true if property == "true" / false in every other case
            Boolean value = Boolean.valueOf(props.getProperty(key));
            // Build setter reflectively from key
            // basically creating `set{key}(boolean value)` and calling it
            try {
              Method setter =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {Boolean.class});
              setter.invoke(target, value);
            } catch (Exception e) {
              System.out.println(e);
            }
            break;
          }
        // Ints
        case PLAYERCOUNT:
        case BLITZTIME:
        case AIDEPTH:
        case AITIME:
          {
            int value = 0;
            try {
              value = Integer.parseUnsignedInt(props.getProperty(key));
            } catch (NumberFormatException e) {
              throw new CustomError(
                  errType,
                  errHeader
                      + "value `"
                      + props.getProperty(key)
                      + "` for key `"
                      + key
                      + "`, expected a positive `"
                      + Integer.TYPE
                      + "`",
                  e.getMessage());
            }
            // Build setter reflectively from key
            // basically creating `set{key}(int value)` and calling it
            try {
              Method setter =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {Integer.TYPE});
              setter.invoke(target, value);
            } catch (Exception e) {
              // Reflected exceptions are wrapped in InvocationTargetException
              // the only possible exception from our setter is CustomError
              // so we can safely cast
              InvocationTargetException qualified = (InvocationTargetException) e;
              CustomError src = (CustomError) qualified.getCause();
              throw new CustomError(errType, errHeader + src.message, src.details);
            }
            break;
          }
        // Strings
        case AIPLAYERS:
        case LANGUAGE:
        case AIMODE:
        case DICTFILE:
        case CONTESTFILE:
        case HISTORYFILE:
          {
            String value = props.getProperty(key);
            // Build setter reflectively from key
            // basically creating `set{key}(String value)` and calling it
            try {
              Method setter =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {String.class});
              setter.invoke(target, value);
            } catch (Exception e) {
              // Reflected exceptions are wrapped in InvocationTargetException
              // the only possible exception from our setter is CustomError
              // so we can safely cast
              InvocationTargetException qualified = (InvocationTargetException) e;
              CustomError src = (CustomError) qualified.getCause();
              throw new CustomError(errType, errHeader + src.message, src.details);
            }
            break;
          }
        default:
          throw new CustomError(
              errType,
              errHeader + "Oops",
              "ConvertPropsToTemplate: ConfigurationPropertiesEnum."
                  + props.getProperty(key)
                  + " not properly checked for");
      }
      this.completeness.put(confEnum, true);
    }
  }

  /**
   * Merges the current ConfigurationBuilder instance: (`this`) with the ConfigurationBuilder
   * source. The template of a ConfigurationBuilder is private, so we use the configuration record
   * of source. Uses the completeness BitMap to find unset fields. If a field is unset in source,
   * keeps the current instance value from before the merge. Mutates this.confStub
   *
   * @param source the ConfigurationBuilder to merge from
   * @throws CustomError
   */
  public void merge(ConfigurationBuilder source) throws CustomError {
    Configuration sourceConf = source.getConfiguration();
    for (ConfigurationPropertiesEnum confEnum : ConfigurationPropertiesEnum.values()) {
      if (source.getCompleteness().get(confEnum)) {
        try {
          String fieldName = confEnum.toString();
          String methAttrName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
          Method getter = Configuration.class.getMethod("get" + methAttrName, null);
          Method setter =
              ConfigurationTemplate.class.getMethod(
                  "set" + methAttrName, new Class[] {getter.getReturnType()});
          // Calling this.confStub.set{fieldName}(source.get({fieldName}))
          setter.invoke(this.confStub, getter.invoke(sourceConf));
        } catch (Exception e) {
          throw new CustomError(ErrorType.FailedConfigFileParsing, "Oops", e.getMessage());
        }
      }
    }
  }

  public String getTemplateString() {
    return this.confStub.toString();
  }

  public Map<ConfigurationPropertiesEnum, Boolean> getCompleteness() {
    return this.completeness;
  }

  public Configuration getConfiguration() {
    return this.confStub.getConfiguration();
  }

  /**
   * Saves a configuration Object to file, using INI format. This creates a valid scrabblerc file.
   *
   * @param dst the target path to write to
   * @param conf the configuration objet to get values to write from
   */
  public static void saveConfiguration(Path dst, Configuration conf) throws IOException {
    BufferedWriter writer = Files.newBufferedWriter(dst);

    String name = dst.getFileName().toString().replace(".", "");
    writer.write("#[" + name + "]\n");
    writer.write("#[Flags]\n");
    writer.write("verbose=" + conf.getVerbose() + "\n");
    writer.write("debug=" + conf.getDebug() + "\n");
    writer.write("blitz=" + conf.getBlitz() + "\n");
    writer.write("superScrabble=" + conf.getSuperScrabble() + "\n");
    writer.write("gui=" + conf.getGui() + "\n\n");

    writer.write("#[Game]\n");
    writer.write("language=" + conf.getLanguage() + "\n");
    writer.write("playerCount=" + conf.getPlayerCount() + "\n");
    writer.write("blitzTime=" + conf.getBlitzTime() + "\n\n");

    writer.write("#[AI]\n");
    writer.write("aiPlayers=" + aiPlayersToString(conf.getAiPlayers()) + "\n");
    writer.write("aiMode=" + conf.getAiMode() + "\n");
    writer.write("aiDepth=" + conf.getAiDepth() + "\n");
    writer.write("aiTime=" + conf.getAiTime() + "\n\n");

    writer.write("#[Storage]\n");
    writer.write("historyFile=" + conf.getHistoryFile() + "\n");
    writer.write("dictFile=" + conf.getDictFile() + "\n");
    writer.write("contestFile=" + conf.getContestFile() + "\n");

    writer.close();
  }

  public static String aiPlayersToString(boolean[] aiPlayers) {
    StringBuilder value = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      if (aiPlayers[i]) {
        if (value.length() != 0) {
          value.append(',');
        }
        value.append(i + 1);
      }
    }
    if (value.length() == 0) {
      return "null";
    }
    return value.toString();
  }
}

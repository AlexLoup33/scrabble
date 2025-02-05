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
import pdp.scrabble.errorsHandling.EnumError;

public class ConfigurationBuilder {

  private Map<ConfigurationPropertiesEnum, Boolean> completeness;
  private ConfigurationTemplate confStub;

  public ConfigurationBuilder(Path src) throws CustomError {
    Properties props = new Properties();
    try {
      // FileReader location is relative to user working directory at runtime
      BufferedReader scrabblerc = Files.newBufferedReader(src);
      props.load(scrabblerc);
    } catch (Exception e) {
      throw createError("Could not access .scrabblerc");
    }

    this.completeness = new HashMap<>();
    for (ConfigurationPropertiesEnum instance : ConfigurationPropertiesEnum.values()) {
      this.completeness.put(instance, false);
    }

    this.confStub = new ConfigurationTemplate();
    convertPropsToTemplate(props, this.confStub);

    // completeness keeps track of properties that have been set
    // .scrabblerc must define all of the properties, otherwise stop execution
    if (this.completeness.containsValue(false)) {
      throw createError("Some properties are missing from .scrabblerc");
    }
  }

  private CustomError createError(String msg) {
    return new CustomError(
        EnumError.FailedConfigFileParsing, "[.scrabblerc] Parsing error: " + msg, true);
  }

  private void convertPropsToTemplate(Properties props, ConfigurationTemplate target)
      throws CustomError {
    for (String key : props.stringPropertyNames()) {
      Class cls = target.getClass();
      ConfigurationPropertiesEnum confEnum = ConfigurationPropertiesEnum.get(key);
      // If key is not a known scrabblerc key, ignore it
      if (confEnum == null) {
        continue;
      }
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
              throw createError(
                  "value `"
                      + props.getProperty(key)
                      + "` for key `"
                      + key
                      + "`, expected a positive `"
                      + Integer.TYPE
                      + "`");
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
              throw src;
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
              throw src;
            }
            break;
          }
        default:
          throw createError(
              "[Dev error] ConvertPropsToTemplate: ConfigurationPropertiesEnum."
                  + props.getProperty(key)
                  + " not properly checked for");
      }
      this.completeness.put(confEnum, true);
    }
  }

  // Merge the current ConfigurationBuilder instance: (`this`)
  // with the ConfigurationBuilder source
  // Uses the completeness BitMap to find unset fields
  // If a field is unset in source, keeps the current instance value from
  // before the merge
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
          throw new CustomError(
              EnumError.FailedConfigFileParsing,
              "[dev Error] ConfigurationBuilder.merge: " + e.getMessage(),
              true);
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

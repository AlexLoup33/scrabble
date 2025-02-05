package pdp.scrabble.configuration;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
              Method meth =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {Boolean.class});
              meth.invoke(target, value);
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
              Method meth =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {Integer.TYPE});
              meth.invoke(target, value);
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
              Method meth =
                  cls.getMethod(
                      "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                      new Class[] {String.class});
              meth.invoke(target, value);
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

  public String getTemplateString() {
    return this.confStub.toString();
  }

  public Configuration getConfiguration() {
    return this.confStub.getConfiguration();
  }

  public static void saveConfiguration(Path dst, Configuration conf) throws IOException {
    try (FileOutputStream file = new FileOutputStream(dst.toFile())){
      String[] parse = String.valueOf(dst).split("/");
      String name = parse[parse.length-1].replace(".", "");
      System.out.println(name);

      file.write(("#["+name+"]\n").getBytes());
      file.write(("#[Flags]\n").getBytes());
      file.write(("verbose="+conf.getVerbose()+"\n").getBytes());
      file.write(("debug="+conf.getDebug()+"\n").getBytes());
      file.write(("blitz="+conf.getBlitz()+"\n").getBytes());
      file.write(("superScrabble="+conf.getSuperScrabble()+"\n").getBytes());
      file.write(("gui="+conf.getGui()+"\n\n").getBytes());

      file.write(("#[Game]\n").getBytes());
      file.write(("language="+conf.getLanguage()+"\n").getBytes());
      file.write(("playerCount="+conf.getPlayerCount()+"\n").getBytes());
      file.write(("blitzTime="+conf.getBlitzTime()+"\n\n").getBytes());

      file.write(("#[AI]\n").getBytes());
      file.write(("aiPlayers="+ Arrays.toString(conf.getAiPlayers()) +"\n").getBytes());
      file.write(("aiMode="+conf.getAiMode()+"\n").getBytes());
      file.write(("aiDepth="+conf.getAiDepth()+"\n").getBytes());
      file.write(("aiTime="+conf.getAiTime()+"\n\n").getBytes());

      file.write(("#[Storage]").getBytes());
      file.write(("historyFile="+conf.getHistoryFile()+"\n").getBytes());
      file.write(("dictFile="+conf.getDictFile()+"\n").getBytes());
      file.write(("contestFile="+conf.getContestFile()+"\n").getBytes());
    }
  }
}

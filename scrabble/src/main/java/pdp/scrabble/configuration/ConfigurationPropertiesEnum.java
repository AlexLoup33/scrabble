package pdp.scrabble.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Binds enum to scrabblerc key
public enum ConfigurationPropertiesEnum {
  VERBOSE("verbose"),
  DEBUG("debug"),
  BLITZ("blitz"),
  SUPERSCRABBLE("superScrabble"),
  GUI("gui"),
  PLAYERCOUNT("playerCount"),
  BLITZTIME("blitzTime"),
  AIPLAYERS("aiPlayers"),
  AIDEPTH("aiDepth"),
  AITIME("aiTime"),
  LANGUAGE("language"),
  AIMODE("aiMode"),
  DICTFILE("dictFile"),
  CONTESTFILE("contestFile"),
  HISTORYFILE("historyFile");

  private final String name;

  // This Map is needed for pattern matching between
  // .scrabblerc values and our enum
  private static final Map<String, ConfigurationPropertiesEnum> ENUM_MAP;

  private ConfigurationPropertiesEnum(String name) {
    this.name = name;
  }

  static {
    Map<String, ConfigurationPropertiesEnum> map =
        new HashMap<String, ConfigurationPropertiesEnum>();
    for (ConfigurationPropertiesEnum instance : ConfigurationPropertiesEnum.values()) {
      map.put(instance.toString(), instance);
    }
    ENUM_MAP = Collections.unmodifiableMap(map);
  }

  public String toString() {
    return this.name;
  }

  public static ConfigurationPropertiesEnum get(String name) {
    return ENUM_MAP.get(name);
  }
}

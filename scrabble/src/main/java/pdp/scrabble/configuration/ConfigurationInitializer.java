package pdp.scrabble.configuration;

import java.nio.file.Paths;

import pdp.scrabble.Utilities;

/**
 * Entry point for getting our final Configuration object Attribute priority: cli args > scrabblerc
 * The rules are as follow
 *
 * <p>1.If an attribute is absent from cli arg, fallback to scrabblerc value 2. If an attribute is
 * absent from scrabblerc, throw error
 */
public class ConfigurationInitializer {
    public static Configuration configurationInit(String[] args) {
        ConfigurationBuilder rcBuilder = new ConfigurationBuilder(Paths.get("./.scrabblerc"));
        ConfigurationBuilder cliBuilder = new ConfigurationBuilder(args);
        rcBuilder.merge(cliBuilder);
        Configuration finalConf = rcBuilder.getConfiguration();

        Utilities.debugInit(finalConf.getDebug());
        Utilities.verboseInit(finalConf.getVerbose());

        Utilities.debug(
                'c',
                "==========loaded conf==========\n%s\n==========loaded conf==========\n",
                rcBuilder.getTemplateString());

        return rcBuilder.getConfiguration();
    }
}

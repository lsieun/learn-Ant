package example;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.WordUtils;

/**
 * Simple example to show how easy it is to retrieve transitive libs with ivy !!!
 */
public final class HelloConsole {
    public static void main(String[] args) throws Exception {
        Option msg = Option.builder("m")
                .longOpt("message")
                .hasArg()
                .desc("the message to capitalize")
                .build();
        Options options = new Options();
        options.addOption(msg);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        String message = line.getOptionValue("m", "Hello Ivy!");
        System.out.println("standard message : " + message);
        System.out.println("capitalized by " + WordUtils.class.getName()
                + " : " + WordUtils.capitalizeFully(message));
    }

    private HelloConsole() {
    }
}

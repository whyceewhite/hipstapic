package ology.hipstapic.service.cli;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 */
public class ImageLoader {

    private static Logger logger = LoggerFactory.getLogger(ImageLoader.class);

    public static void main(String... args) {

        ImageLoaderOptions options = new ImageLoaderOptions();
        CmdLineParser parser = new CmdLineParser(options);

        if (args.length == 0) {
            parser.printUsage(System.err);
            System.exit(1);
        }

        try {

            parser.parseArgument(args);

            if (options.isHelp()) {
                parser.printUsage(System.err);
                System.exit(1);
            }

            ImageLoader.run(options);

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }

    private static void run(ImageLoaderOptions options) {


    }

    private byte[] readFile(String filename) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        Path file = Paths.get(filename);

        try (SeekableByteChannel channel = Files.newByteChannel(file)) {
            while (channel.read(buffer) > 0);
        }
        return buffer.array();

    }
}

package ology.hipstapic.service.cli;

import ology.hipstapic.domain.Picture;
import ology.hipstapic.service.db.PictureService;
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

        } catch (Exception e) {
            logger.error("An error occurred while loading the image to the database.", e);
            System.exit(1);
        }

        System.exit(0);
    }

    private static void run(ImageLoaderOptions options) throws IOException {

        Picture picture = new Picture();
        picture.setTitle(options.getTitle());
        picture.setImage(readFile(options.getFilename()));
        picture.addTag(options.getFilm());
        picture.addTag(options.getFlash());
        picture.addTag(options.getLens());

        PictureService.getInstance().save(picture);
        logger.info("Loaded {}", options.getFilename());
    }

    private static byte[] readFile(String filename) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        Path file = Paths.get(filename);

        try (SeekableByteChannel channel = Files.newByteChannel(file)) {
            while (channel.read(buffer) > 0);
        }
        return buffer.array();

    }
}

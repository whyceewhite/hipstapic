package ology.hipstapic.cli;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import ology.hipstapic.service.domain.Picture;
import ology.hipstapic.service.db.PictureService;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class ImageLoader {

    private static Logger logger = LoggerFactory.getLogger(ImageLoader.class);

    private static final String EXIF_SOFTWARE = "Software";
    private static final String EXIF_CREATE_DATE = "Date/Time Original";
    private static final String EXIF_MAKE = "Make";
    private static final String EXIF_MODEL = "Model";

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
            logger.error("An error occurred while loading the picture information to the database.", e);
            System.exit(1);
        }

        System.exit(0);
    }

    /**
     * <p>
     * Calls the service to load the data store with the values provided on the
     * cli.
     * </p>
     *
     * @param   options The parameters and arguments provided on the command
     *          line.
     */
    private static void run(ImageLoaderOptions options) {

        ImageLoader loader = new ImageLoader();

        if (options.getDirectory() != null) {
            File directory = new File(options.getDirectory());
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    loader.load(file, options.isPrintExif());
                }
            }
        } else {
            loader.load(new File(options.getFilename()), options.isPrintExif());
        }
    }

    private void load(File file, boolean printExif) {

        logger.info("Loading {}", file.getAbsolutePath());
        Picture picture = readExif(file, printExif);
        PictureService.getInstance().save(picture);
    }

    private Picture readExif(File file, boolean isPrintExif) {

        Picture picture = new Picture();
        picture.setFilename(file.getName());

        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (Exception e) {
            logger.error("An error occurred while reading the metadata for file {}: {}", file.getAbsolutePath(), e);
            return null;
        }

        if (isPrintExif) {
            printExif(metadata);
        }

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                switch (tag.getTagName()) {
                    case EXIF_MODEL:
                        picture.setModel(tag.getDescription());
                        break;
                    case EXIF_MAKE:
                        picture.setMake(tag.getDescription());
                        break;
                    case EXIF_SOFTWARE:
                        parseSoftware(picture, tag.getDescription());
                        break;
                    case EXIF_CREATE_DATE:
                        break;
                }
            }
        }

        return picture;
    }

    private void parseSoftware(Picture picture, String softwareExif) {

        logger.debug("Parsing Software EXIF value: {}", softwareExif);

        Pattern pattern = Pattern.compile("(.+) Lens, (.+) Film, (.+) Flash");
        Matcher matcher = pattern.matcher(softwareExif);

        if (matcher.find() && matcher.groupCount() == 3) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String tag = matcher.group(i).replaceAll("\\s+", "");
                if (!tag.equalsIgnoreCase("No")) {
                    picture.addTag(tag);
                    logger.debug("Software parse value: {}", matcher.group(i));
                }
            }
        }
    }

    private void printExif(Metadata metadata) {

        for (Directory directory : metadata.getDirectories()) {
            System.out.println("-----------------------------------");
            System.out.println(directory.getName());
            System.out.println("-----------------------------------");
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
    }

    /**
     * <p>
     * Reads the bytes from from the file that is at the location identified by
     * the filename argument.
     * </p>
     *
     * @param   filename The location and name of the file to read.
     * @return  A byte array containing the file's binary content.
     * @throws  IOException If an error occurs while finding or reading the file.
     */
    byte[] readFile(String filename) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        Path file = Paths.get(filename);

        try (SeekableByteChannel channel = Files.newByteChannel(file)) {
            while (channel.read(buffer) > 0);
        }
        return buffer.array();

    }
}

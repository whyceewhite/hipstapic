package ology.hipstapic.cli;

import org.kohsuke.args4j.Option;

/**
 * <p>
 * Represents the parameters and arguments that are available for the command line
 * interface tool.
 * </p>
 */
public class ImageLoaderOptions {

    @Option(name = "-f", aliases = "--filename", usage = "The file name of the image.")
    private String filename;

    @Option(name = "-d", aliases = "--directory", forbids = {"-f"}, usage = "Loads all files in the given directory.")
    private String directory;

    @Option(name = "-t", aliases = "--title", usage = "The title of the image.")
    private String title;

    @Option(name = "-p", aliases = "--print-exif", usage = "Displays the EXIF information associated with the image file.")
    private boolean printExif;

    @Option(name = "-h", aliases = "--help", usage = "Displays the usage.")
    private boolean help;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPrintExif() {
        return printExif;
    }

    public void setPrintExif(boolean printExif) {
        this.printExif = printExif;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }
}

package ology.hipstapic.service.cli;

import org.kohsuke.args4j.Option;

/**
 * Represents the command line interface options that are available for the
 * image loader function.
 */
public class ImageLoaderOptions {

    @Option(name="-n", aliases = "--filename", usage = "The absolute path and file name to the image. Required")
    private String filename;

    @Option(name="-t", aliases = "--title", usage = "The title of the image. Optional.")
    private String title;

    @Option(name="-l", aliases = "--lens", usage = "The lens used for this image. Optional.")
    private String lens;

    @Option(name="-fi", aliases = "--film", usage = "The film used for this image. Optional.")
    private String film;

    @Option(name="-fl", aliases = "--flash", usage = "The flash used for this image. Optional.")
    private String flash;

    @Option(name="-h", aliases = "--help")
    private boolean help;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }
}

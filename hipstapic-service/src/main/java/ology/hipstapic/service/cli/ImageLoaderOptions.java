package ology.hipstapic.service.cli;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.List;

/**
 * <p>
 * Represents the parameters and arguments that are available for the command line
 * interface tool.
 * </p>
 */
public class ImageLoaderOptions {

    @Option(name = "-f", aliases = "--filename", required = true, usage = "The file name of the image.")
    private String filename;

    @Option(name = "-t", aliases = "--title", usage = "The title of the image.")
    private String title;

    @Option(name = "-tag", aliases = "--tag", usage = "Tags and key words to apply to the image.")
    private List<String> tags;

    @Option(name = "-h", aliases = "--help")
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }
}

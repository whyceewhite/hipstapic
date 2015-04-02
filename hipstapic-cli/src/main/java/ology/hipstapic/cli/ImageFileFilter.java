package ology.hipstapic.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;

/**
 * <p>
 * A filter that indicates which files are acceptable for loading.
 * </p>
 */
public class ImageFileFilter implements FileFilter {

    private static Logger logger = LoggerFactory.getLogger(ImageFileFilter.class);

    @Override
    public boolean accept(File pathname) {

        if (!pathname.isFile()) {
            return false;
        }

        String extension = "";
        int i = pathname.getName().lastIndexOf('.');
        if (i > 0) {
            extension = pathname.getName().substring(i + 1);
        }

        if (extension.equalsIgnoreCase("jpg") ||
                extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("gif") ||
                extension.equalsIgnoreCase("tiff") ||
                extension.equalsIgnoreCase("tif") ||
                extension.equalsIgnoreCase("png")) {
            return true;
        }

        return false;
    }
}

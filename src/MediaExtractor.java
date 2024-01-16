import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class MediaExtractor {

    // define a list of video extensions
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList(
            ".mp4", ".avi", ".mov", ".wmv", ".m4v", ".flv", ".mkv", ".webm", ".vob",
            ".ogv", ".ogg", ".drc", ".gifv", ".mng", ".mts", ".m2ts", ".ts", ".mov",
            ".qt", ".yuv", ".rm", ".rmvb", ".asf", ".amv", ".mpg", ".mp2", ".mpeg",
            ".mpe", ".mpv", ".m2v", ".svi", ".3gp", ".3g2", ".mxf", ".roq", ".nsv",
            ".f4v", ".f4p", ".f4a", ".f4b"
    );

    // define a list of image extensions
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            ".jpeg", ".jpg", ".png", ".gif", ".tiff", ".tif", ".bmp", ".raw", ".psd",
            ".eps", ".ai", ".indd", ".pdf", ".svg", ".webp", ".heif", ".heic", ".cr2",
            ".nef", ".arw", ".srf", ".orf", ".dng", ".xcf", ".wmf", ".emf", ".icns",
            ".ico", ".tga", ".exif"
    );


    // define a list of text based documents
    private static final List<String> TEXT_DOCUMENT_EXTENSIONS = Arrays.asList(
            ".txt", ".doc", ".docx", ".pdf", ".odt", ".rtf", ".tex", ".wpd", ".wps",
            ".md", ".markdown", ".csv", ".xls", ".xlsx", ".ppt", ".pptx", ".log",
            ".msg", ".pages", ".numbers", ".key"
    );

    public static void main(String[] args) {
        // check if source directory path is provided in CMD
        if (args.length < 1) {
            System.out.println("Usage: java MediaExtractor <source_directory>");
            System.exit(1);
        }

        // get the source directory from command line argument
        String sourceDir = args[0];

        // create File objects for source directory and destination directories
        File sourceFolder = new File(sourceDir);
        File videoDir = new File(sourceDir + File.separator + "videos");
        File imageDir = new File(sourceDir + File.separator + "images");
        File textDocDir = new File(sourceDir + File.separator + "text_documents");

        // check for each media type and move files if they exist
        if (hasFilesOfType(sourceFolder, VIDEO_EXTENSIONS)) {
            createDirectory(videoDir.getAbsolutePath());
            moveMediaRecursively(sourceFolder, videoDir, VIDEO_EXTENSIONS);
        }
        if (hasFilesOfType(sourceFolder, IMAGE_EXTENSIONS)) {
            createDirectory(imageDir.getAbsolutePath());
            moveMediaRecursively(sourceFolder, imageDir, IMAGE_EXTENSIONS);
        }
        if (hasFilesOfType(sourceFolder, TEXT_DOCUMENT_EXTENSIONS)) {
            createDirectory(textDocDir.getAbsolutePath());
            moveMediaRecursively(sourceFolder, textDocDir, TEXT_DOCUMENT_EXTENSIONS);
        }
    }

    // method to check if there are any files of a specific type in the directory
    private static boolean hasFilesOfType(File folder, List<String> extensions) {
        File[] fileList = folder.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && isFileExtension(file, extensions)) {
                    return true;
                } else if (file.isDirectory()) {
                    if (hasFilesOfType(file, extensions)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // method to create a directory if it doesn't exist
    private static void createDirectory(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            boolean created = dir.mkdir();
            if (!created) {
                System.out.println("Failed to create the directory: " + directory);
                System.exit(1);
            }
        }
    }

    // method to move media files recursively from source to destination
    private static void moveMediaRecursively(File sourceFolder, File destinationFolder, List<String> extensions) {
        File[] fileList = sourceFolder.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && isFileExtension(file, extensions)) {
                    moveFile(file, destinationFolder);
                } else if (file.isDirectory() && !file.equals(destinationFolder)) {
                    moveMediaRecursively(file, destinationFolder, extensions);
                }
            }
        } else {
            System.err.println("Cannot list files for directory: " + sourceFolder.getAbsolutePath());
        }
    }

    // helper method to check if a file has a specific extension
    private static boolean isFileExtension(File file, List<String> extensions) {
        for (String extension : extensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    // Method to move a file to the destination folder
    private static void moveFile(File file, File destinationFolder) {
        try {
            Path destination = Paths.get(destinationFolder.getAbsolutePath(), file.getName());
            Files.move(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved: " + file.getName());
        } catch (IOException e) {
            System.err.println("Unable to move file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }
}

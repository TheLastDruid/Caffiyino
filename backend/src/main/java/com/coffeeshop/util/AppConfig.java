package com.coffeeshop.util;

import java.io.File;
import java.nio.file.Paths;

/**
 * Configuration constants for the application
 */
public final class AppConfig {
    
    private AppConfig() {
        throw new IllegalStateException("Utility class");
    }
    
    // Image settings
    public static final String IMAGES_DIRECTORY = "images";
    public static final String RESOURCES_PATH = "src/main/resources";
    public static final String FULL_IMAGES_PATH = Paths.get(RESOURCES_PATH, IMAGES_DIRECTORY).toString();
    
    // Supported image formats
    public static final String[] SUPPORTED_IMAGE_FORMATS = {"jpg", "jpeg", "png", "gif", "bmp"};
    
    // Default image sizes
    public static final int CARD_IMAGE_SIZE = 100;
    public static final int PREVIEW_IMAGE_SIZE = 80;
    public static final int THUMBNAIL_SIZE = 50;
    
    /**
     * Get the full path to the images directory
     */
    public static String getImagesDirectory() {
        File imagesDir = new File(FULL_IMAGES_PATH);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
        return imagesDir.getAbsolutePath();
    }
    
    /**
     * Check if a file is a supported image format
     */
    public static boolean isSupportedImageFormat(String filename) {
        if (filename == null) return false;
        
        String extension = getFileExtension(filename).toLowerCase();
        for (String format : SUPPORTED_IMAGE_FORMATS) {
            if (format.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get file extension from filename
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) return "";
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) return "";
        
        return filename.substring(lastDotIndex + 1);
    }
}

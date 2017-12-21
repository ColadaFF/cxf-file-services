package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    private static final List<String> imageExtensions = Arrays.asList("jpg", "png");
    private static final List<String> videoExtensions = Arrays.asList("avi", "mp4");
    private static final List<String> allowedExtensions = Utils.union(imageExtensions, videoExtensions);

    public static Boolean isExtensionAllowed(String fileExtension) {
        return allowedExtensions.contains(fileExtension);
    }

    public static Boolean isVideoExtension(String fileExtension) {
        return videoExtensions.contains(fileExtension);
    }

    public static Boolean isImageExtension(String fileExtension) {
        return imageExtensions.contains(fileExtension);
    }

    public static <E> List<E> union(List<? extends E> list1, List<? extends E> list2) {
        ArrayList<E> result = new ArrayList<E>(list1);
        result.addAll(list2);
        return result;
    }
}

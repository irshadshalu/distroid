package in.frisc.distroid.dummy;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<FileItem> ITEMS = new ArrayList<FileItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, FileItem> ITEM_MAP = new HashMap<String, FileItem>();

    private static final int COUNT = 25;



    public static void makeStatuses(File[] files){
        ITEMS =  new ArrayList<FileItem>();
        try {
            for (int i = 0; i < files.length; i++) {
                addItem(new FileItem(String.valueOf(i), files[i].getAbsolutePath(), files[i].length(), files[i].getName()));
            }
        }
        catch (Exception e){
            Log.wtf(TAG, "ERROR OCCURRed" + e.toString());
            e.printStackTrace();
        }
    }

    private static void addItem(FileItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class FileItem {
        public final String id;
        public final String path;
        public final long size;
        public final String name;
        public String thumbnail;


        public FileItem(String id, String path, long size, String name) {
            this.id = id;
            this.path = path;
            this.size = size;
            this.name = name;
            this.thumbnail = "NONE";
        }

        @Override
        public String toString() {
            return path;
        }
    }
}

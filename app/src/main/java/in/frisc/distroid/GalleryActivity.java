package in.frisc.distroid;

import android.app.Fragment;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.koushikdutta.async.Util;

import in.frisc.distroid.dummy.DummyContent;

public class GalleryActivity extends AppCompatActivity implements GalleryElementFragment.OnListFragmentInteractionListener{

    public static final String TAG = "GALLERy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        // query
        Cursor c = Utils.myDb.query("SELECT rowid,* FROM files");
        while (c.moveToNext()) {
            // get values
            long id = c.getLong(0);
            String path = c.getString(1);
            String name = c.getString(2);
            String thumb = c.getString(3);
            long size = c.getLong(4);
            Log.w(TAG, id + " " + path + " " + name + " "+ thumb+ " ");

        }

    }

    @Override
    public void onListFragmentInteraction(DummyContent.FileItem item) {

    }
}

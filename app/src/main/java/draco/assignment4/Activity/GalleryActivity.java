package draco.assignment4.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import draco.assignment4.Class.Photo;
import draco.assignment4.Class.RecyclerViewAdapter;
import draco.assignment4.R;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Draco on 2016-10-30.
 */

public class GalleryActivity extends AppCompatActivity{
    public Realm realm;
    public static RealmResults<Photo> realmResults = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);

        Realm realm = Realm.getDefaultInstance();
        realmResults = realm.where(Photo.class).findAll();

        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager gLayout = new GridLayoutManager(GalleryActivity.this, 2);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(GalleryActivity.this, realmResults);
        rView.setAdapter(rcAdapter);
    }
}

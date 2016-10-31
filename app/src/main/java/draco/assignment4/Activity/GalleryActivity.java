package draco.assignment4.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(Photo.class).findAll();

        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager gLayout = new GridLayoutManager(GalleryActivity.this, 2);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(GalleryActivity.this, realmResults);
        rcAdapter.notifyDataSetChanged();
        rView.setAdapter(rcAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf = this.getMenuInflater();
        mf.inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage("Do you want to delete all the data from database?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();
                        Intent back_to_main = new Intent(ctx, MainActivity.class);
                        startActivity(back_to_main);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.setCancelable(true);
                builder.create().show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

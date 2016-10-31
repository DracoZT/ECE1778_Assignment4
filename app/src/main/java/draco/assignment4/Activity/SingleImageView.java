package draco.assignment4.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import draco.assignment4.Class.FaceRegion;
import draco.assignment4.Class.Photo;
import draco.assignment4.R;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Draco on 2016-10-31.
 */

public class SingleImageView extends AppCompatActivity {
    private Photo photo = null;
    public Realm realm;
    public RealmResults<Photo> realmResults = null;
    private Context ctx = this;
    private String photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_view_layout);

        realm = Realm.getDefaultInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("photo_info")) {
            photoUri = extras.getString("photo_info");
        }

        realmResults = realm.where(Photo.class).equalTo("photoPath", photoUri).findAll();
        photo = realmResults.first();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getPhotoPath(), options);
        float scale = bitmap.getWidth() / 720;
        Bitmap tempBitmap= Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(bitmap,0,0,null);

        RealmList<FaceRegion> faceRegions = photo.getFaceRegions();
        if(faceRegions.size() != 0){
            for(FaceRegion face : faceRegions){
                Paint mPaint = new Paint();
                mPaint.setStrokeWidth(2);
                mPaint.setColor(Color.GREEN);
                mPaint.setStyle(Paint.Style.STROKE);
                tempCanvas.drawRoundRect(face.getX() * scale,face.getY() * scale,(face.getX() + face.getWidth()) * scale, (face.getY() + face.getHeight()) * scale, 2, 2, mPaint);
            }
        }

        ImageView imgView = (ImageView) findViewById(R.id.FullScreenImg);
        imgView.setImageBitmap(tempBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf = this.getMenuInflater();
        mf.inflate(R.menu.single_img_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.single_img_menu:
                realm.beginTransaction();
                realmResults.deleteFirstFromRealm();
                realm.commitTransaction();
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package draco.assignment4.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import draco.assignment4.Class.FaceRegion;
import draco.assignment4.Class.Photo;
import draco.assignment4.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


public class MainActivity extends AppCompatActivity {
    public Context context = this;
    public Realm main_realm;
    public RealmConfiguration config;
    public static ArrayList<String> fileList;

    Button img_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(context);

        //initialize the realm database
        config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        main_realm = Realm.getDefaultInstance();
        main_realm.beginTransaction();
        main_realm.deleteAll();
        main_realm.commitTransaction();

        //get filelist
        File FileDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/camera");
        fileList = new ArrayList<>();


        for(File file : FileDirectory.listFiles()){
            if(file.getName().toLowerCase().endsWith(".jpg")){
                fileList.add(file.toString());
            }
        }

        RealmQuery<Photo> p_query = main_realm.where(Photo.class);
        RealmResults<Photo> p_res = p_query.findAll();

        /*
        if(p_res.size() != 0){
            Intent view_activity = new Intent(this, GalleryActivity.class);
            this.startActivity(view_activity);
            finish();
        }
        */

        img_load = (Button) findViewById(R.id.LoadImg);
        img_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImageToRealm loadTask = new LoadImageToRealm(MainActivity.this);
                loadTask.execute();
            }
        });

    }

    private class LoadImageToRealm extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog dialog;
        private int count = 0;

        private LoadImageToRealm(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Doing something, please wait.");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setMax(fileList.size());
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Realm realm = Realm.getDefaultInstance();

            for(;count < fileList.size();count++){
                //convert uri to bitmap
                Bitmap c_photo = UriToBit(fileList.get(count));
                //initialize face detector
                FaceDetector detector = new FaceDetector.Builder(context).setTrackingEnabled(false).build();
                Frame frame = new Frame.Builder().setBitmap(c_photo).build();
                SparseArray<Face> faces = detector.detect(frame);
                detector.release();

                realm.beginTransaction();
                Photo photo = realm.createObject(Photo.class);
                photo.setPhotoPath(fileList.get(count));
                //RealmList<FaceRegion> face_list = new RealmList<>();
                for(int i = 0;i < faces.size();i++){
                    Face thisFace = faces.valueAt(i);
                    FaceRegion faceRegion = new FaceRegion();
                    faceRegion.setX(thisFace.getPosition().x);
                    faceRegion.setY(thisFace.getPosition().y);
                    faceRegion.setWidth(thisFace.getWidth());
                    faceRegion.setHeight(thisFace.getHeight());
                    //face_list.add(faceRegion);
                    photo.getFaceRegions().add(realm.copyToRealm(faceRegion));
                }
                //photo.setFaceRegions(realm.copyToRealm());
                realm.commitTransaction();
                publishProgress(count);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(dialog.isShowing())
                dialog.dismiss();
            Intent gView = new Intent(context, GalleryActivity.class);
            startActivity(gView);
        }
    }

    public Bitmap UriToBit(String photo){
        Bitmap bitmap = null;
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable=true;
        bitmap = BitmapFactory.decodeFile(photo, options);
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        double ratio = (double) x / (double) y;
        return Bitmap.createScaledBitmap(bitmap, (int) Math.round(720 * ratio), 720, false);
    }
}

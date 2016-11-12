package draco.assignment4.Class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.SparseArray;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import draco.assignment4.Activity.GalleryActivity;
import draco.assignment4.Activity.MainActivity;
import io.realm.Realm;

import static draco.assignment4.Activity.MainActivity.mdialog;
import static draco.assignment4.Activity.MainActivity.fileList;
import static draco.assignment4.Activity.MainActivity.count;
import static draco.assignment4.Activity.MainActivity.missing_img_list;

/**
 * Created by Draco on 2016-10-31.
 */

public class AsyncTaskModel extends AsyncTask<String, Integer, Void>{

    private Context ctx;

    public AsyncTaskModel(Context context) {
        this.ctx = context;
    }


    @Override
    protected Void doInBackground(String... params) {
        Realm realm = Realm.getDefaultInstance();

        //convert uri to bitmap
        Bitmap c_photo = UriToBit(params[0]);
        //initialize face detector
        FaceDetector detector = new FaceDetector.Builder(ctx).setTrackingEnabled(false).build();
        Frame frame = new Frame.Builder().setBitmap(c_photo).build();
        SparseArray<Face> faces = detector.detect(frame);
        detector.release();

        realm.beginTransaction();
        Photo photo = realm.createObject(Photo.class);
        photo.setPhotoPath(params[0]);
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
        realm.close();

        publishProgress(1);

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mdialog.setProgress(MainActivity.count++);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(count == missing_img_list.size()){
            mdialog.dismiss();
            Intent gView = new Intent(ctx, GalleryActivity.class);
            ctx.startActivity(gView);
            //((Activity)ctx).finish();
        }
    }


    private Bitmap UriToBit(String photo){
        Bitmap bitmap;
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable=true;
        bitmap = BitmapFactory.decodeFile(photo, options);
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        double ratio = (double) x / (double) y;
        return Bitmap.createScaledBitmap(bitmap, (int) Math.round(720 * ratio), 720, false);
    }
}

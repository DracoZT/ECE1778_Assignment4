package draco.assignment4.Class;

import android.net.Uri;
import io.realm.RealmObject;

/**
 * Created by Draco on 2016-10-25.
 */

public class Photo extends RealmObject {

    private Uri photoPath;
    private FaceRegion faceRegion;

    public Uri getPhotoPath(){ return photoPath; }
    public void setPhotoPath(Uri photoPath){ this.photoPath = photoPath; }
    public FaceRegion getFaceRegion(){ return faceRegion; }
    public void setFaceRegion(FaceRegion faceRegion){ this.faceRegion = faceRegion; }
}

package draco.assignment4.Class;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Draco on 2016-10-25.
 */

public class Photo extends RealmObject {

    private String photoPath;
    private RealmList<FaceRegion> faceRegions;

    public String getPhotoPath(){ return photoPath; }
    public void setPhotoPath(String photoPath){ this.photoPath = photoPath; }

    public RealmList<FaceRegion> getFaceRegions(){ return faceRegions; }
    public void setFaceRegions(RealmList<FaceRegion> faceRegions){ this.faceRegions = faceRegions; }
}

package draco.assignment4.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.realm.RealmRecyclerViewAdapter;
import draco.assignment4.R;
import io.realm.OrderedRealmCollection;


/**
 * Created by Draco on 2016-10-30.
 */

public class RecyclerViewAdapter extends RealmRecyclerViewAdapter<Photo, RecyclerViewHolders> {
    Context context;

    public RecyclerViewAdapter(Context context, OrderedRealmCollection<Photo> data) {
        super(context, data, true);
    }


    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        String path = getData().get(position).getPhotoPath();
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 180, 180);
        holder.Photo.setImageBitmap(bitmap);
        //Uri photo_path = Uri.parse(path);
        //Uri mpath = MainActivity.fileList.get(position);
        //holder.Photo.setImageURI(photo_path);
    }

}

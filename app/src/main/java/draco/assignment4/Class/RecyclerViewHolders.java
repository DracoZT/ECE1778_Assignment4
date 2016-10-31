package draco.assignment4.Class;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import draco.assignment4.Activity.GalleryActivity;
import draco.assignment4.Activity.SingleImageView;
import draco.assignment4.R;
import io.realm.RealmResults;

/**
 * Created by Draco on 2016-10-30.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView Photo;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        Photo = (ImageView) itemView.findViewById(R.id.img);
    }

    @Override
    public void onClick(View v) {
        String photoUri = GalleryActivity.realmResults.get(getLayoutPosition()).getPhotoPath();
        Intent fs_view = new Intent(v.getContext(), SingleImageView.class).putExtra("photo_info", photoUri);
        v.getContext().startActivity(fs_view);
    }
}

package draco.assignment4.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import draco.assignment4.Class.AsyncTaskModel;
import draco.assignment4.Class.Photo;
import draco.assignment4.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity{
    public Context context = this;
    public Realm main_realm;
    public RealmConfiguration config;
    public static ArrayList<String> fileList;
    public static ProgressDialog mdialog;
    public static int count;
    private int thread;

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

        if(p_res.size() != 0){
            Intent view_activity = new Intent(this, GalleryActivity.class);
            this.startActivity(view_activity);
            main_realm.close();
            finish();
        }

        //set img button
        img_load = (Button) findViewById(R.id.LoadImg);
        img_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pick number of thread")
                        .setItems(R.array.list_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                thread = (int) Math.pow(2, which);
                                mdialog = new ProgressDialog(MainActivity.this);
                                mdialog.setMessage("Importing photos now.");
                                mdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mdialog.setProgress(0);
                                mdialog.setMax(fileList.size());
                                mdialog.show();
                                Executor executor = new ThreadPoolExecutor(thread, 8, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(fileList.size()));

                                for(int i = 0;i < fileList.size();i++){
                                    new AsyncTaskModel(MainActivity.this).executeOnExecutor(executor, fileList.get(i));
                                }
                            }
                        });

                builder.create().show();

            }
        });
    }

}

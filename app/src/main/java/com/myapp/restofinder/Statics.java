package com.myapp.restofinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.myapp.restofinder.listeners.OnFinishedLoadingImage;
import com.myapp.restofinder.models.User;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.Date;

public class Statics {
    public static User USER;

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = 0;
        listView.setLayoutParams(par);
        listView.requestLayout();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public static void setImage(String key, ImageView image, OnFinishedLoadingImage callback) {
        final long ONE_MEGABYTE = 1024 * 1024;
        FirebaseStorage.getInstance().getReference().child(key).getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            File file = new File(Environment.getDownloadCacheDirectory().getPath() + File.separator + new Date().toString());

            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
                Picasso.get().load(file).into(image);
            } catch (Exception e) {
                Toast.makeText(image.getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            /*Bitmap bmp = BitmapFactory.decodeByteArray(file, 0, file.length);
            image.setImageBitmap(Bitmap.createBitmap(bmp));*/
            if (callback != null)
                callback.onFinished();

        });
    }
}

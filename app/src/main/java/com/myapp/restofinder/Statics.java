package com.myapp.restofinder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.myapp.restofinder.listeners.OnFinishedLoadingImageListener;
import com.myapp.restofinder.models.User;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

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

    public static void setImage(String key, ImageView image, OnFinishedLoadingImageListener callback) {
        final long ONE_MEGA = 1024 * 1024;
        FirebaseStorage.getInstance().getReference().child(key).getBytes(ONE_MEGA).addOnSuccessListener(bytes -> {
            File file = new File(image.getContext().getCacheDir() + File.separator + key.split("/")[1]);
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
                Picasso.get().load(file).into(image);
            } catch (Exception e) {
                Toast.makeText(image.getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            if (callback != null)
                callback.onFinished();

        });
    }
}

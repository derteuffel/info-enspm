package com.derteuffel.infoenspm.adaptor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.derteuffel.infoenspm.R;
import com.derteuffel.infoenspm.entities.Post;
import com.derteuffel.infoenspm.entities.Utilisateur;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    private List<Post> posts= new LinkedList<>();
    ImageLoader imageLoader= AppController.getmInstance().getmImageLoader();


    public PostAdapter(LayoutInflater inflater, Activity activity){
        this.inflater=inflater;
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return posts.size();
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (posts==null)
            Log.e("posts", "null");
        else
            Log.e("posts", "pas null");

        if (inflater==null){
            inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view==null){
            view=inflater.inflate(R.layout.post_list_view, null);
        }
        if (imageLoader==null)
            imageLoader= AppController.getmInstance().getmImageLoader();
            CircleImageView imageView=(CircleImageView) view.findViewById(R.id.post_image_user);
            TextView userName=(TextView) view.findViewById(R.id.user_name_posted);
            TextView datePosted=(TextView) view.findViewById(R.id.post_date);
            TextView title=(TextView)view.findViewById(R.id.post_title);

            //TextView pieces= view.findViewById(R.id.start_btn);

            //getting data for row

            Post post=posts.get(i);
            imageView.setImageBitmap(this.getImageBitmap(post.getAvatar()));
            userName.setText(post.getUserName());
            title.setText(post.getTitre());

            long millisecond=post.getDate().getTime();
            String date_posted= android.text.format.DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            datePosted.setText(date_posted);

           // List<String> piecesjte=new ArrayList<>();

           // for (String str:post.getPieceJointes().get(i).getUrl()){
            Log.e("post", post.toString());


        return view;
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("resoution", "Error getting bitmap", e);
        }
        return bm;
    }
}

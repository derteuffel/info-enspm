package com.derteuffel.infoenspm;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.derteuffel.infoenspm.adaptor.AppController;
import com.derteuffel.infoenspm.adaptor.PostAdapter;
import com.derteuffel.infoenspm.entities.PieceJointe;
import com.derteuffel.infoenspm.entities.Post;
import com.derteuffel.infoenspm.entities.Utilisateur;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private static final String url="http://192.168.8.103:9000/api/posts";
    List<Post> posts = new ArrayList<Post>();
    private PostAdapter adapter;
    private Context context;
    private ListView _list_view;

    private MaterialSearchView searchView;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_home, container, false);

        searchView=view.findViewById(R.id.search_nav);
        _list_view=(ListView)view.findViewById(R.id.list_view);
        adapter= new PostAdapter(getLayoutInflater(),getActivity());
        _list_view.setAdapter(adapter);

       /* searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                //if closed searchview , listview return default
                _list_view=(ListView)view.findViewById(R.id.list_view);
                adapter= new PostAdapter(getLayoutInflater(),getActivity());
                _list_view.setAdapter(adapter);
            }
        });



        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               if (newText!= null && !newText.isEmpty()){
                   List<String> list= new ArrayList<String>();
                   for (String item:posts.get().getUserName())
               }
            }
        });*/

        //create volley request obj

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //parsing json
                for (int i=0;i<response.length();i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Post post = new Post();
                        post.setDate(new Date(object.getLong("date")));
                        //description
                        post.setDescription(object.getString("description"));

                        //titre
                        post.setTitre(object.getString("titre"));

                        //piece jointe
                        JSONArray liste = object.getJSONArray("pieces");
                        List<String> pieces = new LinkedList<>();
                        for (i = 0; i < liste.length(); i++) {

                            JSONObject item = liste.getJSONObject(i);
                            pieces.add(item.getString("fileDownloadUri"));
                        }
                        post.setPieceJointes(pieces);

                        JSONObject userJson = object.getJSONObject("user");
                        post.setUserName(userJson.getString("nom"));
                        post.setAvatar(userJson.getString("avatar"));

                        //post.setPieceJointes();
                        //Log.e("response", response.toString());
                        posts.add(post);
                        Log.e("post", post.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                adapter.setPosts(posts);
                    adapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getmInstance().addToRequestQueue(jsonArrayRequest);
        // Inflate the layout for this fragment
        return view;
    }

}

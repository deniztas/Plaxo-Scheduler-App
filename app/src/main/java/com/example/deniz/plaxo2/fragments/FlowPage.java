package com.example.deniz.plaxo2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.activities.FacebookActivity;
import com.example.deniz.plaxo2.adapters.FacebookAdapter;
import com.example.deniz.plaxo2.model.FacebookObj;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Deniz on 17.03.2018.
 */


public class FlowPage extends Fragment {

    private Button connectFacebookButton;
    public static List<FacebookObj> posts;
    private static String accessToken;
    private FacebookAdapter mAdapter;
    private ListView facebooklistview;
    SwipeRefreshLayout mySwipeRefreshLayout;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flowpage_layout, container, false);

        connectFacebookButton = (Button) rootView.findViewById(R.id.facebook_button);
        facebooklistview = (ListView) rootView.findViewById(R.id.facebook_listview);
        mySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);


        facebooklistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (facebooklistview == null || facebooklistview.getChildCount() == 0) ? 0 : facebooklistview.getChildAt(0).getTop();
                mySwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        connectFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), FacebookActivity.class);
                startActivity(in);
            }
        });

        if (posts == null) {
            getFacebookMe();
        } else {
            mAdapter = new FacebookAdapter(getActivity(), posts);
            facebooklistview.setAdapter(mAdapter);
        }

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (accessToken != null && AccessToken.getCurrentAccessToken() != null) {
                            getFacebookMe();
                            mAdapter = new FacebookAdapter(getActivity(), posts);
                            facebooklistview.setAdapter(mAdapter);
                            mySwipeRefreshLayout.setRefreshing(false);
                        } else {
                            mySwipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), "Login Facebook First", Toast.LENGTH_SHORT).show();

                            if (posts != null) {
                                posts.clear();
                                mAdapter = new FacebookAdapter(getActivity(), posts);
                                facebooklistview.setAdapter(mAdapter);
                            }
                        }
                    }
                }
        );
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("myToken", AccessToken.getCurrentAccessToken() + "");
        if (AccessToken.getCurrentAccessToken() != null)
            accessToken = AccessToken.getCurrentAccessToken().getToken();


        if (accessToken != null && AccessToken.getCurrentAccessToken() != null) {
            if (posts == null) {
                getFacebookMe();
            } else {
                mAdapter = new FacebookAdapter(getActivity(), posts);
                facebooklistview.setAdapter(mAdapter);
            }
        } else {
            if (posts != null) {
                posts.clear();
                mAdapter = new FacebookAdapter(getActivity(), posts);
                facebooklistview.setAdapter(mAdapter);
            }
        }
    }

    public void getFacebookMe() {
        //String me = "";
        if (accessToken != null && AccessToken.getCurrentAccessToken() != null) {
            posts = new ArrayList<>();
            final AccessToken PageAT = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), null, AccessTokenSource.FACEBOOK_APPLICATION_NATIVE, AccessToken.getCurrentAccessToken().getExpires(), null);

            GraphRequest request = GraphRequest.newMeRequest(PageAT, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    try {
                        JSONObject feed = object.getJSONObject("feed");
                        JSONArray jsonarray = feed.getJSONArray("data");

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String message = "";
                            String time = "";

                            if (jsonobject.has("message")) {
                                message = jsonobject.getString("message");
                            } else {
                                continue;
                            }

                            if (jsonobject.has("created_time")) {
                                time = jsonobject.getString("created_time");
                            } else {
                                continue;
                            }

                            String id = jsonobject.getString("id");

                            FacebookObj fb = new FacebookObj(message, id);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ssZ");
                            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                            Date date = formatter.parse(time);
                            fb.setCreatedDate(date);
                            posts.add(fb);
                        }

                    } catch (Exception e) {
                        Log.d("myException", e + "");
                    }
                    mAdapter = new FacebookAdapter(getActivity(), posts);
                    facebooklistview.setAdapter(mAdapter);
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,feed,picture");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            if (posts != null) {
                posts.clear();
                mAdapter = new FacebookAdapter(getActivity(), posts);
                facebooklistview.setAdapter(mAdapter);
            }
        }

    }
}

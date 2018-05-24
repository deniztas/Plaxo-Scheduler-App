package com.example.deniz.plaxo2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.activities.FacebookActivity;

/**
 * Created by Deniz on 17.03.2018.
 */


public class FlowPage extends Fragment {

    private Button connectFacebookButton;
    private Button receiveFriends;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flowpage_layout, container, false);

        connectFacebookButton = (Button) rootView.findViewById(R.id.facebook_button);

        connectFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), FacebookActivity.class);
                startActivity(in);
            }
        });

        /*try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.example.deniz.plaxo2",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
                Toast.makeText(getActivity().getApplicationContext(), Base64.encodeToString(md.digest(),
                        Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        String accessToken = preferences.getString("accessToken", null);

        if(accessToken != null){
            Log.d("myToken", accessToken);
            final AccessToken PageAT = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), null, AccessTokenSource.FACEBOOK_APPLICATION_NATIVE, AccessToken.getCurrentAccessToken().getExpires(), null);

            receiveFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GraphRequest request = GraphRequest.newMyFriendsRequest(
                            PageAT,
                            new GraphRequest.GraphJSONArrayCallback() {
                                @Override
                                public void onCompleted(JSONArray array, GraphResponse response) {
                                    //System.out.print(array);
                                    //System.out.print(response);
                                    // Insert your code here
                                }
                            });

                    request.executeAsync();
                }
            });
        }*/


        return rootView;
    }

}

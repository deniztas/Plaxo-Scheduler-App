package com.example.deniz.plaxo2.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deniz.plaxo2.R;

/**
 * Created by Deniz on 17.03.2018.
 */

public class ContactPage extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.contactpage_layout, container, false);
        return rootView;
    }

}
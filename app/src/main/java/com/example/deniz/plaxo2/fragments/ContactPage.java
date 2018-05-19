package com.example.deniz.plaxo2.fragments;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.adapters.ContactAdapter;
import com.example.deniz.plaxo2.model.Contact;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static android.content.Context.ACCOUNT_SERVICE;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * Created by Deniz on 17.03.2018.
 */

public class ContactPage extends Fragment{

    Button permissionButton;
    TextView permissionText;
    ListView listView;
    private ContactAdapter mAdapter;
    static final Integer ACCOUNTS = 0x6;
    static final Integer READ_CONTACT = 0x1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.contactpage_layout, container, false);
        permissionButton = (Button) rootView.findViewById(R.id.permissionButton);
        permissionText = (TextView) rootView.findViewById(R.id.permissionText);
        listView = (ListView) rootView.findViewById(R.id.contactListview);


        boolean granted = checkContactPermission();

        if(granted){
            permissionButton.setVisibility(View.GONE);
            permissionText.setVisibility(View.GONE);

            ArrayList contacts = getContacts(getActivity());

            //ArrayAdapter adapter = new ArrayAdapter<Contact>(getActivity(), R.layout.contactpage_layout, R.id.test, contacts);
            mAdapter = new ContactAdapter(getActivity(),contacts);

            listView.setAdapter(mAdapter);

            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        Object o = listView.getItemAtPosition(position);
                        System.out.print(o);
                    }
                });
        }


        //REQUEST PERMISSION
        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission(Manifest.permission.GET_ACCOUNTS,ACCOUNTS);
                askForPermission(Manifest.permission.READ_CONTACTS,ACCOUNTS);
            }
        });

        return rootView;
    }

    private boolean checkContactPermission()
    {
        String permission = Manifest.permission.GET_ACCOUNTS;
        String permission2 = Manifest.permission.READ_CONTACTS;

        int res = getContext().checkCallingOrSelfPermission(permission);
        int res2 = getContext().checkCallingOrSelfPermission(permission2);
        return (res == PackageManager.PERMISSION_GRANTED) && (res2 == PackageManager.PERMISSION_GRANTED);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},1);
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},1);
            }
        } else {
            Toast.makeText(getActivity(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Accounts
                case 6:
                    AccountManager manager = (AccountManager) getActivity().getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    Toast.makeText(getActivity(),""+list[0].name,Toast.LENGTH_SHORT).show();
                    for(int i=0; i<list.length;i++){
                        Log.e("Account "+i,""+list[i].name);
                    }
            }

            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Contact> getContacts(Context ctx) {
        List<Contact> currentContacts = Contact.listAll(Contact.class);
        ArrayList<Contact> list = new ArrayList<Contact>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    int primaryId = 1;

                    List<String> checkDuplicate = new ArrayList<String>();


                    for (int i = 0; i < currentContacts.size(); i++) {
                        checkDuplicate.add(currentContacts.get(i).getContactName());
                    }

                    while (cursorInfo.moveToNext()) {

                        String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Contact contact = new Contact();
                        contact.setContactName(contactName);
                        contact.setPhoneNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        contact.setContactId(primaryId);

                        if(!checkDuplicate.contains(contactName)){
                            contact.save();
                            checkDuplicate.add(contactName);
                            primaryId++;
                            list.add(contact);
                        }

                    }

                    cursorInfo.close();

                }
            }
            list.addAll(currentContacts);
            cursor.close();
        }
        System.out.print(list);

        return list;
    }



}
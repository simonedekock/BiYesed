package com.example.android.biyesed1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdekock on 2017-03-30.
 */

public class ContactsRequestActivity extends AppCompatActivity {
    private static final int CONTACTS_LOADER_ID = 1;
    private static final int REQUEST_READ_CONTACTS = 2;
    public static final int RESULT_GARMENT_FEEDBACK = 3;
    List<ContactModel> mContacts;
    ListView mContactsListTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.contacts_list_layout);

        requestPermission();


    }
    public static List<ContactModel> getContacts(Context ctx) {
        List<ContactModel> list = new ArrayList<>();
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
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
                        info.id = id;
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        info.photo = photo;
                        info.photoURI= pURI;
                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
        }
        return list;
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mContacts = ContactsActivity.getContacts(this);
            populateContacts();
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);
        } else {
            mContacts = ContactsActivity.getContacts(this);
            populateContacts();

        }
    }
    public void populateContacts(){
        if(mContacts != null && mContacts.size() != 0) {
            setContentView(R.layout.contacts_list_layout);
            mContactsListTextView = (ListView) findViewById(R.id.contacts_list_view);
            ContactsAdapter<ContactModel> mHistory = new ContactsAdapter<ContactModel>(this, (ArrayList<ContactModel>) mContacts);
            mContactsListTextView.setAdapter(mHistory);
            // ListView Item Click Listener
            mContactsListTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent intentToHandleGarmentActivity = new Intent(ContactsRequestActivity.this, GarmentActivity.class);
                    startActivityForResult(intentToHandleGarmentActivity, RESULT_GARMENT_FEEDBACK);

                }

            });
            //setContentView(R.layout.contacts_list_layout);
        }else
        {
            Toast.makeText(getApplicationContext(),"No Contacts found",Toast.LENGTH_LONG).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_GARMENT_FEEDBACK && resultCode == RESULT_OK && null != data) {
            String feedback = data.getStringExtra("Feedback");
            Toast.makeText(ContactsRequestActivity.this, "Feedback: " + feedback, Toast.LENGTH_SHORT).show();
        }
    }

}
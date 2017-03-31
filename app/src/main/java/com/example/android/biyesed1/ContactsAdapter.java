package com.example.android.biyesed1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsAdapter<ContactModel> extends ArrayAdapter<ContactModel> {



    public ContactsAdapter(Context context, ArrayList<ContactModel> users) {
        super(context, 0,users);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //if(getItem(position) != null && getItem(position) instanceof ContactModel) {
        com.example.android.biyesed1.ContactModel contactModel = (com.example.android.biyesed1.ContactModel) getItem(position);

        //}
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_contact_layout, parent, false);
        }
        // Lookup view for data population
        TextView contactId = (TextView) convertView.findViewById(R.id.id);
        TextView contactName = (TextView) convertView.findViewById(R.id.name);
        TextView contactMobileNumber = (TextView) convertView.findViewById(R.id.mobileNumber);
        ImageView contactPhoto = (ImageView) convertView.findViewById(R.id.photo);
        TextView contactPhotoUri = (TextView) convertView.findViewById(R.id.photoUri);
        // Populate the data into the template view using the data object


        contactId.setText(contactModel.id);
        contactName.setText(contactModel.name);
        contactMobileNumber.setText(contactModel.mobileNumber);
        contactPhoto.setImageBitmap(contactModel.photo);
        contactPhotoUri.setText(contactModel.photoURI.toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
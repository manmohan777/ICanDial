package com.mannuharishsingh.icandial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DialingActivity extends AppCompatActivity {
    List<Uri> images;
    List<String> names;
    List<AndroidContacts> allContactDetail;
    RecyclerView recyclerView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialing);
        recyclerView = findViewById(R.id.dialingList);
        mContext = this;
        images = new ArrayList<>();
        names = new ArrayList<>();

        allContactDetail=new ArrayList<>();
        //recordset
        Cursor cursor_android_contacts = null;
        //makes connection to contact database in android
        ContentResolver contentResolver= getContentResolver();
        try{ //sometimes system has no allowance so try catch
            // initialising the recordset
            //all null means give me all contacts

            cursor_android_contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
            Log.d("contacts1",cursor_android_contacts.getCount()+"");
        }
        catch(Exception e){
            Log.d("error",e.getMessage());
            Toast.makeText(getApplicationContext(),"error is this "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        if(cursor_android_contacts.getCount()>0) {

            //   Toast.makeText(getApplicationContext(),"0 contacts retrieved "+cursor_android_contacts.getCount(),Toast.LENGTH_SHORT).show();
            while (cursor_android_contacts.moveToNext()) {
                //getting contact id
                long id = cursor_android_contacts.getLong(cursor_android_contacts.getColumnIndex(ContactsContract.Contacts._ID));
                AndroidContacts androidContacts = new AndroidContacts();




                //uri of images

                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, (id));

                    //check that photo exist in that contact or not it will return null
                InputStream photoInputStream =
                        ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, person);
                if (photoInputStream!=null) {

                    String contact_id = cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.Contacts._ID));
                    String contact_name = cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //adding image uri to list
                    images.add(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.DISPLAY_PHOTO));


                    // Uri imageUri=cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.Contacts.Phone));
                    androidContacts.contact_name = contact_name;

                    //now we will get all phone number
                    int hasPhoneNumber = Integer.parseInt(cursor_android_contacts.getString(cursor_android_contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {
                        // new cursor for that perticular contact id;
                        //getting a required recordset
                        Cursor phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                , null
                                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                                , new String[]{contact_id},
                                null);
                        int count = 1;
                        while (phoneCursor.moveToNext()) {
                            String contact_phone_number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if (count == 2) {
                                androidContacts.contact_mobilenumber2 = contact_phone_number;
                                break;
                            }

                            androidContacts.contact_mobilenumber1 = contact_phone_number;
                            count++;
                        }
                        phoneCursor.close();
                    }

                    allContactDetail.add(androidContacts);
                    //  images.add(R.drawable.ic_android_black_24dp);
                }
                else{

                }
                Log.d("size123", "" + allContactDetail.size());


            }
        }
        else{
            Toast.makeText(getApplicationContext(),"0 contacts retrieved ",Toast.LENGTH_SHORT).show();
        }
        //sending data to adapter
        AdapterForDialingApp adapterForDialingApp = new AdapterForDialingApp(this, allContactDetail, images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapterForDialingApp);
    }

    @SuppressLint("MissingPermission")
    public void makeAPhoneCall(String phno,Context ctx) {




        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions((Activity) ctx,
                    new String []{Manifest.permission.CALL_PHONE},1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        else{
            String dial = "tel:" + phno.trim();
            ctx.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }


        //}

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager
                    .PERMISSION_GRANTED){

            }
            else {
                Toast.makeText(DialingActivity.this,"Permission Denied",
                        Toast.LENGTH_SHORT);
            }
        }
    }
    public class AndroidContacts{
        public String contact_name="";
        public String contact_mobilenumber1="";
        public String contact_mobilenumber2="";
        public int android_contact_id=0;

    }
}

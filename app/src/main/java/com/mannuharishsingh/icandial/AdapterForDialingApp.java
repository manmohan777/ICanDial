package com.mannuharishsingh.icandial;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class AdapterForDialingApp extends RecyclerView.Adapter<AdapterForDialingApp.ViewHolder>{
   // List<String> nameList;
    List<DialingActivity.AndroidContacts> myContactsList;
    List<Uri> imageList;
    LayoutInflater inflater;
    Context mContext;
    public AdapterForDialingApp(Context ctx, List<DialingActivity.AndroidContacts> myContactsList, List<Uri> imageList){
        this.myContactsList=myContactsList;
        this.imageList=imageList;
        this.inflater=LayoutInflater.from(ctx);
        this.mContext = ctx;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= inflater.inflate(R.layout.dialing_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
           // viewHolder.nameTextView.setText(myContactsList.get(i).contact_name);
           // if(imageList!=null) {
                viewHolder.imageImageView.setImageURI(imageList.get(i));
            //}
//            else{
//                viewHolder.imageImageView.setImageResource(R.drawable.ic_android_black_24dp);
//            }

    }

    @Override
    public int getItemCount() {
        return myContactsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        ImageView imageImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          //  nameTextView= itemView.findViewById(R.id.textView);
            imageImageView= itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(v.getContext(),"Clicked this name :-"+nameList.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                   // String phno = "7017105937";
                   String phno= myContactsList.get(getAdapterPosition()).contact_mobilenumber1;
                    new DialingActivity().makeAPhoneCall(phno,mContext);
                   // mContext.startActivity(new Intent(mContext,DialingActivity.class));

                }


            });
        }


    }
}


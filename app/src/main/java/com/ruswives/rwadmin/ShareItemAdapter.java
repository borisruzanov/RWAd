package com.ruswives.rwadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.ruswives.rwadmin.database.Prefs;
import com.ruswives.rwadmin.model.FsUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ShareItemAdapter extends RecyclerView.Adapter<ShareItemAdapter.MyViewHolder> {

    private List<FsUser> fsUserList;
    private Context context;
    private RecycleListItemListner itemListner;
    private Prefs mPrefs;

    public ShareItemAdapter(List<FsUser> fsUserList, Context context, RecycleListItemListner itemListner) {
        this.fsUserList = fsUserList;
        this.context = context;
        this.itemListner = itemListner;
        mPrefs = new Prefs(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.model_list, parent, false);
        return new MyViewHolder(v, itemListner);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
      colorItem(fsUserList.get(position), holder);
        if (!fsUserList.get(position).getImage().isEmpty())
            Picasso.with(context).load(fsUserList.get(position).getImage()).into(holder.imageView);
        if (fsUserList != null) {
            if (fsUserList.get(position).getGender().equals(Consts.FEMALE)){
                holder.textView.setText(getFemaleFinalString(fsUserList.get(position)));
            } else {
                holder.textView.setText(getMaleFinalString(fsUserList.get(position)));
            }
        } else {
            holder.textView.setText("Error");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemListner.itemListner(fsUserList.get(position), holder);
            }
        });

        holder.mRevomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("SocialAccounts").child(fsUserList.get(position).getGender()).child(fsUserList.get(position).getUid()).removeValue();
            }
        });
    }

    private void colorItem(FsUser fsUser, MyViewHolder holder) {
        String uid = mPrefs.getValue("vHiRR8bamxUT2c0TSJEf2ok5GfE3");
        String prefsUid = mPrefs.getValue(fsUser.getUid());
        if (prefsUid.equals(Consts.CONFIRMED)){
            holder.mContainer.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            String s = "";
        }
    }

    private String getMaleFinalString(FsUser fsUser) {
        String finalString = "";
        String brake = "\n";

        String name = "";
        if (!fsUser.getName().equals("") && !fsUser.getName().equals(Consts.DEFAULT)) {
            name = "Name: " + fsUser.getName() + brake;
        } else {
            name = Consts.DEFAULT;
        }

        String age = "";
        if (!fsUser.getName().equals("") && !fsUser.getAge().equals(Consts.DEFAULT)) {
            age = "Age: " + fsUser.getAge() + brake;
        } else {
            age = Consts.DEFAULT;
        }

        String country = "";
        if (!fsUser.getName().equals("") && !fsUser.getCountry().equals(Consts.DEFAULT)) {
            country = "From: " + fsUser.getCountry() + brake;
        } else {
            country = Consts.DEFAULT;
        }

        String relationship_status = "";
        if (!fsUser.getName().equals("") && !fsUser.getRelationship_status().equals(Consts.DEFAULT)) {
            relationship_status = "Marital status: " + fsUser.getRelationship_status() + brake;
        } else {
            relationship_status = Consts.DEFAULT;
        }

        String hobby = "";
        if (!fsUser.getName().equals("") && !fsUser.getHobby().equals(Consts.DEFAULT)) {
            hobby = "Looking For: " + fsUser.getHobby() + brake;
        } else {
            hobby = Consts.DEFAULT;
        }

        String id = "";
        id = "ID: " + fsUser.getId_soc() + brake;

        List<String> stringList = new ArrayList<>();
        stringList.add(name);
        stringList.add(age);
        stringList.add(country);
        stringList.add(id);

        for (String s : stringList) {
            if (!s.equals(Consts.DEFAULT)) {
                finalString += s;
            }
        }

        String hashTags = brake + "#lookingforlove" +
                " #lookingforfriends " +
                "#dating " +
                "#datingservice " +
                "#datingapps " +
                "#datingwithpurpose " +
                "#DatingSite " +
                "#datingsites " +
                "#datingrichapp " +
                "#seriousrelationship";

        finalString += hashTags;
        String signature = brake + brake + "С этим человеком, Вы сможете пообщаться в нашем приложении для Android на сайте ruswives.com" + brake + brake + "Его " + id;
        finalString += signature;
        return finalString;
    }

    private String getFemaleFinalString(FsUser fsUser) {
        String finalString = "";
        String brake = "\n";
        String name = "";
        if (!fsUser.getName().equals("") && !fsUser.getName().equals(Consts.DEFAULT)) {
            name = "Name: " + fsUser.getName() + brake;
        } else {
            name = Consts.DEFAULT;
        }

        String age = "";
        if (!fsUser.getName().equals("") && !fsUser.getAge().equals(Consts.DEFAULT)) {
            age = "Age: " + fsUser.getAge() + brake;
        } else {
            age = Consts.DEFAULT;
        }

        String country = "";
        if (!fsUser.getName().equals("") && !fsUser.getCountry().equals(Consts.DEFAULT)) {
            country = "From: " + fsUser.getCountry() + brake;
        } else {
            country = Consts.DEFAULT;
        }

        String relationship_status = "";
        if (!fsUser.getName().equals("") && !fsUser.getRelationship_status().equals(Consts.DEFAULT)) {
            relationship_status = "Marital status: " + fsUser.getRelationship_status() + brake;
        } else {
            relationship_status = Consts.DEFAULT;
        }

        String hobby = "";
        if (!fsUser.getName().equals("") && !fsUser.getHobby().equals(Consts.DEFAULT)) {
            hobby = "Looking For: " + fsUser.getHobby() + brake;
        } else {
            hobby = Consts.DEFAULT;
        }

        String id = "";
        id = "ID: " + fsUser.getId_soc() + brake;

        List<String> stringList = new ArrayList<>();
        stringList.add(name);
        stringList.add(age);
        stringList.add(country);
        stringList.add(id);

        for (String s : stringList) {
            if (!s.equals(Consts.DEFAULT)) {
                finalString += s;
            }
        }

        String hashTags = brake + "#lookingforlove" +
                " #lookingforfriends " +
                "#dating " +
                "#datingservice " +
                "#datingapps " +
                "#datingwithpurpose " +
                "#DatingSite " +
                "#datingsites " +
                "#datingrichapp " +
                "#seriousrelationship";

        finalString += hashTags;
        String signature = brake + brake + "You can find and chat with this person in our app ruswives.com" + brake + brake + id;
        finalString += signature;
        return finalString;
    }

    @Override
    public int getItemCount() {
        return fsUserList != null ? fsUserList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RecycleListItemListner itemListner;
        public Button mRevomeBtn;
        public CardView mContainer;

        public MyViewHolder(@NonNull View itemView, RecycleListItemListner itemListner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.model_list_imgview);
            textView = itemView.findViewById(R.id.model_list_txtview);
            mRevomeBtn = itemView.findViewById(R.id.model_list_remove);
            mContainer = itemView.findViewById(R.id.model_list_container);
            this.itemListner = itemListner;
        }
    }
}

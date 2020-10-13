package com.rakpak.pak_parak_admin.BottomnavPage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.Model.NewsListData;
import com.rakpak.pak_parak_admin.R;
import com.rakpak.pak_parak_admin.Upload.JobUpload;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewsTabPages extends Fragment {

    private MaterialCardView jobsbutton;
    private ExtendedFloatingActionButton logout;
    private FirebaseAuth Mauth;
    private RecyclerView newslist;
    private DatabaseReference Mnewsdatabase;
    private String CurrentDate;
    public NewsTabPages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.news_tab_pages, container, false);


        Mauth = FirebaseAuth.getInstance();
        jobsbutton = view.findViewById(R.id.JobsButtonID);
        logout = view.findViewById(R.id.LogOutButton);

        Mnewsdatabase = FirebaseDatabase.getInstance().getReference().child("News");
        Mnewsdatabase.keepSynced(true);
        newslist = view.findViewById(R.id.NewsListID);
        newslist.setHasFixedSize(true);
        newslist.setLayoutManager(new LinearLayoutManager(getActivity()));


        Calendar calendar_date = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat_date = new SimpleDateFormat(DataManager.DatePattern);
        CurrentDate = simpleDateFormat_date.format(calendar_date.getTime());


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               goto_jobbutton(new JobUpload());
            }
        });



        return view;
    }


    @Override
    public void onStart() {

        Query firebasequry = Mnewsdatabase.orderByChild("short");

        FirebaseRecyclerAdapter<NewsListData, NewsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NewsListData, NewsHolder>(
                NewsListData.class,
                R.layout.news_banner,
                NewsHolder.class,
                firebasequry
        ) {
            @Override
            protected void populateViewHolder(final NewsHolder newsHolder, NewsListData newsListData, final int i) {

                final String UID = getRef(i).getKey();
                Mnewsdatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    if(dataSnapshot.hasChild(DataManager.NewsVisiable)){

                                        String visiable_type = dataSnapshot.child(DataManager.NewsVisiable).getValue().toString();

                                        if(visiable_type.equals(DataManager.Visiable)){

                                            if(dataSnapshot.hasChild("message")){
                                                String messageget = dataSnapshot.child("message").getValue().toString();
                                                newsHolder.setMessageset(messageget);
                                            }

                                            if(dataSnapshot.hasChild("jobimage")){
                                                String imageuri = dataSnapshot.child("jobimage").getValue().toString();
                                                newsHolder.setImageset(imageuri);
                                            }

                                            if(dataSnapshot.hasChild("CurrentTime") || dataSnapshot.hasChild("CurrentDate")){
                                                String time = dataSnapshot.child("CurrentTime").getValue().toString();
                                                String date = dataSnapshot.child("CurrentDate").getValue().toString();


                                                if(date.equals(CurrentDate)){
                                                    newsHolder.lottieAnimationView.setVisibility(View.VISIBLE);
                                                    newsHolder.setTimeand_dateset("Post Today");
                                                    newsHolder.timeand_date.setTextColor(getResources().getColor(R.color.red));
                                                }
                                                else {
                                                    newsHolder.lottieAnimationView.setVisibility(View.INVISIBLE);
                                                    newsHolder.setTimeand_dateset(date);
                                                }
                                            }

                                            newsHolder.Mview.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View view) {

                                                    MaterialAlertDialogBuilder Mbuilder = new MaterialAlertDialogBuilder(getActivity());

                                                    CharSequence charSequence[] = new CharSequence[]{
                                                            "Update",
                                                            "Delete"
                                                    };

                                                    Mbuilder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            if(i == 0){

                                                            }
                                                            if(i == 1){
                                                                Mnewsdatabase.child(UID).child(DataManager.NewsVisiable).setValue(DataManager.InVisiable);
                                                            }
                                                        }
                                                    });

                                                    AlertDialog alertDialog = Mbuilder.create();
                                                    alertDialog.show();


                                                    return true;
                                                }
                                            });


                                        }

                                        else if(visiable_type.equals(DataManager.InVisiable)){
                                            newsHolder.banner_layout.setVisibility(View.GONE);
                                        }

                                    }




                                    /// todo end
                                }


                                else {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };


        newslist.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class NewsHolder extends RecyclerView.ViewHolder{

        private View Mview;
        private Context context;
        private MaterialTextView timeand_date, message ;
        private ImageView image;
        private LottieAnimationView lottieAnimationView;
        private RelativeLayout banner_layout;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();
            timeand_date = Mview.findViewById(R.id.NewsTimeAndDate);
            message = Mview.findViewById(R.id.NewsMessage);
            image = Mview.findViewById(R.id.NewsImage);
            lottieAnimationView = Mview.findViewById(R.id.NewAnimactionID);

            banner_layout = Mview.findViewById(R.id.BannerLayout);
        }

        private void setTimeand_dateset(String time){
            timeand_date.setText(time);
        }

        private void setMessageset(String mess){
            message.setText(mess);
        }

        private void setImageset(String img){
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(img).into(image);
                }
            });

        }
    }

    private void goto_jobbutton(Fragment fragment){
        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFream, fragment).addToBackStack(null);
            transaction.commit();
        }
    }
}
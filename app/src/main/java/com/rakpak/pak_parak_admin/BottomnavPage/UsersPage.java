package com.rakpak.pak_parak_admin.BottomnavPage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakpak.pak_parak_admin.BottomSheedDioloag.AuthBottomSheed;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.Model.UsersList;
import com.rakpak.pak_parak_admin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersPage extends Fragment {

    private RecyclerView userlist;
    private DatabaseReference Muser_database;
    private String emailtext, passwordtext;

    public UsersPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.users_page, container, false);

        Muser_database = FirebaseDatabase.getInstance().getReference().child(DataManager.UserRoot);
        Muser_database.keepSynced(true);
        userlist = view.findViewById(R.id.UserView);
        userlist.setHasFixedSize(true);
        userlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {

        FirebaseRecyclerAdapter<UsersList, UserviewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersList, UserviewHolder>(
                UsersList.class,
                R.layout.user_banner,
                UserviewHolder.class,
                Muser_database
        ) {
            @Override
            protected void populateViewHolder(final UserviewHolder userviewHolder, final UsersList usersList, int i) {
                final String UID = getRef(i).getKey();
                Muser_database.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(dataSnapshot.hasChild(DataManager.UserProfileImageURI)){
                                        String uri = dataSnapshot.child(DataManager.UserProfileImageURI).getValue().toString();
                                        userviewHolder.setProfileimageset(uri);
                                    }

                                    if(dataSnapshot.hasChild(DataManager.UserName)){
                                        String name = dataSnapshot.child(DataManager.UserName).getValue().toString();
                                        userviewHolder.setUsernameset(name);
                                    }
                                    if(dataSnapshot.hasChild(DataManager.UserNumber)){
                                        String number = dataSnapshot.child(DataManager.UserNumber).getValue().toString();
                                        userviewHolder.setPhonenumberset(number);
                                    }

                                    if(dataSnapshot.hasChild(DataManager.UserEmail)){
                                        emailtext = dataSnapshot.child(DataManager.UserEmail).getValue().toString();
                                    }

                                    if(dataSnapshot.hasChild(DataManager.UserPassword)){
                                        passwordtext = dataSnapshot.child(DataManager.UserPassword).getValue().toString();
                                    }


                                    userviewHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            AuthBottomSheed bottomSheed = new AuthBottomSheed(emailtext, passwordtext);//what you doing now

                                            bottomSheed.show(getActivity().getSupportFragmentManager(), "open");

                                        }
                                    });
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

        userlist.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class UserviewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private View Mview;
        private MaterialTextView username, phonenumber;
        private CircleImageView profileimage;


        public UserviewHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();
            username = Mview.findViewById(R.id.UserNameTextID);
            phonenumber = Mview.findViewById(R.id.UserTypingStatus);
            profileimage = Mview.findViewById(R.id.UserProfileImageID);
        }

        public void setUsernameset(String nam){
            username.setText(nam);
        }
        public void setPhonenumberset(String num){
            phonenumber.setText(num);
        }
        public void setProfileimageset(String img){
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(img).into(profileimage);
                }
            });

        }
    }
}
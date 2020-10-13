package com.rakpak.pak_parak_admin;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.Model.GlobalChatData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GlobalChat extends Fragment {

    private EditText search;
    private RecyclerView chatlist;
    private DatabaseReference MchatDatabase;

    private RelativeLayout backbutton;

    public  GlobalChat() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_global_chat, container, false);

        MchatDatabase = FirebaseDatabase.getInstance().getReference().child(DataManager.GlobalChatRoot);

        search = view.findViewById(R.id.SearchByUserNameID);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputtext = editable.toString();
                if(inputtext.isEmpty()){
                    showmessage();
                }
                else {
                    searching_message(inputtext);
                }
            }
        });

        chatlist = view.findViewById(R.id.ChatList);
        chatlist.setHasFixedSize(true);
        chatlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        backbutton = view.findViewById(R.id.BackButtonss);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        showmessage();

        return view;
    }


    private void searching_message(String name){

        String lowercase = name.toLowerCase();
        Query searchquery = MchatDatabase.orderByChild(DataManager.UserNameSerach).startAt(lowercase).endAt(lowercase + "\uf8ff");

        FirebaseRecyclerAdapter<GlobalChatData, ChatHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GlobalChatData, ChatHolder>(
                GlobalChatData.class,
                R.layout.globalchat_template,
                ChatHolder.class,
                searchquery
        ) {
            @Override
            protected void populateViewHolder(ChatHolder chatHolder, GlobalChatData globalChatData, int i) {

                String UID = getRef(i).getKey();

                chatHolder.messagelayout.setVisibility(View.GONE);
                chatHolder.imagelayout.setVisibility(View.GONE);
                chatHolder.pdflayout.setVisibility(View.GONE);
                chatHolder.Audiolayout.setVisibility(View.GONE);

                MchatDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {



                                if(dataSnapshot.hasChild(DataManager.GlobalChatType)){
                                    String type = dataSnapshot.child(DataManager.GlobalChatType).getValue().toString();
                                    if(type.equals(DataManager.Text)){
                                        chatHolder.messagelayout.setVisibility(View.VISIBLE);
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatMessage)){
                                            String message = dataSnapshot.child(DataManager.GlobalChatMessage).getValue().toString();
                                            chatHolder.setMessageset(message);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setTimeset(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setimageuri(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String date = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setDateset(date);
                                        }
                                    }

                                    if(type.equals(DataManager.Image)){
                                        chatHolder.imagelayout.setVisibility(View.VISIBLE);

                                        if(dataSnapshot.hasChild(DataManager.GlobalChatMessage)){
                                            String uri = dataSnapshot.child(DataManager.GlobalChatMessage).getValue().toString();
                                            chatHolder.setimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setmagetimes(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setprofileimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String date = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setImagedateset(date);
                                        }
                                    }

                                    if(type.equals(DataManager.PDf)){
                                        chatHolder.pdflayout.setVisibility(View.VISIBLE);

                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setPdftimeset(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setPdfprofileimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String dat = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setPdfdateset(dat);
                                        }
                                    }

                                    if(type.equals(DataManager.Audio)){
                                        chatHolder.Audiolayout.setVisibility(View.VISIBLE);

                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setAudioprofileimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setAudiotimeset(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String date = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setAudioDateset(date);
                                        }
                                    }
                                }

                                chatHolder.Mview.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        MaterialAlertDialogBuilder Mbuider = new MaterialAlertDialogBuilder(getActivity());
                                        Mbuider.setTitle("Are you sure?");
                                        Mbuider.setMessage("Make sure you delete message permanently remove data never find data back!");
                                        Mbuider.setPositiveButton("SURE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                MchatDatabase.child(UID).removeValue();
                                            }
                                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                        AlertDialog alertDialog = Mbuider.create();
                                        alertDialog.show();


                                        return true;
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        };

        chatlist.setAdapter(firebaseRecyclerAdapter);

    }


    private void showmessage(){
        FirebaseRecyclerAdapter<GlobalChatData, ChatHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GlobalChatData, ChatHolder>(
                GlobalChatData.class,
                R.layout.globalchat_template,
                ChatHolder.class,
                MchatDatabase
        ) {
            @Override
            protected void populateViewHolder(ChatHolder chatHolder, GlobalChatData globalChatData, int i) {

                String UID = getRef(i).getKey();

                chatHolder.messagelayout.setVisibility(View.GONE);
                chatHolder.imagelayout.setVisibility(View.GONE);
                chatHolder.pdflayout.setVisibility(View.GONE);
                chatHolder.Audiolayout.setVisibility(View.GONE);

                MchatDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {



                                if(dataSnapshot.hasChild(DataManager.GlobalChatType)){
                                    String type = dataSnapshot.child(DataManager.GlobalChatType).getValue().toString();
                                    if(type.equals(DataManager.Text)){
                                        chatHolder.messagelayout.setVisibility(View.VISIBLE);
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatMessage)){
                                            String message = dataSnapshot.child(DataManager.GlobalChatMessage).getValue().toString();
                                            chatHolder.setMessageset(message);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setTimeset(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setimageuri(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String date = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setDateset(date);
                                        }
                                    }

                                    if(type.equals(DataManager.Image)){
                                        chatHolder.imagelayout.setVisibility(View.VISIBLE);

                                        if(dataSnapshot.hasChild(DataManager.GlobalChatMessage)){
                                            String uri = dataSnapshot.child(DataManager.GlobalChatMessage).getValue().toString();
                                            chatHolder.setimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setmagetimes(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setprofileimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String date = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setImagedateset(date);
                                        }
                                    }

                                    if(type.equals(DataManager.PDf)){
                                        chatHolder.pdflayout.setVisibility(View.VISIBLE);

                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setPdftimeset(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setPdfprofileimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String dat = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setPdfdateset(dat);
                                        }
                                    }

                                    if(type.equals(DataManager.Audio)){
                                        chatHolder.Audiolayout.setVisibility(View.VISIBLE);

                                        if(dataSnapshot.hasChild(DataManager.GlobalImageUri)){
                                            String uri = dataSnapshot.child(DataManager.GlobalImageUri).getValue().toString();
                                            chatHolder.setAudioprofileimageset(uri);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatTime)){
                                            String time = dataSnapshot.child(DataManager.GlobalChatTime).getValue().toString();
                                            chatHolder.setAudiotimeset(time);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.GlobalChatDate)){
                                            String date = dataSnapshot.child(DataManager.GlobalChatDate).getValue().toString();
                                            chatHolder.setAudioDateset(date);
                                        }
                                    }
                                }

                                chatHolder.Mview.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        MaterialAlertDialogBuilder Mbuider = new MaterialAlertDialogBuilder(getActivity());
                                        Mbuider.setTitle("Are you sure?");
                                        Mbuider.setMessage("Make sure you delete message permanently remove data never find data back!");
                                        Mbuider.setPositiveButton("SURE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                MchatDatabase.child(UID).removeValue();
                                            }
                                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                        AlertDialog alertDialog = Mbuider.create();
                                        alertDialog.show();


                                        return true;
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        };

        chatlist.setAdapter(firebaseRecyclerAdapter);
    }


    public static class ChatHolder extends RecyclerView.ViewHolder{

        private Context context;
        private View Mview;


        /// todo message data

         private MaterialTextView textmessage, time, date;
         private RelativeLayout messagelayout;
         private CircleImageView profileimage;

        /// todo message data

        /// todo image layout
        private RelativeLayout imagelayout;
        private MaterialTextView imagetime, imagedate;
        private RoundedImageView image;
        private CircleImageView imageprofile;
        /// todo image layout

        /// todo pdf layout
        private RelativeLayout pdflayout;
        private MaterialTextView pdftime, pdfdate;
        private CircleImageView pdfprofileimage;
        /// todo pdf layout

        /// todo audio layout
        private RelativeLayout Audiolayout;
        private MaterialTextView Audiotime, AudioDate;
        private CircleImageView Audioprofileimage;
        /// todo audio layout


        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();

            textmessage = Mview.findViewById(R.id.MessageText);
            messagelayout = Mview.findViewById(R.id.TextMessaeRootBox);
            time = Mview.findViewById(R.id.Time);
            profileimage = Mview.findViewById(R.id.GlobalProfileImage);
            date = Mview.findViewById(R.id.Date);

            /// todo image
            image = Mview.findViewById(R.id.Image);
            imagelayout = Mview.findViewById(R.id.ImageBox);
            imagetime = Mview.findViewById(R.id.ImageTime);
            imageprofile = Mview.findViewById(R.id.ImageProfileImage);
            imagedate = Mview.findViewById(R.id.ImageDate);
            /// todo image

            /// todo pdf layout
            pdflayout = Mview.findViewById(R.id.PdfLayout);
            pdftime = Mview.findViewById(R.id.PdfTime);
            pdfprofileimage = Mview.findViewById(R.id.PdfProfileImage);
            pdfdate = Mview.findViewById(R.id.PdfDate);
            /// todo pdf layout

            /// todo audio layout
            Audiolayout  = Mview.findViewById(R.id.AudioBox);
            Audioprofileimage = Mview.findViewById(R.id.AudioprofileImage);
            Audiotime = Mview.findViewById(R.id.AudioTime);
            AudioDate = Mview.findViewById(R.id.AudioDate);
            /// todo audio layout
        }

        public void setMessageset(String message){
            textmessage.setText(message);
        }

        public void setTimeset(String timedata){
            time.setText(timedata);
        }

        public void setDateset(String dat){
            date.setText(dat);
        }

        public void setimageuri(String uri){
            Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(uri).placeholder(R.drawable.profile_image_back).into(profileimage);

                }
            });
        }

        /// todo image layout
        public void setimageset(String img){
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

        public void setmagetimes(String time){
            imagetime.setText(time);
        }

        public void setImagedateset(String dat){
            imagedate.setText(dat);
        }

        public void setprofileimageset(String img){
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(imageprofile, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(img).into(imageprofile);
                }
            });

        }
        /// todo image layout

        /// todo pdf data
        public void setPdftimeset(String ptime){
            pdftime.setText(ptime);
        }

        public void setPdfdateset(String dat){
            pdfdate.setText(dat);
        }

        public void setPdfprofileimageset(String img){
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(pdfprofileimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(img).into(pdfprofileimage);
                }
            });

        }
        /// todo pdf data

        /// todo audio data
        public void setAudioprofileimageset(String img){

            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(Audioprofileimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(img).into(Audioprofileimage);
                }
            });

        }

        public void setAudiotimeset(String time){
            Audiotime.setText(time);
        }

        public void setAudioDateset(String dat){
            AudioDate.setText(dat);
        }

        /// todo audio data
    }



}
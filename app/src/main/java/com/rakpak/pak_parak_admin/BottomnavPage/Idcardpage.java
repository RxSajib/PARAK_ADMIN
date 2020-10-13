package com.rakpak.pak_parak_admin.BottomnavPage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.Model.UsersList;
import com.rakpak.pak_parak_admin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class Idcardpage extends Fragment {

    private RecyclerView userlist;
    private DatabaseReference MuserDatabase;
    private Typeface typeface, typefacenormal;
    private EditText searchtext;

    public Idcardpage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.idcardpage, container, false);

        searchtext = view.findViewById(R.id.SearchTextID);

        typeface = ResourcesCompat.getFont(getActivity(), R.font.sincures);
        typefacenormal = ResourcesCompat.getFont(getActivity(), R.font.ubuntureguiar);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);
        userlist = view.findViewById(R.id.UserCardListID);
        userlist.setHasFixedSize(true);
        userlist.setLayoutManager(new LinearLayoutManager(getActivity()));



        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();

                if(input.isEmpty()){
                    read_data();
                }
                else {
                  StartRecoding(input);
                }
            }
        });

        read_data();

        return view;



    }

    private void StartRecoding(String newtext){
        String ConvertLowerCase = newtext.toLowerCase();
        Query searchquery = MuserDatabase.orderByChild(DataManager.search).startAt(ConvertLowerCase).endAt(ConvertLowerCase + "\uf8ff");


        FirebaseRecyclerAdapter<UsersList, IdCardHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersList, IdCardHolder>(
                UsersList.class,
                R.layout.user_card_banner,
                IdCardHolder.class,
                searchquery

        ) {
            @Override
            protected void populateViewHolder(final IdCardHolder idCardHolder, UsersList usersList, int i) {
                final String UID = getRef(i).getKey();
                MuserDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(dataSnapshot.hasChild(DataManager.UserName)){
                                        String name = dataSnapshot.child(DataManager.UserName).getValue().toString();
                                        idCardHolder.setUseNameset(name);
                                    }


                                    if(dataSnapshot.hasChild(DataManager.Userstatus)){
                                        String text= dataSnapshot.child(DataManager.Userstatus).getValue().toString();
                                        if(text.equals(DataManager.notyping)){
                                            idCardHolder.setVerifystatusset("Not Verify");
                                            idCardHolder.verifystatus.setTextColor(Color.RED);
                                        }
                                        else {
                                            idCardHolder.setVerifystatusset("Verify");
                                            idCardHolder.verifystatus.setTextColor(Color.GREEN);
                                        }
                                    }

                                    if(dataSnapshot.hasChild(DataManager.ID)){
                                        String _id = dataSnapshot.child(DataManager.ID).getValue().toString();
                                        idCardHolder.setIdnumberset(DataManager.FirstID+_id);
                                    }
                                    if(dataSnapshot.hasChild(DataManager.UserProfileImageURI)){
                                        String uri = dataSnapshot.child(DataManager.UserProfileImageURI).getValue().toString();
                                        idCardHolder.setProfileimageset(uri);
                                    }

                                    idCardHolder.Mview.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View view) {

                                            //    MuserDatabase.child(UID).child("active_status").setValue("Active");

                                            /// todo current date or issue date
                                            Calendar calendar_time = Calendar.getInstance();
                                            SimpleDateFormat simpleDateFormat_time = new SimpleDateFormat("MMM,dd,yyyy");
                                            final String issue_date = simpleDateFormat_time.format(calendar_time.getTime());


                                            /// todo current date or issue date

                                            /// todo after 12 month date exp date
                                            Calendar nextdate = Calendar.getInstance();
                                            SimpleDateFormat simpleDateFormat_nextdate = new SimpleDateFormat("YYY");
                                            String expdate = simpleDateFormat_nextdate.format(calendar_time.getTime());


                                            Calendar nextdates = Calendar.getInstance();
                                            SimpleDateFormat simpleDateFormat_nextdates = new SimpleDateFormat("MMM,dd");
                                            final String expdates = simpleDateFormat_nextdates.format(nextdates.getTime());


                                            int yeartoconvert_int = Integer.parseInt(expdate);
                                            final int addyear = yeartoconvert_int + 1;


                                            /// todo after 12 month date exp date

                                            final MaterialAlertDialogBuilder FontBuilder = new MaterialAlertDialogBuilder(getActivity());
                                            View fontview = LayoutInflater.from(getContext()).inflate(R.layout.sampel_view_of_card, null, false);

                                            final String[] font = new String[1];
                                            Button cancelbutton = fontview.findViewById(R.id.CancelFontButtonID);
                                            Button updatebutton = fontview.findViewById(R.id.UpdateButtonID);
                                            final EditText inputstatus = fontview.findViewById(R.id.StatusInputID);
                                            final MaterialTextView fontselected = fontview.findViewById(R.id.FontsSelected);

                                            ListView fontlist = fontview.findViewById(R.id.FontListID);
                                            String arrayof_fonts[] = {"corline", "Art Maria", "CattilyDemoRegular"};


                                            FontBuilder.setView(fontview);
                                            final AlertDialog alertDialog = FontBuilder.create();

                                            alertDialog.show();

                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fonts_layut, R.id.Fontname, arrayof_fonts);
                                            fontlist.setAdapter(adapter);

                                            fontlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    String iteams = adapterView.getItemAtPosition(i).toString();

                                                    fontselected.setText(iteams);
                                                    MuserDatabase.child(UID).child("font").setValue(iteams);
                                                }
                                            });




                                            updatebutton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String status_text = inputstatus.getText().toString();


                                                    if (status_text.isEmpty()) {
                                                        Toast.makeText(getActivity(), "status is empty", Toast.LENGTH_LONG).show();
                                                    }
                                                    MuserDatabase.child(UID).child("active").setValue("active_card");
                                                    MuserDatabase.child(UID).child("status").setValue(status_text);
                                                    MuserDatabase.child(UID).child("acive_date").setValue(issue_date);
                                                    MuserDatabase.child(UID).child("exp_date").setValue(expdates+","+Integer.toString(addyear));

                                                    alertDialog.dismiss();

                                                }
                                            });

                                            cancelbutton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    alertDialog.dismiss();
                                                }
                                            });


                                            return true;
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

    }


    private void read_data(){
        FirebaseRecyclerAdapter<UsersList, IdCardHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersList, IdCardHolder>(
                UsersList.class,
                R.layout.user_card_banner,
                IdCardHolder.class,
                MuserDatabase

        ) {
            @Override
            protected void populateViewHolder(final IdCardHolder idCardHolder, UsersList usersList, int i) {
                final String UID = getRef(i).getKey();
                MuserDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){


                                    if(dataSnapshot.hasChild(DataManager.UserName)){
                                        String name = dataSnapshot.child(DataManager.UserName).getValue().toString();
                                        idCardHolder.setUseNameset(name);
                                    }

                                    if(dataSnapshot.hasChild(DataManager.ID)){
                                        String _id = dataSnapshot.child(DataManager.ID).getValue().toString();
                                        idCardHolder.setIdnumberset(DataManager.FirstID+_id);
                                    }
                                    if(dataSnapshot.hasChild(DataManager.UserProfileImageURI)){
                                        String uri = dataSnapshot.child(DataManager.UserProfileImageURI).getValue().toString();
                                        idCardHolder.setProfileimageset(uri);
                                    }

                                    if(dataSnapshot.hasChild(DataManager.Userstatus)){
                                        String text= dataSnapshot.child(DataManager.Userstatus).getValue().toString();
                                        if(text.equals(DataManager.notyping)){
                                            idCardHolder.setVerifystatusset("Not Verify");
                                            idCardHolder.verifystatus.setTextColor(Color.RED);
                                        }
                                        else {
                                            idCardHolder.setVerifystatusset("Verify");
                                            idCardHolder.verifystatus.setTextColor(Color.GREEN);
                                        }
                                    }



                                    idCardHolder.Mview.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View view) {

                                            //    MuserDatabase.child(UID).child("active_status").setValue("Active");

                                            /// todo current date or issue date
                                            Calendar calendar_time = Calendar.getInstance();
                                            SimpleDateFormat simpleDateFormat_time = new SimpleDateFormat("MMM,dd,yyyy");
                                            final String issue_date = simpleDateFormat_time.format(calendar_time.getTime());


                                            /// todo current date or issue date

                                            /// todo after 12 month date exp date
                                            Calendar nextdate = Calendar.getInstance();
                                            SimpleDateFormat simpleDateFormat_nextdate = new SimpleDateFormat("YYY");
                                            String expdate = simpleDateFormat_nextdate.format(calendar_time.getTime());


                                            Calendar nextdates = Calendar.getInstance();
                                            SimpleDateFormat simpleDateFormat_nextdates = new SimpleDateFormat("MMM,dd");
                                            final String expdates = simpleDateFormat_nextdates.format(nextdates.getTime());


                                            int yeartoconvert_int = Integer.parseInt(expdate);
                                            final int addyear = yeartoconvert_int + 1;


                                            /// todo after 12 month date exp date

                                            final MaterialAlertDialogBuilder FontBuilder = new MaterialAlertDialogBuilder(getActivity());
                                            View fontview = LayoutInflater.from(getContext()).inflate(R.layout.sampel_view_of_card, null, false);

                                            final String[] font = new String[1];
                                            Button cancelbutton = fontview.findViewById(R.id.CancelFontButtonID);
                                            Button updatebutton = fontview.findViewById(R.id.UpdateButtonID);
                                            final EditText inputstatus = fontview.findViewById(R.id.StatusInputID);
                                            final MaterialTextView fontselected = fontview.findViewById(R.id.FontsSelected);

                                            ListView fontlist = fontview.findViewById(R.id.FontListID);
                                            String arrayof_fonts[] = {"corline", "Art Maria", "CattilyDemoRegular"};


                                            FontBuilder.setView(fontview);
                                            final AlertDialog alertDialog = FontBuilder.create();

                                            alertDialog.show();

                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fonts_layut, R.id.Fontname, arrayof_fonts);
                                            fontlist.setAdapter(adapter);

                                            fontlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    String iteams = adapterView.getItemAtPosition(i).toString();

                                                    fontselected.setText(iteams);
                                                    MuserDatabase.child(UID).child("font").setValue(iteams);
                                                }
                                            });




                                            updatebutton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String status_text = inputstatus.getText().toString();


                                                    if (status_text.isEmpty()) {
                                                        Toast.makeText(getActivity(), "status is empty", Toast.LENGTH_LONG).show();
                                                    }
                                                    MuserDatabase.child(UID).child("active").setValue("active_card");
                                                    MuserDatabase.child(UID).child("status").setValue(status_text);
                                                    MuserDatabase.child(UID).child("acive_date").setValue(issue_date);
                                                    MuserDatabase.child(UID).child("exp_date").setValue(expdates+","+Integer.toString(addyear));

                                                    alertDialog.dismiss();

                                                }
                                            });

                                            cancelbutton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    alertDialog.dismiss();
                                                }
                                            });


                                            return true;
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
    }





    public static class IdCardHolder extends RecyclerView.ViewHolder {

        private Context context;
        private View Mview;
        private MaterialTextView fullname,  idnumber;
        private CircleImageView profileimage;
        private MaterialTextView verifystatus;

        public IdCardHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            verifystatus = Mview.findViewById(R.id.PositionID);
            context = Mview.getContext();
            idnumber = Mview.findViewById(R.id.UserID);
            fullname = Mview.findViewById(R.id.Fullnames);
            profileimage = Mview.findViewById(R.id.ProfileimageID);

        }



        public void setUseNameset(String nam) {
            fullname.setText(nam);
        }

        public void setIdnumberset(String id_s) {
            idnumber.setText(id_s);
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

        public void setVerifystatusset(String status){
            verifystatus.setText(status);
        }
    }
}
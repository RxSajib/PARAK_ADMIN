package com.rakpak.pak_parak_admin.Navagation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.R;


public class Help extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference MRoot;
    private RelativeLayout backbutton;


    public Help() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.help, container, false);

        MRoot = FirebaseDatabase.getInstance().getReference().child(DataManager.HelpRoot);
        MRoot.keepSynced(true);
        recyclerView = view.findViewById(R.id.HelpList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        backbutton = view.findViewById(R.id.BackButtonS);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return view;
    }

    @Override
    public void onStart() {


        Query firebasequery = MRoot.orderByChild(DataManager.Short);


        FirebaseRecyclerAdapter<com.rakpak.pak_parak_admin.Model.Help, HelpHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<com.rakpak.pak_parak_admin.Model.Help, HelpHolder>(
                com.rakpak.pak_parak_admin.Model.Help.class,
                R.layout.help_banner,
                HelpHolder.class,
                firebasequery
        ) {
            @Override
            protected void populateViewHolder(HelpHolder helpHolder, com.rakpak.pak_parak_admin.Model.Help help, int i) {

                String UID = getRef(i).getKey();
                MRoot.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(dataSnapshot.hasChild(DataManager.Help_Name)){
                                        String name = dataSnapshot.child(DataManager.Help_Name).getValue().toString();
                                        helpHolder.setnameset(name);
                                    }
                                    if(dataSnapshot.hasChild(DataManager.Help_Mobile)){
                                        String number = dataSnapshot.child(DataManager.Help_Mobile).getValue().toString();
                                        helpHolder.setPhoneset(number);
                                    }
                                    if(dataSnapshot.hasChild(DataManager.HelpDetails)){
                                        String details = dataSnapshot.child(DataManager.HelpDetails).getValue().toString();
                                        helpHolder.setDetailsset(details);
                                    }

                                    helpHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {



                                            goto_helppage(new HelpFull_details(), UID);
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

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class HelpHolder extends RecyclerView.ViewHolder{

        private Context context;
        private View Mview;
        private MaterialTextView name, phone, details;

        public HelpHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();
            name = Mview.findViewById(R.id.HelpName);
            phone = Mview.findViewById(R.id.HelpPhoneNumber);
            details = Mview.findViewById(R.id.HelpDetails);
        }

        public void setnameset(String nam){
            name.setText(nam);
        }
        public void setPhoneset(String phn){
            phone.setText(phn);
        }
        public void setDetailsset(String det){
            details.setText(det);
        }
    }

    private void goto_helppage(Fragment fragment, String UID){
        if(fragment != null){

            Bundle bundle = new Bundle();
            bundle.putString(DataManager.HelpUID, UID);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);

            transaction.replace(R.id.MainFream, fragment).addToBackStack(null);
            transaction.commit();
        }
    }
}
package com.rakpak.pak_parak_admin.Navagation;

import android.content.Intent;
import android.content.SyncRequest;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.R;

public class HelpFull_details extends Fragment {

    private Bundle bundle;
    private String data;
    private MaterialTextView toolbarname;
    private DatabaseReference RootRef;

    /// todo all widget
    private MaterialTextView name, mobile, idno, familymembers, heveyoureceivemostaquehelp, sourceofincome, locationtext, montlyincome, howwecanhelpyou;

    private CheckBox checkBox;
    private MaterialButton pdfdownloadbutton;
    private RelativeLayout backbutton;
    /// todo all widget


    public HelpFull_details() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_full_details, container, false);


        backbutton = view.findViewById(R.id.BackID);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        checkBox = view.findViewById(R.id.CheakcBox);
        pdfdownloadbutton = view.findViewById(R.id.DownloadButton);


        RootRef = FirebaseDatabase.getInstance().getReference().child(DataManager.HelpRoot);
        RootRef.keepSynced(true);
        bundle = getArguments();
        data = bundle.getString(DataManager.HelpUID);

        toolbarname = view.findViewById(R.id.ToolbarName);

        name = view.findViewById(R.id.Name);
        mobile = view.findViewById(R.id.Mobile);
        idno = view.findViewById(R.id.IDNOS);
        familymembers = view.findViewById(R.id.FamilymembersID);
        heveyoureceivemostaquehelp = view.findViewById(R.id.BeforeYougetHelp);
        sourceofincome = view.findViewById(R.id.SourceOFIncome);
        locationtext = view.findViewById(R.id.Location);
        montlyincome = view.findViewById(R.id.MontlyIncome);
        howwecanhelpyou = view.findViewById(R.id.HelpDetailss);

        RootRef.child(data)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild(DataManager.Help_Name)){
                                String name = dataSnapshot.child(DataManager.Help_Name).getValue().toString();
                                toolbarname.setText(name);
                            }
                            if(dataSnapshot.hasChild(DataManager.Help_Mobile)){
                                String number = dataSnapshot.child(DataManager.Help_Mobile).getValue().toString();
                                mobile.setText(number);
                            }
                            if(dataSnapshot.hasChild(DataManager.Help_ID)){
                                String _id = dataSnapshot.child(DataManager.Help_ID).getValue().toString();
                                idno.setText(_id);
                            }
                            if(dataSnapshot.hasChild(DataManager.Help_FamilyMembers)){
                                String numberof_family = dataSnapshot.child(DataManager.Help_FamilyMembers).getValue().toString();
                                familymembers.setText(numberof_family);
                            }
                            if(dataSnapshot.hasChild(DataManager.Help_Location)){
                                String location = dataSnapshot.child(DataManager.Help_Location).getValue().toString();
                                locationtext.setText(location);
                            }
                            if(dataSnapshot.hasChild(DataManager.Help_MontlyIncome)){
                                String montlyincometext = dataSnapshot.child(DataManager.Help_MontlyIncome).getValue().toString();
                                montlyincome.setText(montlyincometext);
                            }
                            if(dataSnapshot.hasChild(DataManager.HelpDetails)){
                                String details = dataSnapshot.child(DataManager.HelpDetails).getValue().toString();
                                howwecanhelpyou.setText(details);
                            }
                            if(dataSnapshot.hasChild(DataManager.WhyNeedHelp)){
                                String income = dataSnapshot.child(DataManager.WhyNeedHelp).getValue().toString();
                                heveyoureceivemostaquehelp.setText(income);
                            }
                            if(dataSnapshot.hasChild(DataManager.Help_SourceOfIncome)){
                                String incometext = dataSnapshot.child(DataManager.Help_SourceOfIncome).getValue().toString();
                                sourceofincome.setText(incometext);
                            }

                            if(dataSnapshot.hasChild(DataManager.HelpPdf)){
                                String uri = dataSnapshot.child(DataManager.HelpPdf).getValue().toString();
                                if(!uri.isEmpty()){
                                    checkBox.setVisibility(View.VISIBLE);
                                    pdfdownloadbutton.setVisibility(View.VISIBLE);
                                    checkBox.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(checkBox.isChecked()){
                                                pdfdownloadbutton.isEnabled();

                                               pdfdownloadbutton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                                       startActivity(intent);
                                                   }
                                               });

                                            }
                                            else {
                                                pdfdownloadbutton.isEnabled();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }
}
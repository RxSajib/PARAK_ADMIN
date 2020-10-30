package com.rakpak.pak_parak_admin.Navagation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.rakpak.pak_parak_admin.BottomSheedDioloag.AddTeamBottomSheed;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.Model.TeamModal;
import com.rakpak.pak_parak_admin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class OurTeam extends Fragment {

    private RelativeLayout back_button;
    private FloatingActionButton add_team;
    private RecyclerView teamlist;
    private DatabaseReference MTeamDatabase;
    private static final int SPANCOUNT = 2;
    private String name, designation;
    private LinearLayout show_team;
    private String teaname, deg, uri;
    private AnstronCoreHelper anstronCoreHelper;
    private int PERMISSIONCODE = 1;
    private static final int IMAGEREQUESTCODE = 10;
    private StorageReference MimageStores;
    private ProgressDialog progressDialog;
    private String ImageDownloadUri;

    public OurTeam() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.our_team, container, false);

        progressDialog = new ProgressDialog(getActivity());

        MimageStores = FirebaseStorage.getInstance().getReference().child("TeamImage");
        anstronCoreHelper = new AnstronCoreHelper(getActivity());

        show_team = view.findViewById(R.id.ShowTeamView);
        MTeamDatabase = FirebaseDatabase.getInstance().getReference().child(DataManager.TeamDatabaseRoot);
        MTeamDatabase.keepSynced(true);
        add_team = view.findViewById(R.id.AddTeamID);
        add_team.setColorFilter(Color.WHITE);
        teamlist = view.findViewById(R.id.TeamListID);
        teamlist.setHasFixedSize(true);


        back_button = view.findViewById(R.id.BackButtonID);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });



        add_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddTeamBottomSheed addTeamBottomSheed = new AddTeamBottomSheed();
                addTeamBottomSheed.show(getActivity().getSupportFragmentManager(), "open");
            }
        });

        return  view;
    }

    @Override
    public void onStart() {

        FirebaseRecyclerAdapter<TeamModal, TeamHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TeamModal, TeamHolder>(
                TeamModal.class,
                R.layout.team_iteams,
                TeamHolder.class,
                MTeamDatabase
        ) {
            @Override
            protected void populateViewHolder(final TeamHolder teamHolder, TeamModal teamModal, int i) {
                final String uid = getRef(i).getKey();
                MTeamDatabase.child(uid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(dataSnapshot.exists()){

                                        show_team.setVisibility(View.GONE);

                                        if(dataSnapshot.hasChild(DataManager.TeamName)){
                                             name = dataSnapshot.child(DataManager.TeamName).getValue().toString();
                                            teamHolder.setNameset(name);
                                        }
                                        if(dataSnapshot.hasChild(DataManager.TeamDesignation)){
                                             designation = dataSnapshot.child(DataManager.TeamDesignation).getValue().toString();
                                            teamHolder.setDesginactionset(designation);
                                        }

                                        if(dataSnapshot.hasChild(DataManager.TeamURI)){
                                            String uri = dataSnapshot.child(DataManager.TeamURI).getValue().toString();
                                            teamHolder.setProfleimageset(uri);
                                        }

                                        teamHolder.Mview.setOnLongClickListener(new View.OnLongClickListener() {

                                            @Override
                                            public boolean onLongClick(View view) {

                                                MaterialAlertDialogBuilder Mbuilder = new MaterialAlertDialogBuilder(getActivity());

                                                View Mview = LayoutInflater.from(getActivity()).inflate(R.layout.tam_controlling, null, false);

                                                final EditText nametext, deginationtext;
                                                final MaterialButton update, delete, copy;

                                                CircleImageView profileimage = Mview.findViewById(R.id.ImageViewID);
                                                copy = Mview.findViewById(R.id.CopyButtonID);

                                                profileimage.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if(imagepermission()){
                                                            Intent intent = new Intent(Intent.ACTION_PICK);
                                                            intent.setType("image/*");
                                                            startActivityForResult(intent, IMAGEREQUESTCODE);
                                                        }

                                                    }
                                                });

                                                nametext = Mview.findViewById(R.id.TName);
                                                deginationtext = Mview.findViewById(R.id.Tdes);

                                                update = Mview.findViewById(R.id.TUpdate);
                                                delete = Mview.findViewById(R.id.Delete);

                                                Mbuilder.setView(Mview);

                                                final AlertDialog alertDialog = Mbuilder.create();
                                                alertDialog.show();


                                                MTeamDatabase.child(uid)
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.exists()){
                                                                    if(dataSnapshot.exists()){
                                                                        if(dataSnapshot.hasChild(DataManager.TeamName)){
                                                                            nametext.setText(dataSnapshot.child(DataManager.TeamName).getValue().toString());
                                                                        }
                                                                        if(dataSnapshot.hasChild(DataManager.TeamDesignation)){
                                                                            deginationtext.setText(dataSnapshot.child(DataManager.TeamDesignation).getValue().toString());
                                                                        }


                                                                        update.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                String namestring = nametext.getText().toString().trim();
                                                                                String deginactionstring = deginationtext.getText().toString().trim();

                                                                                if(namestring.isEmpty()){
                                                                                    Toast.makeText(getActivity(), "name require", Toast.LENGTH_LONG).show();
                                                                                }
                                                                                else if(deginactionstring.isEmpty()){
                                                                                    Toast.makeText(getActivity(), "designation require", Toast.LENGTH_LONG).show();

                                                                                }
                                                                                else {
                                                                                    Map<String, Object> usermap = new HashMap<String, Object>();
                                                                                    usermap.put(DataManager.TeamDesignation, deginactionstring);
                                                                                    usermap.put(DataManager.TeamName, namestring);
                                                                                    if(!ImageDownloadUri.isEmpty()){
                                                                                        usermap.put(DataManager.TeamURI, ImageDownloadUri);
                                                                                    }

                                                                                    MTeamDatabase.child(uid).updateChildren(usermap)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(dataSnapshot.exists()){
                                                                                                        Toast.makeText(getActivity(), "data is update success", Toast.LENGTH_LONG).show();
                                                                                                        alertDialog.dismiss();
                                                                                                    }
                                                                                                    else {
                                                                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                                                        alertDialog.dismiss();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }
                                                                        });

                                                                        delete.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                MTeamDatabase.child(uid).removeValue();
                                                                                alertDialog.dismiss();
                                                                            }
                                                                        });

                                                                        copy.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {

                                                                                if(dataSnapshot.hasChild(DataManager.TeamName)){
                                                                                     teaname = dataSnapshot.child(DataManager.TeamName).getValue().toString();
                                                                                }
                                                                                if(dataSnapshot.hasChild(DataManager.TeamDesignation)){
                                                                                     deg = dataSnapshot.child(DataManager.TeamDesignation).getValue().toString();
                                                                                }
                                                                                if(dataSnapshot.hasChild(DataManager.TeamURI)){
                                                                                     uri = dataSnapshot.child(DataManager.TeamURI).getValue().toString();
                                                                                }


                                                                                Map<String, Object> copymap = new HashMap<String, Object>();
                                                                                copymap.put(DataManager.TeamName, teaname);
                                                                                copymap.put(DataManager.TeamDesignation, deg);
                                                                                copymap.put(DataManager.TeamURI, uri);

                                                                                MTeamDatabase.push().updateChildren(copymap)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful()){

                                                                                                }
                                                                                                else {
                                                                                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                                return true;
                                            }


                                        });
                                    }
                                }
                                else {
                                    show_team.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        GridLayoutManager manager = new GridLayoutManager(getActivity(), SPANCOUNT);
        teamlist.setLayoutManager(manager);
        teamlist.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class TeamHolder extends RecyclerView.ViewHolder{

        private View Mview;
        private Context context;
        private CircleImageView profleimage;
        private MaterialTextView name, desginaction;

        public TeamHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();
            profleimage = Mview.findViewById(R.id.TeamImage);
            name = Mview.findViewById(R.id.Teamname);
            desginaction = Mview.findViewById(R.id.DesgnationID);
        }

        public void setProfleimageset(String img){
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(profleimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(img).into(profleimage);
                }
            });

        }
        public void setNameset(String nam){
            name.setText(nam);
        }
        public void setDesginactionset(String des){
            desginaction.setText(des);
        }
    }

    private boolean imagepermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONCODE);
            return false;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEREQUESTCODE && resultCode == RESULT_OK) {

            progressDialog.setMessage("wait for a moment your image is uploading");
            progressDialog.setTitle("Please wait ...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Uri imageuei = data.getData();
            if(imageuei != null){
                final File file = new File(SiliCompressor.with(getActivity())
                        .compress(FileUtils.getPath(getActivity(), imageuei), new File(getActivity().getCacheDir(), "temp")));

                Uri fromfile = Uri.fromFile(file);

                MimageStores.child(anstronCoreHelper.getFileNameFromUri(fromfile))
                        .putFile(fromfile)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Image is upload success", Toast.LENGTH_LONG).show();

                                    ImageDownloadUri = task.getResult().getDownloadUrl().toString();

                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
        }
    }
}
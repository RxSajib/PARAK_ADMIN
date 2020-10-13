package com.rakpak.pak_parak_admin.BottomSheedDioloag;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.rakpak.pak_parak_admin.DataManager.DataManager;
import com.rakpak.pak_parak_admin.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;


public class AddTeamBottomSheed extends BottomSheetDialogFragment {

    private CircleImageView profileimage;
    private static final int IMAGEREQUESTCODE = 100;
    private Uri imageuri;
    private Button cancelbutton, updatebutton;
    private EditText name,designation ;
    private StorageReference ImageStorRef;
    private ProgressBar imageuploadingprogress;
    private String Imagedownloaduri;
    private String CurrentTime, CurrentDate;
    private DatabaseReference MTeamDatabaseRoot;
    private final int PERMISSIONCODE = 100;
    private AnstronCoreHelper anstronCoreHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_team_layout, null, false);

        MTeamDatabaseRoot = FirebaseDatabase.getInstance().getReference().child(DataManager.TeamDatabaseRoot);
        ImageStorRef = FirebaseStorage.getInstance().getReference().child(DataManager.TeamImageRoot);

        anstronCoreHelper = new AnstronCoreHelper(getActivity());

        imageuploadingprogress = view.findViewById(R.id.ImageUploadingProgress);
        profileimage = view.findViewById(R.id.PickImage);
        cancelbutton = view.findViewById(R.id.CancelButtonID);
        updatebutton = view.findViewById(R.id.UpdateButtonID);
        name = view.findViewById(R.id.PickName);
        designation = view.findViewById(R.id.PickDesignation);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imagepermission()){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGEREQUESTCODE);
                }
                else {

                }

            }
        });

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nametext = name.getText().toString().trim();
                final String desgnationtext = designation.getText().toString().trim();

                if(nametext.isEmpty()){
                    name.setError("Name require");
                }
                else if(desgnationtext.isEmpty()){
                    designation.setError("Designation require");
                }
                else {

                    Calendar calendar_time = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat_time = new SimpleDateFormat("hh:mm a");
                    CurrentTime = simpleDateFormat_time.format(calendar_time.getTime());

                    Calendar calendar_date = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat_date = new SimpleDateFormat("yyyy-MM-dd");
                    CurrentDate = simpleDateFormat_date.format(calendar_date.getTime());

                    Map<String, Object> team_map =  new HashMap<>();
                    team_map.put(DataManager.TeamName, nametext);
                    team_map.put(DataManager.TeamDesignation, desgnationtext);
                    team_map.put(DataManager.TeamURI, Imagedownloaduri);
                    team_map.put(DataManager.TemaTime, CurrentTime);
                    team_map.put(DataManager.TeamDate, CurrentDate);


                    MTeamDatabaseRoot.push().updateChildren(team_map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        updatebutton.setEnabled(false);
                                        dismiss();
                                    }
                                    else {
                                        updatebutton.setEnabled(false);
                                        dismiss();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    updatebutton.setVisibility(View.INVISIBLE);
                                    dismiss();
                                }
                            });

                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEREQUESTCODE && resultCode == RESULT_OK) {
            imageuploadingprogress.setVisibility(View.VISIBLE);
            imageuri = data.getData();

            if(imageuri != null){
                profileimage.setImageURI(imageuri);


                final File file = new File(SiliCompressor.with(getActivity())
                .compress(FileUtils.getPath(getActivity(), imageuri), new File(getActivity().getCacheDir(), "temp")));

                Uri fromfile = Uri.fromFile(file);

                ImageStorRef.child(anstronCoreHelper.getFileNameFromUri(fromfile))
                        .putFile(fromfile)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    imageuploadingprogress.setVisibility(View.GONE);
                                    Imagedownloaduri = task.getResult().getDownloadUrl().toString();
                                }else {
                                    imageuploadingprogress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                imageuploadingprogress.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }





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

}

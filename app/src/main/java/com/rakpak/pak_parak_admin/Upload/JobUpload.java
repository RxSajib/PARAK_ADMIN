package com.rakpak.pak_parak_admin.Upload;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import static android.app.Activity.RESULT_OK;

public class JobUpload extends Fragment {

    private ImageView imageView;
    private EditText message;
    private RelativeLayout submitbutton;
    private static final int IMAGECODE = 01;
    private String DownloadImageUri;
    private StorageReference NewsStores;
    private ProgressDialog Mprogress;
    private String CurrentTime, CurrentDate;
    private DatabaseReference MnewsDatabase;
    private int count = 0;
    private int negativecount;
    private RelativeLayout backarraw;
    private static final int PERMISSIONCODE = 100;
    private AnstronCoreHelper anstronCoreHelper;
    private EditText weburl;


    public JobUpload() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.job_upload, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        MnewsDatabase = FirebaseDatabase.getInstance().getReference().child(DataManager.NewsRoot);

        weburl = view.findViewById(R.id.WebsiteUrl);

        backarraw = view.findViewById(R.id.backArrow);
        backarraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


        MnewsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    count = (int) dataSnapshot.getChildrenCount();
                    negativecount = (~(count - 1));
                } else {
                    negativecount = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        imageView = view.findViewById(R.id.PostImageID);
        message = view.findViewById(R.id.MessageTextUD);
        submitbutton = view.findViewById(R.id.SubMitButtonID);

        Mprogress = new ProgressDialog(getActivity());

        NewsStores = FirebaseStorage.getInstance().getReference().child(DataManager.NewsImageRoot);
        anstronCoreHelper = new AnstronCoreHelper(getActivity());
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messagetext = message.getText().toString().trim();
                String url = weburl.getText().toString().trim();

                if (messagetext.isEmpty()) {
                    Toast.makeText(getActivity(), "please input your message", Toast.LENGTH_SHORT).show();
                } else {

                    Mprogress.setTitle("Wait for a moment");
                    Mprogress.setMessage("Please wait your news is uploading");
                    Mprogress.setCanceledOnTouchOutside(false);
                    Mprogress.show();

                    Calendar calendartime = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat_time = new SimpleDateFormat(DataManager.TimePattern);
                    CurrentTime = simpleDateFormat_time.format(calendartime.getTime());

                    Calendar calendar_date = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat_date = new SimpleDateFormat(DataManager.DatePattern);
                    CurrentDate = simpleDateFormat_date.format(calendar_date.getTime());


                    Map<String, Object> usermap = new HashMap<String, Object>();
                    usermap.put(DataManager.JobImage, DownloadImageUri);
                    usermap.put(DataManager.JobMessage, messagetext);
                    usermap.put(DataManager.JobTime, CurrentTime);
                    usermap.put(DataManager.JobDate, CurrentDate);
                    usermap.put(DataManager.JobShort, negativecount);
                    usermap.put(DataManager.NewsVisiable, DataManager.Visiable);
                    usermap.put(DataManager.JobUrl, url);

                    MnewsDatabase.push().updateChildren(usermap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Mprogress.dismiss();
                                        Toast.makeText(getActivity(), "news is updated", Toast.LENGTH_SHORT).show();

                                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imagepermission()) {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGECODE);
                } else {

                }


            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGECODE && resultCode == RESULT_OK) {

            Mprogress.setTitle("Wait for a moment");
            Mprogress.setMessage("Please wait your image is uploading");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Uri imageuri = data.getData();

            if (imageuri != null) {
                imageView.setImageURI(imageuri);

                final File file = new File(SiliCompressor.with(getActivity())
                        .compress(FileUtils.getPath(getActivity(), imageuri), new File(getActivity().getCacheDir(), "temp")));

                Uri fromfile = Uri.fromFile(file);


                NewsStores.child(anstronCoreHelper.getFileNameFromUri(fromfile))
                        .putFile(fromfile)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Mprogress.dismiss();

                                    DownloadImageUri = task.getResult().getDownloadUrl().toString();
                                    Toast.makeText(getActivity(), "Upload success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Mprogress.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Mprogress.dismiss();
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        }
    }

    private boolean imagepermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONCODE);
            return false;
        }
    }
}
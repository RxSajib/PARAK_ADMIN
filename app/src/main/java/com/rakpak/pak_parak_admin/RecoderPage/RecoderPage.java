package com.rakpak.pak_parak_admin.RecoderPage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rakpak.pak_parak_admin.R;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class RecoderPage extends Fragment {

    private MaterialButton recodebutton;
    private MaterialTextView recode;
    private MediaRecorder mediaRecorder;
    private String Mfilename = null;
    private String recodfile;
    private int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private String permission[] = {RECORD_AUDIO, WRITE_EXTERNAL_STORAGE};
    private StorageReference audio;

    public RecoderPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recoder_page, container, false);

        audio = FirebaseStorage.getInstance().getReference().child("Audio");


        recode = view.findViewById(R.id.Textview);
        recodebutton = view.findViewById(R.id.RecodeButton);
        recodebutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if(checkpermission()){
                        startrecoding();
                    }

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    stoprecoding();

                }
                return true;
            }
        });

        return view;
    }


    private void startrecoding(){

        String recodpath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        recodfile = "filename.3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recodpath+"/"+recodfile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            Toast.makeText(getActivity(), "start recoding", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }


    private void stoprecoding(){
        mediaRecorder.stop();
        mediaRecorder.release();

        saveing_data_firebase(recodfile);

        mediaRecorder = null;
    }

    private void saveing_data_firebase(String recodfile) {

        Uri uri = Uri.fromFile(new File(recodfile));

        Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_LONG).show();


        StorageReference filepath = audio.child(uri.getLastPathSegment());
        filepath.putFile(uri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "done", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    private boolean checkpermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }
        else {

            ActivityCompat.requestPermissions(getActivity(), permission, REQUEST_AUDIO_PERMISSION_CODE);
            return false;
        }
    }

}
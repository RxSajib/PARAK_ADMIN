package com.rakpak.pak_parak_admin.SampleUploadingPage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.rakpak.pak_parak_admin.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;



import static android.app.Activity.RESULT_OK;


public class SampleUploadFile extends Fragment {

    private Button uploadbutton;
    private StorageReference Msample_data;
    private Uri pickedimageuri;
    private Uri iamgeuri;
    private ProgressDialog Mprogress;
    private Bitmap tham_bitmap;

    private StorageReference thamstores;

    private ByteArrayOutputStream byteArrayOutputStream;

    public SampleUploadFile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sampleupload_file, container, false);

        thamstores = FirebaseStorage.getInstance().getReference().child("ThamImage");
        Mprogress = new ProgressDialog(getActivity());
        Msample_data = FirebaseStorage.getInstance().getReference().child("sampleimage");
        uploadbutton = view.findViewById(R.id.UploadImage);
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 511);
            }
        });

        return view;
    }

   /* private void uploadimage_beforecrospression(){
        if(pickedimageuri != null){
            File file = new File(SiliCompressor.with(getContext()).compress(FileUtils.getPath(getContext(), pickedimageuri), new File(getActivity().getCacheDir(), "temp")));
            Uri uri = Uri.fromFile(file);
            Msample_data.child("/image").child(coreHelper.getFileNameFromUri(uri)).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Mprogress.setMessage("plea se wait file is uploading ...");
        Mprogress.show();





    }
}
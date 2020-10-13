package com.rakpak.pak_parak_admin.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rakpak.pak_parak_admin.Homepage.Homepage;
import com.rakpak.pak_parak_admin.R;

public class LoginPage extends Fragment {

    private EditText email, password;
    private Button login;
    private ProgressDialog Mprogress;
    private FirebaseAuth Mauth;

    public LoginPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_page, container, false);


        Mauth = FirebaseAuth.getInstance();
        Mprogress = new ProgressDialog(getActivity());

        email = view.findViewById(R.id.EmailID);
        password = view.findViewById(R.id.PasswordID);

        login = view.findViewById(R.id.LoginID);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailtext = email.getText().toString();
                String passwrdtext = password.getText().toString();


                if(emailtext.isEmpty()){
                    email.setError("Email require");
                }
                else if(passwrdtext.isEmpty()){
                    password.setError("Password require");
                }
                else {
                    Mprogress.setTitle("Wait for a moment ...");
                    Mprogress.setMessage("Please wait we are reading email and password");
                    Mprogress.setCanceledOnTouchOutside(false);
                    Mprogress.show();

                    Mauth.signInWithEmailAndPassword(emailtext, passwrdtext)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Mprogress.dismiss();
                                        goto_homepage(new Homepage());
                                    }
                                    else {
                                        Mprogress.dismiss();
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        });




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser Muser = Mauth.getCurrentUser();
        if(Muser != null){
            goto_homepage(new Homepage());
        }

    }

    private void goto_homepage(Fragment fragment){
        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFream, fragment);
            transaction.commit();
        }
    }
}
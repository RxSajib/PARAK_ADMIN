package com.rakpak.pak_parak_admin.BottomSheedDioloag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textview.MaterialTextView;
import com.rakpak.pak_parak_admin.R;

public class AuthBottomSheed extends BottomSheetDialogFragment {

    private MaterialTextView email, password;

    private String emailtext, passwordtext;
    private Context context;

    public AuthBottomSheed(String emailtext, String passwordtext) {
        this.emailtext = emailtext;
        this.passwordtext = passwordtext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View Mview = inflater.inflate(R.layout.auth_bottom_sheed, null, false);

        email = Mview.findViewById(R.id.EmailAddressID);
        password = Mview.findViewById(R.id.Password);

        email.setText(emailtext);
        password.setText(passwordtext);
        return Mview;
    }
}

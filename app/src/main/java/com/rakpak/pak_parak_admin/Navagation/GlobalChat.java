package com.rakpak.pak_parak_admin.Navagation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rakpak.pak_parak_admin.R;

import carbon.component.IconTextRow;

public class GlobalChat extends Fragment {

    private RecyclerView chatrecylerlist;

    public GlobalChat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.global_chat, container, false);

        chatrecylerlist = view.findViewById(R.id.GlobalChatView);
        chatrecylerlist.setHasFixedSize(true);
        chatrecylerlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
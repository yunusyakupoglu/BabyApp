package com.example.babyapp.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.babyapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private Button logoutButton;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Logout successful.", Toast.LENGTH_SHORT).show();

                // AuthActivity'e yönlendir
                Intent intent = new Intent(getContext(), AuthActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
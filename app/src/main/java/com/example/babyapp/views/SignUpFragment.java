package com.example.babyapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.babyapp.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {
    private FirebaseAuth auth;
    private EditText signUpEmail, signUpPassword;
    private Button signUpButton;
    private TextView loginRedirectText;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUpEmail = view.findViewById(R.id.signUp_email);
        signUpPassword = view.findViewById(R.id.signUp_password);
        signUpButton = view.findViewById(R.id.signUp_button);
        loginRedirectText = view.findViewById(R.id.loginRedirectText);

        // set onClickListener for the sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sign up the user
                signUpUser(signUpEmail.getText().toString(), signUpPassword.getText().toString());
            }
        });

        // set onClickListener for the login redirect text
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect the user to the login fragment
                ((AuthActivity)getActivity()).loadFragment(new SignInFragment());
            }
        });

        return view;
    }

    private void signUpUser(String email, String password) {
        // implement the sign up logic using FirebaseAuth
        if (email.isEmpty()){
            signUpEmail.setError("Email cannot be empty");
        }
        if (password.isEmpty()){
            signUpPassword.setError("Password cannot be empty");
        } else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(),"Signup successful", Toast.LENGTH_SHORT).show();
                        ((AuthActivity)getActivity()).loadFragment(new SignInFragment());
                    } else {
                        Toast.makeText(getContext(),"Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
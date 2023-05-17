package com.example.babyapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {
    private FirebaseAuth auth;
    private EditText signInEmail, signInPassword;
    private Button signInButton;
    private TextView signupRedirectText;

    public SignInFragment() {
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

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        signInEmail = view.findViewById(R.id.signIn_email);
        signInPassword = view.findViewById(R.id.signIn_password);
        signInButton = view.findViewById(R.id.signIn_button);
        signupRedirectText = view.findViewById(R.id.signUpRedirectText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(signInEmail.getText().toString(),signInPassword.getText().toString());
            }
        });

        // set onClickListener for the login redirect text
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect the user to the login fragment
                ((AuthActivity)getActivity()).loadFragment(new SignUpFragment());
            }
        });

        return view;
    }

    private void signInUser(String Email, String Password) {
        Email = signInEmail.getText().toString();
        Password = signInPassword.getText().toString();

        if (!Email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            if (!Password.isEmpty()) {
                auth.signInWithEmailAndPassword(Email, Password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(getContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                                // finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Login failed.", Toast.LENGTH_SHORT).show();

                            }
                        });
            } else {
                signInPassword.setError("Password cannot be empty");
            }
        } else if (Email.isEmpty()){
            signInEmail.setError("Email cannot be empty");
        } else {
            signInEmail.setError("Please enter valid email");
        }


    }
}
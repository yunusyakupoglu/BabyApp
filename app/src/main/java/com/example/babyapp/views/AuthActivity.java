package com.example.babyapp.views;

import android.os.Bundle;
import android.util.Log;
import com.example.babyapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AuthActivity extends AppCompatActivity {

    protected SignUpFragment signUpFragment;
    private SignInFragment signInFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SignInFragment signInFragment = new SignInFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.auth_container, signInFragment)
                .commit();
    }

    public void loadFragment(Fragment fragment) {
        switch (fragment.getClass().getSimpleName()) {
            case "SignUpFragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.auth_container, new SignUpFragment()).commit();
                break;
            case "SignInFragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.auth_container, new SignInFragment()).commit();
                break;
            default:
                Log.e("Fragment", "Invalid Fragment type");
                break;
        }
    }
}
package com.example.babyapp.views;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babyapp.R;
import com.example.babyapp.adapters.ProfileAdapter;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ProfileFragment extends Fragment {

    private RecyclerView rv;
    private MainActivity mainActivity;

    private ImageView avatar;
    private TextView usernameTextView;
    private TextView emailTextView;

    private Button editProfile;

    public ProfileFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private PostDao dao;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize FirebaseApp (if not done already)
        if (FirebaseApp.getApps(mainActivity).isEmpty()) {
            FirebaseApp.initializeApp(mainActivity);
        }

        usernameTextView = view.findViewById(R.id.txtProfileUsername);
        emailTextView = view.findViewById(R.id.txtProfileEmail);
        editProfile = view.findViewById(R.id.btnEditProfile);
        avatar = view.findViewById(R.id.imgPersonAvatar);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new EditProfileFragment(mainActivity)).commit();
            }
        });

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        DocumentReference userRef = db.collection("user").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Retrieve user information
                        String userName = document.getString("username");
                        String userEmail = user.getEmail();
                        String photo = document.getString("profilephoto");
                        // Display user information
                        usernameTextView.setText(userName);
                        emailTextView.setText(userEmail);
                        avatar.setImageURI(Uri.parse(photo));
                    } else {
                        // Document doesn't exist
                    }
                } else {
                    // An error occurred
                }
            }
        });

        rv = view.findViewById(R.id.rvProfile);
        BabyAppDatabase db = BabyAppDatabase.getDatabase(getContext()); // context
        dao = db.postDao();
        List<Post> posts = dao.loadPostsByUId(userId);
        ProfileAdapter adapter = new ProfileAdapter(posts);
        rv.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(true);


        return view;
    }
}

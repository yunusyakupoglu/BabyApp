package com.example.babyapp.views;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.babyapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private String selectedImageData;
    private EditText currentPassword ,password, confirmPassword, username;
    private ImageButton profilePhoto;
    private Button saveButton;
    private MainActivity mainActivity;
    private FirebaseFirestore db;

    private static final int IMAGE_PICK_MODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    public EditProfileFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize FirebaseApp (if not done already)
        if (FirebaseApp.getApps(mainActivity).isEmpty()) {
            FirebaseApp.initializeApp(mainActivity);
        }

        currentPassword = view.findViewById(R.id.txtCurrentPassword);
        password = view.findViewById(R.id.txtEditPassword);
        confirmPassword = view.findViewById(R.id.txtEditConfirmPassword);
        username = view.findViewById(R.id.txtEditUserName);
        profilePhoto = view.findViewById(R.id.imgSelectProfilePhoto);
        saveButton = view.findViewById(R.id.btnEditProfileSave);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else{
                    // system os is less then marsmallow
                    pickImageFromGallery();
                }
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
                        String photoUri = document.getString("profilephoto");

                        // Display user information
                        if (photoUri != null) {
                            profilePhoto.setImageURI(Uri.parse(photoUri));
                        } else {
                            // photoUri null ise, alternatif bir işlem yapabilirsiniz veya hata durumunu ele alabilirsiniz.
                        }                            username.setText(userName);

                    } else {
                        // Document doesn't exist
                    }
                } else {
                    // An error occurred
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(username.getText().toString(),selectedImageData,user);
                updateUserPassword(currentPassword.getText().toString(),password.getText().toString(),confirmPassword.getText().toString(),user);
            }
        });
        return view;
    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_MODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(mainActivity, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mainActivity.RESULT_OK && requestCode == IMAGE_PICK_MODE) {
            //imageViewFile.setImageURI(data.getData());
            Uri selectedImageUri = data.getData();
            // Get the path from the Uri
            final String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            profilePhoto.setImageURI(selectedImageUri);
            selectedImageData = selectedImageUri.toString();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mainActivity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public void onImageButtonClick(View view) {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, 1);
    }

    private void updateUserProfile(String username, String photoUri, FirebaseUser currentUser) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("user").document(userId);

            // Güncellenmiş kullanıcı bilgilerini içeren bir harita oluştur
            Map<String, Object> updatedUser = new HashMap<>();
            updatedUser.put("username", username);
            updatedUser.put("profilephoto", photoUri);

            // Kullanıcı belgesini güncelle
            userRef.update(updatedUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: User profile updated for " + userId);
                            // Başarı durumunda gerekli işlemleri yapabilirsiniz
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Error updating user profile for " + userId, e);
                            // Hata durumunda gerekli işlemleri yapabilirsiniz
                        }
                    });
        }
    }

    private void updateUserPassword(String currentPassword, String newPassword, String confirmPassword, FirebaseUser currentUser) {
        if (currentUser != null) {
            if (!TextUtils.isEmpty(currentPassword)) {
                if (!TextUtils.isEmpty(newPassword) && newPassword.equals(confirmPassword)) {
                    // Kullanıcının mevcut oturumunu güncel parola ile yeniden doğrula
                    AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);
                    currentUser.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Kullanıcının parolasını güncelle
                                    currentUser.updatePassword(newPassword)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "onSuccess: User password updated");
                                                    // Parola güncelleme başarılı olduğunda gerekli işlemleri yapabilirsiniz
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "onFailure: Error updating user password", e);
                                                    // Parola güncelleme hatası durumunda gerekli işlemleri yapabilirsiniz
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: Error reauthenticating user", e);
                                    // Kullanıcı doğrulama hatası durumunda gerekli işlemleri yapabilirsiniz
                                }
                            });
                } else {
                    // Yeni parola boş veya null ise veya yeni parolalar uyuşmuyorsa hata mesajı göster
                    Toast.makeText(getContext(), "Invalid new password", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Mevcut parola boş veya null ise hata mesajı göster
                Toast.makeText(getContext(), "Please enter your current password", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
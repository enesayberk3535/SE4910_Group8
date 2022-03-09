package com.befb.ustam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.befb.ustam.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;
    String username,password,name,phoneNumber;
    boolean isUsta,isMusteri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void createUser(View view)
    {
        username = binding.emailEditText.getText().toString();
        password = binding.passwordEditText.getText().toString();
        name = binding.nameEditText.getText().toString();
        isMusteri =  binding.musteriRadioButton.isChecked();
        isUsta = binding.ustaRadioButton.isChecked();
        phoneNumber = binding.phoneEditText.getText().toString();
        mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(registerActivity.this,"register success",Toast.LENGTH_LONG).show();
                    upload();
                }
                else
                    Toast.makeText(registerActivity.this,"register unsuccess",Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void upload() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(mAuth.getUid());
        HashMap<String, Object> postData = new HashMap<>();
        if(isMusteri){
            postData.put("UserType", "Musteri");
        }
        else{
            postData.put("UserType", "Usta");
        }
        postData.put("useremail",userEmail);
        postData.put("PhoneNumber",phoneNumber);
        postData.put("Name",name);
        documentReference.set(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(registerActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registerActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
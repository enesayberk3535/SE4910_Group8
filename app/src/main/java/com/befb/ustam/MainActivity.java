package com.befb.ustam;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import com.befb.ustam.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;
    MainRecyclerAdapter feedRecyclerAdapter;
    private ActivityMainBinding binding;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post) {
            Intent intentToUpload = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(intentToUpload);
        } else if (item.getItemId() == R.id.signout) {
            firebaseAuth.signOut();
            Intent intentToSignUp = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentToSignUp);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postArrayList = new ArrayList<Post>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();


        //RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter = new MainRecyclerAdapter(postArrayList);
        binding.recyclerView.setAdapter(feedRecyclerAdapter);

        //Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        System.out.println("referans: " + firebaseAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //System.out.println(snapshot.getValue().toString());
                    System.out.println(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getDataFromFirestore() {

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        String comment = (String) data.get("comment");
                        String userEmail = (String) data.get("useremail");
                        String dateString =  (String) data.get("postdate");
                        Post post = new Post(userEmail,comment,dateString);

                        postArrayList.add(post);

                    }
                    feedRecyclerAdapter.notifyDataSetChanged();

                }

            }
        });


    }
    public void getDataFromFirestore1() {

        CollectionReference collectionReference = firebaseFirestore.collection("Users");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        String comment = (String) data.get("comment");
                        String userEmail = (String) data.get("useremail");
                        String dateString =  (String) data.get("postdate");
                        Post post = new Post(userEmail,comment,dateString);

                        postArrayList.add(post);

                    }
                    feedRecyclerAdapter.notifyDataSetChanged();

                }

            }
        });


    }

}
package com.befb.ustam.ui.ProfilePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.befb.ustam.Comment;
import com.befb.ustam.MainPageActivity;
import com.befb.ustam.MainRecyclerAdapter;
import com.befb.ustam.R;
import com.befb.ustam.RecyclerCommentAdapter;
import com.befb.ustam.UploadActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProfilePageActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;
    TextView informationTextView;
    TextView phoneTextView;
    TextView descriptionTextView;
    TextView isekleTextview;
    String expertUUID;
    ImageView imageView;
    ImageView imageViewCalisma;
    EditText commentEditText;
    RecyclerView recyclerViewComment;
    RecyclerCommentAdapter recyclerCommentAdapter;
    ArrayList<Comment> commentsArrayList;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        informationTextView = findViewById(R.id.expertInfoTextView);
        descriptionTextView = findViewById(R.id.descriptonCalisma);
        phoneTextView = findViewById(R.id.phoneTextView);
        commentEditText = findViewById(R.id.commentEditText);
        recyclerViewComment = findViewById(R.id.RecyclerViewComment);
        ratingBar = findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.profileImageView);
        imageViewCalisma = findViewById(R.id.imageViewCalisma);
        isekleTextview =findViewById(R.id.textView8);
        commentsArrayList = new ArrayList<>();
        Intent intent = getIntent();
        expertUUID= intent.getStringExtra("expertUUID");

        getDownloadImageUrl();
        getDownloadImageUrl2();
        getDataFromFirestore();
        getDataFromFirestoreComments();
        getDataFromFirestoreStars();
    }

    public void getDataFromFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        DocumentReference documentReference = collectionReference.document(expertUUID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        informationTextView.setText(document.getString("AboutMe"));
                        descriptionTextView.setText(document.getString("DescriptionWork"));
                        phoneTextView.setText("Telefon numarası: "+document.getString("PhoneNumber"));
                        String type = document.getString("UserType");
                        if(type.contains("Musteri")){
                            isekleTextview.setVisibility(View.GONE);
                            imageViewCalisma.setVisibility(View.GONE);
                            descriptionTextView.setVisibility(View.GONE);
                        }
                        else{
                            isekleTextview.setVisibility(View.VISIBLE);
                            imageViewCalisma.setVisibility(View.VISIBLE);
                            descriptionTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }

    public void getDataFromFirestoreComments() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.document(expertUUID).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        List<DocumentSnapshot> allDocument = querySnapshot.getDocuments();
                        for (DocumentSnapshot snapshot : allDocument) {
                            String receivedName = snapshot.getString("Name");
                            String receivedComment = snapshot.getString("comment");
                            System.out.println(">Z " +receivedName + " " + receivedComment);
                            Comment comment = new Comment(receivedName,receivedComment);
                            commentsArrayList.add(comment);
                        }
                    }

                    //RecyclerView
                    recyclerViewComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerCommentAdapter = new RecyclerCommentAdapter(commentsArrayList);
                    recyclerViewComment.setAdapter(recyclerCommentAdapter);

                } else {
                    Log.d("LOGGER", "No such Querysnapshot");
                }
            }
        });
    }
    public void getDownloadImageUrl() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        DocumentReference documentReference = collectionReference.document(expertUUID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if(document.getString("downloadurl") != null && document.getString("downloadurl") != "")
                        Picasso.get().load(document.getString("downloadurl")).into(imageView);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }
    public void getDownloadImageUrl2() {

        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        DocumentReference documentReference = collectionReference.document(expertUUID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if(document.getString("downloadurl2") != null && document.getString("downloadurl2") != "")
                        Picasso.get().load(document.getString("downloadurl2")).into(imageViewCalisma);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }
    public void getDataFromFirestoreStars() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.document(expertUUID).collection("rating").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Float totalStars=0f;
                    Integer quantityStars=0;
                    QuerySnapshot querySnapshot = task.getResult();
                    System.out.println(querySnapshot);
                    if (querySnapshot != null) {
                        List<DocumentSnapshot> allDocument = querySnapshot.getDocuments();
                        quantityStars = allDocument.size();
                        for (DocumentSnapshot snapshot : allDocument) {
                            Map<String,Object> data = snapshot.getData();
                            //Casting
                            if (data.get("stars") != null){
                                Number stars = (Number) data.get("stars");
                                System.out.println("yildiz " + stars);
                                totalStars+=Float.parseFloat(stars.toString());
                            }
                        }
                        System.out.println(totalStars);
                        System.out.println(quantityStars);
                        Float avgStars = totalStars / quantityStars;
                        ratingBar.setRating(avgStars);
                        System.out.println("avg " + avgStars);
                    }



                    //RecyclerView
                    recyclerViewComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerCommentAdapter = new RecyclerCommentAdapter(commentsArrayList);
                    recyclerViewComment.setAdapter(recyclerCommentAdapter);

                } else {
                    Log.d("LOGGER", "No such Querysnapshot");
                }
            }
        });
    }
    public void makeComment(View view)
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();
        String comment = commentEditText.getText().toString();
        HashMap<String, Object> postData = new HashMap<>();
        postData.put("Name",userEmail);
        postData.put("comment",comment);

        firebaseFirestore.collection("Users").document(expertUUID).collection("comments").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                commentsArrayList.clear();
                getDataFromFirestoreComments();
            }
        });
    }

    public void giveStar(View view){
        HashMap<String, Object> postData = new HashMap<>();
        Float star = ratingBar.getRating();
        postData.put("stars",star);
        firebaseFirestore.collection("Users").document(expertUUID).collection("rating").document(mAuth.getCurrentUser().getUid()).set(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Değerlendirmeniz için teşekkürler!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
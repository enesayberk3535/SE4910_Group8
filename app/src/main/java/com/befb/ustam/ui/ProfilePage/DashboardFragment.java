package com.befb.ustam.ui.ProfilePage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.befb.ustam.Comment;
import com.befb.ustam.RecyclerCommentAdapter;
import com.befb.ustam.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;
    RecyclerCommentAdapter recyclerCommentAdapter;
    ArrayList<Comment> commentsArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final FloatingActionButton floatingActionButton = binding.floatingActionButton;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        commentsArrayList = new ArrayList<>();
        getDataFromFirestoreComments();
        getDownloadImageUrl();
        getDataFromFirestore();
        getDataFromFirestoreStars();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void getDownloadImageUrl() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        DocumentReference documentReference = collectionReference.document(mUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Picasso.get().load(document.getString("downloadurl")).into(binding.profileImageView);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }
    public void getDataFromFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        DocumentReference documentReference = collectionReference.document(mUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        binding.expertInfoTextView.setText(document.getString("AboutMe"));
                        binding.phoneTextView.setText("Telefon numarası: "+document.getString("PhoneNumber"));

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
        collectionReference.document(mUser.getUid()).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    binding.RecyclerViewComment.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerCommentAdapter = new RecyclerCommentAdapter(commentsArrayList);
                    binding.RecyclerViewComment.setAdapter(recyclerCommentAdapter);

                } else {
                    Log.d("LOGGER", "No such Querysnapshot");
                }
            }
        });
    }
    public void getDataFromFirestoreStars() {
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.document(mUser.getUid()).collection("rating").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        binding.ratingBar.setRating(avgStars);
                        System.out.println("avg " + avgStars);
                    }


                } else {
                    Log.d("LOGGER", "No such Querysnapshot");
                }
            }
        });
    }
}
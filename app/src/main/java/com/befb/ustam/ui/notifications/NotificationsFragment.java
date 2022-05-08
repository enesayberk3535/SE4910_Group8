package com.befb.ustam.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.befb.ustam.MainRecyclerAdapter;
import com.befb.ustam.Model.CityList;
import com.befb.ustam.Post;
import com.befb.ustam.R;
import com.befb.ustam.databinding.FragmentNotificationsBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    FirebaseFirestore firebaseFirestore;
    String[] items = {"Su tesisati", "Elektrik", "Bilgisayar Tamiri", "Televizyon Tamiri", "Diger"};
    ArrayAdapter<String> adapterItems;
    ArrayList<Post> postArrayList;
    MainRecyclerAdapter feedRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAllCities();
        firebaseFirestore = FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<Post>();
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllResults();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getAllResults(){
        postArrayList.clear();
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("WorkType",binding.autoCompleteTxt1.getText().toString())
                .whereEqualTo("City",binding.spinner2.getSelectedItem().toString())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }


                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        String comment = (String) data.get("comment");
                        String userEmail = (String) data.get("useremail");
                        String dateString =  (String) data.get("postdate");
                        String expertUUID = (String) data.get("expertUUID");
                        String cityString =  (String) data.get("City");
                        Post post = new Post(userEmail,comment,dateString);
                        post.city = cityString;
                        post.expertUUID =expertUUID;
                        postArrayList.add(post);

                    }
                    feedRecyclerAdapter.notifyDataSetChanged();
                }


            }
        });
        binding.recyclerSelectedResults.setLayoutManager(new LinearLayoutManager(getContext()));
        feedRecyclerAdapter = new MainRecyclerAdapter(postArrayList);
        binding.recyclerSelectedResults.setAdapter(feedRecyclerAdapter);
        binding.recyclerSelectedResults.setVisibility(View.VISIBLE);
    }

    public void setAllCities(){
        //DROPBOX

        adapterItems = new ArrayAdapter<String>(getContext(),R.layout.list_item,items);
        binding.autoCompleteTxt1.setAdapter(adapterItems);
        binding.autoCompleteTxt1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = parent.getItemAtPosition(position).toString();
               // Toast.makeText(getContext(), "Item:" + item, Toast.LENGTH_SHORT).show();
            }
        });

        CityList cityList = new CityList();
        try {
            //Load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.citys)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }

            Gson gson = new Gson();
            cityList = gson.fromJson(jsonBuilder.toString(),CityList.class);

            Log.d("Deneme",cityList.getCityDetail().get(0).getName());


        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        }
        List<String> spinnerData = new ArrayList<>();

        for(int i=0;i<cityList.getCityDetail().size();i++){
            spinnerData.add(cityList.getCityDetail().get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerData);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner2.setAdapter(adapter);
    }
}
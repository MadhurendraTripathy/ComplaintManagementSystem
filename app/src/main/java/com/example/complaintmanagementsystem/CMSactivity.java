package com.example.complaintmanagementsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CMSactivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerViewAdapter_CmsActivity mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    ArrayList<ComplaintParameters>mArrayList;
    String ComplaintId;
    String fetchedunit;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmsactivity);


        if(mArrayList==null){
            //getting instance of firestore
            db.collection("Users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    fetchedunit = documentSnapshot.get("unitID").toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
            mArrayList = new ArrayList<>();
            db.collection("Complaints").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            ComplaintParameters cp_obj = d.toObject(ComplaintParameters.class);
                            cp_obj.ComplainId=d.getId();
                           // Log.d("getunitid=",cp_obj.getUnitid());
                            if(cp_obj.getUnitid().equals(fetchedunit)){
                                //Log.d("entered if for",cp_obj.getUnitid());
                                mArrayList.add(cp_obj);
                            }
                        }
                        //setting up recycler view
                        if(mArrayList!=null){
                            Toast.makeText(CMSactivity.this, "Data Fetch Successful", Toast.LENGTH_SHORT).show();
                            setUpRecyclerView(mArrayList,fetchedunit);
                        }
                        else{
                            Toast.makeText(CMSactivity.this, "Empty List", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CMSactivity.this, "Unable To Fetch Records", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //fetching users Unit ID
        db.collection("Users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fetchedunit = documentSnapshot.get("unitID").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        setUpRecyclerView(mArrayList,fetchedunit);


        getSupportActionBar().setTitle("Complaints Section");//Name to be displayed in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//showing back navigation button in toolbar

        fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener(){ //setting functionality of FAB
            @Override
            public void onClick(View v) {
                addNewComplaint();
            }
        });


    }



    //connecting the menu items created into the actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complaint_section_action_buttons,menu);

        MenuItem searchitem = menu.findItem(R.id.search_button_in_action_bar_of_cmsactivity);
        SearchView searchView  = (SearchView) searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.search_button_in_action_bar_of_cmsactivity:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//showing menu items in actionbar completed


    //addNewComplaint
    void addNewComplaint(){
        Intent intent = new Intent(CMSactivity.this,AddNewComplaint.class);
        intent.putExtra("fetchedunitid",fetchedunit);
        startActivity(intent);
    }

    void setUpRecyclerView(ArrayList<ComplaintParameters> marraylist, String fetchedunit){
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapter_CmsActivity(marraylist,fetchedunit);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.openCardView(new RecyclerViewAdapter_CmsActivity.setup_itemViewOnclicklistner() {
            @Override
            public void itemViewClickSetUp(int pos) {
                showMoreDetails(pos);
            }
        });
    }

    void showMoreDetails(int pos){
        Intent intent  = new Intent(CMSactivity.this,ComplaintInDetail.class);
        intent.putExtra("current_position",pos);
        Bundle b = new Bundle();
        b.putSerializable("mArrayList",mArrayList);
        intent.putExtras(b);
        startActivity(intent);
    }


}

package com.example.complaintmanagementsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintInDetail extends AppCompatActivity {

    ArrayList<ComplaintParameters> complaintDetailsArrayList;

    TextView showProperties,showComplaintId;
    TextView showTitle;
    TextView showDesc;
    ImageView showAttachment;
    TextView showTransactions;
    TextView showComments;
    FirebaseFirestore db;
    ArrayList<DocumentSnapshot> comments_arrayList;
    String ComplaintID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_in_detail);
        showProperties = findViewById(R.id.PropertiesOfComplaint_in_detailedView);
        showTitle = findViewById(R.id.TitleOfTheComplaint_in_detailedView);
        showDesc = findViewById(R.id.DescOfTheComplaint_in_detailedView);
        showAttachment = findViewById(R.id.ImageViewAttachment_in_detailedView);
        showTransactions = findViewById(R.id.Transactions_in_detailedView);
        showComments = findViewById(R.id.Comments_in_detailedView);
        showComplaintId = findViewById(R.id.Complaint_Id_in_detailedView);

        getSupportActionBar().setTitle("Complaint Details");//Name to be displayed in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//showing back navigation button in toolbar

        int current_position = getIntent().getIntExtra("current_position",-1);

        Bundle b  = getIntent().getExtras();
        complaintDetailsArrayList = (ArrayList<ComplaintParameters>) b.getSerializable("mArrayList");

        setUpDetails(current_position,complaintDetailsArrayList);


        showComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComplaintInDetail.this,ShowComments.class);
                intent.putExtra("ComplaintId",ComplaintID);
                intent.putExtra("User",showProperties.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void setUpDetails(int current_position,ArrayList<ComplaintParameters> complaintDetailsArrayList) {

        ComplaintParameters cp_current_item = complaintDetailsArrayList.get(current_position);
        showProperties.setText("UNIT ID: "+cp_current_item.getUnitid());
        showTitle.setText("Title :\n"+cp_current_item.getTitle());
        showDesc.setText("Description :\n"+cp_current_item.getDescription());
        if(cp_current_item.getUrl()!=null){
            Picasso.get().load(cp_current_item.getUrl()).placeholder(R.drawable.loadingattachment).into(showAttachment);
        }
        else{
            showAttachment.setImageResource(R.drawable.noattachment);
        }
        ComplaintID = cp_current_item.getComplainId();
        showComplaintId.setText("Complaint ID :\n"+ComplaintID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_complaint_activiy_actionbuttons,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.editcomplaint_in_menu_bar_of_detailComplaint:
                return true;

            case R.id.completed_btn_in_menu_bar_of_detailComplaint:
                return true;

            case R.id.close_btn_in_menu_bar_of_detailComplaint:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}

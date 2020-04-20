package com.example.complaintmanagementsystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ShowComments extends AppCompatActivity {

    Button postComment;
    EditText typeComment;
    TextView showComments;
    ScrollView mScrollView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onStart() {
        super.onStart();

        db.collection("Complaints").document(getIntent().getStringExtra("ComplaintId")).collection("comments_section").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    typeComment.setText(null);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    String comments="\tComments : \n\n";
                    for(DocumentSnapshot d:list){
                        Map<String,Object>mp=d.getData();
                        for(Map.Entry<String,Object>entry : mp.entrySet()){
                            comments+=entry.getKey()+"--"+entry.getValue().toString()+"\n\n";
                        }
                    }
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                    showComments.setText(comments);
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        postComment = findViewById(R.id.PostCommentButton_in_detailedView);
        typeComment = findViewById(R.id.EditTextAddCommnet_in_detailedView);
        showComments = findViewById(R.id.Comments_in_detailedView);
        mScrollView = findViewById(R.id.comments_scroll_view);
        mScrollView.fullScroll(View.FOCUS_DOWN);


        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!typeComment.getText().toString().trim().isEmpty()){
                    db = FirebaseFirestore.getInstance();
                    Map<String,String> mp = new HashMap<String, String>();
                    mp.put(getIntent().getStringExtra("User"),typeComment.getText().toString().trim());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    Date date = new Date();
                    db.collection("Complaints").document(getIntent().getStringExtra("ComplaintId")).collection("comments_section").document(formatter.format(date)).set(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ShowComments.this, "Comment Posted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ShowComments.this, "Unable to post comments", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(ShowComments.this, "Please add a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

package com.example.complaintmanagementsystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintmanagementsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.ref.Reference;
import java.net.URL;

public class AddNewComplaint extends AppCompatActivity {

    TextView munitId;
    Spinner mcategoery;
    Spinner mseverity;
    Spinner mimpact;
    EditText mComplaintTitle;
    EditText mComplaintDescription;
    TextView mShowPathOfFile;
    ImageButton mImageButton;
    Button postComplaintButton;
    Uri filepath;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String url;
    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    StorageReference ref = mFirebaseStorage.getReference();
    StorageReference imageref;
    final int pick_image_request=1;

    String unitid;
    String category;
    String severity;
    String impact;
    String title;
    String description;
    String ComplaintId;
    String AssignedTo ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_complaint);

        getSupportActionBar().setTitle("New Complaint");//Name to be displayed in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//showing back navigation button in toolbar
        //linking to xml
        munitId = findViewById(R.id.unitIdTextView);
        mcategoery = findViewById(R.id.categoerySpinner);
        mseverity = findViewById(R.id.severitySpinner);
        mimpact = findViewById(R.id.impactSpinner);
        mComplaintTitle = findViewById(R.id.titleEdittext);
        mComplaintDescription = findViewById(R.id.descriptionEdittext);
        mShowPathOfFile = findViewById(R.id.TextViewShowingPathOfAttachedFile);
        mImageButton = findViewById(R.id.AddAttachmentsImageButton);
        postComplaintButton = findViewById(R.id.postComplaintButton);
        munitId.setText(getIntent().getStringExtra("fetchedunitid"));

        //selecting image to upload to firestore
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("image Button","Clicked");
                chooseImage();
            }
        });

        //sending data to firestore
        postComplaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetching user entered data
                unitid=munitId.getText().toString();
                category = mcategoery.getSelectedItem().toString();
                severity = mseverity.getSelectedItem().toString();
                impact = mimpact.getSelectedItem().toString();
                title = mComplaintTitle.getText().toString().trim();
                description = mComplaintDescription.getText().toString().trim();

                if(validate(unitid,category,severity,impact,title,description)){
                    imageUpload();
                }
            }
        });
    }

    boolean validate(String unitid,String category,String severity,String impact,String title,String description ){
        if(category.equals("Select-Category")|| severity.equals("Select-Severity")  || impact.equals( "Select-Impact") || title.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please Fill All The Required Details", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    void imageUpload(){
        if(filepath==null){
            uploaddata(null);
        }
        else{
            //uploading attachments and getting download url
            ProgressDialog dialog = ProgressDialog.show(AddNewComplaint.this, "", "Adding Attachment Please Wait...", true);
            final String nameofimage = "image"+System.currentTimeMillis();
            final StorageReference uploader = ref.child(nameofimage);
            uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageref = ref.child(nameofimage);
                    imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            //Toast.makeText(AddNewComplaint.this, url, Toast.LENGTH_LONG).show();
                            uploaddata(url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewComplaint.this, "Unable To Fetch Download Link", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewComplaint.this, "Unable To Add Attachments", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void uploaddata(String URL){
        //Printing Log Messages
        Log.d("I am Printing unitid = ",unitid);
        Log.d("category = ",category);
        Log.d("severity = ",severity);
        Log.d("title = ",title);
        Log.d("description = ",description);
        ProgressDialog dialog = ProgressDialog.show(AddNewComplaint.this, "", "Submitting Your Complaint Please wait...", true);
        //collection reference object
        CollectionReference itemstobeuploaded = db.collection("Complaints");

        //creating an object to be passed into firestore for upload of data
        ComplaintParameters cp = new ComplaintParameters(unitid,category,severity,impact,title,description,URL,"NONE","");
        itemstobeuploaded.add(cp).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddNewComplaint.this, "Complaint Submitted Successfully", Toast.LENGTH_SHORT).show();
                //finish();
                Intent intent = new Intent(AddNewComplaint.this,CMSactivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewComplaint.this, "Complaint Submission Failed Please Retry", Toast.LENGTH_SHORT).show();
            }
        });
   }

    //selecting image to upload
    void chooseImage(){
        Intent intent1=new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, pick_image_request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == pick_image_request && resultCode == RESULT_OK && data != null && data.getData()!=null){
            filepath = data.getData();
            mShowPathOfFile.setText(filepath.toString());
        }
    }
}

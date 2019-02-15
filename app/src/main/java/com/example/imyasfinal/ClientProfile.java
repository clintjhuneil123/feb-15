package com.example.imyasfinal;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.ListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class ClientProfile extends AppCompatActivity {
    static int PreqCode = 1;
    static int REQUESCODE = 1;
    TextView artist_fname1, artist_lastname1,artist_email11,getArtist_cont1;
    ImageView artist_imag1;
    private static Uri pickedImgUri;
    private static String imgpath  = "";
    private static final int IMAGE_REQUEST_1 = 1;
    Query details;
    private StorageReference  mStorageRef;
    FirebaseDatabase database;
    DatabaseReference getCurrentUser,update;
    ImageView pictureclient;
    MaterialEditText email,lname,fname,num;
    Button updateprof;
    FloatingActionButton fabclient;
    FirebaseAuth mAuth;
    Client client;
    RelativeLayout rootLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);


        database = FirebaseDatabase.getInstance();
//        details = database.getReference("ArtistPortfolio").orderByChild("artistID").equalTo(getIntent().getStringExtra("ArtistId"));
        mAuth = FirebaseAuth.getInstance();
        getCurrentUser = FirebaseDatabase.getInstance().getReference();
        artist_fname1 = (TextView) findViewById(R.id.artist_NAME1);
        artist_lastname1 = (TextView) findViewById(R.id.artist_LNAME1);
        artist_email11 = (TextView) findViewById(R.id.artist_EMAIL1);
        getArtist_cont1 = (TextView) findViewById(R.id.artist_NUM1);
        artist_imag1 = (ImageView) findViewById(R.id.img_profile11);
        pictureclient = (ImageView) findViewById(R.id.pictureclient);


            getprofile();


        fabclient = (FloatingActionButton)findViewById(R.id.profileedit);
        fabclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showupdate();
            }
        });

    }

    private void showupdate() {
        update =  FirebaseDatabase.getInstance().getReference("Clients").child(mAuth.getCurrentUser().getUid());
        update.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String date = dataSnapshot.child("lastname").getValue().toString();
                    String location = dataSnapshot.child("firstname").getValue().toString();
                    String time = dataSnapshot.child("email").getValue().toString();
                    String people = dataSnapshot.child("contactnumber").getValue().toString();
                    artist_lastname1.setText(date);
                    artist_fname1.setText(location);
                    artist_email11.setText(time);
                    getArtist_cont1.setText(people);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ClientProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClientProfile.this);
        alertDialog.setTitle("Edit Profile");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.edit_profile_client, null);

        artist_lastname1 = add_menu_layout.findViewById(R.id.edtlname);
        artist_fname1 = add_menu_layout.findViewById(R.id.edtfname);
        artist_email11=add_menu_layout.findViewById(R.id.edtemail);
        getArtist_cont1=add_menu_layout.findViewById(R.id.edtnum);
        updateprof = add_menu_layout.findViewById(R.id.btnupdate);
        pictureclient = add_menu_layout.findViewById(R.id.pictureclient);

        pictureclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= 22) {
                    checkandRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        updateprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateprof();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(client != null)
                {
                    update.child(mAuth.getCurrentUser().getUid()).setValue(client);
                    Snackbar.make(rootLayout1,"New Portfolio"+client.getEmail()+"was updated",Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    private void updateprof() {
//        if (pickedImgUri != null) {
//            final ProgressDialog mDialog = new ProgressDialog(this);
//            mDialog.setMessage("Uploading..");
//            mDialog.show();
//
////            String imageName = UUID.randomUUID().toString();
//            final StorageReference imageFolder = mStorageRef.child("images/+imageName");
//            imageFolder.putFile(pickedImgUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            client = new Client();
//                            client.setFirstname(edtLoc1.getText().toString());
//                            client.setLastname(dateText1.getText().toString());
//                            client.setEmail(timeText1.getText().toString());
//                            client.setContactnumber(edtpeople1.getText().toString());
//
//                        }
//                    });
//
//        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkandRequestForPermission() {
        if (ContextCompat.checkSelfPermission(ClientProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ClientProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(ClientProfile.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ClientProfile.this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, PreqCode);
            }

        } else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_1 && resultCode == RESULT_OK) {
            pickedImgUri = data.getData();
            Picasso.get().load(pickedImgUri).fit().centerCrop().into(pictureclient);

            pickedImgUri = data.getData();
            pictureclient.setImageURI(pickedImgUri);
            final String path = System.currentTimeMillis() + "." + getFileExtension(pickedImgUri);

            StorageReference storageReference = mStorageRef.child("Images").child(path);
            storageReference.putFile(pickedImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    mStorageRef.child("Images/"+path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgpath = uri.toString();
                        }
                    });
                }
            });
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getApplication().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }








    private void getprofile() {
        getCurrentUser = FirebaseDatabase.getInstance().getReference("Clients");
        getCurrentUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                client = dataSnapshot.getValue(Client.class);

                if(dataSnapshot.exists()){
//                    Picasso.get().load(client.getImage()).into(artist_imag);

                    artist_lastname1.setText("Lastname: "+client.getLastname());

                    artist_fname1.setText("Firstname: "+client.getFirstname());

                    getArtist_cont1.setText("Contact Number: "+client.getContactnumber());

                    artist_email11.setText("Email: "+client.getEmail());

//                    artist_description11.setText("Description: "+client.get());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


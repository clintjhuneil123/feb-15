package com.example.imyasfinal;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.ListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

public class ArtistAddList extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;
    RelativeLayout rootLayout;
    FirebaseDatabase db;
    DatabaseReference list1;
    Query list2;
    static int PreqCode = 1;
    static int REQUESCODE = 1;
//    FirebaseStorage storage;
    private StorageReference  mStorageRef;
    ImageView ImgArtistPhoto;
    String artistId="";
    private static Uri pickedImgUri;
    private static String imgpath  = "";
    private static final int IMAGE_REQUEST_1 = 1;
    FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder> searchadapter;
    private String currentTime;
    FirebaseAuth mAuth;
    private String currentDate;

    TextView dateText, timeText;
    MaterialEditText edtName,edtDesc,edtPrice,edtLoc;
    Button btnUpload,timebtn,datebtn;

    ArtistPorfolio newartistPorfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_add_list);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db=FirebaseDatabase.getInstance();
        recyclerView =(RecyclerView) findViewById(R.id.recycler_list1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        mAuth = FirebaseAuth.getInstance();



        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaddartist();
            }
        });



        if(getIntent() !=null){
            artistId = getIntent().getStringExtra("ArtistId");

            if(artistId.equals(mAuth.getCurrentUser().getUid())){
                fab.setVisibility(View.VISIBLE);
            }
            else{
                fab.setVisibility(View.GONE);
            }
            list2 = FirebaseDatabase.getInstance().getReference("ArtistPortfolio").orderByChild("artistID").equalTo(artistId);
            Toast.makeText(this, artistId, Toast.LENGTH_SHORT).show();
            LoadList(artistId);
        }


    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkandRequestForPermission() {
        if (ContextCompat.checkSelfPermission(ArtistAddList.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ArtistAddList.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(ArtistAddList.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ArtistAddList.this, new String[]
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
            Picasso.get().load(pickedImgUri).fit().centerCrop().into(ImgArtistPhoto);

            pickedImgUri = data.getData();
            ImgArtistPhoto.setImageURI(pickedImgUri);
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

    private void uploadImage() {
        if(pickedImgUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading..");
            mDialog.show();

//            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = mStorageRef.child("images/+imageName");
            imageFolder.putFile(pickedImgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(ArtistAddList.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newartistPorfolio = new ArtistPorfolio();
                                    newartistPorfolio.setName(edtName.getText().toString());
                                    newartistPorfolio.setLocation(edtLoc.getText().toString());
                                    newartistPorfolio.setCurrentDate(dateText.getText().toString());
                                    newartistPorfolio.setCurrentTime(timeText.getText().toString());
                                    newartistPorfolio.setDescription(edtDesc.getText().toString());
                                    newartistPorfolio.setPrice(edtPrice.getText().toString());
                                    newartistPorfolio.setArtistID(artistId);
                                    imgpath = uri.toString();
                                    newartistPorfolio.setImage(imgpath);
                                }
                            });
                            newartistPorfolio = new ArtistPorfolio(
                                    edtName.getText().toString(),
                                    imgpath,
                                    edtDesc.getText().toString(),
                                    edtPrice.getText().toString(),
                                    artistId,
                                    edtLoc.getText().toString(),
                                    dateText.getText().toString(),
                                    timeText.getText().toString()
                            );
                            DatabaseReference getId = FirebaseDatabase.getInstance().getReference("ArtistPortfolio");
                            String id = getId.push().getKey();
                            FirebaseDatabase.getInstance().getReference("ArtistPortfolio")
                                    .child(id)
                                    .setValue(newartistPorfolio).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(ArtistAddList.this, "Successfull", Toast.LENGTH_SHORT).show();



                                    }
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(ArtistAddList.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+"%");
                        }
                    });
        }
    }

    private void showaddartist() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ArtistAddList.this);
        alertDialog.setTitle("Add new Portfolio");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_artist_layout, null);
        ImgArtistPhoto = (ImageView) add_menu_layout.findViewById(R.id.photoartist) ;
        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDesc = add_menu_layout.findViewById(R.id.edtDescription);
        edtLoc = add_menu_layout.findViewById(R.id.edtLocation);
        dateText=add_menu_layout.findViewById(R.id.dateText);
        timeText=add_menu_layout.findViewById(R.id.timeText);
        datebtn = add_menu_layout.findViewById(R.id.datebtn);
        timebtn = add_menu_layout.findViewById(R.id.timebtn);
        timebtn.setOnClickListener(this);
        datebtn.setOnClickListener(this);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);


        ImgArtistPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= 22) {
                    checkandRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(newartistPorfolio != null)
                {
                    list1.push().setValue(newartistPorfolio);
                    Snackbar.make(rootLayout,"New Portfolio"+newartistPorfolio.getName()+"was added",Snackbar.LENGTH_SHORT)
                            .show();;
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

    private void LoadList(String artistId) {
        searchadapter = new FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder>(ArtistPorfolio.class,R.layout.listitem,ListViewHolder.class,list2) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ArtistPorfolio model, int position) {
                viewHolder.list_name.setText(model.getName());
                Picasso.get().load(model.getImage())
//                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.list_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent addImages = new Intent(ArtistAddList.this,AddImages.class);
                        addImages.putExtra("ArtistID",searchadapter.getRef(position).getKey());
                        startActivity(addImages);
                    }
                });

            }
        };
        searchadapter.notifyDataSetChanged();
        recyclerView.setAdapter(searchadapter);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate = DateFormat.getDateInstance().format(c.getTime());
        dateText.setText(currentDate);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        if (hourOfDay >= 12){
            hourOfDay = hourOfDay - 12;
            currentTime = hourOfDay + " : " + minute + "0" + " PM";
        }
        else{
            currentTime = hourOfDay + " : " + minute + "0" + " AM";

        }

        timeText.setText("Hour: " + hourOfDay + " Minute: " + minute);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.datebtn){
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        }

        if(id == R.id.timebtn){
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        }
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if(item.getTitle().equals(CommonArt.UPDATE))
//        {
//            showupdatedialog(searchadapter.getRef(item.getOrder()).getKey(),searchadapter.getItem(item.getOrder()));
//        }
//        else if(item.getTitle().equals(CommonArt.DELETE))
//        {
//            deleteportfolio(searchadapter.getRef(item.getOrder()).getKey());
//        }
//
//
//        return super.onContextItemSelected(item);
//    }

//    private void deleteportfolio(String key) {
//        list1.child(key).removeValue();
//    }

    private void showupdatedialog(String key, final ArtistPorfolio item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ArtistAddList.this);
        alertDialog.setTitle("Edit Portfolio");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_artist_layout, null);
        ImgArtistPhoto = (ImageView) add_menu_layout.findViewById(R.id.photoartist) ;
        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDesc = add_menu_layout.findViewById(R.id.edtDescription);
        edtLoc = add_menu_layout.findViewById(R.id.edtLocation);
        dateText=add_menu_layout.findViewById(R.id.dateText);
        timeText=add_menu_layout.findViewById(R.id.timeText);
        datebtn = add_menu_layout.findViewById(R.id.datebtn);
        timebtn = add_menu_layout.findViewById(R.id.timebtn);
        timebtn.setOnClickListener(this);
        datebtn.setOnClickListener(this);

        edtName.setText(item.getName());
        edtDesc.setText(item.getDescription());
        edtLoc.setText(item.getLocation());
        dateText.setText(item.getCurrentDate());
        timeText.setText(item.getCurrentTime());


        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);


        ImgArtistPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= 22) {
                    checkandRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(newartistPorfolio != null)
                {
//                    item.setName(edtName.getText().toString());
//                    item.setDescription(edtDesc.getText().toString());
//                    item.setLocation(edtLoc.getText().toString());
//                    item.getCurrentDate();
//                    item.getCurrentTime();


                    list1.push().setValue(newartistPorfolio);
                    Snackbar.make(rootLayout,"Portfolio"+newartistPorfolio.getName()+"was edited",Snackbar.LENGTH_SHORT)
                            .show();;
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
}

package com.example.imyasfinal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

public class ArtistDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    TextView artist_name, artist_price,artist_description,artist_location,artist_date,artist_time;
    ImageView artist_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String detailId="";
    String clientId="";

    FirebaseDatabase database;
    DatabaseReference details;
    ArtistPorfolio currentPortfolio;
    Request request;
    FirebaseAuth mAuth;

    FloatingActionButton fabart;
    RelativeLayout rootLayout1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String currentTime;
    private String currentDate;
    private String status;
    private static String service;

    TextView dateText, timeText, services;
    MaterialEditText edtLoc,edtrate,edtpeople;
    Button btnUploadart,timebtnart,datebtnart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        details = database.getReference("ArtistPortfolio");

//
        clientId = mAuth.getCurrentUser().getUid();



        artist_location = (TextView) findViewById(R.id.artist_loc);
        artist_date = (TextView) findViewById(R.id.artist_date);
        artist_time = (TextView) findViewById(R.id.artist_time);
        artist_description = (TextView) findViewById(R.id.artist_description);
        artist_name = (TextView) findViewById(R.id.artist_name);
        artist_price = (TextView) findViewById(R.id.artist_price);
        artist_image = (ImageView) findViewById(R.id.img_artist);
        services = (TextView) findViewById(R.id.servicess) ;

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if(getIntent() !=null)

                detailId = getIntent().getStringExtra("ArtistID");
        if(!detailId.isEmpty())
        {
            getDetailArtist(detailId);

        }


    fabart = (FloatingActionButton)findViewById(R.id.fabart);
        fabart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showaddrequest();
        }
    });

    }

    private void showaddrequest() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ArtistDetail.this);
        alertDialog.setTitle("Add Request");
        alertDialog.setMessage("Please fill full information");
        
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_layout, null);
       
        edtLoc = add_menu_layout.findViewById(R.id.edtLocationart);
        edtpeople = add_menu_layout.findViewById(R.id.edtpeopleart);
//        edtrate = add_menu_layout.findViewById(R.id.edtRatesart);
        dateText=add_menu_layout.findViewById(R.id.dateTextart);
        timeText=add_menu_layout.findViewById(R.id.timeTextart);
        services = add_menu_layout.findViewById(R.id.servicess);
        services.setText(service);
        timebtnart = add_menu_layout.findViewById(R.id.timebtnart);
        datebtnart = add_menu_layout.findViewById(R.id.datebtnart);
        timebtnart.setOnClickListener(this);
        datebtnart.setOnClickListener(this);
        btnUploadart = add_menu_layout.findViewById(R.id.btnUploadart);
        
        btnUploadart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrequest();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(currentPortfolio != null)
                {
                    details.push().setValue(currentPortfolio);
                    Snackbar.make(rootLayout1,"New Portfolio"+currentPortfolio.getName()+"was added",Snackbar.LENGTH_SHORT)
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

    private void addrequest() {
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Sending..");
        mDialog.show();

        request = new Request();
            request.setLocation(edtLoc.getText().toString());
            request.setCurrentdate(dateText.getText().toString());
            request.setCurrenttime(timeText.getText().toString());
            request.setPeople(edtpeople.getText().toString());

//            request.setRates(edtrate.getText().toString());
//            request.setStatus();
        String status = "PENDING";

        DatabaseReference getId = FirebaseDatabase.getInstance().getReference("Request");
        String id = getId.push().getKey();
        Request request = new Request(
                edtLoc.getText().toString(),
                edtpeople.getText().toString(),
//                    edtrate.getText().toString(),
                dateText.getText().toString(),
                timeText.getText().toString(),
                status,
                clientId,
                getIntent().getStringExtra("id"),
                id

        );
        FirebaseDatabase.getInstance().getReference("Request")
                .child(id)
                .setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(ArtistDetail.this, "Successfull", Toast.LENGTH_SHORT).show();



                }
            }
        });
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

    private void getDetailArtist(String detailId) {
        details.child(detailId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 currentPortfolio = dataSnapshot.getValue(ArtistPorfolio.class);


                Picasso.get().load(currentPortfolio.getImage()).into(artist_image);

                collapsingToolbarLayout.setTitle(currentPortfolio.getName());

                artist_date.setText("Date: "+currentPortfolio.getCurrentDate());

                artist_time.setText("Artist: "+ currentPortfolio.getCurrentTime());

                artist_location.setText("Location: "+currentPortfolio.getLocation());

                artist_price.setText(currentPortfolio.getPrice());

                artist_name.setText(currentPortfolio.getName());

                service = currentPortfolio.getName();

                artist_description.setText(currentPortfolio.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.datebtnart){
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        }

        if(id == R.id.timebtnart){
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        }
        
    }
}

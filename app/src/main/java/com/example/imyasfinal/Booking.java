package com.example.imyasfinal;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.MenuViewHolder;
import com.example.imyasfinal.ViewHolder.RequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

import static com.example.imyasfinal.Java.myReservation;

public class Booking extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference delete;
    DatabaseReference request,details,update;
    TextView bookloc,booktime,bookdate,bookpeople,bookstatus;
    FirebaseRecyclerAdapter<Request, RequestViewHolder> requestadapter;


    TextView dateText1, timeText1, services1;
    MaterialEditText edtLoc1,edtpeople1;
    Button btnUploadart1,timebtnart1,datebtnart1;
    private static String service;
    ArtistPorfolio currentPortfolio;
    RelativeLayout rootLayout1;
    private String currentTime;
    private String currentDate;
    Request request11;
    String bookingId="";
    String updateId="";
    private static String upid;
    NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        database = FirebaseDatabase.getInstance();
        request=database.getReference("Request");
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_book);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookloc = (TextView) findViewById(R.id.book_location);
        booktime = (TextView) findViewById(R.id.book_date);
        bookdate = (TextView) findViewById(R.id.book_date);
        bookpeople = (TextView) findViewById(R.id.book_people);
//        bookrate = (TextView) findViewById(R.id.book_rate);
        bookstatus=(TextView) findViewById(R.id.book_status);
        notificationManagerCompat = NotificationManagerCompat.from(getApplication());

        menu();


    }

    private void menu() {
        requestadapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>(Request.class,R.layout.pending_layout,RequestViewHolder.class,request) {
            @Override
            protected void populateViewHolder(RequestViewHolder viewHolder, Request model, int position) {
                if(model.getArtistId().equals(mAuth.getCurrentUser().getUid())&&model.getStatus().equals("APPROVE")){
                    PushNotification("Approved","Book Approved");
                }
                else if(model.getArtistId().equals(mAuth.getCurrentUser().getUid())&&model.getStatus().equals("DECLINE")){
                    PushNotification("Decline","decline Book");
                }
                viewHolder.bookloc.setText(model.getLocation());
                viewHolder.bookpeople.setText(model.getPeople());
//                viewHolder.bookrate.setText(model.getRates());
                viewHolder.bookdate.setText(model.getCurrentdate());
                viewHolder.booktime.setText(model.getCurrenttime());
                viewHolder.bookstatus.setText(model.getStatus());
//                viewHolder.bookstatus.setText(model.get);
                upid = requestadapter.getRef(position).getKey();
                final  Request clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        requestadapter.notifyDataSetChanged();
        recyclerView.setAdapter(requestadapter);
    }

    public void PushNotification(String title, String content) {
        Intent notificationIntent = new Intent(this, ArtistHome.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this, myReservation)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .build();

        notificationManagerCompat.notify(3, notification);
    }


// (getActivity(), ShopDashboard.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(),0,notificationIntent,0);
//        Notification notification = new NotificationCompat.Builder(getContext(), myTrades)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle(title)
//                .setContentText(content)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setContentIntent(contentIntent)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(content))
//                .build();
//
//        notificationManagerCompat.notify(3, notification);






    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getTitle().equals(CommonArt.UPDATE))
        {
            update(upid);
        }
        else if (item.getTitle().equals(CommonArt.DELETE))
        {
            delete();
        }

        return super.onContextItemSelected(item);
    }

    private void delete() {
        delete = FirebaseDatabase.getInstance().getReference("Request");
        delete.child(upid).removeValue();
    }

    private void update(String updateId) {
        update =  FirebaseDatabase.getInstance().getReference("Request").child(upid);
        update.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   String date = dataSnapshot.child("currentdate").getValue().toString();
                   String location = dataSnapshot.child("location").getValue().toString();
                   String time = dataSnapshot.child("currenttime").getValue().toString();
                   String people = dataSnapshot.child("people").getValue().toString();
                   dateText1.setText(date);
                   edtLoc1.setText(location);
                   timeText1.setText(time);
                   edtpeople1.setText(people);
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(Booking.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Booking.this);
            alertDialog.setTitle("Edit Request");
            alertDialog.setMessage("Please fill full information");

            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.update_new_layout, null);

            edtLoc1 = add_menu_layout.findViewById(R.id.edtLocationart5);
            edtpeople1 = add_menu_layout.findViewById(R.id.edtpeopleart5);
            dateText1=add_menu_layout.findViewById(R.id.dateTextart5);
            timeText1=add_menu_layout.findViewById(R.id.timeTextart5);
            services1 = add_menu_layout.findViewById(R.id.servicess5);
            services1.setText(service);
            timebtnart1 = add_menu_layout.findViewById(R.id.timebtnart5);
            datebtnart1 = add_menu_layout.findViewById(R.id.datebtnart5);
            timebtnart1.setOnClickListener(this);
            datebtnart1.setOnClickListener(this);
            btnUploadart1 = add_menu_layout.findViewById(R.id.btnUploadart5);

            btnUploadart1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateartrequest();
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
                        update.child(upid).setValue(currentPortfolio);
                        Snackbar.make(rootLayout1,"New Portfolio"+currentPortfolio.getName()+"was updated",Snackbar.LENGTH_SHORT)
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

    private void updateartrequest() {
        request11 = new Request();
        request11.setLocation(edtLoc1.getText().toString());
        request11.setCurrentdate(dateText1.getText().toString());
        request11.setCurrenttime(timeText1.getText().toString());
        request11.setPeople(edtpeople1.getText().toString());

//            request.setRates(edtrate.getText().toString());
//            request.setStatus();
        String status = "PENDING";

        DatabaseReference getId = FirebaseDatabase.getInstance().getReference("Request");
        String id = getId.push().getKey();
        Request request = new Request(
                edtLoc1.getText().toString(),
                edtpeople1.getText().toString(),
                dateText1.getText().toString(),
                timeText1.getText().toString(),
                status,
                bookingId,
                getIntent().getStringExtra("id"),
                id

        );
        FirebaseDatabase.getInstance().getReference("Request")
                .child(upid)
                .setValue(request).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(Booking.this, "Successfull", Toast.LENGTH_SHORT).show();



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
        dateText1.setText(currentDate);

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

        timeText1.setText("Hour: " + hourOfDay + " Minute: " + minute);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.datebtnart5){
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        }

        if(id == R.id.timebtnart5){
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        }


    }
}

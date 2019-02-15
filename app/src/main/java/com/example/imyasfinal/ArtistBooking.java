package com.example.imyasfinal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.ArtistViewBookHolder;
import com.example.imyasfinal.ViewHolder.RequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ArtistBooking extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference getClient;
    private static String name;
    Query request1;
    DatabaseReference approve,decline1,delete,namee;
    Request reques;
    private static String reqid;
    private static String reLid;
    private static String rei;
    private FirebaseAuth mAuth;
    TextView booklocart, booktimeart, bookdateart, bookpeopleart, bookratname, bookstatusart;
    FirebaseRecyclerAdapter<Request, ArtistViewBookHolder> requestartadapter;

    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_booking);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        Toast.makeText(this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
           request1 = database.getReference("Request").orderByChild("artistId").equalTo(mAuth.getCurrentUser().getUid());


        recyclerView = (RecyclerView) findViewById(R.id.recycler_bookart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookratname = (TextView) findViewById(R.id.book_nameart);
        booklocart = (TextView) findViewById(R.id.book_locationart);
        booktimeart = (TextView) findViewById(R.id.book_dateart);
        bookdateart = (TextView) findViewById(R.id.book_dateart);
        bookpeopleart = (TextView) findViewById(R.id.book_peopleart);
//        bookrate = (TextView) findViewById(R.id.book_rate);
        bookstatusart = (TextView) findViewById(R.id.book_statusart);

        artmenu();
    }

    private void artmenu() {
        requestartadapter = new FirebaseRecyclerAdapter<Request, ArtistViewBookHolder>(Request.class, R.layout.pendingartlayout, ArtistViewBookHolder.class, request1) {
            @Override
            protected void populateViewHolder(final ArtistViewBookHolder viewHolder, final Request model, int position) {
//               namee = FirebaseDatabase.getInstance().getReference("Client").child();
                getClient = FirebaseDatabase.getInstance().getReference("Clients");
                getClient.child(model.getClientId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            name = dataSnapshot.child("firstname").getValue().toString().concat(dataSnapshot.child("lastname").getValue().toString());
                            viewHolder.bookratname.setText(name);
                            Toast.makeText(ArtistBooking.this, model.getClientId(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ArtistBooking.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//                viewHolder.bookratname.setText(model.lastname);
                viewHolder.booklocart.setText(model.getLocation());
                viewHolder.bookpeopleart.setText(model.getPeople());
//                viewHolder.bookrate.setText(model.getRates());
                viewHolder.bookdateart.setText(model.getCurrentdate());
                viewHolder.booktimeart.setText(model.getCurrenttime());
                viewHolder.bookstatusart.setText(model.getStatus());
                reqid = model.getRequestId();
                reLid = model.getRequestId();

//                viewHolder.bookstatus.setText(model.get);
                final  Request clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }
        };
        requestartadapter.notifyDataSetChanged();
        recyclerView.setAdapter(requestartadapter);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(CommonArt.ACCEPT))
        {
            showaccept();
        }
        else if(item.getTitle().equals(CommonArt.DECLINE))
        {
            showdecline();

        }
        else if(item.getTitle().equals(CommonArt.DELETE))
        {
            showdelete();

        }

         return super.onContextItemSelected(item);
    }

    private void showdelete() {
        delete = FirebaseDatabase.getInstance().getReference("Request");
        delete.child(reqid).removeValue();
//        delete.child(reid).removeValue();
    }

    private void showaccept() {
        approve = FirebaseDatabase.getInstance().getReference("Request").child(reqid);

        approve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    approve.child("status").setValue("APPROVED");
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ArtistBooking.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showdecline() {
        decline1 = FirebaseDatabase.getInstance().getReference("Request").child(reLid);

        decline1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    decline1.child("status").setValue("DECLINED");
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ArtistBooking.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}


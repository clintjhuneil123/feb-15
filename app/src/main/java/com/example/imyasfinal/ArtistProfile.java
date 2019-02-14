package com.example.imyasfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.ListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ArtistProfile extends AppCompatActivity {
    TextView artist_fname, artist_lastname,artist_description1,artist_email1,getArtist_cont;
    ImageView artist_imag;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String detailId="";
    Query details;
    FirebaseDatabase database;
    DatabaseReference getCurrentUser;

    FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder> adapter;
    Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_profile);

        database = FirebaseDatabase.getInstance();
        details = database.getReference("ArtistPortfolio").orderByChild("artistID").equalTo(getIntent().getStringExtra("ArtistId"));

        getCurrentUser = FirebaseDatabase.getInstance().getReference();
        recyclerView =(RecyclerView) findViewById(R.id.recycler_art);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        artist_fname = (TextView) findViewById(R.id.artist_NAME);
        artist_lastname = (TextView) findViewById(R.id.artist_LNAME);
        artist_description1 = (TextView) findViewById(R.id.artist_DESC);
        artist_email1 = (TextView) findViewById(R.id.artist_EMAIL);
        getArtist_cont = (TextView) findViewById(R.id.artist_NUM);
        artist_imag = (ImageView) findViewById(R.id.img_profile1);


        if(getIntent() !=null)
            detailId = getIntent().getStringExtra("ArtistId");
        if(!detailId.isEmpty())
        {
            getprofile(detailId);
        }
    }

    private void getprofile(String detailId) {

        adapter = new FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder>(ArtistPorfolio.class,R.layout.listitem,ListViewHolder.class,details) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ArtistPorfolio model, int position) {
                viewHolder.list_name.setText(model.getName());
                Picasso.get().load(model.getImage())
//                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.list_image);

                final ArtistPorfolio  local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent artistDetail = new Intent(ArtistProfile.this,ArtistDetail.class);
                        artistDetail.putExtra("ArtistID",adapter.getRef(position).getKey());
                        startActivity(artistDetail);


                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
        getCurrentUser = FirebaseDatabase.getInstance().getReference("Artist");
        getCurrentUser.child(detailId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 artist = dataSnapshot.getValue(Artist.class);

                if(dataSnapshot.exists()){
                    Picasso.get().load(artist.getImage()).into(artist_imag);

                    artist_lastname.setText("Lastname: "+artist.getLastname());

                    artist_fname.setText("Firstname: "+artist.getFirstname());

                    getArtist_cont.setText("Contact Number: "+artist.getContact());

                    artist_email1.setText("Email: "+artist.getEmail());

                    artist_description1.setText("Description: "+artist.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}


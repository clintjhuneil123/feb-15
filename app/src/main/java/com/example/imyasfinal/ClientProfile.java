package com.example.imyasfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ClientProfile extends AppCompatActivity {

    TextView artist_fname1, artist_lastname1,artist_description11,artist_email11,getArtist_cont1;
    ImageView artist_imag1;

    String detailId="";
    Query details;
    FirebaseDatabase database;
    DatabaseReference getCurrentUser;

    FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder> adapter;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);


        database = FirebaseDatabase.getInstance();
        details = database.getReference("ArtistPortfolio").orderByChild("artistID").equalTo(getIntent().getStringExtra("ArtistId"));

        getCurrentUser = FirebaseDatabase.getInstance().getReference();
        artist_fname1 = (TextView) findViewById(R.id.artist_NAME1);
        artist_lastname1 = (TextView) findViewById(R.id.artist_LNAME1);
        artist_email11 = (TextView) findViewById(R.id.artist_EMAIL1);
        getArtist_cont1 = (TextView) findViewById(R.id.artist_NUM1);
        artist_imag1 = (ImageView) findViewById(R.id.img_profile11);

            getprofile();

    }

    private void getprofile() {
        getCurrentUser = FirebaseDatabase.getInstance().getReference("Client");
        getCurrentUser.child(detailId).addValueEventListener(new ValueEventListener() {
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


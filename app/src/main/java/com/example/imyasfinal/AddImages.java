package com.example.imyasfinal;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AddImages extends AppCompatActivity {


    TextView artist_name1, artist_price1,artist_description1,artist_location1,artist_date1,artist_time1;
    ImageView artist_image1;
    CollapsingToolbarLayout collapsingToolbarLayout1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String imagesId="";

    FirebaseDatabase database;
    DatabaseReference images;


    ArtistPorfolio currentPortfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);


        database = FirebaseDatabase.getInstance();
        images = database.getReference("ArtistPortfolio");

//        numberButton1 = (ElegantNumberButton) findViewById(R.id.number_button);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_art);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        artist_location1 = (TextView) findViewById(R.id.artist_loc1);
        artist_date1 = (TextView) findViewById(R.id.artist_date1);
        artist_time1 = (TextView) findViewById(R.id.artist_time1);
        artist_description1 = (TextView) findViewById(R.id.artist_description1);
        artist_name1 = (TextView) findViewById(R.id.artist_name1);
        artist_price1 = (TextView) findViewById(R.id.artist_price1);
        artist_image1 = (ImageView) findViewById(R.id.img_artist1);

        collapsingToolbarLayout1 = (CollapsingToolbarLayout) findViewById(R.id.collapsing1);
        collapsingToolbarLayout1.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout1.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if(getIntent() !=null)
            imagesId = getIntent().getStringExtra("ArtistID");
        if(!imagesId.isEmpty())
        {
            getDetailArtist(imagesId);
        }
    }

    private void getDetailArtist(String detailId) {
        images.child(detailId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPortfolio = dataSnapshot.getValue(ArtistPorfolio.class);


                Picasso.get().load(currentPortfolio.getImage()).into(artist_image1);

                collapsingToolbarLayout1.setTitle(currentPortfolio.getName());

                artist_date1.setText("Date: "+currentPortfolio.getCurrentDate());

                artist_time1.setText("Time: " +currentPortfolio.getCurrentTime());

                artist_location1.setText("Location: "+currentPortfolio.getLocation());

                artist_price1.setText(currentPortfolio.getPrice());

                artist_name1.setText(currentPortfolio.getName());

                artist_description1.setText(currentPortfolio.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.example.imyasfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.MenuViewHolder;
import com.example.imyasfinal.ViewHolder.RequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Booking extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    TextView bookloc,booktime,bookdate,bookpeople,bookrate,bookstatus;
    FirebaseRecyclerAdapter<Request, RequestViewHolder> requestadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        database = FirebaseDatabase.getInstance();
        request=database.getReference("Request");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_book);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookloc = (TextView) findViewById(R.id.book_location);
        booktime = (TextView) findViewById(R.id.book_date);
        bookdate = (TextView) findViewById(R.id.book_date);
        bookpeople = (TextView) findViewById(R.id.book_people);
        bookrate = (TextView) findViewById(R.id.book_rate);
        bookstatus=(TextView) findViewById(R.id.book_status);

        menu();





    }

    private void menu() {
        requestadapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>(Request.class,R.layout.pending_layout,RequestViewHolder.class,request) {
            @Override
            protected void populateViewHolder(RequestViewHolder viewHolder, Request model, int position) {
                viewHolder.bookloc.setText(model.getLocation());
                viewHolder.booktime.setText(model.getCurrenttime());
                viewHolder.bookdate.setText(model.getCurrentdate());
                viewHolder.bookpeople.setText(model.getPeople());
                viewHolder.bookstatus.setText(model.getStatus());
//                viewHolder.bookstatus.setText(model.get);
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
}

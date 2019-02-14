package com.example.imyasfinal.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.R;

import org.w3c.dom.Text;

public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView bookloc,booktime,bookdate,bookpeople,bookrate,bookstatus;

    private ItemClickListener itemClickListener;

    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);

        bookloc = (TextView) itemView.findViewById(R.id.book_location);
        booktime = (TextView) itemView.findViewById(R.id.book_time);
        bookdate = (TextView) itemView.findViewById(R.id.book_date);
        bookpeople = (TextView) itemView.findViewById(R.id.book_people);
        bookrate = (TextView) itemView.findViewById(R.id.book_rate);
        bookstatus = (TextView) itemView.findViewById(R.id.book_status);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}

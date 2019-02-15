package com.example.imyasfinal.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.R;

import org.w3c.dom.Text;

public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {

    public TextView bookloc,booktime,bookdate,bookpeople,bookname,bookstatus;

    private ItemClickListener itemClickListener;

    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);

        bookloc = (TextView) itemView.findViewById(R.id.book_location);
        bookpeople = (TextView) itemView.findViewById(R.id.book_people);
//        bookrate = (TextView) itemView.findViewById(R.id.book_rate);
        bookdate = (TextView) itemView.findViewById(R.id.book_date);
        booktime = (TextView) itemView.findViewById(R.id.book_time);
        bookstatus = (TextView) itemView.findViewById(R.id.book_status);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), CommonArt.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), CommonArt.DELETE);
    }
}

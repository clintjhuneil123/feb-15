package com.example.imyasfinal.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.R;

public class ArtistViewBookHolder  extends RecyclerView.ViewHolder implements View.OnClickListener ,
        View.OnCreateContextMenuListener{

    public TextView booklocart,booktimeart,bookdateart,bookpeopleart,bookratname,bookstatusart;

    private ItemClickListener itemClickListener;

    public ArtistViewBookHolder(@NonNull View itemView) {
        super(itemView);

        booklocart = (TextView) itemView.findViewById(R.id.book_locationart);
        bookpeopleart = (TextView) itemView.findViewById(R.id.book_peopleart);
        bookratname = (TextView) itemView.findViewById(R.id.book_nameart);
        bookdateart = (TextView) itemView.findViewById(R.id.book_dateart);
        booktimeart = (TextView) itemView.findViewById(R.id.book_timeart);
        bookstatusart = (TextView) itemView.findViewById(R.id.book_statusart);

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
        contextMenu.add(0,0,getAdapterPosition(), CommonArt.ACCEPT);
        contextMenu.add(0,1,getAdapterPosition(), CommonArt.DECLINE);
        contextMenu.add(0,2,getAdapterPosition(), CommonArt.DELETE);
    }
}

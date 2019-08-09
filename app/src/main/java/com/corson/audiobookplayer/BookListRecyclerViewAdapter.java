package com.corson.audiobookplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookListRecyclerViewAdapter extends RecyclerView.Adapter<BookListRecyclerViewAdapter.ViewHolder> {

    private List<String> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    BookListRecyclerViewAdapter(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String key = data.get(position);
        holder.textViewKey.setText(key);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewKey;

        ViewHolder(View itemView) {
            super(itemView);
            textViewKey = itemView.findViewById(R.id.recycler_view_row_book_key);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

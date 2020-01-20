package com.corson.audiobookplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.corson.audiobookplayer.model.Audiobook;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BookListRecyclerViewAdapter extends RecyclerView.Adapter<BookListRecyclerViewAdapter.ViewHolder> {

    private List<Audiobook> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    BookListRecyclerViewAdapter(Context context, List<Audiobook> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        System.out.println("Created Adapter. book titles:");
        for (Audiobook a : data) {
            System.out.println(a.getTitle());
        }
    }

    public void clearAll() {
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
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



    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Audiobook book = data.get(position);
        holder.textViewKey.setText(book.getTitle());
        System.out.println("Binding book, title: " + book.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

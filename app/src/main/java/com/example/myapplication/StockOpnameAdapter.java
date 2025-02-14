package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.model.StockOpnameData;
import java.util.List;

public class StockOpnameAdapter extends RecyclerView.Adapter<StockOpnameAdapter.ViewHolder> {

    private List<StockOpnameData> stockOpnames;
    private OnItemClickListener listener;

    // Interface untuk menangani item klik
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter untuk listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Konstruktor untuk menerima data
    public StockOpnameAdapter(List<StockOpnameData> stockOpnames) {
        this.stockOpnames = stockOpnames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout item untuk setiap elemen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_opname, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Ambil data untuk setiap item
        StockOpnameData stockOpname = stockOpnames.get(position);
        holder.noSO.setText(stockOpname.getNoSO());
        holder.tgl.setText(stockOpname.getTgl());

        // Set listener klik pada item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);  // Panggil listener ketika item diklik
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockOpnames.size();  // Mengembalikan jumlah item dalam list
    }

    // ViewHolder untuk setiap item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noSO, tgl;

        public ViewHolder(View itemView) {
            super(itemView);
            noSO = itemView.findViewById(R.id.noSO);  // Mendapatkan referensi dari TextView noSO
            tgl = itemView.findViewById(R.id.tgl);    // Mendapatkan referensi dari TextView tgl
        }
    }
}

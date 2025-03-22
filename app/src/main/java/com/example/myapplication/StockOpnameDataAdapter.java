package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.StockOpnameDataByNoSO;

import java.util.List;

public class StockOpnameDataAdapter extends RecyclerView.Adapter<StockOpnameDataAdapter.ViewHolder> {

    private List<StockOpnameDataByNoSO> dataList;
    private OnItemClickListener listener;

    // Interface untuk menangani item klik
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter untuk listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public StockOpnameDataAdapter(List<StockOpnameDataByNoSO> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock_opname_data, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockOpnameDataByNoSO item = dataList.get(position);
        holder.noLabelTextView.setText(item.getNoLabel()); // Set NoLabel
        holder.idLokasiTextView.setText(item.getIdLokasi()); // Set IdLokasi

        // Set background color bergantian berdasarkan posisi item
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));  // Warna putih untuk posisi genap
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.background_cream));  // Warna abu-abu untuk posisi ganjil
        }

        // Set listener klik pada item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);  // Panggil listener ketika item diklik
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noLabelTextView;
        TextView idLokasiTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            noLabelTextView = itemView.findViewById(R.id.noLabelTextView);
            idLokasiTextView = itemView.findViewById(R.id.idLokasiTextView);
        }
    }
}

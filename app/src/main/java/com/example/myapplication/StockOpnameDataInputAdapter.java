package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;

import java.util.List;

public class StockOpnameDataInputAdapter extends RecyclerView.Adapter<StockOpnameDataInputAdapter.ViewHolder> {

    private List<StockOpnameDataInputByNoSO> dataList;
    private OnDeleteConfirmationListener deleteListener;  // Menyimpan listener untuk menghubungkan dengan activity
    private OnItemClickListener listener;

    // Interface untuk menangani item klik
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter untuk listener
    public void setOnItemClickListener(StockOpnameDataInputAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // Konstruktor untuk menerima listener
    public StockOpnameDataInputAdapter(List<StockOpnameDataInputByNoSO> dataList, OnDeleteConfirmationListener deleteListener) {
        this.dataList = dataList;
        this.deleteListener = deleteListener;  // Menyimpan listener
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock_opname_data_input, parent, false); // Layout item
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockOpnameDataInputByNoSO item = dataList.get(position);

        holder.noLabelInput.setText(item.getNoLabelInput());
        holder.idLokasiInput.setText(item.getIdLokasiInput());
        holder.userIDInput.setText(item.getUserIdInput());

        // Menambahkan listener untuk klik pada itemView
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);  // Panggil listener ketika item diklik
            }
        });

        // Menambahkan listener untuk long press pada itemView
        holder.itemView.setOnLongClickListener(v -> {
            // Panggil listener untuk menampilkan dialog penghapusan
            deleteListener.onDeleteConfirmation(item, position);
            return true;  // Mengindikasikan long click sudah ditangani
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noLabelInput, idLokasiInput, userIDInput;

        public ViewHolder(View itemView) {
            super(itemView);
            noLabelInput = itemView.findViewById(R.id.noLabelInput);
            idLokasiInput = itemView.findViewById(R.id.idLokasiInput);
            userIDInput = itemView.findViewById(R.id.userIDInput);
        }
    }

    // Interface untuk menangani penghapusan item
    public interface OnDeleteConfirmationListener {
        void onDeleteConfirmation(StockOpnameDataInputByNoSO item, int position);
    }
}

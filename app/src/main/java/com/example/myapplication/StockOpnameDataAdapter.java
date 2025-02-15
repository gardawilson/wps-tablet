package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.model.StockOpnameData;
import com.example.myapplication.model.StockOpnameDataByNoSO;

import java.util.List;

public class StockOpnameDataAdapter extends RecyclerView.Adapter<StockOpnameDataAdapter.ViewHolder> {

    private List<StockOpnameDataByNoSO> dataList;

    public StockOpnameDataAdapter(List<StockOpnameDataByNoSO> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock_opname_data, parent, false); // Ganti dengan layout item yang sesuai
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockOpnameDataByNoSO item = dataList.get(position);
        holder.noLabelTextView.setText(item.getNoLabel()); // Set NoLabel
        holder.idLokasiTextView.setText(item.getIdLokasi()); // Set IdLokasi
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
            noLabelTextView = itemView.findViewById(R.id.noLabelTextView); // Sesuaikan dengan ID di layout item
            idLokasiTextView = itemView.findViewById(R.id.idLokasiTextView); // Sesuaikan dengan ID di layout item
        }
    }
}

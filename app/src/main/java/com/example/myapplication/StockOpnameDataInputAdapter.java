package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;

import java.util.List;

public class StockOpnameDataInputAdapter extends RecyclerView.Adapter<StockOpnameDataInputAdapter.ViewHolder> {

    private List<StockOpnameDataInputByNoSO> dataList;

    public StockOpnameDataInputAdapter(List<StockOpnameDataInputByNoSO> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock_opname_data_input, parent, false); // Ganti dengan layout item yang sesuai
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockOpnameDataInputByNoSO item = dataList.get(position);
        holder.noLabelInput.setText(item.getNoLabel());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noLabelInput;

        public ViewHolder(View itemView) {
            super(itemView);
            noLabelInput = itemView.findViewById(R.id.noLabelInput); // Sesuaikan dengan ID di layout item
        }
    }
}

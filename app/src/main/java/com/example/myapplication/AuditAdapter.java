package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.AuditItem;
import com.example.myapplication.model.AuditRequestGroup;
import com.example.myapplication.utils.AuditDisplayFormatter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.AuditViewHolder> {

    public interface OnAuditClickListener {
        void onAuditClick(AuditRequestGroup item);
    }

    private final List<AuditRequestGroup> items = new ArrayList<>();
    private final OnAuditClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private String selectedRequestId = null;

    public AuditAdapter(OnAuditClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<AuditRequestGroup> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        selectedPosition = findSelectedPositionByRequestId(selectedRequestId);
        notifyDataSetChanged();
    }

    public void setSelectedRequestId(String requestId) {
        selectedRequestId = requestId;
        int newPos = findSelectedPositionByRequestId(selectedRequestId);
        if (newPos != selectedPosition) {
            int oldPos = selectedPosition;
            selectedPosition = newPos;
            if (oldPos != RecyclerView.NO_POSITION) notifyItemChanged(oldPos);
            if (selectedPosition != RecyclerView.NO_POSITION) notifyItemChanged(selectedPosition);
        }
    }

    @NonNull
    @Override
    public AuditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit, parent, false);
        return new AuditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuditViewHolder holder, int position) {
        AuditRequestGroup item = items.get(position);

        holder.tvTime.setText(item.getTimeSummary());
        String action = oneLine(item.getActionSummary(), 30).toUpperCase();
        holder.tvActionBadge.setText(action);
        holder.tvActionBadge.setBackground(ContextCompat.getDrawable(
                holder.itemView.getContext(),
                getActionBadgeRes(action)
        ));
        holder.tvActor.setText(item.getActorSummary());
        holder.tvRequestId.setText(oneLine(item.getRequestId(), 120));
        holder.tvPk.setText(oneLine(resolvePkSummary(item), 120));
        boolean isSelected = position == selectedPosition;
        holder.containerRequest.setBackground(ContextCompat.getDrawable(
                holder.itemView.getContext(),
                isSelected ? R.drawable.bg_request_item_selected : R.drawable.bg_request_item_default
        ));

        holder.itemView.setOnClickListener(v -> {
            int prev = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < items.size()) {
                selectedRequestId = items.get(selectedPosition).getRequestId();
            }
            if (prev != RecyclerView.NO_POSITION) notifyItemChanged(prev);
            if (selectedPosition != RecyclerView.NO_POSITION) notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onAuditClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static String oneLine(String value, int maxLength) {
        if (value == null) return "-";
        String normalized = value.replace("\n", " ").replace("\r", " ").trim();
        if (normalized.length() <= maxLength) return normalized;
        return normalized.substring(0, maxLength) + "...";
    }

    private int getActionBadgeRes(String action) {
        if (action == null) return R.drawable.bg_badge_action_default;
        if (action.startsWith("CREATE")) return R.drawable.bg_badge_action_create;
        if (action.startsWith("EDIT")) return R.drawable.bg_badge_action_edit;
        if (action.startsWith("PRINT")) return R.drawable.bg_badge_action_print;
        if (action.startsWith("DELETE")) return R.drawable.bg_badge_action_delete;
        if (action.startsWith("CONSUME")) return R.drawable.bg_badge_action_consume;
        if (action.startsWith("UNCONSUME")) return R.drawable.bg_badge_action_unconsume;
        return R.drawable.bg_badge_action_default;
    }

    private String resolvePkSummary(AuditRequestGroup group) {
        if (group == null || group.getItems() == null || group.getItems().isEmpty()) return "-";
        Set<String> values = new LinkedHashSet<>();
        for (AuditItem item : group.getItems()) {
            Map<String, String> pkMap = AuditDisplayFormatter.toFieldMap(item.getPk());
            for (String value : pkMap.values()) {
                if (value != null && !value.trim().isEmpty() && !"-".equals(value.trim())) {
                    values.add(value.trim());
                }
            }
        }
        if (values.isEmpty()) return "-";
        return String.join(", ", values);
    }

    private int findSelectedPositionByRequestId(String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) return RecyclerView.NO_POSITION;
        for (int i = 0; i < items.size(); i++) {
            String id = items.get(i).getRequestId();
            if (requestId.equals(id)) return i;
        }
        return RecyclerView.NO_POSITION;
    }

    static class AuditViewHolder extends RecyclerView.ViewHolder {
        LinearLayout containerRequest;
        TextView tvActionBadge;
        TextView tvTime;
        TextView tvActor;
        TextView tvRequestId;
        TextView tvPk;

        AuditViewHolder(@NonNull View itemView) {
            super(itemView);
            containerRequest = itemView.findViewById(R.id.containerRequest);
            tvActionBadge = itemView.findViewById(R.id.tvAuditActionBadge);
            tvTime = itemView.findViewById(R.id.tvAuditTime);
            tvActor = itemView.findViewById(R.id.tvAuditActor);
            tvRequestId = itemView.findViewById(R.id.tvAuditRequestId);
            tvPk = itemView.findViewById(R.id.tvAuditPk);
        }
    }
}

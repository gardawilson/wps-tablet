package com.example.myapplication.sidebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.utils.SharedPrefUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SidebarMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnLeafClickListener {
        void onLeafClicked(SidebarNavItem item);
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TOP = 1;
    private static final int TYPE_CHILD = 2;

    private static class Row {
        final int type;
        final String headerTitle;
        final SidebarNavItem item;

        private Row(int type, String headerTitle, SidebarNavItem item) {
            this.type = type;
            this.headerTitle = headerTitle;
            this.item = item;
        }

        static Row header(String title) {
            return new Row(TYPE_HEADER, title, null);
        }

        static Row top(SidebarNavItem item) {
            return new Row(TYPE_TOP, null, item);
        }

        static Row child(SidebarNavItem item) {
            return new Row(TYPE_CHILD, null, item);
        }
    }

    private final Context context;
    private final List<SidebarSection> sections;
    private final List<Row> flatRows = new ArrayList<>();
    private final Set<Integer> expandedParentIds = new HashSet<>();
    private int selectedLeafId = -1;
    private boolean collapsedMode = false;
    private OnLeafClickListener listener;

    public SidebarMenuAdapter(Context context, List<SidebarSection> sections) {
        this.context = context;
        this.sections = sections;
        rebuildRows();
    }

    public void setOnLeafClickListener(OnLeafClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedLeafId(int id) {
        int previousPosition = findTopOrChildPositionById(selectedLeafId);
        selectedLeafId = id;
        int newPosition = findTopOrChildPositionById(selectedLeafId);
        if (previousPosition >= 0) {
            notifyItemChanged(previousPosition);
        }
        if (newPosition >= 0) {
            notifyItemChanged(newPosition);
        }
    }

    /**
     * Membuka (expand) parent yang memiliki child dengan id {@code leafId},
     * supaya menu yang sedang aktif langsung terlihat saat sidebar dirender.
     */
    public void expandParentOf(int leafId) {
        if (leafId < 0) {
            return;
        }
        for (SidebarSection section : sections) {
            for (SidebarNavItem item : section.items) {
                if (!item.hasChildren()) {
                    continue;
                }
                for (SidebarNavItem child : item.children) {
                    if (child.id == leafId && expandedParentIds.add(item.id)) {
                        rebuildRows();
                        notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    public void setCollapsedMode(boolean collapsed) {
        if (this.collapsedMode == collapsed) {
            return;
        }
        this.collapsedMode = collapsed;
        rebuildRows();
        notifyDataSetChanged();
    }

    private int findTopOrChildPositionById(int id) {
        for (int i = 0; i < flatRows.size(); i++) {
            Row row = flatRows.get(i);
            if ((row.type == TYPE_TOP || row.type == TYPE_CHILD) && row.item.id == id) {
                return i;
            }
        }
        return -1;
    }

    private void rebuildRows() {
        flatRows.clear();
        for (SidebarSection section : sections) {
            if (!collapsedMode) {
                flatRows.add(Row.header(section.title));
            }
            for (SidebarNavItem item : section.items) {
                flatRows.add(Row.top(item));
                if (!collapsedMode && item.hasChildren() && expandedParentIds.contains(item.id)) {
                    for (SidebarNavItem child : item.children) {
                        flatRows.add(Row.child(child));
                    }
                }
            }
        }
    }

    private void toggleParent(int position) {
        if (collapsedMode) {
            return;
        }
        Row row = flatRows.get(position);
        if (row.type != TYPE_TOP || !row.item.hasChildren()) {
            return;
        }
        SidebarNavItem parent = row.item;
        boolean expanding = !expandedParentIds.contains(parent.id);
        if (expanding) {
            expandedParentIds.add(parent.id);
            int insertPos = position + 1;
            for (int i = 0; i < parent.children.size(); i++) {
                flatRows.add(insertPos + i, Row.child(parent.children.get(i)));
            }
            notifyItemChanged(position);
            notifyItemRangeInserted(insertPos, parent.children.size());
        } else {
            expandedParentIds.remove(parent.id);
            int removePos = position + 1;
            for (int i = 0; i < parent.children.size(); i++) {
                flatRows.remove(removePos);
            }
            notifyItemChanged(position);
            notifyItemRangeRemoved(removePos, parent.children.size());
        }
    }

    private boolean hasPermission(String permissionKey) {
        if (permissionKey == null) {
            return true;
        }
        List<String> userPermissions = SharedPrefUtils.getPermissions(context);
        return userPermissions != null && userPermissions.contains(permissionKey);
    }

    @Override
    public int getItemViewType(int position) {
        return flatRows.get(position).type;
    }

    @Override
    public int getItemCount() {
        return flatRows.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(inflater.inflate(R.layout.item_sidebar_header, parent, false));
        }
        if (viewType == TYPE_CHILD) {
            return new ChildViewHolder(inflater.inflate(R.layout.item_sidebar_child, parent, false));
        }
        return new TopViewHolder(inflater.inflate(R.layout.item_sidebar_top, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Row row = flatRows.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).title.setText(row.headerTitle);
            return;
        }
        if (holder instanceof ChildViewHolder) {
            bindChild((ChildViewHolder) holder, row.item);
            return;
        }
        bindTop((TopViewHolder) holder, row.item, position);
    }

    private void bindTop(TopViewHolder holder, SidebarNavItem item, int position) {
        holder.label.setText(collapsedMode ? "" : item.label);
        if (item.iconRes != 0) {
            holder.icon.setImageResource(item.iconRes);
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        boolean expandable = item.hasChildren() && !collapsedMode;
        holder.chevron.setVisibility(expandable ? View.VISIBLE : View.GONE);
        if (expandable) {
            holder.chevron.setRotation(expandedParentIds.contains(item.id) ? 180f : 0f);
        }

        boolean selected = !item.hasChildren() && item.id == selectedLeafId;
        holder.itemView.setBackgroundResource(
                selected ? R.drawable.sidebar_row_selected_background : R.drawable.sidebar_row_default_background);

        boolean allowed = hasPermission(item.permissionKey);
        holder.itemView.setAlpha(allowed ? 1f : 0.5f);
        holder.itemView.setEnabled(allowed);

        holder.itemView.setOnClickListener(v -> {
            if (!allowed) {
                return;
            }
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }
            if (item.hasChildren()) {
                toggleParent(currentPosition);
            } else if (listener != null) {
                listener.onLeafClicked(item);
            }
        });
    }

    private void bindChild(ChildViewHolder holder, SidebarNavItem item) {
        holder.label.setText(item.label);

        if (item.iconRes != 0) {
            holder.icon.setImageResource(item.iconRes);
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        boolean selected = item.id == selectedLeafId;
        holder.itemView.setBackgroundResource(
                selected ? R.drawable.sidebar_row_selected_background : R.drawable.sidebar_row_default_background);

        boolean allowed = hasPermission(item.permissionKey);
        holder.itemView.setAlpha(allowed ? 1f : 0.5f);
        holder.itemView.setEnabled(allowed);

        holder.itemView.setOnClickListener(v -> {
            if (allowed && listener != null) {
                listener.onLeafClicked(item);
            }
        });
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        final TextView title;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sectionHeaderText);
        }
    }

    private static class TopViewHolder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView label;
        final ImageView chevron;

        TopViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.topIcon);
            label = itemView.findViewById(R.id.topLabel);
            chevron = itemView.findViewById(R.id.topChevron);
        }
    }

    private static class ChildViewHolder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView label;

        ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.childIcon);
            label = itemView.findViewById(R.id.childLabel);
        }
    }
}

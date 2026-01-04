package com.iconpacksaver.oneui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppIconAdapter extends RecyclerView.Adapter<AppIconAdapter.ViewHolder> {

    private List<AppIconInfo> appList;
    private OnItemDeleteListener deleteListener;

    public interface OnItemDeleteListener {
        void onDelete(int position);
    }

    public AppIconAdapter() {
        this.appList = new ArrayList<>();
    }

    public void setDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setAppList(List<AppIconInfo> appList) {
        this.appList = appList;
        notifyDataSetChanged();
    }

    public void addApp(AppIconInfo appInfo) {
        appList.add(appInfo);
        notifyItemInserted(appList.size() - 1);
    }

    public void removeApp(int position) {
        if (position >= 0 && position < appList.size()) {
            appList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public List<AppIconInfo> getAppList() {
        return appList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppIconInfo app = appList.get(position);
        holder.appName.setText(app.getAppName());
        holder.componentName.setText(app.getComponentName());
        
        if (app.getIconBitmap() != null) {
            holder.appIcon.setImageBitmap(app.getIconBitmap());
        }

        holder.deleteBtn.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView componentName;
        ImageButton deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            componentName = itemView.findViewById(R.id.componentName);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}

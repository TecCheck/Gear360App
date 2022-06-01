package de.teccheck.gear360app.ui;

import android.graphics.drawable.Drawable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.teccheck.gear360app.R;

public class BaseMenuEntryAdapter extends RecyclerView.Adapter<BaseMenuEntryAdapter.ViewHolder> {

    public static final String TAG = "G360_" + BaseMenuEntryAdapter.class.getSimpleName();

    public ArrayList<BaseMenuEntryAdapter.EntryItem> items = new ArrayList<>();

    public BaseMenuEntryAdapter() {

    }

    public void addItem(String label, String description, Drawable icon){
        items.add(new BaseMenuEntryAdapter.EntryItem(label, description, icon));
    }

    @Override
    public void onBindViewHolder(BaseMenuEntryAdapter.ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder " + position);

        CardView cardView = holder.cardView;
        TextView label = cardView.findViewById(R.id.label);
        TextView description = cardView.findViewById(R.id.description);
        ImageView icon = cardView.findViewById(R.id.icon);
        BaseMenuEntryAdapter.EntryItem item = items.get(position);

        if(item.label != null){
            label.setVisibility(View.VISIBLE);
            label.setText(item.label);
        }else {
            label.setVisibility(View.GONE);
        }
        if(item.description != null){
            description.setVisibility(View.VISIBLE);
            description.setText(item.description);
        }else {
            description.setVisibility(View.GONE);
        }
        if(item.icon != null){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(item.icon);
        }else {
            icon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public BaseMenuEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.base_menu_entry_item, parent, false);
        return new BaseMenuEntryAdapter.ViewHolder(cardView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    class EntryItem{
        public EntryItem(String label, String description, Drawable icon){
            this.label = label;
            this.description = description;
            this.icon = icon;
        }
        public String label;
        public String description;
        public Drawable icon;
    }

    
}
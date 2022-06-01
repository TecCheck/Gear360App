package de.teccheck.gear360app.ui;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import de.sematre.fastreflection.FastReflection;
import de.teccheck.gear360app.Gear360Settings;
import de.teccheck.gear360app.R;

public class ConfigEntryAdapter extends RecyclerView.Adapter<ConfigEntryAdapter.ViewHolder> {

    public static final String TAG = "G360_" + ConfigEntryAdapter.class.getSimpleName();

    ArrayList<Field> fields = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public ConfigEntryAdapter(){
        fields = new ArrayList(Arrays.asList(Gear360Settings.class.getFields()));
        ArrayList<Field> filteredFields = new ArrayList<>();
        for (Field f : fields){
            if(f.getType().equals(String.class) || f.getType().equals(Gear360Settings.Config.class)){
                filteredFields.add(f);
            }
        }
        fields = filteredFields;
        Log.d(TAG, "fields.size()" + fields.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.config_entry_item, parent, false);
        return new ViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder " + position);

        Field field = fields.get(position);
        if(field != null){
            CardView cardView = holder.cardView;

            TextView type = cardView.findViewById(R.id.config_type);
            TextView name = cardView.findViewById(R.id.config_name);
            TextView defaultValue = cardView.findViewById(R.id.config_default_value);
            TextView values = cardView.findViewById(R.id.config_values);

            name.setText(field.getName());
            if(field.getType().equals(String.class)){
                type.setText("String");
                defaultValue.setVisibility(View.VISIBLE);
                values.setVisibility(View.GONE);
                String s = "Not found";
                try {
                    s = (String) FastReflection.getFieldValue(new Gear360Settings(), field.getName());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                defaultValue.setText(s);
            }else if(field.getType().equals(Gear360Settings.Config.class)){
                type.setText("Config");
                defaultValue.setVisibility(View.VISIBLE);
                values.setVisibility(View.VISIBLE);
                Gear360Settings.Config c = null;
                try {
                    c = (Gear360Settings.Config) FastReflection.getFieldValue(new Gear360Settings(), field.getName());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(c != null){
                    Log.d(TAG, c.getAsString());
                    defaultValue.setText(c.getDefaultValue());
                    values.setText(c.getAsString());
                }else {

                    values.setVisibility(View.GONE);
                    defaultValue.setText("Null");
                }

            }else{
                type.setText("None");
                name.setText("Not Found");
                defaultValue.setVisibility(View.GONE);
                values.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }
}
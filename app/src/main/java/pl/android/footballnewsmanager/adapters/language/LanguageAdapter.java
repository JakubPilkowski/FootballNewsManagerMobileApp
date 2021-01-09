package pl.android.footballnewsmanager.adapters.language;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.footballnewsmanager.R;

import java.util.List;

public class LanguageAdapter extends ArrayAdapter<String> {

    private List<Drawable> images;
    private List<String> names;

    public LanguageAdapter(@NonNull Context context, int resource, List<String> names, List<Drawable> images) {
        super(context, resource, names);
        this.names = names;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        view.setText(names.get(position));
        view.setCompoundDrawablesWithIntrinsicBounds(images.get(position), null, null, null);
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        view.setText(names.get(position));
        view.setTextColor(getContext().getResources().getColor(R.color.colorTextPrimary));
        view.setCompoundDrawablesWithIntrinsicBounds(images.get(position), null,null,null);
        return view;
    }
}

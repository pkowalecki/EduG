package pl.kowalecki.edug.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Model.AdditionalMenu;
import pl.kowalecki.edug.R;

public class CollapsibleMenuAdapter extends ArrayAdapter<AdditionalMenu> {
    private ArrayList<AdditionalMenu>  additionalMenuArrayList;

    public CollapsibleMenuAdapter(@NonNull Context context, int resource, ArrayList<AdditionalMenu> additionalMenuArrayList) {
        super(context, resource, additionalMenuArrayList);
        this.additionalMenuArrayList = additionalMenuArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int index = position;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collapsible_menu_custom_listview,
                    parent, false);
        }

        ImageView menuImage = convertView.findViewById(R.id.collapsible_menu_image);
        TextView menuText = convertView.findViewById(R.id.collapsible_menu_text);

        menuImage.setImageResource(additionalMenuArrayList.get(position).getMenuElementImage());
        menuText.setText(additionalMenuArrayList.get(position).getMenuElement());

        return convertView;
    }
}

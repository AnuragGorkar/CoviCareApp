package com.example.covicareapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.covicareapp.R;
import com.example.covicareapp.interfaces.OnHomePageClickListener;
import com.example.covicareapp.models.HomePageButton;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<HomePageButton> {

    OnHomePageClickListener onHomePageButtonClickListener;

    public GridViewAdapter(@NonNull Context context, @NonNull ArrayList<HomePageButton> homePageButtons, OnHomePageClickListener onHomePageButtonClick) {
        super(context, 0, homePageButtons);
        this.onHomePageButtonClickListener = onHomePageButtonClick;
    }





    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listItemView = convertView;
        if (listItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        HomePageButton homePageButton = getItem(position);
        ImageView icon = listItemView.findViewById(R.id.itemIcon);
        icon.setImageResource(homePageButton.getImageId());

        TextView buttonName = listItemView.findViewById(R.id.itemName);
        buttonName.setText(homePageButton.getButtonName());

        listItemView.setOnClickListener(view -> {
            onHomePageButtonClickListener.onItemClick(getItem(position));
        });

        return listItemView;
    }
}

package com.example.android.grabyournews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ravi on 6/8/2017.
 */

public class newsAdapter extends ArrayAdapter<news> {

    /**
     * construct {@Link newsAdspter}
     *
     * @param context
     * @param newsArrayList
     */
    public newsAdapter(Context context, ArrayList<news> newsArrayList) {
        super(context, 0, newsArrayList);
    }

    /**
     * returns a list item view that displays news
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if there is an existing list item view i.e... convertview
        //otherwise if convertview is null ,inflate a news_list_item layout
        news News = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }
        //find the title textview with ID title
        TextView title = (TextView) convertView.findViewById(R.id.title);
        //title name at the given position in the list of news_list_item
        title.setText(News.getTitle());

        //find the author textview with author title
        TextView author = (TextView) convertView.findViewById(R.id.author);
        //author name at the given position in the list of news_list_item
        author.setText(News.getAuthor());

        //find the section textview with sectionname title
        TextView sectionName = (TextView) convertView.findViewById(R.id.sectionName);
        //section name at the given position in the list of news_list_item
        sectionName.setText(News.getSectionName());

        //returning the convertview(listview)show data
        return convertView;
    }
}

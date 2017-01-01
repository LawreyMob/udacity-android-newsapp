package com.lawrey.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lawrey on 1/1/17.
 */

public class NewsAdapter extends ArrayAdapter<News>  {

    public NewsAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_text_view);
        TextView publishedDateTextView = (TextView) listItemView.findViewById(R.id.publishedDate_text_view);

        titleTextView.setText(currentNews.getmTitle());

        sectionTextView.setText(currentNews.getmSection());
        publishedDateTextView.setText(dateToReadableDate(currentNews.getmPublishedDate()));

        return listItemView;
    }

    private String dateToReadableDate(String oldDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date date = dateFormat.parse(oldDateString);
            dateFormat = new SimpleDateFormat("LLL dd, yyyy\n" +
                    "h:mm a");
            return dateFormat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "No date to show";
    }
}

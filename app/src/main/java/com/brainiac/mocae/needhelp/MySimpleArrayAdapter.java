package com.brainiac.mocae.needhelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mocae on 2/1/2017.
 */

    public class MySimpleArrayAdapter extends ArrayAdapter<HelpRequest> {
        private final Context context;
        private ArrayList<HelpRequest> values = null;

        public MySimpleArrayAdapter(Context context, ArrayList<HelpRequest> values) {
            super(context, R.layout.help_request_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.help_request_layout, parent, false);

            TextView nameTxtView = (TextView) rowView.findViewById(R.id.nameTxtView);
            TextView peopleTxtView = (TextView) rowView.findViewById(R.id.peopleTxtView);
            TextView dateTxtView = (TextView) rowView.findViewById(R.id.dateTxtView);

            nameTxtView.setText(values.get(position).Name);
            peopleTxtView.setText(values.get(position).NumberOfPeople + "" + " persons requered");

            String startDateParts[] = values.get(position).StartDate.split("\\/");
            String endDateParts[] = values.get(position).EndDate.split("\\/");
            String startDateTimeParts[] = startDateParts[2].split("\\ ");
            String endDateTimeParts[] = endDateParts[2].split("\\ ");
            if (Integer.parseInt(startDateTimeParts[0]) == Integer.parseInt(endDateTimeParts[0])){
                if (Integer.parseInt(startDateParts[1]) == Integer.parseInt(endDateParts[1])){
                    if (Integer.parseInt(startDateParts[0]) == Integer.parseInt(endDateParts[0])){
                        dateTxtView.setText(startDateParts[0] + "/" + startDateParts [1] + "/" +
                                startDateTimeParts[0] + "  " + startDateTimeParts[1] + " -- " + endDateTimeParts[1]);
                    } else {
                        dateTxtView.setText(startDateParts[0] + "/" + startDateParts [1] + "  " +
                                startDateTimeParts [1] + " -- " + endDateParts[0] + "/" + endDateParts[1] + "  " + endDateTimeParts[1]  +
                                "  /" + startDateTimeParts[0]);
                    }
                } else {
                    dateTxtView.setText(startDateParts[0] + "/" + startDateParts [1] + "  " +
                            startDateTimeParts [1] + " -- " + endDateParts[0] + "/" + endDateParts[1] + "  " + endDateTimeParts[1]  +
                            "  /" + startDateTimeParts[0]);
                }
            } else {
                dateTxtView.setText(values.get(position).StartDate + " -- " + values.get(position).EndDate);
            }

            DataStorage.getInstance().addRequest(values.get(position));

            return rowView;
        }
    }

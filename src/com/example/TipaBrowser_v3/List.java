package com.example.TipaBrowser_v3;

import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dos on 26.06.2014.
 */
public class List extends ListFragment {

    final String ATTR_NAME_TEXT = "main_text";
    final String ATTR_NAME_SUBTEXT = "subtext";

    Intent intent;
    DBControlClass dbControlClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbControlClass = new DBControlClass(getActivity());

        int numberOfSessions = dbControlClass.numberOfSessionsFromDB();
        // fill List with data (image, text, subtext)
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String,
                Object>>(numberOfSessions);
        Map<String, Object> map;


        for(int i = 0; i<numberOfSessions; i++){
            map = new HashMap<String, Object>();
            map.put(ATTR_NAME_TEXT, dbControlClass.readSessionFromDB(i+1));
            map.put(ATTR_NAME_SUBTEXT, "Session " + (i+1));
            data.add(map);
        }

        String [] from = {ATTR_NAME_TEXT, ATTR_NAME_SUBTEXT};
        int[] to = {R.id.text, R.id.subtext};

        // simple adapter as an adapter, my_item_list as a layout
        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), data, R.layout.my_item_list, from, to);
        setListAdapter(adapter);
    }

    /**
     * when item in the list is clicked:
     * if it is "Go to Url...", it connects to that Url
     * if it is "Set Urls", it goes to AnotherActivity to set/change Url addresses
     *
     * If there is problems with internet connection or with url addresses,
     * toasts are displayed
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dbControlClass.readSessionFromDB(position+1).toString())));
    }

}

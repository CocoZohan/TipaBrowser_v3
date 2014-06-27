package com.example.TipaBrowser_v3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Dos on 27.06.2014.
 */
public class AlertDialogClass extends Activity implements AdapterView.OnItemClickListener{

    ListView listView;
    Dialog listDialog;
    public boolean itemClicked;
    public int itemNo;
    WebViewClass webViewClass;
    DBControlClass dbControlClass;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void showDialog(Context ctx, String[] values)
    {
        itemClicked = false;
        webViewClass = new WebViewClass();
        dbControlClass = new DBControlClass(ctx);

        listDialog = new Dialog(ctx);
        listDialog.setTitle("Urls in this sessions");
        LayoutInflater layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_list, null, false);
        listDialog.setContentView(view);
        listDialog.setCancelable(true);

        ListView anotherList = (ListView) listDialog.findViewById(R.id.listview);
        anotherList.setOnItemClickListener(this);
        anotherList.setAdapter(new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, values));
        listDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

package com.example.TipaBrowser_v3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    Button btnGo;
    EditText editTextUrl;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editTextUrl = (EditText)findViewById(R.id.editText);

        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://fapl.ru/"));

        btnGo = (Button)findViewById(R.id.buttonGo);
        btnGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        intent.putExtra("initialUrl", "http://fapl.ru/");
        startActivity(intent);
    }
}
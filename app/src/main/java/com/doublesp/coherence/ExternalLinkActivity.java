package com.doublesp.coherence;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class ExternalLinkActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_link);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        int index = data.toString().indexOf("shared/") + "shared/".length();
        String listId = data.toString().substring(index);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(listId);
    }

}

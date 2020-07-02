package com.krytro.wordsaway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv_content = findViewById(R.id.textView_about_content);
        tv_content.setText(Html.fromHtml(getString(R.string.about_content)));
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
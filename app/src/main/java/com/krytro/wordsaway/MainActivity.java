package com.krytro.wordsaway;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    WordsAway wa = new WordsAway();

    private EditText editText_input;
    private CheckBox checkBox_missUrl;
    private CheckBox checkBox_coolapkMode;
    private CheckBox checkBox_zeroWidthSpace;
    private CheckBox checkBox_rowsReverse;
    private CheckBox checkBox_wordsReverse;
    private CheckBox checkBox_verticalText;
    private CheckBox checkBox_lettersFont;
    private TextView textView_result;

    private ClipboardManager cm;

    private String latestResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_input = findViewById(R.id.editTextTextMultiLine);
        checkBox_missUrl = findViewById(R.id.checkBox_missUrl);
        checkBox_coolapkMode = findViewById(R.id.checkBox_coolapkMode);
        checkBox_zeroWidthSpace = findViewById(R.id.checkBox_zeroWidthSpace);
        checkBox_rowsReverse = findViewById(R.id.checkBox_rowsReverse);
        checkBox_wordsReverse = findViewById(R.id.checkBox_wordsReverse);
        checkBox_verticalText = findViewById(R.id.checkBox_verticalText);
        checkBox_lettersFont = findViewById(R.id.checkBox_lettersFont);
        textView_result = findViewById(R.id.textView_result);

        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void processText(View view) {
        String text = editText_input.getText().toString();

        String marked = "\ue0dc$1\ue0dd";
        if (checkBox_missUrl.isChecked()) {
            text = text.replaceAll("(http(s)?:\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w-.\\/?%&=]*)?)", marked);
        }
        if (checkBox_coolapkMode.isChecked()) {
            text = text.replaceAll("(#[\\w\\u4e00-\\u9fa5\\u3040-\\u30ff]{1,20}?#)", marked)
                    .replaceAll("(@[\\w\\u4e00-\\u9fa5\\u3040-\\u30ff]{1,15} ?)", marked)
                    .replaceAll("(\\[[\\w\\u4e00-\\u9fa5]{1,10}?\\])", marked);
        }
        if (checkBox_rowsReverse.isChecked()) {
            text = wa.rowsReverse(text, true);
        }
        if (checkBox_wordsReverse.isChecked()) {
            text = wa.wordsReverse(text, true);
        }
        if (checkBox_zeroWidthSpace.isChecked()) {
            text = wa.mixin(text, "\u200b", true);
        }
        if (checkBox_verticalText.isChecked()) {
            text = wa.verticalText(text);
        }
        if (checkBox_lettersFont.isChecked()) {
            text = wa.font(text, "bold");
        }
        text = text.replaceAll("\\ue0dc([^\\s]+? ?)\\ue0dd", "$1");
        textView_result.setText(text);
        latestResult = text;
    }

    public void onCheckBoxClick(View view) {

    }

    public void copyResult(View view) {
        ClipData data = ClipData.newPlainText("Result", latestResult);
        cm.setPrimaryClip(data);
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "已复制", Toast.LENGTH_SHORT);
        toast.show();
    }
}

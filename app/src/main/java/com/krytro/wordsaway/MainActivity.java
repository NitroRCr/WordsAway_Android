package com.krytro.wordsaway;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    WordsAway wa = new WordsAway();

    private EditText editText_input;
    private EditText editText_maxCol;
    private EditText editText_minRow;
    private CheckBox checkBox_missUrl;
    private CheckBox checkBox_coolapkMode;
    private CheckBox checkBox_zeroWidthSpace;
    private CheckBox checkBox_rowsReverse;
    private CheckBox checkBox_wordsReverse;
    private CheckBox checkBox_verticalText;
    private CheckBox checkBox_lettersFont;
    private TextView textView_result;
    private Spinner spinner_fonts;

    LinearLayout linearLayout_verticalText_options;
    LinearLayout linearLayout_fontSelect;

    private ClipboardManager cm;

    private String latestResult;

    private String[] fontNames = {"monospace", "script", "double-struck", "sans-serif",
            "sans-serif-bold", "sans-serif-italic", "sans-serif-bold-italic", "bold", "italic",
            "bold-italic", "bold-script", "fake-normal"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_input = findViewById(R.id.editTextTextMultiLine);
        editText_maxCol = findViewById(R.id.editTextNumber_maxCol);
        editText_minRow = findViewById(R.id.editTextNumber_minRow);
        checkBox_missUrl = findViewById(R.id.checkBox_missUrl);
        checkBox_coolapkMode = findViewById(R.id.checkBox_coolapkMode);
        checkBox_zeroWidthSpace = findViewById(R.id.checkBox_zeroWidthSpace);
        checkBox_rowsReverse = findViewById(R.id.checkBox_rowsReverse);
        checkBox_wordsReverse = findViewById(R.id.checkBox_wordsReverse);
        checkBox_verticalText = findViewById(R.id.checkBox_verticalText);
        checkBox_lettersFont = findViewById(R.id.checkBox_lettersFont);
        textView_result = findViewById(R.id.textView_result);

        linearLayout_verticalText_options = findViewById(R.id.linearLayout_verticalText_options);
        linearLayout_fontSelect = findViewById(R.id.linearLayout_fontSelect);

        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        spinner_fonts = findViewById(R.id.spinner_font);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fonts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fonts.setAdapter(adapter);
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
            int maxCol = Integer.valueOf(editText_maxCol.getText().toString());
            int minRow = Integer.valueOf(editText_minRow.getText().toString());
            text = wa.verticalText(text, maxCol, minRow);
        }
        spinner_fonts.getSelectedItemId();
        if (checkBox_lettersFont.isChecked()) {
            text = wa.font(text, fontNames[spinner_fonts.getSelectedItemPosition()]);
        }
        text = text.replaceAll("\\ue0dc([^\\s]+? ?)\\ue0dd", "$1");
        textView_result.setText(text);
        latestResult = text;
    }

    public void onCheckBoxClick(View view) {
        boolean isChecked = ((CheckBox)view).isChecked();
        switch (view.getId()) {
            case R.id.checkBox_wordsReverse:
                if (isChecked) {
                    checkBox_rowsReverse.setChecked(false);
                    checkBox_zeroWidthSpace.setChecked(false);
                }
                break;
            case R.id.checkBox_rowsReverse:
                if (isChecked) {
                    checkBox_wordsReverse.setChecked(false);
                }
                break;
            case R.id.checkBox_zeroWidthSpace:
                if (isChecked) {
                    checkBox_wordsReverse.setChecked(false);
                }
                break;
            case R.id.checkBox_verticalText:
                if (isChecked) {
                    linearLayout_verticalText_options.setVisibility(View.VISIBLE);
                } else {
                    linearLayout_verticalText_options.setVisibility(View.GONE);
                }
                break;
            case R.id.checkBox_lettersFont:
                if (isChecked) {
                    linearLayout_fontSelect.setVisibility(View.VISIBLE);
                } else {
                    linearLayout_fontSelect.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void copyResult(View view) {
        ClipData data = ClipData.newPlainText("Result", latestResult);
        cm.setPrimaryClip(data);
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "已复制", Toast.LENGTH_SHORT);
        toast.show();
    }
}

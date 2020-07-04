package com.krytro.wordsaway;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private CheckBox checkBox_shortenUrl;
    private CheckBox checkBox_verticalText;
    private CheckBox checkBox_lettersFont;
    private TextView textView_result;
    private Spinner spinner_fonts;
    private Button button_processText;

    private LinearLayout linearLayout_verticalText_options;
    private LinearLayout linearLayout_fontSelect;

    private ClipboardManager cm;

    private String latestResult;

    private String[] fontNames = {"monospace", "script", "double-struck", "sans-serif",
            "sans-serif-bold", "sans-serif-italic", "sans-serif-bold-italic", "bold", "italic",
            "bold-italic", "bold-script", "fake-normal"};

    private Menu main_menu;

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
        checkBox_shortenUrl = findViewById(R.id.checkBox_shortenUrl);
        checkBox_verticalText = findViewById(R.id.checkBox_verticalText);
        checkBox_lettersFont = findViewById(R.id.checkBox_lettersFont);
        textView_result = findViewById(R.id.textView_result);
        button_processText = findViewById(R.id.button_processText);

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
            int maxCol = Integer.parseInt(editText_maxCol.getText().toString());
            int minRow = Integer.parseInt(editText_minRow.getText().toString());
            if (maxCol == 0) {
                toast("最大列数不能为0");
                return;
            }
            text = wa.verticalText(text, maxCol, minRow);
        }
        spinner_fonts.getSelectedItemId();
        if (checkBox_lettersFont.isChecked()) {
            text = wa.font(text, fontNames[spinner_fonts.getSelectedItemPosition()]);
        }
        text = text.replaceAll("\\ue0dc([^\\s]+? ?)\\ue0dd", "$1");

        if (checkBox_shortenUrl.isChecked()) {
            String[] urls = regFindG(text, "(http(s)?:\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?)");
            setResult("短链接请求中...");
            button_processText.setEnabled(false);
            for (String originUrl : urls) {
                String requestUrl;
                try {
                    requestUrl = URLDecoder.decode(originUrl, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    continue;
                }
                String shortenUrl = httpGet("https://is.gd/create.php?url=" + requestUrl);
                text = text.replaceAll(originUrl, shortenUrl);
            }
            setResult(text);
            button_processText.setEnabled(true);
        } else {
            setResult(text);
        }
    }

    private void setResult(String text) {
        textView_result.setText(text);
        latestResult = text;
    }


    public static String httpGet(String strUrlPath){
        String strResult = "";
        try {
            URL url = new URL(strUrlPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            strResult = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }

    private String[] regFindG(String originalText,String regEx ) {
        List<String> result = new ArrayList<>();
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(originalText);
        while (mat.find()) {
            result.add(mat.group(1));
        }
        return (result.toArray(new String[0]));
    }

    public void onCheckBoxClick(View view) {
        boolean isChecked = ((CheckBox)view).isChecked();
        switch (view.getId()) {
            case R.id.checkBox_missUrl:
                if (isChecked) {
                    checkBox_shortenUrl.setEnabled(true);
                } else {
                    checkBox_shortenUrl.setChecked(false);
                    checkBox_shortenUrl.setEnabled(false);
                }
                break;
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
        toast("已复制");
    }

    public void toast(String content) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        main_menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_usage:
                showUsageDialog();
                break;
            case R.id.menu_item_website:
                openUrl("https://wordsaway.texice.xyz");
                break;
            case R.id.menu_item_about:
                Intent intent_about = new Intent(this, AboutActivity.class);
                startActivity(intent_about);
                break;
        }
        return true;
    }

    private void showUsageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.usage_title);
        builder.setMessage(Html.fromHtml(getString(R.string.usage_content)));
        builder.setPositiveButton(R.string.usage_btn_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void openUrl(String urlText) {
        Uri uri = Uri.parse(urlText);
        Intent intent_url = new Intent();
        intent_url.setAction("android.intent.action.VIEW");
        intent_url.setData(uri);
        startActivity(intent_url);
    }

}

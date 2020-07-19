package com.krytro.wordsaway;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MainActivity extends AppCompatActivity {
    WordsAway wa = new WordsAway();

    private Menu main_menu;

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
    private Spinner spinner_fonts = findViewById(R.id.spinner_font);
    private Button button_processText;

    private LinearLayout linearLayout_verticalText_options;
    private LinearLayout linearLayout_fontSelect;

    private ClipboardManager cm;

    private String latestResult;

    private String[] fontNames = {"monospace", "script", "double-struck", "sans-serif",
            "sans-serif-bold", "sans-serif-italic", "sans-serif-bold-italic", "bold", "italic",
            "bold-italic", "bold-script", "fake-normal"};

    private String currText;

    final static int SET_SHORTEN_URL = 0;
    final static int SHORTEN_URL_FAIL_TODO = 1;


    private int urlNum;
    private int urlDone;
    private String[] originUrls;

    class mHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MainActivity.SET_SHORTEN_URL:
                    System.out.println(message.arg1);
                    setShortenUrl((String)message.obj, originUrls[message.arg1]);
                    break;
                case MainActivity.SHORTEN_URL_FAIL_TODO:
                    shortenUrlFailTodo();
                    break;
            }
        }

        private void setShortenUrl(String data, String originUrl) {
            JSONObject json = null;
            try {
                json = new JSONObject(data);
                currText = currText.replaceAll(originUrl, json.getString("shorturl"));
                System.out.println(currText);
            } catch (JSONException e) {
                e.printStackTrace();
                shortenUrlFailTodo();
            }
            urlCount();
        }

        private void shortenUrlFailTodo() {
            toast("短链接请求失败");
            urlCount();
        }

        private void urlCount() {
            if(++urlDone == urlNum) {
                setResult(currText);
                button_processText.setEnabled(true);
                urlNum = 0;
                urlDone = 0;
                originUrls = new String[]{};
            }
        }
    }

    private Handler mhandler = new mHandler();

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fonts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fonts.setAdapter(adapter);
    }

    public void processText(View view) {
        currText = editText_input.getText().toString();

        String marked = "\ue0dc$1\ue0dd";
        if (checkBox_missUrl.isChecked()) {
            currText = currText.replaceAll("(http(s)?:\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w-.\\/?%&=]*)?)", marked);
        }
        if (checkBox_coolapkMode.isChecked()) {
            currText = currText.replaceAll("(#[\\w\\u4e00-\\u9fa5\\u3040-\\u30ff]{1,20}?#)", marked)
                    .replaceAll("(@[\\w\\u4e00-\\u9fa5\\u3040-\\u30ff]{1,15} ?)", marked)
                    .replaceAll("(\\[[\\w\\u4e00-\\u9fa5]{1,10}?\\])", marked);
        }
        if (checkBox_rowsReverse.isChecked()) {
            currText = wa.rowsReverse(currText, true);
        }
        if (checkBox_wordsReverse.isChecked()) {
            currText = wa.wordsReverse(currText, true);
        }
        if (checkBox_zeroWidthSpace.isChecked()) {
            currText = wa.mixin(currText, "\u200b", true);
        }
        if (checkBox_verticalText.isChecked()) {
            int maxCol = Integer.parseInt(editText_maxCol.getText().toString());
            int minRow = Integer.parseInt(editText_minRow.getText().toString());
            if (maxCol == 0) {
                toast("最大列数不能为0");
                return;
            }
            currText = wa.verticalText(currText, maxCol, minRow);
        }
        spinner_fonts.getSelectedItemId();
        if (checkBox_lettersFont.isChecked()) {
            currText = wa.font(currText, fontNames[spinner_fonts.getSelectedItemPosition()]);
        }
        currText = currText.replaceAll("\\ue0dc([^\\s]+? ?)\\ue0dd", "$1");

        System.out.println(currText);

        if (checkBox_shortenUrl.isChecked()) {
            String[] urls = regFindG(currText, "(http(s)?:\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?)");
            if (urls.length > 0) {
                setResult("短链接请求中...");
                button_processText.setEnabled(false);
                for (int i = 0; i < urls.length; i++) {
                    String originUrl = urls[i];
                    originUrls = urls;
                    urlNum = urls.length;
                    urlDone = 0;
                    System.out.println(urls);
                    try {
                        String urlText = "https://is.gd/create.php?format=json&url="
                                + URLEncoder.encode(originUrl, "UTF-8");
                        URL url = new URL(urlText);

                        Message resolve = new Message();
                        resolve.what = SET_SHORTEN_URL;
                        resolve.arg1 = i;

                        Message reject = new Message();
                        reject.what = SHORTEN_URL_FAIL_TODO;
                        reject.arg1 = i;
                        httpGet(url, resolve, reject);
                    } catch (MalformedURLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = SHORTEN_URL_FAIL_TODO;
                        msg.arg1 = i;
                        mhandler.sendMessage(msg);
                    }
                }
            } else {
                setResult(currText);
            }
        } else {
            setResult(currText);
        }
    }

    private void setResult(String text) {
        textView_result.setText(text);
        latestResult = text;
    }

    private void httpGet(URL url, Message resolve, Message reject) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                InputStream in = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                resolve.obj = result.toString();
                mhandler.sendMessage(resolve);
            } catch (IOException e) {
                e.printStackTrace();
                reject.obj = e;
                mhandler.sendMessage(reject);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
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

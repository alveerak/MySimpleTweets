package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    public EditText tweetTextField;
    public AsyncHttpResponseHandler handler;
    public TwitterClient client;
    public Tweet tweet;
    public TextView tvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tweetTextField = (EditText) findViewById(R.id.ptComposeTweet);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);
        tweetTextField.addTextChangedListener(mTextEditorWatcher);
        client = TwitterApp.getRestClient(this);
        handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                    i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    setResult(2, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharacterCount.setText(String.valueOf(s.length())+"/280");
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void onClickTweet(View w){
        String text = "";
        text = tweetTextField.getText().toString();
        client.sendTweet(text, handler);
        //Intent i = new Intent(this, TimelineActivity.class);
        //i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
        //i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
        //finish();
    }

    public void onCloseCompose(View w) {
        Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
        startActivity(i);
    }
}

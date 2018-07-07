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

public class ReplyActivity extends AppCompatActivity {
    public EditText tweetTextField;
    public AsyncHttpResponseHandler handler;
    public TwitterClient client;
    public Tweet reply_to_tweet;

    public TextView tvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        tweetTextField = (EditText) findViewById(R.id.ptReplyTweet);
        tvCharacterCount = (TextView) findViewById(R.id.tvReplyCharacterCount);
        tweetTextField.addTextChangedListener(mTextEditorWatcher);
        client = TwitterApp.getRestClient(this);
        handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet new_tweet = Tweet.fromJSON(response);
                    Intent i = new Intent(ReplyActivity.this, TimelineActivity.class);
                    i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(new_tweet));
                    setResult(2, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        reply_to_tweet = (Tweet)  Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        tweetTextField.setText("@"+reply_to_tweet.handle);
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
        Intent i = new Intent(ReplyActivity.this, TimelineActivity.class);
        startActivity(i);
    }
}
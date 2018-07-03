package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tweetTextField = (EditText) findViewById(R.id.ptComposeTweet);
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

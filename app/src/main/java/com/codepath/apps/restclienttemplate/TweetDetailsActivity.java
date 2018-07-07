package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

//import butterknife.BindView;
//import butterknife.ButterKnife;


// add some basic code to retrieve and unwrap the Movie from the Intent
public class TweetDetailsActivity extends AppCompatActivity{
    // the tweet to display
    Tweet tweet;

    // the view objects
    ImageView profPic;
    TextView tvDetailsUserName;
    TextView tvDetailsHandle;
    TextView tvDetailsBody;
    TextView tvDetailsTimeElapsed;
    ImageView retweetsImageView;
    ImageView favoritesImageView;
    ImageView replyImageView;

    public TwitterClient actionClient;
    public AsyncHttpResponseHandler actionHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        tvDetailsUserName = (TextView) findViewById(R.id.tvDetailsUserName);
        tvDetailsHandle = (TextView) findViewById(R.id.tvDetailsHandle);
        tvDetailsBody = (TextView) findViewById(R.id.tvDetailsBody);
        tvDetailsTimeElapsed = (TextView) findViewById(R.id.tvDetailsTimeElapsed);
        favoritesImageView = (ImageView) findViewById(R.id.favoritesImageView);
        retweetsImageView = (ImageView) findViewById(R.id.retweetsImageView);
        replyImageView = (ImageView) findViewById(R.id.replyImageView);
        profPic = (ImageView) findViewById(R.id.ivProfPic);

        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        //ButterKnife.bind(this);
        //resolve the view objects

        // load image using glide
        GlideApp.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCornersTransformation(75, 0))
                //.placeholder(R.drawable.flicks_movie_placeholder)
                //.error(R.drawable.flicks_movie_placeholder)
                .into(profPic);

        // set the title and overview
        tvDetailsUserName.setText(tweet.user.name);
        tvDetailsHandle.setText("   @"+tweet.user.screenName);
        tvDetailsBody.setText(tweet.body);
        tvDetailsTimeElapsed.setText(getRelativeTimeAgo(tweet.createdAt));

        actionClient = TwitterApp.getRestClient(this);
        actionHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent i = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
                    i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    setResult(2, i);
                    Toast.makeText(TweetDetailsActivity.this, "You retweeted the tweet from details!", Toast.LENGTH_LONG).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void onFavorite(View v) {
        long id = tweet.uid;
        actionClient.favoriteTweet(id, actionHandler);
        Toast.makeText(this, "You favorited the tweet from details!", Toast.LENGTH_LONG).show();
    }

    public void onRetweet(View v) {
        long id = tweet.uid;
        actionClient.retweet(id, actionHandler);
        Toast.makeText(this, "You retweeted the tweet from details!", Toast.LENGTH_LONG).show();
    }

    public void onReply(View w) {
        Intent i = new Intent(this, ReplyActivity.class);
        i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
        startActivity(i);
    }


    public void onExit(View w) {
        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
    }
}

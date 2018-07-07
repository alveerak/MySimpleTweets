package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class UserActivity extends AppCompatActivity {

    Tweet tweet;
    ImageView userProfPic;
    TextView followers_num;
    TextView likes;
    TextView following_num;
    TextView num_tweets;
    TextView handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userProfPic = (ImageView) findViewById(R.id.userPic);

        followers_num = (TextView) findViewById(R.id.followerCount);
        following_num = (TextView) findViewById(R.id.followingCount);
        likes = (TextView) findViewById(R.id.likesCount);
        handle = (TextView) findViewById(R.id.userHandle);
        num_tweets = (TextView) findViewById(R.id.numTweets);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        //ButterKnife.bind(this);
        //resolve the view objects

        // load image using glide
        GlideApp.with(this)
                .load(tweet.user.profileImageUrl)
                //.placeholder(R.drawable.flicks_movie_placeholder)
                //.error(R.drawable.flicks_movie_placeholder)
                .into(userProfPic);

        // set the title and overview
        followers_num.setText("Followers: " + tweet.user.followersCount);
        handle.setText("@"+tweet.user.screenName);
        likes.setText("Likes: " + tweet.user.likeCount);
        num_tweets.setText("Tweets: " + tweet.user.tweetCount);
        following_num.setText("Following: " + tweet.user.followingCount);

    }

    public void onCloseCompose(View w) {
        Intent i = new Intent(UserActivity.this, TimelineActivity.class);
        startActivity(i);
    }
}

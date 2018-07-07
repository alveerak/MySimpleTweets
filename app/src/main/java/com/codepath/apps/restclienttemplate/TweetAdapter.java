package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    private final Handler handler;
    Context context;
    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, Handler h) {
        handler = h;
        mTweets = tweets;
    }

    public TwitterClient timelineActionClient;
    public AsyncHttpResponseHandler timelineActionHandler;

    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    interface Handler{
        void onClick(Tweet tweet, Context context);
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);
        timelineActionClient = TwitterApp.getRestClient(context);
        timelineActionHandler = new JsonHttpResponseHandler() {};

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimeElapsed.setText(getRelativeTimeAgo(tweet.createdAt));
        holder.tvHandle.setText("    @"+tweet.handle);
        holder.tvNumRetweets.setText(tweet.retweet_count);
        holder.tvNumFavorites.setText(tweet.favorites_count+"  ");
        holder.ivFavoritesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tweet tweetToReply = mTweets.get(position);
                long id = tweet.uid;
                timelineActionClient.retweet(id, timelineActionHandler);
                //holder.ivFavoritesImage.setImageIcon();
                Toast.makeText(context, "You favorited the tweet from timeline!", Toast.LENGTH_LONG).show();
            }
        });
        holder.ivRetweetsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tweet tweetToReply = mTweets.get(position);
                long id = tweet.uid;
                timelineActionClient.retweet(id, timelineActionHandler);
                Toast.makeText(context, "You retweeted the tweet from timeline!", Toast.LENGTH_LONG).show();
            }
        });
        holder.ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tweet tweetToReply = mTweets.get(position);
                //Intent i = new Intent(context, ReplyActivity.class);
                //i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweetToReply));
                handler.onClick(tweetToReply, context);
                //context.startActivity(i);
            }
        });
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tweet tweetToReply = mTweets.get(position);
                Intent i = new Intent(context, UserActivity.class);
                i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweetToReply));
                //handler.onClick(tweetToReply, context);
                context.startActivity(i);
            }
        });

        //holder.ptReplyTweet.setText(new O);

        // load image using glide
        GlideApp.with(context)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCornersTransformation(75, 0))
                //.placeholder(placeholderId)
                //.error(placeholderId)
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
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
    // create a ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimeElapsed;
        public TextView tvHandle;
        public ImageView ivRetweetsImage;
        public TextView tvNumRetweets;
        public ImageView ivFavoritesImage;
        public TextView tvNumFavorites;
        public EditText ptReplyTweet;
        public ImageButton ibReply;



        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeElapsed = (TextView) itemView.findViewById(R.id.tvTimeElapsed);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            ivRetweetsImage = (ImageView) itemView.findViewById(R.id.ivRetweetsImage);
            tvNumRetweets = (TextView) itemView.findViewById(R.id.tvNumRetweets);
            ivFavoritesImage = (ImageView) itemView.findViewById(R.id.ivFavoritesImage);
            tvNumFavorites = (TextView) itemView.findViewById(R.id.tvNumFavorites);
            ptReplyTweet = (EditText) itemView.findViewById(R.id.ptReplyTweet);
            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected Movie
        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = mTweets.get(position);
                // create intent for the new activity
                //Intent intent = new Intent(context, TweetDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                //intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                //context.startActivity(intent);
//                context.startActivityForResult();
                handler.onClick(tweet, context);
            }
        }


    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }



}

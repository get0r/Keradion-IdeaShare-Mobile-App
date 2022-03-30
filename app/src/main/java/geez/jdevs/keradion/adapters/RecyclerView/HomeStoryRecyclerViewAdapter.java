package geez.jdevs.keradion.adapters.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import geez.jdevs.keradion.activities.ViewStoryActivity;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.R;

public class HomeStoryRecyclerViewAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_NORMAL = 0;
    private final int VIEW_TYPE_ROW = 1;
    private List<Story$UserModel> storyUserList;
    private Context mContext;

    public HomeStoryRecyclerViewAdapter(List<Story$UserModel> storiesList, Context mContext) {

        this.storyUserList = storiesList;
        this.mContext = mContext;
    }

    public void addStory(Story$UserModel story) {
        storyUserList.add(story);
        notifyDataSetChanged();
    }
    public void addUserList(Story$UserModel user) {
        storyUserList.add(user);
        notifyDataSetChanged();
    }

    protected class StoryItemViewHolder extends RecyclerView.ViewHolder {

        private TextView topicView,titleView,contentView;
        private TextView writerName,writtenDate,pageName;
        private ImageView writerImg;
        private ImageButton moreOption;

        public StoryItemViewHolder(View view) {
            super(view);
            Typeface titleFont = ResourcesCompat.getFont(mContext, R.font.titlefont);
            topicView = view.findViewById(R.id.story_recycler_item_topic);
            titleView = view.findViewById(R.id.story_recycler_item_title);
            contentView = view.findViewById(R.id.story_recycler_item_content);
            writerName = view.findViewById(R.id.tab_frag_stoty_writer_name);
            writtenDate = view.findViewById(R.id.tab_frag_story_written_date);
            pageName = view.findViewById(R.id.story_recycler_item_writer_pagename);
            writerImg = view.findViewById(R.id.story_recycler_item_writerimg);
            titleView.setTypeface(titleFont);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewIntent = new Intent(mContext, ViewStoryActivity.class);
                    int position = getLayoutPosition();
                    Story$UserModel story = storyUserList.get(position);
                    viewIntent.putExtra("sid",story.getSid());
                    viewIntent.putExtra("title",story.getTitle());
                    viewIntent.putExtra("content",story.getStoryContent());
                    viewIntent.putExtra("writerName",story.getWriterName());
                    viewIntent.putExtra("topic",story.getsTopic());
                    viewIntent.putExtra("writtenDate",story.getWritten_date().substring(0, 10));
                    viewIntent.putExtra("writtenTime",story.getWritten_date().substring(12,19));
                    viewIntent.putExtra("offDate",story.getWritten_date());
                    viewIntent.putExtra("noOfWell",story.getNo_of_well_written());
                    mContext.startActivity(viewIntent);
                }
            });
        }
    }

    protected class UserProfileRowViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView horzUserRecycler;
        private TextView headerText;

        public UserProfileRowViewHolder(@NonNull View itemView) {
            super(itemView);
            horzUserRecycler = itemView.findViewById(R.id.home_frag_story_item_horz_recycler_view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return storyUserList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        System.out.println(viewType);
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                view = LayoutInflater.from(mContext).inflate(R.layout.home_frag_story_item_normal_view,
                    parent, false);
                return new StoryItemViewHolder(view);
            case VIEW_TYPE_ROW:
                view = LayoutInflater.from(mContext).inflate(R.layout.home_frag_story_item_horz_recyc_view,
                        parent, false);
                return new UserProfileRowViewHolder(view);
        }
        return new StoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = storyUserList.get(position).getViewType();
        HomeUserRecyclerViewAdapter mAdapter = new HomeUserRecyclerViewAdapter(mContext, storyUserList.get(position).getUsersList());
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                Story$UserModel story = storyUserList.get(position);
                ((StoryItemViewHolder)holder).titleView.setText((story.getTitle()));
                ((StoryItemViewHolder)holder).contentView.setText((story.getStoryContent().substring(0,70).replace("\n"," ")+"..."));
                ((StoryItemViewHolder)holder).writerName.setText(story.getWriterName());
                ((StoryItemViewHolder)holder).writtenDate.setText(story.getWritten_date().substring(0,10));
                ((StoryItemViewHolder)holder).pageName.setText("");
                ((StoryItemViewHolder)holder).topicView.setText("#".concat(story.getsTopic()));
                break;
            case VIEW_TYPE_ROW:
                Story$UserModel user = storyUserList.get(position);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
                ((UserProfileRowViewHolder)holder).horzUserRecycler.setLayoutManager(mLayoutManager);
                ((UserProfileRowViewHolder)holder).horzUserRecycler.setItemAnimator(new DefaultItemAnimator());
                ((UserProfileRowViewHolder)holder).horzUserRecycler.setAdapter(mAdapter);
                mAdapter.addUser(storyUserList.get(position).getUsersList().get(position));
                break;
        }


        //image here........Picassooo
    }

    @Override
    public int getItemCount() {
        return storyUserList.size();
    }
}

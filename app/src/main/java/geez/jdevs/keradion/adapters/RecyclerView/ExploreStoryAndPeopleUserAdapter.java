package geez.jdevs.keradion.adapters.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import geez.jdevs.keradion.activities.ViewStoryActivity;
import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.R;

public class ExploreStoryAndPeopleUserAdapter extends RecyclerView.Adapter<ExploreStoryAndPeopleUserAdapter.StoryItemViewHolder> {

    private Context mContext;
    private ArrayList<Story$UserModel> storyList;


    public ExploreStoryAndPeopleUserAdapter(Context mContext, ArrayList<Story$UserModel> storyList) {
        this.mContext = mContext;
        this.storyList = storyList;
    }

    public void addStory(Story$UserModel story) {
        storyList.add(story);
        notifyDataSetChanged();
        System.out.println(getItemCount());
    }

    public void removeAll() {
        storyList.clear();
        notifyDataSetChanged();
    }

    protected class StoryItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView storyImage;
        private TextView storyTitle, storyContent, storyTopic, storyDate;
        private TextView writerName, writerPageName;

        public StoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Typeface titleFont = ResourcesCompat.getFont(mContext, R.font.titlefont);
            storyTitle = itemView.findViewById(R.id.tab_frag_story_title);
            storyContent = itemView.findViewById(R.id.tab_frag__content);
            storyTopic = itemView.findViewById(R.id.tab_frag_story_topic);
            storyDate = itemView.findViewById(R.id.tab_frag_story_written_date);
            writerName = itemView.findViewById(R.id.tab_frag_story_writer_name);
            writerPageName = itemView.findViewById(R.id.tab_frag_story_pageName);
            storyTitle.setTypeface(titleFont);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewIntent = new Intent(mContext, ViewStoryActivity.class);
                    int position = getLayoutPosition();
                    Story$UserModel story = storyList.get(position);
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


    @NonNull
    @Override
    public StoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_fragment_explore_story_item_view,
                parent, false);
        return new StoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryItemViewHolder holder, int position) {
        final Story$UserModel story = storyList.get(position);
        holder.storyTitle.setTypeface(ResourcesCompat.getFont(mContext,R.font.titlefont));
        holder.storyTitle.setText((story.getTitle()));
        if (story.getTitle().length() > 200)
            holder.storyContent.setText(("..."));
        else
            holder.storyContent.setText(story.getStoryContent().substring(0, 50).replace("\n"," ").trim().concat("..."));
        holder.writerName.setText(story.getWriterUsername());
        if(story.getWritten_date().length() > 10 )
            holder.storyDate.setText(story.getWritten_date().substring(0, 10).concat(" ").concat(story.getWritten_date().substring(12,19)));
        holder.writerName.setText(story.getWriterName());
        holder.writerPageName.setText("");
        holder.storyTopic.setText("#".concat(story.getsTopic()));
        //image here........Picassooo
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }
}

package com.ruby.gobzy.sync.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ruby.gobzy.sync.R;
import com.ruby.gobzy.sync.pojo.Story;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sunil Vakotar
 * Date: 12/9/13
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryAdapter extends BaseAdapter {
    private List<Story> storyList;
    private Context context;

    public StoryAdapter(Context context, List<Story> storyList) {
        this.storyList = storyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return storyList.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(int position) {
        return storyList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        Story story = storyList.get(position);
        return story.getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.activity_rk_list_detial, null);

        }else{
            row = view;
        }
        Story story = storyList.get(position);

        TextView Title = (TextView) row.findViewById(R.id.TextTitle);
        Title.setText(story.getStoryName());

        return row;
    }
}

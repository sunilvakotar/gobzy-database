package com.ruby.gobzy.sync.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ruby.gobzy.sync.R;
import com.ruby.gobzy.sync.pojo.Category;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sunil Vakotar
 * Date: 11/18/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryAdapter extends BaseAdapter{
    private List<Category> categoryList;
    private Context context;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryList.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        Category category = categoryList.get(position);
        return category.getId();
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
        Category category = categoryList.get(position);

        TextView Title = (TextView) row.findViewById(R.id.TextTitle);
        Title.setText(category.getCategoryName());

        return row;
    }
}

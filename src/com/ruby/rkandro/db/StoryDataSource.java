package com.ruby.rkandro.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.ruby.rkandro.constant.Constant;
import com.ruby.rkandro.pojo.Category;
import com.ruby.rkandro.pojo.Story;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StoryDataSource {
	private SQLiteDatabase database;
	private MyDatabaseHelper databaseHelper;
	private String[] storiesColumns = { Constant.ID, Constant.STORY_CATEGORY_ID, Constant.STORY_NAME};
	private String[] allStoriesColumns = { Constant.ID, Constant.STORY_CATEGORY_ID, Constant.STORY_NAME, Constant.STORY_DESC};
	private String[] categoryColumns = { Constant.ID, Constant.CATEGORY_NAME};
	
	public StoryDataSource(Context context){
		databaseHelper = new MyDatabaseHelper(context);
	}
	
	public void open()throws SQLException {
        database = databaseHelper.getReadableDatabase();
	}
	
	public void close() {
		databaseHelper.close();
	}
	
	public List<Category> getCategories(){
        List<Category> categories = new ArrayList<Category>();
        Cursor cursor = database.query(Constant.TABLE_CATEGORY, categoryColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Category category = cursorToCategory(cursor);
            categories.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return categories;
    }

    private Category cursorToCategory(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(0));
        category.setCategoryName(cursor.getString(1));
        return category;
    }
	
	public List<Story> getStoryByCategory(int categoryId){
		List<Story> stories = new ArrayList<Story>();
		Cursor cursor = database.query(Constant.TABLE_STORY, storiesColumns,
				Constant.STORY_CATEGORY_ID + " = " + categoryId, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
            Story course = cursorToStory(cursor, false);
            stories.add(course);
			cursor.moveToNext();
		}
		cursor.close();
		return stories;
	}
	
	public List<String> getAllStoriesName(){
		Cursor cursor = database.query(Constant.TABLE_STORY, storiesColumns, null, null, null,
				null, null);
		cursor.moveToFirst();
		List<String> cList = new ArrayList<String>(); 
		while (!cursor.isAfterLast()) {
			cList.add(cursor.getString(2));
			cursor.moveToNext();
		}
		cursor.close();
		return cList;
	}

    public Integer getStoryCount(){
        Cursor cursor = database.rawQuery("select count(*) from " + Constant.TABLE_STORY, null);

        // ensure there is at least one row and one column
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }
	
	public Story getStoryById(int storyId){
		Story story = null;
		Cursor cursor = database.query(Constant.TABLE_STORY, allStoriesColumns,
				Constant.ID + " = " + storyId, null, null, null, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			story = cursorToStory(cursor, true);
		}
		cursor.close();
		return story;
	}
	
	private Story cursorToStory(Cursor cursor, boolean all) {
        Story story = new Story();
        story.setId(cursor.getInt(0));
        story.setStoryCategoryId(cursor.getInt(1));
        story.setStoryName(cursor.getString(2));
        if(all){
            story.setStoryDesc(cursor.getString(3));
        }
		return story;
	}

    public void addAllCategory(List<Category> categoryList){
        Cursor cursor;
        ContentValues categoryValues;
        for (Category category:categoryList){
            cursor = database.query(Constant.TABLE_CATEGORY, categoryColumns
                    , Constant.ID +" = "+category.getId(),null,null,null,null);
            if(cursor.getCount() <= 0){
                categoryValues = new ContentValues();
                categoryValues.put(Constant.ID, category.getId());
                categoryValues.put(Constant.CATEGORY_NAME, category.getCategoryName());
                database.insert(Constant.TABLE_CATEGORY, null, categoryValues);
            }
        }
    }

    public void addStories(List<Story> storyList){
        Cursor cursor;
        ContentValues storyValues;
        for (Story story:storyList){
            cursor = database.query(Constant.TABLE_STORY, allStoriesColumns
                    , Constant.ID +" = "+story.getId(),null,null,null,null);
            if(cursor.getCount() <= 0){
                storyValues = new ContentValues();
                storyValues.put(Constant.ID, story.getId());
                storyValues.put(Constant.STORY_NAME, story.getStoryName());
                storyValues.put(Constant.STORY_DESC, story.getStoryDesc());
                storyValues.put(Constant.STORY_CATEGORY_ID, story.getStoryCategoryId());
                database.insert(Constant.TABLE_STORY, null, storyValues);
            }
        }
    }
}

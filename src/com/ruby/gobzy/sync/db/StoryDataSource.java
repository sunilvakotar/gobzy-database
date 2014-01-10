package com.ruby.gobzy.sync.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.ruby.gobzy.sync.constant.Constant;
import com.ruby.gobzy.sync.pojo.Category;
import com.ruby.gobzy.sync.pojo.Story;
import com.ruby.gobzy.sync.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class StoryDataSource {
	private SQLiteDatabase database;
	private MyDatabaseHelper databaseHelper;
	private String[] storiesColumns = { Constant.ID, Constant.STORY_CATEGORY_ID, Constant.STORY_NAME};
	private String[] allStoriesColumns = { Constant.ID, Constant.STORY_CATEGORY_ID, Constant.STORY_NAME, Constant.STORY_DESC};
	private String[] categoryColumns = { Constant.ID, Constant.CATEGORY_NAME};
    private String[] userColumns = {Constant.ID, Constant.USERNAME, Constant.USER_PWD};

	public StoryDataSource(Context context){
		databaseHelper = new MyDatabaseHelper(context);
	}
	
	public void open()throws SQLException {
        database = databaseHelper.getReadableDatabase();
	}
	
	public void close() {
		databaseHelper.close();
	}

    public User getUser(){
        User user = null;
        Cursor cursor = database.query(Constant.TABLE_USER, userColumns,
                null, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
        }
        cursor.close();
        return user;
    }

    public void saveUser(String password){
        ContentValues userValues = new ContentValues();
        userValues.put(Constant.ID, 1);
        userValues.put(Constant.USERNAME, "user");
        userValues.put(Constant.USER_PWD, password);
        database.insert(Constant.TABLE_USER, null, userValues);
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
				Constant.STORY_CATEGORY_ID + " = " + categoryId, null, null, null, Constant.STORY_CREATE_DATE + " DESC");
		cursor.moveToFirst();
        int count = 1;
		while (!cursor.isAfterLast()){
            Story course = cursorToStory(cursor, false, count++);
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
			story = cursorToStory(cursor, true, 0);
		}
		cursor.close();
		return story;
	}
	
	private Story cursorToStory(Cursor cursor, boolean all, int count) {
        Story story = new Story();
        story.setId(cursor.getInt(0));
        story.setStoryCategoryId(cursor.getInt(1));
        if(count > 0){
            story.setStoryName(count+". "+cursor.getString(2));
        }else{
            story.setStoryName(cursor.getString(2));
        }
        if(all){
            story.setStoryDesc(cursor.getString(3));
        }
		return story;
	}

    public void truncateTable(){
        database.execSQL("delete from "+Constant.TABLE_CATEGORY);
        database.execSQL("delete from "+Constant.TABLE_STORY);
    }

    public void addAllCategory(List<Category> categoryList){
        truncateTable();
        ContentValues categoryValues;
        for (Category category : categoryList) {
            categoryValues = new ContentValues();
            categoryValues.put(Constant.ID, category.getId());
            categoryValues.put(Constant.CATEGORY_NAME, category.getCategoryName());
            database.insert(Constant.TABLE_CATEGORY, null, categoryValues);
        }
    }

    public void addStories(List<Story> storyList){
        ContentValues storyValues;
        for (Story story : storyList) {
            storyValues = new ContentValues();
            storyValues.put(Constant.ID, story.getId());
            storyValues.put(Constant.STORY_NAME, story.getStoryName());
            storyValues.put(Constant.STORY_DESC, story.getStoryDesc());
            storyValues.put(Constant.STORY_CATEGORY_ID, story.getStoryCategoryId());
            storyValues.put(Constant.STORY_CREATE_DATE, story.getCreateDate());
            database.insert(Constant.TABLE_STORY, null, storyValues);
        }
    }
}

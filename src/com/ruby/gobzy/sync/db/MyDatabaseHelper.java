package com.ruby.gobzy.sync.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.ruby.gobzy.sync.constant.Constant;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String USER_TABLE_CREATION = "create table "+ Constant.TABLE_USER+
            " ("+Constant.ID+ " integer primary key,"+
            Constant.USERNAME + " text not null, " +
            Constant.USER_PWD + " text not null);";
	
	private static final String CATEGORY_TABLE_CREATION = "create table "+ Constant.TABLE_CATEGORY+
		" ("+Constant.ID+ " integer primary key,"+
		Constant.CATEGORY_NAME + " text not null);";
		
	private static final String STORY_TABLE_CREATION = "create table "+ Constant.TABLE_STORY+
		" ("+Constant.ID+ " integer primary key,"+
		Constant.STORY_CATEGORY_ID + " integer, " +
		Constant.STORY_NAME + " text not null, " +
        Constant.STORY_CREATE_DATE + " integer not null, " +
		Constant.STORY_DESC + " text);";

	private Context myContext;
	public MyDatabaseHelper(Context context) {
		super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
		myContext = context;
	}
	
	public boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = Constant.DB_PATH + Constant.DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
    		Log.w(MyDatabaseHelper.class.getName(), "database does't exist yet." ); 
    	}
    	if(checkDB != null){	 
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(MyDatabaseHelper.class.getName(), CATEGORY_TABLE_CREATION);
		Log.w(MyDatabaseHelper.class.getName(), STORY_TABLE_CREATION);
		db.execSQL(CATEGORY_TABLE_CREATION);
		db.execSQL(STORY_TABLE_CREATION);
        db.execSQL(USER_TABLE_CREATION);
        /*db.execSQL("insert into "+Constant.TABLE_CATEGORY+" (category_name) values ('Category 1')");
        db.execSQL("insert into "+Constant.TABLE_STORY+" (story_category_id, story_name, story_desc) values (1,'story1 category 1','story 1 desc')");
        db.execSQL("insert into "+Constant.TABLE_STORY+" (story_category_id, story_name, story_desc) values (1,'story2 category 1','story 1 desc')");

        db.execSQL("insert into "+Constant.TABLE_CATEGORY+" (category_name) values ('Category 2')");
        db.execSQL("insert into "+Constant.TABLE_STORY+" (story_category_id, story_name, story_desc) values (2,'story3 category 2','story 1 desc')");
        db.execSQL("insert into "+Constant.TABLE_STORY+" (story_category_id, story_name, story_desc) values (2,'story4 category 2','story 1 desc')");*/
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MyDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + Constant.TABLE_STORY);
		db.execSQL("DROP TABLE IF EXISTS" + Constant.TABLE_CATEGORY);
		onCreate(db);
	}

}

package geez.jdevs.keradion.dbManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import geez.jdevs.keradion.models.Story$UserModel;
import geez.jdevs.keradion.models.StoryModel;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ArticleDB";
    private static final String TABLE_NAME = "Articles";
    private static final String KEY_ID = "sid";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_WRITER_ID = "writerId";
    private static final String KEY_NO_OF_WELL_WRITTEN = "no_of_well_written";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_GTOPIC = "gTopic";
    private static final String KEY_STOPIC = "sTopic";
    private static final String KEY_WRITTEN_DATE = "writtenDate";
    private static final String[] columns = {KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_NO_OF_WELL_WRITTEN,
            KEY_FULL_NAME,KEY_STOPIC, KEY_WRITTEN_DATE};

    public SQLiteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATION = "CREATE TABLE Articles ( "
                + "sid TEXT, " + "title TEXT, "
                + "content TEXT, " + "writerId TEXT, "
                + "no_of_well_written TEXT, " + "full_name TEXT, "
                + "gTopic TEXT, " + "sTopic TEXT, "
                + "writtenDate TEXT )";
        sqLiteDatabase.execSQL(CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public boolean deleteOneArticle(StoryModel article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "sid = ?", new String[]{String.valueOf(article.getSid())});
        db.close();
        return true;
    }

    public boolean deleteAllArticle() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ TABLE_NAME;
        db.delete(TABLE_NAME,"1",null);
        db.close();
        return true;
    }

    public Story$UserModel getArticle(String sid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, " sid = ?",
                new String[]{String.valueOf(sid)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        Story$UserModel article = new Story$UserModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
        return article;
    }

    public ArrayList<Story$UserModel> allArticles() {
        ArrayList<Story$UserModel> articleList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        Story$UserModel article = null;
        if(cursor.moveToFirst()) {
            do {
                article = new Story$UserModel(
                        cursor.getString(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(KEY_NO_OF_WELL_WRITTEN)),
                        cursor.getString(cursor.getColumnIndex(KEY_WRITTEN_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_STOPIC)));

                articleList.add(article);
            } while (cursor.moveToNext());
        }
        return articleList;
    }

    public void addArticle(Story$UserModel article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, article.getSid());
        values.put(KEY_TITLE, article.getTitle());
        values.put(KEY_CONTENT, article.getStoryContent());
        values.put(KEY_NO_OF_WELL_WRITTEN, article.getNo_of_well_written());
        values.put(KEY_WRITTEN_DATE, article.getWritten_date());
        values.put(KEY_FULL_NAME, article.getWriterName());
        values.put(KEY_STOPIC, article.getsTopic());

        System.out.println("topic: "+article.getWritten_date());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
}

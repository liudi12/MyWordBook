package jp.ac.jec.cm0113.mywordbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CardSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "CARD_DB";
    public static final int version = 1;
    public static final String TABLE_NAME = "CARD";

    public CardSQLiteOpenHelper(Context context){
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //単語データのテーブルを作成する
        sqLiteDatabase.execSQL("CREATE TABLE " +
                TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Japanese TEXT, Chinese TEXT)");

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (1, '引数', '参数')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (2, '配列', '数组')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (3, '関数', '函数')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     * 登録データを全件検索し結果を返すメソッド
     * @return
     */
    public ArrayList<Card> getAllCard() {
        ArrayList<Card> cards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }

        try {
            //検索実行
            String[] column = new String[]{"Chinese", "Japanese", "_id"};
            Cursor cursor = db.query(TABLE_NAME, column, null, null, null, null, null);

            //取得したデータをCursorオブジェクトから順次取り出す
            while (cursor.moveToNext()) {
                Card tmp = new Card(cursor.getString(cursor.getColumnIndex("Chinese")),
                        cursor.getString(cursor.getColumnIndex("Japanese")),
                        cursor.getInt(cursor.getColumnIndex("_id")));
                cards.add(tmp);
            }
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return cards;
    }

    /**
     * 新しいカードを追加する
     * @param newCard
     * @return TRUE:成功 FALSE:失敗
     */
    public boolean insertCard(Card newCard) {
        long result;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("Japanese", newCard.getJapanese());
            values.put("Chinese", newCard.getChinese());

            result = db.insert(TABLE_NAME, null, values);
        } finally {
            db.close();
        }
        return result != -1;
    }

    /**
     * 重複登録チェック
     * @param japanese
     * @return
     */
    public boolean isExistWord (String japanese) {
        boolean result = false;
        SQLiteDatabase db = getReadableDatabase();

        try {
            //条件検索を行う
            String[] column = new String[]{"Japanese"};
            Cursor cursor = db.query(TABLE_NAME, column, "Japanese = ?", new String[]{japanese},
                    null, null, null);

            //重複してるかどうか
            if(cursor.getCount() != 0) {
                result = true; //重複している
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    /**
     * データベースから削除する
     * @param id　削除したいデータのテーブル内の_id
     */
    public void deleteCardById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            String sid = String.valueOf(id);
            result = db.delete(TABLE_NAME, "_id = ?", new String[]{sid});
            if (result == -1) {
                throw new SQLiteException();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * idによって、カードの情報を取得する
     * @param id
     * @return
     */
    public Card findCardById(int id) {
        Card tmp = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            String sid = String.valueOf(id);
            String[] column = new String[]{"Chinese", "Japanese", "_id"};
            Cursor cur = db.query(TABLE_NAME, column,
                    "_id = ?", new String[]{sid}, null, null, null);
            if (cur.moveToNext()) {
                tmp = new Card(cur.getString(0), cur.getString(1), cur.getInt(2));
            }
            cur.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return tmp;
    }

    /**
     * カード情報更新する
     * @param newCard
     * @return TRUE:成功 FALSE:失敗
     */
    public boolean updateCard(Card newCard) {
        long result = -1;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues(); //アップデートデータの準備
            values.put("Japanese", newCard.getJapanese());
            values.put("Chinese", newCard.getChinese());
            String sid = String.valueOf(newCard.getId());
            result = db.update(TABLE_NAME, values, "_id = ?",
                    new String[]{sid});
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result != -1;
    }
}

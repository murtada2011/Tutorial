package com.codingelab.tutorial.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codingelab.tutorial.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_STREET = "street";
    public static final String CONTACTS_COLUMN_CITY = "place";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String s = "(id integer primary key AUTOINCREMENT, name text,phone text,email text)";
        db.execSQL(
                "create table contacts " +
                        s//, street text,place text)"
        );
    }
      //ننفذ عمليه في الداته ثم نقوم بأعادت تحميلها
      //العمليه هنا حذف الجدول
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
    //اضافه
    public boolean insertContact  (String name, String phone, String email)//, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        //contentValues.put("street", street);
        //contentValues.put("place", place);
        db.insert("contacts", null, contentValues);
        return true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////
    // عرض عن طريق id ب update1
     public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }
    //عرض كل المتغيرت
    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        return res;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    // تنفيذ عملية الحذف بعد اختيار المستخدم
    public Cursor getDataDelete(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "delete from contacts where id ="+id, null );
        return res;
    }

    //عرض اخر اضافه
    public Cursor ShowLastRec()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor last =  db.rawQuery( " SELECT * FROM contacts WHERE id=(SELECT max(id) FROM contacts)", null );
        return last;
    }

    public boolean update1Contact (Integer id, String name, String phone, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }


    //حذف بطريقه اخرى
    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    ////////////////////////////////////////////////////////////////////////
    //عرض بطريقة اخرى
    public List<Student> getAllcontactDetails() {

        List<Student> contactList = new ArrayList<Student>();

        String selectQuery = "select * from contacts";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Student datastudent = new Student();

                datastudent.setId(cursor.getInt(0));
                datastudent.setName(cursor.getString(1));
                datastudent.setPhone(cursor.getString(2));
                datastudent.setEmail(cursor.getString(3));
                contactList.add(datastudent);
            } while (cursor.moveToNext());
        }
        return contactList;
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    public List getDataSearch(String Text){
        SQLiteDatabase db = this.getReadableDatabase();
      //  Cursor res =  db.rawQuery( "select * from contacts where name like "+Text+"%", null );
        List<Student> contactList = new ArrayList<Student>();

        String selectQuery = "select * from contacts";

        SQLiteDatabase database = this.getWritableDatabase();
        //بحث عن اي معلومه بالطريقه التي يفضلها المستخدم
        Cursor cursor = database.rawQuery("select * from contacts where name like '%"+
                Text+"%' or phone like '%"+Text+"%' or email like '%"+Text+"'" , null);
        if(cursor.moveToFirst()) {
            do {
                Student datastudent = new Student();
                datastudent.setId(cursor.getInt(0));
                datastudent.setName(cursor.getString(1));
                datastudent.setPhone(cursor.getString(2));
                datastudent.setEmail(cursor.getString(3));
                contactList.add(datastudent);
            } while (cursor.moveToNext());
        }
        return contactList;
    }
}
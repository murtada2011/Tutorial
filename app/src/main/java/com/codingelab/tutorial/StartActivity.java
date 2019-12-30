package com.codingelab.tutorial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.codingelab.tutorial.Local.DBHelper;
import com.codingelab.tutorial.Local.MainActivityLocal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    //Button ToPhp,ToLocal,Syn;
    DBHelper mydb;
    Syn syn;
    Cursor cursor;
    ImageView local,php,Syn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setTitle("Welcome");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //ToPhp =(Button)findViewById(R.id.bttnToPhp);
        //ToLocal =(Button)findViewById(R.id.bttnToLocal);
        //ToLocal =(Button)findViewById(R.id.bttnToLocal);
        //Syn =(Button)findViewById(R.id.Syn);
        mydb = new DBHelper(this);
        syn=new Syn();

        /////////////////////////////////////////////////////////////////////////////////////////////////
        local=(ImageView)findViewById(R.id.local);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateStudent = new Intent(StartActivity.this, MainActivityLocal.class);
                startActivity(updateStudent);
            }
        });
        ///////////////////////////////////////////////////////////////////////////
        php=(ImageView)findViewById(R.id.php);
        php.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateStudent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(updateStudent);
            }
        });
        //////////////////////////////////////////////////
        //اكواد المزامنه

        Syn=(ImageView)findViewById(R.id.sync);
        Syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////////////////////////////////////////////
                getJSON(syn.URL+"mysql_read.php");
                /////////////////////////////////////////////////////////////

               //syn.doInBackground("Truncate");
                syntoonline();
            }
        });
    }


    private void getJSON(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            //this method will be called after execution
            //so here we are displaying a toast with the json string
            /*@Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }*/
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), "finish SYN", Toast.LENGTH_SHORT).show();
                try {
                    //========
                    loadData2(s);
                    //========
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {



                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }
                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    ///////////////////
    private void loadData2(String json) throws JSONException {
        //SQLiteDatabase db = mydb.getReadableDatabase();
        JSONArray jsonArray = new JSONArray(json);
        // يقارن
        int ii=1; String nameL=""; String nameP="";String phoneP="";String emailP="";
        //String[] heroes = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
             nameP=obj.get("name").toString();
             phoneP=obj.get("phone").toString();
            emailP=obj.get("email").toString();
            System.out.println("ii="+ii);
            //System.out.println("i="+i);
            //String nameS = "";
           // System.out.println("+"+nameP+"+");
            System.out.println("namePHP = "+nameP);
            cursor=mydb.getAllData();

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                     nameL=cursor.getString(1);
                    System.out.println("nameLocal = "+nameL);
                    System.out.println("===============");
                    if(nameP.equals(nameL)){
                        System.out.println(" yeah ");
                        System.out.println(nameL+" "+nameP);
                        ii=0;
                        //String msg=syn.doInBackground("insert",nameL,"te","te");
                        //Toast.makeText(getBaseContext(),msg, Toast.LENGTH_SHORT).show();
                    } /*else{
                       ii=1;
                        System.out.println("no"+ii);
                        nameS=nameL;
                    }*/
                cursor.moveToNext();
            }
            if(ii==1){
                System.out.println("'"+nameL+"'");
              //  syn.doInBackground("insert",nameL,"te","te");
                mydb.insertContact(nameP,phoneP,emailP);
                //nameS="";
                System.out.println("");
               // ii=0;
            }

                ii = 1;
                System.out.println("=/=/=/=/=/=/=");
        }

    }
    //بعد المقارنه والتعديل احذف المعلومات الي باداته الخارجيه وضيف فيه معلومات الداته الداخليه من جديد
    private void syntoonline(){
        syn.doInBackground("Truncate");
        cursor=mydb.getAllData();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String email = cursor.getString(3);
            syn.doInBackground("insert",name,phone,email);
            cursor.moveToNext();
        }
    }
}

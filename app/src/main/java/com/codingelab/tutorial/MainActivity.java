package com.codingelab.tutorial;

import android.app.Activity;
import android.content.Intent;//jنقل بين الصفحات
import android.os.AsyncTask;//الاتصال
import android.os.Bundle;//
import android.os.StrictMode;
import android.view.View;//العرض
import android.widget.Button;//التصميم
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;//يحفظ المصفوفه داخل مصفوفه
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;//
import java.io.InputStreamReader;
import java.net.HttpURLConnection;//حق ربط link
import java.net.URL;
import java.util.ArrayList;//للمصفوفه
import java.util.HashMap;

public class MainActivity extends Activity {
    private Button bttnToAddP,bttnToUpP,bttnToDelP,bttnToSrchP;
    private Syn syn;
    private ListView listViewP;
    //tools:context=".Local.MainActivityLocal">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Online Database");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //سوينا ربط مع الصفحه لاجل التفاعل مع الداته الخارجيه
        this.syn=new Syn();
        listViewP=(ListView)findViewById(R.id.listViewP);
        getJSON(syn.URL+"mysql_read.php");


        ////////////////////////////////////////////////////////////////////////////////////////////
        //انتقال لصفحة الاضافه
        this.bttnToAddP=(Button)findViewById(R.id.bttnToAddP);
        bttnToAddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateStudent = new Intent(MainActivity.this, InsertActivityPhp.class);
                startActivity(updateStudent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        this.bttnToUpP=(Button)findViewById(R.id.bttnToUpdateP);
        bttnToUpP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateStudent = new Intent(MainActivity.this, UpdateActivityPhp.class);
                startActivity(updateStudent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        this.bttnToDelP=(Button)findViewById(R.id.bttnToDeleteP);
        bttnToDelP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateStudent = new Intent(MainActivity.this, DeleteActivityPhp.class);
                startActivity(updateStudent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        this.bttnToSrchP=(Button)findViewById(R.id.bttnToSearchP);
        bttnToSrchP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateStudent = new Intent(MainActivity.this, SearchActivityPhp.class);
                startActivity(updateStudent);
            }
        });

    }
    //this method is actually fetching the json string
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
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
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
    ArrayList<HashMap<String, String>> Items = new ArrayList<HashMap<String, String>>();
    ArrayList<String> test = new ArrayList<String>();
    private void loadIntoListView(String json) throws JSONException {
        Items.clear();
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            JSONObject obj = jsonArray.getJSONObject(i);
            String idd = heroes[i] = obj.getString("id");
            map.put("id",idd);
            String name = heroes[i] = obj.getString("name");
            map.put("name",name);
            String phone = heroes[i] = obj.getString("phone");
            map.put("phone",phone);
            String email = heroes[i] = obj.getString("email");
            map.put("email",email);
            Items.add(map);
            test.add(map.toString());
        }
        System.out.println(test);

        ListAdapter myadapter = new SimpleAdapter(this, Items,
                R.layout.listview_rows,new String[] { "id", "name", "phone", "email"},
                new int[] {R.id.idText, R.id.NameText, R.id.PhoneText, R.id.EmailText});
        listViewP.setAdapter(myadapter);

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        //listview.setAdapter(arrayAdapter);

    }
}


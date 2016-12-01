package android.pms.unipi.androidforumbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    public static final String TOPICS_ACTIVITY ="TOPICS";
    public static final String POSTS_ACTIVITY = "POSTS";
    public static final String LOGIN_ACTIVITY = "LOGIN";
    public static final String REGISTER_ACTIVITY = "REGISTER";
    public static SharedPreferences mSharedPrefs;
    public static SharedPreferences.Editor mSharedEditor;

    static ArrayList<String> listItems=new ArrayList<String>();
    static ArrayAdapter<String> adapterMain;
    ListView forumsListView;
    static String serverUrl = "http://192.168.1.10/phpBB/android_api/";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPrefs=getSharedPreferences("Login", 0);

        forumsListView = (ListView)findViewById(R.id.forums_listview);
        adapterMain = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        forumsListView.setAdapter(adapterMain);

        new JsonTask().execute(serverUrl+"fetch_forums.php");

        forumsListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(MainActivity.this, TopicsActivity.class);
                String message = forumsListView.getItemAtPosition(position).toString();
                intent.putExtra("FORUM_NAME", message);
                startActivity(intent);
            }
        });
    }



   static public void stringToListView(String input,ArrayList<String> list)
    {
        input = removeHtmlChars(input);

        input = input.replace("\n", "");
        input =  input.replace("\"","");
        String[] test = input.split("\\],\\[");
        test[0] = test[0].replace("[","");
        test[test.length-1] = test[test.length-1].replace("]","");
        list.clear();

        for (int i = 0; i < test.length; i++)
            list.add(test[i]);
    }

    public static String removeHtmlChars(String input)
    {
        String find = "<br />";
        if(input.contains(find))
            input = input.substring(input.lastIndexOf(find)+find.length());
        return input;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.preferences:
                intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                if(mSharedEditor!= null)
                {
                    mSharedEditor.putString("Username", "");
                    mSharedEditor.putBoolean("LoggedIn", false);
                    mSharedEditor.commit();
                }
                makeToast(this, "Successfully logged out");
                return true;
            case R.id.register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                return true;
            case R.id.check_login:
                if(mSharedPrefs.getBoolean("LoggedIn",false))
                    Log.d("Response","You are logged in");
                else
                    Log.d("Response","No log in");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void makeToast(Context activityContext,String message)
    {
        Toast mToast = Toast.makeText(activityContext, message, Toast.LENGTH_LONG);
        mToast.show();

    }




}

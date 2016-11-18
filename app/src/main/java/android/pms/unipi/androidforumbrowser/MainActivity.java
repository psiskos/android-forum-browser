package android.pms.unipi.androidforumbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    //static TextView testTextView;
    static String forumData;
    static ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    static ListView forumsListView;
    static String url = "http://192.168.1.10/phpbb/fetch.php";


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forumsListView = (ListView)findViewById(R.id.forums_listview);
        //testTextView = (TextView)findViewById(R.id.testTextview);
        new JsonTask().execute(url);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        forumsListView.setAdapter(adapter);
    }

   static public void stringToListView(String input,ArrayList<String> list)
    {
        input =  input.replace("[","");
        input =  input.replace("]","");
        input =  input.replace("\"","");
        String[] test = input.split(",");
        for (int i = 0; i < test.length; i++)
            list.add(test[i]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.preferences:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}

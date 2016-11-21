package android.pms.unipi.androidforumbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    static ArrayList<String> listItems=new ArrayList<String>();
    static ArrayAdapter<String> adapterMain;
    ListView forumsListView;
    static String serverUrl = "http://192.168.1.10/phpBB/android_api/";


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        String phpWarnings = "<br />\n" +
        "<b>Deprecated</b>:  Automatically populating $HTTP_RAW_POST_DATA is deprecated and will be removed in a future version. To avoid this warning set 'always_populate_raw_post_data' to '-1' in php.ini and use the php://input stream instead. in <b>Unknown</b> on line <b>0</b><br />\n" +
        "<br />\n" +
        "<b>Warning</b>:  Cannot modify header information - headers already sent in <b>Unknown</b> on line <b>0</b><br />";
        input = input.replace(phpWarnings,"");
        input =  input.replace("[","");
        input =  input.replace("]","");
        input =  input.replace("\"","");
        input = input.replace("\n", "");
        //input = input.replace("<b>","");
        //input = input.replace("</b>","");
        //input = input.replace("<br />","");
        list.clear();
        String[] test = input.split(",");

        for (int i = 0; i < test.length; i++){
            list.add(test[i]);
            }

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

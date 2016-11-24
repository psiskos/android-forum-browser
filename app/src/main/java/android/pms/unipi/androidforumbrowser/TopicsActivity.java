package android.pms.unipi.androidforumbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;


public class TopicsActivity extends AppCompatActivity
{

    static ArrayList<String> topicsListItems=new ArrayList<String>();;
    static ArrayAdapter<String> adapterTopics = null;
    ListView topicsListView = null;
    String forum_name;
    String topicsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        topicsListView = (ListView)findViewById(R.id.topicsListView);
        adapterTopics = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicsListItems);
        topicsListView.setAdapter(adapterTopics);

        forum_name = getIntent().getExtras().getString("FORUM_NAME");
        topicsUrl = serverUrl +"fetch_topics.php";
        String topicsToRequest = Integer.toString(PreferencesActivity.numOfTopics);

        new JsonTaskPost().execute(topicsUrl,forum_name,topicsToRequest);

        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(TopicsActivity.this, PostsActivity.class);
                String message = topicsListView.getItemAtPosition(position).toString();
                intent.putExtra("TOPIC_NAME", message);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
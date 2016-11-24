package android.pms.unipi.androidforumbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;


public class TopicsActivity extends AppCompatActivity
{

    static ArrayList<String> topicsListItems=new ArrayList<String>();;
    public static ArrayAdapter<String> adapterTopics = null;
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
    }

}
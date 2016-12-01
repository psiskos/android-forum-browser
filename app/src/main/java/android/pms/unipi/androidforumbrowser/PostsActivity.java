package android.pms.unipi.androidforumbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.pms.unipi.androidforumbrowser.MainActivity.POSTS_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedEditor;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedPrefs;
import static android.pms.unipi.androidforumbrowser.MainActivity.makeToast;
import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;

public class PostsActivity extends AppCompatActivity
{
    String topic_name;
    static ArrayList<String> postsListItems=new ArrayList<String>();;
    static ArrayAdapter<String> adapterPosts = null;
    ListView postsListView = null;
    String postsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        postsListView = (ListView)findViewById(R.id.posts_list_view);
        adapterPosts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, postsListItems);
        postsListView.setAdapter(adapterPosts);

        topic_name = getIntent().getExtras().getString("TOPIC_NAME");
        postsUrl = serverUrl +"fetch_posts.php";
        String postsToRequest = Integer.toString(PreferencesActivity.numOfPosts);

        new JsonTaskPost().execute(postsUrl,topic_name,postsToRequest,POSTS_ACTIVITY);
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
                mSharedEditor.putString("Username","");
                mSharedEditor.putBoolean("LoggedIn",false);
                mSharedEditor.commit();
                makeToast(this,"Successfully logged out");
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
}

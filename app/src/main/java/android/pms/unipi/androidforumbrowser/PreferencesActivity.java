package android.pms.unipi.androidforumbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;

public class PreferencesActivity extends AppCompatActivity
{
    public static int numOfTopics=5;
    public static int numOfPosts=5;

    Integer[] numbers = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

    TextView apiSettings,displaySettings,forumUrlTextView,postsPerPage,topicsPerPage;
    EditText apiUrlEditTextView;
    Spinner topics_spinner,posts_spinner;
    Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        apiSettings = (TextView)findViewById(R.id.apiSettings);
        displaySettings = (TextView)findViewById(R.id.displaySettings);
        forumUrlTextView = (TextView)findViewById(R.id.forumUrlTextView);
        postsPerPage = (TextView)findViewById(R.id.postsPerPage);
        topicsPerPage = (TextView)findViewById(R.id.topicsPerPage);

        apiUrlEditTextView = (EditText)findViewById(R.id.apiUrlEditTextView);

        topics_spinner = (Spinner)findViewById(R.id.topics_spinner);
        posts_spinner = (Spinner)findViewById(R.id.posts_spinner);

        ArrayAdapter<Integer> numbersAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item,numbers);

        topics_spinner.setAdapter(numbersAdapter);
        posts_spinner.setAdapter(numbersAdapter);

        save_button = (Button)findViewById(R.id.save_button);

        topics_spinner.setSelection(numOfTopics-1);
        posts_spinner.setSelection(numOfPosts-1);
        apiUrlEditTextView.setText(serverUrl);

        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                numOfTopics = topics_spinner.getSelectedItemPosition()+1;
                numOfPosts = posts_spinner.getSelectedItemPosition()+1;
                serverUrl = apiUrlEditTextView.getText().toString();
            }
        });

    }
}

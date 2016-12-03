package android.pms.unipi.androidforumbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.pms.unipi.androidforumbrowser.MainActivity.NEWTOPIC_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedPrefs;
import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;

public class NewTopicActivity extends AppCompatActivity
{
    String forum_name;
    EditText topicNameEditTxv;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);

        topicNameEditTxv = (EditText)findViewById(R.id.new_topic_editText);
        Button confirmButton = (Button)findViewById(R.id.confirm_button);

        forum_name = getIntent().getExtras().getString("FORUM_NAME");
        url = serverUrl + "new_topic.php";

        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mSharedPrefs.getBoolean("LoggedIn",false))
                {
                    MainActivity.makeToast(NewTopicActivity.this,"Loggin required");
                }
                else if(topicNameEditTxv.getText().toString().matches(""))
                {
                    MainActivity.makeToast(NewTopicActivity.this,"Insert Topic Name");
                }
                else
                {
                    new JsonTaskPost().execute(url,
                            forum_name,
                            topicNameEditTxv.getText().toString(),
                            NEWTOPIC_ACTIVITY,
                            mSharedPrefs.getString("Username",null));
                }

            }
        });
    }
}

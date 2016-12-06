package android.pms.unipi.androidforumbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.pms.unipi.androidforumbrowser.MainActivity.NEWPOST_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedPrefs;
import static android.pms.unipi.androidforumbrowser.MainActivity.makeToast;
import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;

public class NewPostActivity extends AppCompatActivity
{
    //  params that will be sent to AsynckTask JsonTaskPost
    String topic_name,forum_name;
    EditText postTextEditTxv;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        postTextEditTxv = (EditText)findViewById(R.id.post_text);
        Button confirmButton = (Button)findViewById(R.id.confirm_button_post);

        topic_name = getIntent().getExtras().getString("TOPIC_NAME");
        forum_name = getIntent().getExtras().getString("FORUM_NAME");
        url = serverUrl + "new_post.php";

        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mSharedPrefs.getBoolean("LoggedIn",false))
                {
                    makeToast(NewPostActivity.this,"Login required");
                }
                else if(postTextEditTxv.getText().toString().matches(""))
                {
                    makeToast(NewPostActivity.this,"Empty post message");
                }
                else
                {
                    new JsonTaskPost().execute(url,
                            topic_name,
                            postTextEditTxv.getText().toString(),
                            NEWPOST_ACTIVITY,
                            forum_name,
                            mSharedPrefs.getString("Username",null));
                    makeToast(NewPostActivity.this,"Post Created");
                }

            }
        });
    }
}

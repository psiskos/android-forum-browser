package android.pms.unipi.androidforumbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.pms.unipi.androidforumbrowser.MainActivity.REGISTER_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedEditor;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedPrefs;
import static android.pms.unipi.androidforumbrowser.MainActivity.makeToast;
import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;

public class RegisterActivity extends AppCompatActivity
{
    public static TextView registerServerMessageTxv;
    EditText usernameEditTxv,passwordEditTxv,emailEditTxv;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerServerMessageTxv = (TextView)findViewById(R.id.server_message);
        usernameEditTxv = (EditText)findViewById(R.id.username_edittextview);
        passwordEditTxv = (EditText)findViewById(R.id.password_edittextview);
        emailEditTxv = (EditText)findViewById(R.id.email_edittextview);

        registerButton = (Button)findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String url = serverUrl + "register.php";
                if (usernameEditTxv.getText().toString()!="" && passwordEditTxv.getText().toString()!="")
                {
                    new JsonTaskPost().execute(url,usernameEditTxv.getText().toString()
                            ,passwordEditTxv.getText().toString()
                            ,REGISTER_ACTIVITY
                            ,emailEditTxv.getText().toString());
                }
                else
                {
                    makeToast(RegisterActivity.this,"No empty fields allowed");}

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
                finish();
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

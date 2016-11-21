package android.pms.unipi.androidforumbrowser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;
import static android.pms.unipi.androidforumbrowser.MainActivity.stringToListView;


public class TopicsActivity extends AppCompatActivity
{

    static ArrayList<String> topicsListItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView topicsListView;
    String forum_name;
    String topicsUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        topicsListView = (ListView)findViewById(R.id.topicsListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicsListItems);
        topicsListView.setAdapter(adapter);
        forum_name = getIntent().getExtras().getString("FORUM_NAME");
        topicsUrl = serverUrl +"fetch_topics.php";

        new JsonTaskPost().execute(topicsUrl,forum_name);
    }


    public class JsonTaskPost extends AsyncTask<String, String, String>
    {
        String message = null;

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            OutputStream outStream = null;

            try {
                URL url = new URL(params[0]);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("forum_name", params[1]);
                message = jsonObject.toString();

                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout( 10000 /*milliseconds*/ );
                connection.setConnectTimeout( 15000 /* milliseconds */ );
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setFixedLengthStreamingMode(message.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                //connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                //connection.connect();

                //setup send
                outStream = new BufferedOutputStream(connection.getOutputStream());
                outStream.write(message.getBytes());
                //clean up
                outStream.flush();


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                return buffer.toString();


            } catch (org.json.JSONException e){
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                stringToListView(result,topicsListItems);}
            super.onPostExecute(result);
        }
    }
}
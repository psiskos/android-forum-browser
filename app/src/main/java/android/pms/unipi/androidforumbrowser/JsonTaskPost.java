package android.pms.unipi.androidforumbrowser;

import android.os.AsyncTask;
import android.util.Log;

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

import static android.pms.unipi.androidforumbrowser.LoginActivity.serverMessageTxv;
import static android.pms.unipi.androidforumbrowser.MainActivity.LOGIN_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.NEWPOST_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.NEWTOPIC_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.POSTS_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.REGISTER_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.TOPICS_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedEditor;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedPrefs;
import static android.pms.unipi.androidforumbrowser.MainActivity.removeHtmlChars;
import static android.pms.unipi.androidforumbrowser.MainActivity.stringToListView;
import static android.pms.unipi.androidforumbrowser.PostsActivity.adapterPosts;
import static android.pms.unipi.androidforumbrowser.PostsActivity.postsListItems;
import static android.pms.unipi.androidforumbrowser.TopicsActivity.adapterTopics;
import static android.pms.unipi.androidforumbrowser.TopicsActivity.topicsListItems;


public class JsonTaskPost extends AsyncTask<String, String, String>
{
    String message = null;
    String callingActivity;
    String usernameSharedPrefs;

    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;
        OutputStream outStream = null;
        usernameSharedPrefs = params[1];
        callingActivity = params[3];

        try {
            URL url = new URL(params[0]);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", params[1]);
            jsonObject.put("password", params[2]);

            if(callingActivity.equals(TOPICS_ACTIVITY))
            {
                jsonObject.put("forum_name", params[1]);
                jsonObject.put("number_of_topics", params[2]);
            }
            else if(callingActivity.equals(POSTS_ACTIVITY))
            {
                jsonObject.put("topic_name", params[1]);
                jsonObject.put("number_of_posts", params[2]);
            }
            else if(callingActivity.equals(REGISTER_ACTIVITY))
            {
                jsonObject.put("username", params[1]);
                jsonObject.put("password", params[2]);
                jsonObject.put("email", params[4]);
            }
            else if(callingActivity.equals(NEWTOPIC_ACTIVITY))
            {
                jsonObject.put("forum_name", params[1]);
                jsonObject.put("topic_title", params[2]);
                jsonObject.put("username", params[4]);
            }
            else if(callingActivity.equals(NEWPOST_ACTIVITY))
            {
                jsonObject.put("topic_name", params[1]);
                jsonObject.put("forum_name", params[4]);
                jsonObject.put("username", params[5]);
                jsonObject.put("post_text", params[2]);
            }
            message = jsonObject.toString();

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(message.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            outStream = new BufferedOutputStream(connection.getOutputStream());
            outStream.write(message.getBytes());
            //outStream.flush();
            outStream.close();

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
    protected void onPostExecute(String result)
    {
        if(result!=null)
        {
            if(callingActivity.equals(TOPICS_ACTIVITY))
            {
                stringToListView(result,topicsListItems);
                adapterTopics.notifyDataSetChanged();
            }
            else if(callingActivity.equals(POSTS_ACTIVITY))
            {
                stringToListView(result,postsListItems);
                adapterPosts.notifyDataSetChanged();
            }
            else if(callingActivity.equals(LOGIN_ACTIVITY))
            {
                result = removeHtmlChars(result);
                serverMessageTxv.setText(result);
                mSharedEditor = mSharedPrefs.edit();
                if (result.contains("You are logged in"))
                {
                    mSharedEditor.putString("Username",usernameSharedPrefs);
                    mSharedEditor.putBoolean("LoggedIn",true);
                    mSharedEditor.commit();
                }
                else
                {
                    mSharedEditor.putString("Username","");
                    mSharedEditor.putBoolean("LoggedIn",false);
                    mSharedEditor.commit();
                }
            }

        }
        super.onPostExecute(result);
    }
}

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

import static android.pms.unipi.androidforumbrowser.MainActivity.stringToListView;
import static android.pms.unipi.androidforumbrowser.TopicsActivity.adapterTopics;
import static android.pms.unipi.androidforumbrowser.TopicsActivity.topicsListItems;


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
            jsonObject.put("number_of_topics", params[2]);
            message = jsonObject.toString();

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(message.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            outStream = new BufferedOutputStream(connection.getOutputStream());
            outStream.write(message.getBytes());
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
            stringToListView(result,topicsListItems);
            adapterTopics.notifyDataSetChanged();}
        super.onPostExecute(result);
    }
}

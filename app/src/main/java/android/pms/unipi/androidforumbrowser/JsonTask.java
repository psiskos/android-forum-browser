package android.pms.unipi.androidforumbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.pms.unipi.androidforumbrowser.MainActivity.MAIN_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.MAPS_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.adapterMain;
import static android.pms.unipi.androidforumbrowser.MainActivity.listItems;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedPrefs;
import static android.pms.unipi.androidforumbrowser.MainActivity.stringToListView;
import static android.pms.unipi.androidforumbrowser.MapsActivity.mMap;

//get-only request class
public class JsonTask extends AsyncTask<String, String, String>
{
    String callingActivity;

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        //on JsonTask string callingActivity is always param[1l
        callingActivity = params[1];

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);

            }
            return buffer.toString();


        } catch (MalformedURLException e) {
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
            if(callingActivity.equals(MAIN_ACTIVITY))
            {
                stringToListView(result,listItems);
                adapterMain.notifyDataSetChanged();
            }
            else if(callingActivity.equals(MAPS_ACTIVITY))
                MapsActivity.addMapPin(result,mSharedPrefs.getString("Username",null),mMap);
        }
        super.onPostExecute(result);
    }
}

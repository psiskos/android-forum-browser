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

import static android.pms.unipi.androidforumbrowser.MainActivity.adapterMain;
import static android.pms.unipi.androidforumbrowser.MainActivity.listItems;
import static android.pms.unipi.androidforumbrowser.MainActivity.stringToListView;


public class JsonTask extends AsyncTask<String, String, String>
{
    String logout;

    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected String doInBackground(String... params) {

        if (params.length == 2)
                logout = params[1];

        HttpURLConnection connection = null;
        BufferedReader reader = null;

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
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

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
    protected void onPostExecute(String result) {
        if(result!=null && logout == null)
        {
            stringToListView(result,listItems);
            adapterMain.notifyDataSetChanged();
        }
        super.onPostExecute(result);
    }
}

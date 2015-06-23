package nl.uva.kite.Doko;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.ContentBody;
import com.parse.entity.mime.content.FileBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class UploadInBackground extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... args) {
        try {
            sendPost("http://kite:GetMoney@www.intotheblu.nl/image/artin.jpg", args[0]);
            Log.d("", execute().toString());

        }
        catch(Exception e) {
            Log.e("", e.toString());
        }
        return null;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // do something with data here-display it or send to mainactivity
    }

    public static void sendPost(String url, String imagePath) throws IOException, ClientProtocolException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost httppost = new HttpPost(url);
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("kite", "GetMoney");
        httppost.setHeader(BasicScheme.authenticate(creds, "UTF-8", false));
        File file = new File(imagePath);

        //StringEntity entity = new StringEntity( ac.toXMLString() );
        //entity.setContentType("text/xml");
        //httppost.setEntity(entity);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file, "image/*");
        mpEntity.addPart("userfile", cbFile);

        httppost.setEntity(mpEntity);
        Log.e("", "executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        Log.e("", response.getStatusLine().toString());
        if (resEntity != null) {
            Log.e("", EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
        httpclient.getConnectionManager().shutdown();
    }

}
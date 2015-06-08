package nl.uva.kite.Doko;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseInstallation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.uva.kite.Doko.JSONParser;



public class JSONRetrieve extends AsyncTask<String,String,String> {
    int post_exec;
    Context ctext;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    JSONObject json;
    List<NameValuePair> params;

    JSONRetrieve(Context c, List<NameValuePair> p, int type) {
        ctext = c;
        params = p;
        post_exec = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(ctext);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        json = jsonParser.makeHttpRequest(args[0], "POST", params);
        return null;
    }
    protected void onPostExecute(String ab){
        pDialog.dismiss();
        OnJSONCompleted.dotask(post_exec, json, ctext);
    }
}

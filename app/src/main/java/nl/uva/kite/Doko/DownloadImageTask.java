package nl.uva.kite.Doko;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;
import com.loopj.android.http.RequestParams;


public class DownloadImageTask {

    Bitmap bitmap;
    RequestParams params = new RequestParams();
    public DownloadImageTask() {
        bitmap = null;
    }

    public void setImageFromURL(final ImageView iv, final String... urls) {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
            };

            @Override
            protected String doInBackground(Void... Params) {
                String urldisplay = urls[0];
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error........", e.toString());
                    e.printStackTrace();
                }
                return "";
            }

            protected void onPostExecute(String msg) {
                iv.setImageBitmap(bitmap);
            }
        }.execute(null, null, null);

    }

}
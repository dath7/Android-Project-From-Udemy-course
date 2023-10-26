package com.example.top10dowloaded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listApps;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;

    private String feedCacheUrl = "INVALIDATED";
    public static final String STATE_URL = "feedUrl";
    public static final String STATE_LIMIT = "feedLimit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = (ListView) findViewById(R.id.xmlListView);

        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL);
            feedLimit = savedInstanceState.getInt(STATE_LIMIT);
        }
        dowloadUrl( String.format(feedUrl,feedLimit));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        if (feedLimit == 10 ) {
            menu.findItem(R.id.menu10).setChecked(true);

        }else {
            menu.findItem(R.id.menu25).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        switch (id) {
            case R.id.menuFree:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.menuPaid:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.menuSongs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.menu10:
            case R.id.menu25:
                if (!item.isChecked()) {
                   item.setChecked(true);
                   feedLimit = 35 - feedLimit;
                    Log.d(TAG,"onOptionsItemsSelected: " + item.getTitle()+ "setting feedLimit to" + feedLimit);
                }
                else {
                    Log.d(TAG,"onOptionsItemsSelected: " + item.getTitle() + "feedLimit unchanged");
                }
                break;
            case R.id.menuRefresh:
                // clear cache
                feedCacheUrl = "INVALIDATED";
            default:
                return super.onOptionsItemSelected(item);
        }
        dowloadUrl( String.format(feedUrl,feedLimit));
        return true;
    }

    private void dowloadUrl(String feedUrl) {
        if (!feedUrl.equalsIgnoreCase(feedCacheUrl)) {
        Log.d(TAG, "dowloadUrl: starting AsyncTask");
        DownLoadData downLoadData = new DownLoadData();
        downLoadData.execute(feedUrl);
        feedCacheUrl = feedUrl;
        Log.d(TAG, "dowloadUrl: done");
    }
    else Log.d(TAG, "dowloadUrl: URL not changed");}

    private class DownLoadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DowloadData";


        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG,"doInBackground: start with " + strings[0]);
            String rssFeed = dowloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error Dowloading");
            }
            return rssFeed;
        }

        @Override
        // run when progress completed
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Log.d(TAG,"onPostExecute: parameter is " + s);
            ParseApplication parseApplication = new ParseApplication();
            parseApplication.parse(s);
            // got data
     /*       ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
                  MainActivity.this,R.layout.list_item,parseApplication.getApplications()
            );
            listApps.setAdapter(arrayAdapter);*/
            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseApplication.getApplications());
            listApps.setAdapter(feedAdapter);
        }


        private String dowloadXML(String urlPath) {
            // useful when append a lot
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG,"dowloadXML: the response code was "+ response);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = bufferedReader.read(inputBuffer);
                    if (charsRead < 0)
                        break;
                    if (charsRead > 0)
                        // only copy number of characters read (charsRead)
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));

                }
                bufferedReader.close();
                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "dowloadXML: Invalid URL" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "dowloadXML: IO Exception reading data " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "dowloadXML: SecurityException. Need permission" + e.getMessage());
            }
            return null;
        }

    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString(STATE_URL,feedUrl);
        outState.putInt(STATE_LIMIT,feedLimit);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
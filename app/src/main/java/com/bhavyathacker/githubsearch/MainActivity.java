package com.bhavyathacker.githubsearch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bhavyathacker.githubsearch.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mEdtSearchBox;
    private TextView mTvUrlDisplay;
    private TextView mTvSearchResults;

    private TextView mTvErrorMessage;
    private ProgressBar mPbLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdtSearchBox = findViewById(R.id.edt_search_box);
        mTvUrlDisplay = findViewById(R.id.tv_url_display);
        mTvSearchResults = findViewById(R.id.tv_github_search_results_json);
        mTvErrorMessage = findViewById(R.id.tv_error_message_display);
        mPbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
    }

    void makeGithubSearchQuery() {
        String githubQuery = mEdtSearchBox.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mTvUrlDisplay.setText(githubSearchUrl.toString());
        new GithubQueryTask().execute(githubSearchUrl);
    }

    private void showJsonDataView() {
        mTvErrorMessage.setVisibility(View.INVISIBLE);
        mTvSearchResults.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mTvErrorMessage.setVisibility(View.VISIBLE);
        mTvSearchResults.setVisibility(View.INVISIBLE);
    }


    public class GithubQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                mTvSearchResults.setText(githubSearchResults);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedMenuItem = item.getItemId();
        if (selectedMenuItem == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

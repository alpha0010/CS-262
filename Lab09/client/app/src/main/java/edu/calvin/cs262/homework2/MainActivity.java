package edu.calvin.cs262.homework2;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Reads monopoly API and displays the results.
 */
public class MainActivity extends AppCompatActivity {

    private EditText playerIdText;

    private final List<Player> playerList = new ArrayList<>();
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerIdText = (EditText) findViewById(R.id.playerIdText);
        Button fetchButton = (Button) findViewById(R.id.fetchButton);
        itemsListView = (ListView) findViewById(R.id.playerListView);

        assert fetchButton != null;
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(playerIdText);
                new GetPlayerTask().execute(createURL(playerIdText.getText().toString()));
            }
        });
        playerIdText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().equals("0") && dstart == 0) {
                    return "";
                }
                return null;
            }
        }});
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param id the target user id, or empty for full listing
     * @return URL formatted for cs262 monopoly api
     */
    private URL createURL(String id) {
        try {
            String urlString;
            if (id.isEmpty()) {
                urlString = getString(R.string.list_players_url);
            } else {
                urlString = getString(R.string.get_player_url) + id;
            }
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    /**
     * Deitel's method for programmatically dismissing the keyboard.
     *
     * @param view the TextView currently being edited
     */
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Inner class for GETing the monopoly data asynchronously
     */
    private class GetPlayerTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    try {
                        // list all players
                        return new JSONArray(result.toString());
                    } catch (JSONException e) {
                        // single player id
                        JSONArray arr = new JSONArray();
                        arr.put(new JSONObject(result.toString()));
                        return arr;
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray players) {
            if (players != null) {
                convertJSONtoArrayList(players);
                MainActivity.this.updateDisplay();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Converts the JSON monopoly data to an arraylist suitable for a listview adapter
     *
     * @param players
     *  List of players to be converted to a standard list
     */
    private void convertJSONtoArrayList(JSONArray players) {
        playerList.clear(); // clear old players
        try {
            for (int i = 0; i < players.length(); i++) {
                JSONObject player = players.getJSONObject(i);
                playerList.add(new Player(
                        player.getInt("id"),
                        player.getString("emailaddress"),
                        player.has("name") ? player.getString("name") : "no name")
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the monopoly data on the ListView through a simple adapter
     */
    private void updateDisplay() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        for (Player item : playerList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("email", item.getEmail());
            data.add(map);
        }

        int resource = R.layout.player_item;
        String[] from = {"id", "name", "email"};
        int[] to = {R.id.idTextView, R.id.nameTextView, R.id.emailTextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

}

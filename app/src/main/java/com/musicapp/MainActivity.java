package com.musicapp;

 import android.content.res.Configuration;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.VolleyError;
 import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout mDrawerLayout;
    ListView listView,tagsListView;
    ArrayList<String> tagsArrayList,tracksArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {


            initPortrait();
        }
        else {
         initLandscape();
        }
    }


    public void initPortrait(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        listView = findViewById(R.id.listView);

        tagsArrayList = new ArrayList<>();
        tracksArrayList = new ArrayList<>();
        navigationView.setNavigationItemSelectedListener(this);

//        for (int i = 1; i <= 3; i++) {
//          menu.add()
//        }
        addMenus();
    }

    public void initLandscape(){
        setContentView(R.layout.activity_landscape_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        tagsListView = findViewById(R.id.tagsListView);
        listView = findViewById(R.id.listView);
        ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_dropdown_item_1line,tagsArrayList);
        tagsListView.setAdapter(arrayAdapter);


        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                adapterView.setSelected(true);
                onNavigationItemSelected(navigationView.getMenu().getItem(i));
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
    }


    //
    private void addMenus() {


        new MakeRequest(new HashMap<String, String>(),MainActivity.this,BaseUrl.lastFmBaseUrl+BaseUrl.topTags,
                Request.Method.GET,false){
            @Override
            public JSONObject getJsonResponse(JSONObject j) throws JSONException {

                JSONObject toptags=j.getJSONObject("toptags");
                    JSONArray tags=toptags.getJSONArray("tag");
                Menu menu = navigationView.getMenu();
                for(int i=0;i<=9;i++)
                {
                    JSONObject object=tags.getJSONObject(i);
                    String name =object.getString("name").toUpperCase();
                    tagsArrayList.add(name);
                    menu.add(1,i,1,name);
                    menu.findItem(i).setIcon(R.drawable.ic_menu_slideshow);
                }

                return null;
            }

            @Override
            public JSONArray getJSonArray(JSONArray jsonArray) {
                return null;
            }

            @Override
            public VolleyError getError(VolleyError geterror) {
                return null;
            }
        };


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        getSupportActionBar().setTitle(menuItem.getTitle());
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();

        addTracks(menuItem.getTitle());
        return true;
    }

    private void addTracks(CharSequence title) {

        tracksArrayList.clear();
        new MakeRequest(new HashMap<String, String>(),MainActivity.this,BaseUrl.lastFmBaseUrl+"/2.0/?method=tag.gettoptracks&tag="+title+"&api_key=b13e58a07b8c7e10ddc837da4c332461&format=json",
                Request.Method.GET,true){
            @Override
            public JSONObject getJsonResponse(JSONObject j) throws JSONException {

                JSONObject toptags=j.getJSONObject("tracks");
                JSONArray tags=toptags.getJSONArray("track");

                for(int i=0;i<=9;i++)
                {
                    JSONObject object=tags.getJSONObject(i);
                    String name =object.getString("name").toUpperCase();

                    tracksArrayList.add(name);
//                    Log.e("Name",name);

                }

                ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_dropdown_item_1line,tracksArrayList);
                listView.setAdapter(arrayAdapter);
                return null;
            }

            @Override
            public JSONArray getJSonArray(JSONArray jsonArray) {
                return null;
            }

            @Override
            public VolleyError getError(VolleyError geterror) {
                return null;
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        initLandscape();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        initPortrait();
        }
    }
}

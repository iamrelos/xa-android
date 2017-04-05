package io.keypunchers.xa.app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import io.keypunchers.xa.R;
import io.keypunchers.xa.loaders.DrawerBannerLoader;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.DrawerBanner;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<JSONObject> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_layout, new GamesFragment()).commit();

        getSupportLoaderManager().initLoader(1, null, this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_news) {

        } else if (id == R.id.nav_games) {
            trans.replace(R.id.main_layout, new GamesFragment(), "GAMES_FRAGMENT")
                    .commit();
        } else if (id == R.id.nav_latest_achievements) {

        } else if (id == R.id.nav_screenshots) {

        } else if (id == R.id.nav_upcoming_games) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {
            trans.replace(R.id.main_layout, new AboutFragment(), "ABOUT_FRAGMENT")
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_menu_donate) {
            Toast.makeText(this, "Donate Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        return new DrawerBannerLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {

        try {
            NetworkImageView mIvBannerImg = (NetworkImageView) findViewById(R.id.iv_drawer_banner);
            mIvBannerImg.setImageUrl(data.getString("image_url"), SingletonVolley.getImageLoader());

            TextView mTvTitle = (TextView) findViewById(R.id.tv_banner_title);
            mTvTitle.setText(data.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

    }
}
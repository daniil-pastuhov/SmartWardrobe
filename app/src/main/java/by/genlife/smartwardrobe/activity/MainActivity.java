package by.genlife.smartwardrobe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Tab;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.fragment.AddNewItemFragment;
import by.genlife.smartwardrobe.fragment.AutoSearchFragment;
import by.genlife.smartwardrobe.fragment.CatalogFragment;
import by.genlife.smartwardrobe.fragment.FindByTag;
import by.genlife.smartwardrobe.fragment.NavigationDrawerFragment;
import by.genlife.smartwardrobe.service.WeatherService;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, Constants {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int curFragment = 0;
    private CharSequence mTitle;
    private Fragment prevFragment;
    Apparel selectedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        if (savedInstanceState != null) {
            curFragment = savedInstanceState.getInt(STATE_CUR_FRAGMENT, 0);
        } else {
            startService(new Intent(this, WeatherService.class));
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment newFragment = null;
        curFragment = position;
        String tag = null;
        switch (position) {
            case 0:
                newFragment = AutoSearchFragment.getInstance();
                tag = AutoSearchFragment.TAG;
                break;
            case 1:
                newFragment = CatalogFragment.getInstance();
                tag = CatalogFragment.TAG;
                break;
            case 2:
                newFragment = FindByTag.getInstance();
                tag = FindByTag.TAG;
                break;
            case 3:
                mTitle = getString(R.string.main_menu_find_to_wash);
                break;
            case 4:
                mTitle = getString(R.string.main_menu_pack_to_trip);
                break;
            case 5:
                newFragment = new AddNewItemFragment();
                tag = AddNewItemFragment.TAG;
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, tag).commit();
    }

    public void onSectionAttached() {
        mTitle = Tab.getType(curFragment).getDescription();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case R.id.action_add:
                System.err.println(getSupportFragmentManager().getFragments());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new AddNewItemFragment(), AddNewItemFragment.TAG).commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CUR_FRAGMENT, curFragment);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, WeatherService.class));
        super.onDestroy();
    }
}

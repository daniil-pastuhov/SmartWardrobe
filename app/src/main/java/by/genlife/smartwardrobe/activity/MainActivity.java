package by.genlife.smartwardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Tab;
import by.genlife.smartwardrobe.fragment.AddNewItemFragment;
import by.genlife.smartwardrobe.fragment.AutoSearchFragment;
import by.genlife.smartwardrobe.fragment.CatalogFragment;
import by.genlife.smartwardrobe.fragment.FindByTagFragment;
import by.genlife.smartwardrobe.fragment.NavigationDrawerFragment;
import by.genlife.smartwardrobe.fragment.ToWashFragment;
import by.genlife.smartwardrobe.fragment.TravelFragment;
import by.genlife.smartwardrobe.service.WeatherService;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, Constants {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int curFragment = 0;
    private CharSequence mTitle;

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

    public void showFragment(int position, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment newFragment = null;
        curFragment = position;
        String tag = null;
        switch (position) {
            case 0:
                newFragment = new AutoSearchFragment();
                tag = AutoSearchFragment.TAG;
                break;
            case 1:
                newFragment = new CatalogFragment();
                tag = CatalogFragment.TAG;
                break;
            case 2:
                newFragment = new FindByTagFragment();
                tag = FindByTagFragment.TAG;
                break;
            case 3:
                newFragment = new ToWashFragment();
                tag = ToWashFragment.TAG;
                break;
            case 4:
                newFragment = new TravelFragment();
                tag = TravelFragment.TAG;
                break;
            case 5:
                newFragment = new AddNewItemFragment();
                tag = AddNewItemFragment.TAG;
        }
        if (bundle != null) newFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, tag).commit();
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
                newFragment = new AutoSearchFragment();
                tag = AutoSearchFragment.TAG;
                break;
            case 1:
                newFragment = new CatalogFragment();
                tag = CatalogFragment.TAG;
                break;
            case 2:
                newFragment = new FindByTagFragment();
                tag = FindByTagFragment.TAG;
                break;
            case 3:
                newFragment = new ToWashFragment();
                tag = ToWashFragment.TAG;
                break;
            case 4:
                newFragment = new TravelFragment();
                tag = TravelFragment.TAG;
                break;
            case 5:
                newFragment = new AddNewItemFragment();
                tag = AddNewItemFragment.TAG;
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
            Toast.makeText(this, "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }
        switch (id) {
//            case R.id.action_add:
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, new AddNewItemFragment(), AddNewItemFragment.TAG).commit();
//                break;
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

    public void hideKeyboard() {
            View view = this.getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
    }
}

package com.softjads.jorge.hellojesus.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseUser;
import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.ad.AdActivity;
import com.softjads.jorge.hellojesus.util.about.AboutActivity;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    public FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.bible_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bible_array, R.layout.my_spinner);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.my_spinner_open);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        spinner.setSelection(getIndex(spinner, sharedPref.getString(getString(R.string.preference_key_bible),null)));


        Fabric.with(this, new Crashlytics());





        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        initFragment(MainFragment.newInstance());

        Intent intent =
                new Intent(MainActivity.this, AdActivity.class);
        startActivity(intent);


    }


    private int getIndex(Spinner spinner, String myString){

        if (myString == null){
            myString = "0";
        }

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (i == (Integer.parseInt(myString))){
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //    outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_menu_credit:
                                Intent intent1 = new Intent(MainActivity.this, AdActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.list_menu_about:
                            //    initFragment(QuestionFragment.newInstance());
                                Intent intent =
                                         new Intent(MainActivity.this, AboutActivity.class);
                                 startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        // BEBETO
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        // return EspressoIdlingResource.getIdlingResource();
        return null;
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.contentFrame, fragment);
        transaction.commit();


    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.mensagem01))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                       // MainActivity.super.onBackPressed();
                        //finish();
                        //finishAffinity();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).create().show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.preference_key_bible), String.valueOf(position));
        editor.commit();

        initFragment(MainFragment.newInstance());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

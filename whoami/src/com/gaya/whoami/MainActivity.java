package com.gaya.whoami;

import android.content.*;
import android.content.pm.*;
import android.content.pm.PackageManager.*;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import com.facebook.*;
import com.facebook.Session.*;
import com.gaya.whoami.R.*;
import com.gaya.whoami.adapters.PlayersAdapter;
import com.gaya.whoami.database.FeedReaderDbHelper;
import com.gaya.whoami.fragments.*;

import java.security.*;

/**
 * Our main activity tat handles the basic user login and fragment flow Fragments are defined in Fragments
 *
 * @see com.gaya.whoami.fragments.Fragments
 */
public class MainActivity extends FragmentActivity {


    public GameSettings gameSettings;
    private SplashFragment splashFragment;
    private UiLifecycleHelper uiHelper;//fb ui lifecycle helper
    private boolean isResumed;//fb login state helper

    private final StatusCallback statusCallback = new StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionChanged(session, state, exception);
        }
    };//callback used by fb to notify us when the user's login session has changed
//private PlayersAdapter playersAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.settings:
                //when the settings action bar action is pressed, show the the fb user options

                showFragment(Fragments.SETTINGS, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Globals.init(this); //get all the statics that need a context up

        FeedReaderDbHelper helper = new FeedReaderDbHelper(this);
        SQLiteDatabase dbs = helper.getWritableDatabase();
        helper.deleteDataBase(dbs);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        printKeyHash();
        setContentView(R.layout.main);

        gameSettings = new GameSettings();
//        playersAdapter=new PlayersAdapter(this);
  //      Globals.getPlayerManager().scan();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);


    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            // if the session is already open, try to show the selection fragment
            showFragment(Fragments.WELCOME, false);

        } else {
            // otherwise present the splash screen and ask the user to login, unless the user explicitly skipped.
            showFragment(Fragments.SPLASH, false);
        }

    }

    /**
     * This is called when the facebook session is modified (login/logout) and we decide if to show the main fragment or
     * the splash
     */
    private void onSessionChanged(Session session, SessionState state, Exception exception) {
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            int backStackSize = manager.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            // check for the OPENED state instead of session.isOpened() since for the
            // OPENED_TOKEN_UPDATED state, the welcome fragment should already be showing.
            //showFragment(Fragments.SPLASH, false);
            if (state.equals(SessionState.OPENED)) {
                showFragment(Fragments.WELCOME, false);
            } else if (state.isClosed()) {
                showFragment(Fragments.SPLASH, false);
            }
        }


    }

    /**
     * helper function to display the given fragment
     *
     * @param fragment       the fragment to display
     * @param addToBackStack should this fragment be added to the BackStack
     */
    public void showFragment(Fragments fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(id.fragment_container, fragment.get(this));
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Helper function to print the sha1 key of the build to help register a new key with fb
     */
    void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gaya.whoami",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}
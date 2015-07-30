package dk.aau.mppss.friendfinder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.Arrays;

import dk.aau.mppss.friendfinder.view.Gui;
import dk.aau.mppss.friendfinder.view.fragments.MapsFragment;
import dk.aau.mppss.friendfinder.view.fragments.POIFragment;

/*
FragmentActivity extends ActionBarActivity so we only need to extend ActionBarActivity
to have controls on ActionBar and FragmentActivity
 */
public class MainActivity extends ActionBarActivity {
    private MapsFragment mapsFragment;
    private POIFragment poiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar SetUp:
        if(savedInstanceState != null) {
            this.mapsFragment = (MapsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MapsFragment_State");
        } else {
            this.mapsFragment = new MapsFragment();
        }
        this.poiFragment = new POIFragment();

        ActionBar actionBar = getSupportActionBar();
        //new Gui(actionBar);
        actionBar = Gui.actionBarConfigurations(actionBar);
        actionBar = Gui.addTabs(
                new ArrayList<String>(Arrays.asList("MapsModel View", "POI List")),
                new ArrayList<Fragment>(Arrays.asList(mapsFragment, poiFragment)),
                actionBar
        );
    }

    //Allows to replace a fragment with another:
    public void replaceFragment(Fragment fragment) {
        //We check if activity is finished to avoid
        //java.lang.IllegalStateException: Activity has been destroyed exception:
        if(isFinishing() == false) {
            //.commitAllowingStateLoss(); allows to commit after the previous fragment saved its Instance
            //.commit(); doesn't ensure it and we can get exception with it:
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    //Allowing to return to previous displayed Fragment after switching:
    public void previousFragment() {
        this.getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //We saved MapsFragment in order to allow fragment backup data (for example after a
        //rotation, it resets map so we need to store data map (marker...)):
        getSupportFragmentManager().putFragment(outState, "MapsFragment_State", this.mapsFragment);
    }
}
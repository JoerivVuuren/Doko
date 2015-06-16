package nl.uva.kite.Doko.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.uva.kite.Doko.Fragments.Tabs.Tab3;
import nl.uva.kite.Doko.R;
import nl.uva.kite.Doko.SlidingTab.SlidingTabLayout;
import nl.uva.kite.Doko.SlidingTab.ViewPagerAdapter;

public class TabWrapper extends android.support.v4.app.Fragment {
    // Declaring Your View and Variables
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Wall", "Me", "Members", "Games"};
    int Numboftabs = 4;

    private FragmentActivity myContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.group_wrapper, container, false);

        String value = getActivity().getIntent().getStringExtra("GameRequest");
        if(value != null) {
            //String startValue = b.getString("GameRequest");
            if(value.equals("1")) {
                Log.e("", "yes joeri it worked");
            }
        }


        FragmentManager fragManager = myContext.getSupportFragmentManager();
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(fragManager,Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        //pager.addOnPageChangeListener(this);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    Tab3 tab3 = new Tab3();
                    android.support.v4.app.FragmentManager fragmentmanager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, tab3);
                    fragmentTransaction.commit();
                    Log.e("", "i came in the tablistener");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
        });

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) v.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
}
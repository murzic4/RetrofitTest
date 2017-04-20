package ru.mera.smamonov.retrofittest.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.fragments.ConfigurationFragment;
import ru.mera.smamonov.retrofittest.fragments.GenericFragment;
import ru.mera.smamonov.retrofittest.fragments.LampsFragment;
import ru.mera.smamonov.retrofittest.fragments.ScenesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(LampsFragment.newInstance(), "Lamps");
        mSectionsPagerAdapter.addFragment(ScenesFragment.newInstance(), "Scenes");
        mSectionsPagerAdapter.addFragment(ConfigurationFragment.newInstance(), "Configuration");

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //Log.e(LOG_TAG, "onPageScrolled i:" + i + ", v:" + v + ", i1:" + i1);
            }

            @Override
            public void onPageSelected(int i) {
                Log.e(LOG_TAG, "onPageSelected i:" + i);

                GenericFragment fragment = (GenericFragment) mSectionsPagerAdapter.getItem(i);
                if (fragment != null) {
                    fragment.updateView();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
//                Log.e(LOG_TAG, "onPageScrollStateChanged i:" + i);
            }
        });
    }


    /**
     * A placeholder fragment containing a simple view.
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<GenericFragment> m_fragments = new LinkedList<>();
        List<String> m_fragments_names = new LinkedList<>();

        public void addFragment(GenericFragment fragment, String caption) {
            m_fragments.add(fragment);
            m_fragments_names.add(caption);
        }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return m_fragments.get(position);
        }

        @Override
        public int getCount() {
            return m_fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return m_fragments_names.get(position);
        }
    }

}

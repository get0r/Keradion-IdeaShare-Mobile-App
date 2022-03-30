package geez.jdevs.keradion.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import geez.jdevs.keradion.fragments.ExploreSingleTabFragment;
import geez.jdevs.keradion.fragments.PeoplesSingleTabFragment;

public class TabLayoutViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private boolean explore;
    private ArrayList<String> tabTitleLit;

    public TabLayoutViewPagerAdapter(Context mContext, boolean explore, ArrayList<String> tabTitleLit, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
        this.explore = explore;
        this.tabTitleLit = tabTitleLit;
    }

    @Override
    public Fragment getItem(int position) {
        if(explore) return new ExploreSingleTabFragment(tabTitleLit.get(position));
        return new PeoplesSingleTabFragment();
    }

    @Override
    public int getCount() {
        return tabTitleLit.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleLit.get(position);
    }
}

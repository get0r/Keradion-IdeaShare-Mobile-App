package geez.jdevs.keradion.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import geez.jdevs.keradion.adapters.TabLayoutViewPagerAdapter;
import geez.jdevs.keradion.R;

public class ExploreFragment extends Fragment {

    private TabLayout exploreTabLayout;
    private ViewPager exploreViewPager;
    private TabLayoutViewPagerAdapter exploreViewPagerAdapter;

    public ExploreFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ArrayList<String> tabsList = new ArrayList<>();
        tabsList.add("Newest");
        tabsList.add("Popular");
        tabsList.add("Tech");
        exploreViewPagerAdapter = new TabLayoutViewPagerAdapter(getActivity(),true, tabsList, getChildFragmentManager());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_explore, container, false);
        exploreTabLayout = fragmentView.findViewById(R.id.explore_frag_tabLayout);
        exploreViewPager = fragmentView.findViewById(R.id.explore_frag_viewpager);

        exploreViewPager.setAdapter(exploreViewPagerAdapter);
        exploreTabLayout.setupWithViewPager(exploreViewPager);

        return fragmentView;
    }
}

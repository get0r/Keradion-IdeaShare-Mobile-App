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

public class PeoplesFragment extends Fragment {

    private TabLayout peoplesTabLayout;
    private ViewPager peoplesViewPager;
    private TabLayoutViewPagerAdapter peoplesViewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ArrayList<String> tabsList = new ArrayList<>();
        tabsList.add("Peoples");
        tabsList.add("Pages");
        peoplesViewPagerAdapter = new TabLayoutViewPagerAdapter(getActivity(),false, tabsList, getChildFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_peoples, container, false);
        peoplesTabLayout = fragmentView.findViewById(R.id.peoples_frag_tabLayout);
        peoplesViewPager = fragmentView.findViewById(R.id.peoples_frag_viewpager);

        peoplesViewPager.setAdapter(peoplesViewPagerAdapter);
        peoplesTabLayout.setupWithViewPager(peoplesViewPager);
        return fragmentView;
    }
}

package com.omada.junction.ui.home.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.omada.junction.R;


public class FeedFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.feed_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ViewPager2 feedPager = view.findViewById(R.id.feed_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.feed_tab_layout);

        feedPager.setOffscreenPageLimit(2);
        feedPager.setAdapter(new FeedPagerAdapter(getChildFragmentManager(), getLifecycle()));
        feedPager.setCurrentItem(0);

        new TabLayoutMediator(tabLayout, feedPager, (tab, position) -> {
            switch(position){
                case 0:
                    tab.setText("For You");
                    break;
                case 1:
                    tab.setText("Learn");
                    break;
                case 2:
                    tab.setText("Compete");
                    break;
            }
        }).attach();
    }

    public static class FeedPagerAdapter extends FragmentStateAdapter {

        public FeedPagerAdapter(@NonNull FragmentManager manager, Lifecycle lifecycle) {
            super(manager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0: return new ForYouFragment();
                case 1: return new LearnFragment();
                case 2: return new CompeteFragment();
                default: return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}

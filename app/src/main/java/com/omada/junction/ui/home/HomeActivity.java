package com.omada.junction.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.omada.junction.R;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.ui.articledetails.ArticleDetailsFragment;
import com.omada.junction.ui.eventdetails.EventDetailsFragment;
import com.omada.junction.ui.eventdetails.EventRegistrationFragment;
import com.omada.junction.ui.institute.InstituteActivity;
import com.omada.junction.ui.home.feed.FeedFragment;
import com.omada.junction.ui.more.MoreActivity;
import com.omada.junction.ui.organization.OrganizationProfileFragment;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.HomeFeedViewModel;


public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);

        HomeFeedViewModel homeFeedViewModel = new ViewModelProvider(this).get(HomeFeedViewModel.class);

        if(savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_content_placeholder, new FeedFragment())
                    .commit();

        }

        if(savedInstanceState == null ||
                homeFeedViewModel.getLoadedForYou().getValue() == null || homeFeedViewModel.getLoadedForYou().getValue().size() == 0){

            homeFeedViewModel.getHomeFeed();
        }

        setupBottomNavigation();
        setupTriggers();

    }

    private void setupBottomNavigation(){
        BottomNavigationView bottomMenu = findViewById(R.id.home_bottom_navigation);
        bottomMenu.getMenu().findItem(R.id.home_button).setChecked(true);
        bottomMenu.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            Intent i = null;

            if (itemId == R.id.home_button){
                // TODO refresh feed here
            }
            else if (itemId == R.id.more_button){
                i = new Intent(HomeActivity.this, MoreActivity.class);
            }
            else if (itemId == R.id.institute_button){
                i = new Intent(HomeActivity.this, InstituteActivity.class);
            }


            if (i != null) {
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                return true;

            } else {
                Log.e("HomeActivity", "invalid bottom button press id" + itemId + " " + R.id.more_details_button);
                return false;
            }

        });
    }

    private void setupTriggers(){

        FeedContentViewModel feedContentViewModel = new ViewModelProvider(this).get(FeedContentViewModel.class);

        feedContentViewModel
                .getEventViewHandler()
                .getEventCardDetailsTrigger().observe(this, eventModelLiveEvent -> {

            if(eventModelLiveEvent == null){
                return;
            }

            EventModel eventModel = eventModelLiveEvent.getDataOnceAndReset();
            if(eventModel != null) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_content_placeholder, EventDetailsFragment.newInstance(eventModel))
                        .addToBackStack(null)
                        .commit();
            }

        });

        feedContentViewModel
                .getOrganizationDetailsTrigger()
                .observe(this, stringLiveEvent -> {
                    if(stringLiveEvent != null && stringLiveEvent.getData() != null){

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_content_placeholder,
                                        OrganizationProfileFragment.newInstance(stringLiveEvent.getDataOnceAndReset()))
                                .addToBackStack(null)
                                .commit();
                    }
                });

        feedContentViewModel
                .getEventViewHandler()
                .getEventFormTrigger().observe(this, eventModelLiveEvent -> {

            if(eventModelLiveEvent == null || eventModelLiveEvent.getData() == null){
                return;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_content_placeholder, EventRegistrationFragment.newInstance(eventModelLiveEvent.getDataOnceAndReset()))
                    .addToBackStack(null)
                    .commit();
        });

        feedContentViewModel
                .getEventViewHandler()
                .getCallOrganizerTrigger().observe(this, stringLiveEvent -> {
            //TODO call organizer from here
        });


        feedContentViewModel
                .getEventViewHandler()
                .getMailOrganizerTrigger().observe(this, stringLiveEvent -> {
            //TODO mail organizer from here
        });

        feedContentViewModel
                .getArticleViewHandler()
                .getArticleCardDetailsTrigger()
                .observe(this, articleModelLiveEvent -> {
                    if(articleModelLiveEvent != null && articleModelLiveEvent.getData() != null){

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_content_placeholder, ArticleDetailsFragment.newInstance(articleModelLiveEvent.getDataOnceAndReset()))
                                .addToBackStack(null)
                                .commit();

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomMenu = findViewById(R.id.home_bottom_navigation);
        bottomMenu.getMenu().findItem(R.id.home_button).setChecked(true);
    }

}

package com.omada.junction.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.omada.junction.R;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.data.models.ShowcaseModel;
import com.omada.junction.ui.articledetails.ArticleDetailsFragment;
import com.omada.junction.ui.eventdetails.EventDetailsFragment;
import com.omada.junction.ui.eventdetails.EventRegistrationFragment;
import com.omada.junction.ui.institute.InstituteActivity;
import com.omada.junction.ui.home.feed.FeedFragment;
import com.omada.junction.ui.more.MoreActivity;
import com.omada.junction.ui.organization.OrganizationProfileFragment;
import com.omada.junction.ui.organization.OrganizationShowcaseFragment;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.HomeFeedViewModel;


public class HomeActivity extends AppCompatActivity {


    private HomeFeedViewModel homeFeedViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);

        homeFeedViewModel = new ViewModelProvider(this).get(HomeFeedViewModel.class);

        if(savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_content_placeholder, new FeedFragment())
                    .commit();

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
                if(getSupportFragmentManager().getBackStackEntryCount() == 0){

                    homeFeedViewModel.reinitializeFeed();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_content_placeholder, new FeedFragment())
                            .commit();
                }
                else {
                    getSupportFragmentManager().popBackStack("stack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
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
                                .addToBackStack(
                                        "stack"
                                )
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
                                .addToBackStack(
                                        "stack"
                                )
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
                            .addToBackStack(
                                    "stack"
                            )
                            .commit();
        });

        feedContentViewModel
                .getEventViewHandler()
                .getCallOrganizerTrigger().observe(this, stringLiveEvent -> {

                    if(stringLiveEvent.getData() != null){
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + stringLiveEvent.getDataOnceAndReset()));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }

        });

        feedContentViewModel
                .getEventViewHandler()
                .getMailOrganizerTrigger().observe(this, pairLiveEvent -> {

                    if(pairLiveEvent.getData() != null) {

                        Pair<String, String> data = pairLiveEvent.getDataOnceAndReset();

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{data.second});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding " + data.first);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }

        });

        feedContentViewModel
                .getArticleViewHandler()
                .getArticleCardDetailsTrigger()
                .observe(this, articleModelLiveEvent -> {
                    if(articleModelLiveEvent != null && articleModelLiveEvent.getData() != null){

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_content_placeholder, ArticleDetailsFragment.newInstance(articleModelLiveEvent.getDataOnceAndReset()))
                                .addToBackStack(
                                        "stack"
                                )
                                .commit();

                    }
                });

        feedContentViewModel
                .getOrganizationViewHandler()
                .getOrganizationShowcaseDetailsTrigger()
                .observe(this, showcaseModelLiveEvent -> {

                    if(showcaseModelLiveEvent.getData() != null){

                        ShowcaseModel model = showcaseModelLiveEvent.getDataOnceAndReset();

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_content_placeholder, OrganizationShowcaseFragment.newInstance(model.getCreator(), model.getShowcaseID()))
                                .addToBackStack(
                                        "stack"
                                )
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

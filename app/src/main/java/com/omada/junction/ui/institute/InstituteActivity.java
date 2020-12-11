package com.omada.junction.ui.institute;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.omada.junction.R;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.data.models.OrganizationModel;
import com.omada.junction.ui.eventdetails.EventDetailsFragment;
import com.omada.junction.ui.eventdetails.EventRegistrationFragment;
import com.omada.junction.ui.home.HomeActivity;
import com.omada.junction.ui.more.MoreActivity;
import com.omada.junction.ui.organization.OrganizationProfileFragment;
import com.omada.junction.viewmodels.ApplicationViewModel;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.InstituteFeedViewModel;

public class InstituteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.institute_activity_layout);

        InstituteFeedViewModel instituteFeedViewModel = new ViewModelProvider(this).get(InstituteFeedViewModel.class);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.institute_content_placeholder, new InstituteFeedFragment())
                    .commit();

            instituteFeedViewModel.loadInstituteOrganizations();
            instituteFeedViewModel.loadInstituteHighlights();
        }
        else if(!instituteFeedViewModel.checkInstituteContentLoaded()){

            Toast.makeText(getBaseContext(), "loaded again", Toast.LENGTH_SHORT).show();

            instituteFeedViewModel.loadInstituteOrganizations();
            instituteFeedViewModel.loadInstituteHighlights();
        }

        setupBottomNavigation();
        setupTriggers();
    }

    private void setupTriggers() {

        FeedContentViewModel feedContentViewModel = new ViewModelProvider(this).get(FeedContentViewModel.class);

        feedContentViewModel.getOrganizationViewHandler()
                .getOrganizationModelDetailsTrigger()
                .observe(this, organizationModelLiveEvent -> {

                    if(organizationModelLiveEvent != null && organizationModelLiveEvent.getData() != null){
                        OrganizationModel organizationModel = organizationModelLiveEvent.getDataOnceAndReset();

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.institute_content_placeholder, OrganizationProfileFragment.newInstance(organizationModel))
                                .addToBackStack(null)
                                .commit();
                    }

                });

        feedContentViewModel.getEventViewHandler()
                .getEventCardDetailsTrigger()
                .observe(this, eventModelLiveEvent -> {
                    if(eventModelLiveEvent != null && eventModelLiveEvent.getData() != null){
                        EventModel model = eventModelLiveEvent.getDataOnceAndReset();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.institute_content_placeholder, EventDetailsFragment.newInstance(model))
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
                    .replace(R.id.institute_content_placeholder,
                            EventRegistrationFragment.newInstance(eventModelLiveEvent.getDataOnceAndReset()))
                    .addToBackStack(null)
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

        feedContentViewModel.getOrganizationDetailsTrigger()
                .observe(this, organizationIDLiveEvent -> {
                    if(organizationIDLiveEvent != null && organizationIDLiveEvent.getData() != null){
                        String organizationID = organizationIDLiveEvent.getDataOnceAndReset();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.institute_content_placeholder, OrganizationProfileFragment.newInstance(organizationID))
                                .addToBackStack(null)
                                .commit();
                    }
                });
    }

    private void setupBottomNavigation(){

        BottomNavigationView bottomMenu = findViewById(R.id.institute_bottom_navigation);
        bottomMenu.getMenu().findItem(R.id.institute_button).setChecked(true);
        bottomMenu.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            Intent i = null;

            if (itemId == R.id.home_button){
                i = new Intent(InstituteActivity.this, HomeActivity.class);
            }
            else if (itemId == R.id.more_button){
                i = new Intent(InstituteActivity.this, MoreActivity.class);

            }
            else if (itemId == R.id.institute_button){
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

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomMenu = findViewById(R.id.institute_bottom_navigation);
        bottomMenu.getMenu().findItem(R.id.institute_button).setChecked(true);
    }
}

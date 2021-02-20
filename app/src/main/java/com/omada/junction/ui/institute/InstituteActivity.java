package com.omada.junction.ui.institute;

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
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.data.models.external.OrganizationModel;
import com.omada.junction.data.models.external.ShowcaseModel;
import com.omada.junction.ui.eventdetails.EventDetailsFragment;
import com.omada.junction.ui.eventdetails.EventRegistrationFragment;
import com.omada.junction.ui.home.HomeActivity;
import com.omada.junction.ui.more.MoreActivity;
import com.omada.junction.ui.organization.OrganizationProfileFragment;
import com.omada.junction.ui.organization.OrganizationShowcaseFragment;
import com.omada.junction.utils.TransformUtilities;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.InstituteFeedViewModel;

public class InstituteActivity extends AppCompatActivity {

    private InstituteFeedViewModel instituteFeedViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.institute_activity_layout);

        instituteFeedViewModel = new ViewModelProvider(this).get(InstituteFeedViewModel.class);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.institute_content_placeholder, new InstituteFeedFragment())
                    .commit();

        } else if (!instituteFeedViewModel.checkInstituteContentLoaded()) {

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

                    if (organizationModelLiveEvent != null) {

                        OrganizationModel organizationModel = organizationModelLiveEvent.getDataOnceAndReset();
                        if (organizationModel == null) {
                            return;
                        }
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.institute_content_placeholder, OrganizationProfileFragment.newInstance(organizationModel))
                                .addToBackStack("stack")
                                .commit();
                    }

                });

        feedContentViewModel.getEventViewHandler()
                .getEventCardDetailsTrigger()
                .observe(this, eventModelLiveEvent -> {
                    if (eventModelLiveEvent != null) {
                        EventModel model = eventModelLiveEvent.getDataOnceAndReset();
                        if (model == null) {
                            return;
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.institute_content_placeholder, EventDetailsFragment.newInstance(model))
                                .addToBackStack("stack")
                                .commit();
                    }
                });

        feedContentViewModel
                .getEventViewHandler()
                .getEventFormTrigger().observe(this, eventModelLiveEvent -> {

            if (eventModelLiveEvent == null) {
                return;
            }

            EventModel eventModel = eventModelLiveEvent.getDataOnceAndReset();
            if (eventModel == null) {
                return;
            }

            String url = TransformUtilities.getUrlFromForm(eventModel.getForm());
            if (url != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_content_placeholder, EventRegistrationFragment.newInstance(eventModel))
                        .addToBackStack("stack")
                        .commit();
            }
        });

        feedContentViewModel
                .getEventViewHandler()
                .getCallOrganizerTrigger().observe(this, stringLiveEvent -> {

            if (stringLiveEvent != null) {
                String phone = stringLiveEvent.getDataOnceAndReset();
                if (phone == null) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        feedContentViewModel
                .getEventViewHandler()
                .getMailOrganizerTrigger().observe(this, pairLiveEvent -> {

            if (pairLiveEvent != null) {

                Pair<String, String> data = pairLiveEvent.getDataOnceAndReset();
                if (data == null) {
                    return;
                }

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
                    if (organizationIDLiveEvent != null) {

                        String organizationID = organizationIDLiveEvent.getDataOnceAndReset();
                        if (organizationID == null) {
                            return;
                        }

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.institute_content_placeholder, OrganizationProfileFragment.newInstance(organizationID))
                                .addToBackStack("stack")
                                .commit();
                    }
                });

        feedContentViewModel
                .getOrganizationViewHandler()
                .getOrganizationShowcaseDetailsTrigger()
                .observe(this, showcaseModelLiveEvent -> {

                    if (showcaseModelLiveEvent != null) {

                        ShowcaseModel model = showcaseModelLiveEvent.getDataOnceAndReset();
                        if (model == null) {
                            return;
                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.institute_content_placeholder, OrganizationShowcaseFragment.newInstance(model))
                                .addToBackStack("stack")
                                .commit();

                    }

                });
    }

    private void setupBottomNavigation() {

        BottomNavigationView bottomMenu = findViewById(R.id.institute_bottom_navigation);
        bottomMenu.getMenu().findItem(R.id.institute_button).setChecked(true);
        bottomMenu.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            Intent i = null;

            if (itemId == R.id.home_button) {
                i = new Intent(InstituteActivity.this, HomeActivity.class);
            } else if (itemId == R.id.more_button) {
                i = new Intent(InstituteActivity.this, MoreActivity.class);

            } else if (itemId == R.id.institute_button) {

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                    instituteFeedViewModel.reinitializeFeed();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.institute_content_placeholder, new InstituteFeedFragment())
                            .commit();
                } else {
                    getSupportFragmentManager().popBackStack("stack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }

            if (i != null) {
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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

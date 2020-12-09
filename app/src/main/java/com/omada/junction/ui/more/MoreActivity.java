package com.omada.junction.ui.more;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.omada.junction.R;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.ui.institute.InstituteActivity;
import com.omada.junction.ui.home.HomeActivity;
import com.omada.junction.ui.login.LoginActivity;
import com.omada.junction.viewmodels.UserProfileViewModel;

public class MoreActivity extends AppCompatActivity {

    UserProfileViewModel userProfileViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_activity_layout);

        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.more_content_placeholder, new UserProfileFragment())
                    .commit();
        }

        setupBottomNavigation();
        setupTriggers();
    }

    private void setupBottomNavigation(){

        BottomNavigationView bottomMenu = findViewById(R.id.more_bottom_navigation);
        bottomMenu.getMenu().findItem(R.id.more_button).setChecked(true);
        bottomMenu.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            Intent i = null;

            if (itemId == R.id.home_button){
                i = new Intent(MoreActivity.this, HomeActivity.class);
            }
            else if (itemId == R.id.more_button){
            }
            else if (itemId == R.id.institute_button){
                i = new Intent(MoreActivity.this, InstituteActivity.class);
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
        userProfileViewModel.getEditProfileTrigger()
                .observe(this, booleanLiveEvent -> {

                    if(booleanLiveEvent != null && booleanLiveEvent.getDataOnceAndReset()){

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.more_content_placeholder, new UserProfileEditDetailsFragment())
                                .addToBackStack(null)
                                .commit();

                    }

                });

        userProfileViewModel.getSignOutTrigger()
                .observe(this, authStatusLiveEvent -> {

                    if(authStatusLiveEvent != null && authStatusLiveEvent.getData() != null){

                        if(authStatusLiveEvent.getDataOnceAndReset() == UserDataHandler.AuthStatus.USER_SIGNED_OUT) {
                            Intent i = new Intent(this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(i);
                            finish();
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomMenu = findViewById(R.id.more_bottom_navigation);
        bottomMenu.getMenu().findItem(R.id.more_button).setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

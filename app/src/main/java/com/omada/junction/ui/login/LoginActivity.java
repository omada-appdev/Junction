package com.omada.junction.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.omada.junction.R;
import com.omada.junction.viewmodels.LoginViewModel;
import com.omada.junction.ui.home.HomeActivity;

public class LoginActivity extends AppCompatActivity{

    private LoginViewModel loginViewModel;

    //public enum identifying fragments
    public enum FragmentIdentifier {
        LOGIN_START_FRAGMENT,
        LOGIN_SIGNIN_FRAGMENT,
        LOGIN_DETAILS_FRAGMENT,
        LOGIN_INTERESTS_FRAGMENT,
        LOGIN_FORGOTPASSWORD_FRAGMENT
    }

    private FragmentIdentifier currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        currentFragment = FragmentIdentifier.LOGIN_START_FRAGMENT;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_activity_placeholder, StartFragment.newInstance())
                .commit();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getAuthResultAction().observe(this, authStatusLiveEvent -> {});

        loginViewModel.getFragmentChangeAction().observe(this, fragId -> {

            FragmentIdentifier id = fragId.getDataOnceAndReset();
            if(id == null){
                return;
            }
            currentFragment = id;
            switch(currentFragment){
                case LOGIN_SIGNIN_FRAGMENT:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login_activity_placeholder, SignInFragment.getInstance())
                            .addToBackStack("signin")
                            .commit();
                    break;
                case LOGIN_FORGOTPASSWORD_FRAGMENT:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login_activity_placeholder, ForgotPasswordFragment.newInstance())
                            .addToBackStack("forgot")
                            .commit();
                    break;
                case LOGIN_DETAILS_FRAGMENT:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login_activity_placeholder, DetailsFragment.newInstance())
                            .addToBackStack("details")
                            .commit();
                    break;
                case LOGIN_INTERESTS_FRAGMENT:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login_activity_placeholder, InterestsFragment.newInstance())
                            .addToBackStack("interests")
                            .commit();
                    break;
            }
        });

        loginViewModel.getGoToFeedAction().observe(this, goToFeed -> {
            if(goToFeed.getDataOnceAndReset()){
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginViewModel.getToastMessageAction().observe(this, s -> Toast.makeText(LoginActivity.this, s.getDataOnce(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switch (currentFragment){
            case LOGIN_SIGNIN_FRAGMENT:
                currentFragment = FragmentIdentifier.LOGIN_START_FRAGMENT;
                loginViewModel.exitSignInScreen();
                break;
            case LOGIN_DETAILS_FRAGMENT:
                currentFragment = FragmentIdentifier.LOGIN_INTERESTS_FRAGMENT;
                loginViewModel.exitDetailsScreen();
                break;
            case LOGIN_INTERESTS_FRAGMENT:
                currentFragment = FragmentIdentifier.LOGIN_START_FRAGMENT;
                loginViewModel.exitInterestsScreen();
                break;
        }
    }
}
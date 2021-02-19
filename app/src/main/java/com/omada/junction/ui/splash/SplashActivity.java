package com.omada.junction.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.omada.junction.R;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.ui.home.HomeActivity;
import com.omada.junction.ui.login.LoginActivity;
import com.omada.junction.viewmodels.SplashViewModel;


public class SplashActivity extends AppCompatActivity {

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity_layout);
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        splashViewModel.getCurrentUser();

        splashViewModel.getAuthResultAction().observe(this, authStatusLiveEvent -> {
            if(authStatusLiveEvent != null){
                UserDataHandler.AuthStatus authStatus = authStatusLiveEvent.getDataOnceAndReset();
                if(authStatus==null) return;
                Intent i;
                switch (authStatus){
                    case CURRENT_USER_SUCCESS:
                        Log.e("Splash", UserDataHandler.AuthStatus.CURRENT_USER_SUCCESS.toString());
                        break;
                    case LOGIN_SUCCESS:
                        Log.e("Splash", UserDataHandler.AuthStatus.LOGIN_SUCCESS.toString());
                        i = new Intent(this, HomeActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case CURRENT_USER_FAILURE:
                        Log.e("Splash", UserDataHandler.AuthStatus.CURRENT_USER_FAILURE.toString());
                    case LOGIN_FAILURE:
                        Log.e("Splash", UserDataHandler.AuthStatus.LOGIN_FAILURE.toString());
                        i = new Intent(this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        break;
                }
            }
        });

        splashViewModel.getSignedInUserAction().observe(this, userModelLiveEvent -> {
        });
    }
}

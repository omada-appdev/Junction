package com.omada.junction.ui.uicomponents;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.omada.junction.R;
import com.omada.junction.utils.image.GlideApp;
import com.omada.junction.viewmodels.OrganizationProfileViewModel;

public class CustomBindings {

    @BindingAdapter({"remoteImageUrl"})
    public static void loadImage(ImageView view, String remoteUrl) {
        StorageReference gsRef;

        if(remoteUrl == null || remoteUrl.equals("")){
            return;
        }
        else{
            gsRef = FirebaseStorage.getInstance().getReferenceFromUrl(remoteUrl);
        }

        GlideApp.with(view.getContext())
                .load(gsRef)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    @BindingAdapter({"onFollowClicked"})
    public static void onFollowAction(ToggleButton toggleButton, OrganizationProfileViewModel viewModel){

        toggleButton.setBackgroundResource(R.drawable.ic_favorite_black_24dp);

        toggleButton.setOnClickListener(v -> {

            ToggleButton view = (ToggleButton) v;

            if(view.isChecked()) {
                viewModel.doFollowAction();
                view.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
            }
            else {
                viewModel.doFollowAction();
                view.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
            }
        });

    }

    @BindingAdapter({"formattedText"})
    public static void setFormattedText(FormattedTextView textView, String text){
        textView.setFormattedText(text);
    }

}

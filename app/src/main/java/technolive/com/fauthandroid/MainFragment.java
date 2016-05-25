package technolive.com.fauthandroid;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    // CallbackManager manages the callbacks into the FacebookSdk from an Activity's or Fragment's onActivityResult() method
    private CallbackManager mCallbackManager;
    TextView userName;
    ImageView userImage;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Facebook SDK needs to be initialized after the Fragment or Class is created
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        // The factory class for the CallbackManager needs to be created
        mCallbackManager = CallbackManager.Factory.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        userName = (TextView) view.findViewById(R.id.userName);
        userImage = (ImageView) view.findViewById(R.id.userImage);


        loginButton.setReadPermissions("user_posts");
        loginButton.setFragment(this);

        // RegisterCallback is used to Callback the Registration for LoginResult
        loginButton.registerCallback(mCallbackManager, mCallback);
    }


    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        // This is what happens if the Callback is successful
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayWelcomeMessage(profile);
        }
        // This is what happens if the Callback gets Cancelled for some reason
        @Override
        public void onCancel() {

        }
        // This is what happens if some Exception happens while running the Callback
        @Override
        public void onError(FacebookException error) {

        }
    };

    private void displayWelcomeMessage(Profile profile) {
        if (profile != null)
        {
            userName.setText("Hello, " + profile.getName());
            userImage.setVisibility(View.VISIBLE);

            Glide.with(getContext())
                    .load(profile.getProfilePictureUri(300,300))
                    .dontTransform()
                    .dontAnimate()
                    .into(userImage);

        }
    }

    // The method which handles the CallbackManager based on Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }


}

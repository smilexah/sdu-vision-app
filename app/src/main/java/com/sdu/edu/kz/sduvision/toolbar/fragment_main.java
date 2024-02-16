package com.sdu.edu.kz.sduvision.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sdu.edu.kz.sduvision.R;
import com.sdu.edu.kz.sduvision.prediction_section;

public class fragment_main extends Fragment {

    private ImageView predictButton;
    private ImageView lastViewedImage;
    private TextView lastViewedTitle;
    private TextView lastViewedDescription;

    public fragment_main() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Initialize views
        predictButton = view.findViewById(R.id.predict_button);
        lastViewedImage = view.findViewById(R.id.last_viewed_image);
        lastViewedTitle = view.findViewById(R.id.last_viewed_title);
        lastViewedDescription = view.findViewById(R.id.last_viewed_description);

        // Set up listeners and other initializations here
        setupListeners();

        return view;
    }

    private void setupListeners() {
        // Example: Set an onClickListener for the predictButton
        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click
                Intent intent = new Intent(getActivity(), prediction_section.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Similarly, you can set up listeners for other views if needed
    }

    private void onPredictButtonClicked() {
        // Implement the logic to be executed when the predict button is clicked
    }

    // Other methods for handling user interactions can be added here
}

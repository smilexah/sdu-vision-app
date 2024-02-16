package com.sdu.edu.kz.sduvision.toolbar;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdu.edu.kz.sduvision.LoginPage;
import com.sdu.edu.kz.sduvision.R;

public class fragment_account extends Fragment {

    private FirebaseAuth mAuth;

    public fragment_account() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();

        // Initialize logout and delete account layouts
        RelativeLayout logoutLayout = view.findViewById(R.id.personalDataLayout12);
        logoutLayout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        });

        RelativeLayout deleteAccountLayout = view.findViewById(R.id.personalDataLayout123);
        deleteAccountLayout.setOnClickListener(v -> deleteAccount());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchAndDisplayEmail(view);
    }

    private void fetchAndDisplayEmail(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        TextView emailTextView = view.findViewById(R.id.emailTextView);

        if (user != null) {
            String userId = user.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users").child(userId).child("email");

            ref.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (isAdded() && dataSnapshot.exists()) { // Check if fragment is still attached and data exists
                        String email = dataSnapshot.getValue(String.class);
                        emailTextView.setText(email);
                    } else {
                        emailTextView.setText("ZAEBA");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Failed to read email", databaseError.toException());
                }
            });
        } else {
//            emailTextView.setText("No user logged in");
            Log.d(TAG, "Failed to read email");
        }
    }

    private void deleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                } else {
                    Toast.makeText(getActivity(), "Account deletion failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No user is currently signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getActivity(), LoginPage.class);
        startActivity(intent);
        getActivity().finish();
    }
}

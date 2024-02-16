package com.sdu.edu.kz.sduvision.toolbar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdu.edu.kz.sduvision.Note;
import com.sdu.edu.kz.sduvision.NotesAdapter;
import com.sdu.edu.kz.sduvision.R;

import java.util.ArrayList;
import java.util.List;

public class fragment_notes extends Fragment {
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Note> notesList = new ArrayList<>();
    private DatabaseReference mDatabase;

    public fragment_notes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = view.findViewById(R.id.your_recycler_view); // Replace with your RecyclerView ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotesAdapter(notesList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("notes");

        fetchNotes();

        return view;
    }

    private void fetchNotes() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notesList.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    notesList.add(note);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Data fetch cancelled or failed: " + databaseError.getMessage());

                // You can also check the specific error code
                switch (databaseError.getCode()) {
                    case DatabaseError.DISCONNECTED:
                        break;
                    case DatabaseError.NETWORK_ERROR:
                        break;
                    case DatabaseError.PERMISSION_DENIED:
                        break;
                    default:
                        break;
                }
            }

        });
    }
}

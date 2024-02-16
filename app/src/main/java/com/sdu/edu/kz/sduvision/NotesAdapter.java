package com.sdu.edu.kz.sduvision;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notesList;

    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getContent());
        holder.timeStamp.setText(formatTimestamp(note.getTimestamp()));
        // If you have images, load them here
    }

    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return format.format(date);
    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, timeStamp;

        public NoteViewHolder(View view) {
            super(view);
//            imageView = view.findViewById(R.id.note_image);
            title = view.findViewById(R.id.note_title);
            description = view.findViewById(R.id.note_content);
            timeStamp = view.findViewById(R.id.note_timestamp);
        }
    }
}

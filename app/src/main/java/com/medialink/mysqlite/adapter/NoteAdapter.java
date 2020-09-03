package com.medialink.mysqlite.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medialink.mysqlite.CustomOnItemClickListener;
import com.medialink.mysqlite.Note;
import com.medialink.mysqlite.NoteAddUpdateActivity;
import com.medialink.mysqlite.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> listNote = new ArrayList<>();
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getListNote() {
        return listNote;
    }

    public void setListNote(ArrayList<Note> listNote) {
        if (listNote.size() > 0) {
            this.listNote.clear();
        }
        this.listNote.addAll(listNote);
        notifyDataSetChanged();
    }

    public void addItem(Note note) {
        this.listNote.add(note);
        int position = listNote.size() - 1;
        notifyItemInserted(position);
    }

    public void updateItem(int position, Note note) {
        this.listNote.set(position, note);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        this.listNote.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listNote.size());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = listNote.get(position);
        holder.tvDate.setText(note.getDate());
        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
                        intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position);
                        intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note);
                        activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE);
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription, tvDate;
        private CardView cvNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}

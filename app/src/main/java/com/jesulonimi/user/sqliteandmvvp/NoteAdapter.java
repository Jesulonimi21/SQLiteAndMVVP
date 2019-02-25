package com.jesulonimi.user.sqliteandmvvp;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteViewHolder> {
//    List<Note> noteList = new ArrayList<>();
    RecyclerViewItemClick recyclerViewItemClick;

    protected NoteAdapter() {
        super(DIFF_CALLBACK);
    }
    private static DiffUtil.ItemCallback<Note> DIFF_CALLBACK=new DiffUtil.ItemCallback<Note>(){

        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.getDescription().equals(newItem.getDescription())&&oldItem.getTitle().equals(newItem.getTitle())
                    &&oldItem.getPriority()==newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
//        Note note = noteList.get(position);
        Note note = getItem(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.priority.setText(String.valueOf(note.getPriority()));
    }



//    public void setNoteList(List<Note> noteList) {
//        this.noteList = noteList;
//        notifyDataSetChanged();
//    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView priority;


        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            description = itemView.findViewById(R.id.text_view_desc);
            priority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            if (recyclerViewItemClick != null && position != RecyclerView.NO_POSITION) {
                                recyclerViewItemClick.onRecyclerviewItemClick(getItem(position));
                            }
                        }
                    }
            );
        }
    }

    public Note getNoteAt(int position) {
        //return noteList.get(position);
        return getItem(position);
    }

    public interface RecyclerViewItemClick {
        void onRecyclerviewItemClick(Note note);
    }

    public void onRecyclerViewItemClickListener(RecyclerViewItemClick recyclerViewItemClick) {
        this.recyclerViewItemClick = recyclerViewItemClick;
    }
}

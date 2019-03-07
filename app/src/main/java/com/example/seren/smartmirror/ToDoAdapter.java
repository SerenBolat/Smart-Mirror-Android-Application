package com.example.seren.smartmirror;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{
    List<ToDoList> toDoLists;
    Context context;

    public ToDoAdapter(List<ToDoList> toDoLists, Context context) {
        this.toDoLists = toDoLists;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView toDoContent;
        public TextView toDodate;

        public ViewHolder(View view) {
            super(view);

            toDoContent = (TextView)view.findViewById(R.id.contentTodo);
            toDodate = (TextView)view.findViewById(R.id.dateToDo);
            view.setTag(view);
        }
    }

    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todoadapter_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toDoContent.setText("Content: "+" "+toDoLists.get(position).getContent());
        holder.toDodate.setText("Date: "+" "+String.valueOf(toDoLists.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return toDoLists.size();
    }
}

package com.example.seren.smartmirror;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>
        implements DatePickerDialog.OnDateSetListener{
    List<ToDoList> toDoLists;
    Context context;
    String user_mail;
    private ItemClickListener clickListener;
    String dateofNote="";
    boolean selectDate;

    public ToDoAdapter(List<ToDoList> toDoLists, Context context,String user_mail) {
        this.toDoLists = toDoLists;
        this.context = context;
        this.user_mail = user_mail;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView toDoContent;
        public TextView toDodate;
        public ImageView optionMenu;

        public ViewHolder(View view) {
            super(view);

            toDoContent = (TextView)view.findViewById(R.id.contentTodo);
            toDodate = (TextView)view.findViewById(R.id.dateToDo);
            optionMenu = (ImageView)view.findViewById(R.id.optionMenu);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null) clickListener.onClick(v, getAdapterPosition());

        }
    }

    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todoadapter_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.toDoContent.setText(toDoLists.get(position).getContent());
        holder.toDodate.setText("Date: "+" "+String.valueOf(toDoLists.get(position).getDate()));
        holder.optionMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.optionMenu);
                popupMenu.inflate(R.menu.menu_items);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                //update to do
                                updateToDo(toDoLists.get(position));
                                break;
                            case R.id.doing:
                                //move to doing
                                moveDoing(toDoLists.get(position));
                                break;
                            case R.id.done:
                                //move to done
                                moveDone(toDoLists.get(position));
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popupMenu.show();
            }
        });
    }
    private ToDoList updateToDo;
    private void moveDone(ToDoList toDoList) {
        updateToDo = toDoList;
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("ToDoList").child(user_mail).child(updateToDo.getNotesID());
        ToDoList toDoList1 = new ToDoList(updateToDo.getNotesID(),dateofNote,updateToDo.getContent(),"Done");
        dR.setValue(toDoList1);
    }

    private void moveDoing(ToDoList toDoList) {
        updateToDo = toDoList;
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("ToDoList").child(user_mail).child(updateToDo.getNotesID());
        ToDoList toDoList1 = new ToDoList(updateToDo.getNotesID(),dateofNote,updateToDo.getContent(),"Doing");
        dR.setValue(toDoList1);
    }

    private void updateToDo(ToDoList toDoList) {
        updateToDo = toDoList;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextContent = (EditText) dialogView.findViewById(R.id.editTextContent);
        final ImageButton dateUpdate = (ImageButton) dialogView.findViewById(R.id.calendarUpdate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateToDo);

        dialogBuilder.setTitle("Update your note");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextContent.getText().length()>0||!dateofNote.equals("")){
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference("ToDoList").child(user_mail).child(updateToDo.getNotesID());
                    if(dateofNote.equals(""))
                        dateofNote= updateToDo.getDate();
                    ToDoList toDoList1 = new ToDoList(updateToDo.getNotesID(),dateofNote,editTextContent.getText().toString(),updateToDo.getSituation());
                    dR.setValue(toDoList1);
                    b.dismiss();
                    Toast.makeText(context, "The note updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoLists.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        selectDate = setDate(cal);
    }

    private boolean setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        dateofNote = dateFormat.format(calendar.getTime());
        //((TextView) findViewById(R.id.showDate))
        //.setText(dateFormat.format(calendar.getTime()));
        if(dateFormat.format((calendar.getTime()))!= null){
            return true;
        }
        else {
            return false;
        }
    }

    public void datePicker(View view){

        DatePickerFragment fragment = new DatePickerFragment();
        //fragment.show(((Activity)context).getFragmentManager(), "datePicker");
    }

}


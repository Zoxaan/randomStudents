package com.example.video;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.GroupViewHolder> {

    private List<String> groupList;
    private Context context;
    private MyAdapter adapter;


    public MyAdapter(Context context) {
        this.context = context;
    }

    public void setGroups(List<String> groups) {
        this.groupList = groups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        String groupName = groupList.get(position);
        String[] gn = groupName.split(" ");
        holder.bind(groupName);

        holder.groupButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showContextMenu(v, gn[0]);
                return true;
            }

        });

        holder.groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                String data = (String) holder.groupButton.getText();
                String[] words = data.split(" : ");
                List<String> usersList = dbHelper.getUserTututu(words[0]);

                // Создайте явное намерение (Intent) для открытия новой активности
                Intent intent = new Intent(context, StudentsActivity.class);
                intent.putStringArrayListExtra("userList", new ArrayList<>(usersList));
                context.startActivity(intent);
            }
        });
    }
    private void showContextMenu(View v, String gn) {

        PopupMenu popupMenu = new PopupMenu(context, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.button_context_menu, popupMenu.getMenu());

        // Обработчик выбора пунктов меню
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SQLiteDatabase db;
                DatabaseHelper dbHelper;
                dbHelper = new DatabaseHelper(context);
                db = dbHelper.getWritableDatabase();
                DatabaseHelper.delete(db,gn);
                groupList = dbHelper.getAllGroups();
                setGroups(groupList);
// Обновляем RecyclerView
                /*adapter.notifyDataSetChanged();*/
                db.close();
                return true;
            }
        });

        popupMenu.show();
    }








    @Override
    public int getItemCount() {
        return groupList != null ? groupList.size() : 0;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private Button groupButton;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupButton = itemView.findViewById(R.id.groupButton);
        }

        public void bind(String groupName) {
            groupButton.setText(groupName);
        }
    }
}
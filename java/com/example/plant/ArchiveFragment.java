package com.example.plant;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment implements TimeLineAdapter.OnDeleteItemClickListener {

    private RecyclerView recyclerView;
    private ImageView empty_image;
    private TextView no_empty;
    private MyDatabaseHelper myBoo;
    private ArrayList<Integer> plant_id;
    private ArrayList<String> plant_name;
    private ArrayList<String> plant_var;
    private ArrayList<String> plant_height;
    private ArrayList<String> plant_target;
    private TimeLineAdapter adapter;

    private FloatingActionButton add_button;

    private double processedData;
    private int position;
    private double target;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        //去除默认的顶部导航栏
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }


        recyclerView = view.findViewById(R.id.recyclerView_archive);
        empty_image = view.findViewById(R.id.empty_image);
        no_empty = view.findViewById(R.id.no_empty);
        myBoo = new MyDatabaseHelper(getContext());
        plant_id = new ArrayList<>();
        plant_name = new ArrayList<>();
        plant_var = new ArrayList<>();
        plant_height = new ArrayList<>();
        plant_target = new ArrayList<>();

        add_button = view.findViewById(R.id.main_add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddArchiveActivity.class);
                startActivity(intent);
            }
        });


        storeDataInArrays();


        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置适配器
        adapter = new TimeLineAdapter(getActivity(), getContext(), plant_id, plant_name, plant_var, plant_height, plant_target, this);
        recyclerView.setAdapter(adapter);

        // 获取position和target
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("position", 0);
            target = args.getDouble("target", 0.0);
            updateData(position,target);
        }



        return view;

    }

    void storeDataInArrays() {
        Cursor cursor = myBoo.readAllData();
        if (cursor.getCount() == 0) {
            empty_image.setVisibility(View.VISIBLE);
            no_empty.setVisibility(View.VISIBLE);
        } else {
            plant_id = new ArrayList<>();
            plant_name = new ArrayList<>();
            plant_var = new ArrayList<>();
            plant_height = new ArrayList<>();
            plant_target = new ArrayList<>();

            while (cursor.moveToNext()) {
                plant_id.add(Integer.valueOf(cursor.getString(0)));
                plant_name.add(cursor.getString(1));
                plant_var.add(cursor.getString(2));
                plant_height.add(cursor.getString(3));
                plant_target.add(cursor.getString(4));
            }
            empty_image.setVisibility(View.GONE);
            no_empty.setVisibility(View.GONE);
        }
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_menu, menu); // Inflate your menu resource
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteItem(int position) {
        String plantNameToDelete = plant_name.get(position);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        dbHelper.deleteOneRowByName(plantNameToDelete, plant_id, plant_name, plant_var, plant_height, plant_target);

        // 删除后更新适配器
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, plant_name.size());
    }


    void confirmDialog() {
        AlertDialog.Builder Builder = new AlertDialog.Builder(getContext());
        Builder.setMessage(" Are you sure you want to delete All ? ");
        Builder.setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myBoo = new MyDatabaseHelper(getContext());
                myBoo.deleteAllData();
                Intent intent = new Intent(String.valueOf(getContext()));
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        Builder.setNegativeButton(" No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Builder.create().show();
    }
    public void updateData(int position, double target) {
        if (position >= 0 && position < plant_id.size()) {
            double newData = target;
            int dataId = position;

            // 处理数据
            int newPlantId = plant_id.get(dataId);
            String newPlantName = plant_name.get(dataId);
            String newPlantVar = plant_var.get(dataId);
            String newPlantHeight = plant_height.get(dataId);

            // 更新适配器的数据源和视图
            adapter.updateData(dataId, newPlantId, newPlantName, newPlantVar, newPlantHeight, newData);
            adapter.notifyDataSetChanged();
        } else {
            Log.e("UpdateData", "Invalid position: " + position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    public static ArchiveFragment getInstance(int position, double target) {
        ArchiveFragment fragment = new ArchiveFragment();

        // 将position和target作为参数传递给Fragment
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putDouble("target", target);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onDeleteItemClick(int position) {
        // Handle item deletion here, e.g., call the deleteItem method
        deleteItem(position);
    }



}

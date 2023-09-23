package com.example.plant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {


    private Context context;
    Activity activity;
    private ArrayList plant_id;
    private ArrayList plant_name;
    private ArrayList plant_var;
    private ArrayList plant_height;
    private ArrayList plant_target;


    Animation translate_anim;

    public TimeLineAdapter(FragmentActivity activity, Context context, ArrayList<Integer> plant_id, ArrayList<String> plant_name, ArrayList<String> plant_var, ArrayList<String> plant_height, ArrayList<String> plant_target) {
        this.activity = activity;
        this.context = context;
        this.plant_id = plant_id;
        this.plant_name = plant_name;
        this.plant_var = plant_var;
        this.plant_height = plant_height;
        this.plant_target = plant_target;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new TimeLineAdapter.TimeLineViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.id_text.setText(String.valueOf(plant_id.get(position)));
        holder.name_text.setText(String.valueOf(plant_name.get(position)));
        holder.var_text.setText(String.valueOf(plant_var.get(position)));
        holder.height_text.setText(String.valueOf(plant_height.get(position)));
        holder.target_text.setText(String.valueOf(plant_target.get(position)));


        holder.doubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                Log.d(String.valueOf(context),"The position is"+position);
                intent.putExtra("position", String.valueOf(plant_id.get(position)));
                intent.putExtra("name", String.valueOf(plant_name.get(position)));
                intent.putExtra("var", String.valueOf(plant_name.get(position)));
                intent.putExtra("height", String.valueOf(plant_height.get(position)));
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rowId = String.valueOf(plant_id.get(position));
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                dbHelper.deleteOneRow(rowId);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, plant_id.size());
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return plant_id.size();
    }

    public void updateData(int position, int newPlantId, String newPlantName, String newPlantVar, String newPlantHeight, double newPlantTarget) {
        plant_id.set(position, newPlantId);
        plant_name.set(position, newPlantName);
        plant_var.set(position, newPlantVar);
        plant_height.set(position, newPlantHeight);
        plant_target.set(position, String.valueOf(newPlantTarget));
        notifyItemChanged(position);
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder{
        TextView id_text,name_text,var_text,height_text,target_text,text_target,text_height;
        ImageView doubt,delete;

        RelativeLayout mainLayout;
        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);
            id_text = itemView.findViewById(R.id.id_text);
            name_text = itemView.findViewById(R.id.name_text);
            var_text = itemView.findViewById(R.id.var_text);
            height_text = itemView.findViewById(R.id.height_text);
            target_text = itemView.findViewById(R.id.target_text);
            text_target = itemView.findViewById(R.id.text_target);
            text_height = itemView.findViewById(R.id.text_height);
            doubt = itemView.findViewById(R.id.imageView2);
            delete = itemView.findViewById(R.id.delete);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.tranlate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
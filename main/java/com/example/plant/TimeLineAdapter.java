package com.example.plant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private ArrayList plant_time;


    Animation translate_anim;

    public TimeLineAdapter(FragmentActivity activity, Context context, ArrayList<Integer> plant_id, ArrayList<String> plant_name, ArrayList<String> plant_var, ArrayList<String> plant_height, ArrayList<String> plant_target,ArrayList<String> plant_time) {
        this.activity = activity;
        this.context = context;
        this.plant_id = plant_id;
        this.plant_name = plant_name;
        this.plant_var = plant_var;
        this.plant_height = plant_height;
        this.plant_target = plant_target;
        this.plant_time = plant_time;
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
        holder.timeText.setText(String.valueOf(plant_time.get(position)));
        ViewGroup.LayoutParams layoutParams = holder.lineView.getLayoutParams();
        holder.lineView.setLayoutParams(layoutParams);
        holder.doubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateActivity.class);
                intent.putExtra("name",String.valueOf(plant_name.get(position)));
                intent.putExtra("position",String.valueOf(plant_id.get(position)));
                context.startActivity(intent);
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
        notifyDataSetChanged();
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder{
        TextView id_text,name_text,var_text,height_text,target_text,timeText;
        ImageView doubt;

        View lineView;
        LinearLayout mainLayout;
        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);
            id_text = itemView.findViewById(R.id.id_text);
            name_text = itemView.findViewById(R.id.name_text);
            var_text = itemView.findViewById(R.id.var_text);
            height_text = itemView.findViewById(R.id.height_text);
            target_text = itemView.findViewById(R.id.target_text);
            timeText = itemView.findViewById(R.id.timeText);
            lineView = itemView.findViewById(R.id.lineView);
            doubt = itemView.findViewById(R.id.doubt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.tranlate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
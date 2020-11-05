package com.example.studentfeedback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UniNameAdapter  extends RecyclerView.Adapter<UniNameHolder> {

    Context c;
    ArrayList<Model> models;


    public UniNameAdapter(SelectUniversity selectUniversity, ArrayList<Model> models) {
        this.c =c;
        this.models = models;
    }

    @NonNull
    @Override
    public UniNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_name, null);

        return new UniNameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniNameHolder holder, int position) {

        holder.UniName.setText(models.get(position).getUniname());
        holder.UniLogo.setImageResource(models.get(position).getImage());



    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

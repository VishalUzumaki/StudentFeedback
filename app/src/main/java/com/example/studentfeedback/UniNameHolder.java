package com.example.studentfeedback;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UniNameHolder extends RecyclerView.ViewHolder{


    ImageView UniLogo;
    TextView UniName;

    public UniNameHolder(@NonNull View itemView) {
        super(itemView);
         this.UniLogo = itemView.findViewById(R.id.imageIv);
         this.UniName = itemView.findViewById(R.id.uniName);
    }
}

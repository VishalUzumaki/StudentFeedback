package com.example.studentfeedback;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UniNameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    ImageView UniLogo;
    TextView UniName;

    UniversityClick universityClick;


    public UniNameHolder(@NonNull View itemView) {
        super(itemView);
         this.UniLogo = itemView.findViewById(R.id.imageIv);
         this.UniName = itemView.findViewById(R.id.uniName);

         itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        this.universityClick.onUniversitySelected(view,getLayoutPosition());

    }


    public void setItemClickListener(UniversityClick uc){

        this.universityClick = uc;

    }
}

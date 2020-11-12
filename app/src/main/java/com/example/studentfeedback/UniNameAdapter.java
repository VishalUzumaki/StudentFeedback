package com.example.studentfeedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UniNameAdapter  extends RecyclerView.Adapter<UniNameHolder> {

    Context c;
    ArrayList<Model> models;


    public UniNameAdapter(SelectUniversity selectUniversity, ArrayList<Model> models) {
        this.c =selectUniversity;
        this.models = models;
    }

    @NonNull
    @Override
    public UniNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_name, null); // layout for each file

        return new UniNameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UniNameHolder holder, int position) {

        holder.UniName.setText(models.get(position).getUniname());
        holder.UniLogo.setImageResource(models.get(position).getImage());

        holder.setItemClickListener(new UniversityClick() {
            @Override
            public void onUniversitySelected(View v, int position) {

                String UniversityName = models.get(position).getUniname();

                String extension = models.get(position).getExtension();

                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.UniLogo.getDrawable();

                Bitmap bitmap = bitmapDrawable.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] bytes = stream.toByteArray();

                Intent intent = new Intent(c,Login.class);

                intent.putExtra("name",UniversityName);
                intent.putExtra("extension",extension);
                intent.putExtra("image",bytes);

                SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("University", UniversityName); // Storing University Name

                editor.commit(); // commit changes

                c.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

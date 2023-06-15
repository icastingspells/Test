package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koteki.R;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>  {
    private final LayoutInflater inflater;
    ArrayList<Profile> profiles;

    public ProfileAdapter(Context context, ArrayList<Profile> profiles) {
        this.inflater = LayoutInflater.from(context);
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag((long)profiles.get(position)._id);
        holder.textViewName.setText(profiles.get(position).name.toString());
        holder.textViewAge.setText(String.valueOf(profiles.get(position).age));
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewName, textViewAge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name);
            textViewAge = itemView.findViewById(R.id.age);
        }
    }
}

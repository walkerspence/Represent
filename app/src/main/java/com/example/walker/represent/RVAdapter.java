package com.example.walker.represent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView repName;
        FloatingActionButton repSite;
        FloatingActionButton repInfo;
        CircleImageView repPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            repName = (TextView)itemView.findViewById(R.id.rep_name);
            repPhoto = (CircleImageView) itemView.findViewById(R.id.rep_photo);
            repInfo = (FloatingActionButton) itemView.findViewById(R.id.rep_info);
            repSite = (FloatingActionButton) itemView.findViewById(R.id.rep_site);
        }
    }

    List<Representative> representatives;

    RVAdapter(List<Representative> representatives){
        this.representatives = representatives;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        if (representatives.get(i).repInfo.get("party").equals("Democrat")) {
            v.findViewById(R.id.rep_card_layout).setBackgroundColor(Color.parseColor("#5C508B"));
        } else if (representatives.get(i).repInfo.get("party").equals("Republican")) {
            v.findViewById(R.id.rep_card_layout).setBackgroundColor(Color.parseColor("#C96868"));
        } else {
            v.findViewById(R.id.rep_card_layout).setBackgroundColor(Color.parseColor("#40757A"));
        }
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {
        String type;
        if (representatives.get(i).repInfo.get("type").equals("representative")) {
            type = "Rep. ";
        } else {
            type = "Sen. ";
        }
        final Representative rep = representatives.get(i);
        final String title = type + representatives.get(i).repInfo.get("first_name") + " " + representatives.get(i).repInfo.get("last_name");
        final String url = rep.repInfo.get("url");
        final String mail = rep.repInfo.get("contact_form");
        final String party = rep.repInfo.get("party");
        final String photo = rep.url;
        personViewHolder.repName.setText(title);
        new ImageLoadTask(representatives.get(i).url, personViewHolder.repPhoto).execute();
        personViewHolder.repInfo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MoreInfo.class);
                myIntent.putExtra("title", title);
                myIntent.putExtra("url", url);
                myIntent.putExtra("mail", mail);
                myIntent.putExtra("party", party);
                myIntent.putExtra("photo", photo);
                ((Activity)v.getContext()).startActivityForResult(myIntent, 0);
            }} );
        personViewHolder.repSite.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            v.getContext().startActivity(intent);
        }} );
    }

    @Override
    public int getItemCount() {
        return representatives.size();
    }
}
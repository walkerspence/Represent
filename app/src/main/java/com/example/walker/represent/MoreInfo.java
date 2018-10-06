package com.example.walker.represent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreInfo extends AppCompatActivity {
    String title;
    String url;
    String mail;
    String party;
    String photo_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");
        mail = getIntent().getExtras().getString("mail");
        party = getIntent().getExtras().getString("party");
        photo_url = getIntent().getExtras().getString("photo");

        CardView cv = (CardView) findViewById(R.id.cv);
        CircleImageView photo = (CircleImageView) findViewById(R.id.rep_photo);
        new ImageLoadTask(photo_url, photo).execute();

        if (party.equals("Democrat")) {
            findViewById(R.id.rep_card_layout).setBackgroundColor(Color.parseColor("#5C508B"));
        } else if (party.equals("Republican")) {
            findViewById(R.id.rep_card_layout).setBackgroundColor(Color.parseColor("#C96868"));
        } else {
            findViewById(R.id.rep_card_layout).setBackgroundColor(Color.parseColor("#40757A"));
        }

        TextView rep_name = (TextView) findViewById(R.id.rep_name);
        rep_name.setText(title);

        TextView rep_party = (TextView) findViewById(R.id.rep_party);
        rep_party.setText(party);

        FloatingActionButton rep_site = (FloatingActionButton) findViewById(R.id.rep_site);
        FloatingActionButton rep_mail = (FloatingActionButton) findViewById(R.id.rep_mail);

        rep_site.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            v.getContext().startActivity(intent);
        }} );

        if (mail != null && !mail.equals("null")) {
            System.out.println(mail);
            rep_mail.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
                Uri uri = Uri.parse(mail);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }} );
        } else {
            ViewGroup layout = (ViewGroup) rep_mail.getParent();
            layout.removeView(rep_mail);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(rep_site.getLayoutParams());
            lp.setMargins(37, 0, 0, 0);
            rep_site.setLayoutParams(lp);
        }

    }

}

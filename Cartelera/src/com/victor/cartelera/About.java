package com.victor.cartelera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity 
{
	TextView txtMail;
	ImageView imgPromoter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        txtMail = (TextView) findViewById(R.id.textView4);
        imgPromoter = (ImageView) findViewById(R.id.imageView1);
        
        txtMail.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				
				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vpalmacarrasco@gmail.com"});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
				emailIntent.setType("message/rfc822");
				
				startActivity(Intent.createChooser(emailIntent, "Email "));
			}
		});
        
        imgPromoter.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(Intent.ACTION_VIEW);
				String ruta = "https://www.themoviedb.org/";
				i.setData(Uri.parse(ruta));
		    	startActivity(i);
			}
		});
    }
}

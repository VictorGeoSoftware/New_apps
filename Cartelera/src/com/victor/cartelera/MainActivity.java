package com.victor.cartelera;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity 
{
	//----- VIEW ELEMENTS
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) 
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
        
        
        

        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment 
    {
    	Button button;
    	View rootView;
    	
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            button = (Button) rootView.findViewById(R.id.button1);
            return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) 
        {
        	super.onActivityCreated(savedInstanceState);
        	
        	button.setOnClickListener(new OnClickListener()
        	{
				@Override
				public void onClick(View v) 
				{
					Toast.makeText(getActivity().getApplicationContext(), "Hola ke ase", Toast.LENGTH_SHORT).show();
				}
			});
        }

    }

}

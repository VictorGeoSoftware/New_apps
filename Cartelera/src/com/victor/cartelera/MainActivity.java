package com.victor.cartelera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity 
{
	//----- VIEW ELEMENTS
	//----- Sidebar
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
	
	
	
	
	//----- VARIABLES
		//----- Sidebar
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private String[] mPlanetTitles;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    	//----- INITIALIZATION OF SIDEBAR
	    mTitle = mDrawerTitle = getTitle();
	    mPlanetTitles = getResources().getStringArray(R.array.menu_array);
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.left_drawer);
	    
	    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.drawer_list_item, mPlanetTitles);
	    mDrawerList.setAdapter(adaptador);
	    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	    
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, R.string.drawer_open, R.string.drawer_close)
		        {
		        	public void onDrawerClosed(View view)
		        	{
		        		super.onDrawerClosed(view);
		        		getActionBar().setTitle(mTitle);
		        		invalidateOptionsMenu();
		        	}
		        	
		        	@Override
		        	public void onDrawerOpened(View drawerView) 
		        	{
		        		super.onDrawerOpened(drawerView);
		        		getActionBar().setTitle(mDrawerTitle);
		        		invalidateOptionsMenu();
		        	}
		        };
        
		        mDrawerLayout.setDrawerListener(mDrawerToggle);
		        if (savedInstanceState == null) {
		            selectItem(0);
		        }
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		        
        if (savedInstanceState == null) 
        {
            selectItem(0);
        }
        
    }


    
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) 
		{
			selectItem(position);
		}
    	
    }
  
    private void selectItem(int position)
    {
    	PlaceholderFragment fragment = new PlaceholderFragment();
    	Bundle args = new Bundle();
    	
//    	fragment.setArguments(args);
//    	
//    	FragmentManager fragmentManager = getFragmentManager();
//    	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    	
    	mDrawerList.setItemChecked(position, true);
    	setTitle(mPlanetTitles[position]);
    	mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    public void setTitle(CharSequence title)
    {
    	mTitle = title;
    	getActionBar().setTitle(mTitle);
    }
    
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    	menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
    	return super.onPrepareOptionsMenu(menu);
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
    	if(mDrawerToggle.onOptionsItemSelected(item))
    	{
    		return true;
    	}
    	
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

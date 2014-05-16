package com.victor.cartelera;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;
import com.victor.cartelera.HttpRequest.HttpRequestException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.webkit.WebChromeClient.CustomViewCallback;
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
	static //----- VIEW ELEMENTS
	TextView txtListTitle;
		//----- Sidebar
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    

	 //----- VARIABLES
    static String lblOriginalTitle = "";
	static String lblPremiereDay = "";
	static String lblDescription = "";
	static String lblUnavaliable = "";
	static String lblNextFilms = "";
	static String lblFoundedFilms = "";
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
        
        //------ INITIALIZING OF STRINGS
        //----- INICIALIZACION DE LITERALES
        lblOriginalTitle = getResources().getString(R.string.titulo_original);
        lblPremiereDay = getResources().getString(R.string.fecha_estreno);
        lblDescription = getResources().getString(R.string.descripcion);
        lblUnavaliable = getResources().getString(R.string.no_disponible);
        lblFoundedFilms = getResources().getString(R.string.peliculas_encontradas);
        lblNextFilms = getResources().getString(R.string.proximos_lanzamientos);
        
    }


    
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) 
		{
			txtListTitle.setText(lblNextFilms);
			selectItem(position);
		}
    	
    }
  
    private void selectItem(int position)
    {
    	PlaceholderFragment fragment = new PlaceholderFragment();
    	Bundle args = new Bundle();
    	args.putInt(PlaceholderFragment.MENU_NUMBER, position);
    	fragment.setArguments(args);
    	
    	FragmentManager fragmentManager = getFragmentManager();
    	fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    	
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
    	
    	switch (item.getItemId())
    	{
    		case R.id.action_settings:
    			Toast.makeText(getApplicationContext(), getResources().getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
    		break;
    		
			case R.id.action_websearch:
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.action_websearch), Toast.LENGTH_SHORT).show();
			break;
	
			default:
			break;
		}
    	
        return super.onOptionsItemSelected(item);
    }
    

    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment 
    {
    	//------ Variables declaration
    	public static final String MENU_NUMBER = "menu_number";
    	
    	int option = 0;
    	int numberFilms = 0;
    	String apiWeb = "https://api.themoviedb.org/3";
    	String apiKey = "2ee24d57cde7770db40b27c27759bdfd";    	
    	BeanFindFilms[] beanFilmsFounded;
    	
    		//----- Dialogs variables
    	String pathImageSelectedFilm = "";
    	String titleSelectedFilm = "";
    	String originalTitleSelectedFilm = "";
    	String premiereSelectedFilm = "";
    	String descriptionSelectedFilm = "";
    	
    		//----- GENRES ID
    	private static final int ACTION_ID = 28;
    	private static final int COMEDY_ID = 35;
    	private static final int DRAMA_ID = 18;
    	private static final int ROMANCE_ID = 10749;
    	
    	//----- Elements declaration
    	View rootView;
    		//----- Main fragment
    	
    	EditText edtFilmSearch;
    	ImageView imgButtonSearch;
    	ListView lstFoundedFilms;
    		//----- Adapter
    	Activity adapterContextActiviy;  //--> Necessary for instantiate activity in custom arrayAdapter

    	
    	
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            option = getArguments().getInt(MENU_NUMBER);
            adapterContextActiviy = getActivity();
            
            
            //----- Elements init
            txtListTitle = (TextView) rootView.findViewById(R.id.textView2);
            edtFilmSearch = (EditText) rootView.findViewById(R.id.editText1);
            imgButtonSearch = (ImageView) rootView.findViewById(R.id.imageView1);
            lstFoundedFilms = (ListView) rootView.findViewById(R.id.listView1);
            
            return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) 
        {
        	super.onActivityCreated(savedInstanceState);
        	
        	//----- Initializing API
        	String url = "";
        	switch (option)
        	{
	        	case 0:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey);
	        	break;
	        	case 1:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres=" + ACTION_ID);
	        	break;
	        	case 2:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres="  + DRAMA_ID);	        		
	        	break;
	        	case 3:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres="  + COMEDY_ID);
	        	break;
	        	case 4:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres="  + ROMANCE_ID);
	        	break;
        	}
        	url = url + "&language=es&release_date.gte=2014-01-01&release_date.lte=2015-01-01&sort_by=release_date.desc";
			
			new LoadFilmTask().execute(url);
        	
        	
        	//----- Events for elements
        	imgButtonSearch.setOnClickListener(new OnClickListener()
        	{	
				@Override
				public void onClick(View v) 
				{
					String receivedFilm = edtFilmSearch.getText().toString();
					AnimationSet as = new AnimationSet(true);
					AlphaAnimation aa = new AlphaAnimation(0,1);
					aa.setDuration(500);
					as.addAnimation(aa);
					imgButtonSearch.startAnimation(as);
					
					if(!receivedFilm.contentEquals(""))
					{
						txtListTitle.setText(lblFoundedFilms);
						String url = String.format(apiWeb + "/search/movie?&sort_by=release_date.desc&query="
								+ receivedFilm + "&api_key=" +apiKey + "&language=es&include_image_language=es");
						new LoadFilmTask().execute(url);
					}
				}
			});
        	
        	lstFoundedFilms.setOnItemClickListener(new OnItemClickListener() 
        	{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
				{
					int idFilm = beanFilmsFounded[position].getIdFilm();
					pathImageSelectedFilm = beanFilmsFounded[position].getImagePath();
					titleSelectedFilm = beanFilmsFounded[position].getTittle();
				    originalTitleSelectedFilm = beanFilmsFounded[position].getSubTittle();
					premiereSelectedFilm = beanFilmsFounded[position].getPremiereDay();
					String url = String.format(apiWeb + "/movie/"+ idFilm +	"?&api_key="+ apiKey +"&language=es");
					new LoadDescriptionFilmTask().execute(url);
				}
			});
        }
        
        
        //---------- ASYNC TASK ------------------------------------
        private class LoadFilmTask extends AsyncTask<String, Long, String>
        {
        	ProgressDialog dialog = new ProgressDialog(getActivity());

        	@Override
        	protected void onPreExecute() 
        	{
        		super.onPreExecute();
        		dialog.setMessage(getResources().getString(R.string.msg_consultando_informacion));
        		dialog.show();
        	}
        	
    		@Override
    		protected String doInBackground(String... url) 
    		{
    			try
    			{
    				return HttpRequest.get(url[0]).accept("application/json").body();
    			}
    			catch(HttpRequestException e)
    			{
    				return null;
    			}
    		}
        	
    		@Override
    		protected void onPostExecute(String result) 
    		{
    			Log.i("on post execute LoadFilm", "result: " + result);
    			getFoundedFilms(result);

    			if(dialog.isShowing())
    			{
    				dialog.dismiss();
    			}
    		}
        }
        
        private class LoadDescriptionFilmTask extends AsyncTask<String, Long, String>
        {
        	ProgressDialog dialog = new ProgressDialog(getActivity());
        	
        	@Override
        	protected void onPreExecute() 
        	{
        		// TODO Auto-generated method stub
        		super.onPreExecute();
        		dialog.setMessage(getResources().getString(R.string.msg_consultando_pelicula));
        		dialog.show();
        	}
        	
    		@Override
    		protected String doInBackground(String... params) 
    		{
    			try
    			{
    				return HttpRequest.get(params[0]).accept("application/json").body();
    			}
    			catch(HttpRequestException e)
    			{
    				return null;
    			}
    		}
    		
    		@Override
    		protected void onPostExecute(String result) 
    		{
    			Log.i("load description", "description result: " + result);
    			try 
    			{
    				if(dialog.isShowing())
    				{
    					dialog.dismiss();
    				}
    				
    				JSONObject receivedJson = new JSONObject(result);
    				String descriptionReceived = receivedJson.getString("overview");
    				
    				Log.i("on post execute", "description received: " + descriptionReceived);
    				if(descriptionReceived.contentEquals("null"))
    				{
    					descriptionReceived = lblUnavaliable;
    				}
   				
    				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		    	LayoutInflater inflater = getActivity().getLayoutInflater();
    		    	View layout = inflater.inflate(R.layout.dialog_selected_film, null);
    		    	builder.setView(layout);
	    				ImageView dialogImageView = (ImageView) layout.findViewById(R.id.imageView1);
	    				TextView txtDialogTitle = (TextView) layout.findViewById(R.id.textView1);
	    				TextView txtDialogOriginalTitle = (TextView) layout.findViewById(R.id.textView2);
	    				TextView txtDialogPremiereDay = (TextView) layout.findViewById(R.id.textView3);
	    				TextView txtDialogDescription = (TextView) layout.findViewById(R.id.textView4);
	    				
	    				String path = pathImageSelectedFilm.replace("http://image.tmdb.org/t/p/w500", "");
	    				
	    				txtDialogTitle.setText(titleSelectedFilm);
	    				txtDialogOriginalTitle.setText(lblOriginalTitle + ": " + originalTitleSelectedFilm);
	    				txtDialogPremiereDay.setText(lblPremiereDay + ": " + premiereSelectedFilm);
	    				txtDialogDescription.setText(lblDescription + ": " + descriptionReceived);
	    				
	    				Log.i("on post execute", "path selected film: " + pathImageSelectedFilm + " " + path);
	    				if(path.contentEquals("null"))
	    				{
	    					dialogImageView.setImageDrawable(getResources().getDrawable(R.drawable.no_disponible));
	    				}
	    				else
	    				{
	    					Picasso.with(getActivity()).load(pathImageSelectedFilm).into(dialogImageView);
	    				}
	    				
    		    	builder.show();
    			}
    			catch (JSONException e) 
    			{
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
        }
        

        
      //---------- METODOS Y FUNCIONES ------------------------------------
        public void getFoundedFilms(String result)
        {
        	try 
        	{	
        		String apiPath = "http://image.tmdb.org/t/p/w500";
        		
    			JSONObject receivedJson = new JSONObject(result);
    			numberFilms = receivedJson.getInt("total_results");
    			JSONArray jArrayResults = receivedJson.getJSONArray("results");

    			ArrayList<String> adultValues = getJsonElements(jArrayResults, "adult");
    			ArrayList<String> idValues = getJsonElements(jArrayResults, "id");
    			ArrayList<String> titleValues = getJsonElements(jArrayResults, "title");
    			ArrayList<String> originaltitleValues = getJsonElements(jArrayResults, "original_title");
    			ArrayList<String> dateValues = getJsonElements(jArrayResults, "release_date");
    			ArrayList<String> posterPathValues = getJsonElements(jArrayResults, "poster_path");
    			
    			beanFilmsFounded = new BeanFindFilms[adultValues.size()];
    			for(int i = 0; i < adultValues.size(); i++)
    			{
    				int id = Integer.parseInt(idValues.get(i));
    				String title = titleValues.get(i);
    				String subTitle = originaltitleValues.get(i);
    				String premiereDay = formatDate(dateValues.get(i));
    				String posterPath = apiPath + posterPathValues.get(i);
    				Log.i("get founded films", "idFiml on get founded: " + id);
    				beanFilmsFounded[i] = new BeanFindFilms(id, posterPath, title, subTitle, premiereDay);
    			}
    			
    			
    			Log.i("on post execute", "elements: " + adultValues.size() + " " + idValues.size() + " " + titleValues.size() + " "
    					+ originaltitleValues.size()+ " " + dateValues.size() + " " + posterPathValues.size());
    			
    			FilmListAdapter adapter = new FilmListAdapter(adapterContextActiviy);
    			lstFoundedFilms.setAdapter(adapter);
    		}
        	catch (JSONException e) 
        	{
        		Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.msg_no_pelicula), Toast.LENGTH_SHORT).show();
    			e.printStackTrace();
    		}
        }
        
        public String formatDate(String dateUnformatted)
        {
        	String dateFormatted = "";
        	
        	if(dateUnformatted.length() > 0)
        	{
    	    	String[] unformattedDateValues = dateUnformatted.split("-");
    	    	String day = unformattedDateValues[2];
    	    	String month = unformattedDateValues[1];
    	    	String year = unformattedDateValues[0];
    	    	
    	    	dateFormatted = day + "-"+ month+ "-" + year;
        	}
        	
        	return dateFormatted;
        }
        
        
        
            
        public ArrayList<String> getJsonElements(JSONArray jsonArray, String objectName)
        {
        	ArrayList<String> foundedElements = new ArrayList<String>();
        	
        	for(int i = 0; i < jsonArray.length(); i++)
        	{
        		try 
        		{
    				String element = jsonArray.getJSONObject(i).getString(objectName);
    				foundedElements.add(element);
    			}
        		catch (JSONException e) 
        		{
    				e.printStackTrace();
    			}
        	}
        	
        	return foundedElements;
        }
        
        class FilmListAdapter extends ArrayAdapter<Object>
        {
        	Activity context;
        	
    		public FilmListAdapter(Activity context) 
    		{
    			super(context, R.layout.adapter_peliculas_encontradas, beanFilmsFounded);
    			this.context = context;
    		}
    		
    		public View getView(int position, View convertView, ViewGroup parent)
    		{
    			LayoutInflater inflater = context.getLayoutInflater();
    			View item = inflater.inflate(R.layout.adapter_peliculas_encontradas, null);
    			
    			ImageView imageView = (ImageView)item.findViewById(R.id.imageView1);
    			TextView txtTitle = (TextView)item.findViewById(R.id.textView1);
    			TextView txtOriginalTitle = (TextView)item.findViewById(R.id.textView2);
    			TextView txtPremiereDay = (TextView)item.findViewById(R.id.textView3);
    			
    			if(beanFilmsFounded[position].getImagePath().replace("http://image.tmdb.org/t/p/w500", "").contentEquals("null"))
    			{
    				imageView.setImageDrawable(getResources().getDrawable(R.drawable.no_disponible));
    			}
    			else
    			{
    				Picasso.with(getActivity()).load(beanFilmsFounded[position].getImagePath()).into(imageView);
    			}
    			
    			txtTitle.setText(beanFilmsFounded[position].getTittle());
    			txtOriginalTitle.setText(lblOriginalTitle + ": " + beanFilmsFounded[position].getSubTittle());
    			txtPremiereDay.setText(lblPremiereDay + ": " + beanFilmsFounded[position].getPremiereDay());
    			
    			return item;
    		}
        	
        }

    }

}

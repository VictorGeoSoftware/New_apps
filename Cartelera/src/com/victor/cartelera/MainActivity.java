package com.victor.cartelera;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.victor.cartelera.HttpRequest.HttpRequestException;

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
	static String language = "en";
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
    			Intent i = new Intent(MainActivity.this, About.class);
    			startActivity(i);
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
    	boolean queryByFilm = false;
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
    	Button btnSearch;
    	ListView lstFoundedFilms;
    		//----- Adapter
    	Activity adapterContextActiviy;  //--> Necessary for instantiate activity in custom arrayAdapter

    	
        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            option = getArguments().getInt(MENU_NUMBER);
            adapterContextActiviy = getActivity();
            
            
            //----- Elements init
            txtListTitle = (TextView) rootView.findViewById(R.id.textView2);
            edtFilmSearch = (EditText) rootView.findViewById(R.id.editText1);
            btnSearch= (Button) rootView.findViewById(R.id.button1);
            lstFoundedFilms = (ListView) rootView.findViewById(R.id.listView1);
            
            return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) 
        {
        	super.onActivityCreated(savedInstanceState);
        	
        	String[] dates = getTodayDate().split(" ");

        	//----- Initializing API
        	String url = "";
        	language = Locale.getDefault().getLanguage();
        	queryByFilm = false;
        	Log.i("on activity created", "language: " + language);
        	switch (option)
        	{
	        	case 0:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey) + "&release_date.lte=" + dates[0];
	        	break;
	        	case 1:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres=" + ACTION_ID + "&release_date.lte=" + dates[1]);
	        	break;
	        	case 2:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres="  + DRAMA_ID + "&release_date.lte=" + dates[1]);	        		
	        	break;
	        	case 3:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres="  + COMEDY_ID + "&release_date.lte=" + dates[1]);
	        	break;
	        	case 4:
	        		url = String.format(apiWeb + "/discover/movie?&api_key=" + apiKey + 
	        				"&with_genres="  + ROMANCE_ID + "&release_date.lte=" + dates[1]);
	        	break;
        	}

        	url = url + "&language=" + language + "&sort_by=release_date.desc";
        	Log.i("on activity created", "url: " + url);
			
			new LoadFilmTask().execute(url);
        	
        	
        	//----- Events for elements
        	btnSearch.setOnClickListener(new OnClickListener()
        	{	
				@Override
				public void onClick(View v) 
				{
					String receivedFilm = edtFilmSearch.getText().toString();

					if(!receivedFilm.contentEquals(""))
					{
						txtListTitle.setText(lblFoundedFilms);
						String url = String.format(apiWeb + "/search/movie?&sort_by=release_date.desc&query="
								+ receivedFilm + "&api_key=" +apiKey + "&language="+language+"&include_image_language="+language);
						queryByFilm = true;
						
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
					String url = String.format(apiWeb + "/movie/"+ idFilm +	"?&api_key="+ apiKey +"&language="+language);
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
	    				final ImageView searchImageView = (ImageView) layout.findViewById(R.id.imageView2);
	    				TextView txtDialogTitle = (TextView) layout.findViewById(R.id.textView1);
	    				TextView txtDialogOriginalTitle = (TextView) layout.findViewById(R.id.textView2);
	    				TextView txtDialogPremiereDay = (TextView) layout.findViewById(R.id.textView3);
	    				TextView txtDialogDescription = (TextView) layout.findViewById(R.id.textView4);
	    				
	    				String path = pathImageSelectedFilm.replace("http://image.tmdb.org/t/p/w500", "");
	    				
	    				txtDialogTitle.setText(titleSelectedFilm);
	    				txtDialogOriginalTitle.setText(lblOriginalTitle + ": " + originalTitleSelectedFilm);
	    				txtDialogPremiereDay.setText(lblPremiereDay + ": " + premiereSelectedFilm);
	    				txtDialogDescription.setText(lblDescription + ": " + descriptionReceived);
	    				
	    				if(path.contentEquals("null"))
	    				{
	    					dialogImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_vacio));
	    				}
	    				else
	    				{
	    					Picasso.with(getActivity()).load(pathImageSelectedFilm).into(dialogImageView);
	    				}

	    				searchImageView.setOnClickListener(new View.OnClickListener()
	    				{
							@Override
							public void onClick(View v) 
							{
								String film = getResources().getString(R.string.pelicula);
					            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
					            intent.putExtra(SearchManager.QUERY, film + " " + titleSelectedFilm);
					            startActivity(intent);
							}
						});
	    				
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
    			JSONArray jArrayResults = receivedJson.getJSONArray("results");

    			ArrayList<String> adultValues = getJsonElements(jArrayResults, "adult");
    			ArrayList<String> idValues = getJsonElements(jArrayResults, "id");
    			ArrayList<String> titleValues = getJsonElements(jArrayResults, "title");
    			ArrayList<String> originaltitleValues = getJsonElements(jArrayResults, "original_title");
    			ArrayList<String> dateValues = getJsonElements(jArrayResults, "release_date");
    			ArrayList<String> posterPathValues = getJsonElements(jArrayResults, "poster_path");
    			
    			//ArrayList order inverted when is showing a film genre list
    			if(!queryByFilm)
    			{
	    			Collections.reverse(adultValues);
	    			Collections.reverse(idValues);
	    			Collections.reverse(titleValues);
	    			Collections.reverse(originaltitleValues);
	    			Collections.reverse(dateValues);
	    			Collections.reverse(posterPathValues);
    			}

    			beanFilmsFounded = new BeanFindFilms[adultValues.size()];
    			
    			for(int i = 0; i < adultValues.size(); i++)
    			{
    				int id = Integer.parseInt(idValues.get(i));
    				String title = titleValues.get(i);
    				String subTitle = originaltitleValues.get(i);
    				String premiereDay = formatDate(dateValues.get(i));
    				String posterPath = apiPath + posterPathValues.get(i);

    				beanFilmsFounded[i] = new BeanFindFilms(id, posterPath, title, subTitle, premiereDay);
    			}
    			
    			Log.i("GetFilms", "elementos: " + adultValues.size() + " / " + beanFilmsFounded.length);

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
        
        public String getTodayDate()
        {
        	Calendar c = Calendar.getInstance();
        	SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd");
        	String todayDate = todayFormat.format(c.getTime());
        	
        	c.add(Calendar.MONTH, +3);
        	
        	String nextDate = todayFormat.format(c.getTime());
        	todayDate = todayDate + " " + nextDate;
        	
        	return todayDate;
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
    				Picasso.with(getActivity()).load(R.drawable.ic_vacio).into(imageView);
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

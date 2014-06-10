package com.victor.accesorios;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TooManyListenersException;























import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.victor.accesorios.HttpRequest.HttpRequestException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    private static String jsonResult = "";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.about_me_view, null);
            builder.setView(layout);
            
            ImageView imageView = (ImageView) layout.findViewById(R.id.imageView1);
            TextView txtMail = (TextView) layout.findViewById(R.id.textView4);
            
            imageView.setOnClickListener(new View.OnClickListener()
            {
				@Override
				public void onClick(View v) 
				{
			    	Intent i = new Intent(Intent.ACTION_VIEW);
			    	String ruta = "http://www.geosoftware.es";
			    	i.setData(Uri.parse(ruta));
			    	startActivity(i);
				}
			});
            
            txtMail.setOnClickListener(new View.OnClickListener()
            {
				@Override
				public void onClick(View v) 
				{
					Intent emailIntent = new Intent(Intent.ACTION_SEND);
					
					emailIntent.setData(Uri.parse("mailto:"));
					emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vpalmacarrasco@gmail.com"});
					emailIntent.setType("message/rfc822");
					
					startActivity(Intent.createChooser(emailIntent, "Email "));
				}
			});
            
            builder.setPositiveButton(getResources().getString(R.string.aceptar), new OnClickListener()
            {
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			});
            
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


	public String getJsonResult() {
		return jsonResult;
	}


	public void setJsonResult(String jsonResult) {
		MainActivity.jsonResult = jsonResult;
	}

	/**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

        	switch (position) 
        	{
				case 0:
					return PlaceholderFragment.newInstance();
					
				case 1:
					return CompassFragment.newInstance();
					
				case 2:
					return BurbleFragment.newInstance();
					
				case 3:
					return WeatherFragment.newInstance();
					
				default:
					return null;
			}
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.catalog).toUpperCase(l);
                case 1:
                    return getString(R.string.compass).toUpperCase(l);
                case 2:
                    return getString(R.string.burble_level).toUpperCase(l);
                case 3:
                	return getString(R.string.meteorologia).toUpperCase(l);
            }
            return null;
        }
    }
    
    
    

    
    /**
     * FRAGMENTS
     */
    public static class WeatherFragment extends Fragment
    {
    	//----- Elements
    	View rootView;
    	ListView lstWeather;
    	TextView txtCityName;
    	Activity adapterActivity;
    	
    	
    	//----- Variables
    	boolean requestMade = false;
    	private static String API = "http://api.openweathermap.org/data/2.5/forecast?";
    	private static String API_KEY = "2d05913cf6eebef49b01b4894090f3c8";
    	
    	double latitude = 0;
    	double longitude = 0;
    	
    	String temperature;
    	String sky;
    	String pressure;
    	String humidity;
    	
    	BeanWeatherList[] weatherList;
    	
    	LocationManager locationManager;
    	LocationListener locListenerNetwork;
    	
		
    	public WeatherFragment(){}
    	
    	public static WeatherFragment newInstance()
    	{
    		WeatherFragment weatherFragment = new WeatherFragment();
    		return weatherFragment;
    	}
    	
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    	{
    		// TODO Auto-generated method stub
    		rootView = inflater.inflate(R.layout.fragment_weather, container, false);
    		lstWeather = (ListView) rootView.findViewById(R.id.listView1);
    		txtCityName = (TextView) rootView.findViewById(R.id.textView1);
    		adapterActivity = this.getActivity();
    		
    		//----- Strings resources
    		temperature = getResources().getString(R.string.temperatura) + ": ";
    		sky = getResources().getString(R.string.cielo) + ": ";
    		pressure = getResources().getString(R.string.presion) + ": ";
    		humidity = getResources().getString(R.string.humedad) + ": ";
    		
    		
    		return rootView;
    	}
    	
    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) 
    	{
    		// TODO Auto-generated method stub
    		super.onActivityCreated(savedInstanceState);
    		
    			//----- Getting position
    		locListenerNetwork = new MyLocationListener();
    		
    		locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListenerNetwork);
    		
    			//----- Refresh listView (if needed)
    		MainActivity jObject = new MainActivity();
    		String result = "";
    		
    		if(requestMade)
    		{
    			result = jObject.getJsonResult();
    		}
    		
    		if(lstWeather.getCount() < 1 && !result.contentEquals(""))
    		{
    			getDataFromJson(result);
    		}
    	}
    	
    	
    	
    	//----- Methods and Subroutines
    	private class MyLocationListener implements LocationListener
    	{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				
				if(latitude != 0 && longitude != 0 && requestMade == false && rootView != null)
				{
					//----- JSON request
					String language = Locale.getDefault().getLanguage();
					if(language.contentEquals("es")) language = "sp";
					
					String url = API + "lat="+ latitude + "&lon="+ longitude + "&lang="+ language + "&units=metric&APPID"+API_KEY;
					
					new WeatherTask().execute(url);
					requestMade = true;
				}
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
			}
    	}
    	
    	
    	private class WeatherTask extends AsyncTask<String, Long, String>
    	{
    		
    		@Override
    		protected void onPreExecute() 
    		{
    			// TODO Auto-generated method stub
    			super.onPreExecute();
    		}
    		
			@Override
			protected String doInBackground(String... params) 
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				getDataFromJson(result);
				
				MainActivity jObject = new MainActivity();
				jObject.setJsonResult(result);
			}
    	}
    	
    	public void getDataFromJson(String result)
    	{
			try
			{
				JSONObject receivedJson = new JSONObject(result);
				Log.i("onPostExecute", "result:: " + result);
				JSONObject citiyData = receivedJson.getJSONObject("city");
				String cityName = citiyData.getString("name").replace("-", " ");
				txtCityName.setText(cityName);
				
				
				JSONArray jArrayList = receivedJson.getJSONArray("list");					
				weatherList = new BeanWeatherList[jArrayList.length()];
				
				String oldData = ""; // I only take the first data of one day
				for(int i = 0; i < jArrayList.length(); i++)
				{
					String[] dataHour = jArrayList.getJSONObject(i).getString("dt_txt").split(" ");
					String data = dataHour[0];
					String hour = dataHour[1];
					
					Log.i("getDataFromJson", "data / oldData: " + data + " / " + oldData);	
						JSONObject mainObject = jArrayList.getJSONObject(i).getJSONObject("main");
					String temperatures = mainObject.getString("temp_min") + "ºC / " + mainObject.get("temp_max") + "ºC";
					String pressure = mainObject.getString("pressure");
					String humidity = mainObject.getString("humidity");
					
						JSONArray weatherArray = jArrayList.getJSONObject(i).getJSONArray("weather");
					String description = weatherArray.getJSONObject(0).getString("description");
					String icon = weatherArray.getJSONObject(0).getString("icon");
					
					if(!oldData.contentEquals(data))
					{
						oldData = data;
						weatherList[i] = new BeanWeatherList(oldData, hour, temperatures, description, pressure, humidity, icon);
					}
					else
					{
						weatherList[i] = new BeanWeatherList("", hour, temperatures, description, pressure, humidity, icon);
					}
//					weatherList[i] = new BeanWeatherList(oldData, hour, temperatures, description, pressure, humidity, icon);
				}
				
				WeatherListAdapter adapter = new WeatherListAdapter(adapterActivity, weatherList);
				lstWeather.setAdapter(adapter);
			}
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getActivity(), getResources().getString(R.string.msg_no_informacion), Toast.LENGTH_SHORT).show();
			}
    	}
    	
    	class WeatherListAdapter extends ArrayAdapter<Object>
    	{
    		Activity context;
    		BeanWeatherList list[];
    		String imageUri = "http://openweathermap.org/img/w/";
    		
    		public WeatherListAdapter(Activity context, BeanWeatherList[] list) 
    		{
				super(context, R.layout.weather_row_adapter, list);
				this.context = context;
				this.list = list;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				LayoutInflater inflater = context.getLayoutInflater();
				View item = inflater.inflate(R.layout.weather_row_adapter, null);
				
				ImageView imageView = (ImageView) item.findViewById(R.id.imageView1);
				TextView txtData = (TextView) item.findViewById(R.id.textView1);
				TextView txtHour = (TextView) item.findViewById(R.id.textView2);
				TextView txtTemperature = (TextView) item.findViewById(R.id.textView3);
				TextView txtDescription = (TextView) item.findViewById(R.id.textView4);
				TextView txtPressure = (TextView) item.findViewById(R.id.textView5);
				TextView txtHumidity = (TextView) item.findViewById(R.id.textView6);
				
				Picasso.with(getActivity()).load(imageUri + list[position].getIcon() + ".png").into(imageView);

				if(list[position].getData().contentEquals(""))
				{
					txtData.setHeight(0);
				}
				else
				{
					txtData.setText(getFormattedData(list[position].getData()));
				}
				
				txtHour.setText(list[position].getHour() + "h");
				txtTemperature.setText(list[position].getTemperature());
				txtDescription.setText(list[position].getDescription());
				txtPressure.setText(pressure + list[position].getPressure() + "hPa");
				txtHumidity.setText(humidity + list[position].getHumidity() + "%");
				
				return item;
			}
    		
			public String getFormattedData(String data)
			{
				String[] dataReceived = data.split("-");
				return dataReceived[2] + "-" + dataReceived[1] + "-" + dataReceived[0];
			}
			
			public ArrayList<String> addDaysToDate()
			{
				Calendar c = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
				ArrayList<String> dates = new ArrayList<String>();
				
				dates.add(format.format(c.getTime()));
				for(int i = 0; i < 5; i++)
				{
					c.add(Calendar.DAY_OF_YEAR, 1);
					String date = format.format(c.getTime());
					dates.add(date);
				}

				return dates;
			}
    	}
    }
    
    
    public static class BurbleFragment extends Fragment implements SensorEventListener
    {
    	//----- Elements
    	View rootView;
    	ImageView imgBurble;
    	
    		//----- Device issues
    	SensorManager sensorManager;
    	Sensor accelerometer;
    	Sensor magnetometer;
    	
    	
    	//----- Variables
		float[] matrizGravity;
		float[] matrizGeomagnetic;
		float verticaGlobal = 0;
		float rollGlobal = 0;
		static final float ALPHA = 0.25f;
		
		
		public BurbleFragment(){}
		
		public static BurbleFragment newInstance()
		{
			BurbleFragment burbleFragment = new BurbleFragment();
			return burbleFragment;
		}
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		{
			// TODO Auto-generated method stub
			rootView = inflater.inflate(R.layout.fragment_burble_level, container, false);
			
    		sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
    		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    		
    		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	
			imgBurble = (ImageView) rootView.findViewById(R.id.imageView1);

			return rootView;
		}
		
		@Override
		public void setMenuVisibility(boolean menuVisible) 
		{
			// TODO Auto-generated method stub
			super.setMenuVisibility(menuVisible);
			
			if(rootView != null)
			{
				if(menuVisible)
				{
					sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		    		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
				}
				else
				{
					sensorManager.unregisterListener(this);
				}
			}
		}
		
		@Override
		public void onSensorChanged(SensorEvent event) 
		{
//			// TODO Auto-generated method stub
			float dpiFactor = 500;
			float xCentreView = rootView.getWidth()/2;
			float yCentreView = rootView.getHeight()/2;
			float xCentreImage = imgBurble.getWidth()/2;
			float yCentreImage = imgBurble.getHeight()/2;
			
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{
				matrizGravity = lowPass(event.values.clone(), matrizGravity);
			}

			if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			{
				matrizGeomagnetic = lowPass(event.values.clone(), matrizGeomagnetic);
			}
			
			if(matrizGravity != null && matrizGeomagnetic != null)
			{
				float[] R = new float[9];
				float[] I = new float[9];
				boolean recojeDatos = SensorManager.getRotationMatrix(R, I, matrizGravity, matrizGeomagnetic);
				
				if(recojeDatos)
				{
					float angles[] = new float[3];
					SensorManager.getOrientation(R, angles);
					
					float verticalAngle = angles[1]*180/3.14159f;
					float rollAngle = angles[2]*180/3.14159f;
					
					verticaGlobal = 0.9f*verticaGlobal + 0.1f*verticalAngle;
					rollGlobal = 0.9f*rollGlobal + 0.1f*rollAngle;

					double xImage = xCentreView - xCentreImage + dpiFactor*Math.cos(rollGlobal);
					double yImage = yCentreView - yCentreImage + dpiFactor*Math.cos(verticaGlobal);

					imgBurble.setTranslationX(doubleToFloat(xImage));
					imgBurble.setTranslationY(doubleToFloat(yImage));
				}
			}
			
		}
		
		public float doubleToFloat(double value)
		{
			String valueString = Double.toString(value);
			return Float.parseFloat(valueString);
		}
		
	    protected float[] lowPass(float[] input, float[] output)
	    {
	    	if(output == null)
	    	{
	    		return input;
	    	}
	    	
	    	for(int i = 0; i < input.length; i++)
	    	{
	    		output[i] = output[i] + ALPHA * (input[i] - output[i]);
	    	}
	    	
	    	return output;
	    }

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
    	
		@Override
		public void onResume() 
		{
			// TODO Auto-generated method stub
			super.onResume();
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
		}
		
		@Override
		public void onPause() 
		{
			// TODO Auto-generated method stub
			super.onPause();
			sensorManager.unregisterListener(this);
		}
    }
    
    
    public static class CompassFragment extends Fragment implements SensorEventListener
    {
    	//----- Elements
    	View rootView;
        ImageView imgCompass;
        TextView lblAzimuth;
        TextView lblVerticalAngle;
        TextView lblInclination;
        RadioGroup radioGroup;
        
        	//----- Device issues
    	SensorManager sensorManager;
    	Sensor accelerometer;
    	Sensor magnetometer;
    	
    	
    	//----- Variables
		float[] matrizGravity;
		float[] matrizGeomagnetic;
		boolean cente = true;
		float angleFactor = 200;
		
		
		public CompassFragment(){}
		
		public static CompassFragment newInstance()
		{
			CompassFragment compassFragment = new CompassFragment();
			return compassFragment;
		}
		
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		{
			// TODO Auto-generated method stub
			rootView = inflater.inflate(R.layout.fragment_compass, container, false);
			
    		sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
    		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    		
    		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    		
			imgCompass = (ImageView) rootView.findViewById(R.id.imageView1);
			radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
			lblAzimuth = (TextView) rootView.findViewById(R.id.textView4);
			lblVerticalAngle = (TextView) rootView.findViewById(R.id.textView5);
			lblInclination = (TextView) rootView.findViewById(R.id.textView6);
			
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) 
		{
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
    		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
    		{	
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) 
				{
					switch(checkedId)
					{
						case R.id.radio0:
							// cente
							cente = true;
							angleFactor = 200;
						break;
						
						case R.id.radio1:
							// sexa
							cente = false;
							angleFactor = 180;
						break;
					}
				}
			});
			
		}
		
		@Override
		public void setMenuVisibility(boolean menuVisible) 
		{
			// TODO Auto-generated method stub
			super.setMenuVisibility(menuVisible);
			
			if(rootView != null)
			{
				if(menuVisible)
				{
					sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		    		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
				}
				else
				{
					sensorManager.unregisterListener(this);
				}
			}
		}
		
		@Override
		public void onResume() 
		{
			// TODO Auto-generated method stub
			super.onResume();
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
		}
    	
		@Override
		public void onPause() 
		{
			// TODO Auto-generated method stub
			super.onPause();
			sensorManager.unregisterListener(this);
		}


		@Override
		public void onSensorChanged(SensorEvent event) 
		{
//			// TODO Auto-generated method stub
			DecimalFormat gradesFormat = new DecimalFormat("0.0000");
			DecimalFormat climbFormat = new DecimalFormat("0.00");
			
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			{
				matrizGravity = event.values;
			}
			
			if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			{
				matrizGeomagnetic = event.values;
			}
			
			if(matrizGravity != null && matrizGeomagnetic != null)
			{
				float[] R = new float[9];
				float[] I = new float[9];
				boolean recojeDatos = SensorManager.getRotationMatrix(R, I, matrizGravity, matrizGeomagnetic);
				
				if(recojeDatos)
				{
					float angles[] = new float[3];
					SensorManager.getOrientation(R, angles);
					float imageAcimut = angles[0]*180/3.14159f;
					float acimut = angles[0]*angleFactor/3.14159f;
					float verticalAngle = angles[1]*angleFactor/3.14159f;
					
					imgCompass.setRotation(-imageAcimut);

					lblInclination.setText(climbFormat.format(-verticalAngle*100/45) + "%");
					
					if(acimut < 0)
					{
						acimut = 2 * angleFactor + acimut;
					}

					verticalAngle = verticalAngle + angleFactor/2;
					
					if(cente)
					{
						lblAzimuth.setText(gradesFormat.format(acimut)+"g");
						lblVerticalAngle.setText(gradesFormat.format(verticalAngle)+"g");
					}
					else
					{
						lblAzimuth.setText(pasar_a_sexa(acimut));
						lblVerticalAngle.setText(pasar_a_sexa(verticalAngle));
					}
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
    	
		public String pasar_a_sexa (double angulo) //angulo -> angulo sexa con decimales
		{	
			DecimalFormat Decimal = new DecimalFormat("0.00");
			DecimalFormat NoDecimal = new DecimalFormat("00");
			
			String angulo_str = Double.toString(angulo);
			String grados= quita_puntos(angulo_str);
			double angulo_entero = Double.parseDouble(grados);
			
			//Decimales
			double parte_decimal = angulo - angulo_entero;
			double minutos = 0;
			minutos = parte_decimal * 60; //Minutos
			
			String minutos_str = Double.toString(minutos);
			String minutos_enteros = quita_puntos(minutos_str);
			double minutos_ent = Double.parseDouble(minutos_enteros);
			
			double parte_decimal_segundos = minutos - minutos_ent;
			double segundos = 0;
			segundos = parte_decimal_segundos * 60;
			
			String resultado = grados + "º " + NoDecimal.format(Math.abs(minutos_ent))+ "' " + Decimal.format(Math.abs(segundos))+"''";
			return resultado;
		}
		
		public String quita_puntos(String numero)
		{
			String angulo_str = numero;
			String lector = "";
			String entero = "";
			int caracteres = angulo_str.length();
			
			for (int i = 0; i < caracteres; i++)
			{
				lector = angulo_str.substring(i, i+1);
				if (lector.contentEquals(".") || lector.contentEquals(","))
				{
					i = caracteres;
				}
				else
				{
					entero = entero + lector;
				}
			}
			
			return entero;
		}
    }
    
    public static class PlaceholderFragment extends Fragment 
    {
        BeanCatalog[] datos;
        
        //----- Elements
        Activity arrayAdapterActivity;
        ListView lstCatalog;


        public static PlaceholderFragment newInstance() {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        	arrayAdapterActivity = getActivity();
            lstCatalog = (ListView) rootView.findViewById(R.id.listView1);
		            
		            
            //----- Cargamos listado de aplicaciones
            String baseDatos = getResources().getString(R.string.base_datos);
            String baseDatos_des = getResources().getString(R.string.base_datos_des);
            String geodesia = getResources().getString(R.string.geodesia);
            String geodesia_des = getResources().getString(R.string.geodesia_des);
            String asistente = getResources().getString(R.string.asistente);
            String asistente_des = getResources().getString(R.string.asistente_des);
            String nivelaciones = getResources().getString(R.string.nivelaciones);
            String nivelaciones_des = getResources().getString(R.string.nivelaciones_des);
            String informes = getResources().getString(R.string.informes);
            String informes_des = getResources().getString(R.string.informes_des);
            String resenas = getResources().getString(R.string.resenas);
            String resenas_des = getResources().getString(R.string.resenas_des);
            String levantamientos = getResources().getString(R.string.levantamientos);
            String levantamientos_des = getResources().getString(R.string.levantamientos_des);
            String datum = getResources().getString(R.string.datum);
            String datum_des = getResources().getString(R.string.datum_des);
            
            datos = new BeanCatalog[]
            		{
            			new BeanCatalog(levantamientos, levantamientos_des, R.drawable.ic_levantamientos),
            			new BeanCatalog(datum, datum_des, R.drawable.ic_datum),
            			new BeanCatalog(baseDatos, baseDatos_des, R.drawable.icono),
            			new BeanCatalog(geodesia, geodesia_des, R.drawable.geodesia),
            			new BeanCatalog(asistente, asistente_des, R.drawable.asistente),
            			new BeanCatalog(nivelaciones, nivelaciones_des, R.drawable.nivelaciones),
            			new BeanCatalog(informes, informes_des, R.drawable.informes),
            			new BeanCatalog(resenas, resenas_des, R.drawable.resenas)
            		};
            
            CustomArrayAdapter adapter = new CustomArrayAdapter(arrayAdapterActivity);
            lstCatalog.setAdapter(adapter);


            return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) 
        {
        	// TODO Auto-generated method stub
        	super.onActivityCreated(savedInstanceState);

        	
        	//----- Variables initializing
        	final String buy =  getResources().getString(R.string.copmrar_app);
        	final String msgBuy = getResources().getString(R.string.msg_comprar_app);
        	final String googlePlay = getResources().getString(R.string.google_play);
        	final String geosoftware = getResources().getString(R.string.geosoftware);

        	lstCatalog.setOnItemClickListener(new OnItemClickListener() 
        	{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
				{
					final int pos = position;
					AlertDialog.Builder builder = new AlertDialog.Builder(arrayAdapterActivity);
					builder.setTitle(buy);
					builder.setMessage(msgBuy);

					builder.setPositiveButton(googlePlay, new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							abrirPaginaGoogle(pos);
						}
					});
					
					builder.setNegativeButton(geosoftware, new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							abrirPaginaPayPal(pos);
						}
					});
					
					builder.show();
				}
			});

        }
        
        
        //----- CLASSES, METHODS AND FUNCTIONS -----------------------------------------------
        private void abrirPaginaGoogle(int opcion)
        {
        	Intent i = new Intent(Intent.ACTION_VIEW);
        	String ruta = "";
        	
    		switch (opcion)
    		{
    			case 0:
    				ruta = "https://play.google.com/store/apps/details?id=com.victor.levantamientostopograficos";
    			break;
    			
    			case 1:
    				ruta = "https://play.google.com/store/apps/details?id=com.victor.datum";
    			break;
    			
    			case 2:
    				ruta = "https://play.google.com/store/apps/details?id=com.victor.basededatos";
    			break;
    			
    			case 3:
    				ruta = "https://play.google.com/store/apps/details?id=com.victor.geodesia";
    			break;
    			
    			case 4:
    				ruta = "https://play.google.com/store/apps/details?id=com.topografia.asistentedereplanteo";
    			break;
    			
    			case 5:
    				ruta = "https://play.google.com/store/apps/details?id=com.topografia.nivelaciones";
    			break;
    			
    			case 6:
    				ruta = "https://play.google.com/store/apps/details?id=com.victor.catalogadordevinos";
    			break;
    			
    			case 7:
    				ruta = "https://play.google.com/store/apps/details?id=com.victor.resenas";
    			break;
    		}
    		
        	i.setData(Uri.parse(ruta));
        	startActivity(i);
        }
        
        private void abrirPaginaPayPal(int opcion)
        {
        	Intent i = new Intent(Intent.ACTION_VIEW);
        	String ruta = "";
        	
    		switch (opcion)
    		{
    			case 0:
    				ruta = "http://www.geosoftware.es/es/home/16-levantamientos-topograficos.html";
    			break;
    			
    			case 1:
    				ruta = "http://www.geosoftware.es/es/home/15-datum-conversor-de-coordenadas.html";
    			break;
    		
    			case 2:
    				ruta = "http://www.geosoftware.es/es/home/14-base-de-datos.html";
    			break;
    			
    			case 3:
    				ruta = "http://www.geosoftware.es/index.php?id_product=11&controller=product&id_lang=1";
    			break;
    			
    			case 4:
    				ruta = "http://www.geosoftware.es/index.php?id_product=13&controller=product&id_lang=1";
    			break;
    			
    			case 5:
    				ruta = "http://www.geosoftware.es/index.php?id_product=9&controller=product&id_lang=1";
    			break;
    			
    			case 6:
    				ruta = "http://www.geosoftware.es/index.php?id_product=12&controller=product&id_lang=1";
    			break;
    			
    			case 7:
    				ruta = "http://www.geosoftware.es/index.php?id_product=10&controller=product&id_lang=1";
    			break;
    		}
    		
        	i.setData(Uri.parse(ruta));
        	startActivity(i);
        }
        
        
        class CustomArrayAdapter extends ArrayAdapter<Object>
        {
        	Activity context;
        	
        	CustomArrayAdapter(Activity context)
        	{
        		super(context, R.layout.custom_catalog_row, datos);
        		this.context = context;
        	}
        	
        	public View getView(int position, View convertView, ViewGroup parent)
        	{
        		View item = convertView;
        		ViewHolder holder;
        		
        		if(item == null)
        		{	
        			LayoutInflater inflater = context.getLayoutInflater();
        			item = inflater.inflate(R.layout.custom_catalog_row, null);
        			
        			holder = new ViewHolder();
        			holder.title = (TextView)item.findViewById(R.id.textView1);
        			holder.description = (TextView)item.findViewById(R.id.textView2);
        			holder.imageView = (ImageView)item.findViewById(R.id.imageView1);
        			
        			item.setTag(holder);
        		}
        		else
        		{
        			holder = (ViewHolder) item.getTag();
        		}
        		
        		Bitmap icon = BitmapFactory.decodeResource(getResources(), datos[position].getImage());
        		
        		holder.title.setText(datos[position].getAplication());
        		holder.description.setText(datos[position].getDescription());
        		holder.imageView.setImageBitmap(icon);
        		
        		return item;
        	}
        }
        
        static class ViewHolder
        {
        	TextView title;
        	TextView description;
        	ImageView imageView;
        }
    }

}

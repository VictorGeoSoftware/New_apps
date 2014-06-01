package com.victor.accesorios;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.TooManyListenersException;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

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
        if (id == R.id.action_settings) {
            return true;
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
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
            }
            return null;
        }
    }

    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements SensorEventListener 
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    	//----- Variables
        private static final String ARG_SECTION_NUMBER = "section_number";
        BeanCatalog[] datos;
        Activity arrayAdapterActivity;
    		//----- Magnetometer and accelerometer
		float[] matrizGravity;
		float[] matrizGeomagnetic;
		
		boolean cente = true;
		float angleFactor = 200;
        
        
        //----- Elements
    	SensorManager sensorManager;
    	Sensor accelerometer;
    	Sensor magnetometer;
    	
    	View rootView;
    		//----- Catalog
        ListView lstCatalog;
        	//----- Compass
        ImageView imgCompass;
        TextView lblAzimuth;
        TextView lblVerticalAngle;
        TextView lblInclination;
        RadioGroup radioGroup;


        
        
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
    		sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
    		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    		
        	int section = getArguments().getInt(ARG_SECTION_NUMBER);
        	arrayAdapterActivity = getActivity();
            
            
        	switch (section)
        	{
				case 1:
					Log.i("onCreateView", "actividad 1 creada");
					rootView = inflater.inflate(R.layout.fragment_main, container, false);
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
				break;
				
				case 2:
					rootView = inflater.inflate(R.layout.fragment_compass, container, false);
					imgCompass = (ImageView) rootView.findViewById(R.id.imageView1);
					radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
					lblAzimuth = (TextView) rootView.findViewById(R.id.textView4);
					lblVerticalAngle = (TextView) rootView.findViewById(R.id.textView5);
					lblInclination = (TextView) rootView.findViewById(R.id.textView6);

				break;
				
				case 3:
					rootView = inflater.inflate(R.layout.fragment_burble_level, container, false);
					
				break;

				default:
					rootView = inflater.inflate(R.layout.fragment_main, container, false);
				break;
			}

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

        	int section = getArguments().getInt(ARG_SECTION_NUMBER);
        	
        	switch(section)
        	{
	        	case 1:
	            	//----- Catalog fragment elements
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
	        	break;
	        	
	        	case 2:
	        		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
	        		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	        		
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
	        	break;
	        	
	        	case 3:
	        		
	        	break;
	        	
	        	default:
	        		
	        	break;
        	}
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


		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
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
//					float rollAngle = angles[2]*200/3.14159f;
					
					imgCompass.setRotation(-imageAcimut);

					lblInclination.setText(climbFormat.format(-verticalAngle*100/45) + "%");
					
					if(acimut < 0)
					{
						acimut = 2*angleFactor + acimut;
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
					
//					lblInclination.setText(gradesFormat.format(rollAngle)+"g");
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

}

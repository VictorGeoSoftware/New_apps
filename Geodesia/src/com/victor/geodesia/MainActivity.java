package com.victor.geodesia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("DefaultLocale") public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    //----- Arrays for save added coordinates in each Fragment
    private static ArrayList<String> geoUtmContainerArray = new ArrayList<String>();
    private static ArrayList<String> utmGeoContainerArray = new ArrayList<String>();
    private static ArrayList<String> anamorphosisGeoContainerArray = new ArrayList<String>();
    private static ArrayList<String> anamorphosisUtmContainerArray = new ArrayList<String>();
    private static ArrayList<String> convergenceGeoContainerArray = new ArrayList<String>();
    private static ArrayList<String> convergenceUtmContainerArray = new ArrayList<String>();
    private static ArrayList<String> distRedUtmContainerArray = new ArrayList<String>();
    private static ArrayList<String> distUtmRedContainerArray = new ArrayList<String>();
    private static ArrayList<String> acimutDistanceContainerArray = new ArrayList<String>();
    private static String coordinatesComputeContainer = "";

    //----- Variables for control each fragment in DialogFragments
    private static int FRAGMENT_SELECTED = 0;
    	//----- Values to indicate which Fragment is operative
    private static int GEO_UTM_FRAGMENT = 1;
    private static int UTM_GEO_FRAGMENT = 2;
    private static int ANAMORPHOSIS_GEO_FRAGMENT = 3;
    private static int ANAMORPHOSIS_UTM_FRAGMENT = 4;
    private static int CONVERGENCE_GEO_FRAGMENT = 5;
    private static int CONVERGENCE_UTM_FRAGMENT = 6;
    private static int DISTANCE_GEO_FRAGMENT = 7;
    private static int DISTANCE_UTM_FRAGMENT = 8;
    private static int AZIMUT_DISTANCE_FRAGMENT = 9;
    private static int COORDINATE_COMPUTE_FRAGMENT = 10;
    
    
    //----- Strings for keep calculus report of each fragment
    String reportContent = "";
    private static String reportGeoUtm = "";
    private static String reportUtmGeo = "";
	private static String reportAnamGeo = "";
    private static String reportAnamUtm = "";
    private static String reportConverGeo = "";
    private static String reportConverUtm = "";
    private static String reportDistGeo = "";
    private static String reportDistUtm = "";
    private static String reportAcimutDistance = "";
    private static String reportCoordinateCompute = "";
    
	public static String getReportGeoUtm() {
		return reportGeoUtm;
	}

	public static void setReportGeoUtm(String reportGeoUtm) {
		MainActivity.reportGeoUtm = reportGeoUtm;
	}
	
	public static String getReportUtmGeo() {
		return reportUtmGeo;
	}

	public static void setReportUtmGeo(String reportUtmGeo) {
		MainActivity.reportUtmGeo = reportUtmGeo;
	}
	
    public static String getReportAnamGeo() {
		return reportAnamGeo;
	}

	public static void setReportAnamGeo(String reportAnamGeo) {
		MainActivity.reportAnamGeo = reportAnamGeo;
	}

	public static String getReportAnamUtm() {
		return reportAnamUtm;
	}

	public static void setReportAnamUtm(String reportAnamUtm) {
		MainActivity.reportAnamUtm = reportAnamUtm;
	}

	public static String getReportConverGeo() {
		return reportConverGeo;
	}

	public static void setReportConverGeo(String reportConverGeo) {
		MainActivity.reportConverGeo = reportConverGeo;
	}

	public static String getReportConverUtm() {
		return reportConverUtm;
	}

	public static void setReportConverUtm(String reportConverUtm) {
		MainActivity.reportConverUtm = reportConverUtm;
	}

	public static String getReportDistGeo() {
		return reportDistGeo;
	}

	public static void setReportDistGeo(String reportDistGeo) {
		MainActivity.reportDistGeo = reportDistGeo;
	}

	public static String getReportDistUtm() {
		return reportDistUtm;
	}

	public static void setReportDistUtm(String reportDistUtm) {
		MainActivity.reportDistUtm = reportDistUtm;
	}    
    
    public static String getReportAcimutDistance() {
		return reportAcimutDistance;
	}

	public static void setReportAcimutDistance(String reportAcimutDistance) {
		MainActivity.reportAcimutDistance = reportAcimutDistance;
	}

	public static String getReportCoordinateCompute() {
		return reportCoordinateCompute;
	}

	public static void setReportCoordinateCompute(String reportCoordinateCompute) {
		MainActivity.reportCoordinateCompute = reportCoordinateCompute;
	}


	//----- DATABASES
	ContentValues newEllipsoid = new ContentValues();
	static SQLiteDatabase db;
	
	ContentValues newAnamorphosis = new ContentValues();
	static SQLiteDatabase anaDb;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        
        //----- Initializing Ellipsoids Data Base
        EllipsoidsDataBase dbEllipsoids = new EllipsoidsDataBase(getApplicationContext(), "Ellipsoids", null, 1);
        db = dbEllipsoids.getWritableDatabase();
        
        	//----- Input Hayford and ETRS89 by default.
        Cursor c = db.query("Ellipsoids", null, null, null, null, null, null);
        
        if(c.moveToFirst()){

        } else{
        	ContentValues content = new ContentValues();
			
        	content.put("name", "Hayford_ED50");
			content.put("a", "6378388");
			content.put("f", "297");

			db.insert("Ellipsoids", null, content);
			
        	content.put("name", "GRS80");
			content.put("a", "6378137");
			content.put("f", "298.2572221");

			db.insert("Ellipsoids", null, content);
        }
        
        
        //----- Initializing Anamorphosis Data Base
        AnamorphosisDataBase dbAnamorphosis = new AnamorphosisDataBase(getApplicationContext(), "Anamorphosis", null, 1);
        anaDb = dbAnamorphosis.getWritableDatabase();
        
			//----- Input Spain anamorphosis constant by default
        Cursor cA = anaDb.query("Anamorphosis", null, null, null, null, null, null);
        
		if(cA.moveToFirst()){

        } else{
        	ContentValues content = new ContentValues();
			
        	content.put("name", "Default");
			content.put("k", "0.9996");

			anaDb.insert("Anamorphosis", null, content);
        }
        
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Log.i("onNavigationDrawerListener", "position: " + position);
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position)
        {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, PresentationFragment.newInstance()).commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, GeodeticUtmFragment.newInstance()).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, UtmGeodeticFragment.newInstance()).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, AnamorphosisGeodeticFragment.newInstance()).commit();
                break;
            case 4:
                fragmentManager.beginTransaction().replace(R.id.container, AnamorphosisUtmFragment.newInstance()).commit();
                break;
            case 5:
                fragmentManager.beginTransaction().replace(R.id.container, ConvergenceGeodeticFragment.newInstance()).commit();
                break;
            case 6:
            	fragmentManager.beginTransaction().replace(R.id.container, ConvergenceUtmFragment.newInstance()).commit();
            	break;
            case 7:
            	fragmentManager.beginTransaction().replace(R.id.container, DistanceGeodeticFragment.newInstance()).commit();
            	break;
            case 8:
                fragmentManager.beginTransaction().replace(R.id.container, DistanceUtmFragment.newInstance()).commit();
                break;
            case 9:
                fragmentManager.beginTransaction().replace(R.id.container, DistanceAcimuthFragment.newInstance()).commit();
                break;
            case 10:
                fragmentManager.beginTransaction().replace(R.id.container, CoordinatesComputingFragment.newInstance()).commit();
                break;
        }

        onSectionAttached(position);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.app_name);
                break;
            case 1:
            case 2:
                mTitle = getString(R.string.conversion_coordenadas);
                break;
            case 3:
            case 4:
                mTitle = getString(R.string.anamorfosis_lineal);
                break;
            case 5:
            case 6:
                mTitle = getString(R.string.convergencia);
                break;
            case 7:
            case 8:
                mTitle = getString(R.string.distancias);
                break;
            case 9:
            	mTitle = getString(R.string.acimut_distancia);
            break;
            case 10:
            	mTitle = getString(R.string.calculo_coordenadas);
            break;            
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) 
        {
			case R.id.new_ellipsoid:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				LayoutInflater inflater = this.getLayoutInflater();
				View view = inflater.inflate(R.layout.view_ellipsoid_database, null);
				builder.setView(view);
				
				final EditText edtName = (EditText) view.findViewById(R.id.editText3);
				final EditText edtSemiaxis = (EditText) view.findViewById(R.id.editText1);
				final EditText edtFlattening = (EditText) view.findViewById(R.id.editText2);
				Button btnCancel = (Button) view.findViewById(R.id.button2);
				Button btnSave = (Button) view.findViewById(R.id.button1);
				final ListView lstSavedEllipsoids = (ListView) view.findViewById(R.id.listView1);
				
				final AlertDialog dialog = builder.create();
				
				updateEllipsoidsList(lstSavedEllipsoids);
				
				btnCancel.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						refreshParentFragment();
					}
				});
				
				btnSave.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						String name = edtName.getText().toString().replace(" ", "_");
						String semiAxis = edtSemiaxis.getText().toString();
						String flattening = edtFlattening.getText().toString();
						
						if(name.contentEquals("") || semiAxis.contentEquals("") || flattening.contentEquals(""))
						{
							Toast.makeText(getApplicationContext(), getString(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
						}
						else
						{
							newEllipsoid.put("name", name);
							newEllipsoid.put("a", semiAxis);
							newEllipsoid.put("f", flattening);
							
							if(checkExtistingRegister(name + " " + semiAxis + " " + flattening)){
								Toast.makeText(getApplicationContext(), getString(R.string.valor_registrado), Toast.LENGTH_SHORT).show();
							}else{
								db.insert("Ellipsoids", null, newEllipsoid);
								refreshParentFragment();
								dialog.dismiss();
							}
						}
					}
				});
			
				lstSavedEllipsoids.setOnItemLongClickListener(new OnItemLongClickListener(){
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						String selectedEllipsoid[] = lstSavedEllipsoids.getItemAtPosition(position).toString().split(" ");
						db.execSQL("DELETE FROM Ellipsoids WHERE name=? AND a=? AND f=?", selectedEllipsoid);
						updateEllipsoidsList(lstSavedEllipsoids);
						
						return false;
					}
				});
				
				dialog.show();
			break;
			
			case R.id.new_anamorphosis:
				AlertDialog.Builder anaBuilder = new AlertDialog.Builder(this);
				LayoutInflater anaInflater = this.getLayoutInflater();
				View anaView = anaInflater.inflate(R.layout.view_anamorphosis_database, null);
				anaBuilder.setView(anaView);
				
				final EditText edtAnaName = (EditText) anaView.findViewById(R.id.editText1);
				final EditText edtAnaValue = (EditText) anaView.findViewById(R.id.editText2);
				Button btnAnaCancel = (Button) anaView.findViewById(R.id.button1);
				Button btnAnaSave = (Button) anaView.findViewById(R.id.button2);
				final ListView lstAnamorphosis = (ListView) anaView.findViewById(R.id.listView1); 
				
				final AlertDialog anaDialog = anaBuilder.create();
				
				updateAnamorphosisList(lstAnamorphosis);
				
				btnAnaCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						anaDialog.dismiss();
						refreshParentFragment();
					}
				});
				
				btnAnaSave.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String anaName = edtAnaName.getText().toString().replace(" ", "_");
						String anaValue = edtAnaValue.getText().toString();
						
						if(anaName.contentEquals("") || anaValue.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();							
						}else{
							if(checkExistingAnamorphosis(anaName + " " + anaValue)){
								Toast.makeText(getApplicationContext(), getString(R.string.valor_registrado), Toast.LENGTH_SHORT).show();
							}else{
								newAnamorphosis.put("name", anaName);
								newAnamorphosis.put("k", anaValue);
								
								anaDb.insert("Anamorphosis", null, newAnamorphosis);
								refreshParentFragment();
								anaDialog.dismiss();
							}
						}
					}
				});
				
				lstAnamorphosis.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						String selectedAnamorphosis[] = lstAnamorphosis.getItemAtPosition(position).toString().split(" ");
						anaDb.execSQL("DELETE FROM Anamorphosis WHERE name=? AND k=?", selectedAnamorphosis);
						updateAnamorphosisList(lstAnamorphosis);
						return false;
					}
				});
				
				anaDialog.show();
			break;
	
			case R.id.generate_report:
				AlertDialog.Builder infoBuilder = new AlertDialog.Builder(this);
				LayoutInflater infoInflater = this.getLayoutInflater();
				View infoView = infoInflater.inflate(R.layout.view_dialog_calculus_report, null);
				infoBuilder.setView(infoView);
				
				final TextView txtTitleDialog = (TextView) infoView.findViewById(R.id.textView1);
				final EditText edtInfoName = (EditText) infoView.findViewById(R.id.editText1); 
				final AlertDialog infoDialog = infoBuilder.create();
				Button btnInfoAccept = (Button) infoView.findViewById(R.id.button2);
				Button btnInfoCancel = (Button) infoView.findViewById(R.id.button1);
				
				txtTitleDialog.setText(mTitle);
				//----- Logic
				// 1.- Recuperar valor de array del que vengo
				int wichFragment =  getFRAGMENT_SELECTED();
				reportContent = "";
				
				// 2.- Mirar que la variable del informe correspondiente no esté vacía
				switch (wichFragment) {
					case 0:
						btnInfoAccept.setEnabled(false);
						Toast.makeText(getApplicationContext(), getString(R.string.ventana_no_informes), Toast.LENGTH_SHORT).show();
						break;
					case 1:
						if(reportGeoUtm.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportGeoUtm;
						}
						
						break;
					case 2:
						if(reportUtmGeo.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportUtmGeo;
						}
						break;
					case 3:
						if(reportAnamGeo.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportAnamGeo;
						}
						break;
					case 4:
						if(reportAnamUtm.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportAnamUtm;
						}
						break;
					case 5:
						if(reportConverGeo.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportConverGeo;
						}
						break;
					case 6:
						if(reportConverUtm.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportConverUtm;
						}
						break;
					case 7:
						if(reportDistGeo.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportDistGeo;
						}
						break;
					case 8:
						if(reportDistUtm.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportDistUtm;
						}
						break;
					case 9:
						if(reportAcimutDistance.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportAcimutDistance;
						}
						break;
					case 10:
						if(reportCoordinateCompute.contentEquals("")){
							Toast.makeText(getApplicationContext(), getString(R.string.no_calculos), Toast.LENGTH_SHORT).show();
							btnInfoAccept.setEnabled(false);
						} else{
							reportContent = reportCoordinateCompute;
						}
						break;
				}
				
				btnInfoCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						infoDialog.dismiss();
					}
				});
				
				btnInfoAccept.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String strInfoName = edtInfoName.getText().toString();
						if(!strInfoName.contentEquals("")){
							grabar(strInfoName, reportContent);
							infoDialog.dismiss();
						}
					}
				});
				
				infoDialog.show();
			break;
		}
        
		return false;
    }
    
    public void updateEllipsoidsList(ListView listView)
    {
    	String fields[] = new String[]{"name", "a", "f"};
    	Cursor c = db.query("Ellipsoids", fields, null, null, null, null, null);
    	
    	ArrayList<String> ellipsoidsListArray = new ArrayList<String>();
    	
    	if(c.moveToFirst()){
    		do{
    			String ellipsoidName = c.getString(0);
    			String a = c.getString(1);
    			String f = c.getString(2);
    			
    			ellipsoidsListArray.add(ellipsoidName + " " + a + " " + f);
    		}while(c.moveToNext());
    	}else{
			ellipsoidsListArray.clear();
		}
    	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_list_item_1, ellipsoidsListArray);
		listView.setAdapter(adapter);
    }
    
    public void updateAnamorphosisList(ListView listView){
    	String fields[] = new String[]{"name", "k"};
    	Cursor c = anaDb.query("Anamorphosis", fields, null, null, null, null, null);
    	
    	ArrayList<String> anamorphosisListArray = new ArrayList<String>();
    	
    	if(c.moveToFirst()){
    		do{
    			String anamoName = c.getString(0);
    			String value = c.getString(1);
    			
    			anamorphosisListArray.add(anamoName + " " + value);
    		}while(c.moveToNext());
    	}else{
    		anamorphosisListArray.clear();
    	}
    	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_list_item_1, anamorphosisListArray);
		listView.setAdapter(adapter);
    }
    
    public boolean checkExtistingRegister(String register){
    	boolean exist = false;
    	String fieldValues[] = register.split(" ");
    	String columns[] = new String[]{"name", "a", "f"};
    	Cursor c = db.query("Ellipsoids", columns, "name=? AND a=? AND f=?", fieldValues, null, null, null);
    	
    	if(c.moveToFirst()){
    		exist = true;
    	}
    	
    	return exist;
    }
    
    public boolean checkExistingAnamorphosis(String anamorphosis){
    	boolean exist = false;
    	String fieldValues[] = anamorphosis.split(" ");
    	String columns[] = new String[]{"name", "k"};
    	Cursor c = anaDb.query("Anamorphosis", columns, "name=? AND k=?", fieldValues, null, null, null);
    	
    	if(c.moveToFirst()){
    		exist = true;
    	}
    	
    	return exist;
    }
    
    public void refreshParentFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        int wichFragment = getFRAGMENT_SELECTED();
        
        switch (wichFragment) 
        {
			case 1:
				fm.beginTransaction().replace(R.id.container, GeodeticUtmFragment.newInstance()).commit();
			break;

			case 2:
				fm.beginTransaction().replace(R.id.container, UtmGeodeticFragment.newInstance()).commit();
			break;

            case 3:
            	fm.beginTransaction().replace(R.id.container, AnamorphosisGeodeticFragment.newInstance()).commit();
                break;
            case 4:
            	fm.beginTransaction().replace(R.id.container, AnamorphosisUtmFragment.newInstance()).commit();
                break;
            case 5:
            	fm.beginTransaction().replace(R.id.container, ConvergenceGeodeticFragment.newInstance()).commit();
                break;
            case 6:
            	fm.beginTransaction().replace(R.id.container, ConvergenceUtmFragment.newInstance()).commit();
            	break;
            case 7:
            	fm.beginTransaction().replace(R.id.container, DistanceGeodeticFragment.newInstance()).commit();
            	break;
            case 8:
            	fm.beginTransaction().replace(R.id.container, DistanceUtmFragment.newInstance()).commit();
                break;
            case 9:
            	fm.beginTransaction().replace(R.id.container, DistanceAcimuthFragment.newInstance()).commit();
                break;
            case 10:
            	fm.beginTransaction().replace(R.id.container, CoordinatesComputingFragment.newInstance()).commit();
                break;
			default:
				fm.beginTransaction().replace(R.id.container, PresentationFragment.newInstance()).commit();
			break;
		}
    }
    
    public void grabar (String nombre, String linea)
	{
    	String fichero = getResources().getString(R.string.fichero_generado);
		try
		{
			File tarjeta = Environment.getExternalStorageDirectory();
			File file = new File (tarjeta.getAbsolutePath(), nombre + ".txt");
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
			osw.write(linea + "\n");
			osw.flush();
			osw.close();
			Log.i("GRABAR", "archivo generado: " + file.toString());
			Toast.makeText(getApplicationContext(), fichero + ": " + file.getPath().toString(), Toast.LENGTH_LONG).show();
		}
		catch (Exception e) 
		{
			Toast.makeText(getApplicationContext(), "ERROR: " + e, Toast.LENGTH_LONG).show();
			Log.i("GRABAR", "peta: " + e);
		}
	}
    
    public void refreshEllipsoidsSpinner(Spinner spinner, Activity referenceActivity)
    {
    	String fields[] = new String[]{"name", "a", "f"};
    	Cursor c = db.query("Ellipsoids", fields, null, null, null, null, null);
    	
    	ArrayList<String> ellipsoidsListArray = new ArrayList<String>();
    	
    	if(c.moveToFirst()){
    		do{
    			String ellipsoidName = c.getString(0);
    			String a = c.getString(1);
    			String f = c.getString(2);
    			
    			ellipsoidsListArray.add(ellipsoidName + " " + a + " " + f);
    		}while(c.moveToNext());
    	}else{
			ellipsoidsListArray.clear();
		}
    	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(referenceActivity.getApplicationContext(), 
				android.R.layout.simple_spinner_item, ellipsoidsListArray);
		adapter.setDropDownViewResource(R.layout.view_ellipsoid_spinner_item);
		spinner.setAdapter(adapter);
    }
    
    public void refreshAnamorphosisSpinner(Spinner spinner, Activity referenceActivity)
    {
    	String fields[] = new String[]{"name", "k"};
    	Cursor c = anaDb.query("Anamorphosis", fields, null, null, null, null, null);
    	
    	ArrayList<String> anamorphosisListArray = new ArrayList<String>();
    	
    	if(c.moveToFirst()){
    		do{
    			String name = c.getString(0);
    			String k = c.getString(1);
    			
    			anamorphosisListArray.add(name + " " + k);
    		}while(c.moveToNext());
    	}else{
			anamorphosisListArray.clear();
		}
    	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(referenceActivity.getApplicationContext(), 
				android.R.layout.simple_spinner_item, anamorphosisListArray);
		adapter.setDropDownViewResource(R.layout.view_ellipsoid_spinner_item);
		spinner.setAdapter(adapter);
    }
    
    
    //------ GETTERS AND SETTERS
    public static int getFRAGMENT_SELECTED() {
		return FRAGMENT_SELECTED;
	}

	public static void setFRAGMENT_SELECTED(int fRAGMENT_SELECTED) {
		FRAGMENT_SELECTED = fRAGMENT_SELECTED;
	}    
    public static ArrayList<String> getGeoUtmContainerArray() {
		return geoUtmContainerArray;
	}

	public static void setGeoUtmContainerArray(
			ArrayList<String> geoUtmContainerArray) {
		MainActivity.geoUtmContainerArray = geoUtmContainerArray;
	}

	public static ArrayList<String> getUtmGeoContainerArray() {
		return utmGeoContainerArray;
	}

	public static void setUtmGeoContainerArray(
			ArrayList<String> utmGeoContainerArray) {
		MainActivity.utmGeoContainerArray = utmGeoContainerArray;
	}

	public static ArrayList<String> getAnamorphosisGeoContainerArray() {
		return anamorphosisGeoContainerArray;
	}

	public static void setAnamorphosisGeoContainerArray(
			ArrayList<String> anamorphosisGeoContainerArray) {
		MainActivity.anamorphosisGeoContainerArray = anamorphosisGeoContainerArray;
	}

	public static ArrayList<String> getAnamorphosisUtmContainerArray() {
		return anamorphosisUtmContainerArray;
	}

	public static void setAnamorphosisUtmContainerArray(
			ArrayList<String> anamorphosisUtmContainerArray) {
		MainActivity.anamorphosisUtmContainerArray = anamorphosisUtmContainerArray;
	}

	public static ArrayList<String> getConvergenceGeoContainerArray() {
		return convergenceGeoContainerArray;
	}

	public static void setConvergenceGeoContainerArray(
			ArrayList<String> convergenceGeoContainerArray) {
		MainActivity.convergenceGeoContainerArray = convergenceGeoContainerArray;
	}

	public static ArrayList<String> getConvergenceUtmContainerArray() {
		return convergenceUtmContainerArray;
	}

	public static void setConvergenceUtmContainerArray(
			ArrayList<String> convergenceUtmContainerArray) {
		MainActivity.convergenceUtmContainerArray = convergenceUtmContainerArray;
	}

	public static ArrayList<String> getDistRedUtmContainerArray() {
		return distRedUtmContainerArray;
	}

	public static void setDistRedUtmContainerArray(
			ArrayList<String> distRedUtmContainerArray) {
		MainActivity.distRedUtmContainerArray = distRedUtmContainerArray;
	}

	public static ArrayList<String> getDistUtmRedContainerArray() {
		return distUtmRedContainerArray;
	}

	public static void setDistUtmRedContainerArray(
			ArrayList<String> distUtmRedContainerArray) {
		MainActivity.distUtmRedContainerArray = distUtmRedContainerArray;
	}
	
	public static ArrayList<String> getAcimutDistanceContainerArray() {
		return acimutDistanceContainerArray;
	}

	public static void setAcimutDistanceContainerArray(
			ArrayList<String> acimutDistanceContainerArray) {
		MainActivity.acimutDistanceContainerArray = acimutDistanceContainerArray;
	}
	
	public static String getCoordinatesComputeContainer() {
		return coordinatesComputeContainer;
	}

	public static void setCoordinatesComputeContainer(
			String coordinatesComputeContainer) {
		MainActivity.coordinatesComputeContainer = coordinatesComputeContainer;
	}

//----- FRAGMENTS AND OTHER CLASSES -------------------------------------------------------------------------------
    /*
     *
     *  DIALOGS
     *
     */


	public static class DataBaseDialogFragment extends DialogFragment
    {
        public DataBaseDialogFragment(){}

        //----- ELEMENTS
        EditText edtPointData;
        ListView lstFoundedPoints;
        Button btnCancel;

        
        //----- VARIABLES
        //----- Table to read is defined
        Uri camposUri;
        //----- ContentResolver is initialized for access to data base
        ContentResolver cr;
        FoundPointsAdapter adapter;
        ArrayList<FoundPointsModel> arrayFoundPoints= new ArrayList<FoundPointsModel>();
        String hemisphere = "N";


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.view_dialog_data_base, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            edtPointData = (EditText) view.findViewById(R.id.editText1);
            lstFoundedPoints = (ListView) view.findViewById(R.id.listView1);
            btnCancel = (Button) view.findViewById(R.id.button1);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle arg0)
        {
            super.onActivityCreated(arg0);
            
            try
            {
                camposUri = Uri.parse("content://com.victor.geodesia/geodesia");
                cr = getActivity().getContentResolver();

                String[] campos = new String[] {"proyecto", "nombrePunto", "phi", "landa", "x", "y"};
                Cursor c = cr.query(camposUri, campos, null, null, null);

                int i = 0;

                if(c.moveToFirst())
                {
                    do
                    {
                        String project = c.getString(0);
                        String pointName = c.getString(1);
                        String phi = c.getString(2);
                        String landa = c.getString(3);
                        String x = c.getString(4);
                        String y = c.getString(5);

                        arrayFoundPoints.add(new FoundPointsModel(project, pointName, phi, landa, x, y));
                        i = i + 1;
                    }
                    while(c.moveToNext());

                    adapter = new FoundPointsAdapter(getActivity(), arrayFoundPoints);
                    lstFoundedPoints.setAdapter(adapter);

                    edtPointData.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            adapter.getFilter().filter(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                lstFoundedPoints.setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View selectedPointDialogView = inflater.inflate(R.layout.view_dialog_landa_mc, null);
                        builder.setView(selectedPointDialogView);
                        final int wichFragment = MainActivity.getFRAGMENT_SELECTED();

                        final EditText edtLandaMc = (EditText) selectedPointDialogView.findViewById(R.id.editText);
                        final RadioGroup rgHemisphere = (RadioGroup) selectedPointDialogView.findViewById(R.id.radioGroup1);
                        Button btnAccept = (Button) selectedPointDialogView.findViewById(R.id.button);
                        final AlertDialog selectedPointDialog = builder.create();
                        
                        rgHemisphere.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {
								switch (checkedId) {
									case R.id.radio0:
										hemisphere = "N";
										break;
									case R.id.radio1:
										hemisphere = "S";
										break;
								}
							}
						});

                        btnAccept.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View arg0) {
                                String pointLandaMc = edtLandaMc.getText().toString();
                                boolean isDouble = checkDoubleValue(pointLandaMc);

                                if(isDouble)
                                {
                                    String pointLatitude = arrayFoundPoints.get(i).getLatitude();
                                    String pointLongitude = arrayFoundPoints.get(i).getLongitude();
                                    String pointX = arrayFoundPoints.get(i).getX();
                                    String pointY = arrayFoundPoints.get(i).getY();

                                    String returnValue = "";

                                    // Select and commit parent fragment
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    
                                    switch (wichFragment) 
                                    {
										case 1:
											returnValue = formatPointForList(pointLatitude) + " " 
													+ formatPointForList(pointLongitude) + " " + pointLandaMc;
											geoUtmContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, GeodeticUtmFragment.newInstance()).commit();
										break;

										case 2:
											returnValue = pointX + " " + pointY + " " + pointLandaMc + " " + hemisphere;
											utmGeoContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, UtmGeodeticFragment.newInstance()).commit();
										break;
										
										case 3:
											returnValue = formatPointForList(pointLatitude) + " " 
													+ formatPointForList(pointLongitude) + " " + pointLandaMc;
											anamorphosisGeoContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, AnamorphosisGeodeticFragment.newInstance()).commit();
										break;
										
										case 4:
											returnValue = pointX + " " + pointY + " " + pointLandaMc + " " + hemisphere;
											anamorphosisUtmContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, AnamorphosisUtmFragment.newInstance()).commit();
										break;
										
										case 5:
											returnValue = formatPointForList(pointLatitude) + " " 
													+ formatPointForList(pointLongitude) + " " + pointLandaMc;
											convergenceGeoContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, ConvergenceGeodeticFragment.newInstance()).commit();
										break;
										
										case 6:
											returnValue = pointX + " " + pointY + " " + pointLandaMc + " " + hemisphere;
											convergenceUtmContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, ConvergenceUtmFragment.newInstance()).commit();
										break;
										
										case 9:
											returnValue = pointX + " " + pointY + " " + pointLandaMc + " " + hemisphere;
											acimutDistanceContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, DistanceAcimuthFragment.newInstance()).commit();
										break;
										
										case 10:
											returnValue = pointX + " " + pointY + " " + pointLandaMc + " " + hemisphere;
											setCoordinatesComputeContainer(returnValue);
											fm.beginTransaction().replace(R.id.container, CoordinatesComputingFragment.newInstance()).commit();
										break;
									}
                                    
                                    selectedPointDialog.dismiss();
                                    dismiss();
                                }
                            }
                        });

                        selectedPointDialog.show();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
            catch (Exception e)
            {
                FoundPointsModel nodbObject[] = new FoundPointsModel[1];
                nodbObject[0] = new FoundPointsModel("", getString(R.string.atencion), getString(R.string.no_base_datos), "", "", "");
                arrayFoundPoints.add(nodbObject[0]);
                adapter = new FoundPointsAdapter(getActivity(), arrayFoundPoints);
                lstFoundedPoints.setAdapter(adapter);

                edtPointData.setEnabled(false);
                btnCancel.setText(getString(R.string.comprar));
                btnCancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String ruta = "http://www.geosoftware.es";
                        i.setData(Uri.parse(ruta));
                        startActivity(i);
                        dismiss();
                    }
                });
            }
        }
        

        public boolean checkDoubleValue(String value)
        {
            try
            {
                Double.parseDouble(value);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }
        
        public String formatPointForList(String point)
        {
        	String formattedPoint = "";
        	String pointPrepared = point.replace("º", " ").replace("'", " ");
        	String arrayDataPoint[] = pointPrepared.split(" ");

        	formattedPoint = arrayDataPoint[0] + " " + arrayDataPoint[1] + " " + arrayDataPoint[2];
        	
        	return formattedPoint;
        }

        private class FoundPointsAdapter extends ArrayAdapter<FoundPointsModel>
        {
            Activity contextActivity;

            private ArrayList<FoundPointsModel> originalFileList;
            private ArrayList<FoundPointsModel> fileList;
            private DataPointsFilter filter;

            public FoundPointsAdapter(Activity context, ArrayList<FoundPointsModel> fileList)
            {
                super(context, R.layout.adapter_listview_found_points, fileList);
                this.contextActivity = context;

                this.fileList = new ArrayList<FoundPointsModel>();
                this.fileList.addAll(fileList);
                this.originalFileList = new ArrayList<FoundPointsModel>();
                this.originalFileList.addAll(fileList);
            }

            @Override
            public Filter getFilter()
            {
                if(filter == null)
                {
                    filter = new DataPointsFilter();
                }

                return filter;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                ViewHolder holder = null;

                if(convertView == null)
                {
                    LayoutInflater inflater = contextActivity.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.adapter_listview_found_points, null);

                    holder = new ViewHolder();

                    holder.lblPointName = (TextView) convertView.findViewById(R.id.textView1);
                    holder.lblProject = (TextView) convertView.findViewById(R.id.textView4);
                    holder.lblLatitude = (TextView) convertView.findViewById(R.id.textView2);
                    holder.lblLongitude = (TextView) convertView.findViewById(R.id.textView3);
                    holder.lblX = (TextView) convertView.findViewById(R.id.textView5);
                    holder.lblY = (TextView) convertView.findViewById(R.id.textView6);

                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder) convertView.getTag();
                }

                FoundPointsModel point = fileList.get(position);
                holder.lblPointName.setText(point.getPointName());
                holder.lblProject.setText(point.getProject());
                holder.lblLatitude.setText(point.getLatitude());
                holder.lblLongitude.setText(point.getLongitude());
                holder.lblX.setText(point.getX() + " m");
                holder.lblY.setText(point.getY() + " m");

                return convertView;
            }

            private class DataPointsFilter extends Filter
            {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence)
                {
                    charSequence = charSequence.toString().toLowerCase();
                    FilterResults result = new FilterResults();

                    if(charSequence != null && charSequence.toString().length() > 0)
                    {
                        ArrayList<FoundPointsModel> filteredItems = new ArrayList<FoundPointsModel>();

                        for(int i = 0, l = originalFileList.size(); i < l; i++)
                        {
                            FoundPointsModel point = originalFileList.get(i);

                            if(point.getProject().toLowerCase().contains(charSequence) ||
                                    point.getPointName().toLowerCase().contains(charSequence))
                            {
                                filteredItems.add(point);
                            }
                        }

                        result.count = filteredItems.size();
                        result.values = filteredItems;
                    }
                    else
                    {
                        synchronized (this)
                        {
                            result.values = originalFileList;
                            result.count = originalFileList.size();
                        }
                    }

                    return result;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults)
                {
                    fileList = (ArrayList<FoundPointsModel>) filterResults.values;
                    notifyDataSetChanged();
                    clear();
                    Log.i("publishResults","Starting to publish: " + fileList);
                    for(int i = 0, l = fileList.size(); i < l; i++) {
                        add(fileList.get(i));
                    }

                    notifyDataSetInvalidated();
                }
            }

        }

        private class ViewHolder
        {
            TextView lblPointName;
            TextView lblProject;
            TextView lblLatitude;
            TextView lblLongitude;
            TextView lblX;
            TextView lblY;
        }
    }

	public static class DbDistancesDialogFragment extends DialogFragment
    {
        public DbDistancesDialogFragment(){}

        //----- ELEMENTS
        EditText edtPointData;
        ListView lstFoundedPoints;
        Button btnCancel;

        
        //----- VARIABLES
        //----- Table to read is defined
        Uri camposUri;
        //----- ContentResolver is initialized for access to data base
        ContentResolver cr;
        FoundPointsAdapter adapter;
        ArrayList<FoundPointsModel> arrayFoundPoints= new ArrayList<FoundPointsModel>();
        String hemisphere = "N";


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.view_dialog_data_base, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            edtPointData = (EditText) view.findViewById(R.id.editText1);
            lstFoundedPoints = (ListView) view.findViewById(R.id.listView1);
            btnCancel = (Button) view.findViewById(R.id.button1);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle arg0)
        {
            super.onActivityCreated(arg0);
            
            try
            {
                camposUri = Uri.parse("content://com.victor.geodesia/geodesia");
                cr = getActivity().getContentResolver();

                String[] campos = new String[] {"proyecto", "nombrePunto", "phi", "landa", "x", "y"};
                Cursor c = cr.query(camposUri, campos, null, null, null);

                int i = 0;

                if(c.moveToFirst())
                {
                    do
                    {
                        String project = c.getString(0);
                        String pointName = c.getString(1);
                        String phi = c.getString(2);
                        String landa = c.getString(3);
                        String x = c.getString(4);
                        String y = c.getString(5);

                        arrayFoundPoints.add(new FoundPointsModel(project, pointName, phi, landa, x, y));
                        i = i + 1;
                    }
                    while(c.moveToNext());

                    adapter = new FoundPointsAdapter(getActivity(), arrayFoundPoints);
                    lstFoundedPoints.setAdapter(adapter);

                    edtPointData.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            adapter.getFilter().filter(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                lstFoundedPoints.setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View selectedPointDialogView = inflater.inflate(R.layout.view_dialog_distance_landamc, null);
                        builder.setView(selectedPointDialogView);
                        final int wichFragment = MainActivity.getFRAGMENT_SELECTED();

                        final EditText edtLandaMc = (EditText) selectedPointDialogView.findViewById(R.id.editText);
                        final EditText edtDistance = (EditText) selectedPointDialogView.findViewById(R.id.editText1);
                        final EditText edtHeight = (EditText) selectedPointDialogView.findViewById(R.id.editText2);
                        final RadioGroup rgHemisphere = (RadioGroup) selectedPointDialogView.findViewById(R.id.radioGroup1);
                        Button btnAccept = (Button) selectedPointDialogView.findViewById(R.id.button);
                        final AlertDialog selectedPointDialog = builder.create();
                        
                        
                        rgHemisphere.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {
								switch (checkedId) {
									case R.id.radio0:
										hemisphere = "N";
										break;
									case R.id.radio1:
										hemisphere = "S";
										break;
								}
							}
						});

                        btnAccept.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View arg0) {
                                String pointLandaMc = edtLandaMc.getText().toString();
                                String strDistance = edtDistance.getText().toString();
                                String strHeight = edtHeight.getText().toString();
                                boolean landaIsDouble = checkDoubleValue(pointLandaMc);
                                boolean distanceIsDouble = checkDoubleValue(strDistance);
                                boolean heightIsDouble = checkDoubleValue(strHeight);

                                if(landaIsDouble && distanceIsDouble && heightIsDouble)
                                {
                                    String pointLatitude = arrayFoundPoints.get(i).getLatitude();
                                    String pointLongitude = arrayFoundPoints.get(i).getLongitude();
                                    String pointX = arrayFoundPoints.get(i).getX();
                                    String pointY = arrayFoundPoints.get(i).getY();

                                    String returnValue = "";

                                    // Select and commit parent fragment
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    
                                    switch (wichFragment) 
                                    {
										case 7:
											returnValue = formatPointForList(pointLatitude) + " " 
													+ formatPointForList(pointLongitude) + " " + pointLandaMc + " " +
													strDistance + " " + strHeight;
											distRedUtmContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, DistanceGeodeticFragment.newInstance()).commit();
										break;
										
										case 8:
											returnValue = pointX + " " + pointY + " " + pointLandaMc + " " + hemisphere
														+ " " + strDistance + " " + strHeight;
											distUtmRedContainerArray.add(returnValue);
											fm.beginTransaction().replace(R.id.container, DistanceUtmFragment.newInstance()).commit();
										break;
									}
                                    
                                    selectedPointDialog.dismiss();
                                    dismiss();
                                }
                            }
                        });

                        selectedPointDialog.show();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
            catch (Exception e)
            {
                FoundPointsModel nodbObject[] = new FoundPointsModel[1];
                nodbObject[0] = new FoundPointsModel("", getString(R.string.atencion), getString(R.string.no_base_datos), "", "", "");
                arrayFoundPoints.add(nodbObject[0]);
                adapter = new FoundPointsAdapter(getActivity(), arrayFoundPoints);
                lstFoundedPoints.setAdapter(adapter);

                edtPointData.setEnabled(false);
                btnCancel.setText(getString(R.string.comprar));
                btnCancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String ruta = "http://www.geosoftware.es";
                        i.setData(Uri.parse(ruta));
                        startActivity(i);
                        dismiss();
                    }
                });
            }
        }
        

        public boolean checkDoubleValue(String value)
        {
            try
            {
                Double.parseDouble(value);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }
        
        public String formatPointForList(String point)
        {
        	String formattedPoint = "";
        	String pointPrepared = point.replace("º", " ").replace("'", " ");
        	String arrayDataPoint[] = pointPrepared.split(" ");

        	formattedPoint = arrayDataPoint[0] + " " + arrayDataPoint[1] + " " + arrayDataPoint[2];
        	
        	return formattedPoint;
        }

        private class FoundPointsAdapter extends ArrayAdapter<FoundPointsModel>
        {
            Activity contextActivity;

            private ArrayList<FoundPointsModel> originalFileList;
            private ArrayList<FoundPointsModel> fileList;
            private DataPointsFilter filter;

            public FoundPointsAdapter(Activity context, ArrayList<FoundPointsModel> fileList)
            {
                super(context, R.layout.adapter_listview_found_points, fileList);
                this.contextActivity = context;

                this.fileList = new ArrayList<FoundPointsModel>();
                this.fileList.addAll(fileList);
                this.originalFileList = new ArrayList<FoundPointsModel>();
                this.originalFileList.addAll(fileList);
            }

            @Override
            public Filter getFilter()
            {
                if(filter == null)
                {
                    filter = new DataPointsFilter();
                }

                return filter;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                ViewHolder holder = null;

                if(convertView == null)
                {
                    LayoutInflater inflater = contextActivity.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.adapter_listview_found_points, null);

                    holder = new ViewHolder();

                    holder.lblPointName = (TextView) convertView.findViewById(R.id.textView1);
                    holder.lblProject = (TextView) convertView.findViewById(R.id.textView4);
                    holder.lblLatitude = (TextView) convertView.findViewById(R.id.textView2);
                    holder.lblLongitude = (TextView) convertView.findViewById(R.id.textView3);
                    holder.lblX = (TextView) convertView.findViewById(R.id.textView5);
                    holder.lblY = (TextView) convertView.findViewById(R.id.textView6);

                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder) convertView.getTag();
                }

                FoundPointsModel point = fileList.get(position);
                holder.lblPointName.setText(point.getPointName());
                holder.lblProject.setText(point.getProject());
                holder.lblLatitude.setText(point.getLatitude());
                holder.lblLongitude.setText(point.getLongitude());
                holder.lblX.setText(point.getX() + " m");
                holder.lblY.setText(point.getY() + " m");

                return convertView;
            }

            private class DataPointsFilter extends Filter
            {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence)
                {
                    charSequence = charSequence.toString().toLowerCase();
                    FilterResults result = new FilterResults();

                    if(charSequence != null && charSequence.toString().length() > 0)
                    {
                        ArrayList<FoundPointsModel> filteredItems = new ArrayList<FoundPointsModel>();

                        for(int i = 0, l = originalFileList.size(); i < l; i++)
                        {
                            FoundPointsModel point = originalFileList.get(i);

                            if(point.getProject().toLowerCase().contains(charSequence) ||
                                    point.getPointName().toLowerCase().contains(charSequence))
                            {
                                filteredItems.add(point);
                            }
                        }

                        result.count = filteredItems.size();
                        result.values = filteredItems;
                    }
                    else
                    {
                        synchronized (this)
                        {
                            result.values = originalFileList;
                            result.count = originalFileList.size();
                        }
                    }

                    return result;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults)
                {
                    fileList = (ArrayList<FoundPointsModel>) filterResults.values;
                    notifyDataSetChanged();
                    clear();
                    Log.i("publishResults","Starting to publish: " + fileList);
                    for(int i = 0, l = fileList.size(); i < l; i++) {
                        add(fileList.get(i));
                    }

                    notifyDataSetInvalidated();
                }
            }

        }

        private class ViewHolder
        {
            TextView lblPointName;
            TextView lblProject;
            TextView lblLatitude;
            TextView lblLongitude;
            TextView lblX;
            TextView lblY;
        }
    }
    
	
    /*
     * 
     * NAVIGATOR FRAGMENT
     * 
     */

    public static class NavigatorDialogFragment extends DialogFragment
    {
        public NavigatorDialogFragment(){}

        //----- Elements
        ListView lstFoldersFiles;
        Button btnCancel;
        Button btnBack;
        TextView txtCurrentRoot;


        //----- Variables
        private ArrayList<String> paths = null; // In this variable the different paths are saved 
        private String root="/";
        private File navigatorFile;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.view_file_navigator, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            lstFoldersFiles = (ListView) view.findViewById(R.id.listView1);
            btnCancel = (Button) view.findViewById(R.id.button1);
            btnBack = (Button) view.findViewById(R.id.button2);
            txtCurrentRoot = (TextView) view.findViewById(R.id.textView2);

            getDir(root);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle arg0)
        {
            // TODO Auto-generated method stub
            super.onActivityCreated(arg0);

            lstFoldersFiles.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                {
                    navigatorFile = new File(paths.get(arg2));

                    if(navigatorFile.isDirectory())
                    {
                        if(navigatorFile.canRead())
                        {
                            getDir(paths.get(arg2));
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getString(R.string.accion_no_permitida), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        String typeFile = MimeTypeMap.getFileExtensionFromUrl(navigatorFile.getAbsolutePath());

                        if(typeFile.contentEquals("txt"))
                        {
                            ArrayList<String> readData = readFile(navigatorFile.getAbsolutePath());
                            ArrayList<String> valuesToReturn = new ArrayList<String>();
                            int wichFragment = MainActivity.getFRAGMENT_SELECTED();
                            
                            int documentLenght = 0;
                            
                            switch (wichFragment) {
								case 1:
									documentLenght = 7;  // Geodetic coordinates list
								break;
								case 2:
									documentLenght = 4;  // UTM coordinates list
								break;
								case 3:
									documentLenght = 7;  // Geodetic coordinates list
								break;
								case 4:
									documentLenght = 4;  // UTM coordinates list
								break;
								case 5:
									documentLenght = 7;  // Geodetic coordinates list
								break;
								case 6:
									documentLenght = 4;  // UTM coordinates list
								break;
								case 7:
									documentLenght = 9;  // Geodetic coordinates list with distances
								break;
								case 8:
									documentLenght = 6;  // UTM coordinates list with distances
								break;
								case 9:
									documentLenght = 4;  // UTM coordinates list
								break;
								case 10:
									documentLenght = 4;  // UTM coordinates list
								break;
							}
                            

                            for(int i = 0; i < readData.size()-1; i++)
                            {
                            	String lineElements[] = readData.get(i).split(" ");
                            	
                            	if(lineElements.length == documentLenght)
                            	{
                            		for(int j = 0; j < lineElements.length; j++){
                            			if(documentLenght == 4 || documentLenght == 6){
	                            			try{
	                            				if(documentLenght == 4){
	                            					Double.parseDouble(lineElements[0]);
		                            				Double.parseDouble(lineElements[1]);
		                            				Double.parseDouble(lineElements[2]);	
	                            				} else{
	                            					Double.parseDouble(lineElements[0]);
		                            				Double.parseDouble(lineElements[1]);
		                            				Double.parseDouble(lineElements[2]);
		                            				Double.parseDouble(lineElements[4]);
		                            				Double.parseDouble(lineElements[5]);
	                            				}
	                            				
	                            				if(lineElements[3].contentEquals("N") || lineElements[3].contentEquals("S")){
	                            					if(j == documentLenght-1){
		                            					valuesToReturn.add(readData.get(i));
		                            				}
	                            				} else{
	                            					Toast.makeText(getActivity(), getString(R.string.hemisferio_incorrecto_linea) + " " + (i+1), Toast.LENGTH_SHORT).show();
	                            				}
	                            			}catch(Exception e){
	                            				Toast.makeText(getActivity(), getString(R.string.valores_incorrecto_linea) + " " + (i+1), Toast.LENGTH_SHORT).show();
	                            				dismiss();
	                            			}                            				
                            			}
                            			else{
	                            			try{
	                            				Double.parseDouble(lineElements[j]);
	                            				if(j == documentLenght-1){
	                            					valuesToReturn.add(readData.get(i));
	                            				}
	                            			}catch(Exception e){
	                            				Toast.makeText(getActivity(), getString(R.string.valores_incorrecto_linea) + " " + (i+1), Toast.LENGTH_SHORT).show();
	                            				dismiss();
	                            			}	
                            			}                            			
                            		}
                            	}
                            	else
                            	{
                            		Toast.makeText(getActivity(), getString(R.string.msg_valores_incorrectos) + " " + (i+1), Toast.LENGTH_SHORT).show();
                            		dismiss();
                            		break;
                            	}
                            }
                            
//                             Select and commit parent fragment
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            
                            switch (wichFragment) 
                            {
								case 1:
									geoUtmContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, GeodeticUtmFragment.newInstance()).commit();
								break;

								case 2:
									utmGeoContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, UtmGeodeticFragment.newInstance()).commit();
								break;
								
								case 3:
									anamorphosisGeoContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, AnamorphosisGeodeticFragment.newInstance()).commit();									
								break;
								
								case 4:
									anamorphosisUtmContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, AnamorphosisUtmFragment.newInstance()).commit();									
								break;
								
								case 5:
									convergenceGeoContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, ConvergenceGeodeticFragment.newInstance()).commit();
								break;
								
								case 6:
									convergenceUtmContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, ConvergenceUtmFragment.newInstance()).commit();								
								break;
								
								case 7:
									distRedUtmContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, DistanceGeodeticFragment.newInstance()).commit();
								break;
								
								case 8:
									distUtmRedContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, DistanceUtmFragment.newInstance()).commit();									
								break;
								
								case 9:
									acimutDistanceContainerArray.addAll(valuesToReturn);
									fm.beginTransaction().replace(R.id.container, DistanceAcimuthFragment.newInstance()).commit();									
								break;
							}
                            
                            dismiss();
                        }
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dismiss();
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(!navigatorFile.getAbsolutePath().toString().contentEquals(root))
                    {
                        navigatorFile = new File(navigatorFile.getParent());
                        getDir(navigatorFile.getPath());
                    }
                }
            });
        }

        private void getDir(String dirPath)
        {
            txtCurrentRoot.setText(getString(R.string.ruta_actual) + ": " + dirPath);

            paths = new ArrayList<String>();
            navigatorFile = new File(dirPath);
            File files[] = navigatorFile.listFiles();
            NavigatorModel[] items = new NavigatorModel[files.length];

            for(int i = 0; i < files.length; i++)
            {
                File file = files[i];
                paths.add(file.getPath());

                items[i] = new NavigatorModel(file.isDirectory(), file.getAbsolutePath());
            }

            NavigatorAdapter adapter = new NavigatorAdapter(getActivity(), items);
            lstFoldersFiles.setAdapter(adapter);
        }

        private ArrayList<String> readFile (String nombre)
        {
            File file = new File(nombre);
            ArrayList<String> lines = new ArrayList<String>();

            try
            {
                FileInputStream fIn = new FileInputStream(file);
                InputStreamReader readerFile = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(readerFile);
                String line = br.readLine();
                lines.add(line);

                while (line != null)
                {
                    line  = br.readLine();
                    lines.add(line);
                }

                br.close();
                readerFile.close();
                return lines;
            }
            catch (Exception e)
            {
                e.getStackTrace();
                return null;
            }
        }


        private class NavigatorAdapter extends ArrayAdapter<NavigatorModel>
        {
            Activity contextActivity;
            NavigatorModel[] fileList;

            public NavigatorAdapter(Activity context, NavigatorModel[] fileList)
            {
                super(context, R.layout.adapter_view_file_navigator, fileList);
                this.contextActivity = context;
                this.fileList = fileList;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                LayoutInflater inflater = contextActivity.getLayoutInflater();
                View item = inflater.inflate(R.layout.adapter_view_file_navigator, null);

                TextView lblTittle = (TextView) item.findViewById(R.id.textView1);
                TextView lblSubTitle = (TextView) item.findViewById(R.id.textView2);
                ImageView imageView = (ImageView)item.findViewById(R.id.imageView1);

                boolean isDirectory = fileList[position].isDirectory();
                String filePath = fileList[position].getName();
                File file = new File(filePath);

                String nameItem = file.getName();
                String typeItem = MimeTypeMap.getFileExtensionFromUrl(filePath);

                lblTittle.setText(nameItem.toUpperCase());
                if(isDirectory)
                {
                    imageView.setImageResource(R.drawable.ic_carpeta);
                    lblSubTitle.setText(getString(R.string.carpeta));
                }
                else
                {
                    if(typeItem.contentEquals("txt"))
                    {
                        imageView.setImageResource(R.drawable.ic_archivo);
                        lblSubTitle.setText(getString(R.string.archivo_texto));
                    }
                    else
                    {
                        imageView.setImageResource(R.drawable.ic_desconocido);
                        lblSubTitle.setText(getString(R.string.archivo_desconocido));
                    }
                }


                return item;
            }
        }
    }

    public static class ComputeCoordinatesNavigatorDialogFragment extends DialogFragment
    {
        public ComputeCoordinatesNavigatorDialogFragment(){}

        //----- Elements
        ListView lstFoldersFiles;
        ListView lstFoundedPoints;
        Button btnCancel;
        Button btnBack;
        TextView txtCurrentRoot;


        //----- Variables
        private ArrayList<String> paths = null; // In this variable the different paths are saved 
        private ArrayList<String> foundedPointsArray = new ArrayList<String>();
        private String root="/";
        private File navigatorFile;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.view_comp_coord_file_navigator, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            lstFoldersFiles = (ListView) view.findViewById(R.id.listView1);
            lstFoundedPoints = (ListView) view.findViewById(R.id.listView2);
            btnCancel = (Button) view.findViewById(R.id.button1);
            btnBack = (Button) view.findViewById(R.id.button2);
            txtCurrentRoot = (TextView) view.findViewById(R.id.textView2);

            getDir(root);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle arg0)
        {
            // TODO Auto-generated method stub
            super.onActivityCreated(arg0);

            lstFoldersFiles.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                {
                    navigatorFile = new File(paths.get(arg2));

                    if(navigatorFile.isDirectory())
                    {
                        if(navigatorFile.canRead())
                        {
                            getDir(paths.get(arg2));
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getString(R.string.accion_no_permitida), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        String typeFile = MimeTypeMap.getFileExtensionFromUrl(navigatorFile.getAbsolutePath());

                        if(typeFile.contentEquals("txt"))
                        {
                            ArrayList<String> readData = readFile(navigatorFile.getAbsolutePath());
                            int documentLenght = 4;

                            for(int i = 0; i < readData.size()-1; i++)
                            {
                            	String lineElements[] = readData.get(i).split(" ");
                            	
                            	if(lineElements.length == documentLenght)
                            	{
                            		for(int j = 0; j < lineElements.length; j++)
                            		{
                            			try
                            			{
                        					Double.parseDouble(lineElements[0]);
                            				Double.parseDouble(lineElements[1]);
                            				Double.parseDouble(lineElements[2]);

                            				if(lineElements[3].contentEquals("N") || lineElements[3].contentEquals("S")){
                            					if(j == documentLenght-1){
                            						foundedPointsArray.add(readData.get(i));
	                            				}
                            				} else{
                            					Toast.makeText(getActivity(), getString(R.string.hemisferio_incorrecto_linea) + " " + (i+1), Toast.LENGTH_SHORT).show();
                            				}
                            			}catch(Exception e){
                            				Toast.makeText(getActivity(), getString(R.string.valores_incorrecto_linea) + " " + (i+1), Toast.LENGTH_SHORT).show();
                            				dismiss();
                            			}                         			
                            		}
                            		
                            		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, foundedPointsArray);
                            		lstFoundedPoints.setAdapter(adapter);
                            	}
                            	else
                            	{
                            		Toast.makeText(getActivity(), getString(R.string.msg_valores_incorrectos) + " " + (i+1), Toast.LENGTH_SHORT).show();
                            		dismiss();
                            		break;
                            	}
                            }
                        }
                    }
                }
            });
            
            lstFoundedPoints.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String selectedItem = lstFoundedPoints.getItemAtPosition(position).toString();
	                setCoordinatesComputeContainer(selectedItem);
	                FragmentManager fm = getActivity().getSupportFragmentManager();
	                fm.beginTransaction().replace(R.id.container, CoordinatesComputingFragment.newInstance()).commit();
	
	                dismiss();
					
				}
			});

            btnCancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dismiss();
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(!navigatorFile.getAbsolutePath().toString().contentEquals(root))
                    {
                        navigatorFile = new File(navigatorFile.getParent());
                        getDir(navigatorFile.getPath());
                    }
                }
            });
        }

        private void getDir(String dirPath)
        {
            txtCurrentRoot.setText(getString(R.string.ruta_actual) + ": " + dirPath);

            paths = new ArrayList<String>();
            navigatorFile = new File(dirPath);
            File files[] = navigatorFile.listFiles();
            NavigatorModel[] items = new NavigatorModel[files.length];

            for(int i = 0; i < files.length; i++)
            {
                File file = files[i];
                paths.add(file.getPath());

                items[i] = new NavigatorModel(file.isDirectory(), file.getAbsolutePath());
            }

            NavigatorAdapter adapter = new NavigatorAdapter(getActivity(), items);
            lstFoldersFiles.setAdapter(adapter);
        }

        private ArrayList<String> readFile (String nombre)
        {
            File file = new File(nombre);
            ArrayList<String> lines = new ArrayList<String>();

            try
            {
                FileInputStream fIn = new FileInputStream(file);
                InputStreamReader readerFile = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(readerFile);
                String line = br.readLine();
                lines.add(line);

                while (line != null)
                {
                    line  = br.readLine();
                    lines.add(line);
                }

                br.close();
                readerFile.close();
                return lines;
            }
            catch (Exception e)
            {
                e.getStackTrace();
                return null;
            }
        }


        private class NavigatorAdapter extends ArrayAdapter<NavigatorModel>
        {
            Activity contextActivity;
            NavigatorModel[] fileList;

            public NavigatorAdapter(Activity context, NavigatorModel[] fileList)
            {
                super(context, R.layout.adapter_view_file_navigator, fileList);
                this.contextActivity = context;
                this.fileList = fileList;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                LayoutInflater inflater = contextActivity.getLayoutInflater();
                View item = inflater.inflate(R.layout.adapter_view_file_navigator, null);

                TextView lblTittle = (TextView) item.findViewById(R.id.textView1);
                TextView lblSubTitle = (TextView) item.findViewById(R.id.textView2);
                ImageView imageView = (ImageView)item.findViewById(R.id.imageView1);

                boolean isDirectory = fileList[position].isDirectory();
                String filePath = fileList[position].getName();
                File file = new File(filePath);

                String nameItem = file.getName();
                String typeItem = MimeTypeMap.getFileExtensionFromUrl(filePath);

                lblTittle.setText(nameItem.toUpperCase());
                if(isDirectory)
                {
                    imageView.setImageResource(R.drawable.ic_carpeta);
                    lblSubTitle.setText(getString(R.string.carpeta));
                }
                else
                {
                    if(typeItem.contentEquals("txt"))
                    {
                        imageView.setImageResource(R.drawable.ic_archivo);
                        lblSubTitle.setText(getString(R.string.archivo_texto));
                    }
                    else
                    {
                        imageView.setImageResource(R.drawable.ic_desconocido);
                        lblSubTitle.setText(getString(R.string.archivo_desconocido));
                    }
                }


                return item;
            }
        }
    }
    
    
    /**
     *
     * Main fragment
     *
     */

    public static class PresentationFragment extends Fragment
    {
        //----- View elements declaration
        Activity presentationActivity;
        View rootView;
        LinearLayout lyExamples;
        MainActivity object = new MainActivity();


        public static PresentationFragment newInstance()
        {
            PresentationFragment fragment = new PresentationFragment();
            return fragment;
        }

        public PresentationFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        	MainActivity.setFRAGMENT_SELECTED(0);
        	
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            lyExamples = (LinearLayout) rootView.findViewById(R.id.lyExamples);
            presentationActivity = getActivity();

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);

            lyExamples.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Animación para el layout
                    AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
                    animation.setDuration(1000);
                    animation.setFillAfter(true);
                    lyExamples.startAnimation(animation);

                    // Dialog con listview de opciones
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.txt_example_view, null);
                    builder.setView(layout);
                    final AlertDialog examplesDialog = builder.create();

                    final ListView lstExamples = (ListView) layout.findViewById(R.id.listView1);

                    final SideBarModel[] toolsList = new SideBarModel[]
                            {
                                    new SideBarModel(getString(R.string.conversion_coordenadas), getString(R.string.geodesicas_utm)),
                                    new SideBarModel(getString(R.string.conversion_coordenadas), getString(R.string.utm_geodesicas)),
                                    new SideBarModel(getString(R.string.anamorfosis_lineal), getString(R.string.coordenadas_geodesicas)),
                                    new SideBarModel(getString(R.string.anamorfosis_lineal), getString(R.string.coordenadas_utm)),
                                    new SideBarModel(getString(R.string.convergencia), getString(R.string.coordenadas_geodesicas)),
                                    new SideBarModel(getString(R.string.convergencia), getString(R.string.coordenadas_utm)),
                                    new SideBarModel(getString(R.string.distancias), getString(R.string.reducida_utm)),
                                    new SideBarModel(getString(R.string.distancias), getString(R.string.utm_reducida)),
                                    new SideBarModel(getString(R.string.acimut_distancia), getString(R.string.coordenadas_utm)),
                                    new SideBarModel(getString(R.string.calculo_coordenadas), getString(R.string.coordenadas_utm))
                            };
                    SideListAdapter adapter = new SideListAdapter(presentationActivity, toolsList);
                    lstExamples.setAdapter(adapter);

                    lstExamples.setOnItemClickListener(new OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                        {
                        	Log.i("presentation", "entra");
                        	int position = arg2 + 1;
                        	String fileName = getString(R.string.ejemplo) + "_" + position;
                        	String content = "";
                        	
                        	if(position < 7){
                        		if(position%2 == 0){
                        			content = "496347.299 4396688.075 -3 N" + "\n" +
                        							"600446.903 4735356.582 -9 N" + "\n" +
                        							"443642.136 4635655.192 3 N" + "\n" + 
                        							"279129.108 3111400.684 -15 N";
                        		} else{
                        			content = "39 43 12.21117 -3 2 33.42605 -3" + "\n" +
                        							"42 45 50.98128 -7 46 20.42868 -9" + "\n" +
                        							"41 52 14.90617 2 19 15.12492 3" + "\n" + 
                        							"28 6 35.03998 -17 14 54.06175 -15";
                        		}
                        	} else{
                        		if(position == 7){
                        			content = "39 43 12.21117 -3 2 33.42605 -3 4850 250" + "\n" +
                        							"42 45 50.98128 -7 46 20.42868 -9 1550 1060" + "\n" +
                        							"41 52 14.90617 2 19 15.12492 3 675 650" + "\n" + 
                        							"28 6 35.03998 -17 14 54.06175 -15 80 825";
                        		} 
                        		
                        		if(position == 8){
                        			content = "496347.299 4396688.075 -3 N 5000 825" + "\n" +
                        							"600446.903 4735356.582 -9 N 2350 250" + "\n" +
                        							"443642.136 4635655.192 3 N 1000 75" + "\n" + 
                        							"279129.108 3111400.684 -15 N 275 525";
                        		}
                        		
                        		if(position == 9 || position == 10){
                        			content = "496347.299 4396688.075 -3 N" + "\n" +
                							"600446.903 4735356.582 -9 N" + "\n" +
                							"443642.136 4635655.192 3 N" + "\n" + 
                							"279129.108 3111400.684 -15 N";
                        		}
                        	}
                        	
                        	grabar(fileName, content);
                            examplesDialog.dismiss();
                        }
                    });

                    examplesDialog.show();
                }
            });
        }
        
        public void grabar (String nombre, String linea)
    	{
        	String fichero = getResources().getString(R.string.fichero_generado);
    		try
    		{
    			File tarjeta = Environment.getExternalStorageDirectory();
    			File file = new File (tarjeta.getAbsolutePath(), nombre + ".txt");
    			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
    			osw.write(linea + "\n");
    			osw.flush();
    			osw.close();
    			Log.i("GRABAR", "archivo generado: " + file.toString());
    			Toast.makeText(getActivity(), fichero + ": " + file.getPath().toString(), Toast.LENGTH_LONG).show();
    		}
    		catch (Exception e) 
    		{
    			Toast.makeText(getActivity(), "ERROR: " + e, Toast.LENGTH_LONG).show();
    			Log.i("GRABAR", "peta: " + e);
    		}
    	}

        class SideListAdapter extends ArrayAdapter<SideBarModel>
        {
            Activity contextActivity;
            SideBarModel[] toolsList;

            public SideListAdapter(Activity context, SideBarModel[] toolsList)
            {
                super(context, R.layout.row_adapter_side_list, toolsList);
                this.contextActivity = context;
                this.toolsList = toolsList;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                LayoutInflater inflater = contextActivity.getLayoutInflater();
                View item = inflater.inflate(R.layout.row_adapter_side_list, null);

                TextView lblTittle = (TextView) item.findViewById(R.id.textView1);
                TextView lblSubTitle = (TextView) item.findViewById(R.id.textView2);

                String title = toolsList[position].getTitle();
                String subTitle = toolsList[position].getSubtTitle();

                lblTittle.setText(title);
                lblSubTitle.setText(subTitle);

                return item;
            }
        }
    }


    /**
     *
     * Geodetic to UTM conversor fragment
     *
     */

    public static class GeodeticUtmFragment extends Fragment
    {
        //----- View elements declaration
        View rootView;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;


        //----- Variables declaration
        ArrayList<String> addedPointsArrayList = new ArrayList<String>();
        ArrayList<String> reportCalcXArrayList = new ArrayList<String>();
        ArrayList<String> reportCalcYArrayList = new ArrayList<String>();
        DecimalFormat metersFormat = new DecimalFormat("0.000");
        MainActivity object = new MainActivity();
        Activity thisFragmentActivity;


        public static GeodeticUtmFragment newInstance()
        {
            GeodeticUtmFragment fragment = new GeodeticUtmFragment();
            return fragment;
        }

        public GeodeticUtmFragment(){}
        

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        	MainActivity.setFRAGMENT_SELECTED(GEO_UTM_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            addedPointsArrayList.addAll(getGeoUtmContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);
            }
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setGeoUtmContainerArray(addedPointsArrayList);
					geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setGeoUtmContainerArray(addedPointsArrayList);
                        	
                            DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                            dbFragment.show(getFragmentManager(), "DataBaseFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setGeoUtmContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            btnInputCoordinate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_geodetic_coordinate, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();

                    final EditText txtLatitudeGrades = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtLatitudeMinutes = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLatitudeSecconds = (EditText) layout.findViewById(R.id.editText3);

                    final EditText txtLongitudeGrades = (EditText) layout.findViewById(R.id.editText4);
                    final EditText txtLongitudeMinutes = (EditText) layout.findViewById(R.id.editText5);
                    final EditText txtLongitudeSecconds = (EditText) layout.findViewById(R.id.editText6);

                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);

                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);

                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strLatitudeGrades = txtLatitudeGrades.getText().toString();
                            String strLatitudeMinutes = txtLatitudeMinutes.getText().toString();
                            String strLatitudeSecconds = txtLatitudeSecconds.getText().toString();
                            String strLongitudeGrades = txtLongitudeGrades.getText().toString();
                            String strLongitudeMinutes = txtLongitudeMinutes.getText().toString();
                            String strLongitudeSecconds = txtLongitudeSecconds.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();

                            if(strLatitudeGrades.contentEquals("") || strLatitudeMinutes.contentEquals("") || strLatitudeSecconds.contentEquals("")
                                    || strLongitudeGrades.contentEquals("") || strLongitudeMinutes.contentEquals("") || strLongitudeSecconds.contentEquals("")
                                    || strLongitudeMc.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strLatitudeGrades + " " + strLatitudeMinutes + " " + strLatitudeSecconds + " "
                                        + strLongitudeGrades + " " + strLongitudeMinutes + " " + strLongitudeSecconds + " " + strLongitudeMc);
                                setGeoUtmContainerArray(addedPointsArrayList);
                                geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
                }
            });

            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setGeoUtmContainerArray(addedPointsArrayList);
                    geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                    calculatedCoordinatesListRefresh(addedPointsArrayList, lstCalculatedPoints);
                }
            });

            btnCalculate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
                	
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);
                	
                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		Funciones functions = new Funciones();
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		Log.i("calcular", "elementos: "+addedPointsArrayList.get(i));
                		
                		double phi = functions.recoje_puntos(values[0], values[1], values[2]);
                		double landa = functions.recoje_puntos(values[3], values[4], values[5]);
                		double landaMc = Double.parseDouble(values[6])*Math.PI/180;
                		
                		double xCalculated = functions.problema_directo_x(phi, landa, landaMc, a, f, ko);
                		double yCalculated = functions.problema_directo_y(phi, landa, landaMc, a, f, ko);
                		
                		calculatedCoordinates.add(metersFormat.format(xCalculated) + "m " + metersFormat.format(yCalculated) + "m");
                		
                		reportCalcXArrayList.add(Funciones.getCalculoProblemaDirectoX());
                		reportCalcYArrayList.add(Funciones.getCalculoProblemaDirectoY());
                	}
                	
                    calculatedCoordinatesListRefresh(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates);
                }
            });
        }
        
        
// ------ METHODS AND SUBROUTINES
        public void reportData(ArrayList<String> calculatedCoordinatesArrayList){
        	String title = getString(R.string.informe_calculo) + "\n";
        	String subTitle = getString(R.string.geodesicas_utm) + "\n" + "\n";
        	
        	String titleInputData = getString(R.string.datos_entrada) + "\n";
        	String titleCalculationProccess = "\n" + "\n" + getString(R.string.proceso_calculo) + "\n";
        	String titleResults = "\n" + "\n" + getString(R.string.resultados) + "\n";
        	
        	String inputData = "";
        	String calculationProccessX = "";
        	String calculationProccessY = "";
        	String calculatedCoordinates = "";
        	
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputData = inputData + addedPointsArrayList.get(i) + "\n";
        		calculationProccessX = calculationProccessX + reportCalcXArrayList.get(i) + "\n";
        		calculationProccessY = calculationProccessY + reportCalcYArrayList.get(i) + "\n";
        		calculatedCoordinates = calculatedCoordinates + calculatedCoordinatesArrayList.get(i) + "\n";
        	}
        	
        	setReportGeoUtm(title + subTitle + 
        			titleInputData + inputData +
        			titleCalculationProccess 
        				+ getString(R.string.coordenada_x) + "\n" + calculationProccessX + "\n"
        				+ getString(R.string.coordenada_y) + "\n" + calculationProccessY +
        			titleResults + calculatedCoordinates);
        }
        
        public void geodeticListRefresh(ArrayList<String> list, ListView listView)
        {
            ArrayList<String> formattedData = new ArrayList<String>();

            for(int i = 0; i <  list.size(); i++)
            {
                String[] data = list.get(i).split(" ");
                formattedData.add(data[0] + "º " + data[1] + "' " + data[2] + "''" + "  "
                        + data[3] + "º " + data[4] + "' " + data[5]+ "''" + "  "
                        + data[6] + "º");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedData);
            listView.setAdapter(adapter);
        }
        
        public void calculatedCoordinatesListRefresh(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            lstCalculatedPoints.setAdapter(adapter);
        }
    }


    /**
     *
     *
     * UTM to Geodetic conversor fragment
     *
     * **/

    public static class UtmGeodeticFragment extends Fragment
    {
        //----- View elements declaration
    	Activity thisFragmentActivity;
        View rootView;
        TextView txtToolTitle;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;
        
        
        //----- Variables declaration
        String hemisphere = "N";
        ArrayList<String> addedPointsArrayList =  new ArrayList<String>();
        ArrayList<String> reportCalcPhiArrayList = new ArrayList<String>();
        ArrayList<String> reportCalcLandaArrayList = new ArrayList<String>();
        MainActivity object = new MainActivity();
        
        

        public static UtmGeodeticFragment newInstance()
        {
            UtmGeodeticFragment fragment = new UtmGeodeticFragment();
            return fragment;
        }

        public UtmGeodeticFragment(){};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        	MainActivity.setFRAGMENT_SELECTED(UTM_GEO_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
            
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            txtToolTitle.setText(getString(R.string.utm_geodesicas));
            
            addedPointsArrayList.addAll(getUtmGeoContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
            }
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setUtmGeoContainerArray(addedPointsArrayList);
					refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});
            
            btnInputCoordinate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_utm_coordinate, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();
                    
                    final RadioGroup rgHemispheres = (RadioGroup) layout.findViewById(R.id.radioGroup1);
                    final EditText txtXcoordinate = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtYcoordinate = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);

                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);
                    
                    rgHemispheres.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							switch (checkedId) {
								case R.id.radio0:
									hemisphere = "N";
									break;
								case R.id.radio1:
									hemisphere = "S";
									break;
							}
						}
					});
                    
                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strXcoordinate = txtXcoordinate.getText().toString();
                            String strYcoordinate = txtYcoordinate.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();

                            if(strXcoordinate.contentEquals("") || strYcoordinate.contentEquals("") || strLongitudeMc.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strXcoordinate + " " + strYcoordinate + " " + strLongitudeMc + " " + hemisphere);
                                setUtmGeoContainerArray(addedPointsArrayList);
                                refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setUtmGeoContainerArray(addedPointsArrayList);
                        	
                            DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                            dbFragment.show(getFragmentManager(), "DataBaseFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setUtmGeoContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            
            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setUtmGeoContainerArray(addedPointsArrayList);
                    refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
                    refreshCoordinatesList(addedPointsArrayList, lstCalculatedPoints);
                }
            });
            
            btnCalculate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
    				double e = 2*(1/f) - Math.pow((1/f), 2);
    				Funciones functions = new Funciones();
    				
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);
                	
                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	ArrayList<String> pointIterantionsArray = new ArrayList<String>();
                	Log.i("calcular", "cantidad puntos: "+ addedPointsArrayList.size());
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		
                		double x = Double.parseDouble(values[0]);
                		double y = Double.parseDouble(values[1]);
                		double landaMcRad = Double.parseDouble(values[2])*Math.PI/180;
                		String hemisphere = values[3];
                		
                		double phiIterated = functions.problema_inverso_phi(a, e, f, x, y, 0.00001, ko, hemisphere);
                		double phiCalculated = functions.problema_inverso_phi_absoluta(a, e, f, x, y, phiIterated, ko);
                		String phiFormatted = functions.pasar_a_sexa(phiCalculated*180/Math.PI);
                		
                		double incrLandaRad = functions.problema_inverso_landa(a, e, f, x, phiIterated, ko);
                		double landaCalculated = (landaMcRad + incrLandaRad)*180/Math.PI;
                		String landaFormatted = functions.pasar_a_sexa(landaCalculated);
                		
                		calculatedCoordinates.add(phiFormatted + " " + landaFormatted);
                		
                		// Calculations report
                		reportCalcPhiArrayList.add(Funciones.getCalculoProblemaInversoPhi());
                		
                		String iterations = "";
                		ArrayList<String> iterantionsArray = Funciones.getIteracionProblemaInversoPhi();
                		
                		for(int j = 0; j < iterantionsArray.size(); j++){
                			iterations = iterations + iterantionsArray.get(j) + "\n";
                		}
                		pointIterantionsArray.add(iterations);
                		
                		reportCalcLandaArrayList.add(Funciones.getCalculoProblemaInversoLanda());
                	}
                	
                    refreshCoordinatesList(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates, pointIterantionsArray);
				}
			});
        }
        
        public void reportData(ArrayList<String> calculatedCoordinates, ArrayList<String> pointIterations){
        	// 1.- Title and subTitle
        	String title = getString(R.string.informe_calculo) + "\n" + getString(R.string.utm_geodesicas) + "\n" + "\n";
        	
        	// 2.- Input coordinates list
        	String titleInputCoordinates = getString(R.string.coordenadas_utm) + "\n";
        	String inputCoordinates = "";
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputCoordinates =  inputCoordinates + addedPointsArrayList.get(i) + "\n";
        	}
        	
        	// 3.- Calculus process
        	String calculusProcess = "";
        	String calculated = "";
        	for(int i = 0; i < calculatedCoordinates.size(); i++){        		
        		calculusProcess = calculusProcess + pointIterations.get(i) + "\n"
        						+ reportCalcPhiArrayList.get(i) + "\n" + reportCalcLandaArrayList.get(i) + "\n" + "\n" ;
        		
        		calculated = calculated + calculatedCoordinates.get(i) + "\n";
        	}
        	
        	String returnValue = title +
        						titleInputCoordinates +
        						inputCoordinates + "\n" + "\n" +
        						calculusProcess +
        						getString(R.string.coordenadas_calculadas) + "\n" +
        						calculated; 
        	
        	setReportUtmGeo(returnValue);
        }
        
        //----- METHODS AND SUBROUTINES
        public void refreshCoordinatesList(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        	listView.setAdapter(adapter);
        }
        
        public void refreshAddedCoordinatesList(ArrayList<String> arrayList, ListView listView){
        	ArrayList<String> formattedElementsArrayList = new ArrayList<String>();
        	for(int i = 0; i < arrayList.size(); i++){
        		String elements[] = arrayList.get(i).split(" ");
        		formattedElementsArrayList.add(elements[0] + "m " + elements[1] + "m " + elements[2] + "º " + elements[3]);
        	}
        	
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedElementsArrayList);
        	listView.setAdapter(adapter);
        }
    }

    
    /**
     * 
     * Lineal Anamorphosis with geodetic coordinates 
     * 
     */
    
    public static class AnamorphosisGeodeticFragment extends Fragment{
        //----- View elements declaration
        View rootView;
        TextView txtToolTitle;
        TextView txtToolSubTitle;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        TextView txtCalculatedValues;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;
        

        //----- Variables declaration
        ArrayList<String> addedPointsArrayList = new ArrayList<String>();
        ArrayList<String> reportCalcAnamofphosis = new ArrayList<String>();
        MainActivity object = new MainActivity();
        Activity thisFragmentActivity;
        Funciones functions = new Funciones();
        DecimalFormat format = new DecimalFormat("0.00000");
        
        
        public static AnamorphosisGeodeticFragment newInstance()
        {
        	AnamorphosisGeodeticFragment fragment = new AnamorphosisGeodeticFragment();
            return fragment;
        }

        public AnamorphosisGeodeticFragment(){};
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	MainActivity.setFRAGMENT_SELECTED(ANAMORPHOSIS_GEO_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
            txtToolSubTitle = (TextView) rootView.findViewById(R.id.textView1);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            txtCalculatedValues = (TextView) rootView.findViewById(R.id.textView4);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
            
        	return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            txtToolTitle.setText(getString(R.string.anamorfosis_lineal));
            txtToolSubTitle.setText(getString(R.string.coordenadas_geodesicas));
            txtCalculatedValues.setText(getString(R.string.valores_calculados) + " - " + getString(R.string.geodesicas_utm));
            
            addedPointsArrayList.addAll(getAnamorphosisGeoContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates); 
            }
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setAnamorphosisGeoContainerArray(addedPointsArrayList);
					geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setAnamorphosisGeoContainerArray(addedPointsArrayList);
                        	
                            DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                            dbFragment.show(getFragmentManager(), "DataBaseFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setAnamorphosisGeoContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            btnInputCoordinate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_geodetic_coordinate, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();

                    final EditText txtLatitudeGrades = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtLatitudeMinutes = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLatitudeSecconds = (EditText) layout.findViewById(R.id.editText3);

                    final EditText txtLongitudeGrades = (EditText) layout.findViewById(R.id.editText4);
                    final EditText txtLongitudeMinutes = (EditText) layout.findViewById(R.id.editText5);
                    final EditText txtLongitudeSecconds = (EditText) layout.findViewById(R.id.editText6);

                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);

                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);

                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strLatitudeGrades = txtLatitudeGrades.getText().toString();
                            String strLatitudeMinutes = txtLatitudeMinutes.getText().toString();
                            String strLatitudeSecconds = txtLatitudeSecconds.getText().toString();
                            String strLongitudeGrades = txtLongitudeGrades.getText().toString();
                            String strLongitudeMinutes = txtLongitudeMinutes.getText().toString();
                            String strLongitudeSecconds = txtLongitudeSecconds.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();

                            if(strLatitudeGrades.contentEquals("") || strLatitudeMinutes.contentEquals("") || strLatitudeSecconds.contentEquals("")
                                    || strLongitudeGrades.contentEquals("") || strLongitudeMinutes.contentEquals("") || strLongitudeSecconds.contentEquals("")
                                    || strLongitudeMc.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strLatitudeGrades + " " + strLatitudeMinutes + " " + strLatitudeSecconds + " "
                                        + strLongitudeGrades + " " + strLongitudeMinutes + " " + strLongitudeSecconds + " " + strLongitudeMc);
                                setAnamorphosisGeoContainerArray(addedPointsArrayList);
                                geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
                }
            });

            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setAnamorphosisGeoContainerArray(addedPointsArrayList);
                    geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                    calculatedCoordinatesListRefresh(addedPointsArrayList, lstCalculatedPoints);
                }
            });

            btnCalculate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
                	
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);

                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		Log.i("calcular", "elementos: "+addedPointsArrayList.get(i));
                		
                		double phi = functions.recoje_puntos(values[0], values[1], values[2]);
                		double landa = functions.recoje_puntos(values[3], values[4], values[5]);
                		double landaMc = Double.parseDouble(values[6])*Math.PI/180;
                		double incrLanda = landa - landaMc;
                		double anamorphosisPoint = functions.anamorfosisLinealGeodesicas(a, f, phi, incrLanda, ko);
                		
                		calculatedCoordinates.add(format.format(anamorphosisPoint));
                		
                		reportCalcAnamofphosis.add(Funciones.getCalculoAnamorfosisGeodesicas());
                	}
                	
                    calculatedCoordinatesListRefresh(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates);
                }
            });
        }
        
        
        //----- METHODS AND SUBROUTINES
        public void reportData(ArrayList<String> calculatedCoordinates){
        	// 1.- Title and subTitle
        	String title = getString(R.string.informe_calculo) + "\n" + getString(R.string.anamorfosis_lineal) + "\n" + "\n";
        	
        	// 2.- Input coordinates list
        	String titleInputCoordinates = getString(R.string.coordenadas_geodesicas) + "\n";
        	String inputCoordinates = "";
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputCoordinates =  inputCoordinates + addedPointsArrayList.get(i) + "\n";
        	}
        	
        	// 3.- Calculus process
        	String titleCalculusProcess = getString(R.string.proceso_calculo) + "\n";
        	String calculusProcess = "";
        	for(int i = 0; i < reportCalcAnamofphosis.size(); i++){
        		calculusProcess = calculusProcess + reportCalcAnamofphosis.get(i) + "\n";
        	}
        	
        	String titleResults = getString(R.string.resultados) + "\n";
        	String results = "";
        	for(int i = 0; i < calculatedCoordinates.size(); i++){
        		results = results + calculatedCoordinates.get(i) + "\n";
        	}
        	
        	String report = title + 
        					titleInputCoordinates + inputCoordinates + "\n" + "\n" +
        					titleCalculusProcess + calculusProcess + "\n" + "\n" +
        					titleResults + results;
        			
        	setReportAnamGeo(report);
        }
        
        
        public void geodeticListRefresh(ArrayList<String> list, ListView listView){
            ArrayList<String> formattedData = new ArrayList<String>();

            for(int i = 0; i <  list.size(); i++)
            {
                String[] data = list.get(i).split(" ");
                formattedData.add(data[0] + "º " + data[1] + "' " + data[2] + "''" + "  "
                        + data[3] + "º " + data[4] + "' " + data[5]+ "''" + "  "
                        + data[6] + "º");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedData);
            listView.setAdapter(adapter);
        }
        
        public void calculatedCoordinatesListRefresh(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            lstCalculatedPoints.setAdapter(adapter);
        }
    }
    
    
    /**
     * 
     * Lineal Anamorphosis with UTM coordinates
     * 
     */
    
    public static class AnamorphosisUtmFragment extends Fragment
    {
        //----- View elements declaration
    	Activity thisFragmentActivity;
        View rootView;
        TextView txtToolTitle;
        TextView txtToolSubTitle;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        TextView txtCalculatedValues;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;
        
        
        //----- Variables declaration
        String hemisphere = "N";
        ArrayList<String> addedPointsArrayList =  new ArrayList<String>();
        ArrayList<String> reportCalcAnamUtm = new ArrayList<String>();
        MainActivity object = new MainActivity();
        Funciones functions = new Funciones();
        DecimalFormat format = new DecimalFormat("0.00000");
        

        public static AnamorphosisUtmFragment newInstance()
        {
        	AnamorphosisUtmFragment fragment = new AnamorphosisUtmFragment();
            return fragment;
        }

        public AnamorphosisUtmFragment(){};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        	MainActivity.setFRAGMENT_SELECTED(ANAMORPHOSIS_UTM_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
            txtToolSubTitle = (TextView) rootView.findViewById(R.id.textView1);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            txtCalculatedValues = (TextView) rootView.findViewById(R.id.textView4);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
            
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            txtToolTitle.setText(getString(R.string.anamorfosis_lineal));
            txtToolSubTitle.setText(getString(R.string.coordenadas_utm));
            txtCalculatedValues.setText(getString(R.string.valores_calculados) + " - " + getString(R.string.utm_geodesicas));
            
            addedPointsArrayList.addAll(getAnamorphosisUtmContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
            }
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setAnamorphosisUtmContainerArray(addedPointsArrayList);
					refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});
            
            btnInputCoordinate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_utm_coordinate, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();
                    
                    final RadioGroup rgHemispheres = (RadioGroup) layout.findViewById(R.id.radioGroup1);
                    final EditText txtXcoordinate = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtYcoordinate = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);

                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);
                    
                    rgHemispheres.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							switch (checkedId) {
								case R.id.radio0:
									hemisphere = "N";
									break;
								case R.id.radio1:
									hemisphere = "S";
									break;
							}
						}
					});
                    
                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strXcoordinate = txtXcoordinate.getText().toString();
                            String strYcoordinate = txtYcoordinate.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();

                            if(strXcoordinate.contentEquals("") || strYcoordinate.contentEquals("") || strLongitudeMc.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strXcoordinate + " " + strYcoordinate + " " + strLongitudeMc + " " + hemisphere);
                                setAnamorphosisUtmContainerArray(addedPointsArrayList);
                                refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setAnamorphosisUtmContainerArray(addedPointsArrayList);
                        	
                            DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                            dbFragment.show(getFragmentManager(), "DataBaseFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setAnamorphosisUtmContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            
            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setAnamorphosisUtmContainerArray(addedPointsArrayList);
                    refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
                    refreshCoordinatesList(addedPointsArrayList, lstCalculatedPoints);
                }
            });
            
            btnCalculate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
    				double e = 2*(1/f) - Math.pow((1/f), 2);
    				
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);

                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		
                		double x = Double.parseDouble(values[0]);
                		double y = Double.parseDouble(values[1]);
                		String hemisphere = values[3];
                		
                		double anamorphosis = functions.anamorfosisLinealUtm(a, f, e, x, y, 0.0000, ko, hemisphere);
                		calculatedCoordinates.add(format.format(anamorphosis));
                		reportCalcAnamUtm.add(Funciones.getCalculoAnamorfosisUtm());
                	}
                	
                    refreshCoordinatesList(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates);
				}
			});
        }
        

        //----- METHODS AND SUBROUTINES
        
        public void reportData(ArrayList<String> calculatedCoordinates){
        	String title = getString(R.string.informe_calculo) + "\n" + getString(R.string.anamorfosis_lineal) + "\n" + "\n";
        	
        	String titleInputCoordinates = getString(R.string.coordenadas_utm) + "\n";
        	String inputCoordinates = "";
        	String titleCalculusProcess = getString(R.string.proceso_calculo) + "\n";
        	String calculusProcess = "";
        	String titleCalculated = getString(R.string.valores_calculados) + "\n";
        	String calculated = "";
        	
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputCoordinates =  inputCoordinates + addedPointsArrayList.get(i) + "\n";
        		calculusProcess = calculusProcess + reportCalcAnamUtm.get(i) + "\n";
        		calculated = calculated + calculatedCoordinates.get(i) + "\n";
        	}
        	
        	String returnValue = title +
        						 titleInputCoordinates + inputCoordinates + "\n" + "\n" +
        						 titleCalculusProcess + calculusProcess + "\n" + "\n" +
        						 titleCalculated + calculated; 
        	
        	setReportAnamUtm(returnValue);
        }
        
        public void refreshCoordinatesList(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        	listView.setAdapter(adapter);
        }
        
        public void refreshAddedCoordinatesList(ArrayList<String> arrayList, ListView listView){
        	ArrayList<String> formattedElementsArrayList = new ArrayList<String>();
        	for(int i = 0; i < arrayList.size(); i++){
        		String elements[] = arrayList.get(i).split(" ");
        		formattedElementsArrayList.add(elements[0] + "m " + elements[1] + "m " + elements[2] + "º " + elements[3]);
        	}
        	
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedElementsArrayList);
        	listView.setAdapter(adapter);
        }
    }
    
    
    /**
     * 
     * Meridians convergence Fragment with geodetic coordinates
     * 
     */
    
    public static class ConvergenceGeodeticFragment extends Fragment{
        //----- View elements declaration
        View rootView;
        TextView txtToolTitle;
        TextView txtToolSubTitle;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        TextView txtCalculatedValues;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;
        

        //----- Variables declaration
        ArrayList<String> addedPointsArrayList = new ArrayList<String>();
        ArrayList<String> reportCalcConvergence = new ArrayList<String>();
        MainActivity object = new MainActivity();
        Activity thisFragmentActivity;
        Funciones functions = new Funciones();
        DecimalFormat format = new DecimalFormat("0.00000");
        
        
        public static ConvergenceGeodeticFragment newInstance()
        {
        	ConvergenceGeodeticFragment fragment = new ConvergenceGeodeticFragment();
            return fragment;
        }

        public ConvergenceGeodeticFragment(){};
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	MainActivity.setFRAGMENT_SELECTED(CONVERGENCE_GEO_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
            txtToolSubTitle = (TextView) rootView.findViewById(R.id.textView1);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            txtCalculatedValues = (TextView) rootView.findViewById(R.id.textView4);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
            
        	return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            txtToolTitle.setText(getString(R.string.convergencia));
            txtToolSubTitle.setText(getString(R.string.coordenadas_geodesicas));
            txtCalculatedValues.setText(getString(R.string.valores_calculados) + " - " + getString(R.string.geodesicas_utm));
            
            addedPointsArrayList.addAll(getConvergenceGeoContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates); 
            }
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setConvergenceGeoContainerArray(addedPointsArrayList);
					geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setConvergenceGeoContainerArray(addedPointsArrayList);
                        	
                            DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                            dbFragment.show(getFragmentManager(), "DataBaseFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setConvergenceGeoContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            btnInputCoordinate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_geodetic_coordinate, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();

                    final EditText txtLatitudeGrades = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtLatitudeMinutes = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLatitudeSecconds = (EditText) layout.findViewById(R.id.editText3);

                    final EditText txtLongitudeGrades = (EditText) layout.findViewById(R.id.editText4);
                    final EditText txtLongitudeMinutes = (EditText) layout.findViewById(R.id.editText5);
                    final EditText txtLongitudeSecconds = (EditText) layout.findViewById(R.id.editText6);

                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);

                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);

                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strLatitudeGrades = txtLatitudeGrades.getText().toString();
                            String strLatitudeMinutes = txtLatitudeMinutes.getText().toString();
                            String strLatitudeSecconds = txtLatitudeSecconds.getText().toString();
                            String strLongitudeGrades = txtLongitudeGrades.getText().toString();
                            String strLongitudeMinutes = txtLongitudeMinutes.getText().toString();
                            String strLongitudeSecconds = txtLongitudeSecconds.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();

                            if(strLatitudeGrades.contentEquals("") || strLatitudeMinutes.contentEquals("") || strLatitudeSecconds.contentEquals("")
                                    || strLongitudeGrades.contentEquals("") || strLongitudeMinutes.contentEquals("") || strLongitudeSecconds.contentEquals("")
                                    || strLongitudeMc.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strLatitudeGrades + " " + strLatitudeMinutes + " " + strLatitudeSecconds + " "
                                        + strLongitudeGrades + " " + strLongitudeMinutes + " " + strLongitudeSecconds + " " + strLongitudeMc);
                                setConvergenceGeoContainerArray(addedPointsArrayList);
                                geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
                }
            });

            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setConvergenceGeoContainerArray(addedPointsArrayList);
                    geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                    calculatedCoordinatesListRefresh(addedPointsArrayList, lstCalculatedPoints);
                }
            });

            btnCalculate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
                	
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);

                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		
                		double phi = functions.recoje_puntos(values[0], values[1], values[2]);
                		double landa = functions.recoje_puntos(values[3], values[4], values[5]);
                		double landaMc = Double.parseDouble(values[6])*Math.PI/180;
                		double incrLanda = landa - landaMc;
                		double convergence = functions.convergenciaMeridianosGeodesicas(a, f, phi, incrLanda);
                		
                		calculatedCoordinates.add(format.format(convergence));
                		
                		reportCalcConvergence.add(Funciones.getCalculoConvergenciaGeodesicas());
                	}
                	
                    calculatedCoordinatesListRefresh(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates);
                }
            });
        }
        
        
        //----- METHODS AND SUBROUTINES
        public void reportData(ArrayList<String> calculatedCoordinates){
        	// 1.- Title and subTitle
        	String title = getString(R.string.informe_calculo) + "\n" + getString(R.string.convergencia) + "\n" + "\n";
        	
        	// 2.- Input coordinates list
        	String titleInputCoordinates = getString(R.string.coordenadas_geodesicas) + "\n";
        	String inputCoordinates = "";
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputCoordinates =  inputCoordinates + addedPointsArrayList.get(i) + "\n";
        	}
        	
        	// 3.- Calculus process
        	String titleCalculusProcess = getString(R.string.proceso_calculo) + "\n";
        	String calculusProcess = "";
        	for(int i = 0; i < reportCalcConvergence.size(); i++){
        		calculusProcess = calculusProcess + reportCalcConvergence.get(i) + "\n";
        	}
        	
        	String titleResults = getString(R.string.resultados) + "\n";
        	String results = "";
        	for(int i = 0; i < calculatedCoordinates.size(); i++){
        		results = results + calculatedCoordinates.get(i) + "\n";
        	}
        	
        	String report = title + 
        					titleInputCoordinates + inputCoordinates + "\n" + "\n" +
        					titleCalculusProcess + calculusProcess + "\n" + "\n" +
        					titleResults + results;
        			
        	setReportConverGeo(report);
        }
        
        
        public void geodeticListRefresh(ArrayList<String> list, ListView listView){
            ArrayList<String> formattedData = new ArrayList<String>();

            for(int i = 0; i <  list.size(); i++)
            {
                String[] data = list.get(i).split(" ");
                formattedData.add(data[0] + "º " + data[1] + "' " + data[2] + "''" + "  "
                        + data[3] + "º " + data[4] + "' " + data[5]+ "''" + "  "
                        + data[6] + "º");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedData);
            listView.setAdapter(adapter);
        }
        
        public void calculatedCoordinatesListRefresh(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            lstCalculatedPoints.setAdapter(adapter);
        }
    }
    
    
    /**
     * 
     *  Meridians Convengence calculation with UTM coordinates
     * 
     */
    
    public static class ConvergenceUtmFragment extends Fragment
    {
        //----- View elements declaration
    	Activity thisFragmentActivity;
        View rootView;
        TextView txtToolTitle;
        TextView txtToolSubTitle;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        TextView txtCalculatedValues;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;
        
        
        //----- Variables declaration
        String hemisphere = "N";
        ArrayList<String> addedPointsArrayList =  new ArrayList<String>();
        ArrayList<String> reportCalcConvergenceUtm = new ArrayList<String>();
        MainActivity object = new MainActivity();
        Funciones functions = new Funciones();
        DecimalFormat format = new DecimalFormat("0.00000");
        

        public static ConvergenceUtmFragment newInstance()
        {
        	ConvergenceUtmFragment fragment = new ConvergenceUtmFragment();
            return fragment;
        }

        public ConvergenceUtmFragment(){};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        	MainActivity.setFRAGMENT_SELECTED(CONVERGENCE_UTM_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
            txtToolSubTitle = (TextView) rootView.findViewById(R.id.textView1);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            txtCalculatedValues = (TextView) rootView.findViewById(R.id.textView4);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
            
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            txtToolTitle.setText(getString(R.string.convergencia));
            txtToolSubTitle.setText(getString(R.string.coordenadas_utm));
            txtCalculatedValues.setText(getString(R.string.valores_calculados) + " - " + getString(R.string.utm_geodesicas));
            
            addedPointsArrayList.addAll(getConvergenceUtmContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
            }
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setConvergenceUtmContainerArray(addedPointsArrayList);
					refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});
            
            btnInputCoordinate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_utm_coordinate, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();
                    
                    final RadioGroup rgHemispheres = (RadioGroup) layout.findViewById(R.id.radioGroup1);
                    final EditText txtXcoordinate = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtYcoordinate = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);

                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);
                    
                    rgHemispheres.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							switch (checkedId) {
								case R.id.radio0:
									hemisphere = "N";
									break;
								case R.id.radio1:
									hemisphere = "S";
									break;
							}
						}
					});
                    
                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strXcoordinate = txtXcoordinate.getText().toString();
                            String strYcoordinate = txtYcoordinate.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();

                            if(strXcoordinate.contentEquals("") || strYcoordinate.contentEquals("") || strLongitudeMc.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strXcoordinate + " " + strYcoordinate + " " + strLongitudeMc + " " + hemisphere);
                                setConvergenceUtmContainerArray(addedPointsArrayList);
                                refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setConvergenceUtmContainerArray(addedPointsArrayList);
                        	
                            DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                            dbFragment.show(getFragmentManager(), "DataBaseFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setConvergenceUtmContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            
            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setConvergenceUtmContainerArray(addedPointsArrayList);
                    refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
                    refreshCoordinatesList(addedPointsArrayList, lstCalculatedPoints);
                }
            });
            
            btnCalculate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
    				double e = 2*(1/f) - Math.pow((1/f), 2);
    				
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);

                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		
                		double x = Double.parseDouble(values[0]);
                		double y = Double.parseDouble(values[1]);
                		String hemisphere = values[3];
                		
                		double convergence = functions.convergenciameridianosUtm(a, f, e, x, y, 0.0000, ko, hemisphere);
                		calculatedCoordinates.add(format.format(convergence));
                		reportCalcConvergenceUtm.add(Funciones.getCalculoConvergenciaUtm());
                	}
                	
                    refreshCoordinatesList(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates);
				}
			});
        }
        

        //----- METHODS AND SUBROUTINES
        
        public void reportData(ArrayList<String> calculatedCoordinates){
        	String title = getString(R.string.informe_calculo) + "\n" + getString(R.string.convergencia) + "\n" + "\n";
        	
        	String titleInputCoordinates = getString(R.string.coordenadas_utm) + "\n";
        	String inputCoordinates = "";
        	String titleCalculusProcess = getString(R.string.proceso_calculo) + "\n";
        	String calculusProcess = "";
        	String titleCalculated = getString(R.string.valores_calculados) + "\n";
        	String calculated = "";
        	
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputCoordinates =  inputCoordinates + addedPointsArrayList.get(i) + "\n";
        		calculusProcess = calculusProcess + reportCalcConvergenceUtm.get(i) + "\n";
        		calculated = calculated + calculatedCoordinates.get(i) + "\n";
        	}
        	
        	String returnValue = title +
        						 titleInputCoordinates + inputCoordinates + "\n" + "\n" +
        						 titleCalculusProcess + calculusProcess + "\n" + "\n" +
        						 titleCalculated + calculated; 
        	
        	setReportConverUtm(returnValue);
        }
        
        public void refreshCoordinatesList(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        	listView.setAdapter(adapter);
        }
        
        public void refreshAddedCoordinatesList(ArrayList<String> arrayList, ListView listView){
        	ArrayList<String> formattedElementsArrayList = new ArrayList<String>();
        	for(int i = 0; i < arrayList.size(); i++){
        		String elements[] = arrayList.get(i).split(" ");
        		formattedElementsArrayList.add(elements[0] + "m " + elements[1] + "m " + elements[2] + "º " + elements[3]);
        	}
        	
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedElementsArrayList);
        	listView.setAdapter(adapter);
        }
    }
    
    
    /**
     * 
     * Reduced distance to UTM distance calculation with geodetic coordinates 
     * 
     */
    
    public static class DistanceGeodeticFragment extends Fragment{
        //----- View elements declaration
        View rootView;
        TextView txtToolTitle;
        TextView txtToolSubTitle;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        TextView txtCalculatedValues;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;
        

        //----- Variables declaration
        ArrayList<String> addedPointsArrayList = new ArrayList<String>();
        ArrayList<String> reportCalcDistance = new ArrayList<String>();
        MainActivity object = new MainActivity();
        Activity thisFragmentActivity;
        Funciones functions = new Funciones();
        DecimalFormat format = new DecimalFormat("0.000");
        
        
        public static DistanceGeodeticFragment newInstance()
        {
        	DistanceGeodeticFragment fragment = new DistanceGeodeticFragment();
            return fragment;
        }

        public DistanceGeodeticFragment(){};
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	MainActivity.setFRAGMENT_SELECTED(DISTANCE_GEO_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
            txtToolSubTitle = (TextView) rootView.findViewById(R.id.textView1);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            txtCalculatedValues = (TextView) rootView.findViewById(R.id.textView4);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
            
        	return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            txtToolTitle.setText(getString(R.string.reducida_utm));
            txtToolSubTitle.setText(getString(R.string.coordenadas_geo_distancia));
            txtCalculatedValues.setText(getString(R.string.distancias_calculadas));
            
            addedPointsArrayList.addAll(getDistRedUtmContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates); 
            }
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setDistRedUtmContainerArray(addedPointsArrayList);
					geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setDistRedUtmContainerArray(addedPointsArrayList);
                        	
                            DbDistancesDialogFragment dbFragment = new DbDistancesDialogFragment();
                            dbFragment.show(getFragmentManager(), "DbDistancesFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setDistRedUtmContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            btnInputCoordinate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_dist_geodetic, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();

                    final EditText txtLatitudeGrades = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtLatitudeMinutes = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLatitudeSecconds = (EditText) layout.findViewById(R.id.editText3);

                    final EditText txtLongitudeGrades = (EditText) layout.findViewById(R.id.editText4);
                    final EditText txtLongitudeMinutes = (EditText) layout.findViewById(R.id.editText5);
                    final EditText txtLongitudeSecconds = (EditText) layout.findViewById(R.id.editText6);

                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);
                    final EditText txtDistance = (EditText) layout.findViewById(R.id.editText8);
                    final EditText txtHeight = (EditText) layout.findViewById(R.id.editText9);
                    
                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);

                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strLatitudeGrades = txtLatitudeGrades.getText().toString();
                            String strLatitudeMinutes = txtLatitudeMinutes.getText().toString();
                            String strLatitudeSecconds = txtLatitudeSecconds.getText().toString();
                            String strLongitudeGrades = txtLongitudeGrades.getText().toString();
                            String strLongitudeMinutes = txtLongitudeMinutes.getText().toString();
                            String strLongitudeSecconds = txtLongitudeSecconds.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();
                            String strDistance = txtDistance.getText().toString();
                            String strHeight = txtHeight.getText().toString();

                            if(strLatitudeGrades.contentEquals("") || strLatitudeMinutes.contentEquals("") || strLatitudeSecconds.contentEquals("")
                                    || strLongitudeGrades.contentEquals("") || strLongitudeMinutes.contentEquals("") || strLongitudeSecconds.contentEquals("")
                                    || strLongitudeMc.contentEquals("") || strDistance.contentEquals("") || strHeight.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strLatitudeGrades + " " + strLatitudeMinutes + " " + strLatitudeSecconds + " "
                                        + strLongitudeGrades + " " + strLongitudeMinutes + " " + strLongitudeSecconds + " " 
                                		+ strLongitudeMc + " " + strDistance + " " + strHeight);
                                setDistRedUtmContainerArray(addedPointsArrayList);
                                geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
                }
            });

            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setDistRedUtmContainerArray(addedPointsArrayList);
                    geodeticListRefresh(addedPointsArrayList, lstAddedCoordinates);

                    calculatedCoordinatesListRefresh(addedPointsArrayList, lstCalculatedPoints);
                }
            });

            btnCalculate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
                	double e = 2*(1/f) - Math.pow((1/f), 2);
                	
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);

                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		
                		double phi = functions.recoje_puntos(values[0], values[1], values[2]);
                		double landa = functions.recoje_puntos(values[3], values[4], values[5]);
                		double landaMc = Double.parseDouble(values[6])*Math.PI/180;
                		double incrLanda = landa - landaMc;
                		double distance = Double.parseDouble(values[7]);
                		double height = Double.parseDouble(values[8]);
                		
                		double redDistance = functions.reducidaUtm(a, f, e, phi, distance, height, incrLanda, ko);
                		calculatedCoordinates.add(format.format(redDistance));
                		
                		reportCalcDistance.add(Funciones.getCalculoReducidaUtm());
                	}
                	
                    calculatedCoordinatesListRefresh(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates);
                }
            });
        }
        
        
        //----- METHODS AND SUBROUTINES
        public void reportData(ArrayList<String> calculatedCoordinates){
        	// 1.- Title and subTitle
        	String title = getString(R.string.informe_calculo) + "\n" + getString(R.string.reducida_utm) + "\n" + "\n";
        	
        	// 2.- Input coordinates list
        	String titleInputCoordinates = getString(R.string.coordenadas_geo_distancia) + "\n";
        	String inputCoordinates = "";
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputCoordinates =  inputCoordinates + addedPointsArrayList.get(i) + "\n";
        	}
        	
        	// 3.- Calculus process
        	String titleCalculusProcess = getString(R.string.proceso_calculo) + "\n";
        	String calculusProcess = "";
        	for(int i = 0; i < reportCalcDistance.size(); i++){
        		calculusProcess = calculusProcess + reportCalcDistance.get(i) + "\n";
        	}
        	
        	String titleResults = getString(R.string.resultados) + "\n";
        	String results = "";
        	for(int i = 0; i < calculatedCoordinates.size(); i++){
        		results = results + calculatedCoordinates.get(i) + "\n";
        	}
        	
        	String report = title + 
        					titleInputCoordinates + inputCoordinates + "\n" + "\n" +
        					titleCalculusProcess + calculusProcess + "\n" + "\n" +
        					titleResults + results;
        			
        	setReportDistGeo(report);
        }
        
        
        public void geodeticListRefresh(ArrayList<String> list, ListView listView){
            ArrayList<String> formattedData = new ArrayList<String>();

            for(int i = 0; i <  list.size(); i++)
            {
                String[] data = list.get(i).split(" ");
                formattedData.add(data[0] + "º " + data[1] + "' " + data[2] + "''" + "  "
                        + data[3] + "º " + data[4] + "' " + data[5]+ "''" + "  "
                        + data[6] + "º  " + data[7] + "m  " + data[8] + "m");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedData);
            listView.setAdapter(adapter);
        }
        
        public void calculatedCoordinatesListRefresh(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            lstCalculatedPoints.setAdapter(adapter);
        }
    }
    
    
    /**
     * 
     * Reduced distance to UTM distance calculation with geodetic coordinates 
     * 
     */
    
    public static class DistanceUtmFragment extends Fragment{
        //----- View elements declaration
        View rootView;
        TextView txtToolTitle;
        TextView txtToolSubTitle;
        Button btnFromFile;
        Button btnInputCoordinate;
        ListView lstAddedCoordinates;
        Spinner spnEllipsoids;
        TextView txtAnamorphosis;
        TextView txtCalculatedValues;
        Spinner spnAnamorphosis;
        Button btnCalculate;
        Button btnDeleteAll;
        ListView lstCalculatedPoints;
        

        //----- Variables declaration
        ArrayList<String> addedPointsArrayList = new ArrayList<String>();
        ArrayList<String> reportCalcDistance = new ArrayList<String>();
        MainActivity object = new MainActivity();
        Activity thisFragmentActivity;
        Funciones functions = new Funciones();
        DecimalFormat format = new DecimalFormat("0.000");
        String hemisphere = "N";
        
        
        public static DistanceUtmFragment newInstance()
        {
        	DistanceUtmFragment fragment = new DistanceUtmFragment();
            return fragment;
        }

        public DistanceUtmFragment(){};
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	MainActivity.setFRAGMENT_SELECTED(DISTANCE_UTM_FRAGMENT);
        	
            rootView = inflater.inflate(R.layout.fragment_geodetic_utm, container, false);
            txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
            txtToolSubTitle = (TextView) rootView.findViewById(R.id.textView1);
            btnFromFile = (Button) rootView.findViewById(R.id.button3);
            btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
            lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
            txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
            txtCalculatedValues = (TextView) rootView.findViewById(R.id.textView4);
            spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
            spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
            btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
            btnCalculate = (Button) rootView.findViewById(R.id.button2);
            lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
            
        	return rootView;
        }
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            
            txtToolTitle.setText(getString(R.string.utm_reducida));
            txtToolSubTitle.setText(getString(R.string.coordenadas_utm_distancia));
            txtCalculatedValues.setText(getString(R.string.distancias_calculadas));
            
            addedPointsArrayList.addAll(getDistUtmRedContainerArray());
            if(!addedPointsArrayList.isEmpty()){
            	refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates); 
            }
            
            
            thisFragmentActivity = getActivity();
            object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
            object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
            
            lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setDistUtmRedContainerArray(addedPointsArrayList);
					refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});

            btnFromFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.Selecciona_fuente));

                    builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	setDistUtmRedContainerArray(addedPointsArrayList);
                        	
                            DbDistancesDialogFragment dbFragment = new DbDistancesDialogFragment();
                            dbFragment.show(getFragmentManager(), "DbDistancesFragment");
                        }
                    });

                    builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                        	setDistUtmRedContainerArray(addedPointsArrayList);
                        	
                            NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                            dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            btnInputCoordinate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.view_input_dist_utm, null);
                    builder.setView(layout);
                    final AlertDialog inputCoordinateDialog = builder.create();

                    final RadioGroup rgHemispheres = (RadioGroup) layout.findViewById(R.id.radioGroup1);
                    final EditText txtX = (EditText) layout.findViewById(R.id.editText1);
                    final EditText txtY = (EditText) layout.findViewById(R.id.editText2);
                    final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);
                    final EditText txtDistance = (EditText) layout.findViewById(R.id.editText3);
                    final EditText txtHeight = (EditText) layout.findViewById(R.id.editText4);
                    
                    Button btnCancel = (Button) layout.findViewById(R.id.button1);
                    Button btnAccept = (Button) layout.findViewById(R.id.button2);
                    
                    rgHemispheres.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							switch (checkedId) {
								case R.id.radio0:
									hemisphere = "N";
									break;
								case R.id.radio1:
									hemisphere = "S";
									break;
							}
						}
					});

                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            inputCoordinateDialog.dismiss();
                        }
                    });

                    btnAccept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String strX = txtX.getText().toString();
                            String strY = txtY.getText().toString();
                            String strLongitudeMc = txtLongitudeMc.getText().toString();
                            String strDistance = txtDistance.getText().toString();
                            String strHeight = txtHeight.getText().toString();

                            if(strX.contentEquals("") || strY.contentEquals("") || strLongitudeMc.contentEquals("") || 
                            		strDistance.contentEquals("") || strHeight.contentEquals(""))
                            {
                                Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addedPointsArrayList.add(strX + " " + strY + " " + strLongitudeMc + " " + hemisphere + " " +
                                						strDistance + " " + strHeight);
                                setDistUtmRedContainerArray(addedPointsArrayList);
                                refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);

                                inputCoordinateDialog.dismiss();
                            }
                        }
                    });

                    inputCoordinateDialog.show();
                }
            });

            btnDeleteAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addedPointsArrayList.clear();
                    setDistUtmRedContainerArray(addedPointsArrayList);
                    
                    refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
                    calculatedCoordinatesListRefresh(addedPointsArrayList, lstCalculatedPoints);
                }
            });

            btnCalculate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
                	double a = Double.parseDouble(ellipsoidValues[1]);
                	double f = Double.parseDouble(ellipsoidValues[2]);
                	double e = 2*(1/f) - Math.pow((1/f), 2);
                	
                	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
                	double ko = Double.parseDouble(anamorphosisValue[1]);

                	ArrayList<String> calculatedCoordinates = new ArrayList<String>();
                	
                	for(int i = 0; i < addedPointsArrayList.size(); i++){
                		String values[] = addedPointsArrayList.get(i).split(" ");
                		
                		double x = Double.parseDouble(values[0]);
                		double y = Double.parseDouble(values[1]);
                		double distance = Double.parseDouble(values[4]);
                		double height = Double.parseDouble(values[5]);
                		
                		double utmDistance = functions.utmReducida(a, f, e, x, y, distance, height, ko, hemisphere);
                		calculatedCoordinates.add(format.format(utmDistance));
                		
                		reportCalcDistance.add(Funciones.getCalculoUtmReducida());
                	}
                	
                    calculatedCoordinatesListRefresh(calculatedCoordinates, lstCalculatedPoints);
                    reportData(calculatedCoordinates);
                }
            });
        }
        
        
        //----- METHODS AND SUBROUTINES
        public void reportData(ArrayList<String> calculatedCoordinates){
        	// 1.- Title and subTitle
        	String title = getString(R.string.informe_calculo) + "\n" + getString(R.string.utm_reducida) + "\n" + "\n";
        	
        	// 2.- Input coordinates list
        	String titleInputCoordinates = getString(R.string.coordenadas_utm_distancia) + "\n";
        	String inputCoordinates = "";
        	for(int i = 0; i < addedPointsArrayList.size(); i++){
        		inputCoordinates =  inputCoordinates + addedPointsArrayList.get(i) + "\n";
        	}
        	
        	// 3.- Calculus process
        	String titleCalculusProcess = getString(R.string.proceso_calculo) + "\n";
        	String calculusProcess = "";
        	for(int i = 0; i < reportCalcDistance.size(); i++){
        		calculusProcess = calculusProcess + reportCalcDistance.get(i) + "\n";
        	}
        	
        	String titleResults = getString(R.string.resultados) + "\n";
        	String results = "";
        	for(int i = 0; i < calculatedCoordinates.size(); i++){
        		results = results + calculatedCoordinates.get(i) + "\n";
        	}
        	
        	String report = title + 
        					titleInputCoordinates + inputCoordinates + "\n" + "\n" +
        					titleCalculusProcess + calculusProcess + "\n" + "\n" +
        					titleResults + results;
        			
        	setReportDistUtm(report);
        }
        
        public void refreshAddedCoordinatesList(ArrayList<String> arrayList, ListView listView){
        	ArrayList<String> formattedElementsArrayList = new ArrayList<String>();
        	for(int i = 0; i < arrayList.size(); i++){
        		String elements[] = arrayList.get(i).split(" ");
        		formattedElementsArrayList.add(elements[0] + "m " + elements[1] + "m " + elements[2] + "º " + elements[3]
        				 + " " + elements[4] + "m " + elements[5] + "m");
        	}
        	
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedElementsArrayList);
        	listView.setAdapter(adapter);
        }
        
        public void calculatedCoordinatesListRefresh(ArrayList<String> arrayList, ListView listView){
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            lstCalculatedPoints.setAdapter(adapter);
        }
    }
    
    
    
    /**
    *
    *
    * Distance and Azimuth computing Fragment
    *
    * **/

   public static class DistanceAcimuthFragment extends Fragment
   {
       //----- View elements declaration
	   Activity thisFragmentActivity;
       View rootView;
       TextView txtToolTitle;
       Button btnFromFile;
       Button btnInputCoordinate;
       ListView lstAddedCoordinates;
       Spinner spnEllipsoids;
       TextView txtAnamorphosis;
       Spinner spnAnamorphosis;
       Button btnCalculate;
       Button btnDeleteAll;
       ListView lstCalculatedPoints;
       
       
       //----- Variables declaration
       String hemisphere = "N";
       ArrayList<String> addedPointsArrayList =  new ArrayList<String>();
       ArrayList<String> calculatedValuesArray = new ArrayList<String>();
       MainActivity object = new MainActivity();
       Funciones functions = new Funciones();
       DecimalFormat format = new DecimalFormat("0.000");
       

       public static DistanceAcimuthFragment newInstance()
       {
    	   DistanceAcimuthFragment fragment = new DistanceAcimuthFragment();
           return fragment;
       }

       public DistanceAcimuthFragment(){};

       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
       {
    	   MainActivity.setFRAGMENT_SELECTED(AZIMUT_DISTANCE_FRAGMENT);
       	
           rootView = inflater.inflate(R.layout.fragment_acimut_distance, container, false);
           txtToolTitle = (TextView) rootView.findViewById(R.id.textView7);
           btnFromFile = (Button) rootView.findViewById(R.id.button3);
           btnInputCoordinate = (Button) rootView.findViewById(R.id.button4);
           lstAddedCoordinates = (ListView) rootView.findViewById(R.id.listView1);
           txtAnamorphosis = (TextView) rootView.findViewById(R.id.textView6);
           spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
           spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
           btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
           btnCalculate = (Button) rootView.findViewById(R.id.button2);
           lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView2);
           
           
           return rootView;
       }

       @Override
       public void onActivityCreated(Bundle savedInstanceState)
       {
           super.onActivityCreated(savedInstanceState);
           
           addedPointsArrayList.addAll(getAcimutDistanceContainerArray());
           if(!addedPointsArrayList.isEmpty()){
           	refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
           }
           
           thisFragmentActivity = getActivity();
           object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
           object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
           
           lstAddedCoordinates.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					addedPointsArrayList.remove(position);
					setAcimutDistanceContainerArray(addedPointsArrayList);
					refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
					return false;
				}
			});
           
           btnInputCoordinate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                   AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                   LayoutInflater inflater = getActivity().getLayoutInflater();
                   View layout = inflater.inflate(R.layout.view_input_utm_coordinate, null);
                   builder.setView(layout);
                   final AlertDialog inputCoordinateDialog = builder.create();
                   
                   final RadioGroup rgHemispheres = (RadioGroup) layout.findViewById(R.id.radioGroup1);
                   final EditText txtXcoordinate = (EditText) layout.findViewById(R.id.editText1);
                   final EditText txtYcoordinate = (EditText) layout.findViewById(R.id.editText2);
                   final EditText txtLongitudeMc = (EditText) layout.findViewById(R.id.editText7);

                   Button btnCancel = (Button) layout.findViewById(R.id.button1);
                   Button btnAccept = (Button) layout.findViewById(R.id.button2);
                   
                   rgHemispheres.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							switch (checkedId) {
								case R.id.radio0:
									hemisphere = "N";
									break;
								case R.id.radio1:
									hemisphere = "S";
									break;
							}
						}
					});
                   
                   btnCancel.setOnClickListener(new View.OnClickListener()
                   {
                       @Override
                       public void onClick(View v)
                       {
                           inputCoordinateDialog.dismiss();
                       }
                   });

                   btnAccept.setOnClickListener(new View.OnClickListener()
                   {
                       @Override
                       public void onClick(View v)
                       {
                           String strXcoordinate = txtXcoordinate.getText().toString();
                           String strYcoordinate = txtYcoordinate.getText().toString();
                           String strLongitudeMc = txtLongitudeMc.getText().toString();

                           if(strXcoordinate.contentEquals("") || strYcoordinate.contentEquals("") || strLongitudeMc.contentEquals(""))
                           {
                               Toast.makeText(getActivity(), getText(R.string.no_valores_vacios), Toast.LENGTH_SHORT).show();
                           }
                           else
                           {
                               addedPointsArrayList.add(strXcoordinate + " " + strYcoordinate + " " + strLongitudeMc + " " + hemisphere);
                               setAcimutDistanceContainerArray(addedPointsArrayList);
                               refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);

                               inputCoordinateDialog.dismiss();
                           }
                       }
                   });

                   inputCoordinateDialog.show();
				}
			});

           btnFromFile.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View arg0)
               {
                   AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                   builder.setMessage(getString(R.string.Selecciona_fuente));

                   builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                   {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                    	   setAcimutDistanceContainerArray(addedPointsArrayList);
                       	
                           DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                           dbFragment.show(getFragmentManager(), "DataBaseFragment");
                       }
                   });

                   builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                   {
                       public void onClick(DialogInterface dialog, int which) {
                    	   setAcimutDistanceContainerArray(addedPointsArrayList);
                       	
                           NavigatorDialogFragment dialogFragment =  new NavigatorDialogFragment();
                           dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                       }
                   });

                   final AlertDialog alertDialog = builder.create();
                   alertDialog.show();
               }
           });
           
           btnDeleteAll.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v)
               {
            	   calculatedValuesArray.clear();
                   addedPointsArrayList.clear();
                   setAcimutDistanceContainerArray(addedPointsArrayList);
                   refreshAddedCoordinatesList(addedPointsArrayList, lstAddedCoordinates);
                   refreshCoordinatesList(addedPointsArrayList, lstCalculatedPoints);
               }
           });
           
           btnCalculate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(lstAddedCoordinates.getCount() == 2){
					   	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
		               	double a = Double.parseDouble(ellipsoidValues[1]);
		               	double f = Double.parseDouble(ellipsoidValues[2]);
		   				double e = 2*(1/f) - Math.pow((1/f), 2);
		   				
		   				
		               	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
		               	double ko = Double.parseDouble(anamorphosisValue[1]);
		               	
	               	
		               	//----- Computations
		               	String valuesStation[] = addedPointsArrayList.get(0).split(" ");
		               	String valuesObserved[] = addedPointsArrayList.get(1).split(" ");
		               	double xStation = Double.parseDouble(valuesStation[0]);
		               	double yStation = Double.parseDouble(valuesStation[1]);
		               	double xObserved = Double.parseDouble(valuesObserved[0]);
		               	double yObserved = Double.parseDouble(valuesObserved[1]);
		               	
		               	double cartographicAzimut = functions.acimut(xStation, yStation, xObserved, yObserved, 180);
		               	double utmDistance = functions.distancia(xStation, yStation, xObserved, yObserved);
		               	
		               	double geodeticDist = functions.reduccionDistanciaCuerda(utmDistance, cartographicAzimut, xStation, yStation, a, e, f, ko, hemisphere);
		               	double geodeticAz = functions.calculoAcimutGeodesico(cartographicAzimut, xStation, yStation, xObserved, yObserved, a, f, e, ko, hemisphere);
		               	
		               	String strCartAz = functions.pasar_a_sexa(cartographicAzimut);
		               	String strGeodAz = functions.pasar_a_sexa(geodeticAz);
		               	String strUtmDistance = format.format(utmDistance) + "m";
		               	String strGeodDistance = format.format(geodeticDist) + "m";
		               	
		               	calculatedValuesArray.add(0, strCartAz + " " + strUtmDistance + " " + strGeodAz + " " + strGeodDistance);
		               	refreshCoordinatesList(calculatedValuesArray, lstCalculatedPoints);
		               	
		               	reportData(calculatedValuesArray);
					} else{
						Toast.makeText(getActivity(), getString(R.string.msg_dos_puntos), Toast.LENGTH_SHORT).show();
					}
				}
			});
       }
       
       public void reportData(ArrayList<String> calculatedCoordinates){
    	   String title = getString(R.string.acimut_distancia) + " - " + getString(R.string.valores_calculados);
    	   String value = getString(R.string.resultado);
    	   
			String returnValue = "";
			for(int i = 0; i < calculatedCoordinates.size(); i++){
				returnValue = returnValue + value + (i+1) + ": " + calculatedCoordinates.get(i) + "\n";
			}
			
			setReportAcimutDistance(title + "\n" + "\n" + returnValue);
       }
       
       //----- METHODS AND SUBROUTINES
       public void refreshCoordinatesList(ArrayList<String> arrayList, ListView listView){
	       	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
	       	listView.setAdapter(adapter);
       }
       
       public void refreshAddedCoordinatesList(ArrayList<String> arrayList, ListView listView){
	       	ArrayList<String> formattedElementsArrayList = new ArrayList<String>();
	       	for(int i = 0; i < arrayList.size(); i++){
	       		String elements[] = arrayList.get(i).split(" ");
	       		formattedElementsArrayList.add(elements[0] + "m " + elements[1] + "m " + elements[2] + "º " + elements[3]);
	       	}
	       	
	       	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedElementsArrayList);
	       	listView.setAdapter(adapter);
       }
   }
   
   
   /**
   *
   *
   * Distance and Azimuth computing Fragment
   *
   * **/

  public static class CoordinatesComputingFragment extends Fragment
  {
      //----- View elements declaration
	  Activity thisFragmentActivity;
      View rootView;
      
      RadioGroup rgHemispheres;
      RadioButton rButton1;
      RadioButton rButton2;
      EditText edtEastingX;
      EditText edtNortingY;
      TextView txtFromFile;
      EditText edtAzimutGrades;
      EditText edtAzimutMinutes;
      EditText edtAzimutSecconds;
      EditText edtDistance;

      Spinner spnEllipsoids;
      Spinner spnAnamorphosis;
      Button btnCalculate;
      Button btnDeleteAll;
      ListView lstCalculatedPoints;
      
      
      //----- Variables declaration
      String hemisphere = "N";
      ArrayList<String> addedPointsArrayList =  new ArrayList<String>();
      ArrayList<String> calculatedValuesArray = new ArrayList<String>();
      MainActivity object = new MainActivity();
      Funciones functions = new Funciones();
      DecimalFormat format = new DecimalFormat("0.000");
      

      public static CoordinatesComputingFragment newInstance()
      {
    	  CoordinatesComputingFragment fragment = new CoordinatesComputingFragment();
          return fragment;
      }

      public CoordinatesComputingFragment(){};

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
      {
    	  MainActivity.setFRAGMENT_SELECTED(COORDINATE_COMPUTE_FRAGMENT);
      	
          rootView = inflater.inflate(R.layout.fragment_coordinates_computing, container, false);
          
          rgHemispheres = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
          rButton1 = (RadioButton) rootView.findViewById(R.id.radio0);
          rButton2 = (RadioButton) rootView.findViewById(R.id.radio1);
          edtEastingX = (EditText) rootView.findViewById(R.id.editText1);
          edtNortingY = (EditText) rootView.findViewById(R.id.editText2);
          txtFromFile = (TextView) rootView.findViewById(R.id.textView1);
          edtAzimutGrades = (EditText) rootView.findViewById(R.id.editText3);
          edtAzimutMinutes = (EditText) rootView.findViewById(R.id.editText4);
          edtAzimutSecconds = (EditText) rootView.findViewById(R.id.editText5);
          edtDistance = (EditText) rootView.findViewById(R.id.editText6);
          spnEllipsoids = (Spinner) rootView.findViewById(R.id.spinner1);
          spnAnamorphosis = (Spinner) rootView.findViewById(R.id.spinner2);
          btnDeleteAll = (Button) rootView.findViewById(R.id.button1);
          btnCalculate = (Button) rootView.findViewById(R.id.button2);
          lstCalculatedPoints = (ListView) rootView.findViewById(R.id.listView1);
          
          
          return rootView;
      }

      @Override
      public void onActivityCreated(Bundle savedInstanceState)
      {
          super.onActivityCreated(savedInstanceState);
          
          String receivedCoordinates = getCoordinatesComputeContainer();
          if(!receivedCoordinates.contentEquals("")){
        	  String values[] = receivedCoordinates.split(" ");
        	  edtEastingX.setText(values[0]);
        	  edtNortingY.setText(values[1]);
        	  
        	  String receivedHemisphere = values[3];
        	  hemisphere = receivedHemisphere;
        	  if(hemisphere.contentEquals("N")){
        		  rButton1.setChecked(true);
        		  rButton2.setChecked(false);
        	  } else{
        		  rButton1.setChecked(false);
        		  rButton2.setChecked(true);        		  
        	  }
        	  
          }
          
          thisFragmentActivity = getActivity();
          object.refreshAnamorphosisSpinner(spnAnamorphosis, thisFragmentActivity);
          object.refreshEllipsoidsSpinner(spnEllipsoids, thisFragmentActivity);
          
          rgHemispheres.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.radio0:
						hemisphere = "N";
					break;
					case R.id.radio1:
						hemisphere = "S";
					break;
				}
				
			}
		});
          
          txtFromFile.setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View arg0)
              {
                  AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
                  builder.setMessage(getString(R.string.Selecciona_fuente));

                  builder.setPositiveButton(getString(R.string.base_datos), new DialogInterface.OnClickListener()
                  {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          DataBaseDialogFragment dbFragment = new DataBaseDialogFragment();
                          dbFragment.show(getFragmentManager(), "DataBaseFragment");
                      }
                  });

                  builder.setNegativeButton(getString(R.string.fichero_de_texto), new DialogInterface.OnClickListener()
                  {
                      public void onClick(DialogInterface dialog, int which) {
                          ComputeCoordinatesNavigatorDialogFragment dialogFragment =  new ComputeCoordinatesNavigatorDialogFragment();
                          dialogFragment.show(getFragmentManager(), "NavigatorDialog");
                      }
                  });

                  final AlertDialog alertDialog = builder.create();
                  alertDialog.show();
              }
          });
          
          btnDeleteAll.setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View v)
              {
            	  calculatedValuesArray.clear();
                  refreshCoordinatesList(calculatedValuesArray, lstCalculatedPoints);
              }
          });
          
          btnCalculate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				   	String ellipsoidValues[] = spnEllipsoids.getSelectedItem().toString().split(" ");
	               	double a = Double.parseDouble(ellipsoidValues[1]);
	               	double f = Double.parseDouble(ellipsoidValues[2]);
	   				double e = 2*(1/f) - Math.pow((1/f), 2);
	   				
	   				
	               	String anamorphosisValue[] = spnAnamorphosis.getSelectedItem().toString().split(" ");
	               	double ko = Double.parseDouble(anamorphosisValue[1]);
	               	
               	
	               	//----- Computations
		               	

				}
			});
      }
      
      public void reportData(ArrayList<String> calculatedCoordinates){
   	   String title = getString(R.string.coordenadas_calculadas);
   	   String value = getString(R.string.resultado);
   	   
			String returnValue = "";
			for(int i = 0; i < calculatedCoordinates.size(); i++){
				returnValue = returnValue + value + (i+1) + ": " + calculatedCoordinates.get(i) + "\n";
			}
			
			setReportAcimutDistance(title + "\n" + "\n" + returnValue);
      }
      
      //----- METHODS AND SUBROUTINES
      public void refreshCoordinatesList(ArrayList<String> arrayList, ListView listView){
	       	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
	       	listView.setAdapter(adapter);
      }
      
      public void refreshAddedCoordinatesList(ArrayList<String> arrayList, ListView listView){
	       	ArrayList<String> formattedElementsArrayList = new ArrayList<String>();
	       	for(int i = 0; i < arrayList.size(); i++){
	       		String elements[] = arrayList.get(i).split(" ");
	       		formattedElementsArrayList.add(elements[0] + "m " + elements[1] + "m " + elements[2] + "º " + elements[3]);
	       	}
	       	
	       	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, formattedElementsArrayList);
	       	listView.setAdapter(adapter);
      }
  }
}
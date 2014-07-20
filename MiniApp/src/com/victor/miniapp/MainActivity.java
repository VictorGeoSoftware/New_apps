package com.victor.miniapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	//----- Global elements
	static ListView lstCountries;
	static Activity globalActivity;  // For reference Adapter with PlaceHolderFragment listView.
	
	
	//----- Global variables
	static String  API_CONVERSIONS = "http://iphone.apps.taptapnetworks.com/install/codetest/conversions.json";
	static String  API_PACKCAGES = "http://iphone.apps.taptapnetworks.com/install/codetest/packages.json";
	
	private static String selectedCountry;
	
	private static String M_KG = "KG";
	private static String M_OZ = "OZ";
	private static String M_LB = "LB";
	
	private static double CNT_KG_LB;
	private static double CNT_OZ_KG;
	private static double CNT_LB_KG;
	private static double CNT_KG_OZ;
	
	private static ArrayList<String> countryListArray = new ArrayList<String>();
	private static ArrayList<String> idArray = new ArrayList<String>();
	private static ArrayList<String> destinationArray = new ArrayList<String>();
	private static ArrayList<String> weightArray = new ArrayList<String>();
	private static ArrayList<String> measureArray = new ArrayList<String>();
	
	public static double getCNT_KG_LB() {
		return CNT_KG_LB;
	}

	public static void setCNT_KG_LB(double cNT_KG_LB) {
		CNT_KG_LB = cNT_KG_LB;
	}

	public static double getCNT_OZ_KG() {
		return CNT_OZ_KG;
	}

	public static void setCNT_OZ_KG(double cNT_OZ_KG) {
		CNT_OZ_KG = cNT_OZ_KG;
	}

	public static double getCNT_LB_KG() {
		return CNT_LB_KG;
	}

	public static void setCNT_LB_KG(double cNT_LB_KG) {
		CNT_LB_KG = cNT_LB_KG;
	}

	public static double getCNT_KG_OZ() {
		return CNT_KG_OZ;
	}

	public static void setCNT_KG_OZ(double cNT_KG_OZ) {
		CNT_KG_OZ = cNT_KG_OZ;
	}

	public static String getSelectedCountry() {
		return selectedCountry;
	}

	public static void setSelectedCountry(String selectedCountry) {
		MainActivity.selectedCountry = selectedCountry;
	}

	public static ArrayList<String> getCountryListArray() {
		return countryListArray;
	}

	public static void setCountryListArray(ArrayList<String> countryListArray) {
		MainActivity.countryListArray = countryListArray;
	}
	
	public static ArrayList<String> getIdArray() {
		return idArray;
	}

	public static void setIdArray(ArrayList<String> idArray) {
		MainActivity.idArray = idArray;
	}

	public static ArrayList<String> getDestinationArray() {
		return destinationArray;
	}

	public static void setDestinationArray(ArrayList<String> destinationArray) {
		MainActivity.destinationArray = destinationArray;
	}

	public static ArrayList<String> getWeightArray() {
		return weightArray;
	}

	public static void setWeightArray(ArrayList<String> weightArray) {
		MainActivity.weightArray = weightArray;
	}

	public static ArrayList<String> getMeasureArray() {
		return measureArray;
	}

	public static void setMeasureArray(ArrayList<String> measureArray) {
		MainActivity.measureArray = measureArray;
	}
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		new WeightConversionConstantTaks().execute(API_CONVERSIONS);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
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
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.view_alert_dialog, null);
			builder.setView(view);
			builder.show();
		}
		return super.onOptionsItemSelected(item);
	}
	

	
	/*
	 * Http request for obtain conversion constants
	 */
	
	public class WeightConversionConstantTaks extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			String response = null;
			
			try{
				httpResponse = httpClient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = httpResponse.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					httpResponse.getEntity().writeTo(out);
					out.close();
					
					response = out.toString();
				} else{
					httpResponse.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			}catch(ClientProtocolException e){
				Toast.makeText(getApplicationContext(), getString(R.string.error) + ": " + e, Toast.LENGTH_SHORT).show();
			}catch (IOException e) {
				Toast.makeText(getApplicationContext(), getString(R.string.error) + ": " + e, Toast.LENGTH_SHORT).show();
			}
			
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			ArrayList<String> conversion_values = new ArrayList<String>();
			
			try {
				JSONArray receivedResult = new JSONArray(result);
				
				for(int i = 0; i < receivedResult.length(); i++){
					JSONObject fileObject = receivedResult.getJSONObject(i);
					String valueConversion = fileObject.getString("conversion");
					
					conversion_values.add(valueConversion);
				}
				
				setCNT_KG_LB(Double.parseDouble(conversion_values.get(0)));
				setCNT_OZ_KG(Double.parseDouble(conversion_values.get(1)));
				setCNT_LB_KG(Double.parseDouble(conversion_values.get(2)));
				setCNT_KG_OZ(Double.parseDouble(conversion_values.get(3)));
				
				//----- The HttpRequest for package's data is made here, because if this request fail, it won't be possible to
				//----- make operations without conversion values.
				new DataPackagesRequest().execute(API_PACKCAGES);
				
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), getString(R.string.error) + ": " + e, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * HTTP request for get data about Countries and Packages
	 */
	
	public class DataPackagesRequest extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			idArray.clear();
			destinationArray.clear();
			weightArray.clear();
			measureArray.clear();
		}
		
		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			String response = null;
			
			try{
				httpResponse = httpClient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = httpResponse.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					httpResponse.getEntity().writeTo(out);
					out.close();
					
					response = out.toString();
					
				} else{
					httpResponse.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
				
			}catch(ClientProtocolException e){
				Toast.makeText(getApplicationContext(), getString(R.string.error) + ": " + e, Toast.LENGTH_SHORT).show();
			}catch (IOException e) {
				Toast.makeText(getApplicationContext(), getString(R.string.error) + ": " + e, Toast.LENGTH_SHORT).show();
			}
			
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			try{
				JSONArray receivedResult = new JSONArray(result);
				
				for(int i = 0; i < receivedResult.length(); i++){
					JSONObject fileObject = receivedResult.getJSONObject(i);
					String valueId = fileObject.getString("id");
					String valueDestination = fileObject.getString("destination");
					String valueWeight = fileObject.getString("weight");
					String valueMeasure = fileObject.getString("measure");
					
					idArray.add(valueId);
					destinationArray.add(valueDestination);
					weightArray.add(valueWeight);
					measureArray.add(valueMeasure);
				}
				
				Log.i("onPostExecute", "arrays: " + idArray.size() + " " + destinationArray.size() + " " + weightArray.size() + " " + measureArray.size());
				
				countryListArray.addAll(destinationArray);
				HashSet<String> destinationFilter = new HashSet<String>(countryListArray);
				countryListArray.clear();
				countryListArray.addAll(destinationFilter);
				Log.i("onPostExecute", "number of countries: " + countryListArray.size());
				
				CountryListAdapter adapter = new CountryListAdapter(globalActivity, countryListArray);
				lstCountries.setAdapter(adapter);
				Toast.makeText(globalActivity, "Petición terminada", Toast.LENGTH_SHORT).show();
				
			}catch(Exception e){
				Toast.makeText(globalActivity, getString(R.string.error) + ": " + e, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	
	
	// MODEL VIEW CONTROLLER FOR ListView
	private class CountryListAdapter extends ArrayAdapter<String>
	{
		ArrayList<String> countryArrayList = new ArrayList<String>();
		Activity adapterActivity;
		
		public CountryListAdapter(Activity context, ArrayList<String> countryArrayList) {
			super(context, R.layout.view_countries_listview, countryArrayList);
			
			this.adapterActivity = context;
			this.countryArrayList = countryArrayList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			
			if(convertView == null){
				LayoutInflater inflater = adapterActivity.getLayoutInflater();
				convertView = inflater.inflate(R.layout.view_countries_listview, null);
				
				holder = new ViewHolder();
				
				holder.txtCountryName = (TextView) convertView.findViewById(R.id.textView1);
				
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.txtCountryName.setText(countryArrayList.get(position));
			
			return convertView;
		}
		
		private class ViewHolder{
			TextView txtCountryName;
		}
	}
	
	

	/**
	 * ------------------     FRAGMENTS     ------------------         
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {}
		
		Button btnReload;
		ArrayList<String> countries = getCountryListArray();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			
			globalActivity = getActivity(); // Activity initialized for reference the listView adapter class
			lstCountries = (ListView) rootView.findViewById(R.id.listView1);
			btnReload = (Button) rootView.findViewById(R.id.button1);
			
			return rootView;
		}
		
		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			if(lstCountries.getCount() < 1 && countryListArray.size() > 0){
				MainActivity mInstance = new MainActivity();
				CountryListAdapter adapter = mInstance. new CountryListAdapter(globalActivity, countryListArray);
				lstCountries.setAdapter(adapter);
			}

			btnReload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity mInstance = new MainActivity();
					mInstance. new WeightConversionConstantTaks().execute(API_CONVERSIONS);
				}
			});

			lstCountries.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String selectedCountry = lstCountries.getItemAtPosition(position).toString();
					setSelectedCountry(selectedCountry);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new DetailPackageFragment()).commit();
				}
			});
		}
	}
	
	
	public static class DetailPackageFragment extends Fragment{
		
		public DetailPackageFragment() {}
		
		//----- Fragment Environment variables
		String receivedCountryId = "";
		double totalWeight = 0;
		DecimalFormat kgFormat = new DecimalFormat("###,###,###.000");
		DecimalFormat noDecimal = new DecimalFormat("0");
		
		ArrayList<String> kilogramsArray = new ArrayList<String>();
		ArrayList<String> productsArray = new ArrayList<String>();
		
		//----- Fragment elements
		Button btnBack;
		TextView txtId;
		TextView txtAirplanes;
		TextView txtKilograms;
		ListView lstProducts;
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_detail_package, container, false);
			
			globalActivity = getActivity(); // Activity initialized for reference the listView adapter class
			
			btnBack = (Button) rootView.findViewById(R.id.button1);
			txtId = (TextView) rootView.findViewById(R.id.textView1);
			txtAirplanes = (TextView) rootView.findViewById(R.id.textView3);
			txtKilograms = (TextView) rootView.findViewById(R.id.textView4);
			lstProducts = (ListView) rootView.findViewById(R.id.listView1);
			
			receivedCountryId = getSelectedCountry();
			
			for(int i = 0; i < idArray.size(); i++){
				if(destinationArray.get(i).contentEquals(receivedCountryId)){
					productsArray.add(idArray.get(i));

					Log.i("bucle", "pesos: " + weightArray.get(i) + " " + measureArray.get(i));
					
					if(measureArray.get(i).toString().contentEquals(M_OZ)){
						totalWeight = totalWeight + Double.parseDouble(weightArray.get(i))*CNT_OZ_KG;
					}
					
					if(measureArray.get(i).contentEquals(M_LB)){
						totalWeight = totalWeight + Double.parseDouble(weightArray.get(i))*CNT_LB_KG;
					}
					
					if(measureArray.get(i).contentEquals(M_KG)){
						totalWeight = totalWeight + Double.parseDouble(weightArray.get(i));
					}
				}
			}
			
			return rootView;
		}
		
		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			txtId.setText(receivedCountryId);
			txtKilograms.setText(kgFormat.format(totalWeight) + " " + getString(R.string.kg));
			
			//-- Medium capacity of a standard plane (http://es.wikipedia.org/wiki/Transporte_a%C3%A9reo#Tipos_de_aviones)
			double airplanes = Math.ceil(totalWeight/30000); 
			txtAirplanes.setText(noDecimal.format(airplanes) + " " + getString(R.string.airplanes));
			
			PackageListAdapter adapter = new PackageListAdapter(getActivity(), productsArray);
			lstProducts.setAdapter(adapter);
			
			btnBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment()).commit();
				}
			});
		}
		
		private class PackageListAdapter extends ArrayAdapter<String>
		{
			ArrayList<String> packageArrayList = new ArrayList<String>();
			Activity adapterActivity;
			
			public PackageListAdapter(Activity adapterActivity, ArrayList<String> packageArrayList) {
				super(adapterActivity, R.layout.view_packages_listview, packageArrayList);
				
				this.adapterActivity = adapterActivity;
				this.packageArrayList = packageArrayList;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				
				if(convertView == null){
					LayoutInflater inflater = adapterActivity.getLayoutInflater();
					convertView = inflater.inflate(R.layout.view_packages_listview, null);
					
					holder = new ViewHolder();
					
					holder.txtPackageName = (TextView) convertView.findViewById(R.id.textView1);
					
					convertView.setTag(holder);
				}
				else{
					holder = (ViewHolder) convertView.getTag();
				}
				
				holder.txtPackageName.setText(packageArrayList.get(position));
				
				return convertView;
			}
			
			private class ViewHolder{
				TextView txtPackageName;
			}
		}
	}
}

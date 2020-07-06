package org.techtown.registerlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Menu_home extends Fragment {

    private View view;
    TextView text;
    private ImageButton button1;
    private ImageView imageView;
    private TextView city;
    private TextView temp;
    double longitude;
    double latitude;
    String weather_url;

    String inDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_home, container, false);
        text = (TextView) view.findViewById(R.id.weathertext);
        city = (TextView) view.findViewById(R.id.CityName);
        temp = (TextView) view.findViewById(R.id.text2);
        button1 = (ImageButton) view.findViewById(R.id.locationbutton);
        imageView = (ImageView) view.findViewById(R.id.WeatherImage);
        final LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        final String locationProvider = LocationManager.NETWORK_PROVIDER;


        ViewPager viewPager4 = (ViewPager) view.findViewById(R.id.myViewPager4);
        ViewPagerAdapter4 viewPagerAdapter4 = new ViewPagerAdapter4(getActivity());
        viewPager4.setAdapter(viewPagerAdapter4);


        Button deep = (Button) view.findViewById(R.id.wdeep);
        deep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menu_home_wdeep.class);
                startActivity(intent);
            }

        });

        Button btn_allcodi = (Button) view.findViewById(R.id.allhomecodi);

        btn_allcodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menu_home_allcodi.class);
                startActivity(intent);
            }
        });

        Button btn_backhome = (Button) view.findViewById(R.id.comebackhome);

        btn_backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menu_home_backhome.class);
                startActivity(intent);
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {

                    //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location location = lm.getLastKnownLocation(locationProvider);

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();


                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);

                    weather_url = "http://api.openweathermap.org/data/2.5/weather?lat="
                            +latitude + "&lon=" + longitude + "&APPID=7766574e1e96581f1b16894c1deaaea9";

                    //symbolView.setImageUrl(); = "http://openweathermap.org/img/w/"+resp.weather[0].icon+".png";
                    new GetXMLTask().execute();
                }
                //Document doInBackground(weather_url);
            }
        });
        return view;
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onProviderDisabled(String provider) {
        }
    };


    class GetXMLTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... datas) {

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(weather_url).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while ((readed = in.readLine()) != null) {
                        JSONObject jObject = new JSONObject(readed);
                        return jObject;
                    }
                } else {
                    return null;
                }
                return null;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(JSONObject result) {
            //Log.i(TAG,result.toString());
            if (result != null) {
                String main_weather = "";
                String weather = "";
                String iconName = "";
                String nowTemp = "";
                String maxTemp = "";
                String minTemp = "";
                String sys="";
                String cityname="";
                String likeTemp="";
                String humidity = "";
                String speed = "";
                String description = "";

                try {
                    cityname=result.getString("name");
                    //iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon");
                    nowTemp = result.getJSONObject("main").get("temp").toString();
                    likeTemp = result.getJSONObject("main").get("feels_like").toString();
                    //humidity = result.getJSONObject("main").getString("humidity");
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    speed = result.getJSONObject("wind").getString("speed");
                    main_weather = result.getJSONArray("weather").getJSONObject(0).get("main").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                double likeTemp2 = Double.parseDouble(likeTemp);
                likeTemp2-=273.15;
                double nowTemp2 = Double.parseDouble(nowTemp);
                nowTemp2-=273.15;
                double minTemp2 = Double.parseDouble(minTemp);
                minTemp2-=273.15;
                double maxTemp2 = Double.parseDouble(maxTemp);
                maxTemp2-=273.15;
                main_weather = transferWeather(main_weather);
                //final String cityweather = cityname;
                /*final String msg = cityname+"\n"+ main_weather + "\n습도" + speed + "m/s" + "\n체감온도:" + (int)likeTemp2 +"\n온도현재:" + (int)nowTemp2 +
                        "\n최저:" + (int)minTemp2 + "/최고:" + (int)maxTemp2;*/
                final String Mtemp = (int)maxTemp2+"/"+(int)minTemp2;
                final String Ntemp = (int)nowTemp2 + "";
                city.setText(cityname);
                text.setText(Mtemp);
                temp.setText(Ntemp);
            }
        }

        private String transferWeather(String weather) {
            weather = weather.toLowerCase();

            if (weather.equals("thunder")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.thunder));
                return "번개";
            } else if (weather.equals("drizzle")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.light_rain));
                return "소나기";
            }else if (weather.equals("rain")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.light_rain));
                return "비";
            } else if (weather.equals("snow")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.snowfall));
                return "눈";
            } else if (weather.equals("haze")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.fog));
                return "안개";
            }else if (weather.equals("mist,smoke,dust,fog,sand,ash,squall,tornado")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.fog));
                return "안개";
            }
            else if (weather.equals("clear")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.sun));
                return "맑음";
            } else if (weather.equals("clouds")) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.many_cloud));
                return "구름 많음";
            }
            return "";
        }
    };
}


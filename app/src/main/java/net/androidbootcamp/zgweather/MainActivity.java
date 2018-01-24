package net.androidbootcamp.zgweather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import net.androidbootcamp.zgweather.WeatherFile.Weather;

import org.json.JSONException;

public class MainActivity extends Activity {


    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private TextView cityName;
    private TextView desc;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;

    private TextView hum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String city = "Seattle";
        final String city1 = "New Delhi";
        final String city2 = "Honolulu";
        final String city3 ="Tokyo";

        spinner=(Spinner)findViewById(R.id.spinner);
        adapter=ArrayAdapter.createFromResource(this, R.array.CITY, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        cityName = (TextView) findViewById(R.id.city);
        desc = (TextView) findViewById(R.id.desc);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDeg = (TextView) findViewById(R.id.windDeg);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(spinner.getSelectedItem().toString().equals("Seattle"))
                {
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{city});
                }
                else if (spinner.getSelectedItem().toString().equals("New Delhi"))
                {
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{city1});
                }
                else if (spinner.getSelectedItem().toString().equals("Honolulu")){
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{city2});
                }
                else if (spinner.getSelectedItem().toString().equals("Tokyo")){
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{city3});
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);



            cityName.setText(weather.location.getCity() + "," + weather.location.getCountry());
            desc.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() * 9 / 5 - 459.67)) + (char)0x00B0 + "F");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + (char) 0x00B0);

        }
    }
}

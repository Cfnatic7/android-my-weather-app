package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText cityEditText;

    public static String apiKey = "9SBP5FA7VTPNJR8ASV82RE3HU";
    private Button addCityButton;
    private ListView cityListView;

    private ArrayList<String> cityList;
    private ArrayAdapter<String> cityListAdapter;

    private MainActivity mainActivity = this;

    public static int temperatureUnitId = R.id.action_celsius;

    public static int speedUnitId = R.id.action_kmh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        addCityButton = findViewById(R.id.addCityButton);
        cityListView = findViewById(R.id.cityListView);

        cityList = new ArrayList<>();
        cityListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityList);
        cityListView.setAdapter(cityListAdapter);

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityEditText.getText().toString();
                if (!isConnectedToInternet()) {
                    Toast.makeText(mainActivity, "Can't add city when there is no internet connection.", Toast.LENGTH_LONG).show();
                }
                else if (!cityName.isEmpty()) {
                    new CityNameValidator(new CityNameValidator.OnValidationResultListener() {
                        @Override
                        public void onValidationResult(Boolean isValid) {
                            if (isValid) {
                                addCityToList(cityName);
                                cityListAdapter.notifyDataSetChanged();
                                cityEditText.getText().clear();
                            } else {
                                Toast.makeText(mainActivity, "Location specified is incorrect!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute(cityName);
                }
            }
        });

        cityListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String removedCity = cityList.remove(position);

                deleteWeatherDataFile(removedCity);

                cityListAdapter.notifyDataSetChanged();

                return true;
            }
        });

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = cityListAdapter.getItem(position);
                WeatherData weatherData = WeatherUtils.getWeatherDataFromFile(MainActivity.this, cityName);

                TabLayout tabLayout = findViewById(R.id.tabLayout);
                ViewPager2 viewPager = findViewById(R.id.viewPager);
                WeatherPagerAdapter weatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), getLifecycle(), weatherData);

                viewPager.setAdapter(weatherPagerAdapter);
                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Weather");
                                break;
                            case 1:
                                tab.setText("Additional information");
                                break;
                            case 2:
                                tab.setText("Forecast");
                                break;
                            default:
                                throw new IllegalStateException("Invalid tab position: " + position);
                        }
                    }
                });
                tabLayoutMediator.attach();
            }
        });


        cityList = new ArrayList<>(loadSavedCities());
        cityListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityList);
        cityListView.setAdapter(cityListAdapter);



        if (isConnectedToInternet()) {
            fetchAndSaveWeatherData(cityList);
        } else {
            Toast.makeText(this, "No internet connection. Loading saved weather data (may be outdated).", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchAndSaveWeatherData(List<String> cityList) {
        for (String city : cityList) {
            new Thread(() -> {
                try {
                    URL url = new URL("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + city + "/" + LocalDate.now(ZoneId.systemDefault()) + "/" + LocalDate.now(ZoneId.systemDefault()).plusDays(3) + "?unitGroup=metric&key=" + apiKey);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    String fileName = city + ".json";
                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    fos.write(stringBuilder.toString().getBytes());
                    fos.close();

                    bufferedReader.close();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private boolean isCityNameValid(String city) {
        try {
            URL url = new URL("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + city + "/" + LocalDate.now(ZoneId.systemDefault()) + "/" + LocalDate.now(ZoneId.systemDefault()).plusDays(3) + "?unitGroup=metric&key=" + apiKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return !stringBuilder.toString().contains("Invalid location found");
        } catch (IOException e) {
            return false;
        }
    }

    private void addCityToList(String city) {
        if (!cityList.contains(city)) {
            cityList.add(city);

            if (isConnectedToInternet()) {
                List<String> newCityList = new ArrayList<>();
                newCityList.add(city);
                fetchAndSaveWeatherData(newCityList);
            } else {
                Toast.makeText(this, "No internet connection. Unable to fetch weather data.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void deleteWeatherDataFile(String cityName) {
        String fileName = cityName + ".json";
        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    private void showEditCityDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit City");


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(cityList.get(position));
        builder.setView(input);


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCityName = input.getText().toString().trim();
                String oldCityName = cityList.get(position);

                if (newCityName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "City name cannot be empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (cityList.contains(newCityName) && !cityList.get(position).equals(newCityName)) {
                    Toast.makeText(MainActivity.this, "City already exists in the list.", Toast.LENGTH_LONG).show();
                    return;
                }

                deleteWeatherDataFile(oldCityName);


                cityList.set(position, newCityName);
                cityListAdapter.notifyDataSetChanged();


                if (isConnectedToInternet()) {
                    List<String> updatedCityList = new ArrayList<>();
                    updatedCityList.add(newCityName);
                    fetchAndSaveWeatherData(updatedCityList);
                } else {
                    Toast.makeText(MainActivity.this, "No internet connection. Unable to fetch weather data.", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            for(String city : cityList) {
                refreshWeatherData(city);
            }
            return true;
        }
        else if (id == R.id.action_celsius || id == R.id.action_fahrenheit || id == R.id.action_kmh || id == R.id.action_ms) {
            if (id == R.id.action_celsius) {
                temperatureUnitId = R.id.action_celsius;
            }
            else if(id == R.id.action_fahrenheit) {
                temperatureUnitId = R.id.action_fahrenheit;
            }
            else if(id == R.id.action_kmh) {
                speedUnitId = R.id.action_kmh;
            }
            else {
                speedUnitId = R.id.action_ms;
            }
            refreshView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> loadSavedCities() {
        List<String> savedCities = new ArrayList<>();
        File filesDir = getFilesDir();
        File[] jsonFiles = filesDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });

        for (File jsonFile : jsonFiles) {
            String cityName = jsonFile.getName().replace(".json", "");
            savedCities.add(cityName);
        }

        return savedCities;
    }

    private void refreshWeatherData(String cityName) {
        new Thread(() -> {
            try {
                URL url = new URL("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + cityName + "/" + LocalDate.now(ZoneId.systemDefault()) + "/" + LocalDate.now(ZoneId.systemDefault()).plusDays(3) + "?unitGroup=metric&key=" + apiKey);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                String fileName = cityName + ".json";
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(stringBuilder.toString().getBytes());
                fos.close();

                bufferedReader.close();
                urlConnection.disconnect();

                // Przetwórz nowo pobrane dane
                WeatherData newWeatherData = WeatherUtils.getWeatherDataFromFile(MainActivity.this, cityName);

                // Aktualizuj fragmenty za pomocą nowych danych
                runOnUiThread(() -> {
                    TabLayout tabLayout = findViewById(R.id.tabLayout);
                    ViewPager2 viewPager = findViewById(R.id.viewPager);
                    WeatherPagerAdapter weatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), getLifecycle(), newWeatherData);

                    viewPager.setAdapter(weatherPagerAdapter);
                    TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            switch (position) {
                                case 0:
                                    tab.setText("Weather");
                                    break;
                                case 1:
                                    tab.setText("Additional information");
                                    break;
                                case 2:
                                    tab.setText("Forecast");
                                    break;
                                default:
                                    throw new IllegalStateException("Invalid tab position: " + position);
                            }
                        }
                    });
                    tabLayoutMediator.attach();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void refreshView() {
        for (String city : cityList) {
            WeatherData weatherData = WeatherUtils.getWeatherDataFromFile(MainActivity.this, city);

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            WeatherPagerAdapter weatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), getLifecycle(), weatherData);

            viewPager.setAdapter(weatherPagerAdapter);
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    switch (position) {
                        case 0:
                            tab.setText("Weather");
                            break;
                        case 1:
                            tab.setText("Additional information");
                            break;
                        case 2:
                            tab.setText("Forecast");
                            break;
                        default:
                            throw new IllegalStateException("Invalid tab position: " + position);
                    }
                }
            });
            tabLayoutMediator.attach();
        }
    }

}
package com.github.energion.energionandroid;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.energion.energionandroid.manual.ManualFragment;
import com.github.energion.energionandroid.model.Day;
import com.github.energion.energionandroid.model.Hour;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ManualFragment.OnFragmentInteractionListener {
  private SectionsPagerAdapter pagerAdapter;

  private ViewPager pager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    pagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

    pager = (ViewPager) findViewById(R.id.container);
    pager.setAdapter(pagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(pager);

    getDaysFromServer();
  }

  private List<Day> getDaysFromServer() {
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create();

    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://46.101.202.63/")
        .build();

    BackendService service = retrofit.create(BackendService.class);
    Call<List<Day>> dayList = service.listDays();

    dayList.enqueue(new Callback<List<Day>>() {
      @Override
      public void onResponse(Call<List<Day>> call, Response<List<Day>> response) {
        List<Day> list = response.body();

        for (Day day : list) {
          Log.d("apiResponse", "date: " + day.getDate());

          for (Hour hour : day.getHours()) {
            Log.d("apiResponse", "hour: " + hour.getHour() + " and price is " + hour.getPrice());
          }
        }
      }

      @Override
      public void onFailure(Call<List<Day>> call, Throwable t) {
        Log.e("TAG", "onFailure" + t.getLocalizedMessage());
      }
    });
    return null;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onFragmentInteraction(Uri uri) {
  }
}
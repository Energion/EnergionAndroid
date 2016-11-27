package com.github.energion.energionandroid;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.energion.energionandroid.manual.ManualFragment;
import com.github.energion.energionandroid.model.Day;
import com.github.energion.energionandroid.settings.SettingsActivity;
import com.github.energion.energionandroid.utils.CustomViewPager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ManualFragment.OnFragmentInteractionListener, DataObservable {
  private SectionsPagerAdapter pagerAdapter;
  private List<DataObserver> observerList;

  private ViewPager pager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    pagerAdapter = new SectionsPagerAdapter(this, this, getSupportFragmentManager());

    pager = (CustomViewPager) findViewById(R.id.container);
    pager.setAdapter(pagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(pager);

    observerList = new ArrayList<>();

    getDaysFromServer();

    scheduleNotification(getNotification("Hello"), 10000);
  }

  private List<Day> getDaysFromServer() {
    Gson gson = new GsonBuilder()
        .setDateFormat("dd-MM-yyyy")
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

        for(DataObserver observer : observerList) {
          observer.update(list);
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
      Intent intent = new Intent(this, SettingsActivity.class);
      this.startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onFragmentInteraction(Uri uri) {
  }

  @Override
  public void subscribe(DataObserver observer) {
    observerList.add(observer);
  }

  @Override
  public void unsubscribe(DataObserver observer) {
    observerList.remove(observer);
  }

  private void scheduleNotification(Notification notification, int delay) {

    Intent notificationIntent = new Intent(this, NotificationPublisher.class);
    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    long futureInMillis = SystemClock.elapsedRealtime() + delay;
    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
  }

  private Notification getNotification(String content) {
    Notification.Builder builder = new Notification.Builder(this);
    builder.setContentTitle("Scheduled Notification");
    builder.setContentText(content);
    builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
    builder.setPriority(Notification.PRIORITY_MAX);
    builder.setDefaults(Notification.DEFAULT_ALL);
    return builder.build();
  }
}
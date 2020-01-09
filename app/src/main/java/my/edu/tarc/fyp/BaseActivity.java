package my.edu.tarc.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


public class BaseActivity extends AppCompatActivity implements HistoryFragment.OnFragmentInteractionListener{

    RadioGroup radioGroup1;
    RadioButton buttonClicked;
    private SharedPreferences sharedPreferences;
    String logged;
    public static final String FILE_NAME = "my.edu.tarc.fyp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        /*mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);*/

        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        buttonClicked = (RadioButton)findViewById(R.id.appointment);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                Log.i("matching", "matching inside1 bro" + checkedId);
                switch (checkedId)
                {
                    case R.id.appointment:
                        Log.i("matching", "matching inside1 appointment" +  checkedId);
                        in=new Intent(getBaseContext(),AppointmentActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.service:
                        Log.i("matching", "matching inside1 service" + checkedId);

                        in = new Intent(getBaseContext(), ServiceActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.customer:
                        Log.i("matching", "matching inside1 customer" + checkedId);

                        in = new Intent(getBaseContext(),CustomerActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.calendar:
                        Log.i("matching", "matching inside1 calendar" + checkedId);
                        in = new Intent(getBaseContext(), CalendarActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile:
                        Log.i("matching", "matching inside1 profile" + checkedId);
                        in = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.item1) {
            //Toast.makeText(BaseActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            //Intent myIntent = new Intent(this, HistoryActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            //this.startActivity(myIntent);
            Intent myIntent = new Intent(getBaseContext(), HistoryActivity.class);
            startActivity(myIntent);
            //findViewById(R.id.rvAppointment).setVisibility(View.GONE);
            //fragment to expand appointment details

            /*FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new HistoryFragment();
            fragmentTransaction.replace(R.id.fragment_base, fragment);
            //fragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;
        }*/

        if(id == R.id.item3)
        {

            sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
            sharedPreferences.edit().remove("logged").commit();
            Toast.makeText(BaseActivity.this, "Successfully Logout", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
            findViewById(R.id.rvAppointment).setVisibility(View.VISIBLE);
        }
    }
}

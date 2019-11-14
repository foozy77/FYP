package my.edu.tarc.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class BaseActivity extends AppCompatActivity {

    RadioGroup radioGroup1;
    RadioButton buttonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


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
                    /*case R.id.listing:
                        Log.i("matching", "matching inside1 listing" + checkedId);
                        in = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;*/
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
}

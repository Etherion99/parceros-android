package com.aciv.parceros.Activities.Results;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.aciv.parceros.Adapters.SimpleListAdapter;
import com.aciv.parceros.Models.Local.SelectText;
import com.aciv.parceros.Models.Local.SingleSelectText;
import com.aciv.parceros.R;

import java.util.List;

public class PickDurationActivity extends AppCompatActivity {
    private TextView hoursTV, minutesTV;
    private ListView hoursLV, minutesLV;
    private Button doneB;
    private ImageButton backIB;

    private Context context;
    private List<SelectText> hours, minutes;
    private SimpleListAdapter hoursAdapter, minutesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_duraction);

        hoursTV = findViewById(R.id.hoursTV);
        minutesTV = findViewById(R.id.minutesTV);
        hoursLV = findViewById(R.id.hoursLV);
        minutesLV = findViewById(R.id.minutesLV);
        doneB = findViewById(R.id.doneB);
        backIB = findViewById(R.id.backIB);

        context = this;

        hours = SelectText.generateList(getResources().getStringArray(R.array.hours));
        minutes = SelectText.generateList(getResources().getStringArray(R.array.minutes));

        hours.get(0).setSelected(true);
        minutes.get(0).setSelected(true);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("duration") && getIntent().getStringExtra("duration") != null){
                if(!getIntent().getStringExtra("duration").equals("")){
                    SingleSelectText.deselect(hours);
                    SingleSelectText.deselect(minutes);

                    String[] durationParts = getIntent().getStringExtra("duration").split(":");
                    hours.get(SingleSelectText.getByName(hours, durationParts[0])).setSelected(true);
                    minutes.get(SingleSelectText.getByName(minutes, durationParts[1])).setSelected(true);
                }
            }
        }

        hoursAdapter = new SimpleListAdapter(context, hours, Gravity.CENTER);
        minutesAdapter = new SimpleListAdapter(context, minutes, Gravity.CENTER);

        hoursLV.setAdapter(hoursAdapter);
        minutesLV.setAdapter(minutesAdapter);

        hoursLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleSelectText.deselect(hours);
                hours.get(position).setSelected(true);

                hoursAdapter.notifyDataSetChanged();
            }
        });

        minutesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleSelectText.deselect(minutes);
                minutes.get(position).setSelected(true);

                minutesAdapter.notifyDataSetChanged();
            }
        });

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent();
                response.putExtra("duration", hours.get(SingleSelectText.getSelected(hours)).getName() + ":" + minutes.get(SingleSelectText.getSelected(minutes)).getName());
                setResult(RESULT_OK, response);
                finish();
            }
        });
    }
}

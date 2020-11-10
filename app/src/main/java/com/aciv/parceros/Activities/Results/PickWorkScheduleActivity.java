package com.aciv.parceros.Activities.Results;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.aciv.parceros.Adapters.WorkSchedulesAdapter;
import com.aciv.parceros.Models.Database.Schedule;
import com.aciv.parceros.Models.Database.ScheduleDay;
import com.aciv.parceros.Models.Local.ScheduleItem;
import com.aciv.parceros.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PickWorkScheduleActivity extends AppCompatActivity {
    private Switch work247SW;
    private Button addMoreB, restBreaksB, doneB;
    private ListView workSchedulesLV;
    private ImageButton backIB;

    private Context context;
    private List<ScheduleItem> schedules = new ArrayList<>();
    private WorkSchedulesAdapter schedulesAdapter;
    private int rest_break = -1;
    private final int PICK_DAYS_CODE = 18;
    private final int PICK_REST_BREAKS_CODE = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_work_schedule);

        work247SW = findViewById(R.id.work247SW);
        addMoreB = findViewById(R.id.addMoreB);
        restBreaksB = findViewById(R.id.restBreaksB);
        workSchedulesLV = findViewById(R.id.workSchedulesLV);
        backIB = findViewById(R.id.backIB);
        doneB = findViewById(R.id.doneB);

        context = this;

        /*List<ScheduleDay>daysLS = new ArrayList<>();
        daysLS.add(new ScheduleDay(5));
        daysLS.add(new ScheduleDay(3));
        daysLS.add(new ScheduleDay(6));

        schedules.add(new ScheduleItem(new Schedule(5, 6, daysLS), true));//debug

        List<ScheduleDay>daysLS2 = new ArrayList<>();
        daysLS2.add(new ScheduleDay(2));
        daysLS2.add(new ScheduleDay(6));

        schedules.add(new ScheduleItem(new Schedule(10, 12, daysLS2), true));//debug*/

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("schedules")){
                List<Schedule> schedulesDB = new Gson().fromJson(getIntent().getStringExtra("schedules"), new TypeToken<List<Schedule>>(){}.getType());

                for(Schedule schedule: schedulesDB)
                    schedules.add(new ScheduleItem(schedule, true));
            }

            work247SW.setChecked(getIntent().getBooleanExtra("work_24_7", false));
            rest_break = getIntent().getIntExtra("rest_break", -1);

            if(rest_break != -1)
                restBreaksB.setTextColor(ContextCompat.getColor(context, R.color.accentGradient));
        }

        schedulesAdapter = new WorkSchedulesAdapter(context, schedules);
        workSchedulesLV.setAdapter(schedulesAdapter);

        restBreaksB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickWorkScheduleToPickRestBreaks = new Intent(context, PickRestBreaksActivity.class);
                pickWorkScheduleToPickRestBreaks.putExtra("rest_break", rest_break);
                startActivityForResult(pickWorkScheduleToPickRestBreaks, PICK_REST_BREAKS_CODE);
            }
        });

        work247SW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    work247SW.setTextColor(ContextCompat.getColor(context, R.color.accentGradient));
                else
                    work247SW.setTextColor(ContextCompat.getColor(context, R.color.text));

                for(ScheduleItem item: schedules){
                    item.setActivated(!work247SW.isChecked());
                }

                schedulesAdapter.notifyDataSetChanged();
            }
        });

        addMoreB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, PickWorkDaysActivity.class), PICK_DAYS_CODE);
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
                if(rest_break != -1){
                    if((work247SW.isChecked() || schedules.size() > 0)){
                        Intent response = new Intent();

                        List<Schedule> schedulesDB = new ArrayList<>();

                        for(ScheduleItem item: schedules)
                            schedulesDB.add(item.getSchedule());

                        if(!work247SW.isChecked())
                            response.putExtra("schedules", new Gson().toJson(schedulesDB));

                        response.putExtra("rest_break", rest_break);

                        setResult(RESULT_OK, response);
                        finish();
                    }else{
                        Toast.makeText(context, "Selecciona tu horas de trabajo o indícanos si trabajas 24/7", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context, "Selecciona tu tiempo de descanso ", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_DAYS_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null) {
                            List<ScheduleDay> days = new Gson().fromJson(data.getStringExtra("days"), new TypeToken<List<ScheduleDay>>(){}.getType());
                            String fromHour = data.getStringExtra("fromHour");
                            String toHour = data.getStringExtra("toHour");

                            schedules.add(new ScheduleItem(new Schedule(fromHour, toHour, days), !work247SW.isChecked()));
                            schedulesAdapter.notifyDataSetChanged();
                        }
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case PICK_REST_BREAKS_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        if(data != null){
                            rest_break = data.getIntExtra("rest_break", -1);
                            restBreaksB.setTextColor(ContextCompat.getColor(context, R.color.accentGradient));
                        }
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
}

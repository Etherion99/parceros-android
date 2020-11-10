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
import android.widget.Toast;

import com.aciv.parceros.Adapters.SimpleListAdapter;
import com.aciv.parceros.Models.Local.SelectText;
import com.aciv.parceros.Models.Local.SingleSelectText;
import com.aciv.parceros.R;

import java.util.List;

public class PickCancellationPolicyActivity extends AppCompatActivity {
    private ListView policiesLV;
    private Button doneB;
    private ImageButton backIB;

    private Context context;

    private List<SelectText> policies;
    SimpleListAdapter policiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_cancellation_policy);

        policiesLV = findViewById(R.id.policiesLV);
        doneB = findViewById(R.id.doneB);
        backIB = findViewById(R.id.backIB);

        context = this;

        policies = SelectText.generateList(getResources().getStringArray(R.array.canellation_policies));

        if(getIntent().getExtras() != null)
            if(getIntent().getIntExtra("cancellation", -1) != -1)
                policies.get(getIntent().getIntExtra("cancellation", -1)).setSelected(true);

        policiesAdapter = new SimpleListAdapter(context, policies, Gravity.CENTER);

        policiesLV.setAdapter(policiesAdapter);

        policiesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleSelectText.deselect(policies);
                policies.get(position).setSelected(true);
                policiesAdapter.notifyDataSetChanged();
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
                response.putExtra("cancellation", SingleSelectText.getSelected(policies));
                setResult(RESULT_OK, response);
                finish();
            }
        });
    }
}

package com.aciv.parceros.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.aciv.parceros.Models.Database.Phone;
import com.aciv.parceros.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

import java.util.concurrent.TimeUnit;

public class PhoneValidationActivity extends AppCompatActivity {
    private Button sendB, resendB, editPhoneB;
    private Spinner indicativeSP;
    private EditText phoneET;
    private ProgressBar countdownPB;
    private ConstraintLayout sendCodeCL, verifyCodeCL, codeOptsBCL;
    private CodeInputView codeCIV;

    FirebaseAuth authInstance;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallback;

    private Context context;

    private ArrayAdapter<CharSequence> indicativesAdapter;
    private String codeSent;
    private int countdown;
    private final int timeLimit = 60;
    private Phone newPhone = new Phone("", "");
    private Phone currentPhone = new Phone("", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validation);

        sendB = findViewById(R.id.sendB);
        indicativeSP = findViewById(R.id.indicativeSP);
        phoneET = findViewById(R.id.phoneET);
        countdownPB = findViewById(R.id.countdownPB);
        sendCodeCL = findViewById(R.id.sendCodeCL);
        verifyCodeCL = findViewById(R.id.verifyCodeCL);
        codeCIV = findViewById(R.id.codeCIV);
        codeOptsBCL = findViewById(R.id.codeOptsBCL);
        resendB = findViewById(R.id.reSendB);
        editPhoneB = findViewById(R.id.editPhoneB);

        authInstance = FirebaseAuth.getInstance();

        context = this;

        initVars();

        if(getIntent().getExtras() != null){
            Log.e("intent", "setting number");
            setCurrentNumber(getIntent().getExtras().getString("phone"));
        }else{
            Log.e("intent", "null");
        }

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCodeCL.setVisibility(View.GONE);
                verifyCodeCL.setVisibility(View.VISIBLE);

                newPhone.setIndicative(indicativeSP.getSelectedItem().toString());
                newPhone.setNumber(phoneET.getText().toString());

                sendAuthCode(newPhone.getFullNumber());
            }
        });

        codeCIV.addOnCompleteListener(new OnCodeCompleteListener() {
            @Override
            public void onCompleted(String code) {
                verifyAuthCode(code);
            }
        });

        resendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownPB.setProgress(100);

                codeOptsBCL.setVisibility(View.GONE);
                countdownPB.setVisibility(View.VISIBLE);

                newPhone.setIndicative(indicativeSP.getSelectedItem().toString());
                newPhone.setNumber(phoneET.getText().toString());

                sendAuthCode(newPhone.getFullNumber());
            }
        });

        editPhoneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownPB.setProgress(100);

                codeOptsBCL.setVisibility(View.GONE);
                countdownPB.setVisibility(View.VISIBLE);

                verifyCodeCL.setVisibility(View.GONE);
                sendCodeCL.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setCurrentNumber(String phone){
        currentPhone = new Gson().fromJson(phone, Phone.class);

        if(!currentPhone.getFullNumber().equals("")){
            indicativeSP.setSelection(indicativesAdapter.getPosition(currentPhone.getIndicative()));
            phoneET.setText(currentPhone.getNumber());

            Log.e("phone", currentPhone.getFullNumber());
        }
    }

    private void initVars(){
        indicativesAdapter = ArrayAdapter.createFromResource(context, R.array.indicatives, R.layout.spinner_item_simple);
        indicativeSP.setAdapter(indicativesAdapter);

        verificationCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("phone auth", "fail");
                Log.e("phone auth", e.toString());
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.e("phone auth", "code sent: " + s);
                codeSent = s;
                countdown = timeLimit;
                startCountdown();
                Toast.makeText(PhoneValidationActivity.this, "Código enviado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                Log.e("phone auth", "timeout: " + s);
            }
        };
    }

    private void sendAuthCode(String number){
        Log.e("number", number);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            timeLimit,
            TimeUnit.SECONDS,
            this,
            verificationCallback
        );
    }

    private void startCountdown(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                countdown--;
                countdownPB.setProgress(countdown*100/timeLimit);

                if(countdown > 0){
                    startCountdown();
                }else{
                    codeOptsBCL.setVisibility(View.VISIBLE);
                    countdownPB.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }

    private void verifyAuthCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        authInstance.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();

                            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if(task.isSuccessful()){
                                        Intent response = new Intent();
                                        response.putExtra("phone", new Gson().toJson(newPhone));
                                        setResult(RESULT_OK, response);
                                        finish();
                                    }else{
                                        Toast.makeText(PhoneValidationActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                        Log.e("token fail", task.getException().toString());
                                    }
                                }
                            });
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneValidationActivity.this, "Código de verificación inválido", Toast.LENGTH_SHORT).show();
                                codeCIV.setCode("");
                                codeCIV.setEditable(true);
                            }
                        }
                    }
                });
    }
}

package ru.smirnov.test.moretechapp.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ru.smirnov.test.moretechapp.R;

public class LoanApplicationActivity extends AppCompatActivity {
    private final static String TAG = LoanApplicationActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
    }
}

package ru.smirnov.test.moretechapp.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.models.UserLoan;
import ru.smirnov.test.moretechapp.views.fragments.LoanCalculatorFragment;

public class CalculateLoanActivity extends AppCompatActivity {
    private final static String TAG = CalculateLoanActivity.class.getName();

    public static String cost = "COST_CAR";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_loan);

        Intent intent = getIntent();
        int carCost = intent.getIntExtra(cost, 100000);

        UserLoan userLoan = new UserLoan();
        userLoan.cost = carCost;
        userLoan.term = 5;
        userLoan.initialFee = carCost * 0.3d;
        userLoan.residualPayment = 0;
        LoanCalculatorFragment loanCalculatorFragment = new LoanCalculatorFragment(userLoan);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.container_fr, loanCalculatorFragment);
        fragmentTransaction.commit();
    }
}

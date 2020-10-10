package ru.smirnov.test.moretechapp.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import ru.smirnov.test.moretechapp.databinding.FragmentCalculatorBinding;
import ru.smirnov.test.moretechapp.models.UserLoan;
import ru.smirnov.test.moretechapp.models.UserLoanResult;
import ru.smirnov.test.moretechapp.views.LoanApplicationActivity;

import static ru.smirnov.test.moretechapp.views.RecognitionResultActivity.JSON;

public class LoanCalculatorFragment extends Fragment {
    private final static String TAG = LoanCalculatorFragment.class.getName();

    private UserLoan userLoan;

    private UserLoanResult userLoanResult;

    private FragmentCalculatorBinding viewDataBinding;

    public static String contractRate = "contractRate";
    public static String loanAmount = "loanAmount";
    public static String loanTerm = "loanTerm";
    public static String carCost = "carCost";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        marketPlaceCars = MarketPlaceCars.getInstance();

        userLoan = ViewModelProviders.of(this).get(UserLoan.class);
        userLoanResult = ViewModelProviders.of(this).get(UserLoanResult.class);
        userLoan.cost = 1000000;
        calculateLoan();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = FragmentCalculatorBinding.inflate(inflater, container, false);
        viewDataBinding.setUserLoan(userLoan);
        viewDataBinding.setUserLoanResult(userLoanResult);
        viewDataBinding.continueLoan.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoanApplicationActivity.class);
            intent.putExtra(contractRate, userLoanResult.contractRate);
            intent.putExtra(loanAmount, userLoanResult.loanAmount);
            intent.putExtra(loanTerm, userLoanResult.term);
            intent.putExtra(carCost, userLoan.cost);
            startActivity(intent);
        });
        final int stepSize = 50000;
        viewDataBinding.userInitialFeeSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = ((int)Math.round((float)i/stepSize))*stepSize;
                seekBar.setProgress(i);
                viewDataBinding.userInitialFee.setText(String.format("%d.0 ₽",seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userLoan.initialFee = seekBar.getProgress();
                recalculate();
            }
        });

        viewDataBinding.userCostSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = ((int)Math.round((float)i/stepSize))*stepSize;
                seekBar.setProgress(i);
                viewDataBinding.userCarCost.setText(String.format("%d.0 ₽",seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userLoan.cost = seekBar.getProgress();
                recalculate();
            }
        });

        viewDataBinding.userLastPaymentSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = ((int)Math.round((float)i/stepSize))*stepSize;
                seekBar.setProgress(i);
                viewDataBinding.userLastPayment.setText(String.format("%d.0 ₽",seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userLoan.residualPayment = seekBar.getProgress();
                recalculate();
            }
        });

        viewDataBinding.userTermSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                viewDataBinding.userTerm.setText(String.format("%d",seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userLoan.term = seekBar.getProgress();
                recalculate();
            }
        });


//        viewDataBinding.countLoanBtn.setOnClickListener(view -> {
//            userLoan.cost = Double.parseDouble(viewDataBinding.userCarCost.getText().toString());
//            userLoan.initialFee = Double.parseDouble(viewDataBinding.userInitialFee.getText().toString());
//            userLoan.term = viewDataBinding.userTerm.getProgress();
//            userLoan.residualPayment = Double.parseDouble(viewDataBinding.userLastPayment.getText().toString());
//
//            calculateLoan();
//        });
        return viewDataBinding.getRoot();
    }

    private void calculateLoan() {
        Log.d(TAG, "Start sending request");
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(userLoan);
            String url = "https://gw.hackathon.vtb.ru/vtb/hackathon/calculate";
            Log.d(TAG, String.format("Send json: %s", json));
            RequestBody requestBody = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .addHeader("x-ibm-client-id", "6a1d19ecf31858bebf7f9038a170afb5")
                    .url(url)
                    .post(requestBody)
                    .build();

            new Thread(() -> {
                ResponseBody responseBody = null;
                try {
                    responseBody = client.newCall(request).execute().body();
                    String responseJson = responseBody.string();
                    Log.d(TAG, String.format("Response: %s", responseJson));
                    userLoanResult = objectMapper.readValue(responseJson, UserLoanResult.class);

                    getActivity().runOnUiThread(() -> {
                        viewDataBinding.userPayment.setText(userLoanResult.payment+" ₽");
                        viewDataBinding.userContractRate.setText(userLoanResult.contractRate+" %");
                        viewDataBinding.userLoanAmount.setText(userLoanResult.loanAmount+" ₽");
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void recalculate() {
        calculateLoan();
    }
}

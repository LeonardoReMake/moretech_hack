package ru.smirnov.test.moretechapp.views.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.databinding.ActivityLoanApplicationBinding;
import ru.smirnov.test.moretechapp.databinding.FragmentCalculatorBinding;
import ru.smirnov.test.moretechapp.models.CurrentUser;
import ru.smirnov.test.moretechapp.models.UserLoan;
import ru.smirnov.test.moretechapp.models.UserLoanResult;

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

    public LoanCalculatorFragment() {}

    public LoanCalculatorFragment(UserLoan userLoan) {
        this.userLoan = userLoan;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        marketPlaceCars = MarketPlaceCars.getInstance();

//        userLoan = ViewModelProviders.of(this).get(UserLoan.class);
        userLoanResult = ViewModelProviders.of(this).get(UserLoanResult.class);
        calculateLoan();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = FragmentCalculatorBinding.inflate(inflater, container, false);
        viewDataBinding.setUserLoan(userLoan);
        viewDataBinding.setUserLoanResult(userLoanResult);
        viewDataBinding.continueLoan.setOnClickListener(view -> {
            LoanApplicationDialog dialog = new LoanApplicationDialog(userLoanResult, userLoan);
            dialog.show(getParentFragmentManager(), "load_app");
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

        int stepSizeLast = 1;
        viewDataBinding.userLastPaymentSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = ((int)Math.round((float)i/stepSizeLast))*stepSizeLast;
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

        Toolbar toolbar = viewDataBinding.getRoot().findViewById(R.id.car_toolbar_calc);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(view -> getActivity().finish());
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

    public static class LoanApplicationDialog extends DialogFragment {

        private CurrentUser currentUser;
        private UserLoan userLoan;
        private UserLoanResult userLoanResult;

        private ActivityLoanApplicationBinding activityLoanApplicationBinding;

        public LoanApplicationDialog(UserLoanResult userLoanResult, UserLoan userLoan) {
            this.userLoan = userLoan;
            this.userLoanResult = userLoanResult;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            currentUser = ViewModelProviders.of(this).get(CurrentUser.class);
            currentUser.income = 100000;
            currentUser.fio = "Эдуард Геннадьевич";
            currentUser.gender = "муж.";
            currentUser.birth = "1981-11-01";
            currentUser.nationality = "Российская Федерация";
            currentUser.city = "г. Москва";
            currentUser.number = "+99999999999";
        }

        @Override
        public int getTheme() {
            return R.style.DialogTheme;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            activityLoanApplicationBinding = ActivityLoanApplicationBinding.inflate(inflater, container, false);
            activityLoanApplicationBinding.setCurrentUser(currentUser);

            activityLoanApplicationBinding.closeDialogLoanapp.setOnClickListener(view -> dismiss());
            activityLoanApplicationBinding.getRoot().findViewById(R.id.return_main_loan_application).setOnClickListener(view -> {
                dismiss();
            });
            activityLoanApplicationBinding.getRoot().findViewById(R.id.return_app_loan_application).setOnClickListener(view -> {
                dismiss();
            });
            int stepSize = 1000;
            activityLoanApplicationBinding.userIncomeSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    i = ((int)Math.round((float)i/stepSize))*stepSize;
                    seekBar.setProgress(i);
                    activityLoanApplicationBinding.userIncome.setText(String.format("%d",seekBar.getProgress()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    currentUser.income = seekBar.getProgress();
                }
            });

            activityLoanApplicationBinding.continueLoanApplication.setOnClickListener(view -> {
                activityLoanApplicationBinding.fieldsContainerLoanApp.setVisibility(View.GONE);
                activityLoanApplicationBinding.resultContainerLoanApp.setVisibility(View.VISIBLE);
                sendLoanApplication();
            });

            return activityLoanApplicationBinding.getRoot();
        }

        private void sendLoanApplication() {
            Log.d(TAG, "Start sending request");
            OkHttpClient client = new OkHttpClient();
            ObjectMapper objectMapper = new ObjectMapper();
//                String json = objectMapper.writeValueAsString(userLoan);
            String url = "https://gw.hackathon.vtb.ru/vtb/hackathon/carloan";
            String json = createJson();
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
                    Map<String, Object> responseMap = objectMapper.readValue(responseJson, Map.class);

                    getActivity().runOnUiThread(() -> {
                        String decision = (String) ((Map<String, Object>)((Map<String, Object>) responseMap.get("application")).get("decision_report")).get("application_status");
                        if (decision.equals("prescore_approved")) {
                            activityLoanApplicationBinding.progressIndicatorLoanApp.setVisibility(View.GONE);
                            activityLoanApplicationBinding.resultSuccContainerLoanApp.setVisibility(View.VISIBLE);
                        } else {
                            activityLoanApplicationBinding.progressIndicatorLoanApp.setVisibility(View.GONE);
                            activityLoanApplicationBinding.resultFailContainerLoanApp.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
        }

        @SuppressLint("DefaultLocale")
        private String createJson() {
            String json = "{\n" +
                    "  \"comment\": \"%s\",\n" +
                    "  \"customer_party\": {\n" +
                    "    \"email\": \"apetrovich@example.com\",\n" +
                    "    \"income_amount\": %d,\n" +
                    "    \"person\": {\n" +
                    "      \"birth_date_time\": \"1981-11-01\",\n" +
                    "      \"birth_place\": \"г. Воронеж\",\n" +
                    "      \"family_name\": \"Иванов\",\n" +
                    "      \"first_name\": \"Иван\",\n" +
                    "      \"gender\": \"male\",\n" +
                    "      \"middle_name\": \"Иванович\",\n" +
                    "      \"nationality_country_code\": \"RU\"\n" +
                    "    },\n" +
                    "    \"phone\": \"+99999999999\"\n" +
                    "  },\n" +
                    "  \"datetime\": \"2020-10-10T08:15:47Z\",\n" +
                    "  \"interest_rate\": 15.7,\n" +
                    "  \"requested_amount\": 300000,\n" +
                    "  \"requested_term\": 36,\n" +
                    "  \"trade_mark\": \"Nissan\",\n" +
                    "  \"vehicle_cost\": 600000\n" +
                    "}";
            json = String.format(json, currentUser.comment, currentUser.income);
            json = json.replace("\n", "");
            return json;
        }

    }


}

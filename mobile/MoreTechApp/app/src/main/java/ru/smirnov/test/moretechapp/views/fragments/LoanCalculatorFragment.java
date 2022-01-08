package ru.smirnov.test.moretechapp.views.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.databinding.ActivityLoanApplicationBinding;
import ru.smirnov.test.moretechapp.databinding.FragmentCalculatorBinding;
import ru.smirnov.test.moretechapp.models.CurrentUser;
import ru.smirnov.test.moretechapp.models.UserLoan;
import ru.smirnov.test.moretechapp.models.UserLoanResult;
import ru.smirnov.test.moretechapp.network.services.CalculateCreditService;
import ru.smirnov.test.moretechapp.network.services.CreditCalculateRequest;
import ru.smirnov.test.moretechapp.network.services.CreditCalculateResponse;

@AndroidEntryPoint
public class LoanCalculatorFragment extends Fragment {
    private final static String TAG = LoanCalculatorFragment.class.getName();

    private UserLoan userLoan;

    private UserLoanResult userLoanResult;

    private FragmentCalculatorBinding viewDataBinding;

    public static String contractRate = "contractRate";
    public static String loanAmount = "loanAmount";
    public static String loanTerm = "loanTerm";
    public static String carCost = "carCost";

    private boolean isApplicable = true;

    @Inject
    CalculateCreditService calculateCreditService;

    public LoanCalculatorFragment() {}

    public LoanCalculatorFragment(UserLoan userLoan) {
        this.userLoan = userLoan;
    }

    public LoanCalculatorFragment(UserLoan userLoan, boolean isApplicable) {
        this.userLoan = userLoan;
        this.isApplicable = isApplicable;
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

        if (!isApplicable) {
            viewDataBinding.continueLoan.setVisibility(View.GONE);
        }

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

        return viewDataBinding.getRoot();
    }

    private void calculateLoan() {

        CreditCalculateRequest creditCalculateRequest = new CreditCalculateRequest(userLoan.cost - userLoan.initialFee, userLoan.term);
        Call<CreditCalculateResponse> responseCall = calculateCreditService.calculate(creditCalculateRequest);

        Log.d(TAG, "Start sending request");
        responseCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CreditCalculateResponse> call, Response<CreditCalculateResponse> response) {
                if (!response.isSuccessful()) {
                    onFailure(call, new InternalError());
                    return;
                }

                CreditCalculateResponse creditCalculateResponse = response.body();

                if (creditCalculateResponse == null) {
                    onFailure(call, new InternalError());
                    return;
                }

                double payment = (creditCalculateResponse.getMonthPayment() < 0) ? 0 : creditCalculateResponse.getMonthPayment();
                double loanAmount = (creditCalculateResponse.getFullPayment() < 0) ? 0 : creditCalculateResponse.getFullPayment();

                viewDataBinding.userPayment.setText(String.format("%.2f ₽", payment));
                viewDataBinding.userContractRate.setText(String.format("%.2f%%", creditCalculateResponse.getRate()*100));
                viewDataBinding.userLoanAmount.setText(String.format("%.2f ₽", loanAmount));
            }

            @Override
            public void onFailure(Call<CreditCalculateResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Ошибка при подсчете кредита. Попробуйте позже", Toast.LENGTH_LONG).show();
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
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
//            currentUser.income = 100000;
//            currentUser.fio = "Эдуард Геннадьевич";
//            currentUser.gender = "муж.";
//            currentUser.birth = "1981-11-01";
//            currentUser.nationality = "Российская Федерация";
//            currentUser.city = "г. Москва";
//            currentUser.number = "+99999999999";
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
                activityLoanApplicationBinding.progressIndicatorLoanApp.setVisibility(View.VISIBLE);
                activityLoanApplicationBinding.fieldsContainerLoanApp.setVisibility(View.GONE);
//                activityLoanApplicationBinding.resultContainerLoanApp.setVisibility(View.VISIBLE);
                sendLoanApplication();
            });

            return activityLoanApplicationBinding.getRoot();
        }

        
        private void sendLoanApplication() {
            Log.d(TAG, "Start sending request");
//            OkHttpClient client = new OkHttpClient();
//            ObjectMapper objectMapper = new ObjectMapper();
////                String json = objectMapper.writeValueAsString(userLoan);
//            String url = "http://10.55.131.161:8080/rest/carloan";
//            String json = createJson();
//            Log.d(TAG, String.format("Send json: %s", json));
//            RequestBody requestBody = RequestBody.create(json, JSON);
//            Request request = new Request.Builder()
//                    .addHeader("x-ibm-client-id", "6a1d19ecf31858bebf7f9038a170afb5")
//                    .url(url)
//                    .post(requestBody)
//                    .build();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                activityLoanApplicationBinding.progressIndicatorLoanApp.setVisibility(View.GONE);
                activityLoanApplicationBinding.resultSuccContainerLoanApp.setVisibility(View.VISIBLE);
            }, 3000);

//            new Thread(() -> {
//                ResponseBody responseBody = null;
//                try {
//                    responseBody = client.newCall(request).execute().body();
//                    String responseJson = responseBody.string();
//                    Log.d(TAG, String.format("Response: %s", responseJson));
//                    Map<String, Object> responseMap = objectMapper.readValue(responseJson, Map.class);
//
//                    getActivity().runOnUiThread(() -> {
//
//                    });
//                } catch (IOException e) {
//                    getActivity().runOnUiThread(() -> {
//                        Toast.makeText(getActivity(),
//                                "Не удалось получить ответ от сервера", Toast.LENGTH_LONG).show();
//                    });
//
//                    e.printStackTrace();
//                }
//
//            }).start();
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

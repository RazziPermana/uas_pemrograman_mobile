package com.example.uastest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.DialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SelectTypeDialogFragment extends DialogFragment {

    private RadioGroup rgOptions;
    private RadioButton radioMobile, radioWeb;
    private Button btnSelectType;
    private OnOptionDialogListener onOptionDialogListener;
    private List<Task> taskList;
    private String selectedLogoUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_type, container, false);

        rgOptions = view.findViewById(R.id.rg_options);
        radioMobile = view.findViewById(R.id.radioMobile);
        radioWeb = view.findViewById(R.id.radioWeb);
        btnSelectType = view.findViewById(R.id.btnSelectType);

        fetchLogoData();

        btnSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = rgOptions.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    String selectedType = null;

                    if (checkedRadioButtonId == R.id.radioMobile) {
                        selectedType = radioMobile.getText().toString().trim();
                        selectedLogoUrl = taskList.get(0).getlogoUrl();
                    } else if (checkedRadioButtonId == R.id.radioWeb) {
                        selectedType = radioWeb.getText().toString().trim();
                        selectedLogoUrl = taskList.get(1).getlogoUrl();
                    }

                    if (onOptionDialogListener != null) {
                        onOptionDialogListener.onOptionChoosen(selectedType, selectedLogoUrl);
                    }

                    dismiss();
                }
            }
        });
        return view;
    }

    private void fetchLogoData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Task>> call = apiService.getTasks();

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    taskList = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface OnOptionDialogListener {
        void onOptionChoosen(String selectedType, String selectedLogoUrl);
    }

    public void setOnOptionDialogListener(OnOptionDialogListener onOptionDialogListener) {
        this.onOptionDialogListener = onOptionDialogListener;
    }
}

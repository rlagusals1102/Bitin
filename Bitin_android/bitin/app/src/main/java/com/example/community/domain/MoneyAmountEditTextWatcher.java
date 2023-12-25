package com.example.community.domain;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class MoneyAmountEditTextWatcher implements TextWatcher {
    private EditText moneyAmountEditText;
    private EditText coinAmountEditText;
    private double coinPrice;
    private DecimalFormat decimalFormat;
    private String moneyAmountText = "";

    public MoneyAmountEditTextWatcher(EditText moneyAmountEditText, EditText coinAmountEditText, double coinPrice) {
        this.moneyAmountEditText = moneyAmountEditText;
        this.coinAmountEditText = coinAmountEditText;
        this.coinPrice = coinPrice;
        this.decimalFormat = new DecimalFormat("#,###.########");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!moneyAmountText.equals(moneyAmountEditText.getText().toString())) {
            String text = charSequence.toString().replaceAll(",", "");
            if (text.toString().isEmpty()) {
                text = "0";
            }
            double moneyAmount = Math.ceil(Double.valueOf(text.toString()));
            moneyAmountText = decimalFormat.format(moneyAmount);
            moneyAmountEditText.setText(moneyAmountText);
            coinAmountEditText.setText(decimalFormat.format(moneyAmount / coinPrice));
            Selection.setSelection(coinAmountEditText.getText(), coinAmountEditText.getText().toString().length());
            Selection.setSelection(moneyAmountEditText.getText(), moneyAmountEditText.getText().toString().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

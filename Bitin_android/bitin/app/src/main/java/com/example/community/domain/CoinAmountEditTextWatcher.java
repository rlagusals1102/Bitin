package com.example.community.domain;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class CoinAmountEditTextWatcher implements TextWatcher {
    private EditText coinAmountEditText;
    private EditText moneyAmountEditText;
    private double coinPrice;
    private DecimalFormat decimalFormat;
    private String coinAmountText = "";

    public CoinAmountEditTextWatcher(EditText coinAmountEditText, EditText moneyAmountEditText, double coinPrice) {
        this.coinAmountEditText = coinAmountEditText;
        this.moneyAmountEditText = moneyAmountEditText;
        this.coinPrice = coinPrice;
        this.decimalFormat = new DecimalFormat("#,###.########");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!coinAmountText.equals(coinAmountEditText.getText().toString())) {
            String text = charSequence.toString().replaceAll(",", "");
            if (text.toString().isEmpty()) {
                text = "0";
            }
            double coinAmount = Double.valueOf(text.toString());
            coinAmountText = decimalFormat.format(coinAmount);
            coinAmountEditText.setText(coinAmountText);
            moneyAmountEditText.setText(decimalFormat.format(Math.ceil(coinPrice * coinAmount)));
            Selection.setSelection(coinAmountEditText.getText(), coinAmountEditText.getText().toString().length());
            Selection.setSelection(moneyAmountEditText.getText(), moneyAmountEditText.getText().toString().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

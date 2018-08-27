package com.cooltechworks.creditcarddesign.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import com.cooltechworks.checkoutflow.R;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.stripe.android.view.CardInputListener;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.StripeEditText;

public class TestActivity extends AppCompatActivity {

    private CreditCardView mCreditCardView;
    private CardInputWidget mCardInputWidget;
    private CardNumberEditText mCardNumberEt;
    private ExpiryDateEditText mExpiryDateEt;
    private StripeEditText mCvcNumberEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        mCreditCardView = (CreditCardView) findViewById(com.cooltechworks.creditcarddesign.R.id.credit_card_view);
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

        mCardNumberEt = (CardNumberEditText) mCardInputWidget.findViewById(com.stripe.android.R.id.et_card_number);

        mExpiryDateEt = (ExpiryDateEditText) findViewById(com.stripe.android.R.id.et_expiry_date);
        mCvcNumberEt = (StripeEditText) findViewById(com.stripe.android.R.id.et_cvc_number);

        setupListeners();
    }

    private void setupListeners() {
        mCardNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCreditCardView.setCardNumber(s.toString());

                String cardBrand = mCardNumberEt.getCardBrand();
                mCreditCardView.setCardBrand(cardBrand);
                mCreditCardView.paintCard(cardBrand);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCvcNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCreditCardView.setCVV(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mExpiryDateEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCreditCardView.setCardExpiry(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCardInputWidget.setCardInputListener(new CardInputListener() {
            @Override
            public void onFocusChange(String focusField) {
                if (focusField.equals("focus_cvc")) {
                    mCreditCardView.showBack();
                } else {
                    mCreditCardView.showFront();
                }
            }

            @Override
            public void onCardComplete() {
                mCreditCardView.showFront();
            }

            @Override
            public void onExpirationComplete() {
                mCreditCardView.showBack();
            }

            @Override
            public void onCvcComplete() {
            }

            @Override
            public void onPostalCodeComplete() {

            }
        });
    }
}

package com.cooltechworks.creditcarddesign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.StripeEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_BRAND;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_EXPIRY;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_NUMBER;


public class CardEditActivity extends AppCompatActivity implements CardInputWidget.CardInputListener {

    private CreditCardView mCreditCardView;
    private CardInputWidget mCardInputWidget;
    private CardNumberEditText mCardNumberEt;
    private ExpiryDateEditText mExpiryDateEt;
    private StripeEditText mCvcNumberEt;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_edit);

        mCreditCardView = (CreditCardView) findViewById(com.cooltechworks.creditcarddesign.R.id.credit_card_view);
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        mCardInputWidget.setCardInputListener(this);

        mCardNumberEt = (CardNumberEditText) mCardInputWidget.findViewById(com.stripe.android.R.id.et_card_number);

        mExpiryDateEt = (ExpiryDateEditText) findViewById(com.stripe.android.R.id.et_expiry_date);
        mCvcNumberEt = (StripeEditText) findViewById(com.stripe.android.R.id.et_cvc_number);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setupListeners();
        setupToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static Intent getNewIntent(Context context) {
        return new Intent(context, CardEditActivity.class);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void setupListeners() {
        mCardNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cardBrand = mCardNumberEt.getCardBrand();
                mCreditCardView.setCardBrand(cardBrand);
                mCreditCardView.setCardNumber(s.toString());
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

        mCvcNumberEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_DONE) {
                    Intent intent = new Intent();

                    intent.putExtra(EXTRA_CARD_CVV, mCreditCardView.getCVV());
                    intent.putExtra(EXTRA_CARD_EXPIRY, mCreditCardView.getExpiry());
                    intent.putExtra(EXTRA_CARD_NUMBER, mCreditCardView.getCardNumber());
                    intent.putExtra(EXTRA_CARD_BRAND, mCreditCardView.getBrand());

                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

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

    }

    @Override
    public void onExpirationComplete() {

    }

    @Override
    public void onCvcComplete() {

    }

    private void setupToolbar() {
        if (mToolbar == null) return;

        mToolbar.setTitle("NEW PAYMENT");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}

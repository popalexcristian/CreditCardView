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

import com.stripe.android.view.CardInputListener;
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


public class CardEditActivity extends AppCompatActivity implements CardInputListener {

    private static final String FOCUS_CVC = "focus_cvc";
    private static final String CARD_NUMBER = "focus_card";
    private static final String CARD_EXPIRATION = "focus_expiry";

    private CreditCardView mCreditCardView;
    private CardInputWidget mCardInputWidget;
    private CardNumberEditText mCardNumberEt;
    private ExpiryDateEditText mExpiryDateEt;
    private StripeEditText mCvcNumberEt;

    private String mFocus = CARD_NUMBER;
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

    private void setupToolbar() {
        if (mToolbar == null) return;

        mToolbar.setTitle("NEW PAYMENT");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onFocusChange(String focusField) {
        checkFlip(focusField);
    }

    private void checkFlip(String currentFocus) {
        if (currentFocus.equals(mFocus)) {
            return;
        }

        if (currentFocus.equals(FOCUS_CVC)) {
            switch (mFocus) {
                case CARD_NUMBER:
                    mCreditCardView.showBack();
                    break;
                case CARD_EXPIRATION:
                    mCreditCardView.showBack();
                    break;
                case FOCUS_CVC:
                    break;
            }
        } else {
            switch (mFocus) {
                case CARD_NUMBER:
                    break;
                case CARD_EXPIRATION:
                    break;
                case FOCUS_CVC:
                    mCreditCardView.showFront();
                    break;
            }
        }
        mFocus = currentFocus;
    }

    @Override
    public void onCardComplete() {
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
}

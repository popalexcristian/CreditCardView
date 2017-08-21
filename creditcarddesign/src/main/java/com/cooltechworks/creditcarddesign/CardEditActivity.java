package com.cooltechworks.creditcarddesign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.StripeEditText;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_EXPIRY;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_NUMBER;
import static io.card.payment.CardIOActivity.EXTRA_SCAN_RESULT;


public class CardEditActivity extends AppCompatActivity implements CardInputWidget.CardInputListener {

    private static final int MY_SCAN_REQUEST_CODE = 1;
    private CreditCardView mCreditCardView;
    private CardInputWidget mCardInputWidget;
    private CardNumberEditText mCardNumberEt;
    private ExpiryDateEditText mExpiryDateEt;
    private StripeEditText mCvcNumberEt;

    private static final String mFocus = "focus_cvc";
    private ImageView mCardScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_edit);

        mCreditCardView = (CreditCardView) findViewById(com.cooltechworks.creditcarddesign.R.id.credit_card_view);
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        mCardInputWidget.setCardInputListener(this);
        mCardScan = (ImageView) findViewById(R.id.card_scan_iv);

        mCardNumberEt = (CardNumberEditText) mCardInputWidget.findViewById(com.stripe.android.R.id.et_card_number);

        mExpiryDateEt = (ExpiryDateEditText) findViewById(com.stripe.android.R.id.et_expiry_date);
        mCvcNumberEt = (StripeEditText) findViewById(com.stripe.android.R.id.et_cvc_number);

        setupListeners();
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
                mCreditCardView.setCardNumber(s.toString());

                String cardBrand = mCardNumberEt.getCardBrand();
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

        mCvcNumberEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_DONE) {
                    Intent intent = new Intent();

                    intent.putExtra(EXTRA_CARD_CVV, mCreditCardView.getCVV());
                    intent.putExtra(EXTRA_CARD_EXPIRY, mCreditCardView.getExpiry());
                    intent.putExtra(EXTRA_CARD_NUMBER, mCreditCardView.getCardNumber());

                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        mCardScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(CardEditActivity.this, CardIOActivity.class);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);

                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(EXTRA_SCAN_RESULT);

                mCreditCardView.setCardNumber(scanResult.getFormattedCardNumber());
                mCardNumberEt.setText(scanResult.getFormattedCardNumber());

                if (scanResult.isExpiryValid()) {
                    int expirationYear = scanResult.expiryYear % 100;
                    if (scanResult.expiryMonth < 10) {
                        String expiryMonth = String.format("%02d", scanResult.expiryMonth);

                        mCreditCardView.setCardExpiry(String.format(getString(R.string.expiration_date_placeholder), expiryMonth, expirationYear));
                        mExpiryDateEt.setText(String.format(getString(R.string.expiration_date_placeholder), expiryMonth, expirationYear));
                    } else {
                        mCreditCardView.setCardExpiry(String.format(getString(R.string.expiration_date_placeholder), String.valueOf(scanResult.expiryMonth), expirationYear));
                        mExpiryDateEt.setText(String.format(getString(R.string.expiration_date_placeholder), String.valueOf(scanResult.expiryMonth), expirationYear));
                    }
                    onCardComplete();
                    onExpirationComplete();
                }

                if (scanResult.cvv != null) {
                    mCreditCardView.setCVV(scanResult.cvv);
                    mCvcNumberEt.setText(scanResult.cvv);
                }

            } else {
                Snackbar.make(findViewById(android.R.id.content), "Scan was cancelled", LENGTH_SHORT).show();
            }
        }
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
}

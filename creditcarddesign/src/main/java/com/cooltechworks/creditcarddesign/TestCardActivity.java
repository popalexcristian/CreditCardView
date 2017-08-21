package com.cooltechworks.creditcarddesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestCardActivity extends AppCompatActivity {

    private CreditCardView mCreditCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card);

        mCreditCardView = (CreditCardView) findViewById(R.id.credit_card_view);

    }
}

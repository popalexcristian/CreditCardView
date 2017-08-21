package com.cooltechworks.creditcarddesign;

import static com.cooltechworks.creditcarddesign.CardSelector.CardTypes.DEFAULT;

public class CardSelector {

    public static final int CVV_LENGHT_DEFAULT = 3;
    private static final int CVV_LENGHT_AMEX = 4;

    static CardTypes selectCard(String cardNumber) {
        if (cardNumber != null) {

            CardTypes cardType = CardTypes.getCardType(cardNumber.trim());
            if (cardType != null) {
                return cardType;
            } else {
                return DEFAULT;
            }
        }
        return DEFAULT;
    }

    enum CardTypes {
        VISA(R.drawable.visa_card, R.drawable.ic_visa_front, CVV_LENGHT_DEFAULT, "Visa"),
        VISAELECTRON(R.drawable.visa_card, 0, CVV_LENGHT_DEFAULT, "Visa Electron"),
        MASTERCARD(R.drawable.master_card, 0, CVV_LENGHT_DEFAULT, "Mastercard"),
        MAESTRO(R.drawable.master_card, 0, CVV_LENGHT_DEFAULT, "Maestro"),
        AMERICANEXPRESS(R.drawable.card_color_round_rect_green, 0, CVV_LENGHT_AMEX, "American Express"),
        DINERSCLUB(R.drawable.diners_club_card, 0, CVV_LENGHT_DEFAULT, "Diners Club"),
        DISCOVER(R.drawable.discover_card, 0, CVV_LENGHT_DEFAULT, "Discover"),
        JCB(R.drawable.jcb_card, 0, CVV_LENGHT_DEFAULT, "JCB"),
        ELO(R.drawable.card_color_round_rect_default, 0, CVV_LENGHT_DEFAULT, "ELO"),
        HIPERCARD(R.drawable.card_color_round_rect_default, 0, CVV_LENGHT_DEFAULT, "HiperCard"),
        UNIONPAY(R.drawable.card_color_round_rect_default, 0, CVV_LENGHT_DEFAULT, "UnionPay"),
        DEFAULT(R.drawable.card_color_round_rect_default, 0, CVV_LENGHT_DEFAULT, "");

        private final int mCardBackground;
        private final int mCardLogo;
        private final String mName;
        private final int mCvvLength;

        CardTypes(int cardBackground, int cardLogo, int cvvLength, String cardType) {
            mName = cardType;
            mCardBackground = cardBackground;
            mCardLogo = cardLogo;
            mCvvLength = cvvLength;
        }

        public boolean equalsName(String otherName) {
            return mName.equals(otherName);
        }

        public String toString() {
            return this.mName;
        }

        public int getCardBackground() {
            return mCardBackground;
        }

        public String getName() {
            return mName;
        }

        public int getCvvLength() {
            return mCvvLength;
        }

        public int getCardLogo() {
            return mCardLogo;
        }

        public static CardTypes getCardType(String input) {
            for (CardTypes cardType : CardTypes.values()) {
                if (input.trim().toLowerCase().equals(cardType.getName().trim().toLowerCase())) {
                    return cardType;
                }
            }
            return null;
        }
    }
}

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
        VISA(R.drawable.visa_card, CVV_LENGHT_DEFAULT, "Visa", R.drawable.ic_visa_card),
        VISAELECTRON(R.drawable.visa_card, CVV_LENGHT_DEFAULT, "Visa Electron", R.drawable.ic_visa_card),
        MASTERCARD(R.drawable.master_card, CVV_LENGHT_DEFAULT, "Mastercard", R.drawable.ic_mastercard_card),
        MAESTRO(R.drawable.master_card, CVV_LENGHT_DEFAULT, "Maestro", R.drawable.ic_mastercard_card),
        AMERICANEXPRESS(R.drawable.card_color_round_rect_green, CVV_LENGHT_AMEX, "American Express", R.drawable.ic_americanexpress_card),
        DINERSCLUB(R.drawable.diners_club_card, CVV_LENGHT_DEFAULT, "Diners Club", R.drawable.ic_dinersclub_card),
        DISCOVER(R.drawable.discover_card, CVV_LENGHT_DEFAULT, "Discover", R.drawable.ic_discover_card),
        JCB(R.drawable.jcb_card, CVV_LENGHT_DEFAULT, "JCB", R.drawable.ic_jcb_card),
        ELO(R.drawable.card_color_round_rect_default, CVV_LENGHT_DEFAULT, "ELO", 0),
        HIPERCARD(R.drawable.card_color_round_rect_default, CVV_LENGHT_DEFAULT, "HiperCard", 0),
        UNIONPAY(R.drawable.card_color_round_rect_default, CVV_LENGHT_DEFAULT, "UnionPay", 0),
        DEFAULT(R.drawable.card_color_round_rect_default, CVV_LENGHT_DEFAULT, "", 0);

        private final int mCardBackground;
        private final int mCardLogo;
        private final String mName;
        private final int mCvvLength;

        CardTypes(int cardBackground, int cvvLength, String cardType, int cardLogo) {
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

package com.fh.shop.api.member.common;

public class Constants {

    public static final String SECRET = "ui894o*43434ref+-dsd898s";
//商户id
    public static final String SELLER_ID = "2088621955792435";

    public static final Integer TOKEN_EXPIRE = 30*60;

    public static final String CURR_MEMBER = "member";

    public static final int REQUEST_ERROR = -1;

    public static final int ACTIVE = 1;

    public static final int OK = 1;

    public static final int STATUS_DOWN = 2;

    public static final String CART_JSON_FIELD = "cartJson";

    public static final String CART_COUNT_FIELD = "cartCount";

    public static final String NOT_DEFAULT = "0";

    public static final String DEFAULT = "1";

    public interface ORDER_STATUS{
        int WAIT_PAY=0;
        int PAY_SUCESS=10;
        int TRADE_CLOSE=40;

    }

}

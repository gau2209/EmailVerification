package com.gauu.emailverification.security.token;

import java.util.Calendar;
import java.util.Date;

public class TokenExpirationTime {

    private static final int EXPIRATION_TIME = 10;
    public static Date getExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(cal.getTime().getTime());
    }
}

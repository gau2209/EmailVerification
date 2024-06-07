package com.gauu.emailverification.utilities;

import jakarta.servlet.http.HttpServletRequest;

public class Urlutil {
    public static String getApplicationUrl(HttpServletRequest request) {
        String applicationUrl = request.getRequestURL().toString();
        return applicationUrl.replace(request.getServletPath(),"");
    }
}

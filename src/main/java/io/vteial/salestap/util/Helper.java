package io.vteial.salestap.util;

//import javax.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Helper {

    private static String domainPrefix = null;

    public static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

//    public static String getDomainPrefix(HttpServletRequest request, String profileId) {
//        if (domainPrefix != null) {
//            return domainPrefix;
//        }
//
//        domainPrefix = "http://" + request.getLocalAddr() + ":" + request.getLocalPort();
//
//        return domainPrefix;
//    }

}

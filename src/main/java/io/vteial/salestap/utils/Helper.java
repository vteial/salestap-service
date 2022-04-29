package io.vteial.salestap.utils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Helper {

    private static String domainPrefix = null;

    public static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public static Map<String, String> covertCVException(ConstraintViolationException cve) {
        Map<String, String> vmap = new HashMap<String, String>();
        for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
            vmap.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return vmap;
    }

}

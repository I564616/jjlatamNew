package com.jnj.b2b.storefront.security.filters;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilteredRequest extends HttpServletRequestWrapper {

    private static final Pattern PATTERN = Pattern.compile("<[^\\s]");

    public FilteredRequest(final ServletRequest request) {
        super((HttpServletRequest) request);
    }

    public String sanitize(final String input) {
        if (input == null) {
            return null;
        } else {
            final Matcher matcher = PATTERN.matcher(input);
            final StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, matcher.group().replaceAll("<", "< "));
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
    }

    @Override
    public String getParameter(final String paramName) {
        if (StringUtils.isBlank(paramName)) {
            return null;
        }
        String value = super.getParameter(paramName);
        if (StringUtils.isNotBlank(value)) {
            value = sanitize(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(final String paramName) {
        final String[] values = super.getParameterValues(paramName);
        if (values != null) {
            for (int index = 0; index < values.length; index++) {
                values[index] = sanitize(values[index]);
            }
        }
        return values;
    }
}

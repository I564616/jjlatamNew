package com.jnj.b2b.storefront.security.filters;

import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JnJXSSFilter extends GenericFilterBean {
    private static final Logger LOG = Logger.getLogger(JnJXSSFilter.class);

    private static final String XSS_ALLOWED_URL_PATTERNS = "xss.allowed.url.patterns";
    private static final String XSS_ALLOWED_URL_PATTERNS_FLAG = "xss.allowed.url.patterns.flag";

    private enum RequestAnalysis {
        MALICIOUS, LT_EXIST, SECURE
    }

    private String defaultXSSString;
    private String errorPage;
    private Pattern pattern;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        logRequest(req);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final Map<String, String[]> requestMap = new HashMap<>();
        if (null != request.getParameterMap()) {
            requestMap.putAll(request.getParameterMap());
        }

        final StringBuilder message = new StringBuilder();
        final RequestAnalysis analysis = getRequestAnalysis(requestMap, message);
        final HttpSession session = httpServletRequest.getSession();
        final ServletContext servletContext = session.getServletContext();

        switch (analysis) {
            case MALICIOUS:
                handleMaliciousRequest(request, response, httpServletRequest, message, servletContext);
                break;
            case LT_EXIST:
                chain.doFilter(new FilteredRequest(httpServletRequest), response);
                break;
            default:
                handleSecureRequest(response, chain, req, resp, httpServletRequest);
                break;
        }
    }

    private RequestAnalysis getRequestAnalysis(final Map<String, String[]> requestMap, final StringBuilder message) {
        RequestAnalysis analysis = RequestAnalysis.SECURE;
        final Iterator<Map.Entry<String, String[]>> mapIterator = requestMap.entrySet().iterator();
        message.append("Following fields has malicious script: ");
        while (mapIterator.hasNext()) {
            final Map.Entry<String, String[]> entry = mapIterator.next();
            final String[] value = entry.getValue();
            for (final String parameterValueString : value) {
                final Matcher matcher = pattern.matcher(parameterValueString);
                if (matcher.find()) {
                    message.append(entry.getKey()).append("-->").append(parameterValueString).append("----");
                    analysis = RequestAnalysis.MALICIOUS;
                } else if (!RequestAnalysis.MALICIOUS.equals(analysis) && (!parameterValueString.isEmpty()) && parameterValueString.contains("<")) {
                    analysis = RequestAnalysis.LT_EXIST;
                }
            }
        }
        return analysis;
    }

    private void handleSecureRequest(ServletResponse response, FilterChain chain, HttpServletRequest req, HttpServletResponse resp, HttpServletRequest httpServletRequest) throws IOException, ServletException {
        String requestURILowerCase = StringUtils.lowerCase(httpServletRequest.getRequestURI());
        if (requestURILowerCase.contains(".") && !requestURILowerCase.endsWith(".html")) {
            final String allowedUrlPatternsFlag = Config.getParameter(XSS_ALLOWED_URL_PATTERNS_FLAG);
            if (StringUtils.isNotBlank(allowedUrlPatternsFlag) && "true".equalsIgnoreCase(allowedUrlPatternsFlag)) {
                if (!isValidUrl(requestURILowerCase)) {
                    String redirectURL = getRedirectUrl(req.getServerName());
                    resp.sendRedirect(redirectURL);
                } else {
                    chain.doFilter(httpServletRequest, response);
                }
            } else {
                String redirectURL = getRedirectUrl(req.getServerName());
                resp.sendRedirect(redirectURL);
            }
        } else {
            chain.doFilter(httpServletRequest, response);
        }
    }

    private void handleMaliciousRequest(ServletRequest request, ServletResponse response, HttpServletRequest httpServletRequest, StringBuilder message, ServletContext servletContext) throws ServletException, IOException {
        LOG.error("Malicious Script Found:" + message.toString());
        final String ajaxHeader = httpServletRequest.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(ajaxHeader)) {
            ((HttpServletResponse) response).setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            final String redirectErrorPage = "/WEB-INF/views/desktop/pages/error/serverError.jsp";
            servletContext.getRequestDispatcher(redirectErrorPage).forward(request, response);
        }
    }

    private void logRequest(HttpServletRequest req) {
        LOG.debug("REQUEST SCHEME...." + req.getScheme());
        LOG.debug("REQUEST SERVER NAME...." + req.getServerName());
        LOG.debug("REQUEST CONTEXT PATH...." + req.getContextPath());
        LOG.debug("REQUEST SERVLET PATH...." + req.getServletPath());
        LOG.debug("REQUEST PATH INFO...." + req.getPathInfo());
    }

    protected boolean isValidUrl(final String servletPath) {
        if (servletPath != null) {
            final String allowedUrlPatterns = Config.getParameter(XSS_ALLOWED_URL_PATTERNS);
            final List<String> allowedUrls = Arrays.asList(allowedUrlPatterns.split("\\,"));
            for (final String allowedUrl : allowedUrls) {
                if (servletPath.contains(allowedUrl)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String getRedirectUrl(final String serverName) {
        return "https://" + serverName + "/store";
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        pattern = Pattern.compile(defaultXSSString, Pattern.CASE_INSENSITIVE);

    }

    public void setDefaultXSSString(final String defaultXSSString) {
        this.defaultXSSString = defaultXSSString;
    }

    public String getDefaultXSSString() {
        return defaultXSSString;
    }

    public void setErrorPage(final String errorPage) {
        this.errorPage = errorPage;
    }

    public String getErrorPage() {
        return errorPage;
    }

}

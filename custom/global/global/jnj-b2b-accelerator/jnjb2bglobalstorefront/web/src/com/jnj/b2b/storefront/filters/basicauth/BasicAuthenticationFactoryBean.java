package com.jnj.b2b.storefront.filters.basicauth;


import org.springframework.beans.factory.config.AbstractFactoryBean;

public class BasicAuthenticationFactoryBean extends AbstractFactoryBean<BasicAuthenticationFilter> {
    private String username;
    private String password;
    private boolean activate;

    public BasicAuthenticationFactoryBean(final String username, final String password, final boolean activate) {
        this.username = username;
        this.password = password;
        this.activate = activate;
    }

    @Override
    public Class<?> getObjectType() {
        return BasicAuthenticationFilter.class;
    }

    @Override
    protected BasicAuthenticationFilter createInstance() {
        if(activate) {
            return new BasicAuthenticationFilter(username, password);
        }
        return new DummyBasicAuthenticationFilter();
    }
}

package com.jnj.b2b.loginaddon.recaptcha;

public interface HttpLoader
{

	public String httpPost(String url, String postdata);

	public String httpGet(String url);
}

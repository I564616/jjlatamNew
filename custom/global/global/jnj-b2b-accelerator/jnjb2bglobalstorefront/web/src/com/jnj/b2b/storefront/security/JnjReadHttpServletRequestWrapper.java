package com.jnj.b2b.storefront.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

/**
 * Wrapper for HttpServletRequest which allows reading the body from the post request multiple times. Without the
 * wrapper, the input stream is closed after the body content is read.
 */
public class JnjReadHttpServletRequestWrapper  extends HttpServletRequestWrapper {
	private final String body;
	
	public JnjReadHttpServletRequestWrapper(HttpServletRequest request)  throws IOException{
		super(request);		
        body = IOUtils.toString(request.getInputStream());
	}
	@Override
	 public ServletInputStream getInputStream() throws IOException {
	   final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
	   ServletInputStream servletInputStream = new ServletInputStream() {
	     public int read() throws IOException {
	       return byteArrayInputStream.read();
	     }

		@Override
		public boolean isFinished() {
			return byteArrayInputStream.available() == 0;
		}

		@Override
		public boolean isReady() {
			
			return true;
		}

		@Override
		public void setReadListener(final ReadListener readListener)
		{
			throw new UnsupportedOperationException("ServletInputStream.setReadListener not implemented");
		}
	   };
	   return servletInputStream;
	 }
	
	 @Override
	 public BufferedReader getReader() throws IOException {
	   return new BufferedReader(new InputStreamReader(this.getInputStream()));
	 }

	 public String getBody() {
	   return this.body;
	 }

}

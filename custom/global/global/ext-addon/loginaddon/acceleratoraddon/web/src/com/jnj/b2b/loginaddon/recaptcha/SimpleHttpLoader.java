package com.jnj.b2b.loginaddon.recaptcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


public class SimpleHttpLoader implements HttpLoader
{

	public String httpGet(final String urlS)
	{
		InputStream in = null;
		URLConnection connection = null;
		try
		{
			final URL url = URI.create(urlS).toURL();
			connection = url.openConnection();

			// jdk 1.4 workaround
			setJdk15Timeouts(connection);

			in = connection.getInputStream();

			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final byte[] buf = new byte[1024];
			while (true)
			{
				final int rc = in.read(buf);
				if (rc <= 0)
				{
					break;
				}
				else
				{
					bout.write(buf, 0, rc);
				}
			}

			// return the generated javascript.
			return bout.toString();
		}
		catch (final IOException e)
		{
			throw new ReCaptchaException("Cannot load URL: " + e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (final Exception e)
			{
				// swallow.
			}
		}
	}

	public String httpPost(final String urlS, final String postdata)
	{
		InputStream in = null;
		URLConnection connection = null;
		try
		{
			final URL url = URI.create(urlS).toURL();
			connection = url.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);

			setJdk15Timeouts(connection);

			final OutputStream out = connection.getOutputStream();
			out.write(postdata.getBytes());
			out.flush();

			in = connection.getInputStream();

			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final byte[] buf = new byte[1024];
			while (true)
			{
				final int rc = in.read(buf);
				if (rc <= 0)
				{
					break;
				}
				else
				{
					bout.write(buf, 0, rc);
				}
			}

			out.close();
			in.close();

			// return the generated javascript.
			return bout.toString();
		}
		catch (final IOException e)
		{
			throw new ReCaptchaException("Cannot load URL: " + e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (final Exception e)
			{
				// swallow.
			}
		}
	}

	/**
	 * Timeouts are new from JDK1.5, handle it generic for JDK1.4 compatibility.
	 * 
	 * @param connection
	 */
	protected void setJdk15Timeouts(final URLConnection connection)
	{
		try
		{
			final Method readTimeoutMethod = connection.getClass().getMethod("setReadTimeout", new Class[]
			{ Integer.class });
			final Method connectTimeoutMethod = connection.getClass().getMethod("setConnectTimeout", new Class[]
			{ Integer.class });
			if (readTimeoutMethod != null)
			{
				readTimeoutMethod.invoke(connection, new Object[]
				{ Integer.valueOf(10000) });
				System.out.println("Set timeout.");
			}
			if (connectTimeoutMethod != null)
			{
				connectTimeoutMethod.invoke(connection, new Object[]
				{ Integer.valueOf(10000) });
				System.out.println("Set timeout.");
			}
		}
		catch (final Exception e)
		{
			// swallow silently
		}
	}


}

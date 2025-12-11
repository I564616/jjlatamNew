package com.gt.pac.aera;

/**
 * Created 12:19 13 February 2020.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class PacHiveException extends Exception
{
	public PacHiveException()
	{
	}

	public PacHiveException(String message)
	{
		super(message);
	}

	public PacHiveException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PacHiveException(Throwable cause)
	{
		super(cause);
	}

	public PacHiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.util;


import java.lang.reflect.Method;
import java.util.Comparator;


/**
 * @author Accenture
 * @version 1.0
 * Comparator for sorting logic
 */
/**
 * A comparator to sort on the specified field of a given class.
 * 
 * Reflection is used to retrieve the data to be sorted, therefore you must provide the Class and the method name within
 * the class that will be used to retrieve the data.
 * 
 * Several sort properties can be set:
 * 
 * a) ascending (default true) b) ignore case (default true) c) nulls last (default true)
 */
public class JnjObjectComparator implements Comparator
{
	private static final Class[] EMPTY_CLASS_ARRAY = new Class[] {};
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};
	private Method method;
	private boolean isAscending;
	private boolean isIgnoreCase;
	private boolean isNullsLast = true;



	/*
	 * Sort using default sort properties
	 */
	public JnjObjectComparator(final Class beanClass, final String methodName)
	{
		//	this.methodName = methodName;
		this.isNullsLast = true;
		//this(beanClass, methodName, true);
		try
		{
			method = beanClass.getMethod(methodName, EMPTY_CLASS_ARRAY);
		}
		catch (final NoSuchMethodException name)
		{
			final String message = methodName + "() method does not exist";
			throw new IllegalArgumentException(message);
		}

		//  Make sure the method returns a value

		final Class returnClass = method.getReturnType();

		if (returnClass.getName().equals("void"))
		{
			final String message = methodName + " has a void return type";
			throw new IllegalArgumentException(message);
		}
	}

	/*
	 * Sort in the specified order with the remaining default properties
	 */
	public JnjObjectComparator(final Class beanClass, final String methodName, final boolean isAscending)
	{
		this.isAscending = isAscending;
		try
		{
			method = beanClass.getMethod(methodName, EMPTY_CLASS_ARRAY);
		}
		catch (final NoSuchMethodException nsme)
		{
			final String message = methodName + "() method does not exist";
			throw new IllegalArgumentException(message);
		}

		//  Make sure the method returns a value

		final Class returnClass = method.getReturnType();

		if (returnClass.getName().equals("void"))
		{
			final String message = methodName + " has a void return type";
			throw new IllegalArgumentException(message);
		}
	}

	/*
	 * Sort in the specified order and case sensitivity with the remaining default properties
	 */
	public JnjObjectComparator(final Class beanClass, final String methodName, final boolean isAscending,
			final boolean isIgnoreCase)
	{
		setAscending(isAscending);
		setIgnoreCase(isIgnoreCase);

		//  Make sure the method exists in the given bean class

		try
		{
			method = beanClass.getMethod(methodName, EMPTY_CLASS_ARRAY);
		}
		catch (final NoSuchMethodException nsme)
		{
			final String message = methodName + "() method does not exist";
			throw new IllegalArgumentException(message);
		}

		//  Make sure the method returns a value

		final Class returnClass = method.getReturnType();

		if (returnClass.getName().equals("void"))
		{
			final String message = methodName + " has a void return type";
			throw new IllegalArgumentException(message);
		}
	}

	/*
	 * Set the sort order
	 */
	public void setAscending(final boolean isAscending)
	{
		this.isAscending = isAscending;
	}

	/*
	 * Set whether case should be ignored when sorting Strings
	 */
	public void setIgnoreCase(final boolean isIgnoreCase)
	{
		this.isIgnoreCase = isIgnoreCase;
	}

	/*
	 * Set nulls position in the sort order
	 */
	public void setNullsLast(final boolean isNullsLast)
	{
		this.isNullsLast = isNullsLast;
	}

	/*
	 * Implement the Comparable interface
	 */

	@Override
	public int compare(final Object object1, final Object object2)
	{
		Object field1 = null;
		Object field2 = null;
		try
		{
			field1 = method.invoke(object1, EMPTY_OBJECT_ARRAY);
			field2 = method.invoke(object2, EMPTY_OBJECT_ARRAY);
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}

		// Treat empty strings like nulls

		if (field1 instanceof String && ((String) field1).length() == 0)
		{
			field1 = null;
		}

		if (field2 instanceof String && ((String) field2).length() == 0)
		{
			field2 = null;
		}

		// Handle sorting of null values

		if (field1 == null && field2 == null)
		{
			return 0;
		}

		if (field1 == null)
		{
			return isNullsLast ? 1 : -1;
		}

		if (field2 == null)
		{
			return isNullsLast ? -1 : 1;
		}

		//  Compare objects

		Object objectC1;
		Object objectC2;
		if (isAscending)
		{
			objectC1 = field1;
			objectC2 = field2;
		}
		else
		{
			objectC1 = field2;
			objectC2 = field1;
		}

		if (objectC1 instanceof Comparable)
		{
			if (objectC1 instanceof String && isIgnoreCase)
			{
				return ((String) objectC1).compareToIgnoreCase((String) objectC2);
			}
			else
			{
				return ((Comparable) objectC1).compareTo(objectC2);
			}
		}
		else
		// Compare as a String
		{
			if (isIgnoreCase)
			{
				return objectC1.toString().compareToIgnoreCase(objectC2.toString());
			}
			else
			{
				return objectC1.toString().compareTo(objectC2.toString());
			}
		}

	}

}

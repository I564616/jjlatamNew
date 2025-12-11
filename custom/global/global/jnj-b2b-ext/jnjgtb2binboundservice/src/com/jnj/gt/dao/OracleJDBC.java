/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.company;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author Accenture
 * @version 1.0
 */

public class OracleJDBC
{

	public static void main(final String[] argv)
	{
		//NEERAJ_DB
		//SYSTEM
		//SYSTEM
		//localhost
		//1521
		//xe
		//jdbc:oracle:thin:@localhost:1521:mkyong
		final String driverName = "oracle.jdbc.driver.OracleDriver";//Config.getParameter("inbound.feed.db.driver");
		final String connectionString = "jdbc:oracle:thin:@localhost:1521:NEERAJ_DB";//Config.getParameter("inbound.feed.db.username");
		final String dbUserName = "SYSTEM"; // Config.getParameter("inbound.feed.db.username");
		final String dbPassword = "system"; //Config.getParameter("inbound.feed.db.driver");

		try
		{
			Class.forName(driverName);
		}
		catch (final ClassNotFoundException e)
		{

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;
		}
		System.out.println("Oracle JDBC Driver Registered!");
		Connection connection = null;
		try
		{
			connection = DriverManager.getConnection(connectionString, dbUserName, dbPassword);
		}
		catch (final SQLException e)
		{
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
		if (connection != null)
		{
			System.out.println("You made it, take control your database now!");
		}
		else
		{
			System.out.println("Failed to make connection!");
		}
	}

}
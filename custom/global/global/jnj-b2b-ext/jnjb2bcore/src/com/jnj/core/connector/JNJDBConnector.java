package com.jnj.core.connector;

import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JNJDBConnector {
	
	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParajdbcTemplate;

	private static final Logger LOG = Logger.getLogger(JNJDBConnector.class);
	
	public void setDataSource(final DataSource dataSource)
	{
		LOG.info("#######################################");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		LOG.info(" JDBC Template Initialized::::::::::::::"+ jdbcTemplate);
		this.namedParajdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		LOG.info(" Named Parameter JDBC Template Initialized::::::::::::::"+ namedParajdbcTemplate);
		LOG.info("#######################################");
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}


	/**
	 * @param jdbcTemplate
	 *           the jdbcTemplate to set
	 */
	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	 * @return the namedParajdbcTemplate
	 */
	public NamedParameterJdbcTemplate getNamedParajdbcTemplate()
	{
		return namedParajdbcTemplate;
	}


	/**
	 * @param namedParajdbcTemplate
	 *           the namedParajdbcTemplate to set
	 */
	public void setNamedParajdbcTemplate(final NamedParameterJdbcTemplate namedParajdbcTemplate)
	{
		this.namedParajdbcTemplate = namedParajdbcTemplate;
	}



}

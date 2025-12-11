/**
 *
 */
package com.jnj.core.connector;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * @author Manoj.K.Panda
 *
 */
public class JNJRSADBConnector
{

	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParajdbcTemplate;

	private static final Logger LOG = Logger.getLogger(JNJRSADBConnector.class);

	public void setDataSource(final DataSource dataSource)
	{
		LOG.info("#######################################");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		LOG.info("RSA JDBC Template Initialized::::::::::::::");
		this.namedParajdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		LOG.info("RSA Named Parameter JDBC Template Initialized::::::::::::::");
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

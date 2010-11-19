/*
 Copyright 2008 SEC "Knowledge Genesis", Ltd.
 http://www.kg.ru, http://www.knowledgegenesis.com
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.10.2009 15:04:47$
*/
package ru.pda.chemistry.sql;

import org.apache.log4j.Logger;

import java.sql.*;

import static ru.pda.chemistry.util.StringUtils.getConcatenation;

/**
 * // todo: make logging configurable
 * @author pda (20.10.2009 15:04:47)
 * @version $Revision$
 */
public class SqlConnector
{
	public static SqlConnector create() throws SQLException {
		SqlConnector connector = new SqlConnector();
		connector.createConnection();
		return connector;
	}

	public SqlConnector() {
		sb = new StringBuffer();
	}

	public Connection createConnection() throws SQLException {
		try
		{
			Class.forName(DRIVER_CLASS).newInstance();

			logger.info("");
			logger.info(getConcatenation(sb, "creating connection to ", DATABASE_URL));
			connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
			logger.info(getConcatenation(sb, "connection to ", DATABASE_URL, " created successfully"));
			return connection;
		}
		catch (ClassNotFoundException e)
		{
			logger.error(getConcatenation(sb, "JDBC driver class not found for name ", DRIVER_CLASS, ": "), e);
			throw new RuntimeException(e);
		}
		catch (InstantiationException e)
		{
			logger.error(getConcatenation(sb, "error while creating driver instance for class ", DRIVER_CLASS, ": "), e);
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			logger.error(getConcatenation(sb, "error while creating driver instance for class ", DRIVER_CLASS, ": "), e);
			throw new RuntimeException(e);
		}
		catch (SQLException e)
		{
			logger.error(getConcatenation(sb, "error while creating connection to ", DATABASE_URL, ": "), e);
			throw e;
		}
	}
	public void closeConnection() throws SQLException {
		try
		{
			logger.info(getConcatenation(sb, "closing connection to ", DATABASE_URL));
			connection.close();
			logger.info(getConcatenation(sb, "connection to ", DATABASE_URL, " closed successfully"));
		}
		catch (SQLException e)
		{
			logger.error(getConcatenation(sb, "failed to close connection to ", DATABASE_URL, ": "), e);
			throw e;
		}
	}

	public Connection getConnection() {
		return connection;
	}
	
	public ResultSet getResultSet(String query) throws SQLException {
		logger.info(getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = connection.createStatement();
		statement.executeQuery(query);
		return statement.getResultSet();
	}
	public ResultSet getResultSetNext(String query) throws SQLException {
		logger.info(getConcatenation(sb, "Query: ", query)); // todo: use lesser log level
		
		Statement statement = connection.createStatement();
		statement.executeQuery(query);
		ResultSet resultSet = statement.getResultSet();
		resultSet.next();
		return resultSet;
	}
	public boolean hasResult(String query) throws SQLException {
		ResultSet resultSet = null;
		try
		{
			resultSet = getResultSet(query);
			return resultSet.next();
		}
		finally
		{
			closeResultSet(resultSet);
		}
	}
	public boolean hasResult(PreparedStatement statement) throws SQLException {
		ResultSet resultSet = null;

		try
		{
			resultSet = getResultSet(statement);
			return resultSet.next();
		}
		finally
		{
			if (resultSet != null)
				resultSet.close();
		}
	}
	public void closeStatement(PreparedStatement statement) throws SQLException {
		if (statement == null)
			return;

		statement.close();		
	}
	public void closeResultSet(ResultSet resultSet) throws SQLException {
		if (resultSet == null)
			return; // resultSet wasn't actually created

		Statement statement = resultSet.getStatement();
		resultSet.close();
		statement.close();
	}
	public void execute(String query) throws SQLException {
		logger.info(getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			statement.executeQuery(query);
		}
		finally
		{
			if (statement != null)
				statement.close();
		}
	}
	public int executeUpdate(String query) throws SQLException {
		logger.info(getConcatenation(sb, "Query: ", query)); // todo: use lesser log level

		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			return statement.executeUpdate(query);
		}
		finally
		{
			if (statement != null)
				statement.close();
		}
	}

	public PreparedStatement prepareStatement(String query) throws SQLException {
		logger.info(getConcatenation(sb, "Prepared query: ", query)); // todo: use lesser log level
		return connection.prepareStatement(query);
	}
	public ResultSet getResultSet(PreparedStatement statement) throws SQLException {
		statement.executeQuery();
		return statement.getResultSet();
	}
	public ResultSet getResultSetNext(PreparedStatement statement) throws SQLException {
		statement.executeQuery();
		ResultSet resultSet = statement.getResultSet();
		resultSet.next();

		return resultSet;
	}


	public int getCount(String query) throws SQLException {
		ResultSet resultSet = null;

		try
		{
			resultSet = getResultSetNext(query);
			return resultSet.getInt(1);
		}
		finally
		{
			closeResultSet(resultSet);
		}
	}

	public int getCount(PreparedStatement statement) throws SQLException {
		ResultSet resultSet = null;
		try
		{
			resultSet = getResultSetNext(statement);
			return resultSet.getInt(1);
		}
		finally
		{
			if (resultSet != null)
				resultSet.close();
		}
	}


	private StringBuffer sb;
	private Connection connection;

	// todo: get this all from some .xml config (choosing of DB and maybe name\password too)
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	private static final String DATABASE_URL = "jdbc:mysql://localhost/chemistry_db";

	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final Logger logger = Logger.getLogger(SqlConnector.class); // todo: use configurator to define whether to log sql
}
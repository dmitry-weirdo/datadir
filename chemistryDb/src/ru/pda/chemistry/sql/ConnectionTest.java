/*
 Copyright 2008 SEC "Knowledge Genesis", Ltd.
 http://www.kg.ru, http://www.knowledgegenesis.com
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.10.2009 15:07:05$
*/
package ru.pda.chemistry.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author pda (20.10.2009 15:07:05)
 * @version $Revision$
 */
public class ConnectionTest
{
	public static void main(String[] args) throws SQLException {
		SqlConnector connector =  SqlConnector.create();
		ResultSet resultSet;

		try
		{
//			connector.executeUpdate("insert into section(name) values (\"Биохимия\")");

			resultSet = connector.getResultSetNext("select count(id) from section");
			System.out.println("count: " + resultSet.getInt(1));
			connector.closeResultSet(resultSet);


			resultSet = connector.getResultSet("select count(id) from entity");
			while (resultSet.next())
				System.out.println("in while: count: " + resultSet.getInt(1));

			connector.closeResultSet(resultSet);


			resultSet = connector.getResultSet("select id, name from section order by name");
			while (resultSet.next())
			{
				System.out.println("");
				System.out.println("section id: " + resultSet.getInt(1));
				System.out.println("section name: " + resultSet.getString(2));
			}

			connector.closeResultSet(resultSet);
		}
		finally
		{
//			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}
}
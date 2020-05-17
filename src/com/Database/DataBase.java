package com.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase 
{
	private Connection conn;
	private PreparedStatement pstm;
	private String user="sa";
	private String password="19980711";
	private String className="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String dataBaseUrl="jdbc:sqlserver://127.0.0.1:1433;databasename=DB_HolsanSys";
	
	public DataBase()
	{
		try
		{
			Class.forName(className);
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**Creating a database connection**/
	public Connection getConn()
	{
		try 
		{
			conn=DriverManager.getConnection(dataBaseUrl,user,password);
		} 
		catch (SQLException e) 
		{
			conn=null;
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public void doPstm(String mySql,Object[] myParams)
	{
		if(  mySql != null && !mySql.equals("") )
		{
			if(myParams==null)
				myParams=new Object[0];
			
			getConn();
			if(conn!=null)
			{
				try
				{		
					System.out.println(mySql);
					pstm=conn.prepareStatement(mySql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					for(int i=0;i<myParams.length;i++){
						pstm.setObject(i+1,myParams[i]);
					}
					
					pstm.execute();
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}				
			}			
		}
	}
	
	public ResultSet getRs() throws SQLException
	{
		return pstm.getResultSet();		
	}
	
	public int getCount() throws SQLException
	{
		return pstm.getUpdateCount();		
	}

	public void closed()
	{
		try
		{
			if(pstm!=null)
				pstm.close();			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			if(conn!=null)
			{
				conn.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}

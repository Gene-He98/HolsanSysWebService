package com.Database;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;

public class ConnCls {
	private DataBase dB;
	
	public ConnCls()
	{
		dB=new DataBase();	
	}
	
	//*******the execution scripts of insert, update and delete****Beginning*******
	//insert
	//mySql="insert into table(ID,title,WriteMan,WriteTime) values(?,?,?,?)";
	//Object[] myParams={myID, myTitle,myWriteMan,myWriteTime};
	
	//update
	//mySql="update table set title=?,WriteMan=?,WriteTime=? where ID=?";
	//Object[] myParams={myTitle,myWriteMan,myWriteTime,myID};
	
	//delete
	//mySql="delete from table where ID=?";
	//Object[] myParams={myID};
	//*******the execution scripts of insert, update and delete****Ending*********

	public boolean ExecuteSql(String mySql, Object[] myParams)
	{
		boolean myReturnValue=false;
		
		try
		{
			dB.doPstm(mySql, myParams);
		
			myReturnValue=true;		
		}
		catch(Exception e)
		{
			System.out.println("The ExecuteSql() method failed! (updating database)");
			e.printStackTrace();
		}
		finally
		{
			dB.closed();
		}

		return myReturnValue;
	}

	//Get a return message of execution, returns in XML format
	public String GetResultMsgInXml(String mySql,Object[] myParams)
	{
		String myMsg="error";

		if(ExecuteSql(mySql, myParams))
			myMsg="ok";

		try{
			//创建 DOM解析器的工厂
			DocumentBuilderFactory myDocBuilderFactory=DocumentBuilderFactory.newInstance();

			//调用工厂对象的 newDocumentBuilder方法得到 DOM解析器对象
			DocumentBuilder myDocBuilder=myDocBuilderFactory.newDocumentBuilder();

			//创建一个Document对象，该对象代表一个XML文件
			Document myDoc=myDocBuilder.newDocument();
			myDoc.setXmlStandalone(true);
			myDoc.setXmlVersion("1.0");

			Element myRoot=myDoc.createElement("Root");
			myRoot.setAttribute("xmlns", "com.cclsol.API");

			Element myItemElement = myDoc.createElement("Item");
			myItemElement.setAttribute("msg", myMsg);
			
			myRoot.appendChild(myItemElement);
			
			myDoc.appendChild(myRoot);
			
			myMsg=ConvertXmlDocIntoString(myDoc);
		}
		catch (Exception e)
		{
			System.out.println("Failed to execute a sql script!(execute database)");			
			e.printStackTrace();
		}
		finally
		{
			dB.closed();			
		}	
		
		return myMsg;
	}

	//Get a return message of execution, returns in json format
	public String GetResultMsgInJson(String mySql,Object[] myParams)
	{
		String myMsg="error";

		if(ExecuteSql(mySql, myParams))
			myMsg="ok";
		
		try{	
			List<Map<String,String>> myRecordList=new ArrayList<Map<String,String>>();

			Map<String,String> myMap = new HashMap<String,String>();
			myMap.put("msg", myMsg);
				
			myRecordList.add(myMap);
			
			Gson myGson = new Gson();
			//Gson myGson=new GsonBuilder().disableHtmlEscaping().create();
			myMsg = myGson.toJson(myRecordList);
		}
		catch (Exception e)
		{
			System.out.println("Failed to get a data list from table!(query database)");			
			e.printStackTrace();
		}
		finally
		{
			dB.closed();			
		}	
		
		return myMsg;
	}

	//Get a data list from table, returns in XML format
	public String GetDataListInXml(String mySql,Object[] myParams)
	{
		String myXmlStr="";

		dB.doPstm(mySql, myParams);
		try
		{
			ResultSet myRS=dB.getRs();
			if(myRS!=null)
			{
				//创建 DOM解析器的工厂
				DocumentBuilderFactory myDocBuilderFactory=DocumentBuilderFactory.newInstance();

				//调用工厂对象的 newDocumentBuilder方法得到 DOM解析器对象
				DocumentBuilder myDocBuilder=myDocBuilderFactory.newDocumentBuilder();

				//创建一个Document对象，该对象代表一个XML文件
				Document myDoc=myDocBuilder.newDocument();
				myDoc.setXmlStandalone(true);
				myDoc.setXmlVersion("1.0");

				Element myRoot=myDoc.createElement("Root");
				myRoot.setAttribute("xmlns", "com.cclsol.API");

				ResultSetMetaData myRSMD = myRS.getMetaData();
				
				while(myRS.next())
				{
					Element myItemElement = myDoc.createElement("Item");
					
					for(int i=0;i<myRSMD.getColumnCount();i++)
					{
						myItemElement.setAttribute(myRSMD.getColumnName(i+1), myRS.getString(myRSMD.getColumnName(i+1)));
					}	
					
					myRoot.appendChild(myItemElement);
				}
				myDoc.appendChild(myRoot);
				
				myXmlStr=ConvertXmlDocIntoString(myDoc);
			}
			myRS.close();
		}
		catch (Exception e)
		{
			System.out.println("Failed to get a data list from table!(query database)");			
			e.printStackTrace();
		}
		finally
		{
			dB.closed();			
		}	
		
		return myXmlStr;
	}

	//Get a data list from table, returns in json format
	public String GetDataListInJson(String mySql,Object[] myParams)
	{
		String myJsonStr="";
		
		dB.doPstm(mySql, myParams);
		try{
			ResultSet myRS=dB.getRs();
			if(myRS!=null){
				ResultSetMetaData myRSMD = myRS.getMetaData();
				
				List<Map<String,String>> myRecordList=new ArrayList<Map<String,String>>();

				while(myRS.next()){
					Map<String,String> myMap = new HashMap<String,String>();
					
					for(int i=0;i<myRSMD.getColumnCount();i++)
					{
						myMap.put(myRSMD.getColumnName(i+1), myRS.getString(myRSMD.getColumnName(i+1)));
					}
					
					myRecordList.add(myMap);
				}//while(myRS.next())
				
				Gson myGson = new Gson();
				//Gson myGson=new GsonBuilder().disableHtmlEscaping().create();
				myJsonStr = myGson.toJson(myRecordList);
			}//if(myRS!=null)
			
			myRS.close();
		}//try
		catch (Exception e)
		{
			System.out.println("Failed to get a data list from table!(query database)");			
			e.printStackTrace();
		}
		finally
		{
			dB.closed();			
		}	
		
		return myJsonStr;
	}
	
	private String ConvertXmlDocIntoString(Document myDoc)
	{
		String myXmlStr="";
		
		try
		{
			//创建TransformerFactory类的对象
			TransformerFactory myTransFactory = TransformerFactory.newInstance();

			//通过TransformerFactory创建Transformer对象
			Transformer myTransFormer = myTransFactory.newTransformer();

			//源数据，需要创建DOMSource对象并将Document加载到其中
			myTransFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource myDomSource = new DOMSource(myDoc);

			ByteArrayOutputStream myBAOS = new ByteArrayOutputStream();

			//将DOM树转换为XML文件，第二个参数为要生成的XML文件，需要创建StreamResult对象并指定目的文件
			myTransFormer.transform(myDomSource, new StreamResult(myBAOS));
	
			myXmlStr=myBAOS.toString();
		}
		catch(Exception e)
		{
			myXmlStr="";
		}
		
		return myXmlStr;
	}
}

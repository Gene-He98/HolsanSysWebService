package com.HolsanSys;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import com.Database.ConnCls;

@WebService
public class MainAPI {
	String[] _DictionaryNameArr={"Authority","BaggingStandard","ClassName","Department","MaterialName"
			,"MeasurementUnit","Mould","Position","ProductName","Title","UserStatus","WeightNorms"};
	
	public static void main(String[] args) {
		Endpoint.publish("http://192.168.1.8:8081/MainAPI", new MainAPI());
		System.out.println("WebService成功开启！");
	}
	
	//登录操作
	public String UserLogin(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPassword")String myPassword)
	{
		String mySql="select UserID,UserName,Password"
				+ " from Tab_User"
				+" where (UserID='"+myUserID+"'OR UserName='"+myUserID+"') and Password='"+myPassword+"'";
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//注册操作
	public String UserRegister(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myUserName")String myUserName
			,@WebParam(name="myPassword")String myPassword)
	{
		
		String mySql="insert into Tab_User(UserID,UserName,Password) values("
				+myUserID+",'"+myUserName+"','"+myPassword+"')";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//忘记密码
	public String ForgetPassword(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPassword")String myPassword)
	{
		String mySql="update Tab_User set Password='" + myPassword+"' where UserID='"+myUserID+"'";
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//查询用户绑定的用药人
	public String UserInfo(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID)
	{
		String mySql="select PatientName,Location"
				+ " from Tab_Patient"
				+ " where UserID='"+myUserID+"'";
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//查询用药人信息
	public String PatientInfo(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName)
	{
		String mySql="select*"
				+ " from Tab_Patient"
				+ " where UserID='"+myUserID+"' and PatientName='"+myPatientName+"'" ;
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//添加用药人
	public String AddPatient(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="newPatientName")String newPatientName
			,@WebParam(name="newPatientAge")String newPatientAge
			,@WebParam(name="newPatientSex")String newPatientSex
			,@WebParam(name="newPatientAddress")String newPatientAddress
			,@WebParam(name="newPatientBloodType")String newPatientBloodType
			,@WebParam(name="newPatientMedicalHistory")String newPatientMedicalHistory
			,@WebParam(name="newPatientAllergy")String newPatientAllergy)
	{
		
		String mySql="insert into Tab_Patient values('"
				+newPatientName+"','"+newPatientAge+"','"+newPatientSex
				+"','"+newPatientAddress+"','"+newPatientBloodType+"','"+newPatientMedicalHistory
				+"','"+newPatientAllergy+"','"+myUserID+"','尚未更新')";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//删除用药人
	public String DeletePatient(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName)
	{
		String mySql="delete from Tab_Patient where UserID='"+myUserID+"' and PatientName ='"+myPatientName+"';"
				+"delete from Tab_Notification where UserID='"+myUserID+"' and PatientName ='"+myPatientName+"';" 
				+"delete from Tab_DrugRecord where UserID='"+myUserID+"' and PatientName ='"+myPatientName+"'"  ;
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		return myResult;
		
	}
	
	//修改用药人
	public String ChangePatient(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="newPatientName")String newPatientName
			,@WebParam(name="newPatientAge")String newPatientAge
			,@WebParam(name="newPatientSex")String newPatientSex
			,@WebParam(name="newPatientAddress")String newPatientAddress
			,@WebParam(name="newPatientBloodType")String newPatientBloodType
			,@WebParam(name="newPatientMedicalHistory")String newPatientMedicalHistory
			,@WebParam(name="newPatientAllergy")String newPatientAllergy
			,@WebParam(name="oriPatientName")String oriPatientName)
	{
		
		String mySql="update Tab_Patient set PatientName= '"
				+newPatientName+"', PatientAge='"+newPatientAge+"',PatientSex='"+newPatientSex
				+"',PatientAddress='"+newPatientAddress+"',PatientBloodType='"+newPatientBloodType
				+"',PatientMedicalHistory='"+newPatientMedicalHistory
				+"',PatientAllergy='"+newPatientAllergy+"' where UserID='"+myUserID
				+"'and PatientName='"+oriPatientName
				+"' ; update Tab_Notification set PatientName='"+newPatientName
				+"' where UserID='"+myUserID+"' and PatientName ='"+oriPatientName
				+"' ; update Tab_DrugRecord set PatientName='"+newPatientName
						+"' where UserID='"+myUserID+"' and PatientName ='"+oriPatientName+"'";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(mySql);
		
		return myResult;
	}
	
	//更新地理位置信息
	public String UpdateLocation(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName
			,@WebParam(name="newLocation")String newLocation)
	{
		String mySql="update Tab_Patient set Location='"+newLocation+"' where UserID='"+myUserID+"' and PatientName ='"+myPatientName+"'";
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(mySql);
		return myResult;
	}
	
	//更新用户提醒计划界面信息
	public String NotificationInfo(@WebParam(name="myTransformat")String myTransformat
				,@WebParam(name="myUserID")String myUserID
				,@WebParam(name="myPatientName")String myPatientName)
	{
		String mySql="select*"
				+ " from Tab_Notification"
				+ " where UserID='"+myUserID+"' and PatientName ='"+myPatientName+"' order by DayNotification asc" ;
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
		
	//查询提醒计划细节
	public String NotificationDetail(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName
			,@WebParam(name="myNotificationName")String myNotificationName)
	{
		String mySql="select*"
				+ " from Tab_Notification"
				+ " where UserID='"+myUserID+"' and PatientName ='"+myPatientName
				+"' and NotificationName ='"+myNotificationName+"'" ;
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
		
	//删除提醒计划
	public String DeleteNotification(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName
			,@WebParam(name="myNotificationName")String myNotificationName)
	{
		String mySql="delete  from Tab_Notification  where UserID='"+myUserID+"' and PatientName ='"+myPatientName
				+"' and NotificationName ='"+myNotificationName+"';"
				+"delete  from Tab_DrugRecord  where UserID='"+myUserID+"' and PatientName ='"+myPatientName
				+"' and NotificationName ='"+myNotificationName+"'" ;
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		return myResult;
		
	}
		
	//添加新的提醒计划
	public String AddNotification(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName
			,@WebParam(name="newNotificationName")String newNotificationName
			,@WebParam(name="newDayNotification")String newDayNotification
			,@WebParam(name="newWeekNotification")String newWeekNotification
			,@WebParam(name="newNotificationWay")String newNotificationWay
			,@WebParam(name="newPictureSrc")String newPictureSrc
			,@WebParam(name="newPictureText")String newPictureText
			,@WebParam(name="newVoiceSrc")String newVoiceSrc
			,@WebParam(name="newTextText")String newTextText
			,@WebParam(name="newTinkleSrc")String newTinkleSrc
			,@WebParam(name="newNotificationVibrate")String newNotificationVibrate)
	{
		boolean vibrate;
		if(newNotificationVibrate.equals("1"))
			vibrate = true;
		else
			vibrate = false;
			
		
		String mySql="insert into Tab_Notification values('"
				+newNotificationName+"','"+newDayNotification+"','"+newWeekNotification
				+"','"+newNotificationWay+"','"+newPictureSrc+"','"+newPictureText
				+"','"+newVoiceSrc+"','"+newTextText+"','"+newTinkleSrc
				+"','"+vibrate+"','true','"+myPatientName+"','"+myUserID+"')";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
		
	//修改新的提醒计划
	public String ChangeNotification(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName
			,@WebParam(name="newNotificationName")String newNotificationName
			,@WebParam(name="newDayNotification")String newDayNotification
			,@WebParam(name="newWeekNotification")String newWeekNotification
			,@WebParam(name="newNotificationWay")String newNotificationWay
			,@WebParam(name="newPictureSrc")String newPictureSrc
			,@WebParam(name="newPictureText")String newPictureText
			,@WebParam(name="newVoiceSrc")String newVoiceSrc
			,@WebParam(name="newTextText")String newTextText
			,@WebParam(name="newTinkleSrc")String newTinkleSrc
			,@WebParam(name="newNotificationVibrate")String newNotificationVibrate
			,@WebParam(name="oriNotificationName")String oriNotificaitonName)
	{
		boolean vibrate;
		if(newNotificationVibrate.equals("1"))
			vibrate = true;
		else
			vibrate = false;
			
		
		String mySql="update Tab_Notification set NotificationName='"
				+newNotificationName+"',DayNotification='"+newDayNotification
				+"',WeekNotification='"+newWeekNotification
				+"',NotificationWay='"+newNotificationWay+"',PictureSrc='"+newPictureSrc
				+"',PictureText='"+newPictureText
				+"',VoiceSrc='"+newVoiceSrc+"',TextText='"+newTextText+"',TinkleSrc='"+newTinkleSrc
				+"',NotificationVibrate='"+vibrate+"' where PatientName='"+myPatientName
				+"' and UserID='"+myUserID+"' and NotificationName='"+oriNotificaitonName
				+"' ; update Tab_DrugRecord set NotificationName='"+newNotificationName
				+"' where UserID='"+myUserID+"' and PatientName ='"+myPatientName
				+"' and NotificationName ='"+oriNotificaitonName+"'";
		System.out.println(mySql);
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//修改计划是否开启
	public String ChangeNotificationSwitch(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName
			,@WebParam(name="newSwitch")String newSwitch
			,@WebParam(name="myNotificationName")String myNotificationName)
	{
		boolean Switch;
		if(newSwitch.equals("true"))
			Switch = true;
		else
			Switch = false;
			
		
		String mySql="update Tab_Notification set Switch='"
				+Switch+"' where UserID='"+myUserID+"' and PatientName ='"+myPatientName
				+"' and NotificationName ='"+myNotificationName+"'";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//用药记录
	public String DrugRecord(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName
			,@WebParam(name="myNotificationName")String myNotificationName
			,@WebParam(name="myIfDrug")String myIfDrug)
	{
		SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //set date format
    	Date myNow=new Date();
		String mySql="insert into Tab_DrugRecord values('"
				+mySimpleDateFormat.format(myNow)+"','"+myUserID+"','"+myPatientName
				+"','"+myNotificationName+"','"+myIfDrug+"')";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
		
	//查询用药记录
	public String DrugRecordInfo(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myPatientName")String myPatientName)
	{
		String mySql="select*"
				+ " from Tab_DrugRecord"
				+ " where UserID='"+myUserID+"' and PatientName='"+myPatientName+"' order by DrugTime desc" ;
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//查询药品信息
	public String MedicineInfo(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID)
	{
		String mySql="select*"
				+ " from Tab_Medicine"
				+ " where UserID='"+myUserID+"'" ;
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//查询药品详情
	public String MedicineDetail(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myMedicineName")String myMedicineName)
	{
		String mySql="select*"
				+ " from Tab_Medicine"
				+ " where UserID='"+myUserID+"' and MedicineName='"+myMedicineName+"'" ;
		
		String myResult = PackagingSelectedResultString(myTransformat, mySql);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//添加药品
	public String AddMedicine(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="newMedicineName")String newMedicineName
			,@WebParam(name="newMedicineAnotherName")String newMedicineAnotherName
			,@WebParam(name="newUsage")String newUsage
			,@WebParam(name="newDosage")String newDosage
			,@WebParam(name="newCautions")String newCautions
			,@WebParam(name="newValidity")String newValidity)
	{
		
		String mySql="insert into Tab_Medicine values('"
				+newMedicineName+"','"+newMedicineAnotherName+"','"+newUsage
				+"','"+newDosage+"','"+newCautions+"','"+newValidity
				+"','"+myUserID+"')";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}
	
	//添加药品
	public String ChangeMedicine(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="newMedicineName")String newMedicineName
			,@WebParam(name="newMedicineAnotherName")String newMedicineAnotherName
			,@WebParam(name="newUsage")String newUsage
			,@WebParam(name="newDosage")String newDosage
			,@WebParam(name="newCautions")String newCautions
			,@WebParam(name="newValidity")String newValidity
			,@WebParam(name="oriMedicineName")String oriMedicineName)
	{
		
		String mySql="update Tab_Medicine set MedicineName='"
				+newMedicineName+"',MedicineAnotherName='"+newMedicineAnotherName+"',Usage='"+newUsage
				+"',Dosage='"+newDosage+"',Cautions='"+newCautions+"',Validity='"+newValidity
				+"' where UserID='"+myUserID+"' and MedicineName='"+oriMedicineName+"'";
		
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		System.out.println(myResult);
		
		return myResult;
	}

	//删除药品
	public String DeleteMedicine(@WebParam(name="myTransformat")String myTransformat
			,@WebParam(name="myUserID")String myUserID
			,@WebParam(name="myMedicineName")String myMedicineName)
	{
		String mySql="delete from Tab_Medicine where UserID='"+myUserID+"' and MedicineName ='"+myMedicineName+"'";
		String myResult = PackagingExecuteResultString(myTransformat, mySql, null);
		return myResult;
		
	}
	
    //增删改操作时
    private String PackagingExecuteResultString(String myTransformat, String mySql, Object[] myParams)
    {
    	String myReturnStr="";
    	
    	ConnCls myConnCls=new ConnCls();

		switch(myTransformat)
		{
			case "xml":
				myReturnStr=myConnCls.GetResultMsgInXml(mySql, myParams);
				
				break;
			case "json":
				myReturnStr=myConnCls.GetResultMsgInJson(mySql, myParams);
				
				break;
		}//switch(myTransformat)
		
		return myReturnStr;
    }
	
	//查询操作时
	private String PackagingSelectedResultString(String myTransformat, String mySql)
	{
		String myReturnStr="";
		
		ConnCls myConnCls=new ConnCls();
		
		switch(myTransformat)
		{
			case "xml":
				myReturnStr=myConnCls.GetDataListInXml(mySql, null);
				
				break;
			case "json":
				myReturnStr=myConnCls.GetDataListInJson(mySql, null);
				
				break;
		}
		
		return myReturnStr;
	}
}

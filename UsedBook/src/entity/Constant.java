package entity;

import android.os.Environment;

public class Constant{
	public static final int INSERT=1;
	public static final int DELETE=2;
	public static final int QUERY=3;
	public static final int UPDATE=4;
	public static final int EXCEPTION=7;

	
	public static final int SUCCESS=5;
	public static final int FAIL=6;
	
	public static final int CLASSIFY=8;
	public static final int ADDMORE=9;
    public static final int SEARCH=10;
	//文件暂存位置
	public static final String TEMPPATH= Environment.getExternalStorageDirectory()
			+"/gdut/imageTEMP/";
	
	public static String PublishURL= "http://192.168.1.110:8080/DesignatedDrivingServer/comment.action"; 
	public static String UpLoadURL ="http://192.168.1.110:8080/DesignatedDrivingServer/UpLoadServlet.action";
	public static String GetInfosURL= "http://192.168.1.110:8080/DesignatedDrivingServer/login.action"; 
	public static String SearchInfosURL= "http://192.168.1.110:8080/DesignatedDrivingServer/login.action"; 
	public static String GetMyInfosURL= "http://192.168.1.110:8080/DesignatedDrivingServer/login.action"; 
}
package mydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

public class GetDBConn {
//	private static Connection conn=null;
//	static {
//		DbUtils.loadDriver(DBconfig.getDRIVER());
//		try {
//			conn = DriverManager.getConnection(DBconfig.getURL(),DBconfig.getUSER(),DBconfig.getPASSWORD());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static Connection getConnection(){
		DbUtils.loadDriver(DBconfig.getDRIVER());
		try {
			return DriverManager.getConnection(DBconfig.getURL(),DBconfig.getUSER(),DBconfig.getPASSWORD());
		} catch (SQLException e) {
			e.printStackTrace();
		};
		return null;
	}
	//这个方法还不知道用不用，如果不用DbUtil，就需要这个方法以及对应的重载方法
	public static void Close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}

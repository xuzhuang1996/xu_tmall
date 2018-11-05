package tmall.mydb;

import java.sql.Connection;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class GetC3p0DB {
	//c3p0.xml名字必须为那个，且需要放在src文件夹下
	private static ComboPooledDataSource dataSource = null;
	// 数据库连接池应只被初始化一次.
    static {
        dataSource = new ComboPooledDataSource("tmall");
    	//dataSource = new ComboPooledDataSource();//使用默认的
    }
    public static Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
	
    
}

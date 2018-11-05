package tmall.mydb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBconfig {
	private static  String URL;
	private static  String DRIVER;
	private static  String USER;
	private static  String PASSWORD;
	
	static {
		init();
	}
	
	private static void init(){
		//利用类加载器实现静态变量的初始化
		//DBconfig.class 得到的是class DBconfig,类加载器属于动态的载入,可以提高读写效率.
		InputStream in = DBconfig.class.getClassLoader().getResourceAsStream("mydb/db.properties");
		//在这里打开properties的源代码，继承与hashtable线程安全，而hashtable实现map键值对
		Properties properties = new Properties();
		//现在拿到流的信息就可以读取配置数据了
		try {
			properties.load(in);
			URL = properties.getProperty("URL");
			DRIVER = properties.getProperty("DRIVER");
			USER = properties.getProperty("USER");
			PASSWORD = properties.getProperty("PASSWORD");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getURL() {
		return URL;
	}

	public static void setURL(String uRL) {
		URL = uRL;
	}

	public static String getDRIVER() {
		return DRIVER;
	}

	public static void setDRIVER(String dRIVER) {
		DRIVER = dRIVER;
	}

	public static String getUSER() {
		return USER;
	}

	public static void setUSER(String uSER) {
		USER = uSER;
	}

	public static String getPASSWORD() {
		return PASSWORD;
	}

	public static void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
}

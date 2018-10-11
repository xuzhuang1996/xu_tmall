package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import mydb.DBconfig;
import mydb.Question;
import mydb.QuestionAction;
import mydb.QuestionDAOImp;

public class test1 {

	public static void main(String[] args) {
//		QuestionDAOImp a = new QuestionDAOImp();
//		Question question = new Question();
//		question.setTitle("你是谁");
//		question.setOptionA("faher");
//		question.setOptionB("faher");
//		question.setOptionC("faher");
//		question.setOptionD("faher");
//		question.setAnswer("A");
//		a.Insert(question);
		QuestionAction questionA = new QuestionAction();
		questionA.queryAll();
		
		
//		int a =1;
//		if(ObjisEmpty(a))System.out.print("true");
//		else System.out.print("false,优质");
//		DbUtils.loadDriver(DBconfig.getDRIVER());
//		QueryRunner queryRunner = new QueryRunner();
//		//更新的时候一定要注意，指定值。
//		String sql = "insert into exam_questions (title,optionA,optionB,optionC,optionD,answer) values(?,?,?,?,?,?)";
//		Connection conn = null;
//		try {
//			conn = DriverManager.getConnection(DBconfig.getURL(),DBconfig.getUSER(),DBconfig.getPASSWORD());
//			int row = queryRunner.execute(conn,sql, "你是谁","faher","爸爸","mother","妈妈","A");//不知道为啥这里非用string,
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				System.out.print("success");
//				DbUtils.close(conn);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
	}
	private static boolean ObjisEmpty(Object obj){
		if (obj == null) return true;
		if ((obj instanceof List))return ((List) obj).size() == 0;
		if ((obj instanceof String))return ((String) obj).trim().equals("");
		return false;
	}
}

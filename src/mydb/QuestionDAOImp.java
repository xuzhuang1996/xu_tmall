package mydb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

//真正跟数据库打交道的还是DAOImp啊
public class QuestionDAOImp implements QuestionDAO {
	private Connection conn;
	public QuestionDAOImp(){
		//conn = GetDBConn.getConnection();
		try {
			conn = GetC3p0DB.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int Insert(Question question) {
		try {
			if(conn.isClosed())conn=GetC3p0DB.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("reget-conn-Insert");
		//传入的值不确定，某些可能不传为空，这列就不操作。这个还没做
		if(question==null)return 0;
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into exam_questions (title,optionA,optionB,optionC,optionD,answer) values(?,?,?,?,?,?)";
		Connection conn = GetDBConn.getConnection();
		//不知道为啥，mysql里面char在插入的时候，不能用char，只能用string
		try {
			queryRunner.execute(conn,sql, question.getTitle(),question.getOptionA()
					            ,question.getOptionB(),question.getOptionC(),question.getOptionD(),question.getAnswer());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			System.out.println("insert-success");
			try {
				DbUtils.close(conn);
				System.out.println("insert-close");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 1;
		//不知道为啥这里非用string,
		//现在判断各个值是否为空,
		//想详细讨论的话，见https://www.bilibili.com/video/av28515960?p=2
//		StringBuffer s = new StringBuffer();
//		s.append("insert into table (");
//		if(!ObjisEmpty(question.getTitle()))s.append(",title");
//		s.append("");
//		//接下来消除第一个逗号。首先找到第一个逗号的位置，接着删除他就可以了
//		int i=s.indexOf(",");s.delete(i, i+1);
	}

	@Override
	public int Delete(int id) {
		try {
			if(conn.isClosed())conn=GetC3p0DB.getConnection();
			System.out.println("delete-conn-query");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		QueryRunner qr = new QueryRunner();
		try {
			int reslut = qr.update(conn, "delete from exam_questions WHERE id = ?", id);
			return reslut;//由于finally最终会执行后再返回这个数字，因此可以这样写
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				DbUtils.close(conn);
				System.out.println("delete-conn-close");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public int Update(Question question) {
		try {
			if(conn.isClosed())conn=GetC3p0DB.getConnection();
			System.out.println("update-conn-query");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		QueryRunner qr = new QueryRunner();
		try {
			//因为懒，没有更新其他地方
			int reslut = qr.update(conn, "UPDATE exam_questions set title=? WHERE id=?", question.getTitle(), question.getId());
			return reslut;//由于finally最终会执行后再返回这个数字，因此可以这样写
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				DbUtils.close(conn);
				System.out.println("update-conn-close");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public Question SelectById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Question> selectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Question> query(List<Map<String, Object>> params, String special) {
		try {
			if(conn.isClosed())conn=GetC3p0DB.getConnection();
			System.out.println("reget-conn-query");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Question> result = null;
		StringBuilder sql=new StringBuilder();
		//1=1是为了跟and一起用。不然where没地方放，如果集合不存在，直接执行下面语句也可以
		sql.append("select * from exam_questions where 1=1 ");
		if(params!=null&&params.size()>0){
			for (int i = 0; i < params.size(); i++) {
				Map<String, Object> map=params.get(i);
				sql.append(" and  "+map.get("name")+" "+map.get("rela")+" "+map.get("value")+" ");
			}
		}
		sql.append(special);//为了分页查询，需要在sql语句结尾加上limit 0,3这样的字符来选择分页的数量
		QueryRunner queryRunner = new QueryRunner();
		try {
			//如果不使用ubtiles，查询返回的数据需要result.next()，然后一条数据一条数据的赋值，再添加到list
			result = queryRunner.query(conn,sql.toString(), new BeanListHandler<Question>(Question.class));
			//System.out.println(result.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			System.out.println("query-success");
			try {
				DbUtils.close(conn);
				System.out.println("query-close");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	@Override
	public int countOfquestion() {
		try {
			if(conn.isClosed())conn=GetC3p0DB.getConnection();
			System.out.println("reget-conn-query");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int result = 0;
		StringBuilder sql=new StringBuilder();
		sql.append("select count(*) from exam_questions");//这里其实可以将count(*)换成字符串，自定义选择内容，跟那个条件选择一样
		QueryRunner queryRunner = new QueryRunner();
		try {
			//qr.query()返回object类型 ，先转成 ScalarHandler的Long类型 然后 在转为 int类型,这里原本不加<>，加了可以去掉一些感叹号
			result = ((Long)queryRunner.query(conn,sql.toString(),new ScalarHandler<>())).intValue();//ScalarHandler返回某一个值或者统计函数的值
		    //result = 
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			System.out.println("query-success");
			try {
				DbUtils.close(conn);
				System.out.println("query-count-close");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	//工具，判断是否为空，对值也行的通
//	private boolean ObjisEmpty(Object obj){
//		if (obj == null) return true;
//		if ((obj instanceof List))return ((List) obj).size() == 0;
//		if ((obj instanceof String))return ((String) obj).trim().equals("");
//		return false;
//	}

}

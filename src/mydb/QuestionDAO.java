/**
 * 
 */
package mydb;

import java.util.List;
import java.util.Map;

/**
 * 用于处理question的数据库表的操作接口
 *
 */
public interface QuestionDAO {
	int Insert(Question question);
	int Delete(int id);
	//传入一个完整的Question对象，每一个属性值，如果为null则不更新。有值则更新
	int Update(Question question);
	Question SelectById(int id);
	List<Question>selectByName(String name);
	List<Question>query(List<Map<String, Object>> params, String special);//最终版查询,自己加了一个特殊查询
	int countOfquestion();
}

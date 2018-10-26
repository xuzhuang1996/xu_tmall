package mydb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//这个类下面根据自己需要去实现函数方法
public class QuestionAction {
	private QuestionDAOImp questionDAOI;
	public QuestionAction(){
		questionDAOI = new QuestionDAOImp();
	}
	
	public void add(Question question){
		if(question!=null){
			questionDAOI.Insert(question);
		}
	}
	
	public int delete(int id){
		return questionDAOI.Delete(id);
	}
	
	public int update(Question question){
		return questionDAOI.Update(question);
	}
	
	//这个函数需要自己定义了，根据DAO的标准操作方式，来实现自己的查询。由于这里查询了所有数据，因此这里查询参数可以不写
	public List<Question> queryAll(){
		List<Map<String, Object>> Myparams = new ArrayList<Map<String,Object>>();
		List<Question> questionList = questionDAOI.query(Myparams,"");
		return questionList;
		//System.out.println(questionList);
	}
	//用于分页查询，
	public List<Question> queryAll(int index,int size){
		List<Map<String, Object>> Myparams = new ArrayList<Map<String,Object>>();
		//开始构造参数
		StringBuffer s = new StringBuffer();
		s.append("limit ");
		s.append(index);
		s.append(",");
		s.append(size);
		List<Question> questionList = questionDAOI.query(Myparams,s.toString());
		return questionList;
	}
	//用于查询统计数据，后续有需要可以更改查询条件
	public int count(){
		return questionDAOI.countOfquestion();
	}
	//是否存在某一重复字段的值，这里题目是否重复
	public List<Question> getAllTitle(String title){
		List<Map<String, Object>> Myparams = new ArrayList<Map<String,Object>>();
		Map<String,Object> Myparam = new HashMap<String,Object>();
		Myparam.put("name", "title");
		Myparam.put("rela", "=");
		Myparam.put("value", "'"+title+"'");
		Myparams.add(Myparam);
		List<Question> questionList = questionDAOI.query(Myparams,"");
		return questionList;
	}
}

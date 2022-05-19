package kr.co.doiloppa.common.abstractinfo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AbstractDao2 {
	
	@Resource(name = "sqlSessionTemplate2")
	protected SqlSessionTemplate sqlSession;
	
	
	/**
	 * Query�� ID �� ���(Debug��� �϶���)
	 * @param queryId
	 */
	protected void printQueryId(String queryId) {
		//log4j 삭제
	}
	
	/**
	 * Batch�� CUD ó���� �Ҷ� ȣ�� <br/>
	 *  - CUD�� �ڼ��� ������ ���
	 * @param List<Map<String, Object>> lists = CUD Map = key : queryId, param, CUD
	 */
	public void multiBatch(List<Map<String, Object>> lists){
		
		SqlSession session = sqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH);
		
		for(Map<String, Object> map : lists){
			printQueryId(map.get("queryId").toString());
			// map�� key : queryId, param, CUD
			switch (map.get("CUD").toString()) {
			case "insert":
				session.insert(map.get("queryId").toString(), map.get("param"));
				break;
			case "update":
				session.update(map.get("queryId").toString(), map.get("param"));
				break;
			case "delete":
				session.delete(map.get("queryId").toString(), map.get("param"));
				break;
			}
		}
		
		session.flushStatements();
		session.close();
	}

	/**
	 * insert ó���� �Ҷ� ȣ��
	 * @param queryId
	 * @param params
	 * @return
	 */
	public Integer insert(String queryId, Object params){
		printQueryId(queryId);
		return sqlSession.insert(queryId, params);
	}
	
	/**
	 * Batch�� insert ó���� �Ҷ� ȣ��
	 * @param List<Map<String, Object>> lists = insert Map�� key : queryId, param
	 */
	public void insertBatch(List<Map<String, Object>> lists){
		
		SqlSession session = sqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH);
		
		for(Map<String, Object> map : lists){
			printQueryId(map.get("queryId").toString());
			// Map�� key : queryId, param
			session.insert(map.get("queryId").toString(), map.get("param"));
		}
		
		session.flushStatements();
		session.close();
	}

	/**
	 * update ó���� ȣ��
	 * @param queryId
	 * @param params
	 * @return
	 */
	public Integer update(String queryId, Object params){
		printQueryId(queryId);
		return sqlSession.update(queryId, params);
	}
	
	/**
	 * Batch�� update ó���� �Ҷ� ȣ��
	 * @param List<Map<String, Object>> lists = update Map�� key : queryId, param
	 */
	public void updateBatch(List<Map<String, Object>> lists){
		
		SqlSession session = sqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH);
		
		for(Map<String, Object> map : lists){
			printQueryId(map.get("queryId").toString());
			// Map�� key : queryId, param
			session.update(map.get("queryId").toString(), map.get("param"));
		}
		
		session.flushStatements();
		session.close();
	}

	/**
	 * delete ó���� �Ҷ� ȣ��
	 * @param queryId
	 * @param params
	 * @return
	 */
	public Integer delete(String queryId, Object params){
		printQueryId(queryId);
		return sqlSession.delete(queryId, params);
	}
	
	/**
	 * Batch�� delete ó���� �Ҷ� ȣ��
	 * @param List<Map<String, Object>> lists = delete Map�� key : queryId, param
	 */
	public void deleteBatch(List<Map<String, Object>> lists){
		
		SqlSession session = sqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH);
		
		for(Map<String, Object> map : lists){
			printQueryId(map.get("queryId").toString());
			// Map�� key : queryId, param
			session.delete(map.get("queryId").toString(), map.get("param"));
		}
		
		session.flushStatements();
		session.close();
	}
	
	/**
	 * ���� select
	 * @param queryId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T selectOne(String queryId){
		printQueryId(queryId);
		return (T) sqlSession.selectOne(queryId);
	}
	
	/**
	 * ���� select
	 * @param queryId
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T, P> T selectOne(String queryId, P params){
		printQueryId(queryId);
		return (T) sqlSession.selectOne(queryId, params);
	}

	/**
	 * ���� select
	 * @param queryId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(String queryId){
		printQueryId(queryId);
		return (List<T>) sqlSession.selectList(queryId);
	}

	/**
	 * ���� select
	 * @param queryId
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public <T, P> List<T> selectList(String queryId, P params){
		printQueryId(queryId);
		return (List<T>) sqlSession.selectList(queryId, params);
	}

}

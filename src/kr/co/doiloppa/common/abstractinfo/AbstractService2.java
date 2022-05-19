package kr.co.doiloppa.common.abstractinfo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

public class AbstractService2 {
	
	@Resource(name="abstractDao2")
	private AbstractDao2 dao;

	public Integer insert(String queryId, Object params){
		return dao.insert(queryId, params);
	}
	 
	public Integer update(String queryId, Object params){
		return dao.update(queryId, params);
	}
	 
	public Integer delete(String queryId, Object params){
		return dao.delete(queryId, params);
	}
	 
	@SuppressWarnings("unchecked")
	public <T> T selectOne(String queryId){
		return (T) dao.selectOne(queryId);
	}
	 
	@SuppressWarnings("unchecked")
	public <T, P> T selectOne(String queryId, P params){
		return (T) dao.selectOne(queryId, params);
	}
	 
	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(String queryId){
		return (List<T>) dao.selectList(queryId);
	}
	 
	@SuppressWarnings({ "unchecked" })
	public <T, P> List<T> selectList(String queryId, P params){
		return (List<T>) dao.selectList(queryId, params);
	}

	public List<CmcdBean> getCmcdList(String cat_cd){
		return getCmcdList(cat_cd, null);
	}
	public List<CmcdBean> getCmcdList(String cat_cd, String use_yn){
		//�����ڵ� ��ȸ
		CmcdBean cmcdBean = new CmcdBean();
		cmcdBean.setCat_cd(cat_cd);
		cmcdBean.setUse_yn(use_yn);
		
		try {
			return selectList("codeSqlMap.getCmCdList", cmcdBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<CmcdBean> getCmcdList(String cat_cd, String p_cd, String use_yn){
		//�����ڵ� ��ȸ
		CmcdBean cmcdBean = new CmcdBean();
		cmcdBean.setCat_cd(cat_cd);
		cmcdBean.setP_cd(p_cd);
		cmcdBean.setUse_yn(use_yn);
		
		try {
			return selectList("codeSqlMap.getCmCdList", cmcdBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public CmcdBean getCmcdOne(String cat_cd, String cd, String use_yn){
		//�����ڵ� ��ȸ
		CmcdBean cmcdBean = new CmcdBean();
		cmcdBean.setCat_cd(cat_cd);
		cmcdBean.setCd(cd);
		cmcdBean.setUse_yn(use_yn);
		
		try {
			return selectOne("codeSqlMap.getCmCdOne", cmcdBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public CmcdBean getCmAttrOne(String cat_cd){
		//�����ڵ� ��ȸ
		CmcdBean cmcdBean = new CmcdBean();
		cmcdBean.setCat_cd(cat_cd);
		
		try {
			return selectOne("codeSqlMap.getCmAttrOne", cmcdBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Mail �߼� ����
	 */
	public void insertMailSchedule(Map<String, Object> map) {
		dao.insert("objectSqlMap.insertMailSchedule", map);
	}
}

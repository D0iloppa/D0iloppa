package kr.co.hhi.common.abstractinfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author u.jyung
 *
 */
public class CommonBean {
	private Integer rn;

	private Integer page;
	private Integer cntPerPage;
	private Integer startRowNum;
	private Integer endRowNum;

	private Integer startPage;
	private Integer endPage;
	private Integer nowPage;
	private Integer count;
	private Integer cnt;
	private Integer rowNumber;
	private Integer pageSize;
	private int queryPage;

	private String excelFileName;
	private String columnList;
	private String titleList;

//	private String user_id;
	private String ssoid;
	private String login_user_id;

	private String action_type;

	private String callbackFn;

	private String use_yn;// 사용유무
	private String gen_by;// 작성자
	private Date gen_dt;// 작성일
	private String mdfy_by;// 수정자
	private Date mdfy_dt;// 수정일

	private String keyword; // 검색키워드
	private String key_type;// 검색조건
	private String searchKeyword;

	
	private Integer auth_gr_no;
	
	private int limitStart;
	private int rowCnt;
	
	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getRowCnt() {
		return rowCnt;
	}

	public void setRowCnt(int rowCnt) {
		this.rowCnt = rowCnt;
	}

	public Integer getAuth_gr_no() {
		return auth_gr_no;
	}

	public void setAuth_gr_no(Integer auth_gr_no) {
		this.auth_gr_no = auth_gr_no;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKey_type() {
		return key_type;
	}

	public void setKey_type(String key_type) {
		this.key_type = key_type;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	private List<Map<String, Object>> bean_arr;

	public int getQueryPage() {
		return queryPage;
	}

	public void setQueryPage(int queryPage) {
		this.queryPage = queryPage;
	}

	public String getSsoid() {
		return ssoid;
	}

	public void setSsoid(String ssoid) {
		this.ssoid = ssoid;
	}

	public String getTitleList() {
		return titleList;
	}

	public void setTitleList(String titleList) {
		this.titleList = titleList;
	}

	public String getColumnList() {
		return columnList;
	}

	public void setColumnList(String columnList) {
		this.columnList = columnList;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getRn() {
		return rn;
	}

	public void setRn(Integer rn) {
		this.rn = rn;
	}

	public Integer getPage() {
		if (page == null) {
			page = 1;
		}
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
		queryPage = (page - 1) * 10;
	}

	public Integer getCntPerPage() {
		return cntPerPage;
	}

	public void setCntPerPage(Integer cntPerPage) {
		this.cntPerPage = cntPerPage;
	}

	public Integer getStartRowNum() {
		return startRowNum;
	}

	public void setStartRowNum(Integer startRowNum) {
		this.startRowNum = startRowNum;
	}

	public Integer getEndRowNum() {
		return endRowNum;
	}

	public void setEndRowNum(Integer endRowNum) {
		this.endRowNum = endRowNum;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getEndPage() {
		return endPage;
	}

	public void setEndPage(Integer endPage) {
		this.endPage = endPage;
	}

	public Integer getNowPage() {
		return nowPage;
	}

	public void setNowPage(Integer nowPage) {
		this.nowPage = nowPage;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getCallbackFn() {
		return callbackFn;
	}

	public void setCallbackFn(String callbackFn) {
		this.callbackFn = callbackFn;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}
/*
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
*/
	public List<Map<String, Object>> getBean_arr() {
		return bean_arr;
	}

	public void setBean_arr(List<Map<String, Object>> bean_arr) {
		this.bean_arr = bean_arr;
	}

	public String getLogin_user_id() {
		return login_user_id;
	}

	public void setLogin_user_id(String login_user_id) {
		this.login_user_id = login_user_id;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getGen_by() {
		return gen_by;
	}

	public void setGen_by(String gen_by) {
		this.gen_by = gen_by;
	}

	public Date getGen_dt() {
		return gen_dt;
	}

	public void setGen_dt(Date gen_dt) {
		this.gen_dt = gen_dt;
	}

	public String getMdfy_by() {
		return mdfy_by;
	}

	public void setMdfy_by(String mdfy_by) {
		this.mdfy_by = mdfy_by;
	}

	public Date getMdfy_dt() {
		return mdfy_dt;
	}

	public void setMdfy_dt(Date mdfy_dt) {
		this.mdfy_dt = mdfy_dt;
	}

}

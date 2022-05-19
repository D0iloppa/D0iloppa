<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

<input type="hidden" id="contextpath" value="${pageContext.request.contextPath}">

<h4>DRN REVIEW CONCLUSION</h4>
<div class="pg_top">
	<div class="line mb20">
       <div class="left">
				<select name="" id="drnReviewcodeType" class="pr_gn" style="display: none;">
					<option value="#sel_DrnReivew">REVIEW CONCLUSION</option>
				</select>
             						<input type="checkbox" name="copyCheck" id="copyCheck">
                                        <label for="" class="mr10">이전공사 COPY</label>
                                        <select name="" id="copyProject">
                                        	<c:forEach var="i" items="${getPrjList}" varStatus="vs">
                                            	<option value="${i.prj_id}">${i.prj_nm}</option>
											</c:forEach>
                                        </select>
                                        <button class="button btn_default" id="copyBtn" style="margin-left:10px; display: none;" onclick="Copy()">Copy</button>
                                     <input type="hidden" id="selectPrjId">
			</div>
    	</div>    
    	<div class="right">
             <input type="hidden" id="">
             <button class="button btn_default" style="margin-left:10px;" onclick="apply()">Apply</button>
        </div>
</div>

								<div class="sec">
                                    <div class="tbl_wrap">
                                        <table class="write">
                                            <colgroup>
                                                <col style="width:150px">
                                                <col style="*">
                                            </colgroup>
                                            <tr>
                                                <th><span class="so_required">*</span>Review Conclusion</th>
                                                <td><input type="text" style="width: 100%;" id="review_conclusion"></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                
<!-- <h4>Code</h4>
		<div class="sel_tab">
			<div id="sel_DrnReivew" class="on">
				<div class="tbl_wrap" id="DrnReivew"></div>
			</div>
		</div> -->

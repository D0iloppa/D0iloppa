<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="dialog pop_searchTR" title="Search TR" style="max-width:1000px;">
    <div class="pop_box">
                                                <div style="color:red;">
													※ 참조할 Doc. No를 입력하세요. 해당 도서가 첨부된 TR이 검색됩니다.
												</div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
                                                        <tr>
                                                            <th>
                                                                Ref.DocNo
                                                            </th>
                                                            <td style="display:flex;">
                                                                <input type="text" class="full" id="ref_doc_no">
                												<button class="button btn_blue" onclick="getSearchTR()" style="width:120px;margin-left:5px;">SEARCH TR</button>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    
                                                    <div class="tbl_wrap scroll" id="TrNoList">
                                    				</div>
                                    				<input type="hidden" id="selectTR">
                                                </div>
            <div class="right" style="margin:15px;">
                <!-- <button class="button btn_purple" onclick="searchTRCancel()">Cancel</button> -->
                <button class="button btn_blue" onclick="selectTRonclick()">Select TR</button>
            </div>
    </div>
</div>

<div class="dialog pop_TrDelete2" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete this TR?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="in_delete()">Confirm</button>
                <button class="button btn_purple" onclick="TRInDeleteCancel()">No</button>
            </div>
    </div>
</div>
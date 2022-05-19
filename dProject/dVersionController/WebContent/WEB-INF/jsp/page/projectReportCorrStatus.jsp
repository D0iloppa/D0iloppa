<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<div class="pg_top">
                                    <div class="right">
                                        <button class="button btn_blue" onclick="RetrieveCorr();">Retrieve</button>
                                        <button class="button btn_purple" onclick="downloadReportCorr();">To Excel</button>
                                    </div>
                                </div>
                                <div class="sch_area">
                                    <div class="line">
                                        <div class="left">
                                            <label for="" class="mr5" >Document Date</label>
                                            <input type="text" class="date" id="documnet_dateS">~<input type="text" class="date" id="documnet_dateD">
                                        </div>
                                        <div class="right">
                                            <label for="" style="line-height: 24px; margin-right: 5px;">In/Out</label>
                                            <span><input type="radio" id="corrInOutRadio1" name="corrInOutRadio" value="O" checked><label for="">Outgoing</label></span>
                                            <span><input type="radio" id="corrInOutRadio2" name="corrInOutRadio" value="I"><label for="">Incoming</label></span>
                                        </div>
                                    </div>
                                </div>
                                <p class="result_line"><span>RESULT</span></p>
                                <div class="sec">
                                     <h4 class="reportTitleC" style="float: left;"></h4>
	                                <label id="reportCntListC" style="margin-left: 10px;"></label>
                                     <div class="tbl_wrap corrStatus" id="data_table_corres">
                                    </div>
                                </div>
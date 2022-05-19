<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

								<h4>Daily Notification RECEIVER 설정</h4>
								<div class="sec">
                                    <div class="tbl_wrap">
                                        <table class="write project_Left_title">
                                            <colgroup>
                                                <col style="width:150px">
                                                <col style="*">
                                            </colgroup>
                                            <tr>
                                                <th><span class="so_required">*</span>RECEIVER</th>
                                                <td>
                                                	<button class="button btn_small btn_blue" id="notificationSet" onclick="OrganizationChartMultiOpenNotificationSet()">설정하기</button>
                                                	<span id="NOTINAMELIST"></span>
	                                        		<input type="hidden" id="NOTIRECEIVERLIST">
             									</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                
											<div class="sec">
                                            	<div class="gray_small_box" id="Notification_small_box">
										            <ul class="user_name" id="selectIdUserNameListNotification">
										         	</ul>
										        </div>
										    </div>
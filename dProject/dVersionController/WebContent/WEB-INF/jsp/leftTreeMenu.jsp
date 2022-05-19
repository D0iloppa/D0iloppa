 <%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
 <section id="menu" resizable r-directions="['right']" r-flex="true" class="menu">
  <nav id="tree">
  </nav> 
<!-- 폴더삭제팝업 -->
<div class="dialog pop_deletefolder" title="Delete folder">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
    	Do you want to delete a selected folder?<br/>
    	(Selected folder: <span id="selectedFolder"></span>)
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="deletedSelectedFolder">Delete</button>
                <button class="button btn_blue" id="deletedCancel">Cancel</button>
            </div>
    </div>
</div>
</section>
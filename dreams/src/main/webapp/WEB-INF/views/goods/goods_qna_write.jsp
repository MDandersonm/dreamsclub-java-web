<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!--문의하기-->
<form id="qnaWrite_input" action="<c:url value='/goods'/>" method="post" enctype="multipart/form-data"> <!-- 제출시 이동됨 -->
<input type="hidden" id="qnaWriter" name="memberId" value="${member.memberId }">
<div class="boardWriteMain">
    <div class="boardWriteTitle" style="text-align: center; font-weight: bold; color: #003E00;">
        <h3 style="margin-bottom: 40px;">문의하기</h3>
    </div>
    
    <div class="boardWriteContainer">
        <table class="baordWriteTable">
         <!--제목 입력-->
            <tr>
                <th>제목</th>
                <td>
                    <input type="text" id="qnaWriteTitle" name="qnaTitle" placeholder="제목을 입력해주세요." 
                    	value="${pageInfo.qnaTitle }">
                </td>
            </tr>  
        <!--내용-->
            <tr>
                <th>내용</th>
                <td>
                    <textarea id="qnaWriteContent" name="qnaCont" class="qnaWriteContent" placeholder="내용을 입력해주세요."
                    value="${pageInfo.qnaCont}"></textarea>
                </td>
            </tr>

        </table>


        <!--작성 버튼(취소, 등록)-->
        <div class="qnaWriteBtn">
            <a href="<c:url value="/goods"/>">취소</a> <!-- 현페이지 어케함 -->
            <a href="javascript:chk_form();">등록</a>
        </div>
    </div>
</div>
</form>

<script type="text/javascript">


<!-- 작성되면 창뜨게함 -->
function chk_form() {
	if(document.getElementById("qnaWriteTitle").value==''){
		alert("제목을 입력해주십시오.");
		return false;
	}
	if(document.getElementById("qnaWriteContent").value==''){
		alert("내용을 입력해주십시오.");
		return false;
	}
	document.getElementById('qnaWrite_input').submit();
	  alert("게시글이 등록되었습니다.");
}


</script>
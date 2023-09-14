package xyz.dreams.dao;

import java.util.List;
import java.util.Map;

import xyz.dreams.dto.CommunityDTO;

public interface CommunityDAO {
	/*게시판 등록*/
	int enrollCommunity(CommunityDTO community);

	/*게시판 글 수정*/
	int modifyCommunity(CommunityDTO community);
	
	/*게시판 삭제*/
	int deleteCommunity(int commNo);
	
	/*게시판 글 하나 보기(조회)*/
	CommunityDTO getPage(int communityNo);
	
	/*게시글 조회수 증가*/
	void upCountCommunity(int commNo);
	
	/*게시판 목록*/
	//List<CommunityDTO> getList();
	
	/*페이징 처리*/
	List<CommunityDTO> selectCommunityList(Map<String, Object> map);
	int selectCommunityCount(Map<String, Object> map);  //게시글 총 개수
	
	/*게시글 넘버 => 댓글이용시사용*/
	CommunityDTO selectCommunityByCommNo(int commNo);
}

package xyz.dreams.controller;

import java.util.List;

import javax.servlet.http.HttpSession;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import xyz.dreams.dto.CommunityReplyDTO;
import xyz.dreams.service.CommunityReplyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class CommunityReplyController {
	private final CommunityReplyService communityReplyService;
	
	/*김예지(2023.09.14) - 댓글 등록(비동기식)*/
	@PostMapping("/register")
	public String register(@RequestBody CommunityReplyDTO reply) {
		communityReplyService.addCommunityReply(reply);
		return "success";
	}
	
	
	/*김예지(2023.09.13) - 댓글 목록 출력*/
	//게시판 번호(commNo)가 같은 댓글들의 목록을 한번에 출력
	@GetMapping("/list/{commNo}")
	public List<CommunityReplyDTO> list(@PathVariable int commNo, HttpSession session) throws Exception{

		return communityReplyService.getCommunityReplyList(commNo);
		
	}
	
		
	/*김예지(2023.09.15) - 댓글 삭제*/
	//commReNo를 조건으로 일치하면 댓글 삭제(비동기식)
	@DeleteMapping("/delete/{commReNo}")
	public String deleteReply(@PathVariable int commReNo) {
		communityReplyService.deleteCommunityReply(commReNo);
		return "success";
	}
	
	
	/*김예지(2023.09.18) - 댓글 수정*/
	//body에 CommunityReplyDTO 필드의 내용을 담아와 수정(비동기식)
	@PutMapping("/update")
	public String updateReply(@RequestBody CommunityReplyDTO reply) {
		communityReplyService.modifyCommunityReply(reply);
		return "success";
	}
	
}

package xyz.dreams.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.HtmlEmail;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.dreams.dao.MemberDAO;
import xyz.dreams.dto.MemberDTO;
import xyz.dreams.exception.LoginAuthFailException;
import xyz.dreams.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberDAO memberDAO;

	// 강민경: 매개변수로 회원정보(아이디와 비밀번호)를 전달받아 인증 처리하기 위한 메소드
	// =>인증 성공: 매개변수로 전달받은 아이디의 회원정보를 검색하여 반환 /실패 시 예외 발생
	@Override
	public MemberDTO loginAuth(MemberDTO member) throws LoginAuthFailException {
		// 매개변수로 전달받은 회원정보의 아이디로 기존 회원정보를 검색하여 검색결과를 반환받아 저장
		MemberDTO authMember = memberDAO.selectLoginCheck(member.getMemberId());

		if (authMember == null) {
			throw new LoginAuthFailException("회원정보가 존재하지 않습니다.", member.getMemberId());
		}

		// 매개변수로 전달받은 회원정보의 비밀번호와 검색된 회원정보의 비밀번호를 비교하여
		// 같지 않은 경우 - 비밀번호 인증 실패
		if (!BCrypt.checkpw(member.getMemberPw(), authMember.getMemberPw())) {
			throw new LoginAuthFailException("아이디가 없거나 비밀번호가 맞지 않습니다.", member.getMemberId());
		}

		// 매개변수로 전달받은 회원정보의 아이디로 검색된 회원정보 반환
		return authMember;
	}

	// 강민경: 매개변수로 회원정보(이름, 이메일)를 전달받아 인증 처리하기 위한 메소드
	// =>인증 성공: 매개변수로 전달받은 아이디의 회원정보를 검색하여 반환 /실패 시 예외 발생
	@Override
	public String searchId(MemberDTO member) {
		String id = memberDAO.selectSearch(member);

		// 매개변수로 전달받은 회원정보의 아이디로 검색된 회원정보 반환
		return id;
	}
	//비밀번호 찾기 이메일발송
		@Override
		public void sendEmail(MemberDTO member, String div) throws MemberNotFoundException {
			// Mail Server 설정
			String charSet = "utf-8";
			String hostSMTP = "smtp.gmail.com"; //네이버 이용시 smtp.naver.com
			String hostSMTPid = "findusertest@gmail.com";
			String hostSMTPpwd = "ztkksmyjldkgfqag";

			// 보내는 사람 EMail, 제목, 내용
			String fromEmail = "findusertest@gmail.com";
			String fromName = "드림즈 관리자";
			String subject = "";
			String msg = "";

			if(div.equals("searchPwBtn")) {
				subject = " 임시 비밀번호 입니다.";
				msg += "<div align='center' style='border:1px solid black; font-family:verdana'>";
				msg += "<h3 style='color: blue;'>";
				msg += member.getMemberId() + "님의 임시 비밀번호 입니다. 비밀번호를 변경하여 사용하세요.</h3>";
				msg += "<p>임시 비밀번호 : ";
				msg += member.getMemberPw() + "</p></div>";
			}

			// 받는 사람 E-Mail 주소
			String mail = member.getMemberEmail();
			try {
				
				HtmlEmail email = new HtmlEmail();
				email.setDebug(true);
				email.setCharset(charSet);
				email.setSSL(true);
				email.setHostName(hostSMTP);
				email.setSmtpPort(465); //네이버 이용시 587

				email.setAuthentication(hostSMTPid, hostSMTPpwd);
				email.setTLS(true);
				email.addTo(mail, charSet);
				email.setFrom(fromEmail, fromName, charSet);
				email.setSubject(subject);
				email.setHtmlMsg(msg);
				email.send();
			} catch (Exception e) {
				System.out.println("메일발송 실패 : " + e);
			}
		}
		//비밀번호찾기
		@Override
		public void searchPw(HttpServletResponse response, MemberDTO member) throws MemberNotFoundException {
			response.setContentType("text/html;charset=utf-8");
			MemberDTO memberDTO = memberDAO.selectLoginCheck(member.getMemberId());
			
			// 가입된 아이디가 없으면
			if(memberDTO == null) {
				throw new MemberNotFoundException("등록되지 않은 아이디입니다.", member.getMemberId());
				//throw를 MemberNotFoundException로 할지 아님 LoginAuthFailException으로 할지 고민 
				
			}
			// 가입된 이메일이 아니면
			else if(!member.getMemberEmail().equals(memberDTO.getMemberEmail()))  {
				throw new MemberNotFoundException("등록되지 않은 이메일입니다.", member.getMemberId());
				
			}else {
				// 임시 비밀번호 생성
				String uuid= UUID.randomUUID().toString().replaceAll("-", "").substring(0,10);
				// 비밀번호 변경
				String hashedPw = BCrypt.hashpw(uuid, BCrypt.gensalt());
				member.setMemberPw(hashedPw);
				memberDAO.updatePw(member);
				member.setMemberPw(uuid);
				
				// 비밀번호 변경 메일 발송
				sendEmail(member, "searchPwBtn");
			}
		}
			
	
	
	/*
	 * // 오진서 2 ▼ public void addMember(MemberDTO member) throws
	 * ExistsMemberException { // 매개변수로 전달받은 회원정보의 아이디가 기존 회원정보의 아이디와 중복될 경우 if
	 * (memberDAO.selectMember(member.getMemberId()) != null) { // 예외를 명확하기 구분하고
	 * 예외처리시 필요한 값을 제공하기 위해 직접 생성한 예외 // 클래스를 사용하여 인위적인 예외 발생 throw new
	 * ExistsMemberException("이미 사용중인 아이디를 입력 하였습니다.", member); }
	 * 
	 * // 매개변수로 전달받은 회원정보의 비밀번호를 암호화 처리하여 필드값으로 다시 저장 String hashedPassword =
	 * BCrypt.hashpw(member.getMemberPw(), BCrypt.gensalt());
	 * member.setMemberPw(hashedPassword);
	 * 
	 * memberDAO.insertMember(member);
	 * 
	 * }
	 */
	
	
	// 진서 암호화▼
	@Override
	public void addMember(MemberDTO member) {
		String hashedPassword = BCrypt.hashpw(member.getMemberPw(), BCrypt.gensalt());
		member.setMemberPw(hashedPassword);

		memberDAO.insertMember(member);
	}
	
//	// 진서 자가 아이디체크,, 어캐했
//	public String member_id_check (String memberId) {
//		Optional <MemberEntity> optionMemberEntity = memberRepository.finByMemberId(memberId);
//	}
	
	
	// 소영 ▼
	@Override
	public void modifyMember(MemberDTO member) {
		String hashedPassword = BCrypt.hashpw(member.getMemberPw(), BCrypt.gensalt());
		member.setMemberPw(hashedPassword);

		memberDAO.updateMember(member);
	}

	@Override
	public void removeMember(String memberId) {
		// TODO Auto-generated method stub

	}

	@Override
	public MemberDTO getMember(String memberId) {
		// TODO Auto-generated method stub
		return memberDAO.selectMember(memberId);
	}

	@Override
	public List<MemberDTO> getMemberList() {
		return memberDAO.selectMemberList();
	}

}

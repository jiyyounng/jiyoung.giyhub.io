package com.jachmi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jachmi.domain.MemberVO;
import com.jachmi.domain.RentVO;
import com.jachmi.mapper.MemberMapper;
import com.jachmi.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j 
@RequestMapping("/member/*") //url맵핑과 같은역활
@AllArgsConstructor 	//풀생성자사용으로도 
public class MemberController {
	
	private MemberService service;
	
	@Autowired 
	private MemberMapper mapper;
	
	//내위치설정 팝업창이동
	@GetMapping("/mylocation")
	public String test() {
		return "member/mylocation";
	}
	
	//내위치설정값 받아서 메인에넘기기
	@PostMapping("/mylocation")
	public String test1(HttpServletRequest request,@RequestParam("inputNm") String centerAddr) {
		HttpSession session =request.getSession();	
		session.setAttribute("centerAddr", centerAddr);
		
		return "main";
	}
	
	// 로그인페이지
	@GetMapping("/login")
	public String login() {
		return "member/login";
	}
	// 로그인
	@PostMapping("/login")
	public String signIn(MemberVO vo, HttpServletRequest request,@RequestParam("id") String id) {
		MemberVO signIn = mapper.signIn(vo);
		HttpSession session =request.getSession();	
		if (signIn != null) {
			session.setAttribute("signIn", signIn);
			session.setAttribute("id", id);
			
			
			return "redirect:/";
		} else {
			session.setAttribute("signIn", null);
			return "redirect:/login.do";
		}
	}
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	//회원가입
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join() {
		return "member/join";
	}
	
	@PostMapping("/join")
	public String register(MemberVO vo) {
		log.info("join ... ");
		service.insertMM(vo);
		
		return "redirect:/member/login";
	}
	
	
	
	//마이페이지
	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public String mypage() {
		return "member/mypage";
	}
	//회원정보
	@RequestMapping(value = "/myinfo", method = RequestMethod.GET)
	public String myinfo() {
		return "member/myinfo";
	}
	@RequestMapping(value = "/myinfoupdate", method = RequestMethod.GET)
	public String myinfo2() {
		return "member/myinfoupdate";
	}
	
	//회원정보 수정
	/*
	@PostMapping("/modify")
	public String modify(MemberVO vo, @RequestParam("id")String id, Model model) {
		log.info("modify....");
		service.modifyMM(vo);
		model.addAttribute("member", service.signIn(vo));
		return "redirect:/member/myinfo";
	}
	*/
	
	@PostMapping(value = "/myinfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public MemberVO modify1(@RequestBody Map<String, String> param, HttpSession session) {

		log.info("keySet : " + param.keySet());
		log.info("id value = " + param.get("id"));
		log.info("email value = " + param.get("email"));

		MemberVO vo = new MemberVO();
		//session.removeAttribute(vo.getEmail());

		vo.setId(param.get("id"));
		vo.setEmail(param.get("email"));
		
		service.modifyMM(vo);
		
		MemberVO signIn = mapper.signIn(vo);
		session.setAttribute("signIn", signIn);
		
		return vo;
	}
	@PostMapping(value = "/myinfo2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public MemberVO modify2(@RequestBody Map<String, String> param, HttpSession session) {
		
		log.info("keySet : " + param.keySet());
		log.info("id value = " + param.get("id"));
		log.info("c_main_address value = " + param.get("c_main_address"));
		log.info("addr_ck value = " + param.get("addr_ck"));
		
		MemberVO vo = new MemberVO();
		
		vo.setId(param.get("id"));
		vo.setAddr(param.get("c_main_address"));
		vo.setAddr_ck(param.get("addr_ck"));
		
		service.modifyMM2(vo);
		
		MemberVO signIn = mapper.signIn(vo);
		session.setAttribute("signIn", signIn);
		
		return vo;
	}
	
	//회원탈퇴
	@GetMapping("/unregister")
	public String unregister() {
		return "member/unregister";
	}
	
	//회원탈퇴2
	@GetMapping("/unregister2")
	public String unregister2() {
		return "member/unregister2";
	}
	
	@PostMapping("/unregister3")
	public String unregister3(MemberVO vo, RedirectAttributes rttr, HttpSession session) {
		log.info("unregister3 ... " + vo);
		
		service.deleteMM2(vo);
		session.invalidate();
		
		return "redirect:/";
	}
	
	@RequestMapping("/idCheck")
	@ResponseBody
	public int idCheck(String id) {
		log.info("idCheck..." + id);
		
		int result = service.idCheckM(id);
		
		return result;
	}

}
	
	
	


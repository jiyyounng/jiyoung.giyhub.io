package com.jachmi.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jachmi.domain.RentAttachVO;
import com.jachmi.domain.RentInfoVO;
import com.jachmi.domain.RentVO;
import com.jachmi.service.RentInfoService;
import com.jachmi.service.RentService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;


@Controller
@Log4j
@RequestMapping("/jiyoung/*")
@AllArgsConstructor
public class RentController {

	private RentService service;

	
	// 메인페이지 이동
	@GetMapping("/rent_main")
	public String iamMain() {
		return "jiyoung/rent_main";
	}
	@GetMapping("/date")
	public String iamdate() {
		return "jiyoung/date";
	}
	
	// 카테고리별 리스트
	@GetMapping("/Electronic")
	public String iamElectronic(@RequestParam("p_category")int p_category,Model model, HttpServletRequest request) {
		log.info("list....");
		
		model.addAttribute("list", service.getList(p_category));
		
		return "jiyoung/Electronic";
	}
	
	@GetMapping("/Living")
	public String iamLiving(@RequestParam("p_category")int p_category, Model model) {
		log.info("list....");
		
		model.addAttribute("list", service.getList(p_category));
		return "jiyoung/Living";
	}
	
	@GetMapping("/Etc")
	public String iamEtc(@RequestParam("p_category")int p_category, Model model) {
		log.info("list....");
		
		model.addAttribute("list", service.getList(p_category));
		return "jiyoung/Etc";
	}
	
	//상세페이지
	@GetMapping("/Electronic_info")
	public String iamEInfo(@RequestParam("p_num") int p_num, Model model) {
		log.info("move to elect info...");
		
		model.addAttribute("vo", service.get(p_num));
		return "jiyoung/Electronic_info";
	}
	@GetMapping("/Living_info")
	public String iamLInfo(@RequestParam("p_num") int p_num, Model model) {
		log.info("move to elect info...");
		
		model.addAttribute("vo", service.get(p_num));
		return "jiyoung/Living_info";
	}
	
	@GetMapping("/Etc_info")
	public String iamEtcInfo(@RequestParam("p_num") int p_num, Model model) {
		log.info("move to elect info...");
		
		model.addAttribute("vo", service.get(p_num));
		return "jiyoung/Etc_info";
	}
	
	// 게시글 등록
	@GetMapping("/owner_register")
	public String iamOwner() {
		log.info("move page...");
		return "jiyoung/owner_register";
	}
	@PostMapping("/owner_register")
	public String register(RentVO vo) {
		log.info("register ... ");
		service.register(vo);
		
		return "redirect:/jiyoung/rent_main";
	}
	
	// 수정페이지 이동
	@GetMapping("/Electronic_info_update")
	public String emodify(@RequestParam("p_num") int p_num, Model model) {
		log.info("move page...");
		model.addAttribute("vo", service.get(p_num));
		return "jiyoung/Electronic_info_update";
	}
	@GetMapping("/Living_info_update")
	public String lmodify(@RequestParam("p_num") int p_num, Model model) {
		log.info("move page...");
		model.addAttribute("vo", service.get(p_num));
		return "jiyoung/Living_info_update";
	}

	@GetMapping("/Etc_info_update")
	public String etcmodify(@RequestParam("p_num") int p_num, Model model) {
		log.info("move page...");
		model.addAttribute("vo", service.get(p_num));
		return "jiyoung/Etc_info_update";
	}
	
	// 수정
	@PostMapping("/Electronic_info_update")
	public String emodify(RentVO vo, Model model, @RequestParam("p_num") int p_num, RedirectAttributes rttr) {
		log.info("modify ... ");
		service.Modify(vo);
		model.addAttribute("vo", service.get(p_num));
		rttr.addAttribute("p_num", vo.getP_num());
		return "redirect:/jiyoung/Electronic_info";
	}
	@PostMapping("/Living_info_update")
	public String lmodify(RentVO vo, Model model, @RequestParam("p_num") int p_num, RedirectAttributes rttr) {
		log.info("modify ... ");
		service.Modify(vo);
		model.addAttribute("vo", service.get(p_num));
		rttr.addAttribute("p_num", vo.getP_num());
		return "redirect:/jiyoung/Living_info";
	}
	
	@PostMapping("/Etc_info_update")
	public String etcmodify(RentVO vo, Model model, @RequestParam("p_num") int p_num, RedirectAttributes rttr) {
		log.info("modify ... ");
		service.Modify(vo);
		model.addAttribute("vo", service.get(p_num));
		rttr.addAttribute("p_num", vo.getP_num());
		return "redirect:/jiyoung/Etc_info";
	}
	
	// 삭제
	@PostMapping("/remove")
	public String remove(@RequestParam("p_num") int p_num, RedirectAttributes rttr) {
		log.info("remove ... " + p_num);
		
		service.delete(p_num);
		return "redirect:/jiyoung/rent_main";
	}

	@GetMapping(value="/getAttachList",
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<RentAttachVO>> getAttachList(int p_num) {
		log.info("getAttachList......" + p_num);
		return new ResponseEntity<List<RentAttachVO>>
		         (service.getAttachList(p_num),HttpStatus.OK);
	}
	
	
}

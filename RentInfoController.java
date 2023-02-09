package com.jachmi.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
public class RentInfoController {
		
	private RentInfoService service;
	private RentService service2;
	
//	@GetMapping("/order")
//	public String goOrder() {
//		log.info("order....");
//		return "jiyoung/order";
//	}
	/*
	@GetMapping("/pay")
	public String iamPay(@RequestParam("payment")String payment,HttpSession session) {
		log.info("payment....");
		
		session.setAttribute("vo2", service.rentInfoYes(payment));
		return "jiyoung/rent_main";
	}
	*/
	
	@PostMapping("/rent_info")
	public String register(RentInfoVO vo, RedirectAttributes rttr) {
		log.info("register ... ");
		service.registerInfo(vo);
		rttr.addAttribute("p_num", vo.getP_num());
		log.info("p_num..." + vo.getP_num());
		return "redirect:/jiyoung/order";
	}
	@GetMapping("/order")
	public String iamOrder(@RequestParam("p_num")int p_num,HttpSession session) {
		log.info("vo....");
		//model.addAttribute("vo", service.rentInfoOne(p_num));
		session.setAttribute("vo", service.rentInfoOne(p_num));
		return "jiyoung/order";
	}
	
	
	@PostMapping("/payUpdate")
	public String pmodify(RentVO vo, Model model, @RequestParam("p_num") int p_num) {
		log.info("modify ... ");
		service.update(p_num);
		model.addAttribute("vo", service.rentInfoOne(p_num));
		return "redirect:/jiyoung/rent_main";
	}
	
}

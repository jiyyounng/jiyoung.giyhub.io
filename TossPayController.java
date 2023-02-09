package com.jachmi.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jachmi.domain.RentInfoVO;
import com.jachmi.domain.TossPayVO;
import com.jachmi.service.RentInfoService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/jiyoung/*")
@AllArgsConstructor
public class TossPayController {
	
	
//	@GetMapping("/tosspay")
//	public String iamToss() {		
//	log.info("tosspay request....");
//		return "jiyoung/tosspay";
//	}
	
	/*
	@PostMapping(value= "/tosspay2", consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public void requestPayments(@RequestBody TossPayVO vo) throws IOException, InterruptedException {
		log.info("tosspay2.....");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
				.header("Authorization", "Basic dGVzdF9za19PRVA1OUx5Ylo4QjRHZUVaQmtiVjZHWW83cFJlOg==")
				.header("Content-Type","application/json")
				.method("POST", HttpRequest.BodyPublishers.ofString("{\"paymentKey\":\"zJ4xY7m0kODnyRpQWGrN69yvNjBO12rKwv1M9ENjbeoPaZdL\",\"amount\":amount,\"orderId\":\"cIeUeVjBgdwROoB4brwwi\"}"))
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
	}
	*/
	
	    private final RestTemplate restTemplate = new RestTemplate();

	    private final ObjectMapper objectMapper = new ObjectMapper();
	    
	    /*
	    @PostConstruct
	    private void init() {
	        restTemplate.setErrorHandler(new ResponseErrorHandler() {
	            @Override
	            public boolean hasError(ClientHttpResponse response) {
	                return false;
	            }

	            @Override
	            public void handleError(ClientHttpResponse response) {
	            }
	        });
	    }
	   */
	    //시크릿키 자기껄로 바꿔주기
	    private final String SECRET_KEY = "test_sk_OEP59LybZ8B4GeEZBkbV6GYo7pRe";

	    @RequestMapping("/success")
	    public String confirmPayment(
	            @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount,
	            Model model) throws Exception {

	        HttpHeaders headers = new HttpHeaders();
	        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
	        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        Map<String, String> payloadMap = new HashMap<>();
	        payloadMap.put("orderId", orderId);
	        payloadMap.put("amount", String.valueOf(amount));

	        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

	        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
	                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	            JsonNode successNode = responseEntity.getBody();
	            model.addAttribute("orderId", successNode.get("orderId").asText());
	            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
	            return "/jiyoung/success";
	        } else {
	            JsonNode failNode = responseEntity.getBody();
	            model.addAttribute("message", failNode.get("message").asText());
	            model.addAttribute("code", failNode.get("code").asText());
	            return "jiyoung/fail";
	        }
	    }

	    @RequestMapping("/fail")
	    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
	        model.addAttribute("message", message);
	        model.addAttribute("code", code);
	        return "jiyoung/fail";
	    }
	    /*
	    @RequestMapping("/virtual-account/callback")
	    @ResponseStatus(HttpStatus.OK)
	    public void handleVirtualAccountCallback(@RequestBody CallbackPayload payload) {
	        if (payload.getStatus().equals("DONE")) {
	            // handle deposit result
	        }
	    }
	    
	   
	    private static class CallbackPayload {
	        public CallbackPayload() {}

	        private String secret;
	        private String status;
	        private String orderId;

	        public String getSecret() {
	            return secret;
	        }

	        public void setSecret(String secret) {
	            this.secret = secret;
	        }

	        public String getStatus() {
	            return status;
	        }

	        public void setStatus(String status) {
	            this.status = status;
	        }

	        public String getOrderId() {
	            return orderId;
	        }

	        public void setOrderId(String orderId) {
	            this.orderId = orderId;
	        }
	    }
	*/
		
}

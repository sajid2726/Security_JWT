package com.jwtauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class RestCont {
	public static final String message = "Final message";
	public static String msg301 = "msg301";
	public static final String msg300 = "a";
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome  to JWT";
	}

}

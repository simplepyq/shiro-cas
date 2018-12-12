package cn.pyq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @desc:
 * @author: pyq
 * @date: 2018-12-09 21:26
 */
@Controller
public class UserController {
	@RequestMapping("demo")
	public String demo() {
		return "redirect:home.jsp";
	}
}


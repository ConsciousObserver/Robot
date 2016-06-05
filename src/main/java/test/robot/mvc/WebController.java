package test.robot.mvc;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class WebController {
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("name", "Terry Mcginnis");
		return "index";
	}
	
	@RequestMapping("/remoteDesktop")
	public String remoteDesktop(Map<String, Object> model) {
		return "remoteDesktop";
	}
	
	@RequestMapping("/touchpad")
	public String touchpad(Map<String, Object> model) {
		return "touchpad";
	}
}

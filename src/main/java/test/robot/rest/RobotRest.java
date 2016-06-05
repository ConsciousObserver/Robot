package test.robot.rest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import test.robot.util.RobotUtil;

@RestController
@RequestMapping("util")
public class RobotRest {
	@RequestMapping(path="capture",method=RequestMethod.GET)
	public @ResponseBody String capture() {
		File imageFile = RobotUtil.captureScreen();
		String image64 = null;
		try {
			image64 = Base64.encodeBase64String(FileUtils.readFileToByteArray(imageFile));
		} catch (IOException e) {
			e.printStackTrace();
			image64 = e.getMessage();
		}
		return image64;
	}
	
	@RequestMapping(path="click",method=RequestMethod.GET)
	public @ResponseBody String click(@RequestParam("x") int x, @RequestParam("y") int y) {
		RobotUtil.click(x, y);
		return "done";
	}
}

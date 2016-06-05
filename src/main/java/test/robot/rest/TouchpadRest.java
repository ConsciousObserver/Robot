package test.robot.rest;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import test.robot.util.RobotUtil;

@RestController
@RequestMapping("touchpad")
public class TouchpadRest {

	@RequestMapping(path = "move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String move(@RequestBody List<DeltaXy> deltaList) {
		System.out.println("moved");
		RobotUtil.moveDelta(deltaList, true);
		return "done";
	}

	@RequestMapping(path = "click", method = RequestMethod.GET)
	public @ResponseBody String click() {
		RobotUtil.click();
		return "done";
	}
	
	public static class DeltaMoveRequest {
		private List<DeltaXy> list;

		public List<DeltaXy> getList() {
			return list;
		}
		public void setList(List<DeltaXy> list) {
			this.list = list;
		}
	}
	public static class DeltaXy {
		int deltaX;
		int deltaY;
		long timestamp;
		
		public int getDeltaX() {
			return deltaX;
		}
		public void setDeltaX(int deltaX) {
			this.deltaX = deltaX;
		}
		public int getDeltaY() {
			return deltaY;
		}
		public void setDeltaY(int deltaY) {
			this.deltaY = deltaY;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}
}

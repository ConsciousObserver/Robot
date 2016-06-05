$(function() {
	var touchpad = $("#touchpad");
	var responseArrived = true;

	function move(xyArr) {
		$.ajax({
			url : "/touchpad/move",
			method : "post",
			data : JSON.stringify(xyArr),
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(data) {
				log(data);
				responseArrived = true;
			}
		}).error(function(a, b, err) {
			log("move error : " + JSON.stringify(err));
		});
	}

	function click(x, y) {
		$.get("/touchpad/click", {
			x : x,
			y : y
		}, function(data) {
			console.log(data.length);
		});
	}

	var touchpadOffset = touchpad.offset() || {
		left : 0,
		top : 0
	};

	function getXy(event) {
		var pageX, pageY;
		if (event.type === "touchmove") {
			var touch = event.touches;
			pageX = touch[0].pageX;
			pageY = touch[0].pageY;
		} else {
			pageX = event.pageX;
			pageY = event.pageY;
		}

		var xy = {
			x : pageX - touchpadOffset.left,
			y : pageY - touchpadOffset.top
		}
		// log(event.type + " it's me: " + json(xy));

		return xy;
	}

	var lastXy = {};
	var currentXy = {};

	var xyArr = [];
	var networkCallDelay = 150;// milliseconds

	touchpad.bind(
			"mousemove touchmove",
			function(e) {
				try {
					e.preventDefault();
					var xy = getXy(event);

					if (!lastXy.x && !lastXy.y) {
						lastXy = xy;
					} else if (isDiff(lastXy, xy)) {
						// log(event.type + " : " + "xy : " + json(xy));
						var deltaX = xy.x - lastXy.x;
						var deltaY = xy.y - lastXy.y;

						var lastElement = xyArr[xyArr.length - 1];
						var currentTimestamp = new Date().getTime();
						xyArr.push({
							deltaX : deltaX,
							deltaY : deltaY,
							timestamp : new Date().getTime()
						});

						var timeDiff = xyArr.length <= 1 ? 0
								: lastElement.timestamp - xyArr[0].timestamp;
						if (responseArrived && timeDiff > networkCallDelay) {
							move(xyArr);
							xyArr = [];
							responseArrived = false;
						}

						lastXy = xy;
					}
				} catch (err) {
					console.log("error move : " + err);
					log("error move : " + err);
				}

			}).bind("mouseup touchend", function() {
		lastXy = {};
		xyArr = [];
	});

	touchpad.click(function(event) {
		var xy = getXy(event);
		click(xy.x, xy.y);
	});

	var logBox = touchpad;
	var lastLog = null;
	var duplicateLogCount = 1;
	function log(str) {
		if (lastLog === str) {
			duplicateLogCount++;
			logBox.find("div:last").text(duplicateLogCount + " : " + str);
		} else {
			lastLog = str;
			duplicateLogCount = 0;
			logBox.append("<div>" + str + "</div>");
		}
		console.log(str);
	}

	function json(obj) {
		var str = (obj instanceof String) ? obj : JSON.stringify(obj);
		return str;
	}
	function isDiff(lastXy, currentXy) {
		return !((lastXy.x === currentXy.x) && (lastXy.y === currentXy.y));
	}
});
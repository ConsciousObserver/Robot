$(function () {
	var can = $("#screen");
	var ctx = can.get(0).getContext("2d");
	var image = $("#screenImg");
	
	function capture() {
		$.get("/util/capture", function (data) {
			console.log(data.length);
			console.log(data.substring(0, 100));
			
			drawImage(data);
		});
	}
	
	function click(x, y) {
		$.get("/util/click", {x:x, y:y}, function (data) {
			console.log(data.length);
		});
	}
	
	function drawImage(data) {
		image.attr("src", "data:image/  jpg;base64," + data);
	}
	function getXy(event) {
		var offset = can.offset();
		return {
			x: event.pageX - offset.left,
			y: event.pageY - offset.top
		}
	}
	
	image.load(function () {
		can.attr("width", image.width()).attr("height", image.height());
		ctx.drawImage(this, 0, 0);
	});
	
	can.click(function (e) {
		e.preventDefault();
		var xy = getXy(e);
		console.log(JSON.stringify(xy));
		click(xy.x, xy.y);
	});
	
	can.doubletap(function (e) {
		e.preventDefault();
		var xy = getXy(e);
		console.log(JSON.stringify(xy));
		click(xy.x, xy.y);
		setTimeout(function () {
			click(xy.x, xy.y);
		}, 200);
	});
	
	$("#realodBtn").click(function() {
		capture();
	});
	
	
	capture();
	
	setInterval(function () {
		capture();
	}, 200);
	
});
package com.ford.starstatus.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("starRouteUS")
public class StarRouteUS extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("ftp:{{ftp.us.server}}:{{ftp.us.port}}{{ftp.us.drop.location}}?include=us70.*.csv&doneFileName=${file:name.noext}.go&move=BackUp&password={{ftp.us.password}}&username={{ftp.us.username}}&passiveMode=true&disconnect=true")
			.routeId("starRouteUS")
			.convertBodyTo(String.class)
			.setHeader("RequestDate", simple("${date:now:ddMMyyyy}"))
			.setHeader("RequestTime", simple("${date:now:HHmmss}"))
			.setHeader("RequestReceivedTime", simple("${date:now:HHmmssSSS}"))
			.setHeader("InterfaceName", simple("us70"))
			.setHeader("Region", simple("US"))
			.setHeader("InterfaceFullName", simple("ACAD US70 Star Status Data"))
			.wireTap("direct:ReceivedStarMessageUS");
		
	}

}

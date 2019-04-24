package com.ford.starstatus.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class StarFileProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String inputBody=exchange.getIn().getBody(String.class).trim();
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());		
		exchange.getOut().setBody(inputBody.substring(inputBody.indexOf("\n")+1, inputBody.lastIndexOf("\n")));
		
	}

}

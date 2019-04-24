package com.ford.starstatus.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class JsonArrayProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Object> listObject = new ArrayList<>(1);
		Object obj = exchange.getIn().getBody();
		listObject.add(obj);
		exchange.getIn().setBody(listObject);
		
	}

}

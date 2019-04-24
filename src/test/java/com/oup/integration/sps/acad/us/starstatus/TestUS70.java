package com.oup.integration.sps.acad.us.starstatus;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.TypeConverter;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) //ensure that the CamelContext, routes, and mock endpoints are reinitialized between test methods.
@MockEndpointsAndSkip("ftp:.*|file:.*|activemq:.*") //All endpoints are sniffed and recorded in a mock endpoint. The original endpoint is not invoked.
@MockEndpoints // All endpoints are sniffed and recorded in a mock endpoint.
@UseAdviceWith

public class TestUS70 {
	
	@Autowired
	private CamelContext camelContext;
	
	@EndpointInject(uri = "direct:input")
    protected ProducerTemplate ftpserver;
	
	@EndpointInject(uri="mock:activemq:{{activemq.us.queuename}}")
	private MockEndpoint dropLocationActiveMQ;
	
	@EndpointInject(uri="mock:file:{{file.backup.location}}/Error")
	private MockEndpoint backupErrorLocation;
	
	@EndpointInject(uri="mock:file:{{file.backup.location}}/1.0 ReceivedFromStar")
	private MockEndpoint backupStarLocation;
	
	@EndpointInject(uri="mock:file:{{file.backup.location}}/2.0 SentToBiblio")
	private MockEndpoint backupBiblioLocation;
	
	@Before
	public void setUp() throws Exception {
		RouteDefinition rf =  camelContext.getRouteDefinition("starRouteUS");
		rf.adviceWith(camelContext, new AdviceWithRouteBuilder() {

			@Override
			public void configure() throws Exception {
				replaceFromWith("direct:input");
			}
			
		});
		camelContext.start();
	}

	@Test
	public void validStarus70() throws InterruptedException {
		TypeConverter typeConverter = camelContext.getTypeConverter();
		
		String body = typeConverter.convertTo(String.class, new File("src/test/resources/us700001.csv"));
		backupStarLocation.expectedBodiesReceived(body);
		
		List<String> expectedBodies = new ArrayList<>();
		expectedBodies.add(typeConverter.convertTo(String.class, new File("src/test/resources/us700001.csv_9780191798009.json")));
		
		
		backupBiblioLocation.expectedBodiesReceivedInAnyOrder(expectedBodies);
		
		System.out.println("dropLocationActiveMQ " + dropLocationActiveMQ);
		
		dropLocationActiveMQ.expectedBodiesReceivedInAnyOrder(expectedBodies);
		
		
		
		
		ftpserver.sendBodyAndHeader(body, "CamelFileName", "us700001.csv");
		
		dropLocationActiveMQ.assertIsSatisfied();
		backupStarLocation.assertIsSatisfied();
		backupBiblioLocation.assertIsSatisfied();
		
	}
	

}

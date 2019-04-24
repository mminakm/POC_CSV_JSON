package com.ford.starstatus.routes;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.ford.starstatus.processor.JsonArrayProcessor;
import com.ford.starstatus.processor.StarFileProcessor;
import com.ford.starstatus.star.pojo.StarStatusStarUS;
import com.google.gson.JsonSyntaxException;

@Component
public class MainRouteUS extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		onException(JsonSyntaxException.class, InvalidFormatException.class, IllegalStateException.class, NullPointerException.class, IllegalArgumentException.class, NumberFormatException.class, StringIndexOutOfBoundsException.class)
			.maximumRedeliveries(0)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exceptions due to data issues in the ${header.InterfaceFullName} message processing in Route ${routeId} .\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exceptions due to data issues in the ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{property.ReceivedFileName}_$simple{header.RequestReceivedTime}.txt")
			.handled(true);
			
		onException(IOException.class, GenericFileOperationFailedException.class, FileSystemException.class, FileAlreadyExistsException.class)
			.maximumRedeliveries(3)
			.maximumRedeliveryDelay(300000)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exception due to IO operations in ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exception due to IO operations in ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{property.ReceivedFileName}_$simple{header.RequestReceivedTime}.txt")
			.handled(true);
		
		onException(Exception.class)
			.maximumRedeliveries(0)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exception occurred in ${header.InterfaceFullName} message processing in Route ${routeId} .\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exception occurred in ${header.InterfaceFullName} message processing in Route ${routeId}.\\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{property.ReceivedFileName}_$simple{header.RequestReceivedTime}.txt")
			.handled(true);
		
		from("direct:ReceivedStarMessageUS")
			.routeId("mainRouteUS")
			.convertBodyTo(String.class)
			.wireTap("file:{{file.backup.location}}/1.0 ReceivedFromStar?fileName=${date:now:yyyy/MM/dd/}$simple{header.CamelFileName}_$simple{header.RequestReceivedTime}.csv")
			.log(LoggingLevel.INFO, "com.oup.sps", "Received Star MM file ${header.CamelFileName} Message ${body}")
			.process(new StarFileProcessor())
			.unmarshal(new BindyCsvDataFormat(StarStatusStarUS.class))
			.log(LoggingLevel.INFO, "com.oup.sps", "Split Started for MM file ${header.CamelFileName}")
			.split(body())
				.streaming()
				.parallelProcessing()
				.to("direct:transform_us_star_to_biblio")
				.setHeader("productISBN", jsonpath("$.productISBN").regexReplaceAll(":", "-"))
				/* .process(new JsonArrayProcessor()) */
				.marshal().json(JsonLibrary.Jackson, true)
				.log(LoggingLevel.INFO, "com.oup.sps", "Constructed Biblio message for ISBN ${header.productISBN} : ${body}")
				.wireTap("file:{{file.backup.location}}/2.0 SentToBiblio?fileName=${date:now:yyyy/MM/dd/}$simple{header.CamelFileName}_$simple{header.productISBN}.json")
				.to("activemq:{{activemq.us.queuename}}")
				.log(LoggingLevel.INFO, "com.oup.sps", "${header.InterfaceFullName} file ${header.productISBN} generated");
			
	}

}

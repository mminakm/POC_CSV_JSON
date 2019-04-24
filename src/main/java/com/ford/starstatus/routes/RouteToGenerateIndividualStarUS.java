package com.ford.starstatus.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.dozer.MappingException;
import org.springframework.stereotype.Component;

import com.ford.starstatus.star.pojo.StarStatusStarUS;

@Component("routeToGenerateIndividualStarUS")
public class RouteToGenerateIndividualStarUS extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		onException(MappingException.class, IllegalStateException.class, NullPointerException.class, NumberFormatException.class)
			.maximumRedeliveries(0)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exceptions due to data issues in the file processing in ${routeId} .\n Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exceptions due to data issues in the file processing in ${routeId} .\n Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.DestinationFileName}_$simple{header.RequestReceivedTime}.json")
			.handled(true);

		onException(Exception.class)
			.maximumRedeliveries(0)
			.log(LoggingLevel.ERROR, "com.oup.sps", "Exception occurred while processing messages in ${routeId} Exception Message: ${exchangeProperty.CamelExceptionCaught}")
			.setBody(simple("Exception occurred while processing messages in ${routeId} Exception Message: ${exchangeProperty.CamelExceptionCaught}"))
			.to("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.DestinationFileName}_$simple{header.RequestReceivedTime}.json")
			.handled(true);

		from("direct:transform_us_star_to_biblio")
			.routeId("routeToGenerateIndividualStarUS")
			.to("dozer:Star_To_Biblio_TransformationUS?sourceModel=com.oup.integration.sps.acad.us.starstatus.star.pojo.StarStatusStarUS&targetModel=com.oup.integration.sps.acad.us.starstatus.biblio.pojo.StarStatusBiblioUS&mappingFile=Transformations/Star_To_Biblio_TransformationUS.xml");
		
	}

}

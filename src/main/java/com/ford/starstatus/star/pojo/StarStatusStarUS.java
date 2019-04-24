
package com.ford.starstatus.star.pojo;

import java.text.Format.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
* Star Status US70 STAR
* <p>
* STAR for Star Status US70
* 
*/
@CsvRecord(separator=",")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"productISBN",
"hardCodedValue",
"PDMMainDate"
})
public class StarStatusStarUS {

/**
* STAR Field-->MATNR
* 
*/
@DataField(pos=1, trim=true)
@JsonProperty("productISBN")
@JsonPropertyDescription("STAR Field-->EAN")
private String productISBN = "";
/**
* STAR Field-->MAKTX
* 
*/
@DataField(pos=2, trim=true)
@JsonProperty("hardCodedValue")
@JsonPropertyDescription("STAR Field-->Hard Coded Value")
private String hardCodedValue = "";
/**
* STAR Field-->CSTMP subfield D
* 
*/
@DataField(pos=3, trim=true)
@JsonProperty("PDMMainDate")
@JsonPropertyDescription("STAR Field-->CSTMP subfield D")
private String PDMMainDate = "";

public String getProductISBN() {
	return productISBN;
}
public void setProductISBN(String productISBN) {
	this.productISBN = productISBN;
}
public String getHardCodedValue() {
	return hardCodedValue;
}
public void setHardCodedValue(String hardCodedValue) {
	this.hardCodedValue = hardCodedValue;
}
public String getPDMMainDate() {
	return PDMMainDate;
}
public void setPDMMainDate(String pDMMainDate) {
	PDMMainDate = pDMMainDate;
}

}
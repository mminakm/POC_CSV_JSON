
package com.ford.starstatus.biblio.pojo;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
* Star Status US70 Biblio
* <p>
* Biblio for Star Status US70
* 
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"productISBN",
"PDMMainDate"
})
public class StarStatusBiblioUS {

/**
* STAR Field-->EAN
* 
*/
@JsonProperty("productISBN")
@JsonPropertyDescription("STAR Field-->EAN")
private String productISBN = "";

/**
* STAR Field-->CSTMP subfield D
* 
*/
@JsonProperty("PDMMainDate")
@JsonPropertyDescription("STAR Field-->CSTMP subfield D")
private String PDMMainDate = "";

/**
* STAR Field-->EAN
* 
*/
@JsonProperty("productISBN")
public String getProductISBN() {
	return productISBN;
}

/**
* STAR Field-->EAN
* 
*/
@JsonProperty("productISBN")
public void setProductISBN(String productISBN) {
	this.productISBN = productISBN;
}

/**
* STAR Field-->CSTMP subfield D
* 
*/
@JsonProperty("PDMMainDate")
public String getPDMMainDate() {
	return PDMMainDate;
}

/**
* STAR Field-->CSTMP subfield D
* 
*/
@JsonProperty("PDMMainDate")
public void setPDMMainDate(String pDMMainDate) {
	PDMMainDate = pDMMainDate;
}



}
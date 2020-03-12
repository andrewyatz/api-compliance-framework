package org.ga4gh.ComplianceFramework;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Class containing basic methods used by most of the test cases while checking for API compliance.
 * These methods would improve re-usability of code and reduce overall test development costs.
 */
public class TestingFramework {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(TestingFramework.class);

    /**
     * Method used to extract the body of the response sent by the server, as plain text (using String object).
     * @param response The response object received from the server, in response to a web request.
     * @return The response body as a String object.
     */
    public static String getBodyString(Response response){
        log.info("Extracting response body as String");
        String responseString =  response.getBody().asString();
        log.debug("Extracted body: " + responseString);
        return responseString;
    }

    /**
     * Method used to extract the body of the response sent by the server, as a JSON (using JsonPath object).
     * @param response The response object received from the server, in response to a web request.
     * @return The response body as a JsonPath object.
     */
    public static JsonPath getBodyJSON(Response response){
        log.info("Extracting response body as JSON");
        JsonPath responseJSON = response.jsonPath();
        log.debug("Extracted body: " + responseJSON);
        return responseJSON;
    }

    /**
     * Method used to extract the body of the response sent by the server, as XML (using XmlPath object).
     * @param response The response sent by the server.
     * @return The response body as a XmlPath object.
     */
    public static XmlPath getBodyXML(Response response){
        log.info("Extracting response body as XML");
        XmlPath responseXML = response.xmlPath();
        log.debug("Extracted body: " + responseXML);
        return responseXML;
    }

    /**
     * Method used to extract the response/status code of the response sent by the server.
     * @param response The response sent by the server.
     * @return The response/status code of the response.
     */
    public static int getStatusCode(Response response){
        log.info("Extracting response status code");
        int statusCode = response.getStatusCode();
        log.debug("Extracted status code: " + statusCode);
        return statusCode;
    }

    /**
     * Method used to extract the response/status line/message of the response sent by the server.
     * @param response The response sent by the server.
     * @return The response/status line/message of the response.
     */
    public static String getStatusMessage(Response response){
        log.info("Extracting response status message");
        String statusMessage = response.getStatusLine();
        log.debug("Extracted status code: " + statusMessage);
        return statusMessage;
    }

    /**
     * Method used to check if the response is SUCCESS, i.e., the response/status code is 200.
     * @param response The response sent by the server.
     * @return true or false depending on whether the response is SUCCESS or not.
     */
    public static boolean checkSuccess(Response response){
        log.info("Checking if response was SUCCESS");
        boolean flag = (response.statusCode() == 200);
        log.debug("Response success flag: " + flag);
        log.debug("Response status code: " + String.valueOf(response.statusCode())+" | reason: "+response.statusLine());
        return flag;
    }

    /**
     * Method to compare a response body to a local file and validate if both are same.
     * @param response The response sent by the server. The body of this response needs to be validated against a local file.
     * @param filename The name of the file which contains the expected response body for validation, file should be in resources dir.
     * @return true or false depending on whether the response body is same as contents of the file or not.
     */
    public static boolean validateResponseBodyWithLocalFile(Response response, String filename) throws IOException {
        log.info("Extracting the file contents to a String");
        String fileString = Utilities.readFileToString(filename);
        log.debug("File data: " + fileString);
        String responseString = getBodyString(response);
        log.debug("Extracted response body: " + responseString);
        boolean flag = responseString.equals(fileString);
        log.debug("Response compare flag: " + flag);
        return flag;
    }

    /**
     * Method to check if a particular header is present in a response sent by the server.
     * @param response The response sent by the server.
     * @param headerName The name of the particular header that needs to be searched in the response.
     * @return true or false depending on whether the response contains the header or not.
     */
    public static boolean checkHeaderPresent(Response response, String headerName) {
        boolean flag = false;
        log.info("Extracting all headers from response");
        Headers headerList = response.headers();
        log.debug("Extracted headers: " + headerList);
        for(Header header : headerList){
            if(header.getName().equals(headerName)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Method to validate the value of a header in a response to an expected value.
     * @param response The response sent by the server.
     * @param headerName The name of the particular header, the value of which needs to be validated.
     * @param headerValue The expected value of the header.
     * @return true or false depending on whether the header value is the same as expected value or not.
     */
    public static boolean validateResponseHeader(Response response, String headerName, String headerValue) {
        boolean flag = false;
        log.info("Checking if required header is present in the response");
        if(checkHeaderPresent(response, headerName)){
            log.debug("Header: " + headerName + " present. Comparing values");
            String responseValue = response.header(headerName);
            log.debug("Header: " + headerName + " Response value: " + responseValue);
            if(responseValue.equals(headerValue)){
                flag = true;
            }
        }
        log.debug("Header validation flag: " + flag);
        return flag;
    }
}

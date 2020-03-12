package org.ga4gh.RefgetUtilities;

import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.ComplianceFramework.RequestsRestAssured;
import org.ga4gh.ComplianceFramework.Server;
import org.ga4gh.ComplianceFramework.Utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class containing some utility functions used for testing refget API servers.
 */
public class RefgetUtilities {

    /**
     * The logger object used by log4j to record application logs.
     */
    private static Logger log = LogManager.getLogger(RefgetUtilities.class);

    /**
     * Request object to be used to fire API requests.
     */
    private static RequestsRestAssured request = new RequestsRestAssured();

    /**
     * Method to read the Checksum JSON file and return object of a particular sequence as a JSONObject. The object contains some metadata associated with the sequence.
     * @param ch The name of the sequence whose object is returned.
     * @return The JSONObject of the particular sequence. The object contains fields "md5", "sha512", "is_circular" and "size".
     * @throws IOException if there are errors while reading the file.
     * @throws ParseException if there are errors in converting the file to a JSONObject. Can occur if file does not follow proper JSON syntax.
     */
    public static JSONObject readChecksumsJSON(final String ch) throws IOException, ParseException {
        final Object obj = new JSONParser().parse(new FileReader(Constants.RESOURCE_DIR + "checksums.json"));
        final JSONObject fileObj = (JSONObject) obj;
        return (JSONObject) fileObj.get(ch);
    }

    /**
     * Method to read the full sequence from a file and return as a String.
     * @param filename The name of the file to be read. The file must be present in the 'resources' directory.
     * @return The full sequence as a String.
     */
    public static String readSequenceFromFastaFile(final String filename) throws IOException {
        String sequence = Utilities.readFileToString(filename);
        sequence = sequence.replaceAll(">.*\\r?\\n", "");
        sequence = sequence.replaceAll("\\r?\\n", "");
        return sequence;
    }

    /**
     * Method to return a Sequence object of a valid sequence.
     * @return Sequence object of a valid sequence.
     * @throws IOException if there are errors while reading the file.
     * @throws ParseException if there are errors in converting the file to a JSONObject. Can occur if file does not follow proper JSON syntax.
     */
    public static Sequence getValidSequenceObject(final String seq) throws IOException, ParseException {
        final JSONObject seqChecksumObj = readChecksumsJSON(seq);
        log.debug("Extracted JSONObject: " + seqChecksumObj);
        return new Sequence(seqChecksumObj);
    }

    /**
     * Method to return a Sequence object of a valid circular sequence.
     * @return Sequence object of a valid circular sequence.
     * @throws IOException if there are errors while reading the file.
     * @throws ParseException if there are errors in converting the file to a JSONObject. Can occur if file does not follow proper JSON syntax.
     */
    public static Sequence getValidCircularSequenceObject() throws IOException, ParseException {
        final JSONObject seqChecksumObj = readChecksumsJSON("NC");
        log.debug("Extracted JSONObject: " + seqChecksumObj);
        return new Sequence(seqChecksumObj);
    }

    /**
     * Method to fire a GET request to a refget server and return the sequence.
     * @param refgetServer The server object of the server that will receive the request.
     * @param id The id/hash of the sequence to be retrieved.
     * @return The response sent by the server.
     */
    public static Response getSequenceResponse(final Server refgetServer, final String id){
        final String finalEndpoint = refgetServer.getEndpoint(Constants.SEQUENCE_ENDPOINT + id);
        return request.GET(finalEndpoint);
    }

    /**
     * Method to fire a GET request to a refget server and return the sequence (sub-sequence also supported).
     * @param refgetServer The server object of the server that will receive the request.
     * @param id The id/hash of the sequence to be retrieved.
     * @param start The value of the 'start' parameter of sub-sequence.
     * @param end The value of the 'end' query parameter of sub-sequence.
     * @return The response sent by the server.
     */
    public static Response getSequenceResponse(final Server refgetServer, final String id, final Integer start, final Integer end){
        final String finalEndpoint = refgetServer.getEndpoint(Constants.SEQUENCE_ENDPOINT + id);
        final Map<String, String> parameterMap = new HashMap<>();
        if(start != null) {
            parameterMap.put("start", start.toString());
        }
        if(end != null) {
            parameterMap.put("end", end.toString());
        }
        return request.GETWithQueryParams(finalEndpoint, parameterMap);
    }

    /**
     * Method to fire a GET request with headers to a refget server and return the sequence.
     * @param refgetServer The server object of the server that will receive the request.
     * @param id The id/hash of the sequence to be retrieved.
     * @param headerMap The headers to be sent with the request.
     * @return The response sent by the server.
     */
    public static Response getSequenceResponse(final Server refgetServer, final String id, final Map<String, String> headerMap){
        final String finalEndpoint = refgetServer.getEndpoint(Constants.SEQUENCE_ENDPOINT + id);
        return request.GETWithHeaders(finalEndpoint, headerMap);
    }

    /**
     * Method to fire a GET request to a refget server and return the service info json.
     * @param refgetServer The server object of the server that will receive the request.
     * @return The response sent by the server.
     */
    public static Response getServiceInfoResponse(final Server refgetServer){
        final String finalEndpoint = refgetServer.getEndpoint(Constants.INFO_ENDPOINT);
        return request.GET(finalEndpoint);
    }

    /**
     * Method to fire a GET request with headers to a refget server and return the service info json.
     * @param refgetServer The server object of the server that will receive the request.
     * @param headerMap The headers to be sent with the request.
     * @return The response sent by the server.
     */
    public static Response getServiceInfoResponse(final Server refgetServer, final Map<String, String> headerMap){
        final String finalEndpoint = refgetServer.getEndpoint(Constants.INFO_ENDPOINT);
        return request.GETWithHeaders(finalEndpoint, headerMap);
    }

    /**
     * Method to fire a GET request to a refget server and return the metadata of sequence.
     * @param refgetServer The server object of the server that will receive the request.
     * @param id The id/hash of the sequence whose metadata is to be retrieved.
     * @return The response sent by the server.
     */
    public static Response getMetadataResponse(final Server refgetServer, final String id){
        final String finalEndpoint = refgetServer.getEndpoint(Constants.SEQUENCE_ENDPOINT + id + Constants.METADATA_ENDPOINT);
        return request.GET(finalEndpoint);
    }

    /**
     * Method to fire a GET request with headers to a refget server and return the metadata of sequence.
     * @param refgetServer The server object of the server that will receive the request.
     * @param id The id/hash of the sequence whose metadata is to be retrieved.
     * @param headerMap The headers to be sent with the request.
     * @return The response sent by the server.
     */
    public static Response getMetadataResponse(final Server refgetServer, final String id, final Map<String, String> headerMap){
        final String finalEndpoint = refgetServer.getEndpoint(Constants.SEQUENCE_ENDPOINT + id + Constants.METADATA_ENDPOINT);
        return request.GETWithHeaders(finalEndpoint, headerMap);
    }

    public static String generateTestDescription(final String testName) {
        return "Description";
    }

    public static String generateResultText(final String testName, final int result) {
        return "Test Result";
    }

    public static void removeIfResultPresent(final JSONObject obj, final JSONArray arr) {
        boolean present = false;
        int i;
        for(i = 0 ; i < arr.size() ; i++) {
            if (((JSONObject) arr.get(i)).get("base_url").equals(obj.get("base_url"))){
                present = true;
                break;
            }
        }
        if(present) {
            final JSONArray newArray = new JSONArray();
            for (int index = 0; index < arr.size(); index++) {
                if (index == i) {
                    continue;
                }
                newArray.add(arr.get(index));
            }
            RefgetSession.resultsArray = newArray;
        }
    }
}

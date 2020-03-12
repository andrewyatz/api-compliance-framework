package org.ga4gh.RefgetUtilities;

import io.restassured.path.json.JsonPath;
import static org.ga4gh.ComplianceFramework.Constants.*;
import org.ga4gh.ComplianceFramework.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RefgetSession {

    private static Server refgetServer;

    public static JSONObject testObject = new JSONObject();

    public static JSONArray resultsArray = new JSONArray();

    public static void setupEnvironment(String server){
        refgetServer = new Server(server);
        String serverName = System.getProperty("name");
        if(serverName == null) {
            serverName = "Refget Server";
        }
        refgetServer.setServerName(serverName);

        JsonPath response = RefgetUtilities.getServiceInfoResponse(refgetServer).jsonPath();

        Map<String, Object> props = new HashMap<>();
        props.put(REFGET_PROPERTY_CIRCULAR_SUPPORTED, response.get(REFGET_PROPERTY_CIRCULAR_SUPPORTED));
        props.put(REFGET_PROPERTY_ALGORITHMS, response.get(REFGET_PROPERTY_ALGORITHMS));
        props.put(REFGET_PROPERTY_SUBSEQUENCE_LIMIT, response.get(REFGET_PROPERTY_SUBSEQUENCE_LIMIT));
        props.put(REFGET_PROPERTY_VERSION, response.get(REFGET_PROPERTY_VERSION));
        refgetServer.setServerProperties(props);

        testObject.put("server_name", refgetServer.getServerName());
        testObject.put("base_url", refgetServer.getBaseUrl());
        testObject.put("date_time", java.time.LocalDateTime.now().toString());
        testObject.put("test_results", new JSONObject());
        testObject.put("summary", new JSONObject());
    }

    public static Server getRefgetServer() {
        return refgetServer;
    }
}

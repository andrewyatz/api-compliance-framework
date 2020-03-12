package org.ga4gh.RefgetTestSuite;

import io.restassured.response.Response;
import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.ComplianceFramework.Server;
import org.ga4gh.ComplianceFramework.TestingFramework;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class InfoEndpointTest {

    /**
     * The server instance.
     */
    private Server refgetServer;
    private JSONArray classArray = new JSONArray();
    private JSONObject tempTestObject = new JSONObject();
    private JSONArray tempQueryArray = new JSONArray();


    @BeforeClass
    public void setRefgetServer(ITestContext context){
        refgetServer = RefgetSession.getRefgetServer();
        context.setAttribute("info_result", 1);
    }

    @AfterClass
    public void setOverview(ITestContext context){
        JSONObject summaryObject = new JSONObject();
        summaryObject.put("name", "ServiceInfo");
        summaryObject.put("result", context.getAttribute("info_result"));

        ((JSONObject) RefgetSession.testObject.get("summary")).put("service_info", summaryObject);
        ((JSONObject) RefgetSession.testObject.get("test_results")).put("service_info", classArray);
    }

    @BeforeMethod
    public void clear(){
        tempTestObject = new JSONObject();
        tempQueryArray = new JSONArray();
    }

    @AfterMethod
    public void setResults(ITestResult result, ITestContext context){
        tempTestObject.put("test_name", result.getName());
        tempTestObject.put("parent", this.getClass().getSimpleName());
        tempTestObject.put("api_query_info", tempQueryArray);
        tempTestObject.put("test_description", RefgetUtilities.generateTestDescription(result.getTestName()));
        if(result.getStatus() == ITestResult.SUCCESS) {
            tempTestObject.put("result", 1);
            tempTestObject.put("result_text", RefgetUtilities.generateResultText(result.getTestName(), 1));
        }
        else if(result.getStatus() == ITestResult.SKIP) {
            tempTestObject.put("result", 0);
            tempTestObject.put("result_text", RefgetUtilities.generateResultText(result.getTestName(), 0));
        }
        else if(result.getStatus() == ITestResult.FAILURE) {
            tempTestObject.put("result", -1);
            tempTestObject.put("result_text", RefgetUtilities.generateResultText(result.getTestName(), -1));
            context.setAttribute("info_result", 0);
        }
        classArray.add(tempTestObject);
    }

    @Test
    public void getServiceInfoWithoutAcceptHeader(){
        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer);
        tempQueryArray.add("Response: " + response.getBody().asString());

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
    }

    @Test
    public void getServiceInfoCircular(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());
        tempQueryArray.add("Request Headers: " + headerMap.toString());

        HashMap serviceJson = response.jsonPath().get("refget");
        boolean flag = serviceJson.containsKey("circular_supported");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoAlgorithms(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());
        tempQueryArray.add("Request Headers: " + headerMap.toString());

        HashMap serviceJson = response.jsonPath().get("refget");
        boolean flag = serviceJson.containsKey("algorithms");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoSubsequenceLimit(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());
        tempQueryArray.add("Request Headers: " + headerMap.toString());

        HashMap serviceJson = response.jsonPath().get("refget");
        boolean flag = serviceJson.containsKey("subsequence_limit");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoSupportedApi(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", Constants.INFO_ACCEPT_HEADER);

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());
        tempQueryArray.add("Request Headers: " + headerMap.toString());

        HashMap serviceJson = response.jsonPath().get("type");
        boolean flag = serviceJson.containsKey("version");

        //testing
        Assert.assertTrue(TestingFramework.checkSuccess(response));
        Assert.assertTrue(flag);
    }

    @Test
    public void getServiceInfoWithInvalidHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "dummy_header");

        //firing request
        Response response = RefgetUtilities.getServiceInfoResponse(refgetServer, headerMap);
        tempQueryArray.add("Response: " + response.getBody().asString());
        tempQueryArray.add("Request Headers: " + headerMap.toString());

        //testing
        Assert.assertEquals(TestingFramework.getStatusCode(response), 406);
    }
}

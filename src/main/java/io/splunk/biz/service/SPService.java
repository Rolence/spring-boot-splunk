package io.splunk.biz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splunk.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SPService {

    public static String username = "admin";
    public static String password = "changed!";
    public static String host = "localhost";
    public static int port = 8089;
    public static String scheme = "https";
//    public static String username = "admin";
//
//    public static String password = "your password";
//    public static String host = "your splunk host url like - splunk-xx-test.abc.com";
//    public static int port = 8089;
//    public static String scheme = "https";

    public static Service getSplunkService() {

        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);

        Map<String, Object> connectionArgs = new HashMap<>();

        connectionArgs.put("host", host);
        connectionArgs.put("port", port);
        connectionArgs.put("scheme", scheme);
        connectionArgs.put("username", username);
        connectionArgs.put("password", password);
        connectionArgs.put("token","");

        Service splunkService = Service.connect(connectionArgs);
        splunkService.setToken("eyJraWQiOiJzcGx1bmsuc2VjcmV0IiwiYWxnIjoiSFM1MTIiLCJ2ZXIiOiJ2MiIsInR0eXAiOiJzdGF0aWMifQ.eyJpc3MiOiJhZG1pbiBmcm9tIDRjZjVjYjFmZTc0NyIsInN1YiI6ImFkbWluIiwiYXVkIjoiYXV0aGVudGljYXRpb24iLCJpZHAiOiJTcGx1bmsiLCJqdGkiOiJjNGIyYjYxZmIxMzAwNTY3OTI5ZjQ0NzkzNTkyNzVmZjVmYzljMjMzZTNlMDZkMjFhM2FiMDk1Nzk1MjM2ZjE4IiwiaWF0IjoxNjg2MTM3NjgxLCJleHAiOjE3MjkzMzc2ODEsIm5iciI6MTY4NjEzNzY4MX0.17NuS8iu-PFlqRbx96q4f5hiI4lComdSlU3BLgp30LdOrON1nOzA93bwZCx9RvD7GqJvLcXjbDzQDN6W1XvS0w");

        return splunkService;
    }

    /* Take the Splunk query as the argument and return the results as a JSON
    string */
    public static String getQueryResultsIntoJsonString(String query) throws IOException {

        Service splunkService = getSplunkService();

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setContent("testing splunk event");
        requestMessage.setMethod("POST");
        System.out.println("Events types: "+ splunkService.getEventTypes().size());
        splunkService.send("",requestMessage);

        Args queryArgs = new Args();

        //set "from" time of query. 1 = from beginning
        queryArgs.put("earliest_time", "1");

        //set "to" time of query. now = till now
        queryArgs.put("latest_time", "now");

        Job job = splunkService.getJobs().create(query);

        while(!job.isDone()) {
            try {
                Thread.sleep(500);
            } catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        Args outputArgs = new Args();

        //set format of result set as json
        outputArgs.put("output_mode", "json");

        //set offset of result set (how many records to skip from the beginning)
        //Default is 0
        outputArgs.put("offset", 0);

        //set no. of records to get in the result set.
        //Default is 100
        //If you put 0 here then it would be set to "no limit"
        //(i.e. get all records, don't truncate anything in the result set)
        outputArgs.put("count", 0);

        InputStream inputStream = job.getResults(outputArgs);

        //Now read the InputStream of the result set line by line
        //And return the final result into a JSON string
        //I am using Jackson for JSON processing here,
        //which is the default in Spring boot

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String resultString = null;
        String aLine = null;

        while((aLine = in.readLine()) != null) {

            //Convert the line from String to JsonNode
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(aLine);

            //Get the JsonNode with key "results"
            JsonNode resultNode = jsonNode.get("results");

            //Check if the resultNode is array
            if (resultNode.isArray()) {
                resultString = resultNode.toString();
            }
        }

        return resultString;
    }
//    public void splService(){
//        Service service = Service.connect()
//    }


}

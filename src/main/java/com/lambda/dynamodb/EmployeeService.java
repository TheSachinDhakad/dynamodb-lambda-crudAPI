package com.lambda.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import entity.Employee;

import java.util.List;
import java.util.Map;

public class EmployeeService {
    private DynamoDBMapper dynamoDBMapper;
    private  static String jsonBody = null;

    public APIGatewayProxyResponseEvent saveEmployee(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent , Context context){
        intiDynamoDB();
        Employee employee = Utility.convertStringToObj(apiGatewayProxyRequestEvent.getBody() , context);
        dynamoDBMapper.save(employee);
        jsonBody = Utility.convertObjectToString(employee,context);
        context.getLogger().log("data saved successfully to dynamodb:::" + jsonBody);
        return createApiResponse(jsonBody , 201 , Utility.createHeader());



    }
    public APIGatewayProxyResponseEvent getEmployeeById(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent , Context context){
        intiDynamoDB();
        String empId = apiGatewayProxyRequestEvent.getPathParameters().get("empId");
        Employee employee = dynamoDBMapper.load(Employee.class , empId);
        if(employee!=null){
                jsonBody = Utility.convertObjectToString(employee , context);
                context.getLogger().log("fetch employee By ID:::" + jsonBody);
                return createApiResponse(jsonBody , 200 , Utility.createHeader());

        }
        else {
            jsonBody = "Employee Not Found Exception :"+empId;
            return createApiResponse(jsonBody , 400 , Utility.createHeader());
        }

    }
    public  APIGatewayProxyResponseEvent getEmployee(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent , Context context){
    intiDynamoDB();
        List<Employee> employees = dynamoDBMapper.scan(Employee.class , new DynamoDBScanExpression());
        jsonBody = Utility.convertListObjToString(employees , context);
        context.getLogger().log("fetch employee List:::"+jsonBody);
        return createApiResponse(jsonBody,200,Utility.createHeader());
    }
    public APIGatewayProxyResponseEvent deleteEmployeeById(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent , Context context){
        intiDynamoDB();
        String empId = apiGatewayProxyRequestEvent.getPathParameters().get("empId");
        Employee employee = dynamoDBMapper.load(Employee.class , empId);
        if(employee!=null){
            dynamoDBMapper.delete(employee);
            context.getLogger().log("data delete successfully:::" + empId);
            return createApiResponse("data deleted successfully."+empId,200,Utility.createHeader());
        }else {
            jsonBody = "Employee Not Found Exception : " + empId;
            return createApiResponse(jsonBody , 400,Utility.createHeader());
        }
    }

    // db init function
    private void intiDynamoDB() {
        AmazonDynamoDB client = AmazonDynamoDBAsyncClientBuilder.standard()
                .withRegion("ap-south-1")  // ✅ Must match DynamoDB region
                .build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }

// api response function

    private APIGatewayProxyResponseEvent createApiResponse(String body , int statusCode , Map<String , String> header){
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(body);
        responseEvent.setHeaders(header);
        responseEvent.setStatusCode(statusCode);
        return responseEvent;
    }




}

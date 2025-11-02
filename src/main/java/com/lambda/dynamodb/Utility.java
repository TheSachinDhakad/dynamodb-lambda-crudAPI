package com.lambda.dynamodb;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

    public  static Map<String , String> createHeader(){
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type" , "application/json");
        header.put("X-amazon-author" , "sachin");
        header.put("X-amazon-apiVersion" , "v1");
        return header;

    }

    public static String convertObjectToString(Employee employee , Context context){
        String jsonBody = null;
        try{
            jsonBody = new ObjectMapper().writeValueAsString(employee);


        } catch (JsonProcessingException e) {
            context.getLogger().log("Error while converting obj to string:::" + e.getMessage());
        }
        return jsonBody;
    }
    public static Employee convertStringToObj(String jsonBody , Context context){
        Employee employee = null;
        try{
            employee = new ObjectMapper().readValue(jsonBody , Employee.class);

        } catch (JsonProcessingException e) {
            context.getLogger().log("Error while converting obj to string:::" + e.getMessage());
        }
        return employee;
    }

    public  static String convertListObjToString(List<Employee> employees , Context context){
        String jsonBody = null;

        try {
            jsonBody = new  ObjectMapper().writeValueAsString(employees);

        } catch (JsonProcessingException e) {
            context.getLogger().log("Error while converting obj to string:::" + e.getMessage());
        }
        return jsonBody;
    }
}

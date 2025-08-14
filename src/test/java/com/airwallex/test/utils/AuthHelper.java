package com.airwallex.test.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AuthHelper {
    public static String getFreshToken() {
        Response response = RestAssured.given()
                .baseUri("https://api-demo.airwallex.com")// 认证地址
                .basePath("/api/v1/authentication/login")
                .header("Content-Type","application/json")
                .header("x-client-id", "CVwQh88xTUeqYV-zMpLahg")
                .header("x-api-key", "194dcb42f76d6c19ee0bd52812320d3d20eb69288832197802b31e2d03379bfaf044ccf348ddf46a290f87114e0b84aa")
                .post(); //

        response.then().statusCode(201);
        return "Bearer " + response.jsonPath().getString("token"); //
    }
}
package com.airwallex.test.stepdefs;

import java.util.Properties;
import java.io.InputStream;

import com.airwallex.test.utils.AuthHelper;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
//import io.cucumber.java.en.Then;

public class FxRatesStepDefs {
    private Response response;
    private static final String API_TOKEN;
    private String buyCurrency;
    private String sellCurrency;
    private int amount;
    private Integer sellAmount;

    static {
        // 加载配置文件
        Properties prop = new Properties();
        try (InputStream input = FxRatesStepDefs.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            prop.load(input);
            API_TOKEN = AuthHelper.getFreshToken();
        } catch (Exception e) {
            throw new RuntimeException("加载配置文件失败", e);
        }
    }

        @Given("I prepare to convert 100 AUD to USD")
    public void prepare_conversion() {
        // Test context setup if needed
    }
//切换多种currency
    @Given("I prepare to convert {int} {string} to {string}")
//    @Given("I prepare to convert {int} {string} to {string}")

    public void prepare_conversion(int amount, String buyCurrency, String sellCurrency) {
        this.amount = amount;
        this.buyCurrency = buyCurrency;
        this.sellCurrency = sellCurrency;

//        System.out.printf("准备将 %d %s 转换为 %s%n", amount, buyCurrency, sellCurrency);
    }


    @When("I send the exchange rate request")
    public void send_exchange_request() {
        response = RestAssured.given()
                .header("Authorization", API_TOKEN)
                .queryParam("buy_currency", "AUD")
                .queryParam("sell_currency", "USD")
                .queryParam("buy_amount", "100")
//                .queryParam("buy_currency", buyCurrency)
//                .queryParam("sell_currency", sellCurrency)
//                .queryParam("buy_amount", amount)
//

                .get("https://api-demo.airwallex.com/api/v1/fx/rates/current");
    }

    @Then("the response should contain valid AUD and USD rate details")
    public void verify_rate_details() {
        System.out.println(response.prettyPrint());
        response.then()

                .body("buy_currency", equalTo("AUD"))
                .body("sell_currency", equalTo("USD"))
                .body("currency_pair", equalTo("AUDUSD"))
                .body("rate", greaterThan(0.0f))
                .body("rate_details[0].buy_amount", equalTo(100.0f))
                .body("rate_details[0].level", equalTo("CLIENT"))
                .body("rate_details[0].rate", instanceOf(Float.class))
                .body("rate_details[0].sell_amount", isA(Number.class));
    }

    @Then("the response should contain valid {string} and {string} rate details")
    public void verify_currency_pair_details(String buyCurrency, String sellCurrency) {
        response.then()
                .body("buy_currency", equalTo(buyCurrency))
                .body("sell_currency", equalTo(sellCurrency))
                .body("currency_pair", equalTo(buyCurrency + sellCurrency))
                .body("rate", greaterThan(0.0f))
                .body("rate_details[0].buy_amount", equalTo((float) this.amount))
                .body("rate_details[0].level", equalTo("CLIENT"))
                .body("rate_details[0].rate", instanceOf(Float.class))
                .body("rate_details[0].sell_amount", isA(Number.class));
    }

    @When("I request current rate for {string} to {string} with amount {int}")
    public void requestRate(String buyCurrency, String sellCurrency, int amount) {
        response = RestAssured.given()
                .queryParam("buy_currency", buyCurrency)
                .queryParam("sell_currency", sellCurrency)
                .queryParam("buy_amount", amount)
                .header("Authorization", API_TOKEN)
                .get("https://api-demo.airwallex.com/api/v1/fx/rates/current");
    }

    @Then("the response status code should be {int}")
    public void verifyStatus(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Given("I prepare to convert {int} AUD without specifying sell currency")
    public void prepare_conversion_without_sell_currency(int amount) {
        // 记录买入货币和金额，不设置卖出货币（留空或 null）
        this.buyCurrency = "AUD";
        this.amount = amount;
        this.sellCurrency = null; // 不指定卖出货币
    }

    @Given("I prepare to convert {int} AUD without specifying buy currency")
    public void prepare_conversion_without_buy_currency(int amount) {
        // 记录买入货币和金额，不设置卖出货币（留空或 null）
        this.buyCurrency = null;
        this.amount = amount;
        this.sellCurrency = "USD"; //
    }

    @When("I send the exchange rate request without {string}")
    public void send_request_without_currency(String currencyType) {
        if (currencyType.equals("sell_currency")) {
            response = RestAssured.given()
                    .header("Authorization", API_TOKEN)
                    .queryParam("buy_currency", buyCurrency) // 只传买入货币
                    .queryParam("buy_amount", amount)        // 只传金额
                    // 不添加 sell_currency 参数
                    .get("https://api-demo.airwallex.com/api/v1/fx/rates/current");
        } else {
            response = RestAssured.given()
                    .header("Authorization", API_TOKEN)
                    .queryParam("sell_currency", sellCurrency) // 只传买入货币
                    .queryParam("buy_amount", amount)        // 只传金额
                    // 不添加 buy_currency 参数
                    .get("https://api-demo.airwallex.com/api/v1/fx/rates/current");
        }
    }

    @Then("the response should contain error message {string}")
    public void verify_error_message(String expectedMessage) {
//    // 打印错误响应（方便调试）
        System.out.println("=== 错误响应内容 ===");
        System.out.println(response.prettyPrint());

        // 验证响应中包含预期的错误信息
        response.then()
                .body("message", equalTo(expectedMessage)) // 匹配错误信息
                .body("code", equalTo("invalid_argument")); //
    }

}
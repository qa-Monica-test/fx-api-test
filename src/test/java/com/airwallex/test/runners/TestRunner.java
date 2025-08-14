package com.airwallex.test.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.airwallex.test.stepdefs",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class TestRunner {}

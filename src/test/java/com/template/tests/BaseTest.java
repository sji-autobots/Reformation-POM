package com.template.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.template.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void setUpSuite() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("test-output/ExtentReport.html");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setReportName("Test Execution Report");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        logger.info("ExtentReports initialized");
    }

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver();
        logger.info("WebDriver initialized");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
        logger.info("WebDriver quit");
    }

    @AfterSuite
    public void tearDownSuite() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed");
        }
    }
}

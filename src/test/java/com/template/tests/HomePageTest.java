package com.template.tests;

import com.aventstack.extentreports.Status;
import com.template.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {
    @Test
    public void sampleHomePageTest() {
        test = extent.createTest("Sample Home Page Test");
        HomePage homePage = new HomePage(driver);
        test.log(Status.INFO, "Navigated to Home Page");
        // Example assertion (replace with real checks)
        Assert.assertNotNull(homePage);
        test.log(Status.PASS, "Home Page object is not null");
    }
}

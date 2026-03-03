package com.reformation.tests;



public class OpenSiteTest extends BaseTest {

    @org.testng.annotations.Test
    public void testOpenSite() throws InterruptedException {
        // Dev URL with basic auth
        driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/");
        Thread.sleep(5000);
    }
}

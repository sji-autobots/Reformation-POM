package com.reformation.tests;

import com.reformation.base.BaseTest;
import com.reformation.pages.PDPPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CuteTooRecentlyViewedTest extends BaseTest {


    private static final String[] SKUS = {
            "1318768DOB",
            "1319024TOT",
            "1319250BLK",
            "1319524LTU",
            "1319452BLK"
    };

    @Test(description = "Verify 'We’re cute too' section links to recently viewed PDPs")
    public void testCuteTooRecentlyViewed() {
        PDPPage pdpPage = new PDPPage(driver);
        // Visit each PDP, first with auth
        // Use a single window for all navigation. For authentication, set cookies or use a login page if available.
        // If basic auth is required, set an Authorization header via Chrome DevTools Protocol or use a login form if possible.
        // Here, we will just use driver.get for all SKUs, assuming the session is preserved after the first login.
        // If the first page requires credentials, you may need to automate the login form instead of using the URL with credentials.
        for (String sku : SKUS) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.open('https://stage.thereformation.com/" + sku + ".html', '_blank');");
            // Optionally, add waits/asserts for page load if needed
        }
        // On last PDP, scroll to section and verify links

        pdpPage.scrollToCuteTooSection();
        pdpPage.clickRecentlyViewedButton();
        // Only check for previously visited SKUs (excluding the last one)
        String[] visitedSkus = new String[SKUS.length - 1];
        System.arraycopy(SKUS, 0, visitedSkus, 0, SKUS.length - 1);
        java.util.List<String> allHrefs = new java.util.ArrayList<>();
        org.openqa.selenium.WebElement recSection = driver.findElement(org.openqa.selenium.By.xpath("//div[contains(@class,'pdp__recommendations')]"));
        java.util.List<org.openqa.selenium.WebElement> links = recSection.findElements(org.openqa.selenium.By.tagName("a"));
        for (org.openqa.selenium.WebElement link : links) {
            String href = link.getAttribute("href");
            if (href != null) {
                allHrefs.add(href);
            }
        }
        java.util.List<String> missingSkus = new java.util.ArrayList<>();
        for (String sku : visitedSkus) {
            boolean found = false;
            for (String href : allHrefs) {
                if (href.contains(sku)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                missingSkus.add(sku);
            }
        }
        if (!missingSkus.isEmpty()) {
            System.out.println("[DEBUG] Missing SKUs in 'We're cute too' section: " + missingSkus);
            System.out.println("[DEBUG] All hrefs found: " + allHrefs);
        }
        Assert.assertTrue(missingSkus.isEmpty(), "Not all recently viewed PDPs are linked in the 'We’re cute too' section. Missing SKUs: " + missingSkus);
    }
}

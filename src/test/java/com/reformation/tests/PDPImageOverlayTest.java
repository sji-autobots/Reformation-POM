package com.reformation.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.reformation.base.BaseTest;
import com.reformation.pages.PDPPage;

public class PDPImageOverlayTest extends BaseTest {
    @Test
    public void testPDPImageOverlayZoomAndDismiss() {
        // Precondition: User is on a PDP for SKU 1312627BLK00S
        PDPPage pdpPage = new PDPPage(driver);
        pdpPage.loadPDPWithAuth("1312627BLK00S");
        
        // 1) Select any image for a product
        pdpPage.selectMainProductImage();
        
        // 2) Verify that an image overlay is displayed
        Assert.assertTrue(pdpPage.isImageOverlayDisplayed(), "Image overlay should be displayed after clicking product image.");
        
        // 3) Zoom into the image
        pdpPage.clickZoomButton();
        
        // 4) Verify that the image gets zoomed in
        Assert.assertTrue(pdpPage.isImageZoomedIn(), "Image should be zoomed in after clicking zoom button.");
        
        // 5) Click on "X" close icon on the top right corner
        pdpPage.closeImageOverlay();
        
        // 6) Verify that the image overlay is dismissed
        Assert.assertTrue(pdpPage.isImageOverlayDismissed(), "Image overlay should be dismissed after clicking close button.");
    }
}

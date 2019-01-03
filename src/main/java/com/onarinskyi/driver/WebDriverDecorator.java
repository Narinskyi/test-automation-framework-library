package com.onarinskyi.driver;

import com.onarinskyi.gui.Page;
import com.onarinskyi.environment.Timeout;
import com.onarinskyi.environment.UrlResolver;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WebDriverDecorator implements WebDriver {

    private final Logger log = Logger.getLogger(WebDriverDecorator.class);

    private WebDriver driver;

    private WebDriverWait wait;
    private Timeout timeout;
    private UrlResolver urlResolver;

    private Boolean failOnException;

    WebDriverDecorator(WebDriver driver, Timeout timeout, UrlResolver urlResolver, Boolean failOnException) {
        this.driver = driver;

        this.timeout = timeout;
        this.wait = new WebDriverWait(driver, timeout.explicitWait());
        this.urlResolver = urlResolver;
        this.failOnException = failOnException;

        this.driver.manage().timeouts().implicitlyWait(timeout.implicitWait(), TimeUnit.SECONDS);
        this.driver.manage().window().maximize();
    }

    @Override
    public void get(String s) {
        driver.get(s);
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public WebElement findElement(By locator) {

        log.info("Waiting for presence of element: " + locator);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Element: " + locator + " was not present in DOM after: " + timeout.explicitWait() + " s");
            if (failOnException) {
                throw e;
            }
        }
        return driver.findElement(locator);
    }

    @Override
    public List<WebElement> findElements(By locator) {

        log.info("Waiting for presence of all elements: " + locator);
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            log.error("Elements: " + locator + " were not present in DOM after: " + timeout.explicitWait() + " s");
            if (failOnException) {
                throw e;
            }
        }
        return driver.findElements(locator);
    }

    public List<WebElement> findElements(By locator, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);

        log.info("Waiting for presence of all elements: " + locator + " with timeout of: " + timeoutInSeconds);
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            log.error("Elements: " + locator + " were not present in DOM after: " + timeoutInSeconds + " s");
            if (failOnException) {
                throw e;
            }
        }
        return driver.findElements(locator);
    }

    public List<WebElement> findElementsByPartialText(String partialText) {
        log.info("Waiting for presence of elements by partial text: " + partialText);
        By locator = By.xpath(String.format("//*[contains(text(), '%s')]", partialText));

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            log.error("Elements: " + locator + " were not present in DOM after: " + timeout.explicitWait() + " s");
            if (failOnException) {
                throw e;
            }
        }
        return driver.findElements(locator);
    }

    public WebElement findElementByPartialText(String partialText) {
        return findElementsByPartialText(partialText).get(0);
    }

    private WebElement findVisibleElement(By locator) {

        log.info("Waiting for visibility of element: " + locator);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Element: " + locator + " was not visible after: " + timeout.explicitWait() + " s");
            if (failOnException) {
                throw e;
            }
        }
        return driver.findElement(locator);
    }

    public List<WebElement> findVisibleElements(By locator) {

        log.info("Waiting for visibility of all elements: " + locator);
        try {
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            log.error("Elements: " + locator + " were not visible after: " + timeout.explicitWait() + " s");
            if (failOnException) {
                throw e;
            }
        }
        return driver.findElements(locator);
    }

    public int countElements(By locator) {
        log.info("Counting elements: " + locator);
        return driver.findElements(locator).size();
    }

    private void waitFor(long millisec) {
        log.info("Waiting for " + millisec + " ms");
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitForAJAX() {
        waitFor(200);
        log.info("Waiting for AJAX");
        wait.until((Function<WebDriver, Object>) driver -> ((JavascriptExecutor) driver)
                .executeScript("return jQuery.active == 0").equals(true));
    }

    public void waitForAngular() {
        log.info("Waiting for Angular");
        wait.until((Function<WebDriver, Object>) driver -> ((JavascriptExecutor) driver)
                .executeScript("return getAllAngularTestabilities()[0],isStable()").equals(true));
    }

    public void waitForElementToDisappear(By locator) {
        log.info("Waiting for element to disappear: " + locator);
        wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(locator)));
    }

    public void waitForElement(By locator) {
        log.info("Waiting for element: " + locator);
        findElement(locator);
    }

    public void waitForElementVisibility(By locator) {
        log.info("Waiting for element visibility: " + locator);
        findVisibleElement(locator);
    }

    public void clickOn(By locator) {
        log.info("Clicking on element: " + locator);
        try {
            findVisibleElement(locator).click();
        } catch (WebDriverException e) {
            log.error("It was not possible to click on element " + locator);
            if (failOnException) {
                throw e;
            }
        }
    }

    public void clickUsingActions(By locator) {
        log.info("Clicking on element using actions: " + locator);
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(findElement(locator));
            actions.click();
            actions.build().perform();
        } catch (WebDriverException e) {
            log.error("It was not possible to click on the element: " + locator);
            if (failOnException) {
                throw e;
            }
        }
    }

    public void clickOnEvery(By locator) {
        log.info("Clicking on every element: " + locator);
        try {
            findVisibleElements(locator).forEach(WebElement::click);
        } catch (WebDriverException e) {
            log.error("It was not possible to click on element " + locator);
            if (failOnException) {
                throw e;
            }
        }
    }

    public void tap(String selector) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        //String js = "var event = $.Event( 'touchstart', { pageX:200, pageY:200 } );";
        executor.executeScript("var event = $.Event( 'touchstart', { pageX:200, pageY:200 } );"
                + "$('" + selector + "').trigger( event );");
    }

    public void clearField(By locator) {
        log.info("Clearing field: " + locator);
        findVisibleElement(locator).clear();
    }

    public void type(By locator, String text) {
        log.info("Typing into: " + locator + " text: " + text);
        findVisibleElement(locator).sendKeys(String.valueOf(text));
    }

    public void clearAndType(By locator, String text) {
        clearField(locator);
        type(locator, text);
    }

    public void typeIntoInvisibleField(By locator, String text) {
        log.info("Typing into invisible: " + locator + " text: " + text);
        findElement(locator).sendKeys(String.valueOf(text));
    }

    public void selectByValue(By locator, String value) {
        log.info("Selecting option by value: " + value);
        Select select = new Select(findElement(locator));
        select.selectByValue(value);
    }

    public void selectByVisibleText(By locator, String text) {
        log.info("Selecting option by visible text: " + text);
        Select select = new Select(findElement(locator));
        select.selectByVisibleText(text);
    }

    public String getSelectedDropdownValue(By locator) {
        log.info("Getting selected option of: " + locator + " dropdown");

        Select select = new Select(findElement(locator));
        WebElement selectedOption = select.getFirstSelectedOption();
        return selectedOption.getText();
    }

    public void executeJavascript(String javascript) {
        log.info("Executing javascript: " + javascript);
        ((JavascriptExecutor) driver).executeScript(javascript);
    }

    public void executeJavascript(String javascript, By element) {
        log.info("Executing javascript: " + javascript + " on element: " + element);
        ((JavascriptExecutor) driver).executeScript(javascript, findElement(element));
    }

    private void executeJavascript(String javascript, WebElement element) {
        log.info("Executing javascript: " + javascript + " on element: " + element);
        ((JavascriptExecutor) driver).executeScript(javascript, element);
    }

    public void scrollIntoView(By locator) {
        log.info("Scrolling element into view: " + locator);
        executeJavascript("arguments[0].scrollIntoView(true)", driver.findElement(locator));
    }

    public void hoverOver(By locator) {
        log.info("Hovering over: " + locator);
        new Actions(driver).moveToElement(findElement(locator)).perform();
    }

    public void clickJS(By locator) {
        log.info("Clicking on element using Javascript: " + locator);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", findElement(locator));
    }

    public void openPage(Page page) {
        String url = urlResolver.getResolvedUrlFor(page);
        log.info("Navigating to URL: " + url);
        driver.navigate().to(url);
    }

    public void refreshPage() {
        log.info("Refreshing page: " + driver.getCurrentUrl());
        driver.navigate().refresh();
    }

    public String getElementText(By locator) {
        log.info("Getting text of element:" + locator);
        return findElement(locator).getText();
    }

    public List<String> getElementsText(By locator) {
        log.info("Getting text of all elements: " + locator);
        return findElements(locator).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String getAttribute(By locator, String attribute) {
        log.info("Getting " + attribute + " value of element: " + locator);
        return findElement(locator).getAttribute(attribute);
    }

    public List<String> getAttributes(By locator, String attribute) {
        log.info("Getting " + attribute + " values of element: " + locator);
        List<String> result = new ArrayList<>();
        findElements(locator).forEach(e -> result.add(e.getAttribute(attribute)));
        return result;
    }

    public String getCssValue(By locator, String cssKey) {
        log.info("Getting css " + cssKey + " value of " + locator + " element");
        return findElement(locator).getCssValue(cssKey);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getURLSuffix() {
        return driver.getCurrentUrl().
                replaceAll(urlResolver.getUiBaseUrl(), "");
    }

    public boolean isElementTextChangedTo(By locator, String text) {
        log.info("Getting asynchronous text of " + locator + " element");
        return wait.until(ExpectedConditions.textToBePresentInElementValue(findElement(locator), text));
    }

    public boolean isSelected(By locator) {
        log.info("Getting text of " + locator + " element");
        return findElement(locator).isSelected();
    }

    public boolean isVisible(By locator) {
        log.info("Checking if " + locator + " is visible");
        return findVisibleElement(locator) != null;
    }

    public boolean isPresent(By locator) {
        log.info("Checking if " + locator + " is present");
        return findElement(locator) != null;
    }

    public boolean arePresent(By locator, int expectedElementsCount) {
        boolean result;

        log.info("Checking if multiple gui:" + locator + " are visible");
        List<WebElement> elements = findElements(locator);
        result = (elements.size() == expectedElementsCount);

        for (WebElement element : elements) {
            result &= element.isDisplayed();
        }

        return result;
    }

    public Boolean isEnabled(By locator) {
        log.info("Verifying wether elemtn is enabled: " + locator);
        return driver.findElement(locator).isEnabled();
    }

    public void press(Keys key) {
        log.info("Pressing key: " + key);
        driver.findElement(By.cssSelector("body")).sendKeys(key);
    }

    public void shiftFocus() {
        log.info("Shifting focus from an element");
        clickOn(By.cssSelector("body"));
    }

    public void acceptAlert() {
        log.info("Accepting alert");
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            log.error("Alert was not present");
            if (failOnException) {
                throw e;
            }
        }
    }

    public void dismissAlert() {
        log.info("Dismissing alert");
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
        } catch (TimeoutException e) {
            log.error("Alert was not present");
            if (failOnException) {
                throw e;
            }
        }
    }

    @Attachment(value = "PageObject screenshot", type = "image/png")
    public byte[] takeScreenshot() {
        log.info("Taking screenshot on test failure");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}

package web_core;

import com.google.common.collect.Lists;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.Reporter;
import util.CommonUtil;
import util.LogHelper;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.awaitility.Awaitility.await;

@SuppressWarnings("unused")
public class WebApi {

    WebElement element;
    List<WebElement> elements;
    JavascriptExecutor javascriptExecutor;
    WebDriverWait waitExplicit;
    Actions action;
    By byLocator;
    public WebDriver driver;
    boolean status;

    public WebApi() {
        this.driver = WebAppDriverManager.getDriver();
        PageFactory.initElements(this.driver, this);
        javascriptExecutor = (JavascriptExecutor) driver;
    }

    public void openAnyUrl(String url) {
        driver.get(url);
    }

    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getPageSourceCode() {
        return driver.getPageSource();
    }

    public void backToPreviousPage() {
        driver.navigate().back();
    }

    public WebApi refreshCurrentPage() {
        driver.navigate().refresh();
        return this;
    }

    public void forwardToNextPage() {
        driver.navigate().forward();
    }

    @Step
    public WebApi acceptAlert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().accept();
        }
        return this;
    }

    public void cancelAlert() {
        driver.switchTo().alert().dismiss();
    }

    public String getTextAlert() {
        return driver.switchTo().alert().getText();
    }

    public void sendKeyToAlert(String value) {
        driver.switchTo().alert().sendKeys(value);
    }

    public void clearTextElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        element.clear();
    }

    public void clearTextElement(WebElement element) {
        element = waitForElementVisible(element);
        element.clear();
    }

    public void selectAllWithKeys(WebElement element) {
        // Click element to focus on it first, then send a string sequence
        clickElement(element);
        String os = System.getProperty("os.name");
        action = new Actions(driver);
        if (os.contains("Mac")) {
            action.keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND).build().perform();
        } else {
            action.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).build().perform();
        }
    }

    public void selectAllWithKeys(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        selectAllWithKeys(element);
    }

    public void clearTextElementWithKeys(WebElement element) {
        element = waitForElementVisible(element);
        selectAllWithKeys(element);
        action = new Actions(driver);
        action.sendKeys(Keys.DELETE).build().perform();
    }

    public void clearTextElementWithKeys(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        selectAllWithKeys(element);
        action = new Actions(driver);
        action.sendKeys(Keys.DELETE).build().perform();
    }

    public void clearTextElementWithKeys(String locator) {
        element = waitForElementVisible(locator);
        selectAllWithKeys(element);
        action = new Actions(driver);
        action.sendKeys(Keys.DELETE).build().perform();
    }

    public void clickElement(WebElement element) {
        waitForElementClickable(element);
        element = waitForElementVisible(element);
        element.click();
    }

    public void clickElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        waitForElementClickable(locator);
        element = waitForElementVisible(locator);
        element.click();
    }

    public void submitForm(WebElement element) {
        element = waitForElementVisible(element);
        element.submit();
    }

    public void sendKeyToElement(WebElement element, String value) {
        element = waitForElementVisible(element);
        element.sendKeys(value);
    }

    public void sendKeyToElement(String locator, String sendKeyValue,
                                 String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        element.sendKeys(sendKeyValue);
    }

    public List<String> getAllTextElementFromDrd(String locator) {
        element = waitForElementVisible(locator);
        List<WebElement> elementList = getListElementFromDrd(element);
        List<String> elementText = new ArrayList<>();
        elementList.forEach(webElement -> elementText.add(webElement.getText()));
        return elementText;
    }

    public List<String> getAllTextElementFromDrd(WebElement element) {
        List<WebElement> elementList = getListElementFromDrd(element);
        List<String> elementText = new ArrayList<>();
        elementList.forEach(webElement -> elementText.add(webElement.getText()));
        return elementText;
    }

    public void selectItemInDropdown(String locator, String expectedValueInDropdown,
                                     String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        Select select = new Select(element);
        select.selectByVisibleText(expectedValueInDropdown);
    }

    public void selectItemInDropdown(WebElement element, int index) {
        element = waitForElementVisible(element);
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    public void selectRandomItemInDropdown(WebElement element) {
        element = waitForElementVisible(element);
        Select select = new Select(element);
        String selected = select.getFirstSelectedOption().getText();
        int selectIndex = CommonUtil.getRandomIntegerBetweenRange(1, select.getOptions().size() - 1);
        if (select.getOptions().get(selectIndex).getText().equalsIgnoreCase(selected)) {
            select.selectByIndex(selectIndex - 1);
        } else {
            select.selectByIndex(selectIndex);
        }
        Assert.assertNotEquals(select.getFirstSelectedOption().getText(), selected);
    }

    public void selectRandomItemInDropdownExclude(WebElement element, String exclude) {
        element = waitForElementVisible(element);
        Select select = new Select(element);
        String selected = select.getFirstSelectedOption().getText();
        int selectIndex = CommonUtil.getRandomIntegerBetweenRange(1, select.getOptions().size() - 1);
        if (select.getOptions().get(selectIndex).getText().equalsIgnoreCase(selected) || select.getOptions().get(selectIndex).getText().equalsIgnoreCase(exclude)) {
            select.selectByIndex(selectIndex - 1);
        } else {
            select.selectByIndex(selectIndex);
        }
        Assert.assertNotEquals(select.getFirstSelectedOption().getText(), selected);
    }

    public void selectRandomItemInDropdown(String locator, String... value) {
        element = waitForElementVisible(locator, value);
        Select select = new Select(element);
        String selected = select.getFirstSelectedOption().getText();
        int selectIndex = CommonUtil.getRandomIntegerBetweenRange(1, select.getOptions().size() - 1);
        if (select.getOptions().get(selectIndex).getText().equalsIgnoreCase(selected)) {
            select.selectByIndex(selectIndex - 1);
        } else {
            select.selectByIndex(selectIndex);
        }
        Assert.assertNotEquals(select.getFirstSelectedOption().getText(), selected);
    }

    public void selectItemInDropdown(WebElement element, String expectedValueInDropdown) {
        element = waitForElementVisible(element);
        Select select = new Select(element);
        select.selectByVisibleText(expectedValueInDropdown);
    }

    public List<WebElement> getListElementFromDrd(WebElement locator) {
        element = waitForElementVisible(locator);
        Select select = new Select(element);
        return select.getOptions();
    }

    public String getFirstSelectedItemInDropdown(String locator) {
        element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    public String getFirstSelectedItemInDropdown(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    public int getSizeSelectOption(WebElement e) {
        element = waitForElementVisible(e);
        Select select = new Select(element);
        return select.getOptions().size();
    }

    public List<String> getAllSelectOption(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        List<String> listSelectText = new ArrayList<>();
        select.getOptions().forEach(
                (webElement -> listSelectText.add(webElement.getText()))
        );
        return listSelectText;
    }

    public void selectItemInCustomDropdown(String parentXpath, String allItemXpath,
                                           String expectedItem) throws Exception {

        javascriptExecutor = (JavascriptExecutor) driver;
        WebElement parentDropdown = driver.findElement(By.xpath(parentXpath));
        javascriptExecutor.executeScript("arguments[0].click();", parentDropdown);

        waitExplicit = new WebDriverWait(driver, Duration.ofSeconds(30));
        waitExplicit
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(allItemXpath)));

        List<WebElement> all_Item = driver.findElements(By.xpath(allItemXpath));
        for (WebElement childElement : all_Item) {
            if (childElement.getText().equals(expectedItem)) {
                javascriptExecutor
                        .executeScript("arguments[0].scrollIntoView(true);", childElement);
                Thread.sleep(1000);
                if (childElement.isDisplayed()) {
                    childElement.click();
                } else {
                    javascriptExecutor.executeScript("arguments[0].click();", childElement);
                }
                Thread.sleep(1000);
                break;
            }

        }
    }

    public List<WebElement> getAllElement(String locator) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        List<WebElement> elements = new ArrayList<>();
        byLocator = By.xpath(locator);
        try {
            elements = waitExplicit.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byLocator));
        } catch (Exception ex) {
            LogHelper.getInstance().warn(
                    "=========================== Element not visible==============================");
            LogHelper.getInstance().warn(ex.getMessage());
            System.err.println(
                    "================================== Element not visible===================================");
            System.err.println(ex.getMessage() + "\n");
        }
        return elements;
    }

    public String getAttributeValue(String locator, String attributeName,
                                    String... values) {
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        return element.getAttribute(attributeName);
    }

    public String getAttributeValue(WebElement element, String attributeName) {
        element = waitForElementVisible(element);
        return element.getAttribute(attributeName);
    }

    public String getAttributeValue(String attributeName) {
        return element.getAttribute(attributeName);
    }


    public boolean isAttributePresent(String locator, String attributeName, String attributeValue, String... values) {
        boolean temp = true;
        String actualValue = getAttributeValue(locator, attributeName, values);
        return actualValue.equals(attributeValue);
    }

    public String getTextElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        return element.getText();
    }

    public String getTextElement(WebElement element) {
        element = waitForElementVisible(element);
        return element.getText();
    }

    public String getTextElementByValue(WebElement element) {
        element = waitForElementVisible(element);
        return element.getAttribute("value");
    }

    public String getParentWindowID() {
        return driver.getWindowHandle();
    }

    public int countElementNumber(String locator) {
        overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        elements = driver.findElements(By.xpath(locator));
        resetTimeOut();
        return elements.size();
    }

    public void checkToCheckBoxOrRadioButton(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void checkToCheckBoxOrRadioButton(WebElement element) {
        element = waitForElementVisible(element);
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void unCheckToCheckBox(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        if (element.isSelected()) {
            element.click();
        }
    }

    public void unCheckToCheckBox(WebElement element) {
        element = waitForElementVisible(element);
        if (element.isSelected()) {
            element.click();
        }
    }

    public boolean isControlDisplayed(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        overrideGlobalTimeout(Duration.ZERO);
        try {
            element = driver.findElement(By.xpath(locator));
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return false;
        }
    }

    public boolean isControlDisplayed(WebElement element) {
        element = waitForElementVisible(element);
        return element.isDisplayed();
    }

    public void verifyControlDisplayed(WebElement element, String nameOfElement) {
        Assert.assertTrue(isControlDisplayed(element), nameOfElement + " is not displayed");
    }

    public void verifyControlDisplayed(String locator, String nameOfElement, String... values) {
        Assert.assertTrue(isControlDisplayed(locator, values), nameOfElement + " is not displayed");
    }

    public boolean isControlUnDisplayed(String locator) {
        overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        if (elements.isEmpty()) {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else if (!elements.get(0).isDisplayed()) {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return false;
        }
    }

    public boolean isControlUnDisplayed(String locator, String... values) {
        overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        locator = String.format(locator, (Object[]) values);
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        if (elements.isEmpty()) {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else if (!elements.get(0).isDisplayed()) {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return false;
        }
    }

    public boolean isControlEnabled(WebElement element) {
        element = waitForElementVisible(element);
        return element.isEnabled();
    }

    public boolean isControlEnabled(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        return element.isEnabled();
    }

    public boolean isControlSelected(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        return element.isSelected();
    }

    public boolean isControlSelected(WebElement element) {
        element = waitForElementVisible(element);
        return element.isSelected();
    }

    public void switchToChildWindowByID(String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            if (!runWindow.equals(parentID)) {
                driver.switchTo().window(runWindow);
                break;
            }
        }
    }

    public void switchToChildWindowByTitle(String expectedTitle) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            driver.switchTo().window(runWindow);
            String currentWindowTitle = driver.getTitle();
            if (currentWindowTitle.equals(expectedTitle)) {
                break;
            }
        }
    }

    @Step
    public WebApi waitForNumberOfTabsToBe(int number) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> Assert.assertEquals(driver.getWindowHandles().size(), number));
        return this;
    }

    @Step
    public WebApi switchToTabNumber(int number) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> Assert.assertTrue(driver.getWindowHandles().size() >= number));
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(number - 1));
        return this;
    }

    public WebApi switchToLastTab() {
        sleepTimeInMilSecond(1000);
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
        return this;
    }

    public boolean closeAllExceptParentWindow(String parentWindows) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            if (!runWindow.equals(parentWindows)) {
                driver.switchTo().window(runWindow);
                driver.close();
            }
        }
        driver.switchTo().window(parentWindows);
        return driver.getWindowHandles().size() == 1;
    }

    public void closeAllWindows() {
        Set<String> allWindows = driver.getWindowHandles();
        List<String> newAllWindows = new ArrayList<>(allWindows);
        for (String runWindow : Lists.reverse(newAllWindows)) {
            sleepTimeInMilSecond(200);
            driver.switchTo().window(runWindow).close();
        }
    }

    public WebApi closeCurrentWindow() {
        String currentWindow = driver.getWindowHandle();
        driver.switchTo().window(currentWindow);
        driver.close();
        return this;
    }

    public void switchToFrame(String locator) {
        element = driver.findElement(By.xpath(locator));
        driver.switchTo().frame(element);
    }

    public void backToTopWindow() {
        driver.switchTo().defaultContent();
    }

    public void hoverMouseToElement(String locator) {
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    public void clickToElementByAction(WebElement element) {
        javascriptExecutor.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", element);
        action = new Actions(driver);
        waitForElementVisible(element);
        action.click(element).perform();
    }

    public void hoverMouseToElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    public void doubleClickElement(String locator) {
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.doubleClick(element);
    }

    public void doubleClickElement(WebElement e) {
        action = new Actions(driver);
        action.doubleClick(e).build().perform();
    }

    public void doubleClickToElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.doubleClick(element).build().perform();
    }

    public void rightClickElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.contextClick(element).build().perform();
    }

    public void rightClickElement(String locator) {
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.contextClick(element);
    }

    public void dragAndDrop(String sourceLocator, String targetLocator) {
        WebElement sourceElement, targetElement;
        sourceElement = driver.findElement(By.xpath(sourceLocator));
        targetElement = driver.findElement(By.xpath(targetLocator));
        action = new Actions(driver);
        action.dragAndDrop(sourceElement, targetElement).build().perform();
    }

    public void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
        action = new Actions(driver);
        action.dragAndDrop(sourceElement, targetElement).build().perform();
    }

    public void dragAndDropBy(WebElement sourceElement, int offsetX, int offsetY) {
        action = new Actions(driver);
        action.dragAndDropBy(sourceElement, offsetX, offsetY).build().perform();
    }

    public void sendKeyBoardToElement(WebElement element, Keys key) {
        action = new Actions(driver);
        action.sendKeys(element, key).perform();
    }

    public void sendKeyBoardToElement(String locator, Keys key) {
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.sendKeys(element, key).perform();
    }

    public void sendKeyBoardToElement(String locator, Keys key, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        action = new Actions(driver);
        action.sendKeys(element, key).perform();
    }

    public WebApi sleepTimeInSecond(int second) {
        try {
            Thread.sleep(second * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public WebApi sleepTimeInMilSecond(int milSecond) {
        try {
            Thread.sleep(milSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void highlightElement(String locator) {
        //		javascriptExecutor = (JavascriptExecutor) driver;
        //		element = driver.findElement(By.xpath(locator));
        //		String originalStyle = element.getAttribute("Style");
        //		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", element, "style",
        //				"border:3px solid red; border-style: dashed;");
        //		try {
        //			Thread.sleep(Constants.HIGHLIGHT_ELEMENT_TIMEOUT_FOR_DEMO);
        //		} catch (InterruptedException e) {
        //			e.printStackTrace();
        //		}
        //		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", element, "style",
        //				originalStyle);

    }

    public Object executeForBrowser(String javascript) {
        javascriptExecutor = (JavascriptExecutor) driver;
        return javascriptExecutor.executeScript(javascript);
    }

    public void clickToElementByJavascript(String locator) {
        javascriptExecutor = (JavascriptExecutor) driver;
        element = driver.findElement(By.xpath(locator));
        javascriptExecutor.executeScript("arguments[0].click();", element);
    }

    public void clickToElementByJavascript(WebElement ele) {
        javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].click();", ele);
    }

    public void clickToElementByJavascript(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        javascriptExecutor = (JavascriptExecutor) driver;
        element = driver.findElement(By.xpath(locator));
        javascriptExecutor.executeScript("arguments[0].click();", element);
    }

    public void sendKeyToElementByJavascript(String locator, String value) {
        javascriptExecutor = (JavascriptExecutor) driver;
        element = driver.findElement(By.xpath(locator));
        javascriptExecutor
                .executeScript("arguments[0].setAttribute('value', '" + value + "')", element);
    }

    public void removeAttributeInDOM(String locator, String attribute) {
        javascriptExecutor = (JavascriptExecutor) driver;
        element = driver.findElement(By.xpath(locator));
        javascriptExecutor
                .executeScript("arguments[0].removeAttribute('" + attribute + "');", element);
    }

    public void removeAttributeInDOM(String locator, String attribute,
                                     String... values) {
        javascriptExecutor = (JavascriptExecutor) driver;
        locator = String.format(locator, (Object[]) values);
        element = driver.findElement(By.xpath(locator));
        javascriptExecutor
                .executeScript("arguments[0].removeAttribute('" + attribute + "');", element);
    }

    public void setAttributeInDOM(String locator, String attribute, String value) {
        element = driver.findElement(By.xpath(locator));
        javascriptExecutor
                .executeScript("arguments[0].setAttribute('" + attribute + "', '" + value + "');",
                        element);
    }

    public void scrollToBottomOfElement(WebElement e) {
        javascriptExecutor.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", e);
        sleepTimeInMilSecond(500);
    }

    public void scrollToTopOfElement(WebElement e) {
        javascriptExecutor.executeScript("arguments[0].scrollTop = -arguments[0].scrollHeight", e);
        sleepTimeInMilSecond(500);
    }

    public void scrollToRightOfElement(WebElement e, double rate) {
        javascriptExecutor.executeScript("arguments[0].scrollLeft += arguments[0].scrollWidth*arguments[1]", e, rate);
        sleepTimeInMilSecond(500);
    }

    public void scrollToCentralOfPage() {
        javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight/2)");
        sleepTimeInMilSecond(500);
    }

    public WebApi scrollToBottomPage() {
        javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        sleepTimeInMilSecond(500);
        return this;
    }

    public void navigateToUrlByJavascript(String url) {
        javascriptExecutor.executeScript("window.location = '" + url + "'");
        sleepTimeInMilSecond(500);
    }

    public void uploadSingleFileBySendKeyToElement(String filePath,
                                                   String openButtonLocator,
                                                   String uploadButtonLocator) {
        driver.findElement(By.xpath(openButtonLocator)).sendKeys(filePath);
        driver.findElement(By.xpath(uploadButtonLocator)).click();
    }

    public void uploadMultiFilesBySendKeyToElement(String[] allFilesPaths,
                                                   String openButtonLocator,
                                                   String uploadButtonLocator) {
        for (String file : allFilesPaths) {
            driver.findElement(By.xpath(openButtonLocator)).sendKeys(file);
        }
        List<WebElement> allStartBtn = driver.findElements(By.xpath(uploadButtonLocator));
        for (WebElement startButton : allStartBtn) {
            startButton.click();
        }
    }

    public void waitForElementPresence(String locator) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        byLocator = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.presenceOfElementLocated(byLocator));
    }

    public void waitForAllElementsPresence(String locator) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        byLocator = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.presenceOfAllElementsLocatedBy(byLocator));
    }

    public List<WebElement> waitForAllElementsPresence(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        byLocator = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.presenceOfAllElementsLocatedBy(byLocator));
        return this.driver.findElements(byLocator);
    }

    public boolean waitForElementText(String locator, String text, String... values) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        locator = String.format(locator, (Object[]) values);
        byLocator = By.xpath(locator);
        try {
            return waitExplicit.until(ExpectedConditions.textToBePresentInElementLocated(byLocator, text));
        } catch (Exception ex) {
            LogHelper.getInstance().warn(
                    "=========================== Element text not visible==============================");
            LogHelper.getInstance().warn(ex.getMessage());
            System.err.println(
                    "================================== Element text not visible===================================");
            System.err.println(ex.getMessage() + "\n");
            return false;
        }
    }

    public boolean waitForElementText(WebElement element, String text) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        try {
            return waitExplicit.until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (Exception ex) {
            LogHelper.getInstance().warn(
                    "=========================== Element text not visible==============================");
            LogHelper.getInstance().warn(ex.getMessage());
            System.err.println(
                    "================================== Element text not visible===================================");
            System.err.println(ex.getMessage() + "\n");
            return false;
        }
    }

    public WebElement waitForElementVisible(String locator, String... values) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        locator = String.format(locator, (Object[]) values);
        byLocator = By.xpath(locator);
        try {
            return waitExplicit.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
        } catch (Exception ex) {
            LogHelper.getInstance().warn(
                    "=========================== Element not visible==============================");
            LogHelper.getInstance().warn(ex.getMessage());
            System.err.println(
                    "================================== Element not visible===================================");
            System.err.println(ex.getMessage() + "\n");
            return null;
        }
    }

    public WebElement waitForElementVisible(WebElement element) {
        overrideGlobalTimeout(Duration.ZERO);
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        element = waitExplicit.until(ExpectedConditions.visibilityOf(element));
        waitExplicit.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
        resetTimeOut();
        return element;
    }

    public void waitForElementClickable(String locator) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        byLocator = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.elementToBeClickable(byLocator));
    }

    public WebElement waitForElementClickable(WebElement element) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        return waitExplicit.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementClickable(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        byLocator = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.elementToBeClickable(byLocator));
    }

    public void waitForTextToBePresentInElement(WebElement e, String expectedText) {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        waitExplicit.until(ExpectedConditions.textToBePresentInElement(e, expectedText));
    }

    public void waitForTextToBePresentInElement(String locator, String expectedText, String... values) {
        locator = String.format(locator, (Object[]) values);
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        byLocator = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.textToBePresentInElementLocated(byLocator, expectedText));
    }

    public void waitForElementInvisible(WebElement element) {
        overrideGlobalTimeout(Duration.ZERO);
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getSHORT_TIMEOUT());
        try {
            waitExplicit.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception ex) {
            LogHelper.getInstance().warn(
                    "=========================== Element not invisible==============================");
            LogHelper.getInstance().warn(ex.getMessage());
            System.err.println(
                    "================================== Element not invisible===================================");
            System.err.println(ex.getMessage() + "\n");
        }
        overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
    }

    public void waitForElementInvisible(String locator) {
        overrideGlobalTimeout(Duration.ZERO);
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        byLocator = By.xpath(locator);
        waitExplicit.until(ExpectedConditions.invisibilityOfElementLocated(byLocator));
        overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
    }

    public void waitForAllElementsInvisible(String locator) {
        overrideGlobalTimeout(Duration.ZERO);
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return driver.findElements(By.xpath(locator)).size() == 0;
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, WebAppDriverManager.getVERY_LONG_TIMEOUT());
        wait.until(expectation);
        overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
    }

    public boolean isAlertPresent() {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getSHORT_TIMEOUT());
        try {
            waitExplicit.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForAlertPresence() {
        waitExplicit = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
        waitExplicit.until(ExpectedConditions.alertIsPresent());
    }

    public void overrideGlobalTimeout(Duration timeOut) {
        driver.manage().timeouts().implicitlyWait(timeOut);
    }

    public void resetTimeOut() {
        driver.manage().timeouts().implicitlyWait(WebAppDriverManager.getLONG_TIMEOUT());
    }

    @Step
    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            WebDriverWait wait = new WebDriverWait(driver, WebAppDriverManager.getLONG_TIMEOUT());
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }

    @Step
    public WebApi waitForLoadingIconToDisappear() {
        sleepTimeInMilSecond(500);
        waitForAllElementsInvisible("//div[contains(@class,'AppIndicator__backdrop_')]");
        waitForAllElementsInvisible("//div[contains(@class,'AppIndicator__indicator')]");
        waitForAllElementsInvisible("//div[contains(@class,'Indicator__indicator')]");
        waitForAllElementsInvisible("//div[contains(@class,'Indicator__indicator')]");
        return this;
    }

    /**
     * Scroll to specific element
     *
     * @param locatorOrElement: locator of element: String or WebElement
     * @param values:           For dynamic locator
     */
    public void scrollToElement(Object locatorOrElement, String... values) {
        if (locatorOrElement instanceof WebElement) {
            element = (WebElement) locatorOrElement;
        } else {
            String locator = String.format((String) locatorOrElement, (Object[]) values);
            element = driver.findElement(By.xpath(locator));
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        sleepTimeInMilSecond(500);
    }

    /**
     * Scroll to specific element to view option
     *
     * @param element:     WebElement
     * @param viewOptions: view options (Optional):
     *                     behavior: the transition animation: auto, smooth. Defaults to auto.
     *                     block: vertical alignment: start, center, end, nearest. Defaults to start.
     *                     inline: horizontal alignment: start, center, end, nearest. Defaults to nearest.
     *                     Here are the samples:
     *                     chrome, edge, firefox:
     *                     1. arguments[0].scrollIntoView(); empty means true value
     *                     2. arguments[0].scrollIntoView(true);
     *                     3. arguments[0].scrollIntoView({block: "start", inline: "nearest"}); ==> 1, 2, 3 are the same
     *                     4. arguments[0].scrollIntoView(false);
     *                     5. arguments[0].scrollIntoView({block: "end", inline: "nearest"}); ==> 4, 5 are the same
     *                     6. arguments[0].scrollIntoView({behavior: "smooth", block: "end", inline: "nearest"});
     *                     safari:
     *                     1. arguments[0].scrollIntoViewIfNeeded(); empty means true value
     *                     2. arguments[0].scrollIntoViewIfNeeded(true); ==> 1, 2 are the same. The element will be aligned so it is CENTERED within the visible area of the scrollable ancestor.
     *                     3. arguments[0].scrollIntoViewIfNeeded(false); The element will be aligned to the NEAREST EDGE of the visible area of the scrollable ancestor.
     */
    public void scrollToElement(WebElement element, String... viewOptions) {
        String scriptFormatString = getBrowserName().equalsIgnoreCase("safari") ? "arguments[0].scrollIntoViewIfNeeded(%s);" : "arguments[0].scrollIntoView(%s);";
        String script = viewOptions.length == 0 ? String.format(scriptFormatString, "") : String.format(scriptFormatString, String.join("", viewOptions));
        ((JavascriptExecutor) driver).executeScript(script, element);
    }

    /**
     * Scroll to element when un know element state
     *
     * @param yOffset    Positive: Scroll down,
     *                   Negative: Scroll up.
     * @param element    Element witch is not exist/not displayed until scroll to.
     * @param scrollTime scroll time to avoid looping forever.
     */
    public void scrollToElementUntilDisplayed(int yOffset, WebElement element, int scrollTime) {
        int count = 0;
        boolean isDisplayed;
        do {
            scrollTo(0, yOffset);
            isDisplayed = isControlDisplayed(element, 1L);
            count++;
        } while ((!isDisplayed && count < scrollTime));
    }

    /**
     * Scroll up/down util exceed scroll time
     *
     * @param yOffset:    positive: Scroll Up
     *                    negative: Scroll Down
     * @param scrollTime: the scroll time.
     */
    public void scrollUntil(int yOffset, int scrollTime) {
        int count = 0;
        do {
            scrollTo(0, yOffset);
            count++;
        } while (count < scrollTime);
    }

    /**
     * Check if control is displayed with custom time out.
     *
     * @param element  : Element
     * @param timeout: time out
     * @return true if element is displayed. otherwise return false.
     */
    public boolean isControlDisplayed(WebElement element, long timeout) {
        try {
            element = waitForElementVisible(element, timeout);
            return element.isDisplayed();
        } catch (Exception var3) {
            return false;
        }
    }

    /**
     * Wait for element visible with custom time out.
     *
     * @param element
     * @param timeout
     * @return Web element if found. otherwise null.
     */
    public WebElement waitForElementVisible(WebElement element, long timeout) {
        overrideGlobalTimeout(Duration.ZERO);
        waitExplicit = new WebDriverWait(this.driver, Duration.ofSeconds(timeout));
        try {
            WebElement e = waitExplicit.until(ExpectedConditions.visibilityOf(element));
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return e;
        } catch (Exception var3) {
            overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return null;
        }
    }

    /**
     * Scroll to Top of page
     */
    public void scrollToTopOfPage() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,0)");
    }

    /**
     * Scroll to (x,y).
     *
     * @param x
     * @param y
     */
    public void scrollTo(int x, int y) {
        ((JavascriptExecutor) driver).executeScript(String.format("window.scrollBy(%s,%s)", x, y));
    }

    /**
     * Return true if is safari browser.
     */
    public static boolean isSafari() {
        String browser = System.getProperty("web.browser");
        if (browser.equalsIgnoreCase("safari")) {
            return true;
        }
        return false;
    }

    /**
     * Return true if is Mac OS.
     */
    public static boolean isMacOs() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("mac")) {
            return true;
        }
        return false;
    }

    /**
     * Return true if is Linux.
     */
    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("linux")) {
            return true;
        }
        return false;
    }

    public void clickToElementSafari(WebElement element) {
        if (isSafari()) {
            waitForElementVisible(element);
            clickToElementByJavascript(element);
        } else {
            clickElement(element);
        }
    }

    public void clickToElementSafari(String locator, String... param) {
        if (isSafari()) {
            waitForElementVisible(locator, param);
            clickToElementByJavascript(locator, param);
        } else {
            clickElement(locator, param);
        }
    }

    public void waitForElementInvisible(String locator, long timeout, String... values) {
        overrideGlobalTimeout(Duration.ZERO);
        waitExplicit = new WebDriverWait(this.driver, Duration.ofSeconds(timeout));
        locator = String.format(locator, (Object[]) values);
        byLocator = By.xpath(locator);
        try {
            waitExplicit.until(ExpectedConditions.invisibilityOfElementLocated(byLocator));
        } catch (Exception var4) {
            LogHelper.getInstance().warn(var4.getMessage());
        }
        overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
    }

    public String getCssValue(String locator, String propertyName, String... values) {
        locator = String.format(locator, (Object[]) values);
        element = waitForElementVisible(locator);
        return element.getCssValue(propertyName);
    }

    public String getCssValue(WebElement element, String propertyName) {
        element = this.waitForElementVisible(element);
        return element.getCssValue(propertyName);
    }

    public void sendKeyToElementByJavascript(WebElement element, String value) {
        javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", element);
    }

    public void waitForPageLoadedByAsync() {
        String async = String.format("var readyCallback = arguments[arguments.length - 1];"
                + "var checkReadyState=function() {"
                + "document.readyState !== 'complete' ? setTimeout(checkReadyState, %s) : readyCallback(true);"
                + "};"
                + "checkReadyState();", WebAppDriverManager.getLONG_TIMEOUT());

        try {
            Wait wait = new FluentWait(driver).withTimeout(WebAppDriverManager.getLONG_TIMEOUT())
                    .pollingEvery(WebAppDriverManager.getMILLI_TIMEOUT())
                    .ignoring(Exception.class);
            wait.until(
                    new Function<WebDriver, Boolean>() {
                        @Override
                        public Boolean apply(WebDriver webDriver) {
                            return (Boolean) ((JavascriptExecutor) driver).executeAsyncScript(async, new Object[0]);
                        }
                    });
        } catch (Throwable var3) {
            Reporter.log("Timeout waiting for Page loaded by asynchronous.", true);
        }
    }

    /**
     * Get list of web elements with message format from Findby annotaion
     *
     * @param elements : List of web elements
     * @param args     : arguments of message format
     * @return List of elements with right locator field
     */
    public List<WebElement> getElementsWithArgs(List<WebElement> elements, Object... args) {
        if (args == null || args.length == 0) {
            return elements;
        }
        By by = null;
        if (java.lang.reflect.Proxy.isProxyClass(elements.getClass())) {
            LocatingElementListHandler
                    handler = (LocatingElementListHandler) java.lang.reflect.Proxy.getInvocationHandler(elements);
            try {
                Field locatorField = LocatingElementListHandler.class.getDeclaredField("locator");
                locatorField.setAccessible(true);
                DefaultElementLocator locator = (DefaultElementLocator) locatorField.get(handler);
                Field byFeild = DefaultElementLocator.class.getDeclaredField("by");
                byFeild.setAccessible(true);
                by = (By) byFeild.get(locator);
                by = this.parameter(by, args);
                byFeild.set(locator, by);
            } catch (IllegalAccessException e) {
                Reporter.log("Cannot access the locator field by reflection: " + e.getMessage(), true);
            } catch (NoSuchFieldException e) {
                Reporter.log("No such field matches to locator section: " + e.getMessage(), true);
            }
        }
        return by == null ? elements : driver.findElements(by);
    }

    /**
     * Get web element with message format from Findby annotaion
     *
     * @param element : web element
     * @param args    : arguments of message format
     * @return web element by right locator field with message format
     */
    public WebElement getElementWithArgs(WebElement element, Object... args) {
        if (args == null || args.length == 0) {
            return element;
        }
        By by = null;
        if (java.lang.reflect.Proxy.isProxyClass(element.getClass())) {
            LocatingElementHandler
                    handler = (LocatingElementHandler) java.lang.reflect.Proxy.getInvocationHandler(element);
            try {
                Field locatorField = LocatingElementHandler.class.getDeclaredField("locator");
                locatorField.setAccessible(true);
                DefaultElementLocator locator = (DefaultElementLocator) locatorField.get(handler);
                Field byFeild = DefaultElementLocator.class.getDeclaredField("by");
                byFeild.setAccessible(true);
                by = (By) byFeild.get(locator);
                by = this.parameter(by, args);
                byFeild.set(locator, by);
            } catch (IllegalAccessException e) {
                Reporter.log("Cannot access the locator field by reflection: " + e.getMessage(), true);
            } catch (NoSuchFieldException e) {
                Reporter.log("No such field matches to locator section: " + e.getMessage(), true);
            }
        }
        return by == null ? element : driver.findElement(by);
    }

    private By parameter(By by, Object... args) {
        String byDesc = by.toString();
        if (byDesc.indexOf(":") == -1) {
            return by;
        }
        String using = byDesc.substring(byDesc.indexOf(":") + 2);
        using = MessageFormat.format(using.replace("'", "''"), args);

        if (byDesc.startsWith("By.id")) {
            return By.id(using);
        } else if (byDesc.startsWith("By.name")) {
            return By.name(using);
        } else if (byDesc.startsWith("By.className")) {
            return By.className(using);
        } else if (byDesc.startsWith("By.cssSelector")) {
            return By.cssSelector(using);
        } else if (byDesc.startsWith("By.xpath")) {
            return By.xpath(using);
        } else if (byDesc.startsWith("By.tagName")) {
            return By.tagName(using);
        } else if (byDesc.startsWith("By.linkText")) {
            return By.linkText(using);
        } else if (byDesc.startsWith("By.partialLinkText")) {
            return By.partialLinkText(using);
        } else {
            return by;
        }
    }

    public String getBrowserName() {
        return ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
    }

    @Step
    public WebApi verifyUrl(String expected) {
        waitForPageLoaded();
        await().atMost(20, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    LogHelper.getInstance().info(this.driver.getCurrentUrl());
                    Assert.assertTrue(this.driver.getCurrentUrl().contains(expected));
                });
        return this;
    }
}
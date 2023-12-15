package web_core;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import util.LogHelper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

public class WebAppDriverManager {
    public static ThreadLocal<Boolean> responsiveStatus = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String workingDir = System.getProperty("user.dir");
    @Getter
    @Setter
    private static Duration LONG_TIMEOUT = Duration.ofSeconds(30);
    @Getter
    private static Duration MILLI_TIMEOUT = Duration.ofMillis(400);
    @Getter
    @Setter
    private static Duration SHORT_TIMEOUT = Duration.ofSeconds(5);

    @Getter
    @Setter
    private static Duration MID_TIMEOUT = Duration.ofSeconds(15);

    @Getter
    @Setter
    private static Duration VERY_LONG_TIMEOUT = Duration.ofSeconds(90);

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    public static void openMultiBrowser(String browserName) {
        WebDriver tmpDriver = null;
        // init custom download folder
        new File(System.getProperty("user.dir") + "/downloadFile").mkdirs();
        System.setProperty("web.downloadPath", new File(System.getProperty("user.dir") + "/downloadFile").getPath());
        if (browserName.equalsIgnoreCase("hfirefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--headless");
            System.setProperty("webdriver.firefox.marionette", "true");
            System.setProperty("webdriver.firefox.logfile", workingDir + "\\FirefoxLog.txt");
            tmpDriver = new FirefoxDriver(firefoxOptions);

        } else if (browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--incognito");
            option.addArguments("--disable-extensions");
            option.addArguments("--disable-infobars");
            option.addArguments("--no-sandbox");
            option.addArguments("--disable-blink-features=AutomationControlled");
            String os = System.getProperty("os.name");
            if (os.toLowerCase().contains("linux")) {
                option.addArguments("--disable-dev-shm-usage");
                option.addArguments("--no-sandbox");
                option.addArguments("--disable-gpu");
                option.addArguments("--disable-features=NetworkService");
                option.addArguments("--dns-prefetch-disable");
                option.addArguments("--disable-features=VizDisplayCompositor");

            }
            // Set custom download folder
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("download.default_directory", System.getProperty("web.downloadPath"));
            option.setExperimentalOption("prefs", chromePrefs);
            tmpDriver = new ChromeDriver(option);

        } else if (browserName.equalsIgnoreCase("hchrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions option = new ChromeOptions();
            option.addArguments("headless");
            option.addArguments("--incognito");
            option.addArguments("--disable-extensions");
            option.addArguments("--disable-infobars");
            option.addArguments("window-size=1920,1080");
            option.addArguments("--disable-blink-features=AutomationControlled");
            String os = System.getProperty("os.name");
            if (os.toLowerCase().contains("linux")) {
                option.addArguments("--disable-dev-shm-usage");
                option.addArguments("--no-sandbox");
                option.addArguments("--disable-gpu");
                option.addArguments("--disable-features=NetworkService");
                option.addArguments("--dns-prefetch-disable");
                option.addArguments("--disable-features=VizDisplayCompositor");
            }
            // Set custom download folder
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("download.default_directory", System.getProperty("web.downloadPath"));
            option.setExperimentalOption("prefs", chromePrefs);
            tmpDriver = new ChromeDriver(option);
        } else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            System.setProperty("webdriver.firefox.marionette", "true");
            System.setProperty("webdriver.firefox.logfile", workingDir + "\\FirefoxLog.txt");
            tmpDriver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("safari")) {
            tmpDriver = new SafariDriver();
        }
        setDriver(tmpDriver);
        responsiveStatus.set(false);
        getDriver().manage().timeouts().implicitlyWait(getLONG_TIMEOUT());
        getDriver().manage().window().maximize();
    }

    public static void closeBrowserAndDriver(WebDriver driver) {
        try {
            getDriver().manage().deleteAllCookies();
            PageFactoryManager.get(WebApi.class).closeAllWindows();
            getDriver().quit();
            LogHelper.getInstance().info("-------------QUIT BROWSER SUCCESSFULLY -----------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LogHelper.getInstance().info(e.getMessage());
        }
    }

    public static void cleanAllBrowsers(WebDriver driver) {
        try {
            String driverName = Objects.requireNonNull(driver).toString().toLowerCase();
            String osName = System.getProperty("os.name").toLowerCase();
            LogHelper.getInstance().info("OS name= " + osName);
            String cmd = "";
            if (driverName.contains("chrome")) {
                if (osName.toLowerCase().contains("mac")) {
                    cmd = "killAll Google\\ Chrome";
                    executeCommand(cmd);
                    cmd = "killAll chromedriver";
                    executeCommand(cmd);
                } else if (osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI\"IMAGENAME eq chromedriver*\"";
                    executeCommand(cmd);
                }
            } else if (driverName.contains("safari")) {
                cmd = "killAll safaridriver";
                executeCommand(cmd);
                cmd = "killAll Safari";
                executeCommand(cmd);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "cannot kill browser");
            LogHelper.getInstance().info(e.getMessage());
        }

    }

    private static void executeCommand(String cmd) throws InterruptedException, IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
    }

}

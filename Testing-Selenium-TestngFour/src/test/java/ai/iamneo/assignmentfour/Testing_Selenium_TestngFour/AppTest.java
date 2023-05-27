package ai.iamneo.assignmentfour.Testing_Selenium_TestngFour;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AppTest {
	WebDriver driver;

	@BeforeSuite
	@Parameters("browser")
	public void setup(@Optional("chrome") String browserName) {
		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			System.setProperty("webdriver.chrome.port", "8080");
			driver = new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else {
			System.out.println("Invalid browser name");
		}

	}

	@BeforeTest
	public void openpage() {
		driver.get("https://www.ebay.com");

		WebElement searchBox = driver.findElement(By.name("_nkw"));
		searchBox.sendKeys("Apple watches");
		WebElement categoryDropdown = driver.findElement(By.id("gh-cat"));
		categoryDropdown.click();

		WebElement electronics = driver.findElement(By.xpath("//*[@id='gh-cat']/option[13]"));
		electronics.click();
		WebElement searchButton = driver.findElement(By.id("gh-btn"));
		searchButton.click();
	}

	@BeforeClass
	public void printProductResults() throws IndexOutOfBoundsException {
		System.out.println("Search Results:");
		int sno = 1;
		List<WebElement> productTitles = driver.findElements(By.cssSelector(".s-item__title"));
		for (int s = 1; s < productTitles.size(); s++) {
			WebElement productTitle = productTitles.get(s);
			System.out.println(productTitle.getText());
		}
		List<WebElement> nextPageButtonList = driver
				.findElements(By.cssSelector("[aria-label='Go to next search page']"));
		if (nextPageButtonList.isEmpty()) {
			return;
		}
		WebElement nextPageButton = nextPageButtonList.get(0);
		if (nextPageButton.isEnabled()) {
			nextPageButton.click();
			System.out.println("------ Page " + (++sno) + " ------");
			List<WebElement> productTitlesNextPage = driver.findElements(By.cssSelector(".s-item__title"));
			for (int s = 1; s < productTitlesNextPage.size(); s++) {
				WebElement productTitle = productTitlesNextPage.get(s);
				System.out.println(productTitle.getText());
			}
		}

	}

	@BeforeMethod
	public void printNthProduct() {
		driver.navigate().back();
		int n = 10;
		List<WebElement> productTitles = driver.findElements(By.cssSelector(".s-item__title"));
		if (n > 0 && n <= productTitles.size()) {
			WebElement nthProductTitle = productTitles.get(n);
			System.out.println();
			System.out.println("The " + n + "th product is: " + nthProductTitle.getText());
			System.out.println();
		} else {
			System.out.println("Invalid value of n");
		}
	}

	@Test
	public void printAllProductsOnFirstPage() {
		System.out.println("Products on first page only:");
		List<WebElement> productTitles = driver.findElements(By.cssSelector(".s-item__title"));
		for (int i = 1; i < productTitles.size(); i++) {
			WebElement productTitle = productTitles.get(i);
			System.out.println((i) + ". " + productTitle.getText());
		}
	}

	@AfterMethod
	public void printAllProductsWithScrollDown() throws InterruptedException {
		int productCount = 0;
		while (true) {
			List<WebElement> products = driver.findElements(By.cssSelector(".s-item__title"));
			for (WebElement product : products) {
				System.out.println(product.getText());
				productCount++;
				System.out.println();

			}
			if (!scrollDown()) {
				break;
			}
		}
		System.out.println("Total products: " + productCount);
	}

	@AfterTest
	private boolean scrollDown() throws InterruptedException {
		long initialHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
		Thread.sleep(5000);
		long newHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
		return initialHeight != newHeight;
	}

}

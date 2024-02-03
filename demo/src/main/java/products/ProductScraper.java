package products;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ProductScraper {
    public static void main(String[] args) {
        String chromeDriverPath = "demo/src/main/java/products/chromedriver.exe";

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        WebDriver driver = new ChromeDriver();

        try {
            driver.get("http://develop.dis.rs/dis/akcije/nedeljna-akcija");

            Thread.sleep(5000);

            WebElement itemsBox = driver.findElement(By.id("items-box"));

            String htmlContent = itemsBox.getAttribute("innerHTML");
            extractProductInfo(htmlContent);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void extractProductInfo(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);

        Elements productElements = document.select(".item-box");

        for (Element productElement : productElements) {
            String productName = productElement.select(".name").text();

            String oldPrice = productElement.select(".price-old .value").text();
            String showOldValue = oldPrice.substring(0,oldPrice.length()-2) + "," + oldPrice.substring(oldPrice.length()-2);
            String newPrice = productElement.select(".price .value").text();
            String showNewPrice = newPrice.substring(0,newPrice.length()-2) + "," + newPrice.substring(newPrice.length()-2);

            System.out.println("Product name: " + productName);
            System.out.println("Old price: " + showOldValue);
            System.out.println("New price: " + showNewPrice);
            System.out.println("------------------------------");
        }
    }
}

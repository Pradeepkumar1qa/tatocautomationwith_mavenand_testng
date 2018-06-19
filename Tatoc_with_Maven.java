package tatoc_automation_with_maven;

import java.util.ArrayList;

import org.apache.commons.lang3.math.Fraction;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Tatoc_with_Maven {
	WebDriver driver;
	ArrayList tabs;
	
	
	@Test
	public void oneTimeSetup() {
		System.setProperty("webdriver.chrome.driver", "C:\\software\\chromedriver\\chromedriver.exe");
		driver=new ChromeDriver();
		driver.get("http://10.0.1.86/tatoc");
		
	}
	@Test(dependsOnMethods={"oneTimeSetup"})
	public void first_page_click_on_basic_course(){
		driver.findElement(By.linkText("Basic Course")).click();
		
	} 
	
	@Test(dependsOnMethods={"first_page_click_on_basic_course"})
	public void second_page_click_on_greenbox() {
		driver.findElement(By.className("greenbox")).click();
	}
	
	@Test(dependsOnMethods={"second_page_click_on_greenbox"})
	public void repaint_box2_until_it_matches_box1() {
		String box_color=driver.switchTo().frame("main").findElement(By.id("answer")).getAttribute("class");
		String box_color2=driver.switchTo().frame("child").findElement(By.id("answer")).getAttribute("class");
		while(!box_color.equals(box_color2))
		{   driver.switchTo().parentFrame().findElement(By.linkText("Repaint Box 2")).click();
		    box_color2=driver.switchTo().frame("child").findElement(By.id("answer")).getAttribute("class");

		}
		
		driver.switchTo().parentFrame().findElement(By.linkText("Proceed")).click();
	}
	
	@Test(dependsOnMethods={"repaint_box2_until_it_matches_box1"},description="this function will do drag and drop testing")
	public void drag_and_drop(){
		WebElement from=driver.findElement(By.id("dragbox"));
		WebElement to=driver.findElement(By.id("dropbox"));
		Actions act=new Actions(driver);
		act.dragAndDrop(from, to).build().perform();
		driver.findElement(By.linkText("Proceed")).click();
		
	}
	
	@Test(dependsOnMethods={"drag_and_drop"},description="to open popup window")
	public void launch_popup_window_and_go_to_next_page() {
		driver.findElement(By.partialLinkText("Launch")).click();
		tabs = new ArrayList(driver.getWindowHandles());
        process_popup_and_returnto_main_page(1);
        driver.switchTo().window((String) tabs.get(0));
		driver.findElement(By.linkText("Proceed")).click();
		
	}
	
	@Test(dependsOnMethods={"launch_popup_window_and_go_to_next_page"})
	public void set_cookie(){
		driver.findElement(By.linkText("Generate Token")).click();
		String Token = driver.findElement(By.id("token")).getText();
		String value = Token.replace("Token: ", "");
		Cookie ck = new Cookie("Token", value);
		driver.manage().addCookie(ck);
		driver.findElement(By.linkText("Proceed")).click();

		
	}
	public void process_popup_and_returnto_main_page(int tab_number){
		   
			driver.switchTo().window((String) tabs.get(tab_number));
			driver.findElement(By.id("name")).sendKeys("pradeep");
	        driver.findElement(By.id("submit")).click();
		
		
	}
	
	
	
	@AfterClass
	public void teardown(){
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		
	}
	

}

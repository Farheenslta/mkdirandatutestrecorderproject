package mkdirandatutestrecorderpack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import atu.testrecorder.ATUTestRecorder;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Test1
{
	public static void main(String[] args) throws Exception
	{
		//create a customized result folder
		File fo=new File("1.5crore");
		fo.mkdir();
		//Create extent reports results file in our customized results folder
		ExtentReports er=new ExtentReports(fo.getAbsolutePath()+"\\results.html", false);//false is for appending results
		ExtentTest et=er.startTest("Website Title test");
		//Create Timestamp for dynamic video name
		SimpleDateFormat sf=new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
		Date dt=new Date();
		String fname=sf.format(dt);
		//Create Recording object with Path, video file name and audio (false/true)
		ATUTestRecorder rec=new ATUTestRecorder(fo.getAbsolutePath(),"vedioon"+fname, false);//false for no audio
		rec.start();
		
		//Project oriented code
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver=new ChromeDriver();
		//Maximize browser
		driver.manage().window().maximize();
		driver.get("https://www.google.com");
		WebDriverWait wait=new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q"))).sendKeys("farheensult", Keys.ENTER);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='All']")));
		Thread.sleep(5000);
		driver.findElement(By.name("q")).click();
		Thread.sleep(5000);
		driver.findElement(By.name("q")).sendKeys(Keys.chord(Keys.CONTROL, "a"),Keys.chord(Keys.CONTROL, "c"));
		driver.navigate().to("https://www.gmail.com");
		try
		{
			driver.findElement(By.xpath("//*[text()='Use another account']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@type='email']")));
		}
		catch(Exception ex)
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//*[contains(text(),'Sign in')])[2]"))).click();
		}
		ArrayList<String> al=new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(al.get(1));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("identifier"))).sendKeys(Keys.CONTROL, "v");
		driver.navigate().refresh();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("identifier")));
		String title=driver.getTitle();
		if(title.contains("Gmail"))
		{
			et.log(LogStatus.PASS, "Title Test Passed");
		}
		else
		{
			//Screenshot
			File src=driver.getScreenshotAs(OutputType.FILE);
			File dest=new File(fo.getAbsolutePath()+"\\"+fname+".png");
			FileHandler.copy(src, dest);
			et.log(LogStatus.FAIL, "Title test failed", et.addScreenCapture(fo.getAbsolutePath()+"\\"+fname+".png"));
		}
		//close site
		driver.close();
		
		//stop recording
		rec.stop();
		
		//save and extent report file
		er.endTest(et);
		er.flush();
		er.close();
	}
}

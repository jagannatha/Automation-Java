package moolya.filpkartdemo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.testng.IExecutionListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.internal.IResultListener;
import org.testng.xml.XmlSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.ReportConfigurator;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.KlovReporter;

import moolya.filpkartdemo.pages.W_BasePage;

public class ReporterUtils implements ITestListener,ISuiteListener,IResultListener,IExecutionListener,IReporter{

	public ExtentReports extent;
	public ExtentXReporter extentxReporter;
	public static ExtentTest test;
	public ExtentHtmlReporter htmlReporter;
	public ReportConfigurator rc;

	String dir = System.getProperty("user.dir");
	private KlovReporter klov;
	public static String filePath;

	public void onStart(ISuite arg0) {
		
		// initialize the HtmlReporter
		htmlReporter = new ExtentHtmlReporter(dir+"/reports/Extent.html");
		String host="";
		int port=0;
		String project="";
		String report="";
		String klovServerPort="";
		String url="";
		try {
			host = JavaUtils.getPropValue("host");
			port = Integer.parseInt(JavaUtils.getPropValue("port"));
			project = JavaUtils.getPropValue("project");
			report = JavaUtils.getPropValue("report");
			klovServerPort = JavaUtils.getPropValue("klovServerPort");
			url = "http://"+host+":"+klovServerPort;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		klov = new KlovReporter();
		klov.initMongoDbConnection(host, port);
		klov.setProjectName(project);
		klov.setReportName(report);
		klov.setKlovUrl("http://"+host+":"+klovServerPort);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter,klov);
		
	}

	public void onFinish(ISuite arg0) {

		extent.flush();
		//extent.close();
	}

	public void onStart(ITestContext result) {
	}

	public void onFinish(ITestContext result) {

		//extent.endTest(test);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("onTestFailedButWithinSuccessPercentage " + result.getName());
	}

	public void onTestStart(ITestResult result) {

		String[] clsParts = result.getInstanceName().split("\\.");
		String clsName = clsParts[(clsParts.length)-1];

		test = extent.createTest(clsName);
		test.log(Status.INFO, clsName+" initiated..!");
	}

	public void onTestSuccess(ITestResult result) {

		String[] clsParts = result.getInstanceName().split("\\.");
		String clsName = clsParts[(clsParts.length)-1];

		W_BasePage bp=new W_BasePage(null);
		/*Row ids=bp.readExcel("Execution sheet", clsName, 1);
		try {
			bp.writeexcel("Execution sheet", clsName, "Report", "PASS");
		} catch (Exception e) {

		}
		 */
		test.pass(clsName+" PASS");
	}

	public void onTestFailure(ITestResult result) 
	{
		String[] clsParts = result.getInstanceName().split("\\.");
		String clsName = clsParts[(clsParts.length)-1];

		test.log(Status.FAIL, clsName +" failed..!");
		MediaEntityModelProvider mediaModel = null;
		try {
			mediaModel = MediaEntityBuilder.createScreenCaptureFromPath(filePath).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.fail(result.getThrowable(),mediaModel);
	}

	public void onTestSkipped(ITestResult result) {

		String[] clsParts = result.getInstanceName().split("\\.");
		String clsName = clsParts[(clsParts.length)-1];

		W_BasePage bp=new W_BasePage(null);
		test.skip(clsName+"Skipped"+"\n"+result.getThrowable());
	}


	@SuppressWarnings("unused")
	private String getTestMethodName(ITestResult result)
	{
		return result.getMethod().getConstructorOrMethod().getName();

	}

	public void onConfigurationFailure(ITestResult result) {

		//		String[] clsParts = result.getInstanceName().split("\\.");
		//		String clsName = clsParts[(clsParts.length)-1];
		//		test.log(LogStatus.FAIL, "Failed "+clsName+" because of configuration failure..!");
	}

	public void onConfigurationSkip(ITestResult result) {


	}

	public void onConfigurationSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		//		System.out.println("onConfigurationSuccess");
	}

	public void onExecutionFinish() {

		System.out.println("Test Execution Has Been Completed..!");
	}

	public void onExecutionStart() {

		
		System.out.println("Test Execution Has Begun..!");
	}

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		// TODO Auto-generated method stub
		
	}

}

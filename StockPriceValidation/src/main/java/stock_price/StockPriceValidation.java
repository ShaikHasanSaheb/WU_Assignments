package stock_price;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class StockPriceValidation {
	public static void main(String[] args) throws IOException {

//		Step1: Read data from XLS file and store in HashMap1
		Map<String, Double> xlsData = readDataFromXLS("src/main/resources/output.xlsx");

//		Step2: Read data from website using Selenium  WebDriver and store in HashMap2
		Map<String, Double> websiteData = readDataFromWebsite();

//		Step3	: Compare the values stored in the two HashMap
		compareData(xlsData, websiteData);

	}

	private static Map<String, Double> readDataFromXLS(String filePath) throws IOException {

		Map<String, Double> data = new HashMap<>();
		FileInputStream fis = new FileInputStream(filePath);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0); // I am Assiming data is in the First Sheet

//	    iterates through each row (Row) in the sheet.
//		gets the cells in the first and second columns (assuming the company name is in the first column and the price is in the second column).
		for (Row row : sheet) {
			Cell companyNameCell = row.getCell(0);
			Cell priceCell = row.getCell(1);

//	        retrieves the string value of the company name cell and stores it in companyName.
//	        initializes a variable price to store the price value.
			if (companyNameCell != null && priceCell != null) {
				String companyName = companyNameCell.getStringCellValue();
				double price;
				if (priceCell.getCellType() == CellType.NUMERIC) {
					price = priceCell.getNumericCellValue();
				} else if (priceCell.getCellType() == CellType.STRING) {
					try {
						price = Double.parseDouble(priceCell.getStringCellValue());
					} catch (NumberFormatException e) {
						// Handle invalid numeric values in the string cell
						price = 0.0; // Or set to some default value
					}
				} else {
					// Handle other cell types (formula, blank, etc.) if needed
					price = 0.0; // Or set to some default value
				}
				data.put(companyName, price);
			}
		}

		workbook.close();
		fis.close();

		return data;
	}

	private static Map<String, Double> readDataFromWebsite() {
		System.setProperty("webdriver.chrome.driver", "c:\\Drivers\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://money.rediff.com/losers/bse/daily/groupall");

		Map<String, Double> data = new HashMap<>();
		for (WebElement row : driver.findElements(By.cssSelector(".dataTable tbody tr"))) {
			String stockName = row.findElement(By.cssSelector("td:nth-child(1)")).getText();
			Double stockPrice = Double.parseDouble(row.findElement(By.cssSelector("td:nth-child(4)")).getText());
			data.put(stockName, stockPrice);
		}

		driver.quit();
		return data;

	}

	private static void compareData(Map<String, Double> xlsData, Map<String, Double> websiteData) {

		for (Map.Entry<String, Double> entry : xlsData.entrySet()) {
			String stockName = entry.getKey();
			double xlsPrice = entry.getValue();
//			If the stock name exists in the map, its corresponding price is returned. 
//			If the stock name does not exist in the map, the default value -1.0 is returned.
			double websitePrice = websiteData.getOrDefault(stockName, -1.0); // default values if key not dound

			if (websitePrice == -1.0) {
				System.out.println("Stock price for " + stockName + " not found on website.");
			} else if (xlsPrice != websitePrice) {
				System.out.println("Discrepancy found for " + stockName + ": XLS price = " + xlsPrice
						+ ", Website price = " + websitePrice);
			}
		}
	}
}

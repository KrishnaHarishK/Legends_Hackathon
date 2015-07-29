/**
 * 
 */
package src.com.LegendsHackathon;

import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Workbook; 
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator; 
import java.util.Properties;


/**
 * @author v962494
 *
 */
public class CaseTrackerLoad {
	private static final String FILE_PATH = "C:/Users/Administrator/Documents/GitHub/Legends_Hackathon/input/Case Tracker Business Rules_v2.xls";

	/**
	 * @param args
	 */
	public static Properties props = null;
	public static void main(String[] args) {
		
		props = LoadPropertiesFile();
		//System.out.println("dbdriver: "+props.getProperty("dbdriver"));
		//System.out.println("dbhost: "+props.getProperty("dbhost"));
		//System.out.println("dbusername: "+props.getProperty("dbusername"));
		//System.out.println("dbpassword: "+props.getProperty("dbpassword"));
		System.out.println("A Project part of Legend's Hackathon");
		System.out.println("Starting to load Case Tracker Historical Data");
		loadCaseTrackerHistoricalData(props); 
		System.out.println("Completed loading Case Tracker Historical Data");
	}

	private static Properties LoadPropertiesFile() {
		Properties props = new Properties();
		String propFile = System.getProperty("CaseTrackerPropFile");
		File f = new File(propFile);
        if (!f.exists())
        {
            System.err.println("propFile "+propFile+" does not exist");
            System.exit(1);
        }

        FileInputStream fi = null;
        try
        {
            fi = new FileInputStream(f);
        }
        catch (Exception e)
        {
            System.err.println("Couldn't open file:" + propFile+":"+e);
            System.exit(1);
        }

        try
        {
            props.load(new BufferedInputStream(fi));
        }
        catch (Exception e)
        {
            System.err.println("Couldn't load properties from file:" + propFile+":"+e);
            System.exit(1);
        }
		return props;

		
	}
	
    private static DbConnect getDbConnect(String driver,String host,String username,String password) throws Exception {
        try{
         return  new DbConnect(driver, host, username, password);
        }catch ( Exception exc )
      {
                 System.out.println("Unable to create DB connection. "+exc );
         throw new Exception( "Unable to create DB connection. " +
            "driver: " + driver + "; " +
            "host: " + host + "; " +
            "username: " + username + "; " +
         // "password: " + password + "; " +
            "message: " + exc.getMessage() );
      }
}	

	private static void loadCaseTrackerHistoricalData(Properties props) {
		// TODO Auto-generated method stub
		
		DbConnect dbConn = null;
		String dbdriver = props.getProperty("dbdriver");
		String dbusername = props.getProperty("dbusername");
		String dbpassword = props.getProperty("dbpassword");
		String dbhost = props.getProperty("dbhost");
		
		try {
			dbConn = getDbConnect(dbdriver,dbhost,dbusername,dbpassword);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileInputStream fis = null;
		boolean foundDATASheet = false;
		try { 
            fis = new FileInputStream(FILE_PATH);

            // Using XSSF for xlsx format, for xls use HSSF 
            Workbook workbook = new HSSFWorkbook(fis);
            int numberOfSheets = workbook.getNumberOfSheets();

            //looping over each workbook sheet 
            for (int i = 0; i < numberOfSheets; i++) { 
            	Sheet sheet = workbook.getSheetAt(i);
            	if(!sheet.getSheetName().equalsIgnoreCase("DATA")) {
            		continue;
            	}
            	//Found DATA Sheet
            	foundDATASheet = true;
            	Iterator<Row> rowIterator = sheet.iterator();

            	//iterating over each row 
            	int rowCount = 0;
                while (rowIterator.hasNext()) {
                	Row row = (Row) rowIterator.next();
                	if(row.getRowNum() == 0 || row.getRowNum() > 100){
                	//if(row.getRowNum() == 0){
                		continue; // Ignore the Header Row
                	}
                	rowCount++;
                	//System.out.println("Printing Row: " + row.getRowNum());
                	//System.out.println("----------------------------------------");
                	
                	CaseTrackerEntry caseTrackerEntry = new CaseTrackerEntry();
                	Iterator<Cell> cellIterator = row.cellIterator();
                	while(cellIterator.hasNext()){
                		Cell cell = (Cell) cellIterator.next();
                		switch (cell.getColumnIndex()) 
                        {
                            case 0: // WEEK_NUMBER
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setWeekNumber(cell.getNumericCellValue());
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setWeekNumber(Double.parseDouble(cell.getStringCellValue()));
                                }
                                break;
                            case 1: // YEAR_NUMBER
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setYearNumber(cell.getNumericCellValue());
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setYearNumber(Double.parseDouble(cell.getStringCellValue()));
                                }
                                break;
                            case 2: // MONTH_NUMBER
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setMonthNumber(cell.getNumericCellValue());
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setMonthNumber(Double.parseDouble(cell.getStringCellValue()));
                                }
                                break; 
                            case 3: // CASE_NUMBER
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setCaseNumber(cell.getNumericCellValue());
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setCaseNumber(Double.parseDouble(cell.getStringCellValue()));
                                }
                                break;  
                            case 4: // PRODUCT_NAME
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setProductName(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setProductName(caseTrackerEntry.getProductName().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setProductName(cell.getStringCellValue());
                                	caseTrackerEntry.setProductName(caseTrackerEntry.getProductName().replaceAll("'", "''"));
                                }
                                
                                break;
                            case 5: // PRODUCT_SUB_CATEGORY
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setProductSubCategory(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setProductSubCategory(caseTrackerEntry.getProductSubCategory().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setProductSubCategory(cell.getStringCellValue());
                                	caseTrackerEntry.setProductSubCategory(caseTrackerEntry.getProductSubCategory().replaceAll("'", "''"));
                                }
                                
                                break;
                            case 6: // CASE_NAME
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setCaseName(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setCaseName(caseTrackerEntry.getCaseName().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setCaseName(cell.getStringCellValue());
                                	caseTrackerEntry.setCaseName(caseTrackerEntry.getCaseName().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 7: // CASE_DATE
                            case 14: // TARGET_DATE
                            case 15: // CLOSED_DATE
                            	String tmpCaseDate = "";
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	tmpCaseDate = Double.toString(cell.getNumericCellValue());
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	tmpCaseDate = cell.getStringCellValue();
                                }
                                
                                if(tmpCaseDate.length() < 8) { 
                                	//System.out.println("Invalid Date format: "  + tmpCaseDate);
                                	//System.out.println("Printing Row: " + row.getRowNum());
                                	continue;
                                }
                                
                                int monthIndex = tmpCaseDate.indexOf("/");
                                int dayIndex = tmpCaseDate.indexOf("/", monthIndex+1);
                                int yearIndex = tmpCaseDate.indexOf(" ", dayIndex+1);
                                if(monthIndex == -1 || dayIndex == -1){
                                	continue;
                                }
                                //System.out.println("tmpCaseDate: " + tmpCaseDate + ", monthIndex: " + monthIndex + ", dayIndex: " + dayIndex + ", YearIndex: " + yearIndex);
                                
                                String tmpMonth = tmpCaseDate.substring(0, monthIndex);                                
                                String tmpDay = tmpCaseDate.substring(monthIndex+1, dayIndex);
                                String tmpYear = "";
                                if(yearIndex == -1){
                                	tmpYear = tmpCaseDate.substring(dayIndex+1);
                                } else {
                                	tmpYear = tmpCaseDate.substring(dayIndex+1, yearIndex);
                                }
                                
                                
                                
								String caseDate = null;
								if(tmpYear.length() == 2){
									caseDate = tmpMonth + "/" + tmpDay + "/20" + tmpYear;
								}else{
									caseDate = tmpMonth + "/" + tmpDay + "/" + tmpYear;
								}
								
								if(tmpYear.contains("/")) // Tried to parse it enough; still errors. Just insert the current Date
								{
									DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
									Calendar cal = Calendar.getInstance();
									caseDate = dateFormat.format(cal.getTime()); //08/06/2014
								}
								if(cell.getColumnIndex() == 7){
									caseTrackerEntry.setCaseDate(caseDate);
								}else if (cell.getColumnIndex() == 14){
									caseTrackerEntry.setTargetDate(caseDate);
								}else if (cell.getColumnIndex() == 15){
									caseTrackerEntry.setClosedtDate(caseDate);
								}
								
                                
                                //System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
                                
                                break;
                            case 8: // TICKET_CREATED_BY
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setTicketCreatedBy(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setTicketCreatedBy(caseTrackerEntry.getTicketCreatedBy().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setTicketCreatedBy(cell.getStringCellValue());
                                	caseTrackerEntry.setTicketCreatedBy(caseTrackerEntry.getTicketCreatedBy().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 9: // CUSTOMER_IMPACT
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setCustomerImpact(Double.toString(cell.getNumericCellValue()).toUpperCase());
                                	caseTrackerEntry.setCustomerImpact(caseTrackerEntry.getCustomerImpact().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setCustomerImpact(cell.getStringCellValue().toUpperCase());
                                	caseTrackerEntry.setCustomerImpact(caseTrackerEntry.getCustomerImpact().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 10: // RESEARCH_METHOD
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setResearchMethod(Double.toString(cell.getNumericCellValue()).toUpperCase());
                                	caseTrackerEntry.setResearchMethod(caseTrackerEntry.getResearchMethod().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setResearchMethod(cell.getStringCellValue().toUpperCase());
                                	caseTrackerEntry.setResearchMethod(caseTrackerEntry.getResearchMethod().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 11: // CASE_DESCRIPTION
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setCaseDescription(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setCaseDescription(caseTrackerEntry.getCaseDescription().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setCaseDescription(cell.getStringCellValue());
                                	caseTrackerEntry.setCaseDescription(caseTrackerEntry.getCaseDescription().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 12: // ACTION_NOTES
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setActionNotes(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setActionNotes(caseTrackerEntry.getActionNotes().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setActionNotes(cell.getStringCellValue());
                                	caseTrackerEntry.setActionNotes(caseTrackerEntry.getActionNotes().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 13: // STATUS
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setStatus(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setStatus(caseTrackerEntry.getStatus().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setStatus(cell.getStringCellValue());
                                	caseTrackerEntry.setStatus(caseTrackerEntry.getStatus().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_BLANK){
                                	caseTrackerEntry.setStatus("Open");
                                }
                                
                                break;  
                            case 16: // PARTNER
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setPartner(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setPartner(caseTrackerEntry.getPartner().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setPartner(cell.getStringCellValue());
                                	caseTrackerEntry.setPartner(caseTrackerEntry.getPartner().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 17: // ROOT_CAUSE
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setRootCause(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setRootCause(caseTrackerEntry.getRootCause().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setRootCause(cell.getStringCellValue());
                                	caseTrackerEntry.setRootCause(caseTrackerEntry.getRootCause().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 18: // APPLICATION_NAME
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setApplicationName(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setApplicationName(caseTrackerEntry.getApplicationName().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setApplicationName(cell.getStringCellValue());
                                	caseTrackerEntry.setApplicationName(caseTrackerEntry.getApplicationName().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_BLANK){
                                	caseTrackerEntry.setApplicationName("N/A");
                                }
                                
                                break;  
                            case 19: // FUTURE_STATE
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setFutureState(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setFutureState(caseTrackerEntry.getFutureState().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setFutureState(cell.getStringCellValue());
                                	caseTrackerEntry.setFutureState(caseTrackerEntry.getFutureState().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 20: // NEW_RECORD_FLAG
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setNewRecordFlag(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setNewRecordFlag(caseTrackerEntry.getNewRecordFlag().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setNewRecordFlag(cell.getStringCellValue());
                                	caseTrackerEntry.setNewRecordFlag(caseTrackerEntry.getNewRecordFlag().replaceAll("'", "''"));
                                }
                                
                                break;  
                            case 21: // RESOLUTION
                                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                	caseTrackerEntry.setResolution(Double.toString(cell.getNumericCellValue()));
                                	caseTrackerEntry.setResolution(caseTrackerEntry.getResolution().replaceAll("'", "''"));
                                } else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                                	caseTrackerEntry.setResolution(cell.getStringCellValue());
                                	caseTrackerEntry.setResolution(caseTrackerEntry.getResolution().replaceAll("'", "''"));
                                }
                                
                                break;  

                        }
                		
                	}
                	System.out.println(caseTrackerEntry.toString());
                	//String updateString = buildInsertQuery(caseTrackerEntry);
                	//dbConn.setUpdateString(updateString);
                	//try {
					//	dbConn.execUpdateQuery();
					//} catch (Exception e) {
					//	// TODO Auto-generated catch block
					//	System.out.println("Failed to insert Case Number: " + caseTrackerEntry.getCaseNumber() + ", Product Name: " + 
					//						caseTrackerEntry.getProductName() + ", Case Date: " + caseTrackerEntry.getCaseDate());
					//	e.printStackTrace();
					//}
                }
                System.out.println("Row Count: " + rowCount);
            	
            	
            }
            if(!foundDATASheet){
            	System.out.println("Couldn't find DATA Sheet in the spreadsheet: " + FILE_PATH);
            }
		} 
		catch (FileNotFoundException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 


		
	}

	private static String buildInsertQuery(CaseTrackerEntry caseTrackerEntry) {
		// TODO Auto-generated method stub
		String insertString = "";
		
		if(caseTrackerEntry.getCaseDate().equals(null) || caseTrackerEntry.getCaseDate().equals("")){
			insertString =    "INSERT INTO NPI_CASE_TRACKER "
					+ "(WEEK_NUMBER, "
					+ "YEAR_NUMBER, "
					+ "MONTH_NUMBER, "
					+ "CASE_NUMBER, "
					+ "PRODUCT_NAME, "
					+ "PRODUCT_SUB_CATEGORY, "
					+ "CASE_NAME, "
					//+ "CASE_DATE, "
					+ "TICKET_CREATED_BY, "
					+ "CUSTOMER_IMPACT, "
					+ "RESEARCH_METHOD, "
					+ "CASE_DESCRIPTION, "
					+ "ACTION_NOTES, "
					+ "STATUS, "
					//+ "TARGET_DATE, "
					//+ "CLOSED_DATE, "
					+ "PARTNER, "
					+ "ROOT_CAUSE, "
					+ "APPLICATION_NAME, "
					+ "FUTURE_STATE, "
					+ "NEW_RECORD_FLAG, "
					+ "RESOLUTION) "
					+ "VALUES ("
					+ caseTrackerEntry.getWeekNumber() + ", "
					+ caseTrackerEntry.getYearNumber() + ", "
					+ caseTrackerEntry.getMonthNumber() + ", "
					+ caseTrackerEntry.getCaseNumber() + ", "
					+ "'" + caseTrackerEntry.getProductName() + "', "
					+ "'" + caseTrackerEntry.getProductSubCategory() + "', "
					+ "'" + caseTrackerEntry.getCaseName() + "', "
					//+ "to_date('" + caseTrackerEntry.getCaseDate() + "', 'mm/dd/yyyy')" + ", "
					+ "'" + caseTrackerEntry.getTicketCreatedBy() + "', "
					+ "'" + caseTrackerEntry.getCustomerImpact() + "', "
					+ "'" + caseTrackerEntry.getResearchMethod() + "', "
					+ "'" + caseTrackerEntry.getCaseDescription() + "', "
					+ "'" + caseTrackerEntry.getActionNotes() + "', "
					+ "'" + caseTrackerEntry.getStatus() + "', "
					//+ "to_date('" + caseTrackerEntry.getTargetDate() + "', 'mm/dd/yyyy')" + ", "
					//+ "to_date('" + caseTrackerEntry.getClosedtDate() + "', 'mm/dd/yyyy')" + ", "
					+ "'" + caseTrackerEntry.getPartner() + "', "
					+ "'" + caseTrackerEntry.getRootCause() + "', "
					+ "'" + caseTrackerEntry.getApplicationName() + "', "
					+ "'" + caseTrackerEntry.getFutureState() + "', "						
					+ "'" + caseTrackerEntry.getNewRecordFlag() + "', "
					+ "'" + caseTrackerEntry.getResolution() + "')";			
		}
		else
		{
			insertString =    "INSERT INTO NPI_CASE_TRACKER "
						+ "(WEEK_NUMBER, "
						+ "YEAR_NUMBER, "
						+ "MONTH_NUMBER, "
						+ "CASE_NUMBER, "
						+ "PRODUCT_NAME, "
						+ "PRODUCT_SUB_CATEGORY, "
						+ "CASE_NAME, "
						+ "CASE_DATE, "
						+ "TICKET_CREATED_BY, "
						+ "CUSTOMER_IMPACT, "
						+ "RESEARCH_METHOD, "
						+ "CASE_DESCRIPTION, "
						+ "ACTION_NOTES, "
						+ "STATUS, "
						//+ "TARGET_DATE, "
						//+ "CLOSED_DATE, "
						+ "PARTNER, "
						+ "ROOT_CAUSE, "
						+ "APPLICATION_NAME, "
						+ "FUTURE_STATE, "
						+ "NEW_RECORD_FLAG, "
						+ "RESOLUTION) "
						+ "VALUES ("
						+ caseTrackerEntry.getWeekNumber() + ", "
						+ caseTrackerEntry.getYearNumber() + ", "
						+ caseTrackerEntry.getMonthNumber() + ", "
						+ caseTrackerEntry.getCaseNumber() + ", "
						+ "'" + caseTrackerEntry.getProductName() + "', "
						+ "'" + caseTrackerEntry.getProductSubCategory() + "', "
						+ "'" + caseTrackerEntry.getCaseName() + "', "
						+ "to_date('" + caseTrackerEntry.getCaseDate() + "', 'mm/dd/yyyy')" + ", "
						+ "'" + caseTrackerEntry.getTicketCreatedBy() + "', "
						+ "'" + caseTrackerEntry.getCustomerImpact() + "', "
						+ "'" + caseTrackerEntry.getResearchMethod() + "', "
						+ "'" + caseTrackerEntry.getCaseDescription() + "', "
						+ "'" + caseTrackerEntry.getActionNotes() + "', "
						+ "'" + caseTrackerEntry.getStatus() + "', "
						//+ "to_date('" + caseTrackerEntry.getTargetDate() + "', 'mm/dd/yyyy')" + ", "
						//+ "to_date('" + caseTrackerEntry.getClosedtDate() + "', 'mm/dd/yyyy')" + ", "
						+ "'" + caseTrackerEntry.getPartner() + "', "
						+ "'" + caseTrackerEntry.getRootCause() + "', "
						+ "'" + caseTrackerEntry.getApplicationName() + "', "
						+ "'" + caseTrackerEntry.getFutureState() + "', "						
						+ "'" + caseTrackerEntry.getNewRecordFlag() + "', "
						+ "'" + caseTrackerEntry.getResolution() + "')";
		}
		//System.out.println("SQL Query: " + insertString);
		return insertString;
	}

}

package caseTrackerLoad;


public class CaseTrackerEntry {
	public double getWeekNumber() {
		return weekNumber;
	}
	public String toString() {
		return "CaseTrackerEntry [weekNumber=" + weekNumber + ", yearNumber="
				+ yearNumber + ", monthNumber=" + monthNumber + ", caseNumber="
				+ caseNumber + ", productName=" + productName
				+ ", productSubCategory=" + productSubCategory + ", caseName="
				+ caseName + ", caseDate=" + caseDate + ", ticketCreatedBy="
				+ ticketCreatedBy + ", customerImpact=" + customerImpact
				+ ", researchMethod=" + researchMethod + ", caseDescription="
				+ caseDescription + ", actionNotes=" + actionNotes
				+ ", status=" + status + ", targetDate=" + targetDate
				+ ", closedtDate=" + closedtDate + ", partner=" + partner
				+ ", rootCause=" + rootCause + ", applicationName="
				+ applicationName + ", futureState=" + futureState
				+ ", newRecordFlag=" + newRecordFlag + ", resolution="
				+ resolution + "]";
	}
	public void setWeekNumber(double weekNumber) {
		this.weekNumber = weekNumber;
	}
	public double getYearNumber() {
		return yearNumber;
	}
	public void setYearNumber(double yearNumber) {
		this.yearNumber = yearNumber;
	}
	public double getMonthNumber() {
		return monthNumber;
	}
	public void setMonthNumber(double monthNumber) {
		this.monthNumber = monthNumber;
	}
	public double getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(double caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductSubCategory() {
		return productSubCategory;
	}
	public void setProductSubCategory(String productSubCategory) {
		this.productSubCategory = productSubCategory;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public String getCaseDate() {
		return caseDate;
	}
	public void setCaseDate(String caseDate) {
		this.caseDate = caseDate;
	}
	public String getTicketCreatedBy() {
		return ticketCreatedBy;
	}
	public void setTicketCreatedBy(String ticketCreatedBy) {
		this.ticketCreatedBy = ticketCreatedBy;
	}
	public String getCustomerImpact() {
		return customerImpact;
	}
	public void setCustomerImpact(String customerImpact) {
		this.customerImpact = customerImpact;
	}
	public String getResearchMethod() {
		return researchMethod;
	}
	public void setResearchMethod(String researchMethod) {
		this.researchMethod = researchMethod;
	}
	public String getCaseDescription() {
		return caseDescription;
	}
	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}
	public String getActionNotes() {
		return actionNotes;
	}
	public void setActionNotes(String actionNotes) {
		this.actionNotes = actionNotes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getClosedtDate() {
		return closedtDate;
	}
	public void setClosedtDate(String closedtDate) {
		this.closedtDate = closedtDate;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getRootCause() {
		return rootCause;
	}
	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getFutureState() {
		return futureState;
	}
	public void setFutureState(String futureState) {
		this.futureState = futureState;
	}
	public String getNewRecordFlag() {
		return newRecordFlag;
	}
	public void setNewRecordFlag(String newRecordFlag) {
		this.newRecordFlag = newRecordFlag;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	private double    weekNumber = 0;
	private double    yearNumber = 0;
	private double    monthNumber = 0;
	private double    caseNumber = 0;
	private String productName = "";
	private String productSubCategory = "";
	private String caseName = "";
	private String   caseDate = "";
	private String ticketCreatedBy = "";
	private String customerImpact = "";
	private String researchMethod = "";
	private String caseDescription = "";
	private String actionNotes = "";
	private String status = "";
	private String   targetDate = "";
	private String   closedtDate = "";
	private String partner = "";
	private String rootCause = "";
	private String applicationName = "";
	private String futureState = "";
	private String newRecordFlag = "";
	private String resolution = "";
}

package serviceImplementation;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * this class initializes users.
 * @author Leila
 *
 */
@XmlRootElement
public class Product {

	private String custID, productID, name, brand, itemcondition, status;
	private int startingPrice;
	private String startDate, sellDate, winnerID, description;
	private int sellPrice;
	public Product(String custID, String productID, String name, String brand, String itemcondition, String status,
			int startingPrice, String startDate, String sellDate, String winnerID, String description, int sellPrice) {
		super();
		this.custID = custID;
		this.productID = productID;
		this.name = name;
		this.brand = brand;
		this.itemcondition = itemcondition;
		this.status = status;
		this.startingPrice = startingPrice;
		this.startDate = startDate;
		this.sellDate = sellDate;
		this.winnerID = winnerID;
		this.description = description;
		this.sellPrice = sellPrice;
	}
	
	public Product() {
		super();
	}
	
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getItemcondition() {
		return itemcondition;
	}
	public void setItemcondition(String itemcondition) {
		this.itemcondition = itemcondition;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStartingPrice() {
		return startingPrice;
	}
	public void setStartingPrice(int startingPrice) {
		this.startingPrice = startingPrice;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getSellDate() {
		return sellDate;
	}
	public void setSellDate(String sellDate) {
		this.sellDate = sellDate;
	}
	public String getWinnerID() {
		return winnerID;
	}
	public void setWinnerID(String winnerID) {
		this.winnerID = winnerID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}
	

	

}

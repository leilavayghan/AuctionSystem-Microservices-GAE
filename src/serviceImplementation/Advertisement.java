package serviceImplementation;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.appengine.api.utils.SystemProperty;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * this class is responsible for registering new products.
 * @author Leila
 *
 */
public class Advertisement{

	public String currentTimestamp(){
		Date date = new Date();
        return (new Timestamp(date.getTime()).toString());
	}
	
	/**
	 * gets the greatest customer ID from the database and adds 1 to it and makes a new customer ID.
	 * @param connect
	 * @return
	 * @throws Exception
	 */
	public String createID(Connection connect) throws Exception{

		int counter = 0;
		PreparedStatement statement = connect.prepareStatement("select MAX(productID) from products");
		ResultSet result = statement.executeQuery();

		while(result.next()){

			counter = result.getInt(1)+1;

		}
		return Integer.toString(counter);
	}

	public Connection connect() throws Exception{
		String url;
		Connection connect;
		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
			// Load the class that provides the new "jdbc:google:mysql://" prefix.
			Class.forName("com.mysql.jdbc.GoogleDriver");
			url = "jdbc:google:mysql://advertisementservice-149821:advertisementservice/advertisementservice?user=myuser&password=leila";
			connect = DriverManager.getConnection(url);
		} else {
			// Local MySQL instance to use during development.
			Class.forName("com.mysql.jdbc.Driver");
			url = "jdbc:mysql://127.0.0.1:3306/advertisementservice?user=root";
			connect = DriverManager.getConnection(url);
		}
		return connect;
	}
	
	public static Document convertStringToDocument(String xmlSource) 
			throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xmlSource)));
	}
	
	/**
	 * adds a new product to database
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public String addProduct(Product product) throws Exception{

		Connection connection = connect();
		//create the product ID
		String productID = createID(connection);

		PreparedStatement statement = connection.prepareStatement("INSERT INTO `products` (`custID`, `productID`, `name`, `brand`, `itemcondition`, "
				+ "`startingPrice`, `description`, `status`, `startDate`, `sellDate`, `winnerID`, `sellPrice`)"
				+ "VALUES ('"+product.getCustID()+"','"+productID+"','"+product.getName()+"','"+product.getBrand()+"','"+product.getItemcondition()+
				"','"+product.getStartingPrice()+"','"+product.getDescription()+"','"+"available"+"','"+currentTimestamp()+
				"','1990-12-11 12:16:43.415000','"+product.getWinnerID()+"','"+product.getSellPrice()+"')");

		statement.executeUpdate();

		return "your product is up for sale now :) ";
	}


	/**
	 * using a product name, returns information about a product.
	 * @param productname
	 * @return
	 * @throws Exception
	 */
	public Product getProduct(String productID) throws Exception{

		Connection connection = connect();

		//select from DB
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `products` WHERE `productID` = '"+productID+"'");
		ResultSet result = statement.executeQuery();

		Product myproduct = null;

		while(result.next()){

			myproduct = new Product(result.getString(1),result.getString(2),result.getString(3),result.getString(4),
					result.getString(5),result.getString(8),result.getInt(6),result.getString(9),result.getString(10),result.getString(11),
					result.getString(7),result.getInt(12));

		}

		return myproduct;
	}

	/**
	 * gets the list of all products from database.
	 * @return
	 * @throws Exception
	 */
	public List<Product> getAllProducts() throws Exception{

		Connection connection = connect();

		//select from DB
		PreparedStatement statement = connection.prepareStatement("select * from products");
		ResultSet result = statement.executeQuery();

		List<Product> productlist = new ArrayList<>();

		while(result.next()){

			Product myproduct = new Product(result.getString(1),result.getString(2),result.getString(3),result.getString(4),
					result.getString(5),result.getString(8),result.getInt(6),result.getString(9),result.getString(10),result.getString(11),
					result.getString(7),result.getInt(12));
			productlist.add(myproduct);

		}

		return productlist;
	}

	

	public void putForAuction(String productID) throws Exception{

		
		Connection connection = connect();
			
		PreparedStatement statement = connection
				.prepareStatement("update products set status = 'up', startDate = '"+currentTimestamp()+"' where productID = " + productID);
		statement.executeUpdate();
		

	}

	public String updateProducts(Product product) throws Exception{

		Connection connection = connect();
					
		PreparedStatement statement = connection
				.prepareStatement("update products set sellDate = '"+currentTimestamp()+"' , winnerID = '"+product.getWinnerID()+
						"', sellPrice = "+product.getSellPrice()+", status = 'sold' where productID = " + product.getProductID());
		statement.executeUpdate();
		
		notifyWinner(product);

		return "updated!";
	}


	private void notifyWinner(Product product) throws Exception{
		
		//get winner's email from registration service.
		
		String first = null;
		String last = null;
		String email = null;
		
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(UriBuilder.fromUri("http://regauthservice.appspot.com/rest/RegAuth/register/info/"+product.getWinnerID()).build());

		Document doc = convertStringToDocument(service.accept(MediaType.APPLICATION_XML).get(String.class));
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("user");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				first = eElement.getElementsByTagName("firstname").item(0).getTextContent();
				last =  eElement.getElementsByTagName("lastname").item(0).getTextContent();
				email = eElement.getElementsByTagName("email").item(0).getTextContent();

			}
		}
				
		WebResource service2 = client.resource(UriBuilder.fromUri("http://send-email-152216.appspot.com/?email="+email+"&first="+first+"&last="+last+"&price="+product.getSellPrice()).build());
		service2.get(String.class);
		
	}

	public List<Product> getyourProduct(String custID) throws Exception {
		
		Connection connection = connect();

		//select from DB
		PreparedStatement statement = connection.prepareStatement("select * from products where custID =" + custID);
		ResultSet result = statement.executeQuery();

		List<Product> productlist = new ArrayList<>();

		while(result.next()){

			Product myproduct = new Product(result.getString(1),result.getString(2),result.getString(3),result.getString(4),
					result.getString(5),result.getString(8),result.getInt(6),result.getString(9),result.getString(10),result.getString(11),
					result.getString(7),result.getInt(12));
			productlist.add(myproduct);

		}

		return productlist;
	}
	
	public List<Product> endedProducts() throws Exception {
		
		Connection connection = connect();

		//select from DB
		PreparedStatement statement = connection.prepareStatement("select *,TIMESTAMPDIFF(MINUTE,startDate,NOW()) "
				+ "as diff  from products where status = 'up' and TIMESTAMPDIFF(MINUTE,startDate,NOW()) > 3;");
		ResultSet result = statement.executeQuery();

		List<Product> productlist = new ArrayList<>();

		while(result.next()){

			Product myproduct = new Product(result.getString(1),result.getString(2),result.getString(3),result.getString(4),
					result.getString(5),result.getString(8),result.getInt(6),result.getString(9),result.getString(10),result.getString(11),
					result.getString(7),result.getInt(12));
			productlist.add(myproduct);

		}

		return productlist;
	}

	public String deleteProduct(String custID, String productID) throws Exception {
		
		Connection connection = connect();
		
		PreparedStatement statement = connection
				.prepareStatement("delete from products where custID = '"+custID+"' and productID = '"+productID+"';");
		statement.executeUpdate();

		return "deleted!";
	}

	public String exitAuction(String custID, String productID) throws Exception{
		Connection connection = connect();
		
		PreparedStatement statement = connection
				.prepareStatement("update products set status = 'available' where custID = '"+custID+"' and productID = '"+productID+"';");
		statement.executeUpdate();

		return "exited!";
	}

	public List<Product> yourWins(String custID) throws Exception{
		
		Connection connection = connect();

		//select from DB
		PreparedStatement statement = connection.prepareStatement("select * from products where winnerID =" + custID);
		ResultSet result = statement.executeQuery();

		List<Product> productlist = new ArrayList<>();

		while(result.next()){

			Product myproduct = new Product(result.getString(1),result.getString(2),result.getString(3),result.getString(4),
					result.getString(5),result.getString(8),result.getInt(6),result.getString(9),result.getString(10),result.getString(11),
					result.getString(7),result.getInt(12));
			productlist.add(myproduct);

		}

		return productlist;
	}

}

package serviceImplementation;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ad")
public class ServiceEndpoint {

	Advertisement ad = new Advertisement();
	
	/**
	 * creates a new product for sale.
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public String DBinsert(Product product) throws Exception{
		
		return ad.addProduct(product);
		
	}
	
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public String update(Product product) throws Exception{
		
		return ad.updateProducts(product);
		
	}
	
	@GET
	@Path("/start/{productID}")
	@Produces(MediaType.TEXT_PLAIN)
	public String startAuction(@PathParam ("productID") String productID) throws Exception{
		
		ad.putForAuction(productID);
		return "up for auction!";
		
	}
		
	/**
	 * retrieve a list of all items.
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/info/all")
	@Produces(MediaType.APPLICATION_XML)
	public List<Product> findAllProducts() throws Exception{
		
		return ad.getAllProducts();
	
	}
	
	/**
	 * retrieves information about a specific product.
	 * @param productID
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/info/{productID}")
	@Produces(MediaType.APPLICATION_XML)
	public Product findProduct(@PathParam ("productID") String productID) throws Exception{
		
		return ad.getProduct(productID);
		
	}
	
	@GET
	@Path("/yourproducts/{custID}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Product> yourProducts(@PathParam ("custID") String custID) throws Exception{
		
		return ad.getyourProduct(custID);
		
	}
	
	@GET
	@Path("/ended")
	@Produces(MediaType.APPLICATION_XML)
	public List<Product> endedAuction() throws Exception{
		
		return ad.endedProducts();
		
	}
	
	@GET
	@Path("/delete/{custID}&{productID}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteProduct(@PathParam ("custID") String custID, @PathParam ("productID") String productID) throws Exception{
		
		return ad.deleteProduct(custID,productID);
		
	}
	
	@GET
	@Path("/exit/{custID}&{productID}")
	@Produces(MediaType.TEXT_PLAIN)
	public String exitAuction(@PathParam ("custID") String custID, @PathParam ("productID") String productID) throws Exception{
		
		return ad.exitAuction(custID,productID);
		
	}
	
	@GET
	@Path("/yourwins/{custID}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Product> yourWins(@PathParam ("custID") String custID) throws Exception{
		
		return ad.yourWins(custID);
		
	}

}

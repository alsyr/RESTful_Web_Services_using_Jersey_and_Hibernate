import java.sql.PreparedStatement;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Path("/")
public class Restful {
	private Hw3DAO  dao = new Hw3DAO(); 

	//Retrieve all orders
	@Path("/orders")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnAllOrder() throws Exception
	{ 
		List<Order> allOrders = dao.retrieveOrders();
		String result = "";
		for(Order o : allOrders){
			result += "<p>ID: " + o.getOrderId() + ", Size: " + o.getSize() + 
					", DeliveryTime: " + o.getDeliveryTime() + 
					", Payment: " + o.getPayment() + ", Price: " + o.getPrice() + 
					"</p>";
		}
//		for(Order o : allOrders){
//			result += "<p>ID: " + o.getOrderId() + ", Size: " + o.getSize() + 
//					", Topping(s): " + o.getToppings() + ", DeliveryTime: " + o.getDeliveryTime() + 
//					", Payment: " + o.getPayment() + ", Price: " + o.getPrice() + 
//					"</p>";
//		}
		return result; 
	}

	//Retrieve an order by id
	@Path("/orders/{orderId}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnAnOrder(@PathParam("orderId") String orderId)
	{ 
		int id = Integer.parseInt(orderId);
		Order o = dao.retrieveOrder(id);
		
		if(o == null)
			return "No Order with Id " + id;
		else{
			String result = "";
			result += "<p>ID: " + o.getOrderId() + ", Size: " + o.getSize() + 
					", DeliveryTime: " + o.getDeliveryTime() + 
					", Payment: " + o.getPayment() + ", Price: " + o.getPrice() + 
					"</p>";
			return result; 
		}
	}
	
	//Add a new order
	@Path("/orders")
	@POST
	@Produces(MediaType.TEXT_HTML)
	public String addOrder(@FormParam("deliveryTime") String deliveryTime,
						   @FormParam("payment") String payment,
						   @FormParam("size") String size,
						   @FormParam("userId") String userId,
						   @FormParam("discountedOrder") String discountedOrder) { 
		
		Customer customer = dao.retrieveCustomerById(userId);
		
		PizzaSize theSize = null;
		switch(size) {
			case "small":
				theSize = PizzaSize.SMALL;
				break;
			case "medium":
				theSize = PizzaSize.MEDIUM;
				break;
			case "large":
				theSize = PizzaSize.LARGE;
				break;
			default:
				break;
		}
		
		PaymentMethod method = null;
		switch(payment) {
			case "cash":
				method = PaymentMethod.CASH;
				break;
			case "visa":
				method = PaymentMethod.VISA;
				break;
			case "master":
				method = PaymentMethod.MASTER;
				break;
			default:
				break;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date parsedDate = null;
		Timestamp timestamp = null;
		try {
			parsedDate = dateFormat.parse(deliveryTime); //1987-12-21 11:10:23
			timestamp = new Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
//		dao.createAllTopping();
//		List<Topping> toppings = new ArrayList<Topping>();
//		double toppingPrice = 0;
//		for(int i=0; i<count; i++){
//			System.out.print("Toppings([1]Pepperoni, [2]Mushrooms, [3]Onions, [4]Sausage, [5]Bacon, "
//					+ "[6]Extra cheese, [7]Black olives, [8]Green peppers, [9]Pineapple, [10]Spinach): ");
//			String topping = scan.nextLine();
//			Topping aTopping = null;
//			switch(topping){
//			case "1":
//				aTopping = dao.retrieveTopping(1);
//				break;
//			case "2":
//				aTopping = dao.retrieveTopping(2);
//				break;
//			case "3":
//				aTopping = dao.retrieveTopping(3);
//				break;
//			case "4":
//				aTopping = dao.retrieveTopping(4);
//				break;
//			case "5":
//				aTopping = dao.retrieveTopping(5);
//				break;
//			case "6":
//				aTopping = dao.retrieveTopping(6);
//				break;
//			case "7":
//				aTopping = dao.retrieveTopping(7);
//				break;
//			case "8":
//				aTopping = dao.retrieveTopping(8);
//				break;
//			case "9":
//				aTopping = dao.retrieveTopping(9);
//				break;
//			case "10":
//				aTopping = dao.retrieveTopping(10);
//				break;
//			}
//			toppings.add(aTopping);
//		}
		
		List<Topping> toppings = new ArrayList<Topping>();
		Order o = null;
		final int DISCOUNT_RATE = 10;
		
		if(discountedOrder.equals("true"))
			o = new DiscountedOrder(customer, theSize, method, toppings, timestamp, DISCOUNT_RATE);
		else
			o = new Order(customer, theSize, method, toppings, timestamp);
		
		Order newOrder = dao.createOrder(o);
		String result = "";
		result += "<p>ID: " + o.getOrderId() + ", Size: " + o.getSize() + 
				", DeliveryTime: " + o.getDeliveryTime() + 
				", Payment: " + o.getPayment() + ", Price: " + o.getPrice() + 
				"</p>";
		return "<p>Order created succesfully</p><br>" + result;
	}
	
	
	//Update/Replace the price of the product with id with a new price
	@Path("/orders/{orderId}/{newPrice}")
	@PUT
	@Produces(MediaType.TEXT_HTML)
	public String updateOrder(@PathParam("orderId") String orderId,
							  @PathParam("newPrice") String newPrice) {
		int id = Integer.parseInt(orderId);
		Order o = dao.retrieveOrder(id);
		o.setPrice(Double.parseDouble(newPrice));
		boolean done = dao.updateOrder(o);
		
		if(done) {
			String result = "";
			result += "<p>ID: " + o.getOrderId() + ", Size: " + o.getSize() + 
					", DeliveryTime: " + o.getDeliveryTime() + 
					", Payment: " + o.getPayment() + ", Price: " + o.getPrice() + 
					"</p>";
			return "<p>Order updated Successfully</p><br>" + result;
		}
		else {
			return "<p>Update Failed</p>";
		}
	}
	
	
	//Delete order with id
	@Path("/orders/{orderId}")
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	public String deleteOrder(@PathParam("orderId") String orderId) {
		int id = Integer.parseInt(orderId);
		Order o = dao.retrieveOrder(id);
		boolean done = dao.deleteOrder(o);
		if(done) {
			return "Order " + id + " deleted successfully";
		}
		else{
			return "<p>Delete Failed</p>";
		}
	}
		
	
	
	//Retrieve discounted orders
	@Path("/discounted_order")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getDiscountedOrders() {
		List<Order> allOrders = dao.retrieveOrders();
		List<Order> od = new ArrayList<Order>();
		
		for(Order o : allOrders) {
			if(o instanceof DiscountedOrder)
				od.add(o);
		}
		
		String result = "";
		for(Order o : od){
			result += "<p>ID: " + o.getOrderId() + ", Size: " + o.getSize() + 
					", DeliveryTime: " + o.getDeliveryTime() + 
					", Payment: " + o.getPayment() + ", Price: " + o.getPrice() + 
					"</p>";
		}
		return result;
	}
	
	
	//Retrieve all customers
	@Path("/customers")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getCustomers() throws Exception {
		List<Customer> customers = dao.retrieveCustomers();
		
		String result = "";
		for(Customer c : customers){
			result += "<p>ID: " + c.getUserId() + ", Name: " + c.getUserName() + 
					", Address: " + c.getAddress().toString() + 
					"</p>";
		}
		return result; 
	}
	
	
	//Retrieve customer by id
	@Path("customers/{id}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getCustomer(@PathParam("id") String id) {
		Customer c = dao.retrieveCustomerById(id);

		String result = "";
		result += "<p>ID: " + c.getUserId() + ", Name: " + c.getUserName() + 
				", Address: " + c.getAddress().toString() + 
				"</p>";

		if(c == null)
			return "No Customer with Id " + id;
		else
			return result;
	}
	
	
	//Add a new customer
	@Path("/customers")
	@POST
	@Produces(MediaType.TEXT_HTML)
	public String addCustomer(@FormParam("userName") String userName,
			   				  @FormParam("password") String password,
			   				  @FormParam("streetName") String streetName,
			   				  @FormParam("city") String city,
			   				  @FormParam("state") String state,
			   				  @FormParam("zipCode") String zipCode) {
		
		Customer customer = new Customer(userName, password, streetName, 
				city, state, zipCode);
		Customer c = dao.saveNewCustomer(customer);
		
		String result = "";
		result += "<p>ID: " + c.getUserId() + ", Name: " + c.getUserName() + 
				", Address: " + c.getAddress().toString() + 
				"</p>";
		
		return "<p>Customer created succesfully</p><br>" + result;
	}
	
	
	//Update/Replace the name of the customer identified by id with a new name
	@Path("/customers/{id}/{name}")
	@PUT
	@Produces(MediaType.TEXT_HTML)
	public String updateCustomer(@PathParam("id") String id,
							     @PathParam("name") String name) {
		
		Customer c = dao.retrieveCustomerById(id);
		c.setUserName(name);
		boolean done = dao.updateCustomer(c);
		
		String result = "";
		result += "<p>ID: " + c.getUserId() + ", Name: " + c.getUserName() + 
				", Address: " + c.getAddress().toString() + 
				"</p>";
		
		if(done) {
			return "<p>Update Customer Successful</p><br>" + result;
		}
		else {
			return "<p>Update Failed</p>";
		}
	}
	
	
	//Delete customer with id
	@Path("/customers/{id}")
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	public String deleteCustomer(@PathParam("id") String id) {
		Customer customer = dao.retrieveCustomerById(id);
		dao.deleteCustomer(customer);
		return "Customer " + id + " deleted successfully";
	}
}

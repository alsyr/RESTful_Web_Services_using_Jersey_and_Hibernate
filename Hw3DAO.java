import java.sql.ResultSet;
import java.util.List;

import org.hibernate.*;

public class Hw3DAO {

  private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

  public Hw3DAO() {
  }

  //Function to save a new customer in the database
  public Customer saveNewCustomer(Customer customer) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.save(customer);

    session.getTransaction().commit();
    session.close();
    return customer;
  }

  //Function to retrieve all customers from the database
  public List<Customer> retrieveCustomers() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<Customer> customers = session.createCriteria(Customer.class).list();

    session.getTransaction().commit();
    session.close();
    return customers;
  }

  //Function to retrieve a customer from the database
  public Customer retrieveCustomer(String userName) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    Query query = session.getNamedQuery("Customer.retrieveByUsername");
    query.setString("userName", userName);
    List<Customer> theCustomer = query.list();

    session.getTransaction().commit();
    session.close();

    if (theCustomer.isEmpty())
      return null;

    return theCustomer.get(0);
  }

  //Function to retrieve a customer by ID from the database
  public Customer retrieveCustomerById(String id) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    Query query = session.getNamedQuery("Customer.retrieveById");
    query.setString("id", id);
    List<Customer> theCustomer = query.list();

    session.getTransaction().commit();
    session.close();

    if (theCustomer.isEmpty())
      return null;

    return theCustomer.get(0);
  }

  //Function to create all toppings in the database from the beginning
  public void createAllTopping() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    if (retrieveTopping(1) == null) {
      //									if(true){

      Topping aTopping1 = new Topping();
      aTopping1.setToppingId(1);
      aTopping1.setName("Pepperoni");
      aTopping1.setPrice(1);
      session.save(aTopping1);

      Topping aTopping2 = new Topping();
      aTopping2.setToppingId(2);
      aTopping2.setName("Mushrooms");
      aTopping2.setPrice(0.25);
      session.save(aTopping2);

      Topping aTopping3 = new Topping();
      aTopping3.setToppingId(3);
      aTopping3.setName("Onions");
      aTopping3.setPrice(0.75);
      session.save(aTopping3);

      Topping aTopping4 = new Topping();
      aTopping4.setToppingId(4);
      aTopping4.setName("Sausage");
      aTopping4.setPrice(1.5);
      session.save(aTopping4);

      Topping aTopping5 = new Topping();
      aTopping5.setToppingId(5);
      aTopping5.setName("Bacon");
      aTopping5.setPrice(1.75);
      session.save(aTopping5);

      Topping aTopping6 = new Topping();
      aTopping6.setToppingId(6);
      aTopping6.setName("Extra cheese");
      aTopping6.setPrice(1.25);
      session.save(aTopping6);

      Topping aTopping7 = new Topping();
      aTopping7.setToppingId(7);
      aTopping7.setName("Black olives");
      aTopping7.setPrice(0.5);
      session.save(aTopping7);

      Topping aTopping8 = new Topping();
      aTopping8.setToppingId(8);
      aTopping8.setName("Green peppers");
      aTopping8.setPrice(0.25);
      session.save(aTopping8);

      Topping aTopping9 = new Topping();
      aTopping9.setToppingId(9);
      aTopping9.setName("Pineapple");
      aTopping9.setPrice(0.50);
      session.save(aTopping9);

      Topping aTopping10 = new Topping();
      aTopping10.setToppingId(10);
      aTopping10.setName("Spinach");
      aTopping10.setPrice(0.25);
      session.save(aTopping10);
    }
    session.getTransaction().commit();
  }

  //Function to retrieve a topping
  public Topping retrieveTopping(int toppingId) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    Topping actualTopping = (Topping) session.get(Topping.class, new Integer(toppingId));
    return actualTopping;
  }

  //Function to create an order
  public Order createOrder(Order newOrder) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.save(newOrder);
    session.getTransaction().commit();

    return newOrder;
  }

  //Function to retrieve all orders
  public List<Order> retrieveOrders() {

    Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<Order> orders = session.createCriteria(Order.class).list();

    session.getTransaction().commit();
    session.close();
    return orders;
  }

  //Function to retrieve a particular order
  public Order retrieveOrder(int orderId) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    Order order = (Order) session.get(Order.class, new Integer(orderId));

    session.getTransaction().commit();
    session.close();
    return order;
  }

  //Function to update the name of a customer
  public boolean updateCustomer(Customer customerToChange) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.update(customerToChange);

    session.getTransaction().commit();
    session.close();
    return true;
  }

  //Function to update the price of a product
  public boolean updateOrder(Order orderToChange) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.update(orderToChange);

    session.getTransaction().commit();
    session.close();
    return true;
  }

  //Function to delete a particular order
  public boolean deleteOrder(Order orderToCancel) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.clear();
    session.delete(orderToCancel);

    session.getTransaction().commit();
    session.close();
    return true;
  }

  //Function to delete a particular customer
  public boolean deleteCustomer(Customer customerToDelete) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.clear();
    session.delete(customerToDelete);

    session.getTransaction().commit();
    session.close();
    return true;
  }
}

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER")
@NamedQueries({
    @NamedQuery(name = "Customer.retrieveByUsername", query = "from Customer where username = :username"),
    @NamedQuery(name = "Customer.retrieveById", query = "from Customer where userId = :id")
})
public class Customer {

  @Id
  @GeneratedValue
  private int userId;
  @Column(name = "userName", unique = true)
  private String userName;
  private String password;
  @Embedded
  private Address address;

  @OneToMany(mappedBy = "customer", targetEntity = Order.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Order> orders;

  public Customer() {
    orders = new ArrayList<Order>();
  }

  public Customer(String userName, String password, String streetName,
                  String city, String state, String zipCode) {
    this.userName = userName;
    this.password = password;
    address = new Address(streetName, city, state, zipCode);
    orders = new ArrayList<Order>();
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void addOrder(Order order) {
    orders.add(order);
  }

  public void cancelOrder(Order order) {
    for (int i = 0; i < orders.size(); i++) {
      if (orders.get(i).equals(order)) {
        orders.remove(i);
        break;
      }
    }
  }

  public String toString() {
    return userId + " " + userName + " " + password;
  }
}

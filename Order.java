import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "PIZZA_ORDER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Order {

  @Id
  @GeneratedValue
  @Column(name = "orderId")
  private int orderId;
  protected double price;
  @Temporal(TemporalType.TIMESTAMP)
  private Date deliveryTime;
  @Enumerated(EnumType.STRING)
  private PizzaSize size;
  @Enumerated(EnumType.STRING)
  private PaymentMethod payment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  private Customer customer;

  //	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
  @ManyToMany
  @JoinTable(name = "join_table",
      joinColumns = {@JoinColumn(name = "orderId")},
      inverseJoinColumns = {@JoinColumn(name = "toppingId")}
  )
  private List<Topping> toppings;

  public Order() {
    toppings = new ArrayList<Topping>();
  }

  public Order(Customer customer, PizzaSize size, PaymentMethod payment,
               List<Topping> toppings, Date deliveryTime) {
    this.customer = customer;
    this.size = size;
    this.payment = payment;
    this.toppings = toppings;
    this.deliveryTime = deliveryTime;
    price = countTotalPrice();
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public PizzaSize getSize() {
    return size;
  }

  public void setSize(PizzaSize size) {
    this.size = size;
    price = countTotalPrice();
  }

  public PaymentMethod getPayment() {
    return payment;
  }

  public void setPayment(PaymentMethod payment) {
    this.payment = payment;
  }

  public Date getDeliveryTime() {
    return deliveryTime;
  }

  public void setDate(Date deliveryTime) {
    this.deliveryTime = deliveryTime;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public List<Topping> getToppings() {
    return toppings;
  }

  public void addToppings(Topping topping) {
    toppings.add(topping);
  }

  public void setToppings(List<Topping> toppings) {
    this.toppings = toppings;
    price = countTotalPrice();
  }

  public double countTotalPrice() {
    double total = 0.0;

    switch (size) {
      case SMALL:
        total += 3.0;
        break;
      case MEDIUM:
        total += 5.0;
        break;
      case LARGE:
        total += 7.0;
        break;
      default:
        break;
    }

    double toppingPrice = toppings.size() * 1.0;
    total += toppingPrice;
    return total;
  }
}

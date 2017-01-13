import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="DISCOUNTED_ORDER")
public class DiscountedOrder extends Order{

	private static int discountRate = 10; 

	public DiscountedOrder(){
		new Order();
	}
	
	public int getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(int discountRate) {
		DiscountedOrder.discountRate = discountRate;
	}
	
	public DiscountedOrder(Customer customer, PizzaSize pizzaSize, PaymentMethod paymentMethod, 
			List<Topping> toppings, Date deliveryTime, int discountRate) {
		super(customer, pizzaSize, paymentMethod, toppings, deliveryTime);
		this.discountRate = discountRate;
		price = countTotalPrice();
	}
	
	@Override
	public void setSize(PizzaSize newSize) {
		super.setSize(newSize);
		price = countTotalPrice();
	}

	@Override
	public void setToppings(List<Topping> newToppings) {
		super.setToppings(newToppings);
		price = countTotalPrice();
	}

	@Override
	public double countTotalPrice() {
		return super.countTotalPrice() * (100 - discountRate) / 100;
	}
}

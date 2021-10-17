
import java.io.Serializable;
import java.util.ArrayList;

public class CustomPacket implements Serializable {

    String option;
    String searchingId;
    Customer customer;
    FoodItem item;
    PlaceOrder order;
    String output;

    ArrayList<Customer> CustomerList;
    ArrayList<FoodItem> ItemsList;
    ArrayList<PlaceOrder> OrdersList;

    public CustomPacket() {
    }

    public CustomPacket(String option, String searchingId, Customer customer, String output, ArrayList<Customer> CustomerList) {
        this.option = option;
        this.searchingId = searchingId;
        this.customer = customer;
        this.output = output;
        this.CustomerList = CustomerList;
    }

    public CustomPacket(String option) {
        this.option = option;
    }

    public CustomPacket(ArrayList<PlaceOrder> OrdersList) {
        this.OrdersList = OrdersList;
    }

    public CustomPacket(String option, String searchingId) {
        this.option = option;
        this.searchingId = searchingId;
    }

    public CustomPacket(String option, Customer customer) {
        this.option = option;
        this.customer = customer;
    }

    public CustomPacket(String option, ArrayList<Customer> CustomerList) {
        this.option = option;
        this.CustomerList = CustomerList;
    }

    public CustomPacket(String option, ArrayList<FoodItem> ItemList, String output) {
        this.option = option;
        this.ItemsList = ItemList;
        this.output = output;
    }

    public CustomPacket(String option, FoodItem item) {
        this.option = option;
        this.item = item;
    }

    public CustomPacket(String option, PlaceOrder neworder) {
        this.option = option;
        this.order = neworder;
    }

    public FoodItem getItem() {
        return item;
    }

    public void setItem(FoodItem item) {
        this.item = item;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getOption() {
        return option;
    }

    public String getSearchingId() {
        return searchingId;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public ArrayList<Customer> getCustomerList() {
        return CustomerList;
    }

    public void setCustomerList(ArrayList<Customer> CustomerList) {
        this.CustomerList = CustomerList;
    }

    public ArrayList<FoodItem> getItemsList() {
        return ItemsList;
    }

    public void setItemsList(ArrayList<FoodItem> ItemsList) {
        this.ItemsList = ItemsList;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setSearchingId(String searchingId) {
        this.searchingId = searchingId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PlaceOrder getOrder() {
        return order;
    }

    public void setOrder(PlaceOrder order) {
        this.order = order;
    }

    public ArrayList<PlaceOrder> getOrdersList() {
        return OrdersList;
    }

    public void setOrdersList(ArrayList<PlaceOrder> OrdersList) {
        this.OrdersList = OrdersList;
    }
}

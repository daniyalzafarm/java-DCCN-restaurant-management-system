
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class PlaceOrder implements Serializable {

    int orderid;
    String customerId;
    ArrayList<String> OrderList;
    ArrayList<Integer> QuantityList;
    double totalbill;

    public PlaceOrder(int orderid, String customerId, ArrayList<String> OrderList, double totalbill) {
        this.orderid = orderid;
        this.customerId = customerId;
        this.OrderList = OrderList;
        this.totalbill = totalbill;
    }

    public PlaceOrder(String customerId, ArrayList<String> OrderList, ArrayList<Integer> QuantityList, double totalbill) {
        this.orderid = 1;
        this.customerId = customerId;
        this.OrderList = OrderList;
        this.QuantityList = QuantityList;
        this.totalbill = totalbill;
    }

    public PlaceOrder() {
        this.orderid = 0;
        this.customerId = null;
        this.OrderList = null;
        this.totalbill = 0;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<String> getOrderList() {
        return OrderList;
    }

    public void setOrderList(ArrayList<String> OrderList, ArrayList<Integer> QuantityList) {
        this.OrderList = OrderList;
        this.QuantityList = QuantityList;
    }

    public ArrayList<Integer> getQuantityList() {
        return QuantityList;
    }

    public double getTotalbill() {
        return totalbill;
    }

    public void setTotalbill(double totalbill) {
        this.totalbill = totalbill;
    }

    public static void writeOrderToFile(PlaceOrder s) {

        ObjectOutputStream outputStream = null;
        try {
            ArrayList<PlaceOrder> ordersList = readAllOrders();
            if (ordersList.isEmpty()) {
                ordersList.add(s);
            } else {
                s.setOrderid(ordersList.get(ordersList.size() - 1).getOrderid() + 1);
                ordersList.add(s);
            }

            outputStream = new ObjectOutputStream(new FileOutputStream("Orders.ser"));
            for (int i = 0; i < ordersList.size(); i++) {
                outputStream.writeObject(ordersList.get(i));
            }

        } catch (IOException exp) {
            System.out.println(exp.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static ArrayList<PlaceOrder> readAllOrders() {
        ArrayList<PlaceOrder> OrdersList = new ArrayList<>(0);
        File file = new File("customer.ser");
        if (file.exists()) {
            ObjectInputStream inputStream = null;
            try {
                inputStream = new ObjectInputStream(new FileInputStream("Orders.ser"));
                boolean EOF = false;
                while (!EOF) {
                    try {
                        PlaceOrder myObj = (PlaceOrder) inputStream.readObject();
                        OrdersList.add(myObj);
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class not found");
                    } catch (EOFException end) {
                        EOF = true;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Cannot find file");
            } catch (IOException e) {
                System.out.println("IO Exception while opening stream");
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    System.out.println("IO Exception while closing file");
                }
            }
        }
        return OrdersList;
    }

    public static void DisplayOrdersList(ArrayList<PlaceOrder> OrderList) {
        System.out.println("\n\n----------Orders List----------\n");
        System.out.printf("%-10s%3s%-15s%3s%-20s%3s%-20s%3s%-20s%n", "Order ID", " | ", "Customer ID", " | ", "Items", " | ", "Quantities", " | ", "Total Bill");
        System.out.println("----------------------------------------------------------------------------------------");

        for (PlaceOrder c : OrderList) {
                String name = "";
                String quantity="";
                ArrayList<String> items = c.getOrderList();
                ArrayList<Integer> quantities = c.getQuantityList();
                for (int i = 0; i < items.size(); i++) {
                    name = name.concat(items.get(i));
                    quantity=quantity.concat(String.valueOf(quantities.get(i)));
                    if (i != items.size() - 1) {
                        name = name.concat(",");
                        quantity=quantity.concat(",");
                    }
                }
                System.out.printf("%-10s%3s%-15s%3s%-20s%3s%-20s%3s%-20s%n", c.getOrderid(), " | ", c.getCustomerId(), " | ", name, " | ", quantity, " | ", c.getTotalbill());
            
        }
        System.out.println("\n\n-------Orders List Ended-------\n");
    }
}

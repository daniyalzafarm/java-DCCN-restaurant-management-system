
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    public Server() {

    }

    public void createAndListenSocket() throws FileNotFoundException, IOException {
        try {

            while (true) {
                CustomPacket outputpacket = null;

                try {
                    CustomPacket cp = (CustomPacket) Methods.Recieve(9999);
                    String option = cp.getOption();

                    if (option.equals("vieworders")) {
                        ArrayList<PlaceOrder> orders = PlaceOrder.readAllOrders();
                        outputpacket = new CustomPacket(orders);
                    } else if (option.equals("createorder")) {
                        PlaceOrder neworder = cp.getOrder();
                        PlaceOrder.writeOrderToFile(neworder);
                        outputpacket = new CustomPacket("string");
                        outputpacket.setOutput("\n-------------------------\nThanks for shoping with Us!\nYour Total bill is : " + neworder.getTotalbill()+"\n-------------------------\n");
                    } else if (option.equals("verifylogin")) {
                        outputpacket = new CustomPacket("string");
                        if (Customer.verifyCustomer(cp.getSearchingId())) {
                            outputpacket.setOutput("true");
                        } else {
                            outputpacket.setOutput("false");
                        }
                    } else if (option.equals("addcustomer")) {
                            outputpacket = new CustomPacket("string");
                            if (!Customer.verifyCustomer(cp.getCustomer().getId())) {
                                Customer p = new Customer(cp.getCustomer().getId(), cp.getCustomer().getName(), cp.getCustomer().getPhone(), cp.getCustomer().getAddress());
                                Customer.writeCustomerToFile(p);
                                outputpacket.setOutput("\n-------------------------\n"+cp.getCustomer().getId() + " Customer Addded\n-------------------------\n");
                            } else {
                                outputpacket.setOutput("\n" + cp.getCustomer().getId() + " Customer Already exists!\n");
                            }
                    } else if (option.equals("displayprofile")) {
                        ArrayList<Customer> CustomerData = Customer.readAllCustomers();
                        int con = 0;
                        outputpacket = new CustomPacket("displayprofile");
                        for (Customer c : CustomerData) {
                            con++;
                            if (c.getId().equals(cp.getSearchingId())) {
                                outputpacket.setCustomer(c);
                                break;
                            }
                        }
                    } else if (option.equals("updateprofile")) {
                        ArrayList<Customer> CustomerData = Customer.readAllCustomers();
                        int con = 0;
                        outputpacket = new CustomPacket("updateprofile");
                        for (Customer c : CustomerData) {
                            if (c.getId().equals(cp.getCustomer().getId())) {
                                c.setName(cp.getCustomer().getName());
                                c.setAddress(cp.getCustomer().getAddress());
                                ObjectOutputStream outputStream = null;
                                try {
                                    outputStream = new ObjectOutputStream(new FileOutputStream("customer.ser"));
                                    for (int i = 0; i < CustomerData.size(); i++) {
                                        outputStream.writeObject(CustomerData.get(i));
                                    }
                                    outputpacket.setOutput("\nCustomer Updated!\n");
                                } catch (IOException exp) {
                                    System.out.println("IO Exception while opening file");
                                } finally {
                                    try {
                                        if (outputStream != null) {
                                            outputStream.close();
                                        }
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                                con++;
                                break;
                            }
                        }
                        if (con == 0) {
                            outputpacket.setOutput("\nCustomer Not Found!\n");
                        }
                    } else if (option.equals("viewallcustomers")) {
                        ArrayList<Customer> CustomerData = Customer.readAllCustomers();
                        outputpacket = new CustomPacket("viewallcustomers", CustomerData);

                    } else if (option.equals("deletecustomer")) {
                        outputpacket = new CustomPacket("string");
                        if (Customer.deleteCustomer(cp.getSearchingId())) {
                            outputpacket.setOutput("\nCustomer Deleted!\n");
                        } else {
                            outputpacket.setOutput("\nCustomer NOT Found! \n");
                        }
                    } // ITEM OPERATIONS
                    else if (option.equals("verifyitem")) {
                        outputpacket = new CustomPacket("string");
                        if (FoodItem.verifyItem(cp.getSearchingId())) {
                            outputpacket.setOutput("true");
                        } else {
                            outputpacket.setOutput("false");
                        }
                    } else if (option.equals("additem")) {
                        outputpacket = new CustomPacket("string");
                        if (!FoodItem.verifyItem(cp.getItem().getId())) {
                            FoodItem p = new FoodItem(cp.getItem().getId(), cp.getItem().getName(), cp.getItem().getCategory(), cp.getItem().getPrice());
                            FoodItem.writeFoodItemToFile(p);
                            outputpacket.setOutput("\nNew Item Addded\n");
                        } else {
                            outputpacket.setOutput("\nItem already exists, Please try with different id\n");
                        }

                    } else if (option.equals("deleteitem")) {
                        outputpacket = new CustomPacket("string");
                        if (FoodItem.deleteFoodItem(cp.getSearchingId())) {
                            outputpacket.setOutput("\nItem Deleted!\n");
                        } else {
                            outputpacket.setOutput("\nItem NOT Found! \n");
                        }
                    } else if (option.equals("updateitem")) {
                        ArrayList<FoodItem> ItemsData = FoodItem.readAllItems();
                        outputpacket = new CustomPacket("updateitem");
                        for (FoodItem c : ItemsData) {
                            if (c.getId().equals(cp.getItem().getId())) {
                                c.setName(cp.getItem().getName());
                                c.setCategory(cp.getItem().getCategory());
                                c.setPrice(cp.getItem().getPrice());
                                ObjectOutputStream outputStream = null;
                                try {
                                    outputStream = new ObjectOutputStream(new FileOutputStream("FoodItems.ser"));
                                    for (int i = 0; i < ItemsData.size(); i++) {
                                        outputStream.writeObject(ItemsData.get(i));
                                    }
                                    outputpacket.setOutput("\nItem Updated!\n");
                                } catch (IOException exp) {
                                    System.out.println("IO Exception while opening file");
                                } finally {
                                    try {
                                        if (outputStream != null) {
                                            outputStream.close();
                                        }
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                                break;
                            }
                        }
                    } else if (option.equals("viewallitems")) {
                        ArrayList<FoodItem> ItemsData = FoodItem.readAllItems();
                        outputpacket = new CustomPacket("viewallitems", ItemsData, null);
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }

//              Sending Output
                Methods.Send(outputpacket, 8080);
            }

        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.createAndListenSocket();
    }
}

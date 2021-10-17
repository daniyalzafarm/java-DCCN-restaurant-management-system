
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    public Client() {

    }

    public void createAndListenSocket() throws ClassNotFoundException, SocketException, IOException, InvalidPhone, InvalidQuantity {

        Scanner input = new Scanner(System.in);

        boolean exit = false;
        int j = 1;
        CustomPacket cp = null;
        while (j != 0) {
            System.out.println("\n\n******** Menu ********\n");
            System.out.println("1.Goto Customer Menu");
            System.out.println("2.Goto Admin Menu");
            System.out.println("0.Exit");
            System.out.println("------------------------");
            System.out.print("Enter Your Choice : ");
            int menu = input.nextInt();
            int back = 0;
            switch (menu) {
                case 1:
                    int customercheck = 1;
                    while (customercheck != 0) {
                        System.out.println("\n\n******** Customer Menu ********\n");
                        System.out.println("1.Login");
                        System.out.println("2.Sign Up");
                        System.out.println("------------------------------");
                        System.out.println("-1.Back");
                        System.out.println("0.Exit");
                        System.out.println("------------------------------");
                        System.out.print("Enter your choice : ");
                        String choice = input.next();
                        if ("0".equals(choice)) {
                            System.out.println("\nThank You For Using Our System!\n");
                            System.exit(0);
                        } else if ("-1".equals(choice)) {
                            customercheck = 0;
                        } else if ("1".equals(choice)) {
                            System.out.print("Enter your Username : ");
                            String id = input.next();
                            cp = new CustomPacket("verifylogin", id);
                            Methods.Send(cp, 9999);
                            CustomPacket ReceivedCustomObject = Methods.Recieve(8080);
                            if (ReceivedCustomObject.getOutput().equals("true")) {
                                System.out.println("Verified!");
                                System.out.println("------------------------------");
                                int check1 = 1;
                                while (check1 != 0) {
                                    System.out.println("1.View Profile");
                                    System.out.println("2.Update Profile");
                                    System.out.println("3.See Available Items");
                                    System.out.println("4.Place Order");
                                    System.out.println("------------------------------");
                                    System.out.println("-1.Back");
                                    System.out.println("0.Exit");
                                    System.out.println("------------------------------");
                                    System.out.print("Enter your choice : ");
                                    String choice1 = input.next();
                                    if ("0".equals(choice1)) {
                                        System.out.println("\nThank You For Using Our System!\n");
                                        System.exit(0);
                                    } else if ("-1".equals(choice1)) {
                                        check1 = 0;
                                    } else if ("1".equals(choice1)) {
                                        cp = new CustomPacket("displayprofile", id);
                                        Methods.Send(cp, 9999);
                                        CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                        ReceivedCustomObject1.getCustomer().Display();
                                    } else if ("2".equals(choice1)) {
                                        try {
                                            System.out.println("\n\n******** Updating Profile ********\n");
                                            System.out.print("Update your Name : ");
                                            String name = input.next();
                                            System.out.print("Update your Phone : ");
                                            String phone = input.next();
                                            if (!Customer.verifyPhone(phone)) {
                                                throw new InvalidPhone();
                                            }
                                            System.out.print("Update your Address : ");
                                            String add = input.next();
                                            Customer c = new Customer(id, name, phone, add);
                                            cp = new CustomPacket("updateprofile", c);
                                            Methods.Send(cp, 9999);
                                            CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                            System.out.println(ReceivedCustomObject1.getOutput());
                                        } catch (InvalidPhone | ClassNotFoundException | SocketException ex) {
                                            System.out.println(ex.getMessage());
                                        }

                                    } else if ("3".equals(choice1)) {
                                        cp = new CustomPacket("viewallitems");
                                        Methods.Send(cp, 9999);
                                        CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                        ArrayList<FoodItem> allitems = ReceivedCustomObject1.getItemsList();
                                        if (!allitems.isEmpty()) {
                                            FoodItem.DisplayFoodItemList(allitems);
                                        } else {
                                            System.out.println("\n**********************\n");
                                            System.out.println("No Items Available!");
                                            System.out.println("\n**********************\n");
                                        }
                                    } else if ("4".equals(choice1)) {
                                        cp = new CustomPacket("viewallitems");
                                        Methods.Send(cp, 9999);
                                        CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                        ArrayList<FoodItem> allitems = ReceivedCustomObject1.getItemsList();
                                        if (!allitems.isEmpty()) {
                                            FoodItem.DisplayFoodItemList(allitems);

                                            ArrayList<String> order = new ArrayList<>(0);
                                            ArrayList<Integer> quantity = new ArrayList<>(0);
                                            int stop = 1;
                                            while (stop != 0) {
                                                System.out.print("Enter Item Id to Buy(-1 to checkout!) : ");
                                                String inputid = input.next();
                                                if (inputid.equals("-1")) {
                                                    stop = 0;
                                                } else {
                                                    cp = new CustomPacket("verifyitem", inputid);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject2 = Methods.Recieve(8080);
                                                    if (ReceivedCustomObject2.getOutput().equals("true")) {
                                                        System.out.print("Enter Quantity : ");
                                                        int quan = input.nextInt();
                                                        order.add(inputid);
                                                        quantity.add(quan);
                                                    } else {
                                                        System.out.println(inputid + " NOT found in Items List!");
                                                    }
                                                }

                                            }
                                            double bill = 0;
                                            for (String orderid : order) {
                                                for (FoodItem item : allitems) {
                                                    for (int q : quantity) {
                                                        if (orderid.equals(item.getId())) {
                                                            bill += item.getPrice() * q;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            PlaceOrder neworder = new PlaceOrder(id, order, quantity, bill);
                                            cp = new CustomPacket("createorder", neworder);
                                            Methods.Send(cp, 9999);
                                            CustomPacket CustomObject3 = Methods.Recieve(8080);
                                            System.out.println(CustomObject3.getOutput());
                                        } else {
                                            System.out.println("\n**********************\n");
                                            System.out.println("No Items Available!");
                                            System.out.println("\n**********************\n");
                                        }
                                    } else {
                                        System.out.println("\n-------------------------\nInvalid Selection!\n-------------------------\n");
                                    }
                                }
                            } else {
                                System.out.println("\nNo acccount exist with Username : " + id);
                            }
                        } else if ("2".equals(choice)) {
                            try {
                                System.out.print("Select Username : ");
                                String id = input.next();
                                System.out.print("Enter your Name : ");
                                String name = input.next();
                                System.out.print("Enter your Phone : ");
                                String phone = input.next();
                                if (!Customer.verifyPhone(phone)) {
                                    throw new InvalidPhone();
                                }
                                System.out.print("Enter your Address : ");
                                String add = input.next();
                                Customer c = new Customer(id, name, phone, add);
                                cp = new CustomPacket("addcustomer", c);
                                Methods.Send(cp, 9999);
                                CustomPacket ReceivedCustomObject = Methods.Recieve(8080);
                                System.out.println(ReceivedCustomObject.getOutput());
                            } catch (InvalidPhone | ClassNotFoundException | SocketException ex) {
                                System.out.println(ex.getMessage());
                            }
                        } else {
                            System.out.println("\n-------------------------\nInvalid Selection!\n-------------------------\n");
                        }
                    }

                    break;

                case 2:
                    int admincheck = 1;
                    while (admincheck != 0) {
                        System.out.println("\n\n******** Admin Menu ********\n");
                        System.out.println("1.Login");
                        System.out.println("------------------------------");
                        System.out.println("-1.Back");
                        System.out.println("0.Exit");
                        System.out.println("\n------------------------------\n");

                        System.out.print("Enter your choice : ");
                        String choice = input.next();
                        if ("0".equals(choice)) {
                            System.out.println("\nThank You For Using Our System!\n");
                            System.exit(0);
                        } else if ("-1".equals(choice)) {
                            admincheck = 0;
                        } else if ("1".equals(choice)) {
                            System.out.print("Enter your Username : ");
                            String id = input.next();
                            if (id.equals("admin")) {
                                System.out.println("Verified!");
                                int primarycheck = 1;
                                while (primarycheck != 0) {

                                    System.out.println("\n\n******** Admin Functions ********\n");
                                    System.out.println("01.Manage Orders");
                                    System.out.println("02.Manage Customer");
                                    System.out.println("03.Manage Items");
                                    System.out.println("------------------------------");
                                    System.out.println("-1.Back");
                                    System.out.println("0.Exit");
                                    System.out.println("------------------------------");
                                    System.out.print("Enter your choice : ");
                                    String choice1 = input.next();
                                    if ("0".equals(choice1)) {
                                        System.out.println("\nThank You For Using Our System!\n");
                                        System.exit(0);
                                    } else if ("-1".equals(choice1)) {
                                        primarycheck = 0;
                                    } else if ("1".equals(choice1)) {
                                        int ordercheck = 1;
                                        while (ordercheck != 0) {
                                            System.out.println("\n------------Orders------------");
                                            System.out.println("01.Generate Bill");
                                            System.out.println("02.See all Orders");
                                            System.out.println("------------------------------");
                                            System.out.println("-1.Back");
                                            System.out.println("0.Exit");
                                            System.out.println("------------------------------\n");
                                            System.out.print("Enter your choice : ");
                                            String choice2 = input.next();
                                            if ("0".equals(choice2)) {
                                                System.out.println("\nThank You For Using Our System!\n");
                                                System.exit(0);
                                            } else if ("-1".equals(choice2)) {
                                                ordercheck = 0;
                                            } else if ("1".equals(choice2)) {
                                                System.out.print("Enter Customer Username : ");
                                                String cusername = input.next();
                                                cp = new CustomPacket("verifylogin", cusername);
                                                Methods.Send(cp, 9999);
                                                CustomPacket ReceivedCustomObject = Methods.Recieve(8080);
                                                if (ReceivedCustomObject.getOutput().equals("true")) {
                                                    cp = new CustomPacket("viewallitems");
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    ArrayList<FoodItem> allitems = ReceivedCustomObject1.getItemsList();
                                                    if (!allitems.isEmpty()) {
                                                        FoodItem.DisplayFoodItemList(allitems);

                                                        ArrayList<String> order = new ArrayList<>(0);
                                                        ArrayList<Integer> quantity = new ArrayList<>(0);
                                                        int stop = 1;
                                                        while (stop != 0) {
                                                            try {
                                                                System.out.print("Enter Item Id to Buy(-1 to checkout!) : ");
                                                                String inputid = input.next();
                                                                if (inputid.equals("-1")) {
                                                                    stop = 0;
                                                                } else {
                                                                    cp = new CustomPacket("verifyitem", inputid);
                                                                    Methods.Send(cp, 9999);
                                                                    CustomPacket ReceivedCustomObject2 = Methods.Recieve(8080);
                                                                    if (ReceivedCustomObject2.getOutput().equals("true")) {
                                                                        System.out.print("Enter Quantity : ");
                                                                        int quan = input.nextInt();
                                                                        if (quan <= 0) {
                                                                            throw new InvalidQuantity();
                                                                        }
                                                                        order.add(inputid);
                                                                        quantity.add(quan);
                                                                    } else {
                                                                        System.out.println(inputid + " NOT found in Items List!");
                                                                    }
                                                                }
                                                            } catch (InvalidQuantity | ClassNotFoundException | SocketException ex) {
                                                                System.out.println(ex.getMessage());
                                                            }
                                                        }
                                                        double bill = 0;
                                                        int a = 0;
                                                        for (String orderid : order) {
                                                            for (FoodItem item : allitems) {
                                                                if (orderid.equals(item.getId())) {
                                                                    bill += item.getPrice() * quantity.get(a);
                                                                    a++;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        PlaceOrder neworder = new PlaceOrder(cusername, order, quantity, bill);
                                                        cp = new CustomPacket("createorder", neworder);
                                                        Methods.Send(cp, 9999);
                                                        CustomPacket CustomObject3 = Methods.Recieve(8080);
                                                        System.out.println(CustomObject3.getOutput());
                                                    } else {
                                                        System.out.println("\n**********************\n");
                                                        System.out.println("No Items Available!");
                                                        System.out.println("\n**********************\n");
                                                    }
                                                } else {
                                                    System.out.println("NO acccount exist with Username : " + cusername);
                                                }
                                            } else if ("2".equals(choice2)) {
                                                try {
                                                    cp = new CustomPacket("vieworders");
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedOrders = Methods.Recieve(8080);
                                                    ArrayList<PlaceOrder> allorders = ReceivedOrders.getOrdersList();
                                                    if (!allorders.isEmpty()) {
                                                        PlaceOrder.DisplayOrdersList(allorders);
                                                    } else {
                                                        System.out.println("\n**********************\n");
                                                        System.out.println("No Orders Exists !");
                                                        System.out.println("\n**********************\n");
                                                    }
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else {
                                                System.out.println("\n-------------------------\nInvalid Selection!\n-------------------------\n");
                                            }
                                        }
                                    } else if ("2".equals(choice1)) {
                                        int customerCheck = 1;
                                        while (customerCheck != 0) {
                                            System.out.println("\n----------Customers-----------");
                                            System.out.println("01.Add Customer");
                                            System.out.println("02.View All Customers");
                                            System.out.println("03.Update Customer");
                                            System.out.println("04.Delete Customer");
                                            System.out.println("05.Search Customer");
                                            System.out.println("------------------------------");
                                            System.out.println("-1.Back");
                                            System.out.println("0.Exit");
                                            System.out.println("------------------------------\n");
                                            System.out.print("Enter your choice : ");
                                            String choice2 = input.next();
                                            if ("0".equals(choice2)) {
                                                System.out.println("\nThank You For Using Our System!\n");
                                                System.exit(0);
                                            } else if ("-1".equals(choice2)) {
                                                customerCheck = 0;
                                            } else if ("1".equals(choice2)) {
                                                try {
                                                    System.out.print("Select Username : ");
                                                    String username = input.next();
                                                    System.out.print("Enter Customer Name : ");
                                                    String name = input.next();
                                                    System.out.print("Enter Customer Phone : ");
                                                    String phone = input.next();
                                                    if (!Customer.verifyPhone(phone)) {
                                                        throw new InvalidPhone();
                                                    }
                                                    System.out.print("Enter Customer Address : ");
                                                    String add = input.next();
                                                    Customer c = new Customer(username, name, phone, add);
                                                    cp = new CustomPacket("addcustomer", c);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    System.out.println(ReceivedCustomObject1.getOutput());
                                                } catch (InvalidPhone | ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else if ("2".equals(choice2)) {
                                                cp = new CustomPacket("viewallcustomers");
                                                Methods.Send(cp, 9999);
                                                CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                ArrayList<Customer> allcustomers = ReceivedCustomObject1.getCustomerList();
                                                if (!allcustomers.isEmpty()) {
                                                    Customer.DisplayCustomerList(allcustomers);
                                                } else {
                                                    System.out.println("\n**********************\n");
                                                    System.out.println("No Customer Exists!");
                                                    System.out.println("\n**********************\n");
                                                }
                                            } else if ("3".equals(choice2)) {
                                                System.out.println("\n\n******** Updating Profile ********\n");
                                                System.out.print("Enter Current Username : ");
                                                String username = input.next();
                                                cp = new CustomPacket("verifylogin", username);
                                                Methods.Send(cp, 9999);
                                                CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                if (ReceivedCustomObject1.getOutput().equals("true")) {
                                                    try {
                                                        System.out.println("User Verified!");
                                                        System.out.print("Enter Updated Name : ");
                                                        String name = input.next();
                                                        System.out.print("Update your Phone : ");
                                                        String phone = input.next();
                                                        if (!Customer.verifyPhone(phone)) {
                                                            throw new InvalidPhone();
                                                        }
                                                        System.out.print("Enter Updated Address : ");
                                                        String add = input.next();
                                                        Customer c = new Customer(username, name, phone, add);
                                                        cp = new CustomPacket("updateprofile", c);
                                                        Methods.Send(cp, 9999);
                                                        CustomPacket ReceivedCustomObject2 = Methods.Recieve(8080);
                                                        System.out.println(ReceivedCustomObject2.getOutput());
                                                    } catch (ClassNotFoundException | SocketException ex) {
                                                        System.out.println(ex.getMessage());
                                                    }
                                                } else {
                                                    System.out.println("\nNo acccount exist with Username : " + id);
                                                }
                                            } else if ("4".equals(choice2)) {
                                                try {
                                                    System.out.print("Enter Customer Username : ");
                                                    String username = input.next();
                                                    cp = new CustomPacket("deletecustomer", username);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    System.out.println(ReceivedCustomObject1.getOutput());
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else if ("5".equals(choice2)) {
                                                try {
                                                    System.out.println("\n******** Searching Customer ********\n");
                                                    System.out.print("Enter Item Username you want to search : ");
                                                    String idSearch = input.next();
                                                    cp = new CustomPacket("verifylogin", idSearch);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    if (ReceivedCustomObject1.getOutput().equals("true")) {
                                                        System.out.println(idSearch + " found in Customer List!");
                                                    } else {
                                                        System.out.println(idSearch + " NOT found in Customer List!");
                                                    }
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else {
                                                System.out.println("\n-------------------------\nInvalid Selection!\n-------------------------\n");
                                            }
                                        }
                                    } else if ("3".equals(choice1)) {
                                        int itemcheck = 1;
                                        while (itemcheck != 0) {
                                            System.out.println("\n-----------Food Items----------");
                                            System.out.println("01.Add Item");
                                            System.out.println("02.View All Items");
                                            System.out.println("03.Update Item");
                                            System.out.println("04.Delete Item");
                                            System.out.println("05.Search Item");
                                            System.out.println("------------------------------");
                                            System.out.println("-1.Back");
                                            System.out.println("0.Exit");
                                            System.out.println("------------------------------\n");
                                            System.out.print("Enter your choice : ");
                                            String choice2 = input.next();
                                            if ("0".equals(choice2)) {
                                                System.out.println("\nThank You For Using Our System!\n");
                                                System.exit(0);
                                            } else if ("-1".equals(choice2)) {
                                                itemcheck = 0;
                                            } else if ("1".equals(choice2)) {
                                                try {
                                                    System.out.print("Select Item ID : ");
                                                    String itemid = input.next();
                                                    System.out.print("Enter Item Name : ");
                                                    String itemname = input.next();
                                                    System.out.print("Enter Category : ");
                                                    String categ = input.next();
                                                    System.out.print("Enter Price : ");
                                                    double price = input.nextDouble();
                                                    FoodItem i = new FoodItem(itemid, itemname, categ, price);
                                                    cp = new CustomPacket("additem", i);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    System.out.println(ReceivedCustomObject1.getOutput());
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else if ("2".equals(choice2)) {
                                                try {
                                                    cp = new CustomPacket("viewallitems");
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    ArrayList<FoodItem> allitems = ReceivedCustomObject1.getItemsList();
                                                    if (!allitems.isEmpty()) {
                                                        FoodItem.DisplayFoodItemList(allitems);
                                                    } else {
                                                        System.out.println("\n**********************\n");
                                                        System.out.println("No Items Available!");
                                                        System.out.println("\n**********************\n");
                                                    }
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else if ("3".equals(choice2)) {
                                                try {
                                                    System.out.println("\n\n******** Updating Item ********\n");
                                                    System.out.print("Enter Current Item Id : ");
                                                    String itemid = input.next();
                                                    cp = new CustomPacket("verifyitem", itemid);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    if (ReceivedCustomObject1.getOutput().equals("true")) {
                                                        System.out.println("\nItem Verified!\n");
                                                        System.out.print("Enter Updated Name : ");
                                                        String name = input.next();
                                                        System.out.print("Enter Updated Category : ");
                                                        String categ = input.next();
                                                        System.out.print("Enter Updated Price : ");
                                                        double price = input.nextDouble();
                                                        FoodItem c = new FoodItem(itemid, name, categ, price);
                                                        cp = new CustomPacket("updateitem", c);
                                                        Methods.Send(cp, 9999);
                                                        CustomPacket ReceivedCustomObject2 = Methods.Recieve(8080);
                                                        System.out.println(ReceivedCustomObject2.getOutput());
                                                    } else {
                                                        System.out.println("No Item exist with Item id : " + id);
                                                    }
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else if ("4".equals(choice2)) {
                                                try {
                                                    System.out.print("Enter Item Id : ");
                                                    String itemid = input.next();
                                                    cp = new CustomPacket("deleteitem", itemid);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    System.out.println(ReceivedCustomObject1.getOutput());
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else if ("5".equals(choice2)) {
                                                try {
                                                    System.out.println("\n\n******** Searching Food Item ********\n");
                                                    System.out.print("Enter Item id you want to search : ");
                                                    String idSearch = input.next();
                                                    cp = new CustomPacket("verifyitem", idSearch);
                                                    Methods.Send(cp, 9999);
                                                    CustomPacket ReceivedCustomObject1 = Methods.Recieve(8080);
                                                    if (ReceivedCustomObject1.getOutput().equals("true")) {
                                                        System.out.println(idSearch + " found in Items List!");
                                                    } else {
                                                        System.out.println(idSearch + " NOT found in Items List!");
                                                    }
                                                } catch (ClassNotFoundException | SocketException ex) {
                                                    System.out.println(ex.getMessage());
                                                }
                                            } else {
                                                System.out.println("\n-------------------------\nInvalid Selection!\n-------------------------\n");
                                            }
                                        }
                                    }
                                }
                            } else {
                                System.out.println("\n-------------------------\nInvalid Username!\n-------------------------\n");
                            }
                        } else {
                            System.out.println("\n-------------------------\nInvalid Selection!\n-------------------------\n");
                        }
                    }
                    break;

                case 0:
                    System.out.println("\n-------------------------\nThank You For Using Our System!\n-------------------------\n");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n-------------------------\nInvalid Choice!\n-------------------------\n");
                    break;
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, SocketException, InvalidPhone, InvalidQuantity {
        Client client = new Client();
        client.createAndListenSocket();
    }
}

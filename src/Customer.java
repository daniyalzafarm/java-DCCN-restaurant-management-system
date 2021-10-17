
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

public class Customer implements Serializable {

    private String id;
    private String name;
    private String phone;
    private String address;

    public Customer(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static ArrayList<Customer> readAllCustomers() {
        ArrayList<Customer> CustomerList = new ArrayList<>(0);
        File file = new File("customer.ser");
        if (file.exists()) {
            ObjectInputStream inputStream = null;
            try {
                inputStream = new ObjectInputStream(new FileInputStream("customer.ser"));
                boolean EOF = false;
                while (!EOF) {
                    try {
                        Customer c = (Customer) inputStream.readObject();
                        CustomerList.add(c);
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
        return CustomerList;
    }

    public static void writeCustomerToFile(Customer s) {
        ObjectOutputStream outputStream = null;
        try {
            ArrayList<Customer> CustomersList = readAllCustomers();
            CustomersList.add(s);

            outputStream = new ObjectOutputStream(new FileOutputStream("customer.ser"));
            for (int i = 0; i < CustomersList.size(); i++) {
                outputStream.writeObject(CustomersList.get(i));
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

    public static boolean verifyCustomer(String username) {
        ArrayList<Customer> list = readAllCustomers();
        boolean verified = false;

        for (Customer w : list) {
            if (w.getId().equals(username)) {
                verified = true;
                break;
            }
        }
        return verified;
    }

    public static boolean deleteCustomer(String id) {
        File file = new File("customer.ser");
        boolean deleted = false;
        if (file.exists()) {
            ArrayList<Customer> list = readAllCustomers();
            for (Customer w : list) {
                if (w.getId().equals(id)) {
                    list.remove(w);
                    deleted = true;
                    ObjectOutputStream outputStream = null;
                    try {
                        outputStream = new ObjectOutputStream(new FileOutputStream("customer.ser"));
                        for (int i = 0; i < list.size(); i++) {
                            outputStream.writeObject(list.get(i));
                        }
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
        } else {
            deleted = false;
        }
        return deleted;
    }

    public static boolean verifyPhone(String phone) {
        boolean check = false;
        if (phone.startsWith("03") && phone.length() == 11) {
            check = true;
        }
        return check;
    }

    public void Display() {
        System.out.println("\n**********************");
        System.out.println("Customer ID : " + this.getId());
        System.out.println("Name : " + this.getName());
        System.out.println("Phone : " + this.getPhone());
        System.out.println("Address : " + this.getAddress());
        System.out.println("**********************\n");

    }

    public static void DisplayCustomerList(ArrayList<Customer> CustomerList) {
        System.out.println("\n\n----------Customers List----------\n");
        System.out.printf("%-20s%3s%-20s%3s%-15s%3s%-40s%n", "Username", " | ", "Name", " | ", "Phone", " | ", "Address");
        System.out.println("----------------------------------------------------------------");
        for (Customer c : CustomerList) {
            System.out.printf("%-20s%3s%-20s%3s%-15s%3s%-40s%n", c.getId(), " | ", c.getName(), " | ", c.getPhone(), " | ", c.getAddress());
        }
        System.out.println("\n\n-------Customers List Ended-------\n");
    }

}

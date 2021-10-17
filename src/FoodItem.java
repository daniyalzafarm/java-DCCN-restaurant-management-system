
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

public class FoodItem implements Serializable {

    private String id;
    private String name;
    private String category;
    private double price;

    public FoodItem(String id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void updateItemPrice(String id, double price) {
        this.price = price;
        ArrayList<FoodItem> list = readAllItems();
        for (FoodItem w : list) {
            if (id.equals(w.getId())) {
                w.setPrice(price);
                break;
            }
        }
        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(new FileOutputStream("FoodItems.ser"));
            for (int i = 0; i < list.size(); i++) {
                outputStream.writeObject(list.get(i));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
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

    public static ArrayList<FoodItem> readAllItems() {
        ArrayList<FoodItem> FoodItemList = new ArrayList<>(0);
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream("FoodItems.ser"));
            boolean EOF = false;
            while (!EOF) {
                try {
                    FoodItem myObj = (FoodItem) inputStream.readObject();
                    FoodItemList.add(myObj);
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
        return FoodItemList;
    }

    public static void writeFoodItemToFile(FoodItem s) {
        ObjectOutputStream outputStream = null;
        try {
            ArrayList<FoodItem> FoodItemsList = readAllItems();
            FoodItemsList.add(s);

            outputStream = new ObjectOutputStream(new FileOutputStream("FoodItems.ser"));
            for (int i = 0; i < FoodItemsList.size(); i++) {
                outputStream.writeObject(FoodItemsList.get(i));
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

    public static boolean verifyItem(String id) {
        ArrayList<FoodItem> list = readAllItems();
        boolean verified = false;

        for (FoodItem w : list) {
            if (w.getId().equals(id)) {
                verified = true;
                break;
            }
        }
        return verified;
    }

    public static boolean deleteFoodItem(String id) {
        File file = new File("FoodItems.ser");
        boolean deleted = false;
        if (file.exists()) {
            ArrayList<FoodItem> list = readAllItems();
            for (FoodItem w : list) {
                if (id.equals(w.getId())) {
                    list.remove(w);
                    deleted = true;
                    ObjectOutputStream outputStream = null;
                    try {
                        outputStream = new ObjectOutputStream(new FileOutputStream("FoodItems.ser"));
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

    public void Display() {
        System.out.println("Item ID : " + this.getId());
        System.out.println("Name : " + this.getName());
        System.out.println("Category : " + this.getCategory());
        System.out.println("Price : " + this.getPrice());
        System.out.println("**********************");
    }

    public static void DisplayFoodItemList(ArrayList<FoodItem> FoodItemList) {
        System.out.println("\n\n----------Items List----------\n");
        System.out.printf("%-10s%3s%-20s%3s%-20s%3s%-20s%n", "Item ID", " | ", "Item Name", " | ", "Category", " | ", "Price");
        System.out.println("-------------------------------------------------------------------");
        for (FoodItem c : FoodItemList) {
            System.out.printf("%-10s%3s%-20s%3s%-20s%3s%.2f%n", c.getId(), " | ", c.getName(), " | ", c.getCategory(), " | ", c.getPrice());
        }
        System.out.println("\n\n-------Items List Ended-------\n");
    }
}

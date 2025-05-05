package com.kolawole;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class Main {
    //Class Scanner
    static Scanner scanner = new Scanner(System.in);
    //Class File
    private static final String FILE_PATH = "products.csv";
    //Global variable cart
    private static final Map<String, Integer> cart = new HashMap<>();
    public static void main(String[] args) {
        //Home Screen
        boolean flag = true;
        while (flag) {
            System.out.println("*** Welcome to the Home Screen ***");
            System.out.println("Please select one of the following options: ");
            System.out.println("P - To Display Products");
            System.out.println("C - To Display Cart");
            System.out.println("E - To Exit Application");
            String userInput = scanner.nextLine().toUpperCase().trim();
            switch (userInput){
                case "P":
                    displayProductsScreen();
                    break;
                case "C":
                    //method to display user cart
                    break;
                case "E":
                    System.out.println("Thank You Goodbye");
                    flag = false; //break the while loop
                    break;
                default:
                    System.out.println("Please Enter Option P, C, or E. ");

            }
        }
    }
    public static void displayProductsScreen(){
        boolean flag = true;
        while (flag){
            System.out.println("*** Welcome to the Display Products Screen ***");
            System.out.println("Please select one of the following options: ");
            System.out.println("V - View the Product List");
            System.out.println("S - Search or Filter Product List");
            System.out.println("A - Add a Product to Cart");
            System.out.println("H - Go back to Home Screen");
            String userInputProduct = scanner.nextLine().toUpperCase().trim();
            switch (userInputProduct){
                case "V":
                    //view all products
                    printProducts(loadProducts());
                    break;
                case "S":
                    //method to search or filter for products
                    filterProducts();
                    break;
                case "A":
                    //method to add a product to cart
                    addProductToCart();
                    break;
                case "H":
                    //Go back to Home Screen
                    flag = false;
                    break;
                default:
                    System.out.println("Please Enter Option: S, A, or H. ");

            }
        }
    }

    public static void displayCartScreen(){
        boolean flag = true;
        while (flag){
            System.out.println("*** Welcome to the Display Cart Screen ***");
            System.out.println("Please select one of the following options: ");
            System.out.println("C - To Checkout ");
            System.out.println("A - Remove product from cart");
            System.out.println("X - Go back to Home Screen");
            String userInputCart = scanner.nextLine().toLowerCase().trim();
            switch (userInputCart){
                case "C":
                    //method to checkout product
                    break;
                case "A":
                    //method to remove products from cart
                    break;
                case "X":
                    //method to go back to the Home Screen
                    flag = false;
                    break;
                default:
                    System.out.println("Please select options C, A, or X");
            }
        }
    }
    public static List<Product> loadProducts(){
        //BuffReader
        List<Product> productList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            bufferedReader.readLine(); //Vet important early bug
            while ((line = bufferedReader.readLine()) != null){
                //Build a new product list
                String[] splitProduct = line.split("\\|");
                Product product = new Product(splitProduct[0], splitProduct[1], Double.parseDouble(splitProduct[2]), splitProduct[3]);

                productList.add(product);

            }
        }
        catch (IOException e){
            System.out.println("Error reading the product file");
        }
        catch (NumberFormatException e){
            System.out.println("Error parsing the product price");
        }
        return productList;
    }
    public static void printProducts(List<Product> productList){
        for (Product product : productList){
            System.out.println(product.displayProductFormat());
        }
    }

    public static void addProductToCart(){
        //Ask user for a SKU
        System.out.println("To add an item to your cart, please provide item SKU number");
        String userSkuInput = scanner.nextLine();
        //Load the products list
        List<Product> productList = loadProducts();
        //Search for matching products
        boolean flag = false;
        for (Product product : productList){
            if(product.getSku().equalsIgnoreCase(userSkuInput)){
                cart.add(product);
                System.out.println(product.getProductName() + " has been added to you cart.");
                flag = true; //found the product
                break;
            }
        }
        //If item is not found, it's not available
        if(!flag){
            System.out.println("No product matching SKU value");
        }
    }

    public static void filterProducts(){
        //filter by price range
        //filter by name
        //filter by department
        boolean flag = true;
        while(flag){
            System.out.println("Filter by the following options");
            System.out.println("N - To Filter by Name");
            System.out.println("P - To Filter by Price Range");
            System.out.println("D - To Filter by Department");
            System.out.println("X - Go back to Display Product Screen");
            String userFilterInput = scanner.nextLine().toUpperCase().trim();
            switch (userFilterInput){
                case "N":
                    // Filter by name
                    filterByName();
                    break;
                case "P":
                    //Filter by price
                    filterByPriceRange();
                    break;
                case "D":
                    //Filter by department
                    filterByDepartment();
                case "X":
                    flag = false;
                    break;
                default:
                    System.out.println("Please select option N, P, D, or X ");

            }
        }

    }

    public static void filterByName(){
        String userFilterInput = scanner.nextLine().toLowerCase().trim();
        List<Product> filteredProductList = new ArrayList<>();
        List<Product> productList = loadProducts();

        for (Product product : productList){
            if(product.getProductName().toLowerCase().contains(userFilterInput)){
                filteredProductList.add(product);
            }
        }

        if(filteredProductList.isEmpty()){
            System.out.println("No product with this name in our list");
        }
        else{
            printProducts(filteredProductList);
        }
    }

    public static void filterByDepartment(){
        String userDepartmentInput = scanner.nextLine().toLowerCase().trim();
        List<Product> filteredProductList = new ArrayList<>();
        List<Product> productList = loadProducts();

        for (Product product : productList){
            if(product.getDepartment().toLowerCase().contains(userDepartmentInput)){
                filteredProductList.add(product);
            }
        }

        if(filteredProductList.isEmpty()){
            System.out.println("No product with this department in our list");
        }
        else{
            printProducts(filteredProductList);
        }
    }

    public static void filterByPriceRange(){
        //Ask the user for min price
        //Ask for the max price
        try{
            System.out.println("What is the minimum price:");
            double minPrice = Double.parseDouble(scanner.nextLine().trim());

            System.out.println("What is the maximum price:");
            double maxPrice = Double.parseDouble(scanner.nextLine().trim());

            if (minPrice>maxPrice){
                throw new IllegalArgumentException("Minimum price can't be greater than the Maximum Price");
            }
            List<Product> productList = loadProducts();
            List<Product> filteredPriceList = new ArrayList<>();

            for(Product product : productList){
                double productPrice = product.getPrice();
                if(productPrice >= minPrice && productPrice <= maxPrice){
                    filteredPriceList.add(product);
                }
            }
            if(filteredPriceList.isEmpty()){
                System.out.println("No products found in this price range");
            }
            else{
                printProducts(filteredPriceList);
            }
        }
        catch (NumberFormatException e){
            System.out.println("Invalid input. please enter valid numbers for price");
        }

    }

    public static void checkOut(){

    }




}

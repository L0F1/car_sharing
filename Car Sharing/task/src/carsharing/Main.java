package carsharing;

import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {

    private static Scanner scanner;
    private static Company company;
    private static Car car;
    private static Customer customer;

    public static void main(String[] args) {
        String fileName = parseDbFileName(args);
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\\n");

        company = new Company(fileName);
        car = new Car(fileName);
        customer = new Customer(fileName);

        logInMenu();
    }

    private static void logInMenu() {
        int choice;

        do {
            out.println("\n1. Log in as a manager");
            out.println("2. Log in as a customer");
            out.println("3. Create a customer");
            out.println("0. Exit\n");

            choice = readAndCheckInput(3);

            switch (choice) {
                // Log in as a manager
                case 1:
                    managerMenu();
                    break;
                // Log in as a customer
                case 2:
                    String customers = customer.getAll();
                    if (customers.equals("")) out.println("The customer list is empty!");
                    else chooseCustomerMenu(customers);
                    break;
                // Create customer
                case 3:
                    out.println("Enter the customer name:");
                    String newCostomer = scanner.next();
                    customer.add(newCostomer);
                    out.println("The customer was created!");
                    break;
                // Exit
                case 0:
                    customer.close();
                    car.close();
                    company.close();
                    break;
                // Input error
                default:
                    break;
            }
        } while(choice != 0);
    }

    private static void managerMenu() {
        out.println("\n1. Company list");
        out.println("2. Create a company");
        out.println("0. Back\n");

        int choice = readAndCheckInput(2);

        switch (choice) {
            // company list
            case 1:
                String companys = company.getAll();
                if (companys.equals("")) {
                    out.println("The company list is empty!");
                }
                else chooseCompanyMenu(companys);
                managerMenu();
                break;
            // Create a company
            case 2:
                out.println("Enter the company name:");
                String newCompany = scanner.next();
                company.add(newCompany);
                out.println("The company was created!");
                managerMenu();
                break;
            // Back
            case 0:
                break;
            // Input error
            default:
                managerMenu();
                break;
        }
    }

    private static void chooseCustomerMenu(String customers) {
        out.println("\nChoose a customer:");
        out.print(customers);
        out.println("0. Back\n");

        int choice = readAndCheckInput(customers.split("\n").length);

        // Input error
        if (choice < 0) {
            chooseCustomerMenu(customers);
            return;
        }
        // Choosing the customer
        if (choice != 0) customerMenu(choice);

    }

    private static void chooseCompanyMenu(String companys) {
        out.println("\nChoose the company:");
        out.print(companys);
        out.println("0. Back\n");

        int choice = readAndCheckInput(companys.split("\n").length);

        // Input error
        if (choice < 0) {
            chooseCompanyMenu(companys);
            return;
        }
        // Choosing the company
        if (choice != 0) companyMenu(choice);
    }

    private static void customerMenu(int customer_id) {
        out.println("\n1. Rent a car");
        out.println("2. Return a rented car");
        out.println("3. My rented car");
        out.println("0. Back\n");

        int choice = readAndCheckInput(3);

        switch (choice) {
            // Rent a car
            case 1:
                if (!customer.isCarRented(customer_id)) {
                    String companys = company.getAll();
                    if (companys.equals("")) out.println("The company list is empty!");
                    else rentCarChooseComMenu(companys, customer_id);
                } else out.println("You've already rented a car!");
                customerMenu(customer_id);
                break;
            // Return a rented car
            case 2:
                if (customer.isCarRented(customer_id)) {
                    customer.returnRentedCar(customer_id);
                    out.println("You've returned a rented car!");
                } else out.println("You didn't rent a car!");
                customerMenu(customer_id);
                break;
            // My rented car
            case 3:
                if (customer.isCarRented(customer_id)) {
                    out.println(customer.getRentedCar(customer_id));
                } else out.println("You didn't rent a car!");
                customerMenu(customer_id);
                break;
            // Back
            case 0:
                break;
            // Input error
            default:
                customerMenu(customer_id);
                break;
        }
    }

    private static void companyMenu(int company_id) {
        out.println(String.format("\n%s company:",company.getCompany(company_id)));
        out.println("1. Car list");
        out.println("2. Create a car");
        out.println("0. Back\n");

        int choice = readAndCheckInput(2);

        switch (choice) {
            // Car list
            case 1:
                if(car.getAll(company_id).equals("")) out.println("The car list is empty!");
                else out.print(car.getAll(company_id));
                companyMenu(company_id);
                break;
            // Create a car
            case 2:
                out.println("Enter the car name:");
                String newCar = scanner.next();
                car.add(newCar, company_id);
                out.println("The car was created!");
                companyMenu(company_id);
                break;
            // Back
            case 0:
                break;
            // Input error
            default:
                companyMenu(company_id);
                break;
        }
    }

    private static void rentCarChooseComMenu(String companys, int customer_id) {
        out.println("\nChoose the company:");
        out.print(companys);
        out.println("0. Back\n");

        int choice = readAndCheckInput(companys.split("\n").length);

        // Input error
        if (choice < 0) {
            rentCarChooseComMenu(companys, customer_id);
            return;
        }
        if (choice != 0) {
            String cars = car.getAll(choice);
            if (cars.equals("")) {
                out.println(String.format("No available cars in the %s company",
                        company.getCompany(choice)));
                rentCarChooseComMenu(companys, customer_id);
            } else rentCarChooseCarMenu(cars, customer_id, choice);
        }
    }

    private static void rentCarChooseCarMenu(String cars, int customer_id, int company_id) {
        out.println("\nChoose a car:");
        out.print(cars);
        out.println("0. Back\n");

        int choice = readAndCheckInput(cars.split("\n").length);

        // Input error
        if (choice < 0) {
            rentCarChooseCarMenu(cars, customer_id, company_id);
            return;
        }
        if (choice != 0) {
            String carName = cars.split("\n")[choice - 1].substring(3);
            customer.rentCar(carName, customer_id, company_id);
            out.println(String.format("You rented '%s'", carName));
        }
    }

    /**
     * return -1 if input is not int
     * return -2 if input is not in range
     */
    private static int readAndCheckInput(int range) {
        int choice;

        try {
            choice = scanner.nextInt();
        } catch(InputMismatchException e) {
            out.println("Wrong type, try again...");
            scanner.next();
            return -1;
        }
        if (choice < 0 || choice > range) {
            out.println("Wrong number, try again...");
            return -2;
        }
        return choice;
    }

    private static String parseDbFileName(String[] args) {
        if(args.length >= 2 && args[0].equals("-databaseFileName")) {
            return args[1];
        }
        else return "carsharingdb";
    }
}
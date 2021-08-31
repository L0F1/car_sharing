package carsharing;

import java.sql.*;

public class Customer extends DAO {

    private static final String DD_QUERY = "CREATE TABLE IF NOT EXISTS customer" +
            " (id INT PRIMARY KEY AUTO_INCREMENT(1,1), " +
            "name VARCHAR UNIQUE NOT NULL, " +
            "rented_car_id INT REFERENCES car(id))";
    private static final String tableName = "customer";

    public Customer(String DbName) {
        super(DbName, tableName, DD_QUERY);
    }

    public String getAll() {
        return super.getAll(String.format("SELECT * FROM %s  ORDER BY id", tableName), "name");
    }

    public void add(String customer) {
        super.add(String.format("INSERT INTO %s (name) VALUES ('%s')", tableName, customer));
    }

    public void rentCar(String carName, int customer_id, int company_id) {
        try {
            Statement st = this.conn.createStatement();
            ResultSet result = st.executeQuery(String.format("SELECT car.id AS car_id FROM car JOIN company ON " +
                    "car.company_id = company.id WHERE car.name = '%s' AND company.id = %d", carName, company_id));
            int car_id = 0;
            while(result.next()) {
                car_id = result.getInt("car_id");
            }
            st.executeUpdate("UPDATE car SET is_rented = TRUE WHERE id = " + car_id);
            st.executeUpdate(String.format("UPDATE customer SET rented_car_id = %d WHERE id = %d", car_id, customer_id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isCarRented(int customer_id) {
        ResultSet result;
        StringBuilder output = new StringBuilder("");

        try {
            Statement st = this.conn.createStatement();
            result = st.executeQuery(String.format("SELECT name from %s WHERE id = %d AND rented_car_id IS NOT NULL", tableName, customer_id));

            while(result.next()) {
                output.append(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return !output.toString().equals("");
    }

    public void returnRentedCar(int customer_id) {
        try {
            Statement st = this.conn.createStatement();
            ResultSet result = st.executeQuery("SELECT rented_car_id AS car_id FROM customer WHERE id = " + customer_id);
            int car_id = 0;
            while(result.next()) {
                car_id = result.getInt("car_id");
            }
            st.executeUpdate("UPDATE car SET is_rented = FALSE WHERE id = " + car_id);
            st.executeUpdate("UPDATE customer SET rented_car_id = NULL WHERE id = " + customer_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getRentedCar(int customer_id) {
        ResultSet result;
        StringBuilder output = new StringBuilder("Your rented car:\n");
        String carName = "";

        try {
            Statement st = this.conn.createStatement();
            result = st.executeQuery("SELECT car.name AS car_name FROM customer JOIN car ON car.id = " +
                    "customer.rented_car_id WHERE customer.id = " + customer_id);
            while(result.next()) {
                carName = result.getString("car_name");
                output.append(carName);
            }
            output.append("\nCompany:\n");
            result = st.executeQuery(String.format("SELECT company.name AS com_name FROM company JOIN car ON company.id = " +
                    "car.company_id WHERE car.name = '%s'", carName));
            while(result.next()) {
                output.append(result.getString("com_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    public void close() {
        super.close(tableName);
    }
}

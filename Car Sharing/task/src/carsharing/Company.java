package carsharing;

import java.sql.*;

public class Company extends DAO {

    private static final String DD_QUERY = "CREATE TABLE IF NOT EXISTS company" +
            " (id INT PRIMARY KEY AUTO_INCREMENT(1,1), name VARCHAR UNIQUE NOT NULL)";
    private static final String tableName = "company";

    public Company(String DbName) {
        super(DbName, tableName, DD_QUERY);
    }

    public String getCompany(int id) {
        ResultSet result;

        try {
            Statement st = this.conn.createStatement();
            result = st.executeQuery("SELECT name FROM company WHERE id = " + id);
            if (result.next())
                return result.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getAll() {
        return super.getAll(String.format("SELECT * FROM %s ORDER BY id", tableName), "name");
    }

    public void add(String company) {
        super.add(String.format("INSERT INTO %s (name) VALUES ('%s')", tableName, company));
    }

    public void close() {
        super.close(tableName);
    }
}

package carsharing;

import java.sql.*;

public abstract class DAO {
    private static final String DRIVER = "org.h2.Driver";
    private static final String DB_PATH = "jdbc:h2:./src/carsharing/db/";
    protected Connection conn;

    public DAO(String DbName, String tableName, String DD_QUERY) {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_PATH + DbName);
            conn.setAutoCommit(true);
            Statement st = this.conn.createStatement();
            // create table
            st.executeUpdate(DD_QUERY);
            // reset auto increment
            st.executeUpdate(String.format("ALTER TABLE %s ALTER COLUMN id RESTART WITH 1", tableName));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAll(String query, String columnName) {
        ResultSet result;
        StringBuilder output = new StringBuilder();

        try {
            Statement st = this.conn.createStatement();
            result = st.executeQuery(query);
            int count = 1;

            while(result.next()) {
                output.append(count).append(". ").append(result.getString(columnName)).append("\n");
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    public void add(String query) {
        try {
            Statement st = this.conn.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void drop(String tableName) {
        try {
            Statement st = this.conn.createStatement();
            st.executeUpdate(String.format("DROP TABLE %s IF EXISTS", tableName));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(String tableName) {
        try {
            //drop(tableName);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

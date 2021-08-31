package carsharing;

public class Car extends DAO {

    private static final String DD_QUERY = "CREATE TABLE IF NOT EXISTS car" +
            " (id INT PRIMARY KEY AUTO_INCREMENT(1,1)," +
            " name VARCHAR UNIQUE NOT NULL," +
            " is_rented BOOLEAN NOT NULL," +
            " company_id INT NOT NULL REFERENCES company(id))";
    private static final String tableName = "car";

    public Car(String DbName) {
        super(DbName, tableName, DD_QUERY);
    }

    public String getAll(int company_id) {
        return super.getAll(String.format("SELECT * FROM %s WHERE company_id = %d AND is_rented = FALSE ORDER BY id", tableName, company_id),
                "name");
    }

    public void add(String car, int company_id) {
        super.add(String.format("INSERT INTO %s (name, is_rented, company_id) VALUES ('%s', %s, %d)", tableName, car, "FALSE", company_id));
    }

    public void close() {
        super.close(tableName);
    }
}

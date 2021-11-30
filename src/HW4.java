import java.sql.*;

public class HW4 {
    public static void main(String[] args) {
        // Load mysql driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Can't load driver");
        }

        // connect to db and run queries
        try {
            // connect to db
            System.out.println("Starting connection...");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "");
            System.out.println("Connectiion established.\n");

            Statement stmt = con.createStatement();
            // run queries
            runQuery1(stmt, "select * from student");
            // runQuery2(stmt, "");
            // runQuery3(stmt, "");
            // runQuery4(stmt, "");
            // runQuery5(stmt, "");
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " Can't connect to database.");
            while (e != null) {
                System.out.println("Message: " + e.getMessage());
                e = e.getNextException();
            }
        } catch (Exception e) {
            System.out.println("Some other errors and stuff...yeup");
        }
    }
    // runs a query on the database
    public static void runQuery1(Statement stmt, String query) throws SQLException {
        ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 1...");
        while (result.next()) {
            String name = result.getString("sname");
            String major = result.getString("major");
            System.out.println(name + " is majoring in " + major + "!");
        }

        System.out.println();
    }
    public static void runQuery2(Statement stmt, String query) throws SQLException {
        ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 2...");
        while (result.next()) {
            String name = result.getString("sname");
            String major = result.getString("major");
            System.out.println(name + " is majoring in " + major + "!");
        }

        System.out.println();
    }
    public static void runQuery3(Statement stmt, String query) throws SQLException {
        ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 3...");
        while(result.next()) {
            // do something
        }

        System.out.println();
    }
    public static void runQuery4(Statement stmt, String query) throws SQLException {
        ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 4...");
        while(result.next()) {
            // do something
        }

        System.out.println();
    }
    public static void runQuery5(Statement stmt, String query) throws SQLException {
        ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 1...");
        while(result.next()) {
            // do something
        }

        System.out.println();
    }
}
import java.sql.*;
import java.util.Scanner;

public class HW4 {
    static Scanner s;
    static Statement stmt;

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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/real_estate", "root", "");
            System.out.println("Connectiion established.\n");

            stmt = con.createStatement();
            s = new Scanner(System.in);

            // run queries
            runQuery1();
            // runQuery2();
            // runQuery3();|
            // runQuery4();
            // runQuery5();
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
    public static void runQuery1() throws SQLException {
        System.out.print("Enter client's id: ");
        int cid = s.nextInt();

        String query = "SELECT lb.cid, listprice, street, city, state, zip, acreage" +
                "FROM land_buyer lb, land_view lv" +
                "WHERE lv.acreage >= lb.min_acres" +
                "and lv.acreage <= lb.max_acres" +
                "and cid = " + cid + ";";
        ResultSet result = stmt.executeQuery(query);

        while (result.next()) {
            String listprice = result.getString("listprice");
            String street = result.getString("street");
            String city = result.getString("city");
            String state = result.getString("state");
            String zip = result.getString("zip");
            String acreage = result.getString("acreage");

            System.out.println("cid: " + cid);
            System.out.println("listprice: " + listprice);
            System.out.println("street: " + street);
            System.out.println("city: " + city);
            System.out.println("state: " + state);
            System.out.println("zip: " + zip);
            System.out.println("acreage: " + acreage);
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
        while (result.next()) {
            // do something
        }

        System.out.println();
    }

    public static void runQuery4(Statement stmt, String query) throws SQLException {
        ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 4...");
        while (result.next()) {
            // do something
        }

        System.out.println();
    }

    public static void runQuery5(Statement stmt, String query) throws SQLException {
        ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 1...");
        while (result.next()) {
            // do something
        }

        System.out.println();
    }
}
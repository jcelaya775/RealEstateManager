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

            System.out.print("Enter a query to run: ");
            int query = s.nextInt();

            switch (query) {
                case 1:
                    runQuery1();
                    break;
                case 2:
                    runQuery2();
                    break;
                case 3:
                    runQuery3();
                    break;
                case 4:
                    runQuery4();
                    break;
                case 5:
                    runQuery5();
                    break;
            }
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

        String view = "CREATE OR REPLACE VIEW land_view AS\n"
                + "SELECT la.pid, listprice, street, city, state, zip, acreage\n"
                + "FROM land la join listing li\n"
                + "WHERE la.pid = li.pid;\n";
        stmt.executeUpdate(view);

        String query = "SELECT lv.pid, listprice, street, city, state, zip, acreage\n"
                + "FROM land_buyer lb, land_view lv\n"
                + "WHERE lv.acreage >= lb.min_acres\n"
                + "AND lv.acreage <= lb.max_acres\n"
                + "AND cid = " + cid + ";";
        ResultSet result = stmt.executeQuery(query);

        System.out.println("\nProperties that match land buying client " + cid + "'s preferences:\n");
        while (result.next()) {
            String pid = result.getString("pid");
            String listprice = result.getString("listprice");
            String street = result.getString("street");
            String city = result.getString("city");
            String state = result.getString("state");
            String zip = result.getString("zip");
            String acreage = result.getString("acreage");

            System.out.println("pid: " + pid);
            System.out.println("listprice: " + listprice);
            System.out.println("street: " + street);
            System.out.println("city: " + city);
            System.out.println("state: " + state);
            System.out.println("zip: " + zip);
            System.out.println("acreage: " + acreage);
            System.out.println();
        }

        System.out.println();
    }

    public static void runQuery2() throws SQLException {
        System.out.print("Enter house buying client's id: ");
        int cid = s.nextInt();

        String view = "CREATE OR REPLACE VIEW house_view AS\n"
                + "SELECT h.pid, listprice, street, city, state, zip, bed, bath, style\n"
                + "FROM house h join listing l\n"
                + "ON h.pid = l.pid;";
        stmt.executeUpdate(view);

        String query = "SELECT cid, pid, listprice, street, city, state, zip, hb.bed, hb.bath, hb.style\n"
                + "FROM house_buyer hb, house_view hv\n"
                + "WHERE hb.style = hv.style\n"
                + "AND hb.bed = hv.bed\n"
                + "AND hb.bath = hv.bath\n"
                + "AND cid = " + cid + ";";
        ResultSet result = stmt.executeQuery(query);

        System.out.println("\nProperties that match house buying client " + cid + "'s preferences:\n");
        while (result.next()) {
            String pid = result.getString("pid");
            String listprice = result.getString("listprice");
            String street = result.getString("street");
            String city = result.getString("city");
            String state = result.getString("state");
            String zip = result.getString("zip");
            String bed = result.getString("bed");
            String bath = result.getString("bath");
            String style = result.getString("style");

            System.out.println("pid: " + pid);
            System.out.println("listprice: " + listprice);
            System.out.println("street: " + street);
            System.out.println("city: " + city);
            System.out.println("state: " + state);
            System.out.println("zip: " + zip);
            System.out.println("bed: " + bed);
            System.out.println("bath: " + bath);
            System.out.println("style: " + style);
            System.out.println();
        }

        System.out.println();
    }

    public static void runQuery3() throws SQLException {
        System.out.print("Enter property id: ");
        int pid = s.nextInt();

        // execute transcations view
        String view = "CREATE OR REPLACE VIEW transactions AS\n"
                + "(SELECT * FROM house_trans)\n"
                + "UNION\n"
                + "(SELECT * FROM land_trans);";
        stmt.executeUpdate(view);

        // execute buying_client view
        view = "CREATE OR REPLACE VIEW buying_client AS\n"
                + "(SELECT c.cid, fname, lname\n"
                + "FROM house_buyer h JOIN client c\n"
                + "ON h.cid = c.cid)\n"
                + "UNION\n"
                + "(SELECT c.cid, fname, lname\n"
                + "FROM land_buyer l JOIN client c\n"
                + "ON l.cid = c.cid);";
        stmt.executeUpdate(view);

        // execute selling_client view
        view = "CREATE OR REPLACE VIEW selling_client AS\n"
                + "SELECT c.cid, fname, lname \n"
                + "FROM seller s JOIN client c\n"
                + "ON s.cid = c.cid;";
        stmt.executeUpdate(view);

        // execute query
        String query = "SELECT DISTINCT pid, bc.cid as buy_cid, bc.fname, bc.lname, sc.cid as sell_cid, sc.fname, sc.lname, r1.rid as buy_rid, r1.fname, r1.lname, r2.rid as sell_rid, r2.fname, r2.lname\n"
                + "FROM transactions t,  buying_client bc, selling_client sc, realtor r1, realtor r2\n"
                + "WHERE t.buy_cid = bc.cid\n"
                + "AND t.sell_cid = sc.cid\n"
                + "AND (t.buy_rid = r1.rid OR t.buy_rid = r1.rid)\n"
                + "AND (t.sell_rid = r2.rid OR t.sell_cid = r2.rid);";
        ResultSet result = stmt.executeQuery(query);

        System.out.println("\nAll individuals involved in property " + pid + ":\n");
        while (result.next()) {

        }

        System.out.println();
    }

    public static void runQuery4() throws SQLException {
        // ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 4...");
        // while (result.next()) {
        // do something
        // }

        System.out.println();
    }

    public static void runQuery5() throws SQLException {
        // ResultSet result = stmt.executeQuery(query);

        System.out.println("Prcessing results from query 5...");
        // while (result.next()) {
        // do something
        // }

        System.out.println();
    }
}
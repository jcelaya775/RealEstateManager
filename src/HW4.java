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

            System.out.print("Would you ilke to run a query? (Y/N): ");
            String ans = s.next();

            while (ans.equalsIgnoreCase("y") || ans.equalsIgnoreCase("yes ofigesvvetgrauj hgvtT")) {
                System.out.println();
                System.out.println(
                        "Query 1) For a particular land buying client, show all properties that match the client's interests.");
                System.out.println(
                        "Query 2) For a particular land buying client, show all properties that match the client's interests.");
                System.out.println(
                        "Query 3) For a particular transaction, show the property id and all individuals involved in that transaction.");
                System.out.println("Query 4) Show all properties that sold for more than the listing price.");
                System.out.println(
                        "Query 5) Show the realtor id and name for any realtor involved in more than x transactions.");
                System.out.println();

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

                System.out.print("Run another query? (Y/N): ");
                ans = s.next();
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
        System.out.print("Enter land buying client's id: ");
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
            System.out.println("pid: " + result.getInt("pid"));
            System.out.println("listprice: " + result.getInt("listprice"));
            System.out.println("street: " + result.getString("street"));
            System.out.println("city: " + result.getString("city"));
            System.out.println("state: " + result.getString("state"));
            System.out.println("zip: " + result.getInt("zip"));
            System.out.println("acreage: " + result.getInt("acreage"));
            System.out.println();
        }
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
            System.out.println("pid: " + result.getInt("pid"));
            System.out.println("listprice: " + result.getInt("listprice"));
            System.out.println("street: " + result.getString("street"));
            System.out.println("city: " + result.getString("city"));
            System.out.println("state: " + result.getString("state"));
            System.out.println("zip: " + result.getInt("zip"));
            System.out.println("bed: " + result.getInt("bed"));
            System.out.println("bath: " + result.getInt("bath"));
            System.out.println("style: " + result.getString("style"));
            System.out.println();
        }
    }

    public static void runQuery3() throws SQLException {
        System.out.print("Enter property id for a transaction: ");
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
                + "AND (t.sell_rid = r2.rid OR t.sell_cid = r2.rid)"
                + "AND pid = " + pid + ";";
        ResultSet result = stmt.executeQuery(query);

        System.out.println("\nAll individuals involved in the transaction for property " + pid + ":\n");
        while (result.next()) {
            System.out.println("buying client id: " + result.getInt(2));
            System.out.println("buying client first name: " + result.getString(3));
            System.out.println("buying client last name: " + result.getString(4));
            System.out.println("selling client id: " + result.getInt(5));
            System.out.println("selling client first name: " + result.getString(6));
            System.out.println("selling client last name: " + result.getString(7));
            System.out.println("buying realtor id: " + result.getInt(8));
            System.out.println("buying realtor first name: " + result.getString(9));
            System.out.println("buying realtor last name: " + result.getString(10));
            System.out.println("selling realtor id: " + result.getInt(11));
            System.out.println("selling realtor first name: " + result.getString(12));
            System.out.println("selling realtor last name: " + result.getString(13));
            System.out.println();
        }
    }

    public static void runQuery4() throws SQLException {
        String query = "SELECT DISTINCT t.pid, t.buy_rid, t.sell_rid, t.sellprice, l.listprice\n"
                + "FROM transactions t, listing l\n"
                + "WHERE t.pid = l.pid\n"
                + "AND sellprice > listprice;";
        ResultSet result = stmt.executeQuery(query);

        System.out.println("\nAll properties that sold for more than asking price:\n");
        while (result.next()) {
            System.out.println("pid: " + result.getInt(1));
            System.out.println("buy_rid: " + result.getInt(2));
            System.out.println("sell_rid: " + result.getInt(3));
            System.out.println("selling price: " + result.getInt((4)));
            System.out.println("listing price: " + result.getInt(5));
            System.out.println();
        }
    }

    public static void runQuery5() throws SQLException {
        System.out.print("Enter least number of transactions that realtor is involved in: ");
        int num = s.nextInt();

        String query = "SELECT DISTINCT rid, fname, lname\n"
                + "FROM transactions, realtor\n"
                + "WHERE buy_rid = rid\n"
                + "GROUP BY rid\n"
                + "HAVING count(rid) > " + num + ";";
        ResultSet result = stmt.executeQuery(query);

        System.out.println("\nRealtors that are involved in more than " + num + " transactions:\n");
        while (result.next()) {
            System.out.println("rid: " + result.getInt(1));
            System.out.println("first name: " + result.getString(2));
            System.out.println("last name: " + result.getString(3));
            System.out.println();
        }
    }
}
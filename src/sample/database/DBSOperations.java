package sample.database;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;

public class DBSOperations {

    public static void add(Connection connection, Order order) {
        try {

            ArrayList<String> commands = new ArrayList<>();

            commands.add("Insert into address values(DEFAULT," + order.getPerson().getAddress().getHouseNumber() + ",'" + order.getPerson().getAddress().getStreet() + "','" + order.getPerson().getAddress().getCity() + "');");
            commands.add("Insert into person values(DEFAULT,'" + order.getPerson().getName() + "','" + order.getPerson().getSurname() + "','" + order.getPerson().getEmail() + "','" + order.getPerson().getPhoneNumber() + "',(SELECT max(id) from address));");
            commands.add("Insert into vehicle values(DEFAULT,'" + order.getVehicle().getPlateNumber() + "',(SELECT id from vehicletype where description='" + order.getVehicle().getVehicleType() + "'));"); //CHECK ENUM ?
            commands.add("Insert into orders values(DEFAULT, '" + order.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + "',(SELECT max(id) from person), (SELECT id from timeRange where timeOfStart='" + order.getTimeRange().format(DateTimeFormatter.ISO_LOCAL_TIME) + "'), (SELECT max(id) from vehicle))");

            for (String problem : order.getProblems()) {
                commands.add("Insert into orders_has_problem values((SELECT max(id) from person),(SELECT id from problem where description='" + problem + "'));");
            }

            for (int i = 0; i < commands.size(); i++) {
                PreparedStatement statement = connection.prepareStatement(commands.get(i));
                statement.execute();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addOld(Connection con, String name, int age){
        try {
            Statement statement = con.createStatement();
            String s = "Insert into person values('" + name + "'," + age + " );";
            PreparedStatement preparedStmt = con.prepareStatement(s);
            preparedStmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
/*
    public static void printAll(Connection con){
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT jmeno, vek FROM person");

            while (rs.next()) {
                String s = rs.getString("jmeno");
                int i = rs.getInt("vek");
                System.out.println(s + " " + i);
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public static ArrayList<String> getList(Connection con){
        ArrayList<String> export = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT jmeno, vek FROM person");

            while (rs.next()) {
                String s = rs.getString("jmeno");
                export.add(s);
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return export;
    }
*/
}

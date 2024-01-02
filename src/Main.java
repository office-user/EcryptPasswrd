import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Scanner;

public class Main {


    //Global Variables
   private static final String dbUrl = "jdbc:mysql://127.0.0.1:3306/my_database";
    private static final  String dbUser = "root";
    private static final String dbPassword = "";


    public static String hashPasswordM(String plainPasswordText){

        String hashedPassword = BCrypt.hashpw(plainPasswordText, BCrypt.gensalt(12));
        return hashedPassword;
    }


    public static void storeInDatabase(String name, String username, String password){

        try{
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            //Create Statement Object.
            Statement statement = connection.createStatement();
            System.out.println("Connection Establish with Database");



         // Create Query for Insert Data in Database.
          String query = "INSERT INTO users(name,username,password) VALUES ('"+name+"','"+username+"','"+password+"')";

          //For Insert Data in Database we use executeUpdate Method.
          statement.executeUpdate(query);

          // To Get User Value
          String getQuery= "SELECT password FROM users WHERE id='"+1+"'";
          ResultSet rs = statement.executeQuery(getQuery);
          while(rs.next()) {
              System.out.println(rs.getString("password"));
          }

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    //main Method
    public static void main(String[] args) {
        // Tell Us Your Name

        // Create Scanner Object
        Scanner sc = new Scanner(System.in);



        // Ask User to Enter Name
        System.out.print("Enter Your Name: ");
        String ns = sc.nextLine();


        //Ask User to Enter there Username
        System.out.print("Enter Your Username: ");
        String username = sc.nextLine();

        //Ask User to Enter there Password
        System.out.print("Enter Your Password: ");
        String password = sc.nextLine();


        //Create HashPassword Method who Return Value
        String hashPassword = hashPasswordM(password);
        System.out.println(hashPassword);


        //Store In Database
        //Create a Method for Database to Store Value
        storeInDatabase(ns,username,hashPassword);



    }

}


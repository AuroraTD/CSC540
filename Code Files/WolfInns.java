/**
 * 
 * CSC 540
 * 
 * Wolf Inns
 * Hotel Management Database System
 * 
 * Team C
 * Abhay Soni                   (asoni3)
 * Aurora Tiffany-Davis         (attiffan)
 * Manjusha Trilochan Awasthi   (mawasth)
 * Samantha Scoggins            (smscoggi)
 *
 */

// Imports
import java.util.Scanner;
import java.sql.*;

// WolfInns class
public class WolfInns {
    
    // Declare constants - commands
    private static final String CMD_REPORT_HOTELS = "Hotels";
    private static final String CMD_REPORT_ROOMS = "Rooms";
    private static final String CMD_REPORT_STAFF = "Staff";
    private static final String CMD_REPORT_CUSTOMERS = "Customers";
    private static final String CMD_REPORT_STAYS = "Stays";
    private static final String CMD_REPORT_SERVICES = "Services";
    private static final String CMD_QUIT = "Quit";
    
    // Declare constants - connection parameters
    private static final String JDBC_URL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/smscoggi";
    private static final String JDBC_USER = "smscoggi";
    private static final String JDBC_PASSWORD = "200157888";
    
    // Declare variables
    private static Connection jdbc_connection;
    private static Statement jdbc_statement;
    private static ResultSet jdbc_result;
    
    /** 
     * Print available commands
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void printAvailableCommands() {
        
        try {
            
            System.out.println("");
            System.out.println("Available commands:");
            System.out.println("");
            System.out.println("'" + CMD_REPORT_HOTELS + "'");
            System.out.println("\t- run report on hotels");
            System.out.println("'" + CMD_REPORT_ROOMS + "'");
            System.out.println("\t- run report on rooms");
            System.out.println("'" + CMD_REPORT_STAFF + "'");
            System.out.println("\t- run report on staff");
            System.out.println("'" + CMD_REPORT_CUSTOMERS + "'");
            System.out.println("\t- run report on customers");
            System.out.println("'" + CMD_REPORT_STAYS + "'");
            System.out.println("\t- run report on stays");
            System.out.println("'" + CMD_REPORT_SERVICES + "'");
            System.out.println("\t- run report on service types");
            System.out.println("'" + CMD_QUIT + "'");
            System.out.println("\t- exit the program");
            System.out.println("");
        
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Establish a connection to the database
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void connectToDatabase() {
        
        try {
            
            // Get JDBC driver
            Class.forName("org.mariadb.jdbc.Driver");
            
            // Initialize JDBC stuff to null
            jdbc_connection = null;
            jdbc_statement = null;
            jdbc_result = null;
            
            // Establish connection
            jdbc_connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            jdbc_statement = jdbc_connection.createStatement();
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
        
    /** 
     * Drop database tables, if they exist
     * (to support running program many times)
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void dropExistingTables() {

        try {
            
            // Declare variables
            DatabaseMetaData metaData;
            String tableName;

            /* Find out what tables already exist
             * https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html
             */
            metaData = jdbc_connection.getMetaData();
            jdbc_result = metaData.getTables(null, null, "%", null);
            
            // Go through and delete each existing table
            while (jdbc_result.next()) {
                // Get table name
                tableName = jdbc_result.getString(3);
                /* Drop disable foreign key checks to avoid complaint
                 * https://stackoverflow.com/questions/4120482/foreign-key-problem-in-jdbc
                 */
                jdbc_statement.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
                // Drop table
                jdbc_statement.executeUpdate("DROP TABLE " + tableName);
                // Re-establish normal foreign key checks
                jdbc_statement.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
            }
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Create database tables
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void createTables() {
        
        try {
            
            // Drop all tables that already exist, so that we may run repeatedly
            dropExistingTables();
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            /* Create table: Customers
             * phone number to be entered as 10 digit int ex: 9993335555
             * requires "BIGINT" instead of just "INT"
             * SSN to be entered as 9 digit int ex: 100101000
             * requires "BIGINT" instead of just "INT"
             */
            jdbc_statement.executeUpdate("CREATE TABLE Customers ("+
                "SSN BIGINT NOT NULL,"+
                "Name VARCHAR(255) NOT NULL,"+
                "DOB DATE NOT NULL,"+
                "PhoneNum BIGINT NOT NULL,"+
                "Email VARCHAR(255) NOT NULL,"+
                "PRIMARY KEY (SSN)"+
            ")");

            // Create table: ServiceTypes
            jdbc_statement.executeUpdate("CREATE TABLE ServiceTypes ("+
                "Name VARCHAR(255) NOT NULL,"+
                "Cost INT NOT NULL,"+
                "PRIMARY KEY (Name)"+
            ")");

            /* Create table: Staff
             * phone number to be entered as 10 digit int ex: 9993335555
             * requires "BIGINT" instead of just "INT"
             */
            jdbc_statement.executeUpdate("CREATE TABLE Staff ("+
                "ID INT NOT NULL AUTO_INCREMENT,"+
                "Name VARCHAR(225) NOT NULL,"+
                "DOB DATE NOT NULL,"+
                "JobTitle VARCHAR(225),"+
                "Dep VARCHAR(225) NOT NULL,"+
                "PhoneNum BIGINT NOT NULL,"+
                "Address VARCHAR(225) NOT NULL,"+
                "HotelID INT,"+
                "PRIMARY KEY(ID)"+
            ")");

            /* Create table: Hotels
             * this is done after Staff table is created
             * because manager ID references Staff table
             * phone number to be entered as 10 digit int ex: 9993335555
             * requires "BIGINT" instead of just "INT"
             */
            jdbc_statement.executeUpdate("CREATE TABLE Hotels ("+
                "ID INT NOT NULL AUTO_INCREMENT,"+
                "Name VARCHAR(225) NOT NULL,"+
                "StreetAddress VARCHAR(225) NOT NULL,"+
                "City VARCHAR(225) NOT NULL,"+
                "State VARCHAR(225) NOT NULL,"+
                "PhoneNum BIGINT Not Null,"+
                "ManagerID INT Not Null,"+
                "Primary Key(ID),"+
                "CONSTRAINT UC_HACS UNIQUE (StreetAddress, City, State),"+
                "CONSTRAINT UC_HPN UNIQUE (PhoneNum),"+
                "CONSTRAINT UC_HMID UNIQUE (ManagerID),"+
                "CONSTRAINT FK_HMID FOREIGN KEY (ManagerID) REFERENCES Staff(ID)"+
            ")");

            /* Alter table: Staff
             * needs to happen after Hotels table is created
             * because hotel ID references Hotels table
             */
            jdbc_statement.executeUpdate("ALTER TABLE Staff "+
                "ADD CONSTRAINT FK_STAFFHID "+
                "FOREIGN KEY (HotelID) "+
                "REFERENCES Hotels(ID)"
            ); 

            // Create table: Rooms
            jdbc_statement.executeUpdate("CREATE TABLE Rooms ("+
                "RoomNum INT NOT NULL,"+
                "HotelID INT NOT NULL,"+
                "Category VARCHAR(225) NOT NULL,"+
                "MaxOcc INT NOT NULL,"+
                "NightlyRate DOUBLE NOT NULL,"+
                "DRSStaff INT,"+
                "DCStaff INT,"+
                "PRIMARY KEY(RoomNum,HotelID),"+
                "CONSTRAINT FK_ROOMHID FOREIGN KEY (HotelID) REFERENCES Hotels(ID),"+
                "CONSTRAINT FK_ROOMDRSID FOREIGN KEY (DRSStaff) REFERENCES Staff(ID),"+
                "CONSTRAINT FK_ROOMDCID FOREIGN KEY (DCStaff) REFERENCES Staff(ID)"+
            ")");

            // Create table: Stays
            jdbc_statement.executeUpdate("CREATE TABLE Stays ("+
                "ID INT NOT NULL AUTO_INCREMENT,"+
                "StartDate DATE NOT NULL,"+
                "CheckInTime TIME NOT NULL,"+
                "RoomNum INT NOT NULL,"+
                "HotelID INT NOT NULL,"+
                "CustomerSSN BIGINT NOT NULL,"+
                "NumGuests INT NOT NULL,"+
                "CheckOutTime TIME,"+
                "EndDate DATE,"+
                "PaymentMethod ENUM('CASH','CARD') NOT NULL,"+
                "CardType ENUM('VISA','MASTERCARD','HOTEL'),"+
                "CardNumber INT,"+
                "BillingAddress VARCHAR(255) NOT NULL,"+
                "PRIMARY KEY(ID),"+
                "CONSTRAINT UC_STAYKEY UNIQUE (StartDate, CheckInTime,RoomNum, HotelID),"+
                "CONSTRAINT FK_STAYRID FOREIGN KEY (RoomNum) REFERENCES Rooms(RoomNum),"+
                "CONSTRAINT FK_STAYHID FOREIGN KEY (HotelID) REFERENCES Rooms(HotelID),"+
                "CONSTRAINT FK_STAYCSSN FOREIGN KEY (CustomerSSN) REFERENCES Customers(SSN)"+
            ")");

            // Create table: Provided
            jdbc_statement.executeUpdate("CREATE TABLE Provided ("+
                "ID INT NOT NULL AUTO_INCREMENT,"+
                "StayID INT NOT NULL,"+
                "StaffID INT NOT NULL,"+
                "ServiceName VARCHAR(255) NOT NULL,"+
                "PRIMARY KEY(ID),"+
                "CONSTRAINT FK_PROVSTAYID FOREIGN KEY (StayID) REFERENCES Stays(ID),"+
                "CONSTRAINT FK_PROVSTAFFID FOREIGN KEY (StaffID) REFERENCES Staff(ID),"+
                "CONSTRAINT FK_PROVSERV FOREIGN KEY (ServiceName) REFERENCES ServiceTypes(Name)"+
            ")");
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Populate Customers Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void populateCustomersTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            // Populate table
            jdbc_statement.executeUpdate("INSERT INTO Customers "+
                "(SSN, Name, DOB, PhoneNum, Email)" +
                "VALUES (100101000, 'John Smith', '1980-01-01', 9198675309, 'john.smith@gmail.com')"
            );
            // TODO: update this with actual chosen customers (the above is just an example)
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Populate ServiceTypes Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void populateServiceTypesTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            // Populate table
            // TODO: add inserts based on pattern established in populateCustomersTable
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Populate Staff Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void populateStaffTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            // Populate table
            // TODO: add inserts based on pattern established in populateCustomersTable
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Populate Hotels Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void populateHotelsTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            // Populate table
            // TODO: add inserts based on pattern established in populateCustomersTable
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Populate Rooms Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void populateRoomsTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            // Populate table
            // TODO: add inserts based on pattern established in populateCustomersTable
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Populate Stays Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void populateStaysTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            // Populate table
            // TODO: add inserts based on pattern established in populateCustomersTable
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Populate Provided Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void populateProvidedTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            // Populate table
            // TODO: add inserts based on pattern established in populateCustomersTable
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Print all results from a given table
     * 
     * Arguments -  tableName - The table to print out
     * Return -     None
     */
    public static void printEntireTable(String tableName) {

        try {
            
            System.out.println("\nEntries in the " + tableName + " table:\n");
            jdbc_result = jdbc_statement.executeQuery("SELECT * FROM " + tableName);
            printQueryResultSet(jdbc_result);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Print query result set
     * Modified from, but inspired by: https://coderwall.com/p/609ppa/printing-the-result-of-resultset
     * 
     * Arguments -  resultSetToPrint -  The result set to print
     * Return -     None
     */
    public static void printQueryResultSet(ResultSet resultSetToPrint) {
        
        try {
            
            // Declare variables
            ResultSetMetaData metaData;
            String tupleValue;
            int numColumns;
            int i;

            // Is there anything useful in the result set?
            if (jdbc_result.next()) {
                // Get metadata
                metaData = jdbc_result.getMetaData();
                numColumns = metaData.getColumnCount();
                // Go through the result set tuple by tuple
                do {
                    for (i = 1; i <= numColumns; i++) {
                        if (i > 1) System.out.print(",  ");
                        tupleValue = jdbc_result.getString(i);
                        System.out.print(metaData.getColumnName(i) + ": " + tupleValue);
                    }
                    System.out.println("");
                } while(jdbc_result.next());
                // Print an extra empty line just for readability
                System.out.println("");
            } else {
                // Tell the user that the result set is empty
                System.out.println("(no results)\n");
            }
            
            /* TODO: make this print prettier
             * Right now it prints like this (for a Customer):
             * SSN: 100101000,  Name: John Smith,  DOB: 1980-01-01,  PhoneNum: 9198675309,  Email: john.smith@gmail.com
            */
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }

    /* MAIN function
     * 
     * Welcomes the user, states available commands, listens to and acts on user commands
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void main(String[] args) {
        
        try {
        
            // Declare local variables
            boolean quit = false;
            
            // Print welcome
            System.out.println("\nWelcome to Wolf Inns Hotel Management System");
            
            // Connect to database
            System.out.println("\nConnecting to database...");
            connectToDatabase();
            
            // Create tables
            System.out.println("\nCreating tables...");
            createTables();
            
            // Populate tables
            System.out.println("\nPopulating tables...");
            populateCustomersTable();
            populateServiceTypesTable();
            populateStaffTable();
            populateHotelsTable();
            populateRoomsTable();
            populateStaysTable();
            
            // Print available commands
            printAvailableCommands();
            
            // Watch for user input
            Scanner scanner = new Scanner(System.in);
            while (quit == false) {
                System.out.print("> ");
                String command = scanner.next();
                switch (command) {
                    case CMD_REPORT_HOTELS:
                        printEntireTable("Hotels");
                        break;
                    case CMD_REPORT_ROOMS:
                        printEntireTable("Rooms");
                        break;
                    case CMD_REPORT_STAFF:
                        printEntireTable("Staff");
                        break;
                    case CMD_REPORT_CUSTOMERS:
                        printEntireTable("Customers");
                        break;
                    case CMD_REPORT_STAYS:
                        printEntireTable("Stays");
                        break;
                    case CMD_REPORT_SERVICES:
                        printEntireTable("ServiceTypes");
                        break;
                    case CMD_QUIT:
                        quit = true;
                        break;
                    default:
                        // Remind the user about what commands are available
                        System.out.println("\nCommand not recognized");
                        printAvailableCommands();
                        break;
                }
            }
            
            // Clean up
            scanner.close();
        
        }
        catch (Throwable err) {
            err.printStackTrace();
        }

    }

}

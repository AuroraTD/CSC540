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
                "CardNumber BIGINT,"+
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
            
            System.out.println("Tables created successfully!");
            
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
            
            // Populating data for Customers
            jdbc_statement.executeUpdate("INSERT INTO Customers"+
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+
    								"(555284568, 'Isaac Gray', '1982-11-12', '9194562158', 'issac.gray@gmail.com');");
            jdbc_statement.executeUpdate("INSERT INTO Customers"+ 
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+ 
    								"(111038548, 'Jay Sharp', '1956-07-09', '9191237548', 'jay.sharp@gmail.com');"); 
            jdbc_statement.executeUpdate("INSERT INTO Customers "+ 
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+ 
    								"(222075875, 'Jenson Lee', '1968-09-25', '9194563217', 'jenson.lee@gmail.com');");
            jdbc_statement.executeUpdate("INSERT INTO Customers "+ 
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+ 
    								" (333127845, 'Benjamin Cooke', '1964-01-07', '9191256324', 'benjamin.cooke@gmail.com');");
            jdbc_statement.executeUpdate("INSERT INTO Customers "+ 
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+ 
    								" (444167216, 'Joe Bradley', '1954-04-07', '9194587569', 'joe.bradley@gmail.com');");
            jdbc_statement.executeUpdate("INSERT INTO Customers "+ 
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+ 
    								" (666034568, 'Conor Stone', '1975-06-04', '9194567216', 'conor.stone@gmail.com');");
            jdbc_statement.executeUpdate("INSERT INTO Customers "+ 
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+ 
    								" (777021654, 'Elizabeth Davis', '1964-07-26', '9195432187', 'elizabeth.davis@gmail.com');");
            jdbc_statement.executeUpdate("INSERT INTO Customers "+ 
    								"(SSN, Name, DOB, PhoneNum, Email) VALUES "+ 
    								" (888091545, 'Natasha Moore', '1966-08-14', '9194562347', 'natasha.moore@gmail.com');");
            
            System.out.println("Customers table loaded!");
    		
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
            
            // Populating data for ServiceTypes
            jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
					"(Name, Cost) VALUES "+
					"('PHONE_BILL', 25);");
			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
								"(Name, Cost) VALUES "+
								" ('DRY_CLEANING', 20);");
			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
								"(Name, Cost) VALUES "+
								" ('GYM', 35);");
			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
								"(Name, Cost) VALUES "+
								" ('ROOM_SERVICE', 25);");
			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
								"(Name, Cost) VALUES "+
								" ('CATERING', 50);");
			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
								"(Name, Cost) VALUES "+
								" ('SPECIAL_SERVICE', 40);");
			
			System.out.println("ServiceTypes table loaded!");

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
            
            // Populating data for Staff
    		// Staff for Hotel#1
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (1, 'Zoe Holmes', '1980-10-02', 'Manager', 'Manager', 8141113134, '123 6th St. Melbourne, FL 32904', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff  "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (2, 'Katelyn Weeks', '1970-04-20', 'Front Desk Representative', 'Front Desk Representative', 6926641058, '123 6th St. Melbourne, FL 32904', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (3, 'Abby Huffman', '1990-12-14', 'Room Service', 'Room Service', 6738742135, '71 Pilgrim Avenue Chevy Chase, MD 20815', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (4, 'Oliver Gibson', '1985-05-12', 'Room Service', 'Room Service', 1515218329, '70 Bowman St. South Windsor, CT 06074', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (5, 'Michael Day', '1983-02-25', 'Catering', 'Catering', 3294931245, '4 Goldfield Rd. Honolulu, HI 96815', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (6, 'David Adams', '1985-01-17', 'Dry Cleaning', 'Dry Cleaning', 9194153214, '44 Shirley Ave. West Chicago, IL 60185', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (7, 'Ishaan Goodman', '1993-04-19', 'Gym', 'Gym', 5203201425, '514 S. Magnolia St. Orlando, FL 32806', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (8, 'Nicholas Read', '1981-01-14', 'Catering', 'Catering', 2564132017, '236 Pumpkin Hill Court Leesburg, VA 20175', NULL);");
    		
    		// Staff for Hotel#2
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (9, 'Dominic Mitchell', '1971-03-13', 'Manager', 'Manager', 2922497845, '7005 South Franklin St. Somerset, NJ 08873', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (10, 'Oliver Lucas', '1961-05-11', 'Front Desk Representative', 'Front Desk Representative', 2519881245, '7 Edgefield St. Augusta, GA 30906', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (11, 'Molly Thomas', '1987-07-10', 'Room Service', 'Room Service', 5425871245, '541 S. Holly Street Norcross, GA 30092', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (12, 'Caitlin Cole', '1989-08-15', 'Catering', 'Catering', 4997845612, '7 Ivy Ave. Traverse City, MI 49684', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (13, 'Victoria Medina', '1989-02-04', 'Dry Cleaning', 'Dry Cleaning', 1341702154, '8221 Trenton St. Jamestown, NY 14701', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (14, 'Will Rollins', '1982-07-06', 'Gym', 'Gym', 7071264587, '346 Beacon Lane Quakertown, PA 18951', NULL);");
    		
    		// Staff for Hotel#3
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (15, 'Masen Shepard', '1983-01-09', 'Manager', 'Manager', 8995412364, '3 Fulton Ave. Bountiful, UT 84010', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (16, 'Willow Roberts', '1987-02-08', 'Front Desk Representative', 'Front Desk Representative', 5535531245, '7868 N. Lees Creek Street Chandler, AZ 85224', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (17, 'Maddison Davies', '1981-03-07', 'Room Service', 'Room Service', 6784561245, '61 New Road Ithaca, NY 14850', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (18, 'Crystal Barr', '1989-04-06', 'Catering', 'Catering', 4591247845, '9094 6th Ave. Macomb, MI 48042', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (19, 'Dayana Tyson', '1980-05-05', 'Dry Cleaning', 'Dry Cleaning', 4072134587, '837 W. 10th St. Jonesboro, GA 30236', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (20, 'Tommy Perry', '1979-06-04', 'Gym', 'Gym', 5774812456, '785 Bohemia Street Jupiter, FL 33458', NULL);");
    		
    		// Staff for Hotel#4
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (21, 'Joshua Burke', '1972-01-10', 'Manager', 'Manager', 1245214521, '8947 Briarwood St. Baldwin, NY 11510', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (22, 'Bobby Matthews', '1982-02-14', 'Front Desk Representative', 'Front Desk Representative', 5771812456, '25 W. Dogwood Lane Bemidji, MN 56601', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (23, 'Pedro Cohen', '1983-04-24', 'Room Service', 'Room Service', 8774812456, '9708 Brickyard Ave. Elyria, OH 44035', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (24, 'Alessandro Beck', '1981-06-12', 'Catering', 'Catering', 5774812452, '682 Glen Ridge St. Leesburg, VA 20175', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (25, 'Emily Petty', '1984-08-19', 'Dry Cleaning', 'Dry Cleaning', 5772812456, '7604 Courtland St. Easley, SC 29640', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (26, 'Rudy Cole', '1972-01-09', 'Gym', 'Gym', 5774812856, '37 Marconi Drive Owensboro, KY 42301', NULL);");
    		
    		// Staff for Hotel#5
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (27, 'Blair Ball', '1981-01-10', 'Manager', 'Manager', 8854124568, '551 New Saddle Ave. Cape Coral, FL 33904', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (28, 'Billy Lopez', '1982-05-11', 'Front Desk Representative', 'Front Desk Representative', 5124562123, '99 Miles Road Danbury, CT 06810', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (29, 'Lee Ward', '1983-06-12', 'Room Service', 'Room Service', 9209124562, '959 S. Tailwater St. Ridgewood, NJ 07450', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (30, 'Ryan Parker', '1972-08-13', 'Catering', 'Catering', 1183024152, '157 State Dr. Attleboro, MA 02703', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (31, 'Glen Elliott', '1971-09-14', 'Catering', 'Catering', 6502134785, '9775 Clinton Dr. Thornton, CO 80241', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (32, 'Ash Harrison', '1977-02-15', 'Dry Cleaning', 'Dry Cleaning', 9192451365, '9924 Jefferson Ave. Plainfield, NJ 07060', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (33, 'Leslie Little', '1979-12-16', 'Gym', 'Gym', 9192014512, '7371 Pin Oak St. Dalton, GA 30721', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (34, 'Mason West', '1970-10-17', 'Gym', 'Gym', 6501231245, '798 W. Valley Farms Lane Saint Petersburg, FL 33702', NULL);");
    		
    		//Staff for Hotel#6
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (35, 'Riley Dawson', '1975-01-09', 'Manager', 'Manager', 1183021245, '898 Ocean Court Hilliard, OH 43026', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (36, 'Gabe Howard', '1987-03-01', 'Front Desk Representative', 'Front Desk Representative', 6501421523, '914 Edgefield Dr. Hartselle, AL 35640', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (37, 'Jessie Nielsen', '1982-06-02', 'Room Service', 'Room Service', 7574124587, '7973 Edgewood Road Gallatin, TN 37066', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (38, 'Gabe Carlson', '1983-08-03', 'Room Service', 'Room Service', 5771245865, '339 Pine Lane Tampa, FL 33604', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (39, 'Carmen Lee', '1976-01-04', 'Catering', 'Catering', 9885234562, '120 Longbranch Drive Port Richey, FL 34668', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (40, 'Mell Tran', '1979-06-05', 'Dry Cleaning', 'Dry Cleaning', 9162451245, '32 Pearl St. Peoria, IL 61604', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (41, 'Leslie Cook', '1970-10-08', 'Gym', 'Gym', 6501245126, '59 W. High Ridge Street Iowa City, IA 52240', NULL);");
    		
    		//Staff for Hotel#7
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (42, 'Rory Burke', '1971-01-05', 'Manager', 'Manager', 7702653764, '9273 Ridge Drive Winter Springs, FL 32708', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (43, 'Macy Fuller', '1972-02-07', 'Front Desk Representative', 'Front Desk Representative', 7485612345, '676 Myers Street Baldwin, NY 11510', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (44, 'Megan Lloyd', '1973-03-01', 'Room Service', 'Room Service', 7221452315, '849 George Lane Park Ridge, IL 60068', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (45, 'Grace Francis', '1974-04-09', 'Catering', 'Catering', 3425612345, '282 Old York Court Mechanicsburg, PA 17050', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (46, 'Macy Fuller', '1975-05-02', 'Dry Cleaning', 'Dry Cleaning', 4665127845, '57 Shadow Brook St. Hudson, NH 03051', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (47, 'Cory Hoover', '1976-06-12', 'Gym', 'Gym', 9252210735, '892 Roosevelt Street Ithaca, NY 14850', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (48, 'Sam Graham', '1977-07-25', 'Gym', 'Gym', 7226251245, '262 Bayberry St. Dorchester, MA 02125', NULL);");
    		
    		//Staff for Hotel#8
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (49, 'Charlie Adams', '1981-01-01', 'Manager', 'Manager', 6084254152, '9716 Glen Creek Dr. Newark, NJ 07103', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (50, 'Kiran West', '1985-02-02', 'Front Desk Representative', 'Front Desk Representative', 9623154125, '68 Smith Dr. Lexington, NC 27292', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (51, 'Franky John', '1986-03-03', 'Room Service', 'Room Service', 8748544152, '6 Shirley Road Fairborn, OH 45324', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (52, 'Charlie Bell', '1985-04-04', 'Room Service', 'Room Service', 9845124562, '66 Elm Street Jupiter, FL 33458', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (53, 'Jamie Young', '1986-06-05', 'Catering', 'Catering', 9892145214, '8111 Birch Hill Avenue Ravenna, OH 44266', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (54, 'Jackie Miller', '1978-08-06', 'Dry Cleaning', 'Dry Cleaning', 9795486234, '9895 Redwood Court Glenview, IL 60025', NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Staff "+
    									" (ID, Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID ) VALUES "+
    									" (55, 'Jude Cole', '1979-03-07', 'Gym', 'Gym', 9195642251, '8512 Cambridge Ave. Lake In The Hills, IL 60156', NULL);");
         
    		System.out.println("Staff table loaded!");
    		
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
            
            // Populating data for Hotels
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (1, 'The Plaza', '768 5th Ave', 'New York', 'NY', 9194152368, 1);");
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (2, 'DoubleTree', '4810 Page Creek Ln', 'Raleigh', 'NC', 9192012364, 9);");
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (3, 'Ramada', '1520 Blue Ridge Rd', 'Raleigh', 'NC', 9190174632, 15);");
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (4, 'Embassy Suites', '201 Harrison Oaks Blvd', 'Raleigh', 'NC', 6502137942, 21);");
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (5, 'Four Seasons', '57 E 57th St,', 'New York', 'NY', 6501236874, 27);");
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (6, 'The Pierre', '2 E 61st St', 'New York', 'NY', 6501836874, 35);");
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (7, 'Fairfield Inn & Suites', '10040 Sellona St', 'Raleigh', 'NC', 6501236074, 42);");
    		jdbc_statement.executeUpdate("INSERT INTO Hotels "+
    									" (ID, Name, StreetAddress, City, State, PhoneNum, ManagerID) VALUES " +
    									" (8, 'Mandarin Oriental', '80 Columbus Cir', 'New York', 'NY', 6591236874, 49);");
    		
    		System.out.println("Hotels table loaded!");
            
            // End transaction
            jdbc_connection.commit();
            jdbc_connection.setAutoCommit(true);
            
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
        
    }
    
    /** 
     * Update Staff Table
     * 
     * Arguments -  None
     * Return -     None
     */
    public static void updateHotelIdForStaff() {
    	
    	 try {
             
             // Start transaction
             jdbc_connection.setAutoCommit(false);
             
             // Update(Assign) HotelId for Staff 
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 1 WHERE ID >=1 AND ID <=8;");
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 2 WHERE ID >=9 AND ID <=14;");
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 3 WHERE ID >=15 AND ID <=20;");
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 4 WHERE ID >=21 AND ID <=26;");
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 5 WHERE ID >=27 AND ID <=34;");
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 6 WHERE ID >=35 AND ID <=41;");
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 7 WHERE ID >=42 AND ID <=48;");
			jdbc_statement.executeUpdate("UPDATE Staff SET HotelID = 8 WHERE ID >=49 AND ID <=55;");
			
			System.out.println("Hotel Id's updated for Staff!");
             
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
            
            // Populating data for Rooms
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 1, 'ECONOMY', 3, 150, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 1, 'PRESIDENTIAL_SUITE', 4, 450, 3, 5);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (3, 1, 'EXECUTIVE_SUITE', 4, 300, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 2, 'DELUXE', 3, 200, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 2, 'ECONOMY', 3, 125, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (3, 2, 'EXECUTIVE_SUITE', 4, 250, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 3, 'PRESIDENTIAL_SUITE', 3, 550, 17, 18);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 3, 'ECONOMY', 2, 350, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (3, 3, 'DELUXE', 3, 450, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 4, 'ECONOMY', 4, 100, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 4, 'EXECUTIVE_SUITE', 4, 250, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 5, 'DELUXE', 3, 300, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 5, 'EXECUTIVE_SUITE', 4, 400, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (3, 5, 'PRESIDENTIAL_SUITE', 4, 500, 29, 30);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 6, 'ECONOMY', 2, 220, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 6, 'DELUXE', 4, 350, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 7, 'ECONOMY', 2, 125, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 7, 'EXECUTIVE_SUITE', 4, 400, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (1, 8, 'ECONOMY', 2, 200, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (2, 8, 'DELUXE', 3, 250, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (3, 8, 'EXECUTIVE_SUITE', 3, 300, NULL, NULL);");
    		jdbc_statement.executeUpdate("INSERT INTO Rooms "+
    									" (RoomNum, HotelID, Category, MaxOcc, NightlyRate, DRSStaff, DCStaff) VALUES " +
    									" (4, 8, 'PRESIDENTIAL_SUITE', 4, 450, 51, 53);"); 
            System.out.println("Rooms Table loaded!");
            
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
            
            // Populating data for Stays
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (1, '2018-01-12', '20:10:00', 1, 1, 555284568, 3, '10:00:00', '2018-01-20', 'CARD', 'VISA', '4400123454126587', '7178 Kent St. Enterprise, AL 36330');");
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (2, '2018-02-15', '10:20:00', 3, 2, 111038548, 2, '08:00:00', '2018-02-18', 'CASH', NULL, NULL, '754 East Walt Whitman St. Hopkins, MN 55343');");
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (3, '2018-03-01', '15:00:00', 1, 3, 222075875, 1, '13:00:00', '2018-03-05', 'CARD', 'HOTEL', '1100214521684512', '178 Shadow Brook St. West Chicago, IL 60185');");
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (4, '2018-02-20', '07:00:00', 2, 4, 333127845, 4, '15:00:00', '2018-02-27', 'CARD', 'MASTERCARD', '4400124565874591', '802B Studebaker Drive Clinton Township, MI 48035');");
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (5, '2018-03-05', '11:00:00', 3, 5, 444167216, 4, '08:00:00', '2018-03-12', 'CARD', 'VISA', '4400127465892145', '83 Inverness Court Longwood, FL 32779');");
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (6, '2018-03-01', '18:00:00', 1, 6, 666034568, 1, '23:00:00', '2018-03-01', 'CASH', NULL, NULL, '55 Livingston Ave. Selden, NY 11784');");
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (7, '2018-01-20', '06:00:00', 2, 7, 777021654, 3, '10:00:00', '2018-02-01', 'CARD', 'HOTEL', '1100214532567845', '87 Gregory Street Lawndale, CA 90260');");
    		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    								" (ID, StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    								" (8, '2018-02-14', '09:00:00', 4, 8, 888091545, 2, '10:00:00', '2018-02-18', 'CARD', 'VISA', '4400178498564512', '34 Hall Ave. Cranberry Twp, PA 16066');"); 
    	    System.out.println("Stays table loaded!");
            
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
            
            // Populating data for Provided
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (1, 1, 7, 'GYM')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (2, 1, 5, 'CATERING')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (3, 2, 11, 'ROOM_SERVICE')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (4, 3, 19, 'DRY_CLEANING')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (5, 4, 26, 'GYM')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (6, 5, 32, 'DRY_CLEANING')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (7, 6, 38, 'ROOM_SERVICE')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (8, 7, 48, 'GYM')");
    		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    									" (ID, StayID, StaffID, ServiceName) VALUES " +
    									" (9, 8, 54, 'DRY_CLEANING')");
    		System.out.println("Provided table loaded!");
    		
            
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
            updateHotelIdForStaff();
            populateRoomsTable();
            populateStaysTable();
            populateProvidedTable();
            
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

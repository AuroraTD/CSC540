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
import java.text.NumberFormat;
import java.util.ArrayList;

// WolfInns class
public class WolfInns {
    
    // DECLARATIONS
    
    // Declare constants - commands
    
    private static final String CMD_MAIN =                  "MAIN";
    private static final String CMD_QUIT =                  "QUIT";
    private static final String CMD_FRONTDESK =             "FRONTDESK";
    private static final String CMD_BILLING =               "BILLING";
    private static final String CMD_REPORTS =               "REPORTS";
    private static final String CMD_MANAGE =                "MANAGE";
    
    private static final String CMD_FRONTDESK_AVAILABLE =   "AVAILABILITY";
    private static final String CMD_FRONTDESK_ASSIGN =      "ASSIGNROOM";
    
    private static final String CMD_BILLING_GENERATE =      "BILLFORSTAY";
    
    private static final String CMD_REPORT_REVENUE =        "REVENUE";
    private static final String CMD_REPORT_HOTELS =         "HOTELS";
    private static final String CMD_REPORT_ROOMS =          "ROOMS";
    private static final String CMD_REPORT_STAFF =          "STAFF";
    private static final String CMD_REPORT_CUSTOMERS =      "CUSTOMERS";
    private static final String CMD_REPORT_STAYS =          "STAYS";
    private static final String CMD_REPORT_SERVICES =       "SERVICES";
    private static final String CMD_REPORT_PROVIDED =       "PROVIDED";
    
    private static final String CMD_MANAGE_HOTEL_ADD =      "ADDHOTEL";
    private static final String CMD_MANAGE_HOTEL_UPDATE =   "UPDATEHOTEL";
    private static final String CMD_MANAGE_HOTEL_DELETE =   "DELETEHOTEL";
    private static final String CMD_MANAGE_STAFF_ADD =      "ADDSTAFF";
    private static final String CMD_MANAGE_STAFF_UPDATE =   "UPDATESTAFF";
    private static final String CMD_MANAGE_STAFF_DELETE =   "DELETESTAFF";
    
    private static final String CMD_MANAGE_ROOM_ADD =       "ADDROOM";
    private static final String CMD_MANAGE_ROOM_UPDATE =    "UPDATEROOM";
    private static final String CMD_MANAGE_ROOM_DELETE =    "DELETEROOM"; 
    
    private static final String CMD_MANAGE_CUSTOMER_ADD =     "ADDCUSTOMER";
    private static final String CMD_MANAGE_CUSTOMER_UPDATE =  "UPDATECUSTOMER";
    private static final String CMD_MANAGE_CUSTOMER_DELETE =  "DELETECUSTOMER";
    
    // Declare constants - connection parameters
    private static final String JDBC_URL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/smscoggi";
    private static final String JDBC_USER = "smscoggi";
    private static final String JDBC_PASSWORD = "200157888";
    
    // Declare variables - high level
    private static Connection jdbc_connection;
    private static Statement jdbc_statement;
    private static ResultSet jdbc_result;
    private static String currentMenu;
    
    // Declare variables - prepared statements - HOTELS
    private static PreparedStatement jdbcPrep_insertNewHotel;
    private static PreparedStatement jdbcPrep_updateNewHotelManager;
    private static PreparedStatement jdbcPrep_udpateHotelName;
    private static PreparedStatement jdbcPrep_updateHotelStreetAddress;
    private static PreparedStatement jdbcPrep_updateHotelCity;
    private static PreparedStatement jdbcPrep_udpateHotelState;
    private static PreparedStatement jdbcPrep_updateHotelPhoneNum;
    private static PreparedStatement jdbcPrep_updateHotelManagerID;
    private static PreparedStatement jdbcPrep_demoteOldManager;
    private static PreparedStatement jdbcPrep_promoteNewManager;
    private static PreparedStatement jdbcPrep_getNewestHotelID;
    private static PreparedStatement jdbcPrep_getHotelSummaryForAddress;
    private static PreparedStatement jdbcPrep_getHotelSummaryForPhoneNumber;
    private static PreparedStatement jdbcPrep_getHotelSummaryForStaffMember;
    private static PreparedStatement jdbcPrep_getHotelByID;
    private static PreparedStatement jdbcPrep_deleteHotel;  
    
    // Declare variables - prepared statements - ROOMS
    private static PreparedStatement jdbcPrep_insertNewRoom;
    private static PreparedStatement jdbcPrep_updateRoomCategory;
    private static PreparedStatement jdbcPrep_updateRoomMaxOccupancy;
    private static PreparedStatement jdbcPrep_updateRoomNightlyRate;
    private static PreparedStatement jdbcPrep_updateRoomDRSStaff;
    private static PreparedStatement jdbcPrep_updateRoomDCStaff;
    private static PreparedStatement jdbcPrep_deleteRoom;
    private static PreparedStatement jdbcPrep_isValidRoomNumber; 
    private static PreparedStatement jdbcPrep_isRoomCurrentlyOccupied;
    private static PreparedStatement jdbcPrep_isValidHotelId;
    private static PreparedStatement jdbcPrep_getRoomByHotelIDRoomNum; 
    private static PreparedStatement jdbcPrep_getOneExampleRoom;
    private static PreparedStatement jdbcPrep_assignDedicatedStaff;
    
    // Declare variables - prepared statements - STAFF
    private static PreparedStatement jdbcPrep_insertNewStaff;
    private static PreparedStatement jdbcPrep_getNewestStaffID;
    private static PreparedStatement jdbcPrep_updateStaffName;
    private static PreparedStatement jdbcPrep_updateStaffDOB;
    private static PreparedStatement jdbcPrep_updateStaffJobTitle;
    private static PreparedStatement jdbcPrep_updateStaffDepartment;
    private static PreparedStatement jdbcPrep_updateStaffPhoneNum;
    private static PreparedStatement jdbcPrep_updateStaffAddress;
    private static PreparedStatement jdbcPrep_updateStaffHotelID;
    private static PreparedStatement jdbcPrep_updateStaffRangeHotelID;
    private static PreparedStatement jdbcPrep_getStaffByID;
    private static PreparedStatement jdbcPrep_deleteStaff;  
    
    // Declare variables - prepared statements - Customers
    private static PreparedStatement jdbcPrep_insertNewCustomer;
    private static PreparedStatement jdbcPrep_updateCustomerName;
    private static PreparedStatement jdbcPrep_updateCustomerDateOfBirth;
    private static PreparedStatement jdbcPrep_updateCustomerPhoneNumber;
    private static PreparedStatement jdbcPrep_updateCustomerEmail;
    private static PreparedStatement jdbcPrep_deleteCustomer; 
    private static PreparedStatement jdbcPrep_getCustomerBySSN; 
    private static PreparedStatement jdbcPrep_isValidCustomer; 
    private static PreparedStatement jdbcPrep_isCustomerCurrentlyStaying;
    
    // Declare variables - prepared statements - Stays
    private static PreparedStatement jdbcPrep_assignRoom;
    private static PreparedStatement jdbcPrep_getNewestStay;
    
    /* Why is the scanner outside of any method?
     * See https://stackoverflow.com/questions/13042008/java-util-nosuchelementexception-scanner-reading-user-input
     */
    private static Scanner scanner;
    
    // SETUP
    
    /** 
     * Print available commands
     * 
     * Arguments -  menu -  The menu we are currently in (determines available commands).
     *                      For example main, reports, etc.
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/08/18 -  ATTD -  Add ability to print entire Provided table.
     *                  03/09/18 -  ATTD -  Add ability to delete a hotel.
     *                  03/11/18 -  ATTD -  Add ability to report revenue.
     *                  03/11/18 -  ATTD -  Add ability to generate bill for customer stay.
     *                  03/12/18 -  ATTD -  Add ability to delete a staff member.
     *                  03/23/18 -  ATTD -  Add ability to update basic information about a hotel.
											Use new general error handler. 
     *                  03/24/18 -  MTA  -  Added ability to support Manage task for Room i.e add, update and delete room 
     *                  03/24/18 -  ATTD -  Add ability to insert new staff member.
     *                  03/26/18 -  ATTD -  Add ability to update basic info about a staff member.
     *                  03/27/18 -  MTA  -  Add ability to add, update and delete customer.
     *                  03/27/18 -  ATTD -  Add ability to check room availability.
     *                  04/01/18 -  ATTD -  Add ability to assign a room to a customer.
     */
    public static void printAvailableCommands(String menu) {
        
        try {
            
            System.out.println("");
            System.out.println(menu + " Menu available commands:");
            System.out.println("");
            
            switch (menu) {
                case CMD_MAIN:
                    System.out.println("'" + CMD_FRONTDESK + "'");
                    System.out.println("\t- perform front-desk tasks");
                    System.out.println("'" + CMD_BILLING + "'");
                    System.out.println("\t- bill customers");
                    System.out.println("'" + CMD_REPORTS + "'");
                    System.out.println("\t- run reports");
                    System.out.println("'" + CMD_MANAGE + "'");
                    System.out.println("\t- manage the hotel chain (add hotels, etc)");
                    System.out.println("'" + CMD_QUIT + "'");
                    System.out.println("\t- exit the program");
                    System.out.println("");
                    break;
                case CMD_FRONTDESK:
                    System.out.println("'" + CMD_FRONTDESK_AVAILABLE + "'");
                    System.out.println("\t- check room availability");
                    System.out.println("'" + CMD_FRONTDESK_ASSIGN + "'");
                    System.out.println("\t- assign a room to a customer");
                    System.out.println("'" + CMD_MAIN + "'");
                    System.out.println("\t- go back to the main menu");
                    System.out.println("");
                    break;
                case CMD_BILLING:
                    System.out.println("'" + CMD_BILLING_GENERATE + "'");
                    System.out.println("\t- generate a bill and an itemized receipt for a customer stay");
                    System.out.println("'" + CMD_MAIN + "'");
                    System.out.println("\t- go back to the main menu");
                    System.out.println("");
                    break;
                case CMD_REPORTS:
                    System.out.println("'" + CMD_REPORT_REVENUE + "'");
                    System.out.println("\t- run report on a hotel's revenue during a given date range");
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
                    System.out.println("'" + CMD_REPORT_PROVIDED + "'");
                    System.out.println("\t- run report on services provided to guests");
                    System.out.println("'" + CMD_MAIN + "'");
                    System.out.println("\t- go back to the main menu");
                    System.out.println("");
                    break;
                case CMD_MANAGE:
                    System.out.println("'" + CMD_MANAGE_HOTEL_ADD + "'");
                    System.out.println("\t- add a hotel");
                    System.out.println("'" + CMD_MANAGE_HOTEL_UPDATE + "'");
                    System.out.println("\t- update information about a hotel");
                    System.out.println("'" + CMD_MANAGE_HOTEL_DELETE + "'");
                    System.out.println("\t- delete a hotel");
                    System.out.println("'" + CMD_MANAGE_STAFF_ADD + "'");
                    System.out.println("\t- add a staff member");
                    System.out.println("'" + CMD_MANAGE_STAFF_UPDATE + "'");
                    System.out.println("\t- update information about a staff member");
                    System.out.println("'" + CMD_MANAGE_STAFF_DELETE + "'");
                    System.out.println("\t- delete a staff member");
                    
                    System.out.println("'" + CMD_MANAGE_ROOM_ADD + "'");
                    System.out.println("\t- add a room");
                    System.out.println("'" + CMD_MANAGE_ROOM_UPDATE + "'");
                    System.out.println("\t- update details of the room");
                    System.out.println("'" + CMD_MANAGE_ROOM_DELETE + "'");
                    System.out.println("\t- delete a room");
                    
                    System.out.println("'" + CMD_MANAGE_CUSTOMER_ADD + "'");
                    System.out.println("\t- add a customer");
                    System.out.println("'" + CMD_MANAGE_CUSTOMER_UPDATE + "'");
                    System.out.println("\t- update details of the customer");
                    System.out.println("'" + CMD_MANAGE_CUSTOMER_DELETE + "'");
                    System.out.println("\t- delete a customer");
                    
                    System.out.println("'" + CMD_MAIN + "'");
                    System.out.println("\t- go back to the main menu");
                    System.out.println("");
                    break;
                default:
                    break;
            }
            

        
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Establish a connection to the database
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
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
            handleError(err);
        }
        
    }
    
    /** 
     * Create prepared statements
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/20/18 -  ATTD -  Created method.
     *                  03/21/18 -  ATTD -  Fix off-by-one error in updating new hotel manager.
     *                                      Add more prepared statements to use when inserting new hotel.
     *                  03/23/18 -  ATTD -  Add support for updating basic information about a hotel.
     *                                      Use new general error handler.
     *                  03/24/18 -  ATTD -  Add support for deleting a hotel.
     *                  03/24/18 -  ATTD -  Add support for adding a new staff member.
     *                  03/26/18 -  ATTD -  Add ability to update basic info about a staff member.
     *                  03/27/18 -  ATTD -  Add prepared statement for updating staff hotel ID by staff ID range.
     *                                      Use prepared statement to delete staff.
     *                                      Add ability to report one example room.
     *                  04/01/18 -  ATTD -  Add ability to assign a room to a customer.
     */
    public static void createPreparedStatements() {
        
        try {
            
            // Declare variables
            String reusedSQLVar;

            /* Insert new hotel
             * Indices to use when calling this prepared statement:
             * 1 - name
             * 2 - street address
             * 3 - city
             * 4 - state
             * 5 - phone number
             * 6 - manager ID
             * 7 - manager ID (again)
             */
            reusedSQLVar = 
                "INSERT INTO Hotels (Name, StreetAddress, City, State, PhoneNum, ManagerID) " + 
                "SELECT ?, ?, ?, ?, ?, ? " + 
                "FROM Staff " + 
                "WHERE " + 
                "Staff.ID = ? AND " + 
                "Staff.ID NOT IN (SELECT DCStaff FROM Rooms WHERE DCStaff IS NOT NULL) AND " + 
                "Staff.ID NOT IN (SELECT DRSStaff FROM Rooms WHERE DRSStaff IS NOT NULL);";
            jdbcPrep_insertNewHotel = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update new hotel manager
             * to give new manager correct job title and hotel assignment
             * intended to be called in same transaction as insertion of new hotel
             * therefore the insertion is not yet committed
             * therefore max ID needs incremented
             * Indices to use when calling this prepared statement: n/a
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET JobTitle = 'Manager', HotelID = (SELECT MAX(ID) FROM Hotels) " + 
                "WHERE " + 
                "ID = (SELECT ManagerID FROM Hotels WHERE ID = (SELECT MAX(ID) FROM Hotels));";
            jdbcPrep_updateNewHotelManager = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update hotel name
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel name
             * 2 -  hotel ID
             */
            reusedSQLVar = 
                "UPDATE Hotels " + 
                "SET Name = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_udpateHotelName = jdbc_connection.prepareStatement(reusedSQLVar);

            /* Update hotel street address
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel street address
             * 2 -  hotel ID
             */
            reusedSQLVar = 
                "UPDATE Hotels " + 
                "SET StreetAddress = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateHotelStreetAddress = jdbc_connection.prepareStatement(reusedSQLVar); 

            /* Update hotel city
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel city
             * 2 -  hotel ID
             */
            reusedSQLVar = 
                "UPDATE Hotels " + 
                "SET City = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateHotelCity = jdbc_connection.prepareStatement(reusedSQLVar); 

            /* Update hotel state
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel state
             * 2 -  hotel ID
             */
            reusedSQLVar = 
                "UPDATE Hotels " + 
                "SET State = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_udpateHotelState = jdbc_connection.prepareStatement(reusedSQLVar); 
            
            /* Update hotel phone number
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel phone number
             * 2 -  hotel ID
             */
            reusedSQLVar = 
                "UPDATE Hotels " + 
                "SET PhoneNum = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateHotelPhoneNum = jdbc_connection.prepareStatement(reusedSQLVar); 
            
            /* Update hotel managerID
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel manager ID
             * 2 -  hotel ID
             */
            reusedSQLVar = 
                "UPDATE Hotels " + 
                "SET ManagerID = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateHotelManagerID = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get one example room, just to show the user what the attributes to filter on are
             * Don't filter on DCStaff or DRSStaff, that doesn't really make sense for the front desk rep
             * Indices to use when calling this prepared statement: n/a
             */
            reusedSQLVar = 
                "SELECT RoomNum, HotelID, Category, MaxOcc, NightlyRate from Rooms LIMIT 1;";
            jdbcPrep_getOneExampleRoom = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Demote the old manager of a given hotel to front desk representative
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET JobTitle = 'Front Desk Representative' " + 
                "WHERE JobTitle = 'Manager' AND HotelID = ?;";
            jdbcPrep_demoteOldManager = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Promote a staff member to management of a hotel
             * Indices to use when calling this prepared statement: 
             * 1 -  hotel ID
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET JobTitle = 'Manager', HotelID = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_promoteNewManager = jdbc_connection.prepareStatement(reusedSQLVar);

            /* Get the ID of the newest hotel in the DB
             * Indices to use when calling this prepared statement: n/a
             */
            reusedSQLVar = "SELECT MAX(ID) FROM Hotels;";
            jdbcPrep_getNewestHotelID = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get a summary of info about the hotel which has a particular street address, city, state
             * Indices to use when calling this prepared statement:
             * 1 -  street address
             * 2 -  city
             * 3 -  state
             */
            reusedSQLVar = 
                "SELECT " + 
                "StreetAddress, City, State, ID AS HotelID, Name AS HotelName " + 
                "FROM Hotels WHERE StreetAddress = ? AND City = ? AND State = ?;";
            jdbcPrep_getHotelSummaryForAddress = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get a summary of info about the hotel which has a particular phone number
             * Indices to use when calling this prepared statement:
             * 1 -  phone number
             */
            reusedSQLVar = 
                "SELECT " + 
                "PhoneNum AS PhoneNumber, ID AS HotelID, Name AS HotelName " + 
                "FROM Hotels WHERE PhoneNum = ?;";
            jdbcPrep_getHotelSummaryForPhoneNumber = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get a summary of info about the hotel to which a staff member is assigned
             * Indices to use when calling this prepared statement:
             * 1 -  staff ID
             */
            reusedSQLVar = 
                "SELECT " + 
                "Staff.ID AS StaffID, Staff.Name AS StaffName, Hotels.ID AS HotelID, Hotels.Name AS HotelName " + 
                "FROM Staff, Hotels WHERE Staff.ID = ?" + 
                " AND Staff.HotelID = Hotels.ID;";
            jdbcPrep_getHotelSummaryForStaffMember = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get all values of a given tuple (by ID) from the Hotels table
             * Indices to use when calling this prepared statement:
             * 1 -  ID
             */
            reusedSQLVar = 
                "SELECT * FROM Hotels WHERE ID = ?;";
            jdbcPrep_getHotelByID = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Delete a hotel (by ID)
             * Any hotel, room, customer, staff member, or service type associated with a current guest stay may not be deleted
             * Indices to use when calling this prepared statement:
             * 1 -  ID
             */
            reusedSQLVar = 
                "DELETE FROM Hotels WHERE ID = ? AND ID NOT IN " + 
                "(SELECT HotelID FROM Stays WHERE CheckOutTime IS NULL OR EndDate IS NULL);";
            jdbcPrep_deleteHotel = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Insert new staff member
             * Indices to use when calling this prepared statement:
             * 1 - name
             * 2 - date of birth
             * 3 - job title
             * 4 - department
             * 5 - phone number
             * 6 - address
             * 7 - hotel ID
             */
            reusedSQLVar = 
                "INSERT INTO Staff (Name, DOB, JobTitle, Dep, PhoneNum, Address, HotelID) " + 
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
            jdbcPrep_insertNewStaff = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get the ID of the newest staff member in the DB
             * Indices to use when calling this prepared statement: n/a
             */
            reusedSQLVar = "SELECT MAX(ID) FROM Staff;";
            jdbcPrep_getNewestStaffID = jdbc_connection.prepareStatement(reusedSQLVar);

            /* Update staff member name
             * Indices to use when calling this prepared statement: 
             * 1 -  staff name
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET Name = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateStaffName = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update staff member date of birth
             * Indices to use when calling this prepared statement: 
             * 1 -  staff date of birth
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET DOB = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateStaffDOB = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update staff member job title
             * Any staff member currently dedicated to serving a presidential suite may not have their job title changed
             * Since we set DCStaff and DRSStaff to NULL when a room is released, we needn't look at the Stays table
             * Indices to use when calling this prepared statement: 
             * 1 -  staff job title
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET JobTitle = ? " + 
                "WHERE ID = ? AND " + 
                "ID NOT IN (SELECT DCStaff FROM Rooms WHERE DCStaff IS NOT NULL) AND " + 
                "ID NOT IN (SELECT DRSStaff FROM Rooms WHERE DRSStaff IS NOT NULL);";
            jdbcPrep_updateStaffJobTitle = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update staff member department
             * Indices to use when calling this prepared statement: 
             * 1 -  staff department
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET Dep = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateStaffDepartment = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update staff member phone number
             * Indices to use when calling this prepared statement: 
             * 1 -  staff phone number
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET PhoneNum = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateStaffPhoneNum = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update staff member address
             * Indices to use when calling this prepared statement: 
             * 1 -  staff address
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET Address = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateStaffAddress = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update staff member assigned hotel ID
             * Indices to use when calling this prepared statement: 
             * 1 -  staff hotel ID
             * 2 -  staff ID
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET HotelID = ? " + 
                "WHERE ID = ?;";
            jdbcPrep_updateStaffHotelID = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Update staff member assigned hotel ID, by range of staff ID
             * Indices to use when calling this prepared statement: 
             * 1 -  staff hotel ID
             * 2 -  staff ID min (inclusive)
             * 3 -  staff ID max (inclusive)
             */
            reusedSQLVar = 
                "UPDATE Staff " + 
                "SET HotelID = ? WHERE " + 
                "ID >= ? AND ID <= ?;";
            jdbcPrep_updateStaffRangeHotelID = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get all values of a given tuple (by ID) from the Staff table
             * Indices to use when calling this prepared statement:
             * 1 -  ID
             */
            reusedSQLVar = 
                "SELECT * FROM Staff WHERE ID = ?;";
            jdbcPrep_getStaffByID = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Delete a staff member (by ID)
             * Any hotel, room, customer, staff member, or service type associated with a current guest stay may not be deleted
             * Since we set DCStaff and DRSStaff to NULL when a room is released, we needn't look at the Stays table
             * Indices to use when calling this prepared statement:
             * 1 -  ID
             */
            reusedSQLVar = 
                "DELETE FROM Staff " + 
                "WHERE ID = ? AND " + 
                "ID NOT IN (SELECT DCStaff FROM Rooms WHERE DCStaff IS NOT NULL) AND " + 
                "ID NOT IN (SELECT DRSStaff FROM Rooms WHERE DRSStaff IS NOT NULL);";
            jdbcPrep_deleteStaff = jdbc_connection.prepareStatement(reusedSQLVar);
            
            // Add room
            reusedSQLVar = "INSERT INTO Rooms (RoomNum, HotelID, Category, MaxOcc, NightlyRate) VALUES (? , ?, ?, ?, ?); ";
        	jdbcPrep_insertNewRoom = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update room category
            reusedSQLVar = "UPDATE Rooms SET Category = ? WHERE RoomNum = ? AND hotelID = ?;";
        	jdbcPrep_updateRoomCategory = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update room max occupancy
            reusedSQLVar = "UPDATE Rooms SET MaxOcc = ? WHERE RoomNum = ? AND hotelID = ?;";
        	jdbcPrep_updateRoomMaxOccupancy = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update room nightly rate
            reusedSQLVar = "UPDATE Rooms SET NightlyRate = ? WHERE RoomNum = ? AND hotelID = ?;";
        	jdbcPrep_updateRoomNightlyRate = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update room Dedicated Room Staff
            reusedSQLVar = "UPDATE Rooms SET DRSStaff = ? WHERE RoomNum = ? AND hotelID = ?;";
        	jdbcPrep_updateRoomDRSStaff = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update room Dedicated Catering Staff
            reusedSQLVar = "UPDATE Rooms SET DCStaff = ? WHERE RoomNum = ? AND hotelID = ?;";
        	jdbcPrep_updateRoomDCStaff = jdbc_connection.prepareStatement(reusedSQLVar);
        	 
        	// Delete room
        	reusedSQLVar = "DELETE FROM Rooms WHERE RoomNum = ? AND hotelID = ?; ";
        	jdbcPrep_deleteRoom = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Check if room number belongs to hotel 
        	reusedSQLVar = "SELECT COUNT(*) AS CNT FROM Rooms WHERE hotelID = ? AND RoomNum = ? ;";			 
			jdbcPrep_isValidRoomNumber = jdbc_connection.prepareStatement(reusedSQLVar); 
			
			// Check if room is currently occupied
			reusedSQLVar = "SELECT COUNT(*) AS CNT FROM Stays WHERE hotelID = ? AND RoomNum = ? AND (CheckOutTime IS NULL OR EndDate IS NULL);";			 
			jdbcPrep_isRoomCurrentlyOccupied = jdbc_connection.prepareStatement(reusedSQLVar); 

			// Check if hotel exists in the database
			reusedSQLVar = "SELECT COUNT(*) AS CNT FROM Hotels WHERE ID = ? ;"; 
			jdbcPrep_isValidHotelId = jdbc_connection.prepareStatement(reusedSQLVar); 
			
			// Report room details by room number and hotel id
			reusedSQLVar = "SELECT * FROM Rooms WHERE RoomNum = ? AND hotelID = ?; ";
        	jdbcPrep_getRoomByHotelIDRoomNum = jdbc_connection.prepareStatement(reusedSQLVar);
			 
            // Add customer
            reusedSQLVar = "INSERT INTO Customers (SSN, Name, DOB, PhoneNum, Email) VALUES (? , ?, ?, ?, ?); ";
        	jdbcPrep_insertNewCustomer = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Report customer by SSN
        	reusedSQLVar = "SELECT * FROM Customers WHERE SSN = ?";
        	jdbcPrep_getCustomerBySSN = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update Customer Name
        	reusedSQLVar = "UPDATE Customers SET Name = ? WHERE SSN = ?; ";
        	jdbcPrep_updateCustomerName = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update Customer Date Of Birth
        	reusedSQLVar = "UPDATE Customers SET DOB = ? WHERE SSN = ?; ";
        	jdbcPrep_updateCustomerDateOfBirth = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update Customer Phone Number
        	reusedSQLVar = "UPDATE Customers SET PhoneNum = ? WHERE SSN = ?; ";
        	jdbcPrep_updateCustomerPhoneNumber = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Update Customer Email
        	reusedSQLVar = "UPDATE Customers SET Email = ? WHERE SSN = ?; ";
        	jdbcPrep_updateCustomerEmail = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Check if customer exists in the database
        	reusedSQLVar = "SELECT COUNT(*) AS CNT FROM Customers WHERE SSN = ?";
        	jdbcPrep_isValidCustomer = jdbc_connection.prepareStatement(reusedSQLVar);  
        	
        	// Delete customer
        	reusedSQLVar = "DELETE FROM Customers WHERE SSN = ?; ";
        	jdbcPrep_deleteCustomer = jdbc_connection.prepareStatement(reusedSQLVar);
        	
        	// Check if customer is associated with current(ongoing) stay
        	reusedSQLVar = "SELECT COUNT(*) AS CNT FROM Stays WHERE CustomerSSN = ? AND (CheckOutTime IS NULL OR EndDate IS NULL);";
        	jdbcPrep_isCustomerCurrentlyStaying = jdbc_connection.prepareStatement(reusedSQLVar);
        	
            /* Assign a room to a customer
             * Enforce the presence of further billing information in the case that the customer chooses to pay with a credit card.  
             * This is enforced by using a SELECT statement to produce values to be inserted.  
             * If the WHERE clause evaluates to false then no values will be produced, and thus the insertion will not take place.
             * Indices to use when calling this prepared statement:
             * 1 -  Customer SSN
             * 2 -  Number of guests
             * 3 -  Payment method
             * 4 -  Card type
             * 5 -  Card number
             * 6 -  Billing address
             * 7 -  Room number
             * 8 -  Hotel ID
             * 9 -  Payment method (again)
             * 10 - Card type (again)
             * 11 - Card number (again)
             * 12 - Billing address (again)
             */
            reusedSQLVar = 
                "INSERT INTO Stays " + 
                "(StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, PaymentMethod, CardType, CardNumber, BillingAddress) " + 
                "SELECT CURDATE(), CURTIME(), RoomNum, HotelID, ?, ?, ?, ?, ?, ? " + 
                "FROM Rooms WHERE RoomNum = ? AND HotelID = ? AND (? <> 'CARD' OR (? IS NOT NULL AND ? IS NOT NULL AND ? IS NOT NULL));";
            jdbcPrep_assignRoom = jdbc_connection.prepareStatement(reusedSQLVar);

            /* Assign dedicated staff to a presidential suite
             * Get first available room service staff in the hotel
             * Get first available catering staff in the hotel
             * Make sure there is actually a customer stay assigned for this room
             * Indices to use when calling this prepared statement:
             * 1 -  Hotel ID
             * 2 -  Room number
             * 3 -  Hotel ID (again)
             * 4 -  Room number (again)
             * 5 -  Hotel ID (again)
             */
            reusedSQLVar = 
                "UPDATE Rooms SET " + 
                "DRSStaff = (SELECT MIN(ID) FROM (SELECT ID, HotelID FROM STAFF WHERE JobTitle = 'Room Service' AND HotelID = ? AND " + 
                "ID NOT IN (SELECT DRSStaff FROM Rooms WHERE DRSStaff IS NOT NULL))), " + 
                "DCStaff = (SELECT MIN(ID) FROM (SELECT ID, HotelID FROM STAFF WHERE JobTitle = 'Catering' AND HotelID = ? AND " + 
                "ID NOT IN (SELECT DCStaff FROM Rooms WHERE DCStaff IS NOT NULL))) " +
                "WHERE RoomNum = ? AND HotelID = ? AND EXISTS(SELECT * FROM Stays WHERE RoomNum = ? AND HotelID = ? AND EndDate IS NULL);";
            jdbcPrep_assignDedicatedStaff = jdbc_connection.prepareStatement(reusedSQLVar);
            
            /* Get the newest stay in the DB
             * Indices to use when calling this prepared statement: n/a
             */
            reusedSQLVar = "SELECT * FROM Stays WHERE ID >= ALL (SELECT ID FROM Stays);";
            jdbcPrep_getNewestStay = jdbc_connection.prepareStatement(reusedSQLVar);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
        
    // TABLE CREATION
    
    /** 
     * Drop database tables, if they exist
     * (to support running program many times)
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
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
            handleError(err);
        }
        
    }
    
    /** 
     * Create database tables
     * 
     * Note:    CHECK    
     *              Per https://dev.mysql.com/doc/refman/5.7/en/create-table.html,
     *              "The CHECK clause is parsed but ignored by all storage engines",
     *          ASSERTION
     *              Per https://stackoverflow.com/questions/34769321/unexplainable-mysql-error-when-trying-to-create-assertion
     *              "This list does not include CREATE ASSERTION, so MariaDB does not support this functionality"
     *          So unfortunately there are some data entry error checks that we must perform
     *          in the application rather than letting the DBMS do it for us
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/08/18 -  ATTD -  Changed state to CHAR(2).
     *                  03/09/18 -  ATTD -  Added on delete rules for foreign keys.
     *                  03/11/18 -  ATTD -  Added amount owed to Stays relation.
     *                  03/12/18 -  ATTD -  Changed Provided table to set staff ID to NULL when staff member is deleted.
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/14/18 -  ATTD -  Changed service type names to enum, to match project assumptions.
     *                  03/17/18 -  ATTD -  Billing address IS allowed be NULL (when payment method is not card) per team discussion.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/24/18 -  MTA -   Name the primary key constraints.
     */
    public static void createTables() {
        
        try {

            // Drop all tables that already exist, so that we may run repeatedly
            dropExistingTables();
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
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
                    "CONSTRAINT PK_CUSTOMERS PRIMARY KEY (SSN)"+
                ")");
    
                // Create table: ServiceTypes
                jdbc_statement.executeUpdate("CREATE TABLE ServiceTypes ("+
                    "Name ENUM('Phone','Dry Cleaning','Gym','Room Service','Catering','Special Request') NOT NULL,"+
                    "Cost INT NOT NULL,"+
                    "CONSTRAINT PK_SERVICE_TYPES PRIMARY KEY (Name)"+
                ")");
    
                /* Create table: Staff
                 * phone number to be entered as 10 digit int ex: 9993335555
                 * requires "BIGINT" instead of just "INT"
                 */
                jdbc_statement.executeUpdate("CREATE TABLE Staff ("+
                    "ID INT NOT NULL AUTO_INCREMENT,"+
                    "Name VARCHAR(255) NOT NULL,"+
                    "DOB DATE NOT NULL,"+
                    "JobTitle VARCHAR(255),"+
                    "Dep VARCHAR(255) NOT NULL,"+
                    "PhoneNum BIGINT NOT NULL,"+
                    "Address VARCHAR(255) NOT NULL,"+
                    "HotelID INT,"+
                    "CONSTRAINT PK_STAFF PRIMARY KEY(ID)"+
                ")");
    
                /* Create table: Hotels
                 * this is done after Staff table is created
                 * because manager ID references Staff table
                 * phone number to be entered as 10 digit int ex: 9993335555
                 * requires "BIGINT" instead of just "INT"
                 */
                jdbc_statement.executeUpdate("CREATE TABLE Hotels ("+
                    "ID INT NOT NULL AUTO_INCREMENT,"+
                    "Name VARCHAR(255) NOT NULL,"+
                    "StreetAddress VARCHAR(255) NOT NULL,"+
                    "City VARCHAR(255) NOT NULL,"+
                    "State CHAR(2) NOT NULL,"+
                    "PhoneNum BIGINT Not Null,"+
                    "ManagerID INT Not Null,"+
                    "CONSTRAINT PK_HOTELS Primary Key(ID),"+
                    "CONSTRAINT UC_HACS UNIQUE (StreetAddress, City, State),"+
                    "CONSTRAINT UC_HPN UNIQUE (PhoneNum),"+
                    "CONSTRAINT UC_HMID UNIQUE (ManagerID),"+
                    /* If a manager is deleted from the system and not replaced in the same transaction, no choice but to delete hotel
                     * A hotel cannot be without a manager
                     */
                    "CONSTRAINT FK_HMID FOREIGN KEY (ManagerID) REFERENCES Staff(ID) ON DELETE CASCADE"+
                ")");
    
                /* Alter table: Staff
                 * needs to happen after Hotels table is created
                 * because hotel ID references Hotels table
                 */
                jdbc_statement.executeUpdate("ALTER TABLE Staff "+
                    "ADD CONSTRAINT FK_STAFFHID "+
                     /* If a hotel is deleted, no need to delete the staff that work there,
                      * NULL is allowed (currently unassigned staff)
                      */
                    "FOREIGN KEY (HotelID) REFERENCES Hotels(ID) ON DELETE SET NULL"
                ); 
    
                // Create table: Rooms
                jdbc_statement.executeUpdate("CREATE TABLE Rooms ("+
                    "RoomNum INT NOT NULL,"+
                    "HotelID INT NOT NULL,"+
                    "Category VARCHAR(255) NOT NULL,"+
                    "MaxOcc INT NOT NULL,"+
                    "NightlyRate DOUBLE NOT NULL,"+
                    "DRSStaff INT,"+
                    "DCStaff INT,"+
                    "CONSTRAINT PK_ROOMS PRIMARY KEY(RoomNum,HotelID),"+
                    // If a hotel is deleted, then the rooms within it should also be deleted
                    "CONSTRAINT FK_ROOMHID FOREIGN KEY (HotelID) REFERENCES Hotels(ID) ON DELETE CASCADE,"+
                    /* If a staff member dedicated to a room is deleted by the end of a transaction
                     * then something has probably gone wrong, because that staff member should have been replaced
                     * to maintain continuous service
                     * Nonetheless, not appropriate to delete the room in this case
                     * NULL is allowed
                    */
                    "CONSTRAINT FK_ROOMDRSID FOREIGN KEY (DRSStaff) REFERENCES Staff(ID) ON DELETE SET NULL,"+
                    "CONSTRAINT FK_ROOMDCID FOREIGN KEY (DCStaff) REFERENCES Staff(ID) ON DELETE SET NULL"+
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
                    "AmountOwed DOUBLE,"+
                    "PaymentMethod ENUM('CASH','CARD') NOT NULL,"+
                    "CardType ENUM('VISA','MASTERCARD','HOTEL'),"+
                    "CardNumber BIGINT,"+
                    "BillingAddress VARCHAR(255),"+
                    "CONSTRAINT PK_STAYS PRIMARY KEY(ID),"+
                    "CONSTRAINT UC_STAYKEY UNIQUE (StartDate, CheckInTime,RoomNum, HotelID),"+
                    /* If a room is deleted, then the stay no longer makes sense and should be deleted
                     * Need to handle room/hotel together as a single foreign key
                     * Because a foreign key is supposed to point to a unique tuple
                     * And room number by itself is not unique
                    */
                    "CONSTRAINT FK_STAYRID FOREIGN KEY (RoomNum, HotelID) REFERENCES Rooms(RoomNum, HotelID) ON DELETE CASCADE,"+
                    // If a customer is deleted, then the stay no longer makes sense and should be deleted
                    "CONSTRAINT FK_STAYCSSN FOREIGN KEY (CustomerSSN) REFERENCES Customers(SSN) ON DELETE CASCADE"+
                ")");
    
                // Create table: Provided
                jdbc_statement.executeUpdate("CREATE TABLE Provided ("+
                    "ID INT NOT NULL AUTO_INCREMENT,"+
                    "StayID INT NOT NULL,"+
                    "StaffID INT,"+
                    "ServiceName ENUM('Phone','Dry Cleaning','Gym','Room Service','Catering','Special Request') NOT NULL,"+
                    "CONSTRAINT PK_PROVIDED PRIMARY KEY(ID),"+
                    // If a stay is deleted, then the service provided record no longer makes sense and should be deleted
                    "CONSTRAINT FK_PROVSTAYID FOREIGN KEY (StayID) REFERENCES Stays(ID) ON DELETE CASCADE,"+
                    // If a staff member is deleted, then the service provided record still makes sense but has staff ID as NULL
                    "CONSTRAINT FK_PROVSTAFFID FOREIGN KEY (StaffID) REFERENCES Staff(ID) ON DELETE SET NULL,"+
                    // If a service type is deleted, then the service provided record no longer makes sense and should be deleted
                    "CONSTRAINT FK_PROVSERV FOREIGN KEY (ServiceName) REFERENCES ServiceTypes(Name) ON DELETE CASCADE"+
                ")");
                
                // If success, commit
                jdbc_connection.commit();
                
                System.out.println("Tables created successfully!");
            
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    // TABLE POPULATION 
    
    /** 
     * Populate Customers Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/07/18 -  MTA -   Populated method.
     *                  03/08/18 -  ATTD -  Shifted some string constants purely for readability (no functional changes).
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void populateCustomersTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
                // Populating data for Customers
            	addCustomer("555284568", "Isaac Gray", "1982-11-12", "9194562158", "issac.gray@gmail.com", false); 
            	addCustomer("111038548", "Jay Sharp", "1956-07-09", "9191237548", "jay.sharp@gmail.com", false);
            	addCustomer("222075875", "Jenson Lee", "1968-09-25", "9194563217", "jenson.lee@gmail.com", false);
            	addCustomer("333127845", "Benjamin Cooke", "1964-01-07", "9191256324", "benjamin.cooke@gmail.com", false);  
            	addCustomer("444167216", "Joe Bradley", "1954-04-07", "9194587569", "joe.bradley@gmail.com", false);    
            	addCustomer("666034568", "Conor Stone", "1975-06-04", "9194567216", "conor.stone@gmail.com", false);
            	addCustomer("777021654", "Elizabeth Davis", "1964-07-26", "9195432187", "elizabeth.davis@gmail.com", false);
            	addCustomer("888091545", "Natasha Moore", "1966-08-14", "9194562347", "natasha.moore@gmail.com", false);
            	addCustomer("888092545", "Gary Vee", "1996-03-10", "9199237455", "gary.vee@gmail.com", false);                                         
                
                // If success, commit
                jdbc_connection.commit();
                
                System.out.println("Customers table loaded!");
    		
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Populate ServiceTypes Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/07/18 -  MTA -   Populated method.
     *                  03/08/18 -  ATTD -  Shifted some string constants purely for readability (no functional changes).
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/14/18 -  ATTD -  Changed service type names to match job titles, to make queries easier.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void populateServiceTypesTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
                // Populating data for ServiceTypes
                // TODO: use prepared statement instead
                jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
    				"(Name, Cost) VALUES "+
    				"('Phone', 25);");
    			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
    				"(Name, Cost) VALUES "+
    				"('Dry Cleaning', 20);");
    			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
    				"(Name, Cost) VALUES "+
    				"('Gym', 35);");
    			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
    				"(Name, Cost) VALUES "+
    				"('Room Service', 25);");
    			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
    				"(Name, Cost) VALUES "+
    				"('Catering', 50);");
    			jdbc_statement.executeUpdate("INSERT INTO ServiceTypes "+ 
    				"(Name, Cost) VALUES "+
    				"('Special Request', 40);");
    			
                // If success, commit
                jdbc_connection.commit();
    			
    			System.out.println("ServiceTypes table loaded!");

            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Populate Staff Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/07/18 -  MTA -   Populated method.
     *                  03/08/18 -  ATTD -  Shifted some string constants purely for readability (no functional changes).
     *                  03/09/18 -  ATTD -  Removed explicit setting of ID (this is auto incremented).
     *                  03/10/18 -  ATTD -  Removed explicit setting of hotel ID to null.
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/16/18 -  ATTD -  Changing departments to emphasize their meaninglessness.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/24/18 -  ATTD -  Call insert new staff member method, rather than having SQL directly in this method.
     */
    public static void populateStaffTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
                // Populating data for Staff
                
             // Staff for Hotel#1
                updateInsertStaff ("Zoe Holmes", "1980-10-02", "Manager", "A", 8141113134L, "123 6th St. Melbourne, FL 32904", 0, false);
                updateInsertStaff ("Katelyn Weeks", "1970-04-20", "Front Desk Representative", "B", 6926641058L, "123 6th St. Melbourne, FL 32904", 0, false);
                updateInsertStaff ("Abby Huffman", "1990-12-14", "Room Service", "C", 6738742135L, "71 Pilgrim Avenue Chevy Chase, MD 20815", 0, false);
                updateInsertStaff ("Oliver Gibson", "1985-05-12", "Room Service", "A", 1515218329L, "70 Bowman St. South Windsor, CT 06074", 0, false);
                updateInsertStaff ("Michael Day", "1983-02-25", "Catering", "B", 3294931245L, "4 Goldfield Rd. Honolulu, HI 96815", 0, false);
                updateInsertStaff ("David Adams", "1985-01-17", "Dry Cleaning", "C", 9194153214L, "44 Shirley Ave. West Chicago, IL 60185", 0, false);
                updateInsertStaff ("Ishaan Goodman", "1993-04-19", "Gym", "A", 5203201425L, "514 S. Magnolia St. Orlando, FL 32806", 0, false);
                updateInsertStaff ("Nicholas Read", "1981-01-14", "Catering", "B", 2564132017L, "236 Pumpkin Hill Court Leesburg, VA 20175", 0, false);

                // Staff for Hotel#2
                updateInsertStaff ("Dominic Mitchell", "1971-03-13", "Manager", "A", 2922497845L, "7005 South Franklin St. Somerset, NJ 08873", 0, false);
                updateInsertStaff ("Oliver Lucas", "1961-05-11", "Front Desk Representative", "A", 2519881245L, "7 Edgefield St. Augusta, GA 30906", 0, false);
                updateInsertStaff ("Molly Thomas", "1987-07-10", "Room Service", "B", 5425871245L, "541 S. Holly Street Norcross, GA 30092", 0, false);
                updateInsertStaff ("Caitlin Cole", "1989-08-15", "Catering", "B", 4997845612L, "7 Ivy Ave. Traverse City, MI 49684", 0, false);
                updateInsertStaff ("Victoria Medina", "1989-02-04", "Dry Cleaning", "C", 1341702154L, "8221 Trenton St. Jamestown, NY 14701", 0, false);
                updateInsertStaff ("Will Rollins", "1982-07-06", "Gym", "C", 7071264587L, "346 Beacon Lane Quakertown, PA 18951", 0, false);

                // Staff for Hotel#3
                updateInsertStaff ("Masen Shepard", "1983-01-09", "Manager", "A", 8995412364L, "3 Fulton Ave. Bountiful, UT 84010", 0, false);
                updateInsertStaff ("Willow Roberts", "1987-02-08", "Front Desk Representative", "A", 5535531245L, "7868 N. Lees Creek Street Chandler, AZ 85224", 0, false);
                updateInsertStaff ("Maddison Davies", "1981-03-07", "Room Service", "A", 6784561245L, "61 New Road Ithaca, NY 14850", 0, false);
                updateInsertStaff ("Crystal Barr", "1989-04-06", "Catering", "B", 4591247845L, "9094 6th Ave. Macomb, MI 48042", 0, false);
                updateInsertStaff ("Dayana Tyson", "1980-05-05", "Dry Cleaning", "B", 4072134587L, "837 W. 10th St. Jonesboro, GA 30236", 0, false);
                updateInsertStaff ("Tommy Perry", "1979-06-04", "Gym", "B", 5774812456L, "785 Bohemia Street Jupiter, FL 33458", 0, false);

                // Staff for Hotel#4
                updateInsertStaff ("Joshua Burke", "1972-01-10", "Manager", "C", 1245214521L, "8947 Briarwood St. Baldwin, NY 11510", 0, false);
                updateInsertStaff ("Bobby Matthews", "1982-02-14", "Front Desk Representative", "C", 5771812456L, "25 W. Dogwood Lane Bemidji, MN 56601", 0, false);
                updateInsertStaff ("Pedro Cohen", "1983-04-24", "Room Service", "C", 8774812456L, "9708 Brickyard Ave. Elyria, OH 44035", 0, false);
                updateInsertStaff ("Alessandro Beck", "1981-06-12", "Catering", "A", 5774812452L, "682 Glen Ridge St. Leesburg, VA 20175", 0, false);
                updateInsertStaff ("Emily Petty", "1984-08-19", "Dry Cleaning", "A", 5772812456L, "7604 Courtland St. Easley, SC 29640", 0, false);
                updateInsertStaff ("Rudy Cole", "1972-01-09", "Gym", "A", 5774812856L, "37 Marconi Drive Owensboro, KY 42301", 0, false);

                // Staff for Hotel#5
                updateInsertStaff ("Blair Ball", "1981-01-10", "Manager", "A", 8854124568L, "551 New Saddle Ave. Cape Coral, FL 33904", 0, false);
                updateInsertStaff ("Billy Lopez", "1982-05-11", "Front Desk Representative", "B", 5124562123L, "99 Miles Road Danbury, CT 06810", 0, false);
                updateInsertStaff ("Lee Ward", "1983-06-12", "Room Service", "B", 9209124562L, "959 S. Tailwater St. Ridgewood, NJ 07450", 0, false);
                updateInsertStaff ("Ryan Parker", "1972-08-13", "Catering", "B", 1183024152L, "157 State Dr. Attleboro, MA 02703", 0, false);
                updateInsertStaff ("Glen Elliott", "1971-09-14", "Catering", "B", 6502134785L, "9775 Clinton Dr. Thornton, CO 80241", 0, false);
                updateInsertStaff ("Ash Harrison", "1977-02-15", "Dry Cleaning", "C", 9192451365L, "9924 Jefferson Ave. Plainfield, NJ 07060", 0, false);
                updateInsertStaff ("Leslie Little", "1979-12-16", "Gym", "C", 9192014512L, "7371 Pin Oak St. Dalton, GA 30721", 0, false);
                updateInsertStaff ("Mason West", "1970-10-17", "Gym", "C", 6501231245L, "798 W. Valley Farms Lane Saint Petersburg, FL 33702", 0, false);

                //Staff for Hotel#6
                updateInsertStaff ("Riley Dawson", "1975-01-09", "Manager", "C", 1183021245L, "898 Ocean Court Hilliard, OH 43026", 0, false);
                updateInsertStaff ("Gabe Howard", "1987-03-01", "Front Desk Representative", "A", 6501421523L, "914 Edgefield Dr. Hartselle, AL 35640", 0, false);
                updateInsertStaff ("Jessie Nielsen", "1982-06-02", "Room Service", "A", 7574124587L, "7973 Edgewood Road Gallatin, TN 37066", 0, false);
                updateInsertStaff ("Gabe Carlson", "1983-08-03", "Room Service", "A", 5771245865L, "339 Pine Lane Tampa, FL 33604", 0, false);
                updateInsertStaff ("Carmen Lee", "1976-01-04", "Catering", "A", 9885234562L, "120 Longbranch Drive Port Richey, FL 34668", 0, false);
                updateInsertStaff ("Mell Tran", "1979-06-05", "Dry Cleaning", "A", 9162451245L, "32 Pearl St. Peoria, IL 61604", 0, false);
                updateInsertStaff ("Leslie Cook", "1970-10-08", "Gym", "B", 6501245126L, "59 W. High Ridge Street Iowa City, IA 52240", 0, false);

                //Staff for Hotel#7
                updateInsertStaff ("Rory Burke", "1971-01-05", "Manager", "B", 7702653764L, "9273 Ridge Drive Winter Springs, FL 32708", 0, false);
                updateInsertStaff ("Macy Fuller", "1972-02-07", "Front Desk Representative", "B", 7485612345L, "676 Myers Street Baldwin, NY 11510", 0, false);
                updateInsertStaff ("Megan Lloyd", "1973-03-01", "Room Service", "B", 7221452315L, "849 George Lane Park Ridge, IL 60068", 0, false);
                updateInsertStaff ("Grace Francis", "1974-04-09", "Catering", "B", 3425612345L, "282 Old York Court Mechanicsburg, PA 17050", 0, false);
                updateInsertStaff ("Macy Fuller", "1975-05-02", "Dry Cleaning", "C", 4665127845L, "57 Shadow Brook St. Hudson, NH 03051", 0, false);
                updateInsertStaff ("Cory Hoover", "1976-06-12", "Gym", "C", 9252210735L, "892 Roosevelt Street Ithaca, NY 14850", 0, false);
                updateInsertStaff ("Sam Graham", "1977-07-25", "Gym", "C", 7226251245L, "262 Bayberry St. Dorchester, MA 02125", 0, false);

                //Staff for Hotel#8
                updateInsertStaff ("Charlie Adams", "1981-01-01", "Manager", "C", 6084254152L, "9716 Glen Creek Dr. Newark, NJ 07103", 0, false);
                updateInsertStaff ("Kiran West", "1985-02-02", "Front Desk Representative", "C", 9623154125L, "68 Smith Dr. Lexington, NC 27292", 0, false);
                updateInsertStaff ("Franky John", "1986-03-03", "Room Service", "A", 8748544152L, "6 Shirley Road Fairborn, OH 45324", 0, false);
                updateInsertStaff ("Charlie Bell", "1985-04-04", "Room Service", "A", 9845124562L, "66 Elm Street Jupiter, FL 33458", 0, false);
                updateInsertStaff ("Jamie Young", "1986-06-05", "Catering", "A", 9892145214L, "8111 Birch Hill Avenue Ravenna, OH 44266", 0, false);
                updateInsertStaff ("Jackie Miller", "1978-08-06", "Dry Cleaning", "A", 9795486234L, "9895 Redwood Court Glenview, IL 60025", 0, false);
                updateInsertStaff ("Jude Cole", "1979-03-07", "Gym", "A", 9195642251L, "8512 Cambridge Ave. Lake In The Hills, IL 60156", 0, false);
        		
                // If success, commit
                jdbc_connection.commit();
             
        		System.out.println("Staff table loaded!");
    		
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Populate Hotels Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/07/18 -  MTA -   Populated method.
     *                  03/08/18 -  ATTD -  Shifted some string constants purely for readability (no functional changes).
     *                  03/09/18 -  ATTD -  Calling method to insert hotels (which also update's new manager's staff info).
     *                  03/11/18 -  ATTD -  Removed 9th hotel.
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void populateHotelsTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
                // Populating data for Hotels
                updateInsertHotel("The Plaza", "768 5th Ave", "New York", "NY", 9194152368L, 1, false);
                updateInsertHotel("DoubleTree", "4810 Page Creek Ln", "Raleigh", "NC", 9192012364L, 9, false);
        		updateInsertHotel("Ramada", "1520 Blue Ridge Rd", "Raleigh", "NC", 9190174632L, 15, false);
        		updateInsertHotel("Embassy Suites", "201 Harrison Oaks Blvd", "Raleigh", "NC", 6502137942L, 21, false);
        		updateInsertHotel("Four Seasons", "57 E 57th St", "New York", "NY", 6501236874L, 27, false);
        		updateInsertHotel("The Pierre", "2 E 61st St", "New York", "NY", 6501836874L, 35, false);
        		updateInsertHotel("Fairfield Inn & Suites", "0040 Sellona St", "Raleigh", "NC", 6501236074L, 42, false);
        		updateInsertHotel("Mandarin Oriental", "80 Columbus Cir", "New York", "NY", 6591236874L, 49, false);
        		
                // If success, commit
                jdbc_connection.commit();
                
        		System.out.println("Hotels table loaded!");
            
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Update Staff Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  MTA -   Created method.
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/27/18 -  ATTD -  Use prepared statement.
     */
    public static void updateHotelIdForStaff() {
    	
    	 try {
             
             // Start transaction
             jdbc_connection.setAutoCommit(false);
             
             try {
                 
                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 1);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 1);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 8);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();
                 
                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 2);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 9);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 14);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();

                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 3);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 15);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 20);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();
                 
                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 4);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 21);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 26);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();
                 
                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 5);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 27);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 34);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();

                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 6);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 35);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 41);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();

                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 7);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 42);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 48);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();
                 
                 jdbcPrep_updateStaffRangeHotelID.setInt(1, 8);
                 jdbcPrep_updateStaffRangeHotelID.setInt(2, 49);
                 jdbcPrep_updateStaffRangeHotelID.setInt(3, 55);
                 jdbcPrep_updateStaffRangeHotelID.executeUpdate();

                 // If success, commit
                 jdbc_connection.commit();
    			
                 System.out.println("Hotel Id's updated for Staff!");
             
             }
             catch (Throwable err) {
                 
                 // Handle error
                 handleError(err);
                 // Roll back the entire transaction
                 jdbc_connection.rollback();
                 
             }
             finally {
                 // Restore normal auto-commit mode
                 jdbc_connection.setAutoCommit(true);
             }
             
         }
         catch (Throwable err) {
             handleError(err);
         }
    }
        
    /** 
     * Populate Rooms Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/07/18 -  MTA -   Populated method.
     *                  03/08/18 -  ATTD -  Shifted some string constants purely for readability (no functional changes).
     *                  03/11/18 -  ATTD -  Changed dedicated presidential suite staff for hotel 9 to avoid staff conflicts.
     *                  03/11/18 -  ATTD -  Removed hotel 9.
     *                  03/12/18 -  ATTD -  Do not set DRSStaff and DCStaff to NULL explicitly (no need to).
     *                                      Do not set DRSStaff and DCStaff to non-NULL, for rooms not currently occupied.
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/24/18 -  MTA -   Using prepared statements to populate the data.
     */
    public static void populateRoomsTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
                // Populating data for Rooms
                
                // Hotel # 1   
        		addRoom(1, 1, "ECONOMY", 3, 150, false);
        		addRoom(2, 1, "PRESIDENTIAL_SUITE", 4, 450, false);
        		addRoom(3, 1, "EXECUTIVE_SUITE", 4, 300, false);
        		
        		// Hotel # 2 
        		addRoom(1, 2, "DELUXE", 3, 200, false);
        		addRoom(2, 2, "ECONOMY", 3, 125,false);
        		addRoom(3, 2, "EXECUTIVE_SUITE", 4, 250, false);
        		  
        		// Hotel # 3
        		addRoom(1, 3, "PRESIDENTIAL_SUITE", 3, 550, false);
        		addRoom(2, 3, "ECONOMY", 2, 350, false);
        		addRoom(3, 3, "DELUXE", 3, 450, false);
        		 
        		// Hotel # 4
        		addRoom(1, 4, "ECONOMY", 4, 100, false);
        		addRoom(2, 4, "EXECUTIVE_SUITE", 4, 250, false); 
        		 
        		// Hotel # 5
        		addRoom(1, 5, "DELUXE", 3, 300, false); 
        		addRoom(2, 5, "EXECUTIVE_SUITE", 4, 400, false); 
        		addRoom(3, 5, "PRESIDENTIAL_SUITE", 4, 500, false); 
        		 
        		// Hotel # 6
        		addRoom(1, 6, "ECONOMY", 2, 220, false); 
        		addRoom(2, 6, "DELUXE", 4, 350, false); 
        		 
        		// Hotel # 7
        		addRoom(1, 7, "ECONOMY", 2, 125, false); 
        		addRoom(2, 7, "EXECUTIVE_SUITE", 4, 400, false); 
        		 
        		// Hotel # 8
        		addRoom(1, 8, "ECONOMY", 2, 200, false); 
        		addRoom(2, 8, "DELUXE", 3, 250, false); 
        		addRoom(3, 8, "EXECUTIVE_SUITE", 3, 300, false); 
        		addRoom(4, 8, "PRESIDENTIAL_SUITE", 4, 450, false); 	
        		updateRoom(4, 8, "DRSStaff", "51", false);
        		updateRoom(4, 8, "DCStaff", "53", false);
        		
                // If success, commit
                jdbc_connection.commit();
    
                System.out.println("Rooms Table loaded!");
            
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Populate Stays Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/07/18 -  MTA -   Populated method.
     *                  03/08/18 -  ATTD -  Shifted some string constants purely for readability (no functional changes).
     *                  03/09/18 -  ATTD -  Removed explicit setting of ID (this is auto incremented).
     *                  03/11/18 -  ATTD -  Added amount owed to Stays relation.
     *                  03/11/18 -  ATTD -  Removed hotel 9.
     *                                      It was added to demonstrate that we can have NULL values for check out time, end date.
     *                                      This demonstration is instead added into some of the existing stays for hotels 1-8.
     *                                      The reason for this change is just to keep the amount of data to a reasonably low level 
     *                                      to help us think through queries and updates more quickly.
     *                  03/11/18 -  ATTD -  Do not set amount owed here (risk of calculating wrong when we calculate by hand).
     *                                      Instead, set by running billing report on the stay.
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/17/18 -  ATTD -  Billing address IS allowed be NULL (when payment method is not card) per team discussion.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void populateStaysTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
                // Stays where the guest has already checked out'
                // TODO: use prepared statement instead
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    				" ('2018-01-12', '20:10:00', 1, 1, 555284568, 3, '10:00:00', '2018-01-20', 'CARD', 'VISA', '4400123454126587', '7178 Kent St. Enterprise, AL 36330');");
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod) VALUES "+ 
    				" ('2018-02-15', '10:20:00', 3, 2, 111038548, 2, '08:00:00', '2018-02-18', 'CASH');");
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    				" ('2018-03-01', '15:00:00', 1, 3, 222075875, 1, '13:00:00', '2018-03-05', 'CARD', 'HOTEL', '1100214521684512', '178 Shadow Brook St. West Chicago, IL 60185');");
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    				" ('2018-02-20', '07:00:00', 2, 4, 333127845, 4, '15:00:00', '2018-02-27', 'CARD', 'MASTERCARD', '4400124565874591', '802B Studebaker Drive Clinton Township, MI 48035');");
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    				" ('2018-03-05', '11:00:00', 3, 5, 444167216, 4, '08:00:00', '2018-03-12', 'CARD', 'VISA', '4400127465892145', '83 Inverness Court Longwood, FL 32779');");
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, CheckOutTime, EndDate, PaymentMethod) VALUES "+ 
    				" ('2018-03-01', '18:00:00', 1, 6, 666034568, 1, '23:00:00', '2018-03-01', 'CASH');");
        		// Stays that are still going on
        		// TODO: use prepared statement instead
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    				" ('2018-01-20', '06:00:00', 2, 7, 777021654, 3, 'CARD', 'HOTEL', '1100214532567845', '87 Gregory Street Lawndale, CA 90260');");
        		jdbc_statement.executeUpdate("INSERT INTO Stays "+
    				" (StartDate, CheckInTime, RoomNum, HotelID, CustomerSSN, NumGuests, PaymentMethod, CardType, CardNumber, BillingAddress) VALUES "+ 
    				" ('2018-02-14', '09:00:00', 4, 8, 888091545, 2, 'CARD', 'VISA', '4400178498564512', '34 Hall Ave. Cranberry Twp, PA 16066');");
        		
                // If success, commit
                jdbc_connection.commit();
        		
                System.out.println("Stays table loaded!");
            
            }
            catch (Throwable err) {
                
                // Handle Error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Populate Provided Table
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/07/18 -  MTA -   Populated method.
     *                  03/08/18 -  ATTD -  Shifted some string constants purely for readability (no functional changes).
     *                  03/09/18 -  ATTD -  Removed explicit setting of ID (this is auto incremented).
     *                  03/11/18 -  ATTD -  Added another gym stay to stay ID 1, to more fully exercise ability to produce itemized receipt
     *                                      (itemized receipt needs to sum costs for all instances of the same service type).
     *                  03/12/18 -  ATTD -  Corrected JDBC transaction code (add try-catch).
     *                  03/14/18 -  ATTD -  Changed service type names to match job titles, to make queries easier.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void populateProvidedTable() {
        
        try {
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            
                // Populating data for Provided
                // TODO: use prepared statement instead
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (1, 7, 'Gym')");
                jdbc_statement.executeUpdate("INSERT INTO Provided " + 
                    " (StayID, StaffID, ServiceName) VALUES " +
                    " (1, 7, 'Gym')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (1, 5, 'Catering')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (2, 11, 'Room Service')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (3, 19, 'Dry Cleaning')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (4, 26, 'Gym')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (5, 32, 'Dry Cleaning')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (6, 38, 'Room Service')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (7, 48, 'Gym')");
        		jdbc_statement.executeUpdate("INSERT INTO Provided " + 
    				" (StayID, StaffID, ServiceName) VALUES " +
    				" (8, 54, 'Dry Cleaning')");
        		
                // If success, commit
                jdbc_connection.commit();
                
        		System.out.println("Provided table loaded!");
    		
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Update Stays Table with amount owed for each stay that has actually ended
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/11/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void updateAmountOwedForStays() {
        
        try {
            
            // Declare variables (this method calls another which uses our global jdbc result, so we need to disambiguate)
            ResultSet local_jdbc_result;
            int stayID;

            // Find the stays that have actually ended (no transaction needed for a query)
            // TODO: use prepared statement instead
            local_jdbc_result = jdbc_statement.executeQuery("SELECT ID FROM Stays WHERE EndDate IS NOT NULL");
            
            // Go through and update amount owed for all stays that have actually ended
            while (local_jdbc_result.next()) {
                stayID = local_jdbc_result.getInt(1);
                queryItemizedReceipt(stayID, false);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    // FRONT DESK
    
    /** 
     * Front Desk task: Check availability of rooms based on a wide range of characteristics
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/27/18 -  ATTD -  Created method.
     */
    public static void frontDeskCheckAvailability() {
        
        try {
            
            // Declare local variables
            ArrayList<String> filters = new ArrayList<>();
            String attributeToFilter = "";
            String filterAttrToApply = "";
            String valueToFilter = "";
            String filterValToApply = "";
            String sqlToExecute = "";
            int numRoomsAvailable = 0;
            int i;
            boolean userWantsToStop = false;
            boolean valueIsNumeric = false;
            
            // Print example room so user has some context
            System.out.println("\nExample Room (showing filter options):\n");
            jdbc_result = jdbcPrep_getOneExampleRoom.executeQuery();
            printQueryResultSet(jdbc_result);
            
            // Keep filtering results until the user wants to stop
            while (userWantsToStop == false) {
                
                // Print # available rooms in the Wolf Inns chain given existing filtering (need count but later will need all results)
                sqlToExecute = "SELECT count(*) FROM Rooms";
                for (i = 0; i < filters.size(); i++) {
                    // If we're here at all, we have at least one filter, so need a WHERE clause
                    if (i == 0) {
                        sqlToExecute += " WHERE ";
                    }
                    // Each element of filters is of the form attr:value
                    filterAttrToApply = filters.get(i).split(":")[0];
                    filterValToApply = filters.get(i).split(":")[1];
                    // Deal with special case Maximum Occupancy
                    if (filterAttrToApply.equals("MaxOcc")) {
                        sqlToExecute += filterAttrToApply + " >= " + filterValToApply;
                    }
                    // Deal with special case Nightly Rate
                    else if (filterAttrToApply.equals("NightlyRate")) {
                        sqlToExecute += filterAttrToApply + " <= " + filterValToApply;
                    }
                    // Deal with non-special cases (if string, must include quotes!)
                    else {
                        valueIsNumeric = true;
                        try {
                            Double.parseDouble(filterValToApply);
                        }
                        catch (NumberFormatException e) {
                            valueIsNumeric = false;
                        }
                        if (valueIsNumeric) {
                            sqlToExecute += filterAttrToApply + " = " + filterValToApply;
                        }
                        else {
                            sqlToExecute += filterAttrToApply + " = '" + filterValToApply + "'";
                        }
                    }
                    // If this wasn't the end, add an AND for the next one
                    if (i < filters.size() - 1) {
                        sqlToExecute += " AND ";
                    }
                }
                sqlToExecute += ";";
                jdbc_result = jdbc_statement.executeQuery(sqlToExecute);
                if (jdbc_result.next()) {
                    numRoomsAvailable = jdbc_result.getInt(1);
                }
                System.out.println("\nRooms Available: " + numRoomsAvailable + "\n");
                
                if (numRoomsAvailable == 0) {
                    userWantsToStop = true;
                }
                else {
                    
                    // Get name of attribute they want to filter on
                    System.out.print("\nEnter the name of the attribute you wish to ADD a filter for (or press <Enter> to stop)\n> ");
                    attributeToFilter = scanner.nextLine();
                    if (isValueSane("AnyAttr", attributeToFilter)) {
                        // Get value they want to change the attribute to
                        if (attributeToFilter.equals("MaxOcc")) {
                            System.out.print("\nFor maximum occupancy, filtering will include any rooms with your desired occupancy or above");
                        }
                        else if (attributeToFilter.equals("NightlyRate")) {
                            System.out.print("\nFor nightly rate, filtering will include any rooms with your desired rate or below");
                        }
                        System.out.print("\nEnter the value you wish to apply as a filter (or press <Enter> to stop)\n> ");
                        valueToFilter = scanner.nextLine();
                        if (isValueSane(attributeToFilter, valueToFilter)) {
                            // Add this filter to the growing list
                            filters.add(attributeToFilter + ":" + valueToFilter);
                        }
                        else {
                            userWantsToStop = true;
                        }
                    }
                    else {
                        userWantsToStop = true;
                    }
                    
                }
                
            }
            // Report full info about rooms that satisfied all the filters
            sqlToExecute = sqlToExecute.replace("count(*)", "*");
            jdbc_result = jdbc_statement.executeQuery(sqlToExecute);
            printQueryResultSet(jdbc_result);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Front Desk task: Assign a room to a customer
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   04/01/18 -  ATTD -  Created method.
     */
    public static void frontDeskAssignRoom() {
        
        try {
            
            // Declare local variables
            String roomNum = "";
            String hotelID = "";
            String numGuests = "";
            String customerSSN = "";
            String paymentMethod = "";
            String cardType = "";
            String cardNumber = "";
            String billingAddress = "";
            
            // Get room number
            System.out.print("\nEnter the room number\n> ");
            roomNum = scanner.nextLine();
            if (isValueSane("RoomNum", roomNum)) {
                // Get hotel ID
                System.out.print("\nEnter the hotel ID\n> ");
                hotelID = scanner.nextLine();
                if (isValueSane("HotelID", hotelID)) {
                    // Get number of guests
                    System.out.print("\nEnter the number of guests staying in this room\n> ");
                    numGuests = scanner.nextLine();
                    if (isValueSane("NumGuests", numGuests)) {
                        // Get customer SSN
                        System.out.print("\nEnter the customer's SSN\n> ");
                        customerSSN = scanner.nextLine();
                        if (isValueSane("CustomerSSN", customerSSN)) {
                            // Get payment method
                            System.out.print("\nEnter the payment method\n> ");
                            paymentMethod = scanner.nextLine();
                            if (isValueSane("PaymentMethod", paymentMethod)) {
                                // Get card type
                                System.out.print("\nEnter the credit card type\n> ");
                                cardType = scanner.nextLine();
                                if (isValueSane("CardType", cardType)) {
                                    // Get card number
                                    System.out.print("\nEnter the credit card number\n> ");
                                    cardNumber = scanner.nextLine();
                                    if (isValueSane("CardNumber", cardNumber)) {
                                        // Get billing address
                                        System.out.print("\nEnter the billing address\n> ");
                                        billingAddress = scanner.nextLine();
                                        if (isValueSane("BillingAddress", billingAddress)) {
                                            // Okay, at this point everything else I can think of can be caught by a Java exception or a SQL exception
                                            updateInsertStay(
                                                Integer.parseInt(roomNum), 
                                                Integer.parseInt(hotelID), 
                                                Long.parseLong(customerSSN), 
                                                Integer.parseInt(numGuests), 
                                                paymentMethod, 
                                                cardType, 
                                                Long.parseLong(cardNumber), 
                                                billingAddress, 
                                                true
                                            );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    // BILLING
    
    /** 
     * Billing task: Generate bill and itemized receipt for a customer's stay
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/11/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void generateBillAndReceipt() {
        
        try {

            // Declare variables
            int stayID;
            
            // Get stay ID
            System.out.print("\nEnter the stay ID\n> ");
            stayID = Integer.parseInt(scanner.nextLine());
            
            // Call method to actually interact with the DB
            queryItemizedReceipt(stayID, true);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    // REPORTS
    
    /** 
     * Report task: Report revenue earned by a hotel over a date range
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/11/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new attribute / value sanity checking method.
     *                                      Use new general error handler.
     */
    public static void reportHotelRevenue() {

        try {
            
            // Declare local variables
            int hotelID;
            String hotelIdAsString = "";
            String startDate = "";
            String endDate = "";
            
            // Get name
            System.out.print("\nEnter the hotel ID\n> ");
            hotelIdAsString = scanner.nextLine();
            if (isValueSane("ID", hotelIdAsString)) {
                hotelID = Integer.parseInt(hotelIdAsString);
                // Get start date
                System.out.print("\nEnter the start date\n> ");
                startDate = scanner.nextLine();
                if (isValueSane("StartDate", startDate)) {
                    // Get end date
                    System.out.print("\nEnter the end date\n> ");
                    endDate = scanner.nextLine();
                    if (isValueSane("EndDate", endDate)) {
                        // Call method to actually interact with the DB
                        queryHotelRevenue(hotelID, startDate, endDate);
                    }
                }
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }

    /** 
     * Report task: Report all results from a given table
     * 
     * Arguments -  tableName - The table to print out
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void reportEntireTable(String tableName) {

        try {
            
            // Report entire table (no transaction needed for a query)
            System.out.println("\nEntries in the " + tableName + " table:\n");
            // TODO: use prepared statement instead
            jdbc_result = jdbc_statement.executeQuery("SELECT * FROM " + tableName);
            printQueryResultSet(jdbc_result);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Report task: Report all values of a single tuple of the Hotels table, by ID
     * 
     * Arguments -  id -        The ID of the tuple.
     * Return -     success -   True if the hotel was found.
     * 
     * Modifications:   03/23/18 -  ATTD -  Created method.
     *                  03/28/18 -  ATTD -  Return whether query was successful (hotel was found).
     */
    public static boolean reportHotelByID(int id) {

        // Declare variables
        boolean success = false;
        
        try {

            // Get entire tuple from table
            jdbcPrep_getHotelByID.setInt(1, id);
            jdbc_result = jdbcPrep_getHotelByID.executeQuery();
            
            // Was the hotel found?
            if (jdbc_result.next()) {
                success = true;
                jdbc_result.beforeFirst();
            }
            
            // Print result
            System.out.println("\nHotel Information:\n");
            printQueryResultSet(jdbc_result);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
        return success;
        
    }
    
    /** 
     * Report task: Report all values of a single tuple of the Staff table, by ID
     * 
     * Arguments -  id -        The ID of the tuple.
     * Return -     success -   True if the staff member was found.
     * 
     * Modifications:   03/26/18 -  ATTD -  Created method.
     *                  03/28/18 -  ATTD -  Return whether query was successful (staff was found).
     */
    public static boolean reportStaffByID(int id) {

        // Declare variables
        boolean success = false;
        
        try {
            
            // Get entire tuple from table
            jdbcPrep_getStaffByID.setInt(1, id);
            jdbc_result = jdbcPrep_getStaffByID.executeQuery();
            
            // Was the staff member found?
            if (jdbc_result.next()) {
                success = true;
                jdbc_result.beforeFirst();
            }
            
            // Print result
            System.out.println("\nStaff Member Information:\n");
            printQueryResultSet(jdbc_result);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
        return success;
        
    }
    
    /** 
     * Report task:   Report all values of a single tuple of the Rooms table, by hotelId and RoomNum
     * 
     * Arguments -    hotelId       - Hotel to which the room belongs
     * 				  roomNumber    - Room number  
     * Return -       None
     * 
     * Modifications: 03/25/18 -  MTA -  Created method.
     */
    public static void reportRoomByHotelIdRoomNum(int hotelId, int roomNum) {

        try {
        	 
        	jdbcPrep_getRoomByHotelIDRoomNum.setInt(1, roomNum);
        	jdbcPrep_getRoomByHotelIDRoomNum.setInt(2, hotelId);
            jdbc_result = jdbcPrep_getRoomByHotelIDRoomNum.executeQuery();
            
            // Print result
            System.out.println("\nInformation:\n");
            printQueryResultSet(jdbc_result);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Report task:   Report all values of a single tuple of the Customers table, by customerSSN
     * 
     * Arguments -    customerSSN - Customer Social Security Number
     * Return -       None
     * 
     * Modifications: 03/27/18 -  MTA -  Created method.
     */
    public static void reportCustomerBySSN(String ssn) {

        try {
        	 
        	jdbcPrep_getCustomerBySSN.setLong(1, Long.parseLong(ssn)); 
            jdbc_result = jdbcPrep_getCustomerBySSN.executeQuery();
            
            // Print result
            System.out.println("\nInformation:\n");
            printQueryResultSet(jdbc_result);
            
        }
        catch (Throwable err) {
            handleError(err);
        } 
    }
    
    // PRINT
    
    /** 
     * Print query result set
     * Modified from, but inspired by: https://coderwall.com/p/609ppa/printing-the-result-of-resultset
     * 
     * Arguments -  resultSetToPrint -  The result set to print
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/08/18 -  ATTD -  Made printout slightly prettier.
     *                  03/08/18 -  ATTD -  At the end, print number of records in result set.
     *                  03/21/18 -  ATTD -  Print column label instead of column name.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  04/01/18 -  ATTD -  Make the result set print out correctly regardless of contents
     *                                      (at the expense of speed - will run slower now, but still seems to just quite fast enough).
     */
    public static void printQueryResultSet(ResultSet resultSetToPrint) {
        
        try {
            
            // Declare variables
            ArrayList<Integer> columnWidths = new ArrayList<>();
            ResultSetMetaData metaData;
            String columnName;
            String tupleValue;
            int columnWidth;
            int numColumns;
            int numTuples;
            int i;

            // Is there anything useful in the result set?
            if (jdbc_result.next()) {
                
                // Get metadata
                metaData = jdbc_result.getMetaData();
                numColumns = metaData.getColumnCount();
                numTuples = 0;
                do {
                    numTuples++;
                }
                while (jdbc_result.next());
                jdbc_result.beforeFirst();
                
                // Figure out how many chars to use for each column
                for (i = 1; i <= numColumns; i++) {
                    columnName = metaData.getColumnLabel(i);
                    columnWidth = columnName.length();
                    while (jdbc_result.next()) {
                        tupleValue = jdbc_result.getString(i);
                        // Tuple value could be null, if so that's 4 chars to print ("NULL")
                        if (tupleValue == null) {
                            if (4 > columnWidth) {
                                columnWidth = 4;
                            }
                        }
                        else if (tupleValue.length() > columnWidth) {
                            columnWidth = tupleValue.length();
                        }
                    }
                    columnWidths.add(columnWidth + 1);
                    jdbc_result.beforeFirst();
                }

                /* Print column headers
                 * use column label instead of column name,
                 * otherwise you will not see the effect of aliasing
                 */
                for (i = 1; i <= numColumns; i++) {
                    columnName = metaData.getColumnLabel(i);
                    System.out.print(padRight(columnName, columnWidths.get(i-1)));
                }
                System.out.println("");
                System.out.println("");
                
                // Go through the result set tuple by tuple
                while (jdbc_result.next()) {
                    for (i = 1; i <= numColumns; i++) {
                        tupleValue = jdbc_result.getString(i);
                        System.out.print(padRight(tupleValue, columnWidths.get(i-1)));
                    }
                    System.out.print("\n");
                }
                
                // Print number of records found
                System.out.println("");
                System.out.println("(" + numTuples + " entries)");
                System.out.println("");
                
            }
            else {
                // Tell the user that the result set is empty
                System.out.println("(no results)\n");
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Pad a string with space characters to reach a given number of total characters
     * https://stackoverflow.com/questions/388461/how-can-i-pad-a-string-in-java/391978#391978
     * 
     * Arguments -  stringIn -          The result set to print
     *              numDesiredChars -   The desired number of characters in the padded result
     * Return -     stringOut -         The padded string
     * 
     * Modifications:   03/08/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static String padRight(String stringIn, int numDesiredChars) {
        
        // Declare variables
        String stringOut = stringIn;
        
        try {
            // Pad string
            stringOut = String.format("%1$-" + numDesiredChars + "s", stringIn); 
        }
        catch (Throwable err) {
            handleError(err);
        }
        
        return stringOut;
         
    }

    // MANAGE
    
    /** 
     * Management task: Add a new hotel
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/08/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new attribute / value sanity checking method.
     *                                      Use new general error handler.
     */
    public static void manageHotelAdd() {

        try {
            
            // Declare local variables
            String hotelName = "";
            String streetAddress = "";
            String city = "";
            String state = "";
            String phoneNumAsString = "";
            String managerIdAsString = "";
            long phoneNum = 0;
            int managerID = 0;
            
            // Get name
            System.out.print("\nEnter the hotel name\n> ");
            hotelName = scanner.nextLine();
            if (isValueSane("Name", hotelName)) {
                // Get street address
                System.out.print("\nEnter the hotel's street address\n> ");
                streetAddress = scanner.nextLine();
                if (isValueSane("StreetAddress", streetAddress)) {
                    // Get city
                    System.out.print("\nEnter the hotel's city\n> ");
                    city = scanner.nextLine();
                    if (isValueSane("City", city)) {
                        // Get state
                        System.out.print("\nEnter the hotel's state\n> ");
                        state = scanner.nextLine();
                        if (isValueSane("State", state)) {
                            // Get phone number
                            System.out.print("\nEnter the hotel's phone number\n> ");
                            phoneNumAsString = scanner.nextLine();
                            if (isValueSane("PhoneNum", phoneNumAsString)) {
                                phoneNum = Long.parseLong(phoneNumAsString);
                                // Get manager
                                System.out.print("\nEnter the hotel's manager's staff ID\n> ");
                                managerIdAsString = scanner.nextLine();
                                if (isValueSane("ManagerID", managerIdAsString)) {
                                    managerID = Integer.parseInt(managerIdAsString);
                                    // Okay, at this point everything else I can think of can be caught by a Java exception or a SQL exception
                                    updateInsertHotel(hotelName, streetAddress, city, state, phoneNum, managerID, true);
                                }
                            }
                        }
                    }
                }
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Management task: Change information about a hotel
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/23/18 -  ATTD -  Created method.
     *                  03/17/18 -  ATTD -  Fix copy-paste error keeping proposed values from being sanity-checked.
     *                  03/28/18 -  ATTD -  Stop immediately if invalid hotel ID is entered.
     */
    public static void manageHotelUpdate() {

        try {

            // Declare local variables
            String hotelIdAsString = "";
            String attributeToChange = "";
            String valueToChangeTo;
            int hotelID = 0;
            boolean userWantsToStop = false;
            boolean hotelFound = false;
            
            // Print hotels to console so user has some context
            reportEntireTable("Hotels");
            
            // Get hotel ID
            System.out.print("\nEnter the hotel ID for the hotel you wish to make changes for\n> ");
            hotelIdAsString = scanner.nextLine();
            if (isValueSane("ID", hotelIdAsString)) {
                hotelID = Integer.parseInt(hotelIdAsString);
                // Print just that hotel to console so user has some context
                hotelFound = reportHotelByID(hotelID);
                if (hotelFound) {
                    
                    // Keep updating values until the user wants to stop
                    while (userWantsToStop == false) {
                        // Get name of attribute they want to change
                        System.out.print("\nEnter the name of the attribute you wish to change (or press <Enter> to stop)\n> ");
                        attributeToChange = scanner.nextLine();
                        if (isValueSane("AnyAttr", attributeToChange)) {
                            // Get value they want to change the attribute to
                            System.out.print("\nEnter the value you wish to change this attribute to (or press <Enter> to stop)\n> ");
                            valueToChangeTo = scanner.nextLine();
                            if (isValueSane(attributeToChange, valueToChangeTo)) {
                                // Okay, at this point everything else I can think of can be caught by a Java exception or a SQL exception
                                updateChangeHotelInfo(hotelID, attributeToChange, valueToChangeTo);
                            }
                            else {
                                userWantsToStop = true;
                            }
                        }
                        else {
                            userWantsToStop = true;
                        }
                    }
                    // Report results of all the updates
                    reportHotelByID(hotelID);
                    
                }

            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Management task: Delete a hotel
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/09/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/24/18 -  ATTD -  Provide more context for user.
     *                                      Change hotel ID from long to int.
     */
    public static void manageHotelDelete() {

        try {
            
            // Declare local variables
            int hotelID = 0;
            
            // Print hotels to console so user has some context
            reportEntireTable("Hotels");
            
            // Get ID of hotel to delete
            System.out.print("\nEnter the hotel ID for the hotel you wish to delete\n> ");
            hotelID = Integer.parseInt(scanner.nextLine());

            // Call method to actually interact with the DB
            updateDeleteHotel(hotelID, true);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /**
     * Task: Manage Rooms
     * 
     * Operation: Add a room
     * 
     * Arguments -  None
     * Returns -    None
     * 
     * Modifications:   03/24/18 -  MTA -   Added functionality to add new room
     * 				    03/25/18 -  MTA -   Fix the category type
     *                  03/28/18 -  ATTD -  Use same field names as in tables themselves
     *                                      - for developer ease
     *                                      - because "isValueSane" method uses table attribute names
     */
    public static void manageRoomAdd() {
    	
    	try { 
    		  
    		String hotelId = getValidDataFromUser("ADD_ROOM", "HotelId", "Enter the hotel id for which you are adding new room\n> ");
    		
    		String roomNumber = getValidDataFromUser("ADD_ROOM", "RoomNum", "Enter the room number\n> ", hotelId); 
    		
    		String category = getValidDataFromUser("ADD_ROOM","Category", "Enter the room's category.\nAvailable options are 'ECONOMY', 'DELUXE', 'EXECUTIVE_SUITE', 'PRESIDENTIAL_SUITE' \n>");
               
    		String maxOccupancy = getValidDataFromUser("ADD_ROOM","MaxOcc", "Enter the room's maximum occupancy\n> "); 
    		
    		String nightlyRate = getValidDataFromUser("ADD_ROOM", "NightlyRate", "Enter the room's nightly rate\n> "); 
              
            addRoom(Integer.parseInt(roomNumber), Integer.parseInt(hotelId), category.toUpperCase(), Integer.parseInt(maxOccupancy), Integer.parseInt(nightlyRate), true);
           
        }
        catch (Throwable err) {
            handleError(err);
        }
    } 
    
    /**
     * Task: Manage
     * Operation: Update room details
     * 
     * Modifications:   03/24/18 -  MTA -   Added functionality to update room details
     *                  03/28/18 -  ATTD -  Use same field names as in tables themselves
     *                                      - for developer ease
     *                                      - because "isValueSane" method uses table attribute names
     */
    public static void manageRoomUpdate() {
    	try {
    		boolean userWantsToStop = false; 
    		 
            // Print hotels to console so user has some context
            reportEntireTable("Rooms");
            
            String hotelId = getValidDataFromUser("UPDATE_ROOM", "HotelId", "Enter the hotel ID for the room you wish to make changes for\n> ");
            String roomNumber = getValidDataFromUser("UPDATE_ROOM", "RoomNum", "Enter the room number you wish to make changes for\n> ", hotelId); 
            
            reportRoomByHotelIdRoomNum(Integer.parseInt(hotelId), Integer.parseInt(roomNumber));
                
            while(!userWantsToStop) { 
            	
            	// Get the attribute the user wants to update
                System.out.print("\nChoose the attribute you wish to change\n1. Room Category\n2. Max Occupancy\n3. Nightly Rate\n4. Exit\n> ");
                int attributeToChange = Integer.parseInt(scanner.nextLine());
            	
            	switch(attributeToChange){
	             	case 1:
	             		String category = getValidDataFromUser("UPDATE_ROOM","Category", "Enter the new value for room's category.\nAvailable options are 'ECONOMY', 'DELUXE', 'EXECUTIVE_SUITE', 'PRESIDENTIAL_SUITE' \n>");
	             		updateRoom(Integer.parseInt(roomNumber), Integer.parseInt(hotelId), "Category", category.toUpperCase(), true);
	             		break;
	             	case 2:
	             		String maxOccupancy = getValidDataFromUser("UPDATE_ROOM","MaxOcc", "Enter the new value for room's maximum occupancy\n> ");
	             		updateRoom(Integer.parseInt(roomNumber), Integer.parseInt(hotelId), "MaxOcc", maxOccupancy, true);
	             		break;
	             	case 3:
	             		String nightlyRate = getValidDataFromUser("UPDATE_ROOM", "NightlyRate", "Enter the new value for room's nightly rate\n> ");
	             		updateRoom(Integer.parseInt(roomNumber), Integer.parseInt(hotelId), "NightlyRate", nightlyRate, true);
	             		break;
	             	case 4:
	             		userWantsToStop = true;
	             		break;
	             	default: System.out.println("Please choose a number between 1 to 4"); 
	            } 
            } 
                
            // Report results of all the updates
            reportRoomByHotelIdRoomNum(Integer.parseInt(hotelId), Integer.parseInt(roomNumber)); 
        }
        catch (Throwable err) {
            handleError(err);
        }
    }
    
    /**
     * Task: Manage
     * Operation: Delete a room
     * 
     * Modifications:   03/24/18 -  MTA -   Added functionality to delete room
     *                  03/28/18 -  ATTD -  Use same field names as in tables themselves
     *                                      - for developer ease
     *                                      - because "isValueSane" method uses table attribute names
     */
    public static void manageRoomDelete() {
    	
    	try { 
             
            // Print hotels to console so user has some context
            reportEntireTable("Rooms");
            
            // Get hotelid and roomnumber to be deleted
            String hotelId = getValidDataFromUser("DELETE_ROOM", "HotelId", "Enter the hotel ID for the room you wish to delete\n> "); 
    		String roomNumber = getValidDataFromUser("DELETE_ROOM", "RoomNum", "Enter the room number you wish to delete\n> ", hotelId);

            // Call method to actually interact with the DB
            deleteRoom(Integer.parseInt(hotelId), Integer.parseInt(roomNumber), true);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
    	
    } 
    
    /**
     * Task: Manage
     * Operation: Add new customer
     * 
     * Modifications:   03/27/18 -  MTA -   Added method
     *                  03/28/18 -  ATTD -  Use same field names as in tables themselves
     *                                      - for developer ease
     *                                      - because "isValueSane" method uses table attribute names
     */
    public static void manageCustomerAdd() {
    	
    	try { 
  		  
    		String ssn = getValidDataFromUser("ADD_CUSTOMER", "SSN", "Enter the customer's SSN\n> ");
    		
    		String name = getValidDataFromUser("ADD_CUSTOMER", "Name", "Enter the customer's name\n> "); 
    		
    		String dob = getValidDataFromUser("ADD_CUSTOMER", "DOB", "Enter the customer's Date Of Birth(in format YYYY-MM-DD)\n>");
               
    		String phoneNumber = getValidDataFromUser("ADD_CUSTOMER", "PhoneNum", "Enter the customer's phone number\n> "); 
    		
    		String email = getValidDataFromUser("ADD_CUSTOMER", "Email", "Enter the customer's email\n> "); 
              
            addCustomer( ssn, name, dob, phoneNumber, email, true);
           
        }
        catch (Throwable err) {
            handleError(err);
        }
    	
    } 
    
    /**
     * Task: Manage
     * Operation: Update customer
     * 
     * Modifications: 03/27/18 - MTA - Added method
     */
    public static void manageCustomerUpdate() {
    	
    	try { 

    		boolean userWantsToStop = false; 
    		 
            // Print hotels to console so user has some context
            reportEntireTable("Customers");
            
            String customerSSN = getValidDataFromUser("UPDATE_CUSTOMER", "SSN", "Enter the SSN for the customer you wish to make changes for\n> ");
            
            reportCustomerBySSN(customerSSN);
                
            while(!userWantsToStop) { 
            	
            	// Get the attribute the user wants to update
                System.out.print("\nChoose the attribute you wish to change\n1. Name\n2. Date Of Birth\n3. Phone Number\n4. Email\n5. Exit\n> ");
                int attributeToChange = Integer.parseInt(scanner.nextLine());
            	
            	switch(attributeToChange){
	             	case 1:
	             		String customerName = getValidDataFromUser("UPDATE_CUSTOMER","Name", "Enter the new value for customer's name\n>");
	             		updateCustomer(customerSSN, "Name", customerName, true);
	             		break;
	             	case 2:
	             		String dob = getValidDataFromUser("UPDATE_CUSTOMER","DOB", "Enter the new value for customer's date of birth\n> ");
	             		updateCustomer(customerSSN, "DOB", dob, true);
	             		break;
	             	case 3:
	             		String phoneNumber = getValidDataFromUser("UPDATE_CUSTOMER", "PhoneNum", "Enter the new value for customer's phone number\n> ");
	             		updateCustomer(customerSSN, "PhoneNum", phoneNumber, true);
	             		break;
	             	case 4:
	             		String email = getValidDataFromUser("UPDATE_CUSTOMER", "Email", "Enter the new value for customer's email\n> ");
	             		updateCustomer(customerSSN, "Email", email, true);
	             		break;
	             	case 5:
	             		userWantsToStop = true;
	             		break;
	             	default: System.out.println("Please choose a number between 1 to 5"); 
	            } 
            } 
                
            // Report results of all the updates
            reportCustomerBySSN(customerSSN); 
        
             
        }
        catch (Throwable err) {
            handleError(err);
        }
    	
    } 
    
    /**
     * Task: Manage
     * Operation: Delete a customer
     * 
     * Modifications: 03/27/18 - MTA - Added method
     */
    public static void manageCustomerDelete() {
    	
    	try { 
            
            // Print hotels to console so user has some context
            reportEntireTable("Customers");
            
            // Get SSN of the customer to be deleted
            String ssn = getValidDataFromUser("DELETE_CUSTOMER", "SSN", "Enter the SSN for the customer you wish to delete\n> ");  

            // Call method to actually interact with the DB
            deleteCustomer(ssn, true); 
            
        }
        catch (Throwable err) {
            handleError(err);
        }
    	
    } 
    
    /**
     * Task: Helper method to get data from user
     * 
     * Arguments: operation - Operation the user is performing (ADD_ROOM / UPDATE_ROOM / DELETE_ROOM)  
     * 			  fieldName - Name of the field (used while checking if entered data is sane)
     *            message - The message asking user to enter the data 
     *            params(Optional) - Extra parameters needed to validate the sanity 
     * 
     * Returns: Valid user entered data 
     * 
     * Modifications:   03/24/18 -  MTA -   Added method
     *                  03/28/18 -  ATTD -  Use same field names as in tables themselves
     *                                      - for developer ease
     *                                      - because "isValueSane" method uses table attribute names
     */
    public static String getValidDataFromUser (String operation, String fieldName, String message, String...params ){
    	
    	boolean isValid = false;
    	int attempt = 0;
    	String value = "";
    	
    	// Repeat till user enters a correct field value
        while(!isValid) {
        	// Ask user to enter/re-enter the data
        	String messagePrefix = (attempt == 0) ? "\n" : "\nRe-";
        	System.out.println(messagePrefix + message); 
        	value = scanner.nextLine(); 
        	
        	if (fieldName.equalsIgnoreCase("HotelId")) {
        		boolean isSane = isValueSane(fieldName, value); 
    			if (isSane) {  
    				// Extra checks for Hotel Id when deleting room:
        			// 1. Check if the entered hotel id is valid
        			boolean isValidHotelId = isValidHotelId(Integer.parseInt(value));
        			if (isValidHotelId) { 
        				isValid = true;  
        			} else {
        				System.out.println("ERROR: The entered hotel id does not exist in database");
        			}  
    			}  
        	} 
        	
        	else if (fieldName.equalsIgnoreCase("RoomNum") && (operation.equals("DELETE_ROOM") || operation.equals("UPDATE_ROOM"))) {
        		boolean isSane = isValueSane(fieldName, value); 
    			if (isSane) {  
    				// Extra checks for Room Number when deleting/updating room :
        			// 1. Check if the entered room number is present for given hotel
        			boolean isAssociated = isValidRoomForHotel(Integer.parseInt(params[0]), Integer.parseInt(value));
        			if (isAssociated) { 
        				// 2. Make sure there are no guests staying in this room currently
    	    			boolean isRoomOccupied = isRoomCurrentlyOccupied(Integer.parseInt(params[0]), Integer.parseInt(value));
    	    			if (!isRoomOccupied) {
    	    				isValid = true;  
    	    			} else { 
    	    				System.out.println("ERROR: The room is currently occupied, hence cannot be modified"); 
    	    			} 
        			} else { 
        				System.out.println("ERROR: The room number is not associated with this hotel");   
        			}  
    			}  
        	} 
        	
        	else if (fieldName.equalsIgnoreCase("RoomNum") && operation.equals("ADD_ROOM")) {
        		 
        		boolean isSane = isValueSane(fieldName, value); 
    			if (isSane) { 
    				// Extra checks for Room Number when adding room:
    				// 1.check if the entered room number is not already present for given hotel
        			boolean isAssociated = isValidRoomForHotel(Integer.parseInt(params[0]), Integer.parseInt(value));
        			if (!isAssociated) { 
        				isValid = true; 
        			} else {
        				System.out.println("ERROR: This room number is already associated with different room in this hotel");  
        			}
    			}     
        	}  
        	
        	else if (fieldName.equalsIgnoreCase("SSN")) {
       		 
        		if (operation.equals("UPDATE_CUSTOMER") || operation.equals("DELETE_CUSTOMER")) {
        			boolean isSane = isValueSane(fieldName, value);
        			if (isSane) {
        				// Extra checks for SSN when adding customer info:
        				// 1.check if the entered SSN is valid i.e exists in Customers table
            			boolean isExistingCustomer = isValidCustomer(value);
            			if (isExistingCustomer) {  
            				if (operation.equals("DELETE_CUSTOMER")) {
            					// Extra checks for SSN when deleting customer info:
                    			// 2. Check if customer is currently staying in any hotel
            					boolean isCurrentlyStaying = isCustomerCurrentlyStaying(value);
            					if (!isCurrentlyStaying) {
            						isValid = true; 
            					} else {
            						System.out.println("ERROR: The customer is associated with a current guest stay"); 
            					}            					
            				} else {
            					isValid = true; 
            				}            				
            			} else {
            				System.out.println("ERROR: The entered SSN does not exist in our database");  
            			}
        			}  
        		}
        		   
        	}  
        	
        	else {
        		isValid = isValueSane(fieldName, value);
        	}
        	
        	attempt++;
        } 
        
        // Now the data is valid, return it
        return value;
    }
    
    /** 
     * Management task: Add a new staff member
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/24/18 -  ATTD -  Created method.
     */
    public static void manageStaffAdd() {

        try {
            
            // Declare local variables
            String name = "";
            String dob = "";
            String jobTitle = "";
            String department = "";
            String phoneNumAsString = "";
            String address = "";
            String hotelIdAsString = "";
            long phoneNum = 0;
            int hotelID = 0;
            
            // Get name
            System.out.print("\nEnter the new staff member's name\n> ");
            name = scanner.nextLine();
            if (isValueSane("Name", name)) {
                // Get date of birth
                System.out.print("\nEnter the new staff member's date of birth\n> ");
                dob = scanner.nextLine();
                if (isValueSane("DOB", dob)) {
                    // Get job title
                    System.out.print("\nEnter the new staff member's job title\n> ");
                    jobTitle = scanner.nextLine();
                    if (isValueSane("JobTitle", jobTitle)) {
                        // Get department
                        System.out.print("\nEnter the new staff member's department\n> ");
                        department = scanner.nextLine();
                        if (isValueSane("Dep", department)) {
                            // Get phone number
                            System.out.print("\nEnter the new staff member's phone number\n> ");
                            phoneNumAsString = scanner.nextLine();
                            if (isValueSane("PhoneNum", phoneNumAsString)) {
                                phoneNum = Long.parseLong(phoneNumAsString);
                                // Get address
                                System.out.print("\nEnter the new staff member's full address\n> ");
                                address = scanner.nextLine();
                                if (isValueSane("Address", address)) {
                                    // Get hotel ID
                                    System.out.print("\nEnter the new staff member's hotel ID (or press <Enter> if they are not assigned to any particular hotel)\n> ");
                                    hotelIdAsString = scanner.nextLine();
                                    hotelID = hotelIdAsString.length() == 0 ? 0 : Integer.parseInt(hotelIdAsString);
                                    // Okay, at this point everything else I can think of can be caught by a Java exception or a SQL exception
                                    updateInsertStaff(name, dob, jobTitle, department, phoneNum, address, hotelID, true);
                                }
                            }
                        }
                    }
                }
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Management task: Change information about a staff member
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/26/18 -  ATTD -  Created method.
     *                  03/28/18 -  ATTD -  Stop immediately if invalid staff ID entered.
     */
    public static void manageStaffUpdate() {

        try {

            // Declare local variables
            String staffIdAsString = "";
            String attributeToChange = "";
            String valueToChangeTo;
            int staffID = 0;
            boolean userWantsToStop = false;
            boolean staffFound = false;
            
            // Print staff to console so user has some context
            reportEntireTable("Staff");
            
            // Get hotel ID
            System.out.print("\nEnter the staff ID for the staff member you wish to make changes for\n> ");
            staffIdAsString = scanner.nextLine();
            if (isValueSane("ID", staffIdAsString)) {
                staffID = Integer.parseInt(staffIdAsString);
                // Print just that staff member to console so user has some context
                staffFound = reportStaffByID(staffID);
                if (staffFound) {
                    
                    // Keep updating values until the user wants to stop
                    while (userWantsToStop == false) {
                        // Get name of attribute they want to change
                        System.out.print("\nEnter the name of the attribute you wish to change (or press <Enter> to stop)\n> ");
                        attributeToChange = scanner.nextLine();
                        if (isValueSane("AnyAttr", attributeToChange)) {
                            // Get value they want to change the attribute to
                            System.out.print("\nEnter the value you wish to change this attribute to (or press <Enter> to stop)\n> ");
                            valueToChangeTo = scanner.nextLine();
                            if (isValueSane(attributeToChange, valueToChangeTo)) {
                                // Okay, at this point everything else I can think of can be caught by a Java exception or a SQL exception
                                updateChangeStaffInfo(staffID, attributeToChange, valueToChangeTo);
                            }
                            else {
                                userWantsToStop = true;
                            }
                        }
                        else {
                            userWantsToStop = true;
                        }
                    }
                    // Report results of all the updates
                    reportStaffByID(staffID);
                    
                }

            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * Management task: Delete a staff member
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/12/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/27/18 -  ATTD -  Provide more context for user.
     *                                      Change staff ID from long to int.
     */
    public static void manageStaffDelete() {

        try {
            
            // Declare local variables
            int staffID = 0;
            
            // Print staff members to console so user has some context
            reportEntireTable("Staff");
            
            // Get ID of staff members to delete
            System.out.print("\nEnter the staff ID for the staff member you wish to delete\n> ");
            staffID = Integer.parseInt(scanner.nextLine());

            // Call method to actually interact with the DB
            updateDeleteStaff(staffID, true);
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    // SANITY CHECK VALUES
    
    /** 
     * Given a value, and an indication of its intended meaning, determine if it's "sane"
     * This method exists in part because the version of MariaDB suggested for use in this project
     * supports neither CHECK nor ASSERTION!
     * 
     * Arguments -  attributeName - The name of the attribute ("PhoneNum", "Address", etc)
     *              proposedValue - The proposed value for the attribute (as a string)
     * Return -     okaySoFar -     True if the value seems sane at first glance
     * 
     * Modifications:   03/23/18 -  ATTD -  Created method.
     * 					03/24/18 -  MTA -   Added validations for fields when adding a new room.
     * 					03/27/18 -  MTA -   Added validations for fields when adding a new customer.
     *                  03/28/18 -  ATTD -  Combined / removed redundancies for checks:
     *                                      - Customer date of bBirth
     *                                      - Customer email
     *                                      - Customer phone number
     *                                      - Customer name
     *                                      - Customer SSN
     *                                      Used "equalsIgnoreCase" more widely (better than "equals").
     */
    public static boolean isValueSane(String attributeName, String proposedValue) {
        
        // Declare local variables
        boolean okaySoFar = true;
        
        try {
            
            if (okaySoFar && attributeName.length() == 0) {
                System.out.println("Attribute not entered (cannot proceed)\n");
                okaySoFar = false;
            }
            if (okaySoFar && proposedValue.length() == 0) {
                System.out.println("Value not entered (cannot proceed)\n");
                okaySoFar = false;
            }
            /* Check for malformed state
             * DBMS seems to accept a malformed date with no complaints
             * even though we define as CHAR(2)
             */
            if (okaySoFar && attributeName.equalsIgnoreCase("State") && proposedValue.length() != 2) {
                System.out.println("State '" + proposedValue + "' malformed, should have 2 letters (cannot proceed)\n");
                okaySoFar = false;
            }
            // Check for malformed phone number
            if (okaySoFar && attributeName.equalsIgnoreCase("PhoneNum")) {
                try {
                    if (Long.parseLong(proposedValue) <= 0) {
                        System.out.println("Phone number '" + proposedValue + "' malformed, should be positive (cannot proceed)\n");
                        okaySoFar = false;
                    } else if (proposedValue.length() != 10) {
                        System.out.println("Phone number '" + proposedValue + "' malformed, should have 10 digits (cannot proceed)\n");
                        okaySoFar = false;
                    }  
                }
                catch(NumberFormatException nfe) {
                   System.out.println("Phone number '" + proposedValue + "' malformed, should be a number (cannot proceed)\n");
                   okaySoFar = false;
               }  
            }
            // Check for malformed SSN
            if (okaySoFar && attributeName.contains("SSN")) {
                try {
                    if (Long.parseLong(proposedValue) <= 0) {
                        System.out.println("SSN '" + proposedValue + "' malformed, should be positive (cannot proceed)\n");
                        okaySoFar = false;
                    } else if (proposedValue.length() != 9) {
                        System.out.println("SSN '" + proposedValue + "' malformed, should have 9 digits (cannot proceed)\n");
                        okaySoFar = false;
                    }  
                }
                catch(NumberFormatException nfe) {
                   System.out.println("SSN '" + proposedValue + "' malformed, should be a number (cannot proceed)\n");
                   okaySoFar = false;
               }  
            }
            /* Check for malformed date
             * DBMS seems to accept a malformed date with no complaints
             * https://stackoverflow.com/questions/2149680/regex-date-format-validation-on-java
             */
            if (okaySoFar && (attributeName.contains("Date") || attributeName.equalsIgnoreCase("DOB") ) && proposedValue.matches("\\d{4}-\\d{2}-\\d{2}") == false) {
                System.out.println("Date must be entered in the format 'YYYY-MM-DD' (cannot proceed)\n");
                okaySoFar = false;
            }
            /* Check for "bad" manager
             * Don't know of a way to have DBMS check that manager isn't dedicated to a presidential suite (ASSERTION not supported)
             */
            if (okaySoFar && attributeName.equalsIgnoreCase("ManagerID")) {
                // TODO: use prepared statement instead
                jdbc_result = jdbc_statement.executeQuery(
                        "SELECT Staff.ID, Staff.Name, Rooms.RoomNum, Rooms.hotelID " + 
                        "FROM Staff, Rooms " + 
                        "WHERE Staff.ID = " + 
                        Integer.parseInt(proposedValue) + 
                        " AND (Rooms.DRSStaff = " + Integer.parseInt(proposedValue) + " OR Rooms.DCStaff = " + Integer.parseInt(proposedValue) + ")");
                
                if (jdbc_result.next()) {
                    System.out.println("\nThis manager cannot be used, because they are already dedicated to serving a presidential suite\n");
                    jdbc_result.beforeFirst();
                    printQueryResultSet(jdbc_result);
                    okaySoFar = false;
                }
            }
            
            /* ******************************** VALIDATIONS FOR ADDING NEW ROOM ************************************* */
           
            // Check if entered hotel id for room is valid ( i.e non-negative number) 
            try{
            	 if (attributeName.equalsIgnoreCase("HotelId") && Integer.parseInt(proposedValue) <= 0) {
           		     System.out.println("\nERROR: Hotel ID should be a positive number");
                	 okaySoFar = false;
                 }  
            } catch(NumberFormatException nfe) {
            	System.out.println("\nERROR: Hotel ID should be a number");
            	okaySoFar = false;
            }
            
            // Check if entered room number is valid ( i.e non-negative number)
            try{
            	 if (attributeName.equalsIgnoreCase("RoomNum") && Integer.parseInt(proposedValue) <= 0) {
            		 System.out.println("\nERROR: Room Number should be a positive number");
                 	 okaySoFar = false;
                 }  
            } catch(NumberFormatException nfe) {
            	System.out.println("\nERROR: Room number should be a number");
            	okaySoFar = false;
            }  
            
            // Check if entered room category is valid ( i.e 'Economy', 'Deluxe', 'Executive Suite', 'Presidential Suite' )
            if (attributeName.equalsIgnoreCase("Category") && 
        	     !(proposedValue.equalsIgnoreCase("ECONOMY") || proposedValue.equalsIgnoreCase("DELUXE") || proposedValue.equalsIgnoreCase("EXECUTIVE_SUITE") || proposedValue.equalsIgnoreCase("PRESIDENTIAL_SUITE"))) {
        		 	System.out.println("\nERROR: Allowed values for room category are 'ECONOMY', 'DELUXE', 'EXECUTIVE_SUITE', 'PRESIDENTIAL_SUITE' ");
        		 	okaySoFar = false; 
            } 
            
            // Check if entered room max occupancy is valid ( i.e non-negative number)
            try{
            	 if (attributeName.equalsIgnoreCase("MaxOcc")) {
            		 if (Integer.parseInt(proposedValue) <= 0) {
            			 System.out.println("\nERROR: Room Max Occupancy should be a positive number");
                      	 okaySoFar = false; 	
            		 } 
            		 if (Integer.parseInt(proposedValue) > 4) {
            			 System.out.println("\nERROR: Maximum allowed value for Room Occupancy is 4");
                      	 okaySoFar = false; 	
            		 } 
                 }
            } catch(NumberFormatException nfe) {
            	System.out.println("\nERROR: Room Max Occupancy should be a number");
            	okaySoFar = false;
            }
             
            // Check if entered Room Nightly rate is valid ( i.e non-negative number)
            try{
            	 if (attributeName.equalsIgnoreCase("NightlyRate") && Integer.parseInt(proposedValue) <= 0) {
            		 System.out.println("\nERROR: Room Nightly rate should be a positive number");
                 	 okaySoFar = false;
                 }
            } catch(NumberFormatException nfe) {
            	System.out.println("\nERROR: Room Nightly rate should be a number");
            	okaySoFar = false;
            }      
             
        }
        catch (Throwable err) {
            handleError(err);
            okaySoFar = false;
        }

        // Return
        return okaySoFar;
        
    }
    
    // QUERIES
    
    /** 
     * DB Query: Stay bill and itemized receipt
     * 
     * Note:    Since we want to produce an itemized receipt, and then pull from it the total amount owed by the customer, it would be awesome if we could use the VIEW feature
     *          (sort of a stored query that can be operated on like a table).
     *          Per http://www.mysqltutorial.org/mysql-views.aspx, "You cannot use subqueries in the FROM clause of the SELECT statement that defines the view before MySQL 5.7.7"
     *          Per https://classic.wolfware.ncsu.edu/wrap-bin/mesgboard/csc:540::001:1:2018?task=ST&Forum=8&Topic=7, "We are running version 5.5.57 it seems"
     * 
     * Arguments -  stayID -        The ID of the stay for which we wish to generate a bill and an itemized receipt
     *              reportResults - True if we wish to report the itemized receipt and total amount owed
     *                              (false for mass calculation of amounts owed on stays)
     * Return -     None
     * 
     * Modifications:   03/11/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void queryItemizedReceipt (int stayID, boolean reportResults) {

        try {
            
            // Declare variables
            NumberFormat currency;
            String itemizedReceiptSQL;
            double amountOwedBeforeDiscount = 0d;
            double amountOwedAfterDiscount = 0d;
            
            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
                
                // Generate an itemized receipt for the stay
                // TODO: use prepared statement instead
                itemizedReceiptSQL = 
                        "(" + 
                            "SELECT 'NIGHT' AS Item, Nights AS Qty, NightlyRate AS ItemCost, Nights * NightlyRate AS TotalCost " + 
                            "FROM (" + 
                                "SELECT DATEDIFF(EndDate, StartDate) AS Nights, NightlyRate " + 
                                "FROM (" + 
                                    "SELECT StartDate, EndDate, NightlyRate " + 
                                    "FROM Rooms NATURAL JOIN (" + 
                                        "SELECT StartDate, EndDate, RoomNum, HotelID " + 
                                        "FROM Stays " + 
                                        "WHERE ID = " + 
                                        stayID +
                                    ") AS A " + 
                                ") AS B " + 
                            ") AS C " + 
                        ") " + 
                        "UNION " + 
                        "(" + 
                            "SELECT Name AS Item, COUNT(*) AS Qty, Cost AS ItemCost, COUNT(*) * Cost AS TotalCost " + 
                            "FROM (" + 
                                "ServiceTypes NATURAL JOIN (" + 
                                    "SELECT StayID, ServiceName AS Name " + 
                                    "FROM Provided " + 
                                    "WHERE StayID = " + 
                                    stayID + 
                                ") AS ServicesProvided " + 
                            ") GROUP BY Item" + 
                        ")";
                jdbc_result = jdbc_statement.executeQuery(itemizedReceiptSQL + ";");
                
                // Print the itemized receipt and the total amount owed
                if (reportResults) {
                    System.out.println("\nItemized Receipt for Stay ID: " + stayID + "\n");
                    printQueryResultSet(jdbc_result);
                }
                
                // Calculate the total amount owed, both before and after the possible hotel credit card discount
                // TODO: use prepared statement instead
                jdbc_result = jdbc_statement.executeQuery(
                        "SELECT SUM(TotalCost) FROM (" + 
                        itemizedReceiptSQL + 
                        ") AS ItemizedReceipt;");
                jdbc_result.next();
                amountOwedBeforeDiscount = jdbc_result.getDouble(1);
                // TODO: use prepared statement instead
                jdbc_result = jdbc_statement.executeQuery(
                        "SELECT IF((SELECT CardType FROM Stays WHERE ID = " + 
                        stayID + 
                        ") = 'HOTEL', SUM(TotalCost) * 0.95, SUM(TotalCost)) FROM (" + 
                        itemizedReceiptSQL + 
                        ") AS ItemizedReceipt;");
                jdbc_result.next();
                amountOwedAfterDiscount = jdbc_result.getDouble(1);
                
                // Update the stay with the total amount owed
                // TODO: use prepared statement instead
                jdbc_statement.executeUpdate("UPDATE Stays SET AmountOwed = " + amountOwedAfterDiscount + " WHERE ID = " + stayID);
                
                // If success, commit
                jdbc_connection.commit();
                
                // Print the total amount owed
                if (reportResults) {
                    currency = NumberFormat.getCurrencyInstance();
                    System.out.println("\nTotal Amount Owed Before Discount: " + currency.format(amountOwedBeforeDiscount));
                    System.out.println("\nWolfInns Credit Card Discount Applied: " + (amountOwedBeforeDiscount == amountOwedAfterDiscount ? "0%" : "5%"));
                    System.out.println("\nTotal Amount Owed After Discount: " + currency.format(amountOwedAfterDiscount) + "\n");
                }
                
            }
            catch (Throwable err) {
                // Handle error
                handleError(err);
                // Roll back the entire transaction
                jdbc_connection.rollback(); 
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * DB Query: Hotel Revenue
     * 
     * Arguments -  hotelID -       The ID of the hotel
     *              queryStartDate -     The start date of the date range we want revenue for
     *              queryEndDate -       The end date of the date range we want revenue for
     * Return -     None
     * 
     * Modifications:   03/09/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void queryHotelRevenue (int hotelID, String queryStartDate, String queryEndDate) {

        try {
            
            // Declare variables
            double revenue;
            NumberFormat currency;

            /* Get revenue for one hotel from a date range
             * Revenue is earned when the guest checks OUT
             * So we always look at the end date for the customer's stay
             * No transaction needed for a query
             */
            // TODO: use prepared statement instead
            jdbc_result = jdbc_statement.executeQuery("SELECT SUM(AmountOwed) " + 
                    "FROM Stays " +
                    "WHERE HotelID = " + hotelID + " AND Stays.EndDate >= '" + queryStartDate + "' AND Stays.EndDate <= '" + queryEndDate + "'");
            
            jdbc_result.next();
            revenue = jdbc_result.getDouble(1);
            
            // Print report
            currency = NumberFormat.getCurrencyInstance();
            System.out.println("\nRevenue earned: " + currency.format(revenue) + "\n");
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    // UPDATES
    
    /** 
     * DB Update: Insert Hotel
     * 
     * Note:    This does NOT support chain reaction of moving managers between hotels.
     *          Manager of new hotel CANNOT be existing manager of another hotel.
     * 
     * Arguments -  hotelName -     The name of the hotel to insert
     *              streetAddress - The street address of the hotel to insert
     *              city -          The city in which the hotel is located
     *              state -         The state in which the hotel is located
     *              phoneNum -      The phone number of the hotel
     *              managerID -     The staff ID of the person to promote to manager of the new hotel
     *              reportSuccess - True if we should print success message to console (should be false for mass population of hotels)
     * Return -     None
     * 
     * Modifications:   03/09/18 -  ATTD -  Created method.
     *                  03/12/18 -  ATTD -  Add missing JDBC commit statement.
     *                  03/16/18 -  ATTD -  Changing departments to emphasize their meaninglessness.
     *                  03/20/18 -  ATTD -  Switch to using prepared statements.
     *                  03/21/18 -  ATTD -  Debug inserting new hotel with prepared statements.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     */
    public static void updateInsertHotel (String hotelName, String streetAddress, String city, String state, long phoneNum, int managerID, boolean reportSuccess) {
        
        // Declare variables
        int newHotelID;
        
        try {

            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {

                /* Insert new hotel, using prepared statement
                 * Drop disable unique checks to avoid complaint
                 * This is needed because the hotel may get a manager who is already managing a different hotel
                 * We will fix this shortly but we need to avoid complaint in the meantime
                 */
                jdbcPrep_insertNewHotel.setString(1, hotelName);
                jdbcPrep_insertNewHotel.setString(2, streetAddress);
                jdbcPrep_insertNewHotel.setString(3, city);
                jdbcPrep_insertNewHotel.setString(4, state);
                jdbcPrep_insertNewHotel.setLong(5, phoneNum);
                jdbcPrep_insertNewHotel.setInt(6, managerID);
                jdbcPrep_insertNewHotel.setInt(7, managerID); 
                jdbcPrep_insertNewHotel.executeUpdate();
                
                // Update new hotel's manager (job title and hotel assignment), using prepared statement
                jdbcPrep_updateNewHotelManager.executeUpdate();

                // If success, commit
                jdbc_connection.commit();
                
                // Then, tell the user about the success
                if (reportSuccess) {
                    jdbc_result = jdbcPrep_getNewestHotelID.executeQuery();
                    jdbc_result.next();
                    newHotelID = jdbc_result.getInt(1);
                    System.out.println("\n'" + hotelName + "' hotel added (hotel ID: " + newHotelID + ")!\n");
                }
                
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * DB Update: Change Hotel Information
     * 
     * Arguments -  hotelID -           The ID of the hotel to update
     *              attributeToChange - The attribute to update
     *              valueToChangeTo -   The value to update this attribute to (as a string)
     * Return -     None
     * 
     * Modifications:   03/23/18 -  ATTD -  Created method.
     */
    public static void updateChangeHotelInfo (int hotelID, String attributeToChange, String valueToChangeTo) {
        
        try {

            // Safeguard - make sure changes will commit
            jdbc_connection.setAutoCommit(true);
            
            // Update hotel info, using prepared statement
            switch (attributeToChange) {
                case "Name":
                    jdbcPrep_udpateHotelName.setString(1, valueToChangeTo);
                    jdbcPrep_udpateHotelName.setInt(2, hotelID);
                    jdbcPrep_udpateHotelName.executeUpdate();
                    break;
                case "StreetAddress":
                    jdbcPrep_updateHotelStreetAddress.setString(1, valueToChangeTo);
                    jdbcPrep_updateHotelStreetAddress.setInt(2, hotelID);
                    jdbcPrep_updateHotelStreetAddress.executeUpdate();
                    break;
                case "City":
                    jdbcPrep_updateHotelCity.setString(1, valueToChangeTo);
                    jdbcPrep_updateHotelCity.setInt(2, hotelID);
                    jdbcPrep_updateHotelCity.executeUpdate();
                    break;
                case "State":
                    jdbcPrep_udpateHotelState.setString(1, valueToChangeTo);
                    jdbcPrep_udpateHotelState.setInt(2, hotelID);
                    jdbcPrep_udpateHotelState.executeUpdate();
                    break;
                case "PhoneNum":
                    jdbcPrep_updateHotelPhoneNum.setLong(1, Long.parseLong(valueToChangeTo));
                    jdbcPrep_updateHotelPhoneNum.setInt(2, hotelID);
                    jdbcPrep_updateHotelPhoneNum.executeUpdate();
                    break;
                case "ManagerID":
                    /* This one is a special case
                     * 1 -  Demote old manager to front desk representative
                     * 2 -  Actually update the manager ID of the hotel
                     * 3 -  Update new manager to have the correct job title and hotel assignment
                     */
                    jdbc_connection.setAutoCommit(false);
                    try {
                        // Demote old manager
                        jdbcPrep_demoteOldManager.setInt(1, hotelID);
                        jdbcPrep_demoteOldManager.executeUpdate();
                        // Update hotel manager ID
                        jdbcPrep_updateHotelManagerID.setLong(1, Integer.parseInt(valueToChangeTo));
                        jdbcPrep_updateHotelManagerID.setInt(2, hotelID);
                        jdbcPrep_updateHotelManagerID.executeUpdate();
                        // Promote new manager
                        jdbcPrep_promoteNewManager.setInt(1, hotelID);
                        jdbcPrep_promoteNewManager.setInt(2, Integer.parseInt(valueToChangeTo));
                        jdbcPrep_promoteNewManager.executeUpdate();
                        // If success, commit
                        jdbc_connection.commit();
                    }
                    catch (Throwable err) {
                        // Handle error
                        handleError(err);
                        // Roll back the entire transaction
                        jdbc_connection.rollback(); 
                    }
                    finally {
                        // Restore normal auto-commit mode
                        jdbc_connection.setAutoCommit(true);
                    }
                    break;
                default:
                    System.out.println(
                        "\nCannot update the '" + attributeToChange + "' attribute of a hotel, because this is not a recognized attribute for Wolf Inns hotels\n"
                    );
                    break;
            }
            
        }
        catch (Throwable err) {
            handleError(err);            
        }
        
    }
    
    /** 
     * DB Update: Delete Hotel
     * 
     * Why does this method exist at all when it is so dead simple?
     * 1. To keep with the pattern of isolating methods that directly interact with the DBMS, 
     * from those that interact with the user (readability of code)
     * 2. In case in the future we find some need for mass deletes.
     * 
     * Arguments -  hotelID -       The ID of the hotel
     *              reportSuccess - True if we should print success message to console (should be false for mass deletion of hotels)
     * Return -     None
     * 
     * Modifications:   03/09/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/24/18 -  ATTD -  Use prepared statement.
     *                                      Do not claim success unless the hotel actually got deleted.
     */
    public static void updateDeleteHotel (int hotelID, boolean reportSuccess) {

        try {

            /* Remove the hotel from the Hotels table
             * No need to explicitly set up a transaction, because only one SQL command is needed
             */
            jdbcPrep_deleteHotel.setInt(1, hotelID);
            jdbcPrep_deleteHotel.executeUpdate();
            
            // Did the deletion succeed?
            jdbcPrep_getHotelByID.setInt(1, hotelID);
            jdbc_result = jdbcPrep_getHotelByID.executeQuery();
            if (jdbc_result.next()) {
                // Always complain about a failure (never fail silently)
                System.out.println(
                    "\nHotel ID " + 
                    hotelID + 
                    " was NOT deleted (this can happen if a customer is currently staying in the hotel)\n"
                );
            }
            else {
                // Tell the user about the success, if we're supposed to
                if (reportSuccess) {
                    System.out.println("\nHotel ID " + hotelID + " deleted!\n");
                }
            }
            
        }
        catch (Throwable err) {
             handleError(err);
        }
        
    }
    
    /** 
     * DB Update: Insert Staff Member
     * 
     * Arguments -  name -          The name of the new staff member
     *              dob -           The date of birth of the new staff member
     *              jobTitle -      The job title of the new staff member
     *              department -    The department of the new staff member
     *              phoneNum -      The phone number of the new staff member
     *              address -       The address of the new staff member
     *              hotelID -       The ID of the hotel to which the staff member is assigned (OR zero if not assigned to any hotel)
     *              reportSuccess - True if we should print success message to console (should be false for mass population of hotels)
     * Return -     None
     * 
     * Modifications:   03/24/18 -  ATTD -  Created method.
     */
    public static void updateInsertStaff (String name, String dob, String jobTitle, String department, long phoneNum, String address, int hotelID, boolean reportSuccess) {
        
        // Declare variables
        int newStaffID;
        
        try {

            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {

                /* Insert new staff member, using prepared statement
                 * If a zero is passed for hotel ID,
                 * that means the new staff member isn't to be assigned to any particular hotel,
                 * so set hotel ID in the tuple to NULL
                 */
                jdbcPrep_insertNewStaff.setString(1, name);
                jdbcPrep_insertNewStaff.setString(2, dob);
                jdbcPrep_insertNewStaff.setString(3, jobTitle);
                jdbcPrep_insertNewStaff.setString(4, department);
                jdbcPrep_insertNewStaff.setLong(5, phoneNum);
                jdbcPrep_insertNewStaff.setString(6, address);
                if (hotelID == 0) {
                    jdbcPrep_insertNewStaff.setNull(7, java.sql.Types.INTEGER);
                }
                else {
                    jdbcPrep_insertNewStaff.setInt(7, hotelID); 
                }
                jdbcPrep_insertNewStaff.executeUpdate();

                // If success, commit
                jdbc_connection.commit();
                
                // Then, tell the user about the success
                if (reportSuccess) {
                    jdbc_result = jdbcPrep_getNewestStaffID.executeQuery();
                    jdbc_result.next();
                    newStaffID = jdbc_result.getInt(1);
                    System.out.println("\n'" + name + "' staff member added (staff ID: " + newStaffID + ")!\n");
                }
                
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * DB Update: Change Staff Information
     * 
     * Arguments -  staffID -           The ID of the staff member to update
     *              attributeToChange - The attribute to update
     *              valueToChangeTo -   The value to update this attribute to (as a string)
     * Return -     None
     * 
     * Modifications:   03/27/18 -  ATTD -  Created method.
     */
    public static void updateChangeStaffInfo (int staffID, String attributeToChange, String valueToChangeTo) {
        
        try {

            // Update hotel info, using prepared statement
            switch (attributeToChange) {
                case "Name":
                    jdbcPrep_updateStaffName.setString(1, valueToChangeTo);
                    jdbcPrep_updateStaffName.setInt(2, staffID);
                    jdbcPrep_updateStaffName.executeUpdate();
                    break;
                case "DOB":
                    jdbcPrep_updateStaffDOB.setDate(1, java.sql.Date.valueOf(valueToChangeTo));
                    jdbcPrep_updateStaffDOB.setInt(2, staffID);
                    jdbcPrep_updateStaffDOB.executeUpdate();
                    break;
                case "JobTitle":
                    jdbcPrep_updateStaffJobTitle.setString(1, valueToChangeTo);
                    jdbcPrep_updateStaffJobTitle.setInt(2, staffID);
                    jdbcPrep_updateStaffJobTitle.executeUpdate();
                    break;
                case "Dep":
                    jdbcPrep_updateStaffDepartment.setString(1, valueToChangeTo);
                    jdbcPrep_updateStaffDepartment.setInt(2, staffID);
                    jdbcPrep_updateStaffDepartment.executeUpdate();
                    break;
                case "PhoneNum":
                    jdbcPrep_updateStaffPhoneNum.setLong(1, Long.parseLong(valueToChangeTo));
                    jdbcPrep_updateStaffPhoneNum.setInt(2, staffID);
                    jdbcPrep_updateStaffPhoneNum.executeUpdate();
                    break;
                case "Address":
                    jdbcPrep_updateStaffAddress.setString(1, valueToChangeTo);
                    jdbcPrep_updateStaffAddress.setInt(2, staffID);
                    jdbcPrep_updateStaffAddress.executeUpdate();
                    break;
                case "HotelID":
                    jdbcPrep_updateStaffHotelID.setInt(1, Integer.parseInt(valueToChangeTo));
                    jdbcPrep_updateStaffHotelID.setInt(2, staffID);
                    jdbcPrep_updateStaffHotelID.executeUpdate();
                    break;
                default:
                    System.out.println(
                        "\nCannot update the '" + attributeToChange + "' attribute of a staff member, because this is not a recognized attribute for Wolf Inns staff\n"
                    );
                    break;
            }
            
        }
        catch (Throwable err) {
            handleError(err);            
        }
        
    }
    
    /** 
     * DB Update: Delete Staff Member
     * 
     * Arguments -  staffID -       The ID of the staff member
     *              reportSuccess - True if we should print success message to console (should be false for mass deletion of staff members)
     * Return -     None
     * 
     * Modifications:   03/12/18 -  ATTD -  Created method.
     *                  03/23/18 -  ATTD -  Use new general error handler.
     *                  03/24/18 -  ATTD -  Use prepared statement.
     *                                      Do not claim success unless the staff member actually got deleted.
     */
    public static void updateDeleteStaff (int staffID, boolean reportSuccess) {

        try {
            
            /* Remove the staff member from the Staff table
             * No need to explicitly set up a transaction, because only one SQL command is needed
             */
            jdbcPrep_deleteStaff.setInt(1, staffID);
            jdbcPrep_deleteStaff.executeUpdate();
            
            // Did the deletion succeed?
            jdbcPrep_getStaffByID.setInt(1, staffID);
            jdbc_result = jdbcPrep_getStaffByID.executeQuery();
            if (jdbc_result.next()) {
                // Always complain about a failure (never fail silently)
                System.out.println(
                    "\nStaff ID " + 
                    staffID + 
                    " was NOT deleted " + 
                    "(this can happen if the staff member is currently dedicated to serving a room in which a guest is staying)\n"
                );
            }
            else {
                // Tell the user about the success, if we're supposed to
                if (reportSuccess) {
                    System.out.println("\nStaff ID " + staffID + " deleted!\n");
                }
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    } 
    
    /** 
     * DB Update: Add new room
     * 
     * Arguments -  roomNumber    - Room number 
     *              hotelId       - Hotel to which the room belongs
     *              category      - Room Category ('Economy', 'Deluxe', 'Executive Suite', 'Presidential Suite')
     *              maxOccupancy  - Max Occupancy for the room
     *              nightlyRate   - Nightly rate for the room
     *              reportSuccess - True if need to print success message after method completes
     * Return -     None
     * 
     * Modifications:   03/24/18 -  MTA -  Added method. 
     * 					03/25/18 -  MTA -  Updated method signature.
     */
    public static void addRoom( int roomNumber, int hotelId, String category, int maxOccupancy, int nightlyRate, boolean reportSuccess){
    
        try {

            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {
            	           
            	jdbcPrep_insertNewRoom.setInt(1, roomNumber);
            	jdbcPrep_insertNewRoom.setInt(2, hotelId);
            	jdbcPrep_insertNewRoom.setString(3, category);
            	jdbcPrep_insertNewRoom.setInt(4, maxOccupancy);
            	jdbcPrep_insertNewRoom.setInt(5, nightlyRate);  
                 
            	jdbcPrep_insertNewRoom.executeUpdate();

                // If success, commit
                jdbc_connection.commit();
                
                if (reportSuccess)
                {
                	System.out.println("\nRoom has been successfully added to the database! \n");
                } 
            } 
            catch (Throwable ex) {
                
                // Handle pk violation
            	if (ex.getMessage() != null && ex.getMessage().matches("(.*)Duplicate entry(.*)for key 'PRIMARY'(.*)")) { 
            		handleError(ex, "PK_ROOMS");
            	} else {
            		handleError(ex);	
            	} 
                
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
    }
  
     /** 
     * DB Update: Add new customer
     * 
     * Arguments -  ssn           - Customer Social Security Number
     *              name          - Customer Name
     *              dob      	  - Customer Date Of Birth
     *              phoneNumber   - Customer Phone Number
     *              email         - Customer email
     *              reportSuccess - True if need to print success message after method completes
     * Return -     None
     * 
     * Modifications:   03/27/18 -  MTA -  Added method. 
     */
    public static void addCustomer( String ssn, String name, String dob, String phoneNumber, String email, boolean reportSuccess){
    
        try {

            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {             	
            	
            	jdbcPrep_insertNewCustomer.setLong(1, Long.parseLong(ssn));
            	jdbcPrep_insertNewCustomer.setString(2, name);
            	jdbcPrep_insertNewCustomer.setDate(3, java.sql.Date.valueOf(dob));
            	jdbcPrep_insertNewCustomer.setLong(4, Long.parseLong(phoneNumber));
            	jdbcPrep_insertNewCustomer.setString(5, email);  
                 
            	jdbcPrep_insertNewCustomer.executeUpdate();

                // If success, commit
                jdbc_connection.commit();
                
                if (reportSuccess)
                {
                	System.out.println("\nCustomer has been successfully added to the database! \n");
                } 
            
            } 
            catch (Throwable ex) {
                
                // Handle pk violation
            	if (ex.getMessage() != null && ex.getMessage().matches("(.*)Duplicate entry(.*)for key 'PRIMARY'(.*)")) { 
            		handleError(ex, "PK_CUSTOMERS");
            	} else {
            		handleError(ex);	
            	} 
                
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true); 
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
    }
    
    /** 
	 * DB Update: Update customer details
	 * 
	 * Arguments -  ssn   - Customer Social Security Number 
	 *              columnName    - Name of the column for Rooms table that is being updated 
	 *              columnValue   - Value of column for Rooms table that is being updated 
	 *              reportSuccess - True if need to print success message after method completes 
	 * Return -     None
	 * 
	 * Modifications:   03/28/18 -  MTA -  Added method. 
	 */
	public static void updateCustomer( String ssn, String columnName, String columnValue, boolean reportSuccess){
	
	    try {
	
	        // Start transaction
	        jdbc_connection.setAutoCommit(false);
	        
	        try {
	        	
	        	switch (columnName) {
				case "Name":
					jdbcPrep_updateCustomerName.setString(1, columnValue); 
					jdbcPrep_updateCustomerName.setLong(2, Long.parseLong(ssn)); 
					jdbcPrep_updateCustomerName.executeUpdate();
					break;
					
				case "DOB":
					jdbcPrep_updateCustomerDateOfBirth.setString(1, columnValue); 
					jdbcPrep_updateCustomerDateOfBirth.setLong(2, Long.parseLong(ssn)); 
					jdbcPrep_updateCustomerDateOfBirth.executeUpdate();
					break;
					
				case "PhoneNum":
					jdbcPrep_updateCustomerPhoneNumber.setString(1, columnValue); 
					jdbcPrep_updateCustomerPhoneNumber.setLong(2, Long.parseLong(ssn)); 
					jdbcPrep_updateCustomerPhoneNumber.executeUpdate();
					break;
					
				case "Email":
					jdbcPrep_updateCustomerEmail.setString(1, columnValue); 
					jdbcPrep_updateCustomerEmail.setLong(2, Long.parseLong(ssn)); 
					jdbcPrep_updateCustomerEmail.executeUpdate();
					break;

				default:
					System.out.println(
                        "\nCannot update the '" + columnName + "' attribute of customer, because this is not a recognized attribute for Wolf Inns Customers\n"
                    );
					break;
				}
	        	 
	            // If success, commit
	            jdbc_connection.commit();
	            
	            if (reportSuccess)
	            {
	            	System.out.println("\nCustomer details were successfully updated! \n");        	
	            } 
	        } 
	        catch (Throwable ex) {
	             
	        	handleError(ex);	
	        	 
	            // Roll back the entire transaction
	            jdbc_connection.rollback();
	            
	        }
	        finally {
	            // Restore normal auto-commit mode
	            jdbc_connection.setAutoCommit(true); 
	        }
	        
	    }
	    catch (Throwable err) {
	        handleError(err);
	    }
	}
    
	// TODO: move this so that it goes: addRoom, updateRoom, deleteRoom
	/** 
	 * DB Update: Update room details
	 * 
	 * Arguments -  roomNumber    - Room number 
	 *              hotelId       - Hotel to which the room belongs
	 *              columnName    - Name of the column for Rooms table that is being updated 
	 *              columnValue   - Value of column for Rooms table that is being updated 
	 *              reportSuccess - True if need to print success message after method completes 
	 * Return -     None
	 * 
	 * Modifications:   03/25/18 -  MTA -  Added method. 
	 */
	public static void updateRoom( int roomNumber, int hotelId, String columnName, String columnValue, boolean reportSuccess){
	
	    try {
	
	        // Start transaction
	        jdbc_connection.setAutoCommit(false);
	        
	        try {
	        	
	        	switch (columnName) {
				case "Category":
					jdbcPrep_updateRoomCategory.setString(1, columnValue); 
					jdbcPrep_updateRoomCategory.setInt(2, roomNumber);
					jdbcPrep_updateRoomCategory.setInt(3, hotelId); 
					jdbcPrep_updateRoomCategory.executeUpdate();
					break;

				case "MaxOcc":
					jdbcPrep_updateRoomMaxOccupancy.setString(1, columnValue); 
					jdbcPrep_updateRoomMaxOccupancy.setInt(2, roomNumber);
					jdbcPrep_updateRoomMaxOccupancy.setInt(3, hotelId); 
					jdbcPrep_updateRoomMaxOccupancy.executeUpdate();
					break;
					
				case "NightlyRate":
					jdbcPrep_updateRoomNightlyRate.setString(1, columnValue); 
					jdbcPrep_updateRoomNightlyRate.setInt(2, roomNumber);
					jdbcPrep_updateRoomNightlyRate.setInt(3, hotelId); 
					jdbcPrep_updateRoomNightlyRate.executeUpdate();
					break;
					
				case "DRSStaff":
					jdbcPrep_updateRoomDRSStaff.setString(1, columnValue); 
					jdbcPrep_updateRoomDRSStaff.setInt(2, roomNumber);
					jdbcPrep_updateRoomDRSStaff.setInt(3, hotelId); 
					jdbcPrep_updateRoomDRSStaff.executeUpdate();
					break;
					
				case "DCStaff":
					jdbcPrep_updateRoomDCStaff.setString(1, columnValue); 
					jdbcPrep_updateRoomDCStaff.setInt(2, roomNumber);
					jdbcPrep_updateRoomDCStaff.setInt(3, hotelId); 
					jdbcPrep_updateRoomDCStaff.executeUpdate();
					break;

				default:
					System.out.println(
	                    "\nCannot update the '" + columnName + "' attribute of room, because this is not a recognized attribute for Wolf Inns Rooms\n"
	                );
					break;
				}
	
	            // If success, commit
	            jdbc_connection.commit();
	            
	            if (reportSuccess)
	            {
	            	System.out.println("\nRoom details were successfully updated! \n");        	
	            } 
	        } 
	        catch (Throwable ex) {
	        	
	        	handleError(ex);	
	        	 
	            // Roll back the entire transaction
	            jdbc_connection.rollback();
	            
	        }
	        finally {
	            // Restore normal auto-commit mode
	            jdbc_connection.setAutoCommit(true); 
	        }
	        
	    }
	    catch (Throwable err) {
	        handleError(err);
	    }
	}
    
	// TODO: move this so that it goes: addRoom, updateRoom, deleteRoom
    /** 
     * DB Update: Delete room
     * 
     * Arguments -  hotelId       - Hotel to which the room belongs
     * 				roomNumber    - Room number  
     *              reportSuccess - True if need to print success message after method completes
     * Return -     None
     * 
     * Modifications:   03/25/18 -  MTA -  Added method. 
     */
    public static void deleteRoom(int hotelId, int roomNumber, boolean reportSuccess) {
    	try {

            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {                      
  
            	jdbcPrep_deleteRoom.setInt(1, roomNumber);
            	jdbcPrep_deleteRoom.setInt(2, hotelId); 
            	
            	jdbcPrep_deleteRoom.executeUpdate();

                // If success, commit
                jdbc_connection.commit();
                
                if (reportSuccess)
                {
                	System.out.println("\nRoom has been successfully deleted from the database! \n");        	
                } 
            } 
            catch (Throwable ex) {
                
                handleError(ex);	 
                
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true); 
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
    }
    
    /** 
     * DB Update: Delete customer
     * 
     * Arguments -  ssn       -  Customer Social Security Number 
     *              reportSuccess - True if need to print success message after method completes
     * Return -     None
     * 
     * Modifications:   03/28/18 -  MTA -  Added method. 
     */
    public static void deleteCustomer(String ssn, boolean reportSuccess) {
    	try {

            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {                      
  
            	jdbcPrep_deleteCustomer.setLong(1, Long.parseLong(ssn)); 
            	
            	jdbcPrep_deleteCustomer.executeUpdate();

                // If success, commit
                jdbc_connection.commit();
                
                if (reportSuccess)
                {
                	System.out.println("\nCustomer has been successfully deleted from the database! \n");        	
                } 
            } 
            catch (Throwable ex) {
                
                handleError(ex);	 
                
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true); 
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
    }

    /** 
     * DB Update: Insert Stay
     * 
     * Arguments -  roomNum -           The room the customer will stay in
     *              hotelID -           The hotel the customer will stay in
     *              customerSSN -       The customer's SSN
     *              numGuests -         The number of guests staying in the room
     *              paymentMethod -     The method of payment (card, etc).
     *              cardType -          The type of card (or blank string if not paying with card)
     *              cardNumber -        The card number (or -1 if not paying with card)
     *              billingAddress -    The billing address (or blank string if not paying with card)
     *              reportSuccess -     True if we should print success message to console (should be false for mass population of hotels)
     * Return -     None
     * 
     * Modifications:   04/01/18 -  ATTD -  Created method.
     */
    public static void updateInsertStay (int roomNum, int hotelID, long customerSSN, int numGuests, String paymentMethod, String cardType, long cardNumber, String billingAddress, boolean reportSuccess) {
        
        // Declare variables
        String roomType = "";
        
        try {

            // Start transaction
            jdbc_connection.setAutoCommit(false);
            
            try {

                /* Insert new stay, using prepared statement
                 * If the payment method is not "CARD", pass NULL for card type, card number, and billing address
                 * Indices to use when calling this prepared statement:
                 * 1 -  Customer SSN
                 * 2 -  Number of guests
                 * 3 -  Payment method
                 * 4 -  Card type
                 * 5 -  Card number
                 * 6 -  Billing address
                 * 7 -  Room number
                 * 8 -  Hotel ID
                 * 9 -  Payment method (again)
                 * 10 - Card type (again)
                 * 11 - Card number (again)
                 * 12 - Billing address (again)
                 */
                jdbcPrep_assignRoom.setLong(1, customerSSN);
                jdbcPrep_assignRoom.setInt(2, numGuests);
                jdbcPrep_assignRoom.setString(3, paymentMethod);
                jdbcPrep_assignRoom.setString(9, paymentMethod);
                if (paymentMethod.equals("CARD")) {
                    jdbcPrep_assignRoom.setString(4, cardType);
                    jdbcPrep_assignRoom.setString(10, cardType);
                    jdbcPrep_assignRoom.setLong(5, cardNumber);
                    jdbcPrep_assignRoom.setLong(11, cardNumber);
                    jdbcPrep_assignRoom.setString(6, billingAddress);
                    jdbcPrep_assignRoom.setString(12, billingAddress);
                }
                else {
                    jdbcPrep_assignRoom.setNull(4, java.sql.Types.VARCHAR);
                    jdbcPrep_assignRoom.setNull(10, java.sql.Types.VARCHAR);
                    jdbcPrep_assignRoom.setNull(5, java.sql.Types.BIGINT);
                    jdbcPrep_assignRoom.setNull(11, java.sql.Types.BIGINT);
                    jdbcPrep_assignRoom.setNull(6, java.sql.Types.VARCHAR);
                    jdbcPrep_assignRoom.setNull(12, java.sql.Types.VARCHAR);
                }
                jdbcPrep_assignRoom.setInt(7, roomNum);
                jdbcPrep_assignRoom.setInt(8, hotelID);
                jdbcPrep_assignRoom.executeUpdate();
                
                /* Assign dedicated staff to a presidential suite (if that's the room type)
                 * Indices to use when calling this prepared statement:
                 * 1 -  Hotel ID
                 * 2 -  Room number
                 * 3 -  Hotel ID (again)
                 * 4 -  Room number (again)
                 * 5 -  Hotel ID (again)
                 */
                jdbcPrep_getRoomByHotelIDRoomNum.setInt(1, roomNum);
                jdbcPrep_getRoomByHotelIDRoomNum.setInt(2, hotelID);
                jdbc_result = jdbcPrep_getRoomByHotelIDRoomNum.executeQuery();
                while (jdbc_result.next()) {
                    roomType = jdbc_result.getString("RoomType");     
                }
                if (roomType == "PRESIDENTIAL_SUITE") {
                    jdbcPrep_assignDedicatedStaff.setInt(1, hotelID);
                    jdbcPrep_assignDedicatedStaff.setInt(2, roomNum);
                    jdbcPrep_assignDedicatedStaff.setInt(3, hotelID);
                    jdbcPrep_assignDedicatedStaff.setInt(4, roomNum);
                    jdbcPrep_assignDedicatedStaff.setInt(5, hotelID);
                    jdbcPrep_assignDedicatedStaff.executeUpdate();
                }

                // If success, commit
                jdbc_connection.commit();
                
                // Then, tell the user about the success
                if (reportSuccess) {
                    System.out.println("\nRoom Assigned!\n");
                    jdbc_result = jdbcPrep_getNewestStay.executeQuery();
                    printQueryResultSet(jdbc_result);
                }
                
            }
            catch (Throwable err) {
                
                // Handle error
                handleError(err);
                
                // Roll back the entire transaction
                jdbc_connection.rollback();
                
            }
            finally {
                // Restore normal auto-commit mode
                jdbc_connection.setAutoCommit(true);
            }
            
        }
        catch (Throwable err) {
            handleError(err);
        }
        
    }
    
    /** 
     * DB Check: Check if room number is associated with given hotel
     * 
     * Arguments -  roomNumber    - Room number
     *              hotelId       - Hotel Id 
     * Return -     boolean       - True if the room number is associated with given hotel
      * 
     * Modifications:   03/24/18 -  MTA -  Added method. 
     */
    public static boolean isValidRoomForHotel(int hotelId, int roomNumber){  
    	
    	try {      		
	        try {				
				 jdbcPrep_isValidRoomNumber.setInt(1, hotelId);
				 jdbcPrep_isValidRoomNumber.setInt(2, roomNumber);   
				 
				 ResultSet rs = jdbcPrep_isValidRoomNumber.executeQuery();
				 int cnt = 0;
				 
				 while (rs.next()) {
					cnt = rs.getInt("CNT"); 	
				 }
				 
				 if (cnt > 0) { 
					 return true;
				 }   
				 
	        }
	        catch (Throwable err) {
	            handleError(err);
	        }  
    	} 
    	catch (Throwable err) { 
    		handleError(err); 
        }
        
        return false; 
    }
    
    /** 
     * DB Check: Check if given hotel id exists in the database
     * 
     * Arguments -  hotelId       - Hotel Id  
     * Return -     boolean       - True if the hotel id exists
     * 
     * Modifications:   03/25/18 -  MTA -  Added method. 
     */
    public static boolean isValidHotelId(int hotelId) {
    	try {   	        
    		try {  				 
				 jdbcPrep_isValidHotelId.setInt(1, hotelId); 
				 
				 ResultSet rs = jdbcPrep_isValidHotelId.executeQuery();
				 int cnt = 0;
				 
				 while (rs.next()) {
					cnt = rs.getInt("CNT"); 	
				 }
				 
				 if (cnt > 0) { 
					 return true;
				 }   
				 
	        }
	        catch (Throwable err) {
	            handleError(err);
	        }  
    	} 
    	catch (Throwable err) { 
    		handleError(err); 
        }
        
        return false; 
    }
    
    /** 
     * DB Check: Check if room number for given hotel is currently occupied
     * 
     * Arguments -  roomNumber    - Room number
     *              hotelId       - Hotel Id 
     * Return -     boolean       - True if the room number for given hotel is currently occupied by guests
     * 
     * Modifications:   03/25/18 -  MTA -  Added method. 
     */
    public static boolean isRoomCurrentlyOccupied(int hotelId, int roomNumber){  
    	
    	try {      		
	        try {	        				 
				 jdbcPrep_isRoomCurrentlyOccupied.setInt(1, hotelId);
				 jdbcPrep_isRoomCurrentlyOccupied.setInt(2, roomNumber);   
				 
				 ResultSet rs = jdbcPrep_isRoomCurrentlyOccupied.executeQuery();
				 int cnt = 0;
				 
				 while (rs.next()) {
					cnt = rs.getInt("CNT"); 	
				 }
				 
				 if (cnt > 0) { 
					 return true;
				 }   
				 
	        }
	        catch (Throwable err) {
	            handleError(err);
	        } 
    	} 
    	catch (Throwable err) { 
    		handleError(err); 
        }
        
        return false; 
    }
    
    /** 
     * DB Check: Check if customer exists in Customers table
     * 
     * Arguments -  ssn       - Customer SSN  
     * Return -     boolean   - True if the customer is present in Customers table
      * 
     * Modifications:   03/28/18 -  MTA -  Added method. 
     */
    public static boolean isValidCustomer(String ssn){  
    	
    	try {      		
	        try {				
	        	jdbcPrep_isValidCustomer.setLong(1, Long.parseLong(ssn)); 
				 
				 ResultSet rs = jdbcPrep_isValidCustomer.executeQuery();
				 int cnt = 0;
				 
				 while (rs.next()) {
					cnt = rs.getInt("CNT");  	
				 }
				 
				 if (cnt == 1) { 
					 return true;
				 }   
				 
	        }
	        catch (Throwable err) {
	            handleError(err);
	        }  
    	} 
    	catch (Throwable err) { 
    		handleError(err); 
        }
        
        return false; 
    }
    
    /** 
     * DB Check: Check if customer is associated with current guest stay 
     * 
     * Arguments -  ssn       - Room number                 
     * Return -     boolean   - True if the customer is associated with current guest stay 
     * 
     * Modifications:   03/28/18 -  MTA -  Added method. 
     */
    public static boolean isCustomerCurrentlyStaying(String ssn){  
    	
    	try {      		
	        try {	        				 
				 jdbcPrep_isCustomerCurrentlyStaying.setLong(1, Long.parseLong(ssn)); 
				 
				 ResultSet rs = jdbcPrep_isCustomerCurrentlyStaying.executeQuery();
				 int cnt = 0;
				 
				 while (rs.next()) {
					cnt = rs.getInt("CNT"); 	
				 }
				 
				 if (cnt > 0) { 
					 return true;
				 }   
				 
	        }
	        catch (Throwable err) {
	            handleError(err);
	        } 
    	} 
    	catch (Throwable err) { 
    		handleError(err); 
        }
        
        return false; 
    }
    
    /** 
     * General error handler
     * Turn obscure error stack into human-understandable feedback
     * 
     * Arguments -  err -   An error object.
     *           -  pkViolation - Name of the primary key constraint being violated
     * Return -     None
     * 
     * Modifications:   03/23/18 -  ATTD -  Created method.
     *                  03/24/18 -  MTA -   Handle primary key violation.
     *                  03/28/18 -  ATTD -  Handle unknown column in WHERE clause.
     */
    public static void handleError(Throwable err, String... pkViolation) {
        
        // Declare variables
        String errorMessage;
        
        try {
            
            // TODO: Manjusha noted an issue that revealed - we do not yet know how to tell the user about primary key problem (non-unique insert attempted)
            
            // Handle specific errors
            errorMessage = err.toString();
            
            // HOTELS constraint violated
            if (errorMessage.contains("UC_HACS")) {
                System.out.println(
                    "\nCannot use this address / city / state for the hotel, because it is already used for another hotel\n"
                );
            }
            else if (errorMessage.contains("UC_HPN")) {
                System.out.println(
                    "\nCannot use this phone number for the hotel, because it is already used for another hotel\n"
                );
            }
            else if (errorMessage.contains("UC_HMID")) {
                System.out.println(
                    "\nCannot use this manager for the hotel, because they are already managing another hotel\n"
                );
            }
            else if (errorMessage.contains("FK_HMID")) {
                System.out.println(
                        "\nCannot use this manager for the hotel, because they are not registered as a Wolf Inns staff member\n"
                    );
            }
            else if (errorMessage.contains("PK_HOTELS")) {
                System.out.println(
                    "\nCannot use this ID for the hotel, because it is already used for another hotel\n"
                );
            }
            
            // STAFF constraint violated
            else if (errorMessage.contains("FK_STAFFHID")) {
                System.out.println(
                    "\nCannot assign the staff member to this hotel, because it is not registered as a Wolf Inns hotel\n"
                );
            }
            else if (errorMessage.contains("PK_STAFF")) {
                System.out.println(
                    "\nCannot use this ID for the Staff, because it is already used for another staff member\n"
                );
            }
            
            // ROOMS constraint violated
            else if (errorMessage.contains("FK_ROOMHID")) {
                System.out.println(
                    "\nCannot assign the room to this hotel, because it is not registered as a Wolf Inns hotel\n"
                );
            }
            else if (errorMessage.contains("FK_ROOMDRSID")) {
                System.out.println(
                    "\nCannot assign this staff member as dedicated room service staff, because they are not registered as a Wolf Inns staff member\n"
                );
            }
            else if (errorMessage.contains("FK_ROOMDCID")) {
                System.out.println(
                    "\nCannot assign this staff member as dedicated catering staff, because they are not registered as a Wolf Inns staff member\n"
                );
            }
            else if (errorMessage.contains("PK_ROOMS") || (pkViolation.length > 0 && pkViolation[0].contains("PK_ROOMS"))) {
            	System.out.println(
                    "\nCannot use this room number for the hotel, because it is already used for another room\n"
                );
            }
            
            // STAYS constraint violated
            else if (errorMessage.contains("UC_STAYKEY")) {
                System.out.println(
                    "\nCannot use this combination of start date / check in time / room number / hotel ID the stay, because it is already used for another stay\n"
                );
            }
            else if (errorMessage.contains("FK_STAYRID")) {
                System.out.println(
                    "\nCannot use this combination of room number / hotel ID the stay, because it is not registered as a Wolf Inns hotel room\n"
                );
            }
            else if (errorMessage.contains("FK_STAYCSSN")) {
                System.out.println(
                    "\nCannot use this customer SSN the stay, because there is no registered Wolf Inns customer with that SSN\n"
                );
            }
            else if (errorMessage.contains("PK_STAYS")) {
            	System.out.println(
                    "\nCannot use this ID for the stay, because it is already used for another stay\n"
                );
            }
                        
            // PROVIDED constraint violated
            else if (errorMessage.contains("FK_PROVSTAYID")) {
                System.out.println(
                    "\nCannot use this stay ID for the provided service, because it is not registerd as a Wolf Inns customer stay\n"
                );
            }
            else if (errorMessage.contains("FK_PROVSTAFFID")) {
                System.out.println(
                    "\nCannot use this staff member for the provided service, because they are not registerd as a Wolf Inns staff member\n"
                );
            }
            else if (errorMessage.contains("FK_PROVSERV")) {
                System.out.println(
                    "\nCannot use this service name for the provided service, because it is not registerd as a Wolf Inns available service\n"
                );
            }
            else if (errorMessage.contains("PK_PROVIDED")) {
                System.out.println(
                    "\nCannot use this ID for the service provided, because it is already used for another provided service\n"
                );
            }
            
            // Customer constraint violated
            else if (errorMessage.contains("PK_CUSTOMERS") || (pkViolation.length > 0 && pkViolation[0].contains("PK_CUSTOMERS"))) {
                System.out.println(
                    "\nCannot use this SSN for the customer, because it is already used for another customer\n"
                );
            }
            
            // SERVICETYPES constraint violated
            else if (errorMessage.contains("PK_SERVICE_TYPES")) {
                System.out.println(
                    "\nCannot use this name for the service type, because it is already used for another service type\n"
                );
            }
            
            // Used bad column (non-existent attribute)
            else if (errorMessage.contains("Unknown column")) {
                System.out.println(
                    "\nCannot use this attribute - it does not exist (check spelling)\n"
                );
            }
            
            // Number format error
            else if (errorMessage.contains("NumberFormatException")) {
                System.out.println("Cannot use this value because it is not a number\n");
            }
            
            // Illegal Argument Exception
            else if (errorMessage.contains("IllegalArgumentException")) {
                System.out.println("Cannot use this value because it it not valid\n");
            }
            
            // Primary Key Violation
            else if (errorMessage.contains("SQLIntegrityConstraintViolationException")) {
                System.out.println("Primary Key Constraint violated\n");
            }            
            
            // Don't know what happened, best we can do is print the stack trace as-is
            else {
                err.printStackTrace();
            }
            
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        
    }
    
    // MAIN
    
    /* MAIN function
     * 
     * Welcomes the user, states available commands, listens to and acts on user commands
     * 
     * Arguments -  None
     * Return -     None
     * 
     * Modifications:   03/07/18 -  ATTD -  Created method.
     *                  03/08/18 -  ATTD -  Add ability to print entire Provided table.
     *                  03/08/18 -  ATTD -  Add sub-menus (report, etc) off of main menu.
     *                  03/09/18 -  ATTD -  Add ability to delete a hotel.
     *                  03/11/18 -  ATTD -  Add ability to report revenue.
     *                  03/11/18 -  ATTD -  Add ability to generate bill for customer stay.
     *                  03/12/18 -  ATTD -  Add ability to delete staff member.
     *                  03/20/18 -  ATTD -  Add call to new method for creating prepared statements.
     *                  03/21/18 -  ATTD -  Close more resources during clean-up.
     *                  03/23/18 -  ATTD -  Add ability to update basic information about a hotel.
     *                                      Use new general error handler.
     *                  03/24/18 -  MTA -   Add ability to add room.
     *                  03/24/18 -  ATTD -  Close prepared statement for deleting a hotel.
     *                  03/24/18 -  ATTD -  Add ability to insert new staff member.
     *                  03/26/18 -  ATTD -  Add ability to update basic info about a staff member.
     *                  03/27/18 -  ATTD -  Use prepared statement to delete staff.
     *                                      Add ability to check if rooms are available.
     *                  03/27/18 -  MTA -   Add ability to add, update and delete customer.
     *                  04/01/18 -  ATTD -  Add ability to assign a room to a customer.
     */
    public static void main(String[] args) {
        
        try {
        
            // Declare local variables
            boolean quit = false;
            String command;
            
            // Print welcome
            System.out.println("\nWelcome to Wolf Inns Hotel Management System");
            
            // Connect to database
            System.out.println("\nConnecting to database...");
            connectToDatabase();
            
            // Create prepared statements
            System.out.println("\nCreating prepared statements...");
            createPreparedStatements();
            
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
            updateAmountOwedForStays();
            
            // Print available commands
            printAvailableCommands(CMD_MAIN);
            
            // Watch for user input
            currentMenu = CMD_MAIN;
            scanner = new Scanner(System.in);
            while (quit == false) {
                System.out.print("> ");
                command = scanner.nextLine();
                switch (currentMenu) {
                    case CMD_MAIN:
                        // Check user's input (case insensitively)
                        switch (command.toUpperCase()) {
                            case CMD_FRONTDESK:
                                // Tell the user their options in this new menu
                                printAvailableCommands(CMD_FRONTDESK);
                                // Remember what menu we're in
                                currentMenu = CMD_FRONTDESK;
                            break;
                            case CMD_BILLING:
                                // Tell the user their options in this new menu
                                printAvailableCommands(CMD_BILLING);
                                // Remember what menu we're in
                                currentMenu = CMD_BILLING;
                            break;
                            case CMD_REPORTS:
                                // Tell the user their options in this new menu
                                printAvailableCommands(CMD_REPORTS);
                                // Remember what menu we're in
                                currentMenu = CMD_REPORTS;
                                break;
                            case CMD_MANAGE:
                                // Tell the user their options in this new menu
                                printAvailableCommands(CMD_MANAGE);
                                // Remember what menu we're in
                                currentMenu = CMD_MANAGE;
                                break;
                            case CMD_QUIT:
                                quit = true;
                                break;
                            default:
                                // Remind the user about what commands are available
                                System.out.println("\nCommand not recognized");
                                printAvailableCommands(CMD_MAIN);
                                break;
                        }
                        break;
                    case CMD_FRONTDESK:
                        // Check user's input (case insensitively)
                        switch (command.toUpperCase()) {
                            case CMD_FRONTDESK_AVAILABLE:
                                frontDeskCheckAvailability();
                                break;
                            case CMD_FRONTDESK_ASSIGN:
                                frontDeskAssignRoom();
                                break;
                            case CMD_MAIN:
                                // Tell the user their options in this new menu
                                printAvailableCommands(CMD_MAIN);
                                // Remember what menu we're in
                                currentMenu = CMD_MAIN;
                                break;
                            default:
                                // Remind the user about what commands are available
                                System.out.println("\nCommand not recognized");
                                printAvailableCommands(CMD_FRONTDESK);
                                break;
                        }
                        break;
                    case CMD_BILLING:
                        // Check user's input (case insensitively)
                        switch (command.toUpperCase()) {
                            case CMD_BILLING_GENERATE:
                                generateBillAndReceipt();
                                break;
                            case CMD_MAIN:
                                // Tell the user their options in this new menu
                                printAvailableCommands(CMD_MAIN);
                                // Remember what menu we're in
                                currentMenu = CMD_MAIN;
                                break;
                            default:
                                // Remind the user about what commands are available
                                System.out.println("\nCommand not recognized");
                                printAvailableCommands(CMD_BILLING);
                                break;
                        }
                        break;
                    case CMD_REPORTS:
                        // Check user's input (case insensitively)
                        switch (command.toUpperCase()) {
                            case CMD_REPORT_REVENUE:
                                reportHotelRevenue();
                            break;
                            case CMD_REPORT_HOTELS:
                                reportEntireTable("Hotels");
                                break;
                            case CMD_REPORT_ROOMS:
                                reportEntireTable("Rooms");
                                break;
                            case CMD_REPORT_STAFF:
                                reportEntireTable("Staff");
                                break;
                            case CMD_REPORT_CUSTOMERS:
                                reportEntireTable("Customers");
                                break;
                            case CMD_REPORT_STAYS:
                                reportEntireTable("Stays");
                                break;
                            case CMD_REPORT_SERVICES:
                                reportEntireTable("ServiceTypes");
                                break;
                            case CMD_REPORT_PROVIDED:
                                reportEntireTable("Provided");
                                break;
                            case CMD_MAIN:
                                // Tell the user their options in this new menu
                                printAvailableCommands(CMD_MAIN);
                                // Remember what menu we're in
                                currentMenu = CMD_MAIN;
                                break;
                            default:
                                // Remind the user about what commands are available
                                System.out.println("\nCommand not recognized");
                                printAvailableCommands(CMD_REPORTS);
                                break;
                        }
                        break;
                    case CMD_MANAGE:
                        // Check user's input (case insensitively)
                        switch (command.toUpperCase()) {
                        case CMD_MANAGE_HOTEL_ADD:
                            manageHotelAdd();
                            break;
                        case CMD_MANAGE_HOTEL_UPDATE:
                            manageHotelUpdate();
                            break;
                        case CMD_MANAGE_HOTEL_DELETE:
                            manageHotelDelete();
                            break;
                        case CMD_MANAGE_STAFF_ADD:
                            manageStaffAdd();
                            break;
                        case CMD_MANAGE_STAFF_UPDATE:
                            manageStaffUpdate();
                            break;
                        case CMD_MANAGE_STAFF_DELETE:
                            manageStaffDelete();
                            break;
                        case CMD_MANAGE_ROOM_ADD:
                        	manageRoomAdd();
                            break;
                        case CMD_MANAGE_ROOM_UPDATE:
                        	manageRoomUpdate();
                            break;
                        case CMD_MANAGE_ROOM_DELETE:
                        	manageRoomDelete();
                            break;
                        case CMD_MANAGE_CUSTOMER_ADD:
                        	manageCustomerAdd();
                        	break;
                        case CMD_MANAGE_CUSTOMER_UPDATE:
                        	manageCustomerUpdate();
                        	break;
                        case CMD_MANAGE_CUSTOMER_DELETE:
                        	manageCustomerDelete();
                        	break;
                        case CMD_MAIN:
                            // Tell the user their options in this new menu
                            printAvailableCommands(CMD_MAIN);
                            // Remember what menu we're in
                            currentMenu = CMD_MAIN;
                            break;
                        default:
                            // Remind the user about what commands are available
                            System.out.println("\nCommand not recognized");
                            printAvailableCommands(CMD_MANAGE);
                            break;
                        }
                        break;
                    default:
                        break;
                }
            }
            
            // Clean up
            scanner.close();
            jdbc_statement.close();
            jdbc_result.close();
            // Hotels
            jdbcPrep_insertNewHotel.close();
            jdbcPrep_updateNewHotelManager.close();
            jdbcPrep_udpateHotelName.close();
            jdbcPrep_updateHotelStreetAddress.close();
            jdbcPrep_updateHotelCity.close();
            jdbcPrep_udpateHotelState.close();
            jdbcPrep_updateHotelPhoneNum.close();
            jdbcPrep_updateHotelManagerID.close();
            jdbcPrep_demoteOldManager.close();
            jdbcPrep_promoteNewManager.close();
            jdbcPrep_getNewestHotelID.close();
            jdbcPrep_getHotelSummaryForAddress.close();
            jdbcPrep_getHotelSummaryForPhoneNumber.close();
            jdbcPrep_getHotelSummaryForStaffMember.close();
            jdbcPrep_getHotelByID.close(); 
            jdbcPrep_deleteHotel.close();
            // Staff
            jdbcPrep_insertNewStaff.close();
            jdbcPrep_getNewestStaffID.close(); 
            jdbcPrep_updateStaffName.close(); 
            jdbcPrep_updateStaffDOB.close(); 
            jdbcPrep_updateStaffJobTitle.close(); 
            jdbcPrep_updateStaffDepartment.close(); 
            jdbcPrep_updateStaffPhoneNum.close(); 
            jdbcPrep_updateStaffAddress.close(); 
            jdbcPrep_updateStaffHotelID.close();
            jdbcPrep_updateStaffRangeHotelID.close();
            jdbcPrep_getStaffByID.close();
            jdbcPrep_deleteStaff.close();
            // Rooms
            jdbcPrep_insertNewRoom.close(); 
            jdbcPrep_updateRoomCategory.close();
            jdbcPrep_updateRoomMaxOccupancy.close();
            jdbcPrep_updateRoomNightlyRate.close();
            jdbcPrep_updateRoomDCStaff.close();
            jdbcPrep_updateRoomDRSStaff.close();
            jdbcPrep_deleteRoom.close();
            jdbcPrep_isValidRoomNumber.close();
            jdbcPrep_isRoomCurrentlyOccupied.close();
            jdbcPrep_isValidHotelId.close();
            jdbcPrep_getRoomByHotelIDRoomNum.close();
            jdbcPrep_getOneExampleRoom.close();
            jdbcPrep_assignDedicatedStaff.close();
            // Customers
            jdbcPrep_insertNewCustomer.close();
            jdbcPrep_updateCustomerName.close();
            jdbcPrep_updateCustomerDateOfBirth.close();
            jdbcPrep_updateCustomerPhoneNumber.close();
            jdbcPrep_updateCustomerEmail.close();
            jdbcPrep_deleteCustomer.close();
            jdbcPrep_getCustomerBySSN.close();
            jdbcPrep_isValidCustomer.close();                 
            jdbcPrep_isCustomerCurrentlyStaying.close();
            // Stays
            jdbcPrep_assignRoom.close();
            jdbcPrep_getNewestStay.close(); 
            // Connection
            jdbc_connection.close();
        
        }
        catch (Throwable err) {
            handleError(err);
        }

    }

}

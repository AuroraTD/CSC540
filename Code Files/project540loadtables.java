import java.sql.*;


public class project540loadtables {

    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/smscoggi"; // Using SERVICE_NAME
    private static final String user = "smscoggi";
    private static final String password = "200157888";


    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {

                connection = DriverManager.getConnection(jdbcURL, user, password);
                statement = connection.createStatement();

                ////sample cats create table
                // statement.executeUpdate("CREATE TABLE CATS (CNAME VARCHAR(20), " +
                // "TYPE VARCHAR(30), AGE INTEGER, WEIGHT FLOAT, SEX CHAR(1))");
                ////sample insert cats data
                // statement.executeUpdate("INSERT INTO CATS VALUES ('Oscar', 'Egyptian Mau'," +
                // " 3, 23.4, 'F')");
                ///sql for dropping table-
                ///DROP TABLE #temptable;

               /* CREATE TABLE Orders (
                    OrderID int NOT NULL,
                    OrderNumber int NOT NULL,
                    PersonID int,
                    PRIMARY KEY (OrderID),                              ///sample primary key setup
                    CONSTRAINT FK_PersonOrder FOREIGN KEY (PersonID)    ////sample fk setup + naming
                    REFERENCES Persons(PersonID)
                );*/

                /*CREATE TABLE Persons (
                    ID int NOT NULL,                                    ////not null set up
                    LastName varchar(255) NOT NULL,
                    FirstName varchar(255),
                    Age int,
                    CONSTRAINT UC_Person UNIQUE (ID,LastName)           ///setup for unique sets that would form fd's
                ); */

                /*CREATE TABLE Persons (
                    ID int NOT NULL AUTO_INCREMENT,                     ////setup for autoincrement field
                    LastName varchar(255) NOT NULL,
                    FirstName varchar(255),
                    Age int,
                    PRIMARY KEY (ID)
                );*/


                statement.executeUpdate("CREATE TABLE Customers (
                    SSN INT NOT NULL,
                    Name VARCHAR(255) NOT NULL,
                    DOB DATE NOT NULL,
                    PhoneNum INT NOT NULL,        
                    Email VARCHAR(255) NOT NULL,
                    PRIMARY KEY (SSN)
                )");    /// phone number to be entered as 10 digit int ex: 9993335555

                statement.executeUpdate("CREATE TABLE ServiceTypes (
                    Name VARCHAR(255) NOT NULL,
                    Cost INT NOT NULL,
                    PRIMARY KEY (Name)
                )");

                statement.executeUpdate("CREATE TABLE Staff (
                    ID INT NOT NULL AUTO_INCREMENT,
                    Name VARCHAR(225) NOT NULL,
                    DOB DATE NOT NULL,
                    JobTitle VARCHAR(225),
                    Dep VARCHAR(225) NOT NULL,
                    PhoneNum INT NOT NULL,
                    Address VARCHAR(225) NOT NULL,
                    HotelID INT,
                    PRIMARY KEY(ID)   
                )");

                statement.executeUpdate("CREATE TABLE Hotels (
                    ID INT NOT NULL AUTO_INCREMENT, 
                    Name VARCHAR(225) NOT NULL,
                    StreetAddress VARCHAR(225) NOT NULL,
                    City VARCHAR(225) NOT NULL,
                    State VARCHAR(225) NOT NULL,
                    PhoneNum INT Not Null,
                    ManagerID INT Not Null,
                    Primary Key(ID),
                    CONSTRAINT UC_HACS UNIQUE (StreetAddress, City, State),
                    CONSTRAINT UC_HPN UNIQUE (PhoneNum),
                    CONSTRAINT UC_HMID UNIQUE (ManagerID),
                    CONSTRAINT FK_HMID FOREIGN KEY (ManagerID) REFERENCES Staff(ID)
                )");


                statement.executeUpdate("ALTER TABLE Staff
                    ADD CONSTRAINT FK_STAFFHID 
                    FOREIGN KEY (HotelID)    
                    REFERENCES Hotels(ID)
                ");/// needs to happen after hotel table is created


            } 
            finally {
                close(result);
                close(statement);
                close(connection);
            }
        } 
        catch(Throwable oops) {
                oops.printStackTrace();
        }
    }
}
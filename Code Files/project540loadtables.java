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
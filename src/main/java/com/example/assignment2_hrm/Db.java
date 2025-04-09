package com.example.assignment2_hrm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class handles database operations for the HRM application.
 */
public class Db {
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /*
     * DATABASE => GET CONNECTION
     * */
    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:hrm.db");
                logger.info("Connected to database");
            }
        } catch (SQLException e) {
            logger.severe("Database connection error: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    /*
    * DATABASE => CLOSE CONNECTION
    * */
    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.severe("Error closing database connection: " + e.getMessage());
        }
    }

    /*---- EMPLOYEE RELATED FUNCTIONS START ---------*/
    /*
    * EMPLOYEE TABLE => CREATE IF TABLE DOES NOT EXIST AND INSERT TEST DATA
    * */
    public void createEmployeeTable() {
        getConnection();
        String createTableSQLQuery = "CREATE TABLE IF NOT EXISTS employees ("
                + "employeeId TEXT PRIMARY KEY, "
                + "firstName TEXT, "
                + "lastName TEXT, "
                + "address TEXT, "
                + "city TEXT, "
                + "province TEXT, "
                + "postcode TEXT, "
                + "country TEXT, "
                + "password TEXT, "
                + "phone TEXT, "
                + "email TEXT, "
                + "isActive BOOLEAN, "
                + "hourlyRate REAL, "
                + "startDate DATE, "
                + "endDate DATE, "
                + "departmentId INTEGER, "
                + "role TEXT)";
        try (PreparedStatement statement = connection.prepareStatement(createTableSQLQuery)) {
            statement.executeUpdate();
            logger.info("Table created");
            // Check if the table already contains data
            String checkDataQuery = "SELECT COUNT(*) FROM employees";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkDataQuery);
                 ResultSet resultSet = checkStatement.executeQuery()) {

                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    // Table is empty; insert test data
                    insertTestEmployeeData();
                    logger.info("Test data inserted");
                } else {
                    logger.info("Table already contains data; skipping test data insertion");
                }
            }
            // Inserting Test Data of Employees

        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    /*
    * EMPLOYEE TABLE => INSERT A NEW EMPLOYEE OBJECT TO TABLE
    * */
    public void insertEmployee(Employee employee) {
        getConnection();
        String insertSQL = "INSERT INTO employees (employeeId, firstName, lastName, address, city, province, postcode, "
                + "country, password, phone, email, isActive, hourlyRate, startDate, endDate, departmentId, role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            // Get values from the Employee object
            statement.setString(1, employee.getEmployeeId());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getLastName());
            statement.setString(4, employee.getAddress());
            statement.setString(5, employee.getCity());
            statement.setString(6, employee.getProvince());
            statement.setString(7, employee.getPostcode());
            statement.setString(8, employee.getCountry());
            statement.setString(9, employee.getPassword());
            statement.setString(10, employee.getPhone());
            statement.setString(11, employee.getEmail());
            statement.setBoolean(12, employee.getIsActive());
            statement.setDouble(13, employee.getHourlyRate());

            // Convert startDate and endDate to java.sql.Date
            statement.setDate(14, (employee.getStartDate() != null) ? new java.sql.Date(employee.getStartDate().getTime()) : null);
            statement.setDate(15, (employee.getEndDate() != null) ? new java.sql.Date(employee.getEndDate().getTime()) : null);

            statement.setInt(16, employee.getDepartmentId());
            statement.setString(17, employee.getRole());

            // Execute the insertion
            statement.executeUpdate();
            logger.info("New Employee inserted");

            closeConnection();
        } catch (SQLException e) {
            logger.info("Error inserting employee");
        }
    }

    /*
     * EMPLOYEE TABLE => UPDATE A NEW EMPLOYEE OBJECT TO TABLE
     * */
    public void updateEmployee(Employee employee) {
        getConnection();

        // Update SQL query to update employee details except employeeId, startDate, country, and email
        String updateSQL = "UPDATE employees SET "
                + "firstName = ?, "
                + "lastName = ?, "
                + "address = ?, "
                + "city = ?, "
                + "province = ?, "
                + "postcode = ?, "
                + "password = ?, "
                + "phone = ?, "
                + "isActive = ?, "
                + "hourlyRate = ?, "
                + "endDate = ?, "
                + "departmentId = ?, "
                + "role = ? "
                + "WHERE employeeId = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            // Set values from the Employee object
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getAddress());
            statement.setString(4, employee.getCity());
            statement.setString(5, employee.getProvince());
            statement.setString(6, employee.getPostcode());
            statement.setString(7, employee.getPassword());
            statement.setString(8, employee.getPhone());
            statement.setBoolean(9, employee.getIsActive());
            statement.setDouble(10, employee.getHourlyRate());

            // Setting only endDate (startDate, country, and email are not updated)
            statement.setDate(11, (employee.getEndDate() != null) ? new java.sql.Date(employee.getEndDate().getTime()) : null);

            statement.setInt(12, employee.getDepartmentId());
            statement.setString(13, employee.getRole());

            // Set the employeeId for the WHERE clause
            statement.setString(14, employee.getEmployeeId());

            // Execute the update
            statement.executeUpdate();
            logger.info("Employee updated successfully");

            closeConnection();
        } catch (SQLException e) {
            logger.info("Error updating employee: " + e.getMessage());
        }
    }

    /*
     * EMPLOYEE TABLE => DELETE EMPLOYEE OBJECT TO TABLE
     * */
    public void deleteEmployee(String employeeId) {
        getConnection();

        // SQL query to delete an employee by employeeId
        String deleteSQL = "DELETE FROM employees WHERE employeeId = ?";

        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            // Set the employeeId for the WHERE clause
            statement.setString(1, employeeId);

            // Execute the delete operation
            int rowsAffected = statement.executeUpdate();

            // Check if any row was affected (i.e., the employee was deleted)
            if (rowsAffected > 0) {
                logger.info("Employee with ID " + employeeId + " deleted successfully.");
            } else {
                logger.info("No employee found with ID " + employeeId);
            }

            closeConnection();
        } catch (SQLException e) {
            logger.info("Error deleting employee: " + e.getMessage());
        }
    }

    /*
    * EMPLOYEE TABLE => GET LIST OF ALL EMPLOYEES
    * */
    public List<Employee> getAlleEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        getConnection();
        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getString("employeeId"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postcode"),
                        resultSet.getString("country"),
                        resultSet.getString("password"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getBoolean("isActive"),
                        resultSet.getDouble("hourlyRate"),
                        resultSet.getDate("startDate"),
                        resultSet.getDate("endDate"),
                        resultSet.getInt("departmentId"),
                        resultSet.getString("role")
                );
                employees.add(employee);
            }

            closeConnection();
        }catch(SQLException e) {
            logger.info(e.toString());
        }

        return employees;
    }

    /*
    * EMPLOYEE TABLE => INSERT TEST EMPLOYEES TO TABLE
    * */
    private void insertTestEmployeeData() {
        insertEmployee(new Employee("EMP001", "John", "Doe", "123 Elm Street", "Toronto", "Ontario", "M5H 2N2", "Canada", "password123", "6471234567", "john.doe@example.com", true, 25.50, java.sql.Date.valueOf("2023-01-15"), java.sql.Date.valueOf("2025-01-15"), 1, "Software Engineer"));
        insertEmployee(new Employee("EMP002", "Jane", "Smith", "456 Maple Avenue", "Vancouver", "British Columbia", "V6B 2T4", "Canada", "securepass", "6049876543", "jane.smith@example.com", true, 30.00, java.sql.Date.valueOf("2022-05-20"), java.sql.Date.valueOf("2024-05-20"), 2, "Project Manager"));
        insertEmployee(new Employee("EMP003", "Michael", "Brown", "789 Pine Street", "Calgary", "Alberta", "T2P 3L9", "Canada", "mypass456", "4035551234", "michael.brown@example.com", false, 20.00, java.sql.Date.valueOf("2021-03-10"), java.sql.Date.valueOf("2023-03-10"), 3, "Business Analyst"));
        insertEmployee(new Employee("EMP004", "Emily", "Johnson", "321 Cedar Lane", "Ottawa", "Ontario", "K1A 0A9", "Canada", "mypassword", "6134445566", "emily.johnson@example.com", true, 28.75, java.sql.Date.valueOf("2022-07-01"), java.sql.Date.valueOf("2024-07-01"), 1, "UI/UX Designer"));
        insertEmployee(new Employee("EMP005", "Daniel", "Williams", "654 Birch Road", "Montreal", "Quebec", "H2Z 1A7", "Canada", "pass123", "5143332211", "daniel.williams@example.com", false, 22.50, java.sql.Date.valueOf("2020-11-15"), java.sql.Date.valueOf("2023-11-15"), 4, "Software Tester"));
        insertEmployee(new Employee("EMP006", "Sophia", "Taylor", "987 Oak Drive", "Halifax", "Nova Scotia", "B3J 2K9", "Canada", "password789", "9022223344", "sophia.taylor@example.com", true, 35.00, java.sql.Date.valueOf("2023-02-01"), java.sql.Date.valueOf("2025-02-01"), 5, "Product Manager"));
        insertEmployee(new Employee("EMP007", "James", "Anderson", "258 Spruce Way", "Winnipeg", "Manitoba", "R3C 4C6", "Canada", "myp@ss", "2046667788", "james.anderson@example.com", true, 18.00, java.sql.Date.valueOf("2022-10-15"), java.sql.Date.valueOf("2024-10-15"), 6, "Customer Support"));
        insertEmployee(new Employee("EMP008", "Charlotte", "Thomas", "369 Elm Grove", "Edmonton", "Alberta", "T5K 0L8", "Canada", "123password", "7805556677", "charlotte.thomas@example.com", false, 24.00, java.sql.Date.valueOf("2021-06-01"), java.sql.Date.valueOf("2023-06-01"), 2, "HR Specialist"));
        insertEmployee(new Employee("EMP009", "Oliver", "Martinez", "147 Fir Boulevard", "Victoria", "British Columbia", "V8W 1L7", "Canada", "passw0rd", "2509991111", "oliver.martinez@example.com", true, 40.00, java.sql.Date.valueOf("2023-08-01"), java.sql.Date.valueOf("2025-08-01"), 7, "Data Scientist"));
        insertEmployee(new Employee("EMP010", "Amelia", "Garcia", "852 Ash Circle", "Quebec City", "Quebec", "G1A 1A1", "Canada", "admin123", "4188889999", "amelia.garcia@example.com", true, 32.50, java.sql.Date.valueOf("2022-09-01"), java.sql.Date.valueOf("2024-09-01"), 8, "Marketing Specialist"));
    }

    /*
     * EMPLOYEE TABLE => GENERATE UNIQUE ID TO INSERT INTO TABLE - INCREMENTING BY +1
     * */
    public String generateEmployeeId() {
        String query = "SELECT employeeId FROM employees ORDER BY employeeId DESC LIMIT 1";
        getConnection();
        String lastEmployeeId = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lastEmployeeId = resultSet.getString("employeeId");
            }

            // If there's no employee yet in the database, return the first ID
            if (lastEmployeeId == null) {
                closeConnection();
                return "EMP001";
            }

            // Extract the numeric part of the lastEmployeeId
            String numericPart = lastEmployeeId.substring(3); // Extracts the part after "EMP"
            int lastNumber = Integer.parseInt(numericPart);
            int newNumber = lastNumber + 1;

            // Format the new number to have 3 digits (e.g., "004", "005")
            String formattedNumber = String.format("%03d", newNumber);
            closeConnection();
            return "EMP" + formattedNumber;

        } catch (SQLException e) {
            logger.info(e.toString());
            return null;
        }
    }

    /*
     * EMPLOYEE TABLE => GET LIST OF ALL EMPLOYEE IDs
     */
    public List<String> getAllEmployeeIds() {
        List<String> employeeIds = new ArrayList<>();
        String query = "SELECT employeeId FROM employees";
        getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                employeeIds.add(resultSet.getString("employeeId"));
            }
        } catch (SQLException e) {
            logger.info("Error retrieving employee IDs: " + e.toString());
        }
        return employeeIds;
    }

    /*---- USER PROFILES RELATED FUNCTIONS START ---------*/

    public void createUserProfileTable() {
        getConnection();

        // SQL query to create the UserProfile table
        String createTableSQLQuery = "CREATE TABLE IF NOT EXISTS userProfiles ("
                + "userId TEXT PRIMARY KEY, "
                + "email TEXT, "
                + "username TEXT, "
                + "password TEXT, "
                + "isActive BOOLEAN, "
                + "fullName TEXT, "
                + "role TEXT)";

        try (PreparedStatement statement = connection.prepareStatement(createTableSQLQuery)) {
            statement.executeUpdate();
            logger.info("UserProfile table created");

            // Check if the table already contains data
            String checkDataQuery = "SELECT COUNT(*) FROM userProfiles";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkDataQuery);
                 ResultSet resultSet = checkStatement.executeQuery()) {

                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    // Table is empty; insert test data
                    insertTestUserProfileData();
                    logger.info("Test data inserted for UserProfile");
                } else {
                    logger.info("UserProfile table already contains data; skipping test data insertion");
                }
            }
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    /*
     * USER PROFILE TABLE => ADDING TEST USERS
     * */
    private void insertTestUserProfileData() {
        // Insert sample user profiles
        String insertSQL = "INSERT INTO userProfiles (userId, email, username, password, isActive, fullName, role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            // Insert sample profile for APP_ADMIN
            statement.setString(1, "admin123");
            statement.setString(2, "admin@hrmapp.com");
            statement.setString(3, "adminUser");
            statement.setString(4, "adminPass123");
            statement.setBoolean(5, true);
            statement.setString(6, "Admin User");
            statement.setString(7, "APP_ADMIN");
            statement.executeUpdate();

            // Insert sample profile for APP_USER
            statement.setString(1, "user456");
            statement.setString(2, "user@hrmapp.com");
            statement.setString(3, "regularUser");
            statement.setString(4, "userPass456");
            statement.setBoolean(5, true);
            statement.setString(6, "Regular User");
            statement.setString(7, "APP_USER");
            statement.executeUpdate();

            logger.info("Sample user profiles inserted");
        } catch (SQLException e) {
            logger.info("Error inserting test data for user profiles: " + e.getMessage());
        }
    }

    /*
     * USER PROFILE TABLE => VALIDATE USERNAME AND PASSWORD
     * */
    public UserProfile validateUserCredentials(String username, String password) {
        UserProfile userProfile = null;
        // SQL query to check if the username and password exist in the UserProfile table
        String query = "SELECT * FROM userProfiles WHERE LOWER(username) = LOWER(?) AND password = ?";
        getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username.toLowerCase());
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // If user exists, create a UserProfile object
                    userProfile = new UserProfile(
                            resultSet.getString("userId"),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getBoolean("isActive"),
                            resultSet.getString("fullName"),
                            resultSet.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            logger.info("Error checking user credentials: " + e.getMessage());
        }

        return userProfile;
    }

    /*---- DEPARTMENT RELATED FUNCTIONS START ---------*/

    /*
    * DEPARTMENT TABLE => CREATE IF TABLE DOES NOT EXIST AND INSERT TEST DATA
    * */
    public void createDepartmentTable() {
        getConnection();
        
        // First, drop the table if it exists
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS departments");
            logger.info("Dropped existing departments table");
        } catch (SQLException e) {
            logger.info("Error dropping departments table: " + e.toString());
        }

        String createTableSQLQuery = "CREATE TABLE IF NOT EXISTS departments ("
                + "departmentId INTEGER PRIMARY KEY, "
                + "departmentName TEXT, "
                + "departmentCode TEXT, "
                + "numberOfEmployees INTEGER, "
                + "managerId TEXT)";

        try (PreparedStatement statement = connection.prepareStatement(createTableSQLQuery)) {
            // Create the table
            statement.executeUpdate();
            logger.info("Department table created");

            // Insert test data
            insertTestDepartmentData();
            logger.info("Test department data inserted");
        } catch (SQLException e) {
            logger.info("Error creating department table: " + e.toString());
        }
    }

    /*
    * DEPARTMENT TABLE => INSERT TEST DEPARTMENTS TO TABLE
    * */
    private void insertTestDepartmentData() {
        String insertDataSQL = "INSERT INTO departments (departmentId, departmentName, departmentCode, numberOfEmployees, managerId) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
            // Insert test data for departments
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Engineering");
            preparedStatement.setString(3, "ENG");
            preparedStatement.setInt(4, 25);
            preparedStatement.setString(5, "EMP001");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Human Resources");
            preparedStatement.setString(3, "HR");
            preparedStatement.setInt(4, 10);
            preparedStatement.setString(5, "EMP002");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "Marketing");
            preparedStatement.setString(3, "MKT");
            preparedStatement.setInt(4, 15);
            preparedStatement.setString(5, "EMP003");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "Finance");
            preparedStatement.setString(3, "FIN");
            preparedStatement.setInt(4, 12);
            preparedStatement.setString(5, "EMP004");
            preparedStatement.executeUpdate();

            // More departments can be added as needed
            logger.info("Test department data successfully inserted.");
        } catch (SQLException e) {
            logger.info("Error inserting test department data: " + e.toString());
        }
    }

    /*
     * DEPARTMENT TABLE => INSERT A NEW DEPARTMENT
     */
    public void insertDepartment(Department department) {
        getConnection();
        String sql = "INSERT INTO departments (departmentName, departmentCode, numberOfEmployees, managerId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, department.getDepartmentName());
            statement.setString(2, department.getDepartmentCode());
            statement.setInt(3, department.getNumberOfEmployees());
            statement.setString(4, department.getManagerId());
            statement.executeUpdate();
            logger.info("New department added to the database.");
        } catch (SQLException e) {
            logger.info("Error adding department: " + e.toString());
        }
    }

    /*
     * DEPARTMENT TABLE => UPDATE AN EXISTING DEPARTMENT
     */
    public void updateDepartment(Department department) {
        getConnection();
        String sql = "UPDATE departments SET departmentName = ?, departmentCode = ?, numberOfEmployees = ?, managerId = ? WHERE departmentId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, department.getDepartmentName());
            statement.setString(2, department.getDepartmentCode());
            statement.setInt(3, department.getNumberOfEmployees());
            statement.setString(4, department.getManagerId());
            statement.setInt(5, department.getDepartmentId());
            statement.executeUpdate();
            logger.info("Department updated successfully.");
        } catch (SQLException e) {
            logger.info("Error updating department: " + e.toString());
        }
    }

    /*
     * DEPARTMENT TABLE => DELETE A DEPARTMENT
     */
    public void deleteDepartment(int departmentId) {
        getConnection();
        String sql = "DELETE FROM departments WHERE departmentId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, departmentId);
            statement.executeUpdate();
            logger.info("Department deleted successfully.");
        } catch (SQLException e) {
            logger.info("Error deleting department: " + e.toString());
        }
    }

    /*
     * DEPARTMENT TABLE => GET LIST OF ALL DEPARTMENTS
     */
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM departments";
        getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Department department = new Department(
                        resultSet.getInt("departmentId"),
                        resultSet.getString("departmentName"),
                        resultSet.getString("departmentCode"),
                        resultSet.getInt("numberOfEmployees"),
                        resultSet.getString("managerId")
                );
                departments.add(department);
            }
        } catch (SQLException e) {
            logger.info("Error retrieving departments: " + e.toString());
        }
        return departments;
    }

    /*---- PAYROLL RELATED FUNCTIONS START ---------*/
}

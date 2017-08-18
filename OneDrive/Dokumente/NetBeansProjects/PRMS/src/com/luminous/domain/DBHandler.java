/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.domain;


import com.luminous.Start;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 *
 * @author Luminous
 */
public class DBHandler {
    
   Connection con;
    private Start app;
   
   public DBHandler(Start appt) {
       this.app = appt;
       
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            int code = getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
  
   
   public int getConnection() {
       try {
           con = DriverManager.getConnection("jdbc:mysql://localhost:3306/policeRMS", "root", "");
           System.out.println("Connected");
          //app.util.showMessage("connected");
           return -1;
       } catch (SQLException ex) {
           int exc = ex.getErrorCode();
           switch(exc) {
               case 1049: createDB();
           }
           return exc;
       }
       
   }

    public int createDB() {
       try {
           con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "");
           
           PreparedStatement prep = con.prepareStatement("create database policeRMS");
           prep.executeUpdate();
           
           con = DriverManager.getConnection("jdbc:mysql://localhost:3306/policeRMS", "root", "");
           prep = con.prepareStatement("create TABLE police ( id int(6) NOT NULL AUTO_INCREMENT PRIMARY KEY, rank varchar(50), fname varchar(50), lname varchar(50), gender varchar(10), phone varchar(30), policeid varchar(50), password varchar(50), address varchar(50), pictures varchar(200) )");
           
           prep = con.prepareStatement("create TABLE CrimeReport ( id int(6) NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar (100), phone varchar(100), gender varchar(15), address varchar(500), caseType varchar(100), statement varchar(10000), caseid varchar(50), cDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  )");

           prep = con.prepareStatement("Insert into TABLE police (rank, fname, lname, gender, phone, policeid, password, address) values (Inspector General, muhammed, saliu, male, 07047388293, nigpol1, admin, UnderG)");
           prep.executeUpdate();
           return -1;
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return ex.getErrorCode();
       }
    }
    
    public boolean userExists(String policeid, String pasword) {
       if (con == null) {
           getConnection();
       }
       
       try {  
            PreparedStatement prep = con.prepareStatement("select * from police where policeid = ?");
            prep.setString(1, policeid);
            ResultSet rst = prep.executeQuery();
            System.out.println("confirmed police id");
            //System.out.println("password " + rst.getString("password"));
            if (rst.next() && rst.getString("password").equals(pasword)) {
                return true;
            }
            app.authCtrl.updateStat("Wrong username/Password");
            return false;
         }
          catch(SQLException ex) {
               switch(ex.getErrorCode()) {
                   case 0:
                       if (getConnection() != -1) {
                           System.out.println("Network Problem");
                           return false;
                       }
                   default:
                           System.out.println(ex.getMessage());
                           return false;
               } 
            }
       catch(NullPointerException ex) {
           System.out.println("Network Problem. Null Pointer");
           app.authCtrl.updateStat("Network Problem. Null Pointer");
           return false;
       }
       
    }
    
    public boolean registerUser(String passportPath, String rankSelected, String fname, String lname, String genderSelected, String pNumber, String pwd, String addFld) {
        if (con == null) {
            getConnection();
        }
        
       try {
           PreparedStatement prep = con.prepareStatement("Insert into police (rank, fname, lname, gender, phone, password, address, pictures) "
                                                          + "values (?,?,?,?,?,?,?,?)");
           prep.setString(1, rankSelected);
           prep.setString(2, fname);
           prep.setString(3, lname);
           prep.setString(4, genderSelected);
           prep.setString(5, pNumber);
           prep.setString(6, pwd);
           prep.setString(7, addFld);
           prep.setString(8, passportPath);
           int finish = prep.executeUpdate();  
           if (finish > 0 ) {
               System.out.println("Userdata logged in DB Successfully");
               //app.util.showMessage("Userdata logged in DB Successfully");
               prep = con.prepareStatement("select id from police where phone = ?");
               prep.setString(1, pNumber);
               ResultSet rs = prep.executeQuery();
               String polId = "NIGPOL";
               if(rs.next()) {
                   polId += rs.getInt(1);
                   System.out.println("Police ID is " + polId);
                   
                   //test
                PreparedStatement prepped = con.prepareStatement("update police set policeid = ? where phone = ?");
                prepped.setString(1, polId);
                prepped.setString(2, pNumber);
                int suc = prepped.executeUpdate();
                if (suc > 0 ) {
                    System.out.println("PoliceId assigned successfully");

                   System.out.println("your login id is " + polId + " and password is " + pwd);
                   app.util.showMessage("Your login id is " + polId + "\nPassword is " + pwd);
                   return true;
                }
                else System.out.println("Error inserting polid into db");

               }
               
               
           }
           
           return false;
           
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return false;
       }
    }

    public boolean registerCrimeReport(String fname, double phoneNum, String genVal, String add, String caseVal, String rStmt) {
        if (con == null) {
            getConnection();
        }
        
       try {
           PreparedStatement prep = con.prepareStatement("insert into crimereport (name, phone, gender, address, caseType, statement) values (?,?,?,?,?,?)");
           prep.setString(1, fname);
           prep.setDouble(2, phoneNum);
           prep.setString(3, genVal);
           prep.setString(4, add);
           prep.setString(5, caseVal);
           prep.setString(6, rStmt);
           int success = prep.executeUpdate();
           
           if (success > 0) {
               prep = con.prepareStatement("select id from crimereport where phone = ?");
               prep.setDouble(1, phoneNum);
               ResultSet rst = prep.executeQuery();
               int caseidVal = 0;
               String caseidPrefix = "CASEID";
               if (rst.next()) {
                   caseidVal = rst.getInt("id");
                   
                    caseidPrefix = "CASEID";
                    caseidPrefix += caseidVal;
               }
               
               prep = con.prepareStatement("update crimereport set caseid = ? where phone = ?");
               prep.setString(1, caseidPrefix);
               prep.setDouble(2, phoneNum);
               prep.executeUpdate();
               app.util.showMessage("Registration Successful" + 
                                        "\nCaseID is " + caseidPrefix);
               Alert alat = new Alert(Alert.AlertType.INFORMATION, "Registration Succesful. \n CaseID is " + caseidPrefix, ButtonType.OK);
               alat.showAndWait();
               return true;
           }
           
           return false;
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return false;
       }
       catch (NullPointerException ex) {
           app.util.showMessage("Network Problem");
           return false;
       }
    
    }

    public ObservableList<ObservableMap<String, Object>> getRegOfficerData() {
        if (con== null) {
            getConnection();
        }
        
        ObservableList<ObservableMap<String, Object>> res = FXCollections.observableArrayList();
       try {
           PreparedStatement prep = con.prepareStatement("select * from police");
           ResultSet rst = prep.executeQuery();
           
           while(rst.next()) {
               ObservableMap<String, Object> map = FXCollections.observableHashMap();
               map.put("rank", rst.getString("rank"));
               map.put("fname", rst.getString("fname"));
               map.put("lname", rst.getString("lname"));
               String fullname = rst.getString("fname") + " " +  rst.getString("lname");
               map.put("name", fullname);
               map.put("phone", rst.getString("phone"));
               map.put("address", rst.getString("address"));
               map.put("id", rst.getInt("id"));
               res.add(map);
           }
           return res;
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       } catch (NullPointerException ex) {
           app.util.showMessage("Network Problem");
           return null;
       }
        
    }
    
    public ObservableList<ObservableMap<String, Object>> getRegOfficerData(String filter) {
        if (con== null) {
            getConnection();
        }
        
        ObservableList<ObservableMap<String, Object>> res = FXCollections.observableArrayList();
       try {
           PreparedStatement prep = con.prepareStatement("select * from police where fname like ?");
           prep.setString(1, "%" + filter + "%");
           ResultSet rst = prep.executeQuery();
           
           while(rst.next()) {
               ObservableMap<String, Object> map = FXCollections.observableHashMap();
               map.put("rank", rst.getString("rank"));
               map.put("fname", rst.getString("fname"));
               map.put("lname", rst.getString("lname"));
               String fullname = rst.getString("fname") + " " +  rst.getString("lname");
               map.put("name", fullname);
               map.put("phone", rst.getString("phone"));
               map.put("address", rst.getString("address"));
               map.put("id", rst.getInt("id"));
               res.add(map);
           }
           return res;
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       } catch (NullPointerException ex) {
           app.util.showMessage("Network Problem");
           return null;
       }
        
    }

    public boolean updateInfo(String rankSelected, String fname, String lname, double pNumber, String addFld, int id) {
        if (con == null) {
            getConnection();
        }
        
       try {
           PreparedStatement prep = con.prepareStatement("update police set rank = ?, fname = ?, lname = ?, phone = ?, address = ? where id = ?");
           prep.setString(1, rankSelected);
           prep.setString(2, fname);
           prep.setString(3, lname);
           prep.setDouble(4, pNumber);
           prep.setString(5, addFld);
           prep.setInt(6, id);
           prep.executeUpdate();
           return true;
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return false;
       } catch(NullPointerException ex) {
           app.util.showMessage("Network Problem");
           return false;
       }
    }

    public boolean deleteOffRow(int i) {
        if (con == null) {
            getConnection();
        }
        
       try {
           PreparedStatement prep = con.prepareStatement("delete from police where id = ?");
           prep.setInt(1, i);
           return prep.executeUpdate() > -1;
//           if (suc > 0) {
//               Alert al = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure you want to delete this Officer?", ButtonType.YES, ButtonType.NO);
//               al
//           }
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return false;
       }
        
    }

    public ObservableList<ObservableMap<String, Object>> getCrimeHistoryData() {
        if (con == null) {
            getConnection();
        }
        ObservableList<ObservableMap<String, Object>> list = FXCollections.observableArrayList();
       try {
           PreparedStatement prep = con.prepareStatement("select * from crimereport");
           ResultSet rs = prep.executeQuery();
           //int x = prep.executeUpdate();
           while (rs.next()) {
               ObservableMap<String, Object> mapp = FXCollections.observableHashMap();
               mapp.put("name", rs.getString("name"));
               mapp.put("phone", rs.getString("phone"));
               mapp.put("gender", rs.getString("gender"));
               mapp.put("address", rs.getString("address"));
               mapp.put("casetype", rs.getString("caseType"));
               mapp.put("statement", rs.getString("statement"));
               mapp.put("caseId", rs.getString("caseid"));
               mapp.put("cDate", rs.getTimestamp("cDate"));
               list.add(mapp);
           }
           return list;
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       }
    }

    public ObservableList<ObservableMap<String, Object>> getCrimeHistoryData(Timestamp fromDate, Timestamp toDate) {
        if (con == null) {
            getConnection();
        }
        ObservableList<ObservableMap<String, Object>> list = FXCollections.observableArrayList();
       try {
           PreparedStatement prep = con.prepareStatement("select * from crimereport where cdate between ? and ?");
           prep.setTimestamp(1, fromDate);
           prep.setTimestamp(2, toDate);
           ResultSet rs = prep.executeQuery();
           //int x = prep.executeUpdate();
           while (rs.next()) {
               ObservableMap<String, Object> mapp = FXCollections.observableHashMap();
               mapp.put("name", rs.getString("name"));
               mapp.put("phone", rs.getString("phone"));
               mapp.put("gender", rs.getString("gender"));
               mapp.put("address", rs.getString("address"));
               mapp.put("casetype", rs.getString("caseType"));
               mapp.put("statement", rs.getString("statement"));
               mapp.put("caseId", rs.getString("caseid"));
               mapp.put("cDate", rs.getTimestamp("cDate"));
               list.add(mapp);
           }
           return list;
       } catch (SQLException ex) {
           Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       }
    }

    public ObservableList<ObservableMap<String, Object>> getCrimeHistoryData(String filter) {
        if (con == null) {
            getConnection();
        }
        
        ObservableList<ObservableMap<String, Object>> list = FXCollections.observableArrayList();
        try{
            PreparedStatement prep = con.prepareStatement("select * from crimereport where name like ?");
            prep.setString(1, "%"+ filter + "%");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                ObservableMap<String, Object> mapp = FXCollections.observableHashMap();
                mapp.put("name", rs.getString("name"));
               mapp.put("phone", rs.getString("phone"));
               mapp.put("gender", rs.getString("gender"));
               mapp.put("address", rs.getString("address"));
               mapp.put("casetype", rs.getString("caseType"));
               mapp.put("statement", rs.getString("statement"));
               mapp.put("caseId", rs.getString("caseid"));
               mapp.put("cDate", rs.getTimestamp("cDate"));
               list.add(mapp);
            }
            return list;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public ObservableList<ObservableMap<String, Object>> getCrimeHistoryData2(String filter) {
        if (con == null) {
            getConnection();
        }
        
        ObservableList<ObservableMap<String, Object>> list = FXCollections.observableArrayList();
        try{
            PreparedStatement prep = con.prepareStatement("select * from crimereport where caseid like ?");
            prep.setString(1, "%"+ filter + "%");
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                ObservableMap<String, Object> mapp = FXCollections.observableHashMap();
                mapp.put("name", rs.getString("name"));
               mapp.put("phone", rs.getString("phone"));
               mapp.put("gender", rs.getString("gender"));
               mapp.put("address", rs.getString("address"));
               mapp.put("casetype", rs.getString("caseType"));
               mapp.put("statement", rs.getString("statement"));
               mapp.put("caseId", rs.getString("caseid"));
               mapp.put("cDate", rs.getTimestamp("cDate"));
               list.add(mapp);
            }
            return list;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ObservableList<ObservableMap<String, Object>> getCrimeHistoryData3(String filter) {
        if (con == null) {
            getConnection();
        }
        
        ObservableList<ObservableMap<String, Object>> list = FXCollections.observableArrayList();
        try{
            PreparedStatement prep = con.prepareStatement("select * from crimereport where caseType = ?");
            prep.setString(1, filter );
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                ObservableMap<String, Object> mapp = FXCollections.observableHashMap();
                mapp.put("name", rs.getString("name"));
               mapp.put("phone", rs.getString("phone"));
               mapp.put("gender", rs.getString("gender"));
               mapp.put("address", rs.getString("address"));
               mapp.put("casetype", rs.getString("caseType"));
               mapp.put("statement", rs.getString("statement"));
               mapp.put("caseId", rs.getString("caseid"));
               mapp.put("cDate", rs.getTimestamp("cDate"));
               list.add(mapp);
            }
            return list;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.jsfs;

import bsp.beans.CustomerOperationInterface;
import bsp.entities.Customer;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author zuoy1
 */
@Named("userController")
@ApplicationScoped
public class UserController implements Serializable{
    private String userName;
    private String password;
    private String message;
    private boolean isLogin;
    Customer customer;
    @EJB
    private CustomerOperationInterface coi;

    public UserController() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String validate() {
        int id = Integer.parseInt(userName);
        Customer c = coi.searchCustomer(id, password);
        if (c != null){
            isLogin = true;
            customer = c;
        }
        return "/response";
    }
    

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void refresh(String customerID){
        userName = customerID;
        if (!customerID.equals("")) {
            Customer c = coi.searchCustomerByID(Integer.parseInt(customerID));
            customer = c;
            isLogin = true;
        }
        
    }
    
    
}

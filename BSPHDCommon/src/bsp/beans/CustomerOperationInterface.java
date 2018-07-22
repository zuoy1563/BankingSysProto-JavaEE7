/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.beans;

import bsp.entities.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author zuoy1
 */
@Remote
public interface CustomerOperationInterface {
    
    List<Customer> getAllCustomers();
    
    void persist(Customer customer);
    
    void merge(Customer customer);
    
    void destory(Customer customer);
    
    Customer searchCustomerByID(int id);
    
    Customer searchCustomer(int userName, String password);
    
}

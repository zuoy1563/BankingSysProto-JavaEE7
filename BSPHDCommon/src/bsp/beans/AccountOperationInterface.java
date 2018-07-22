/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.beans;

import bsp.entities.Account;
import bsp.entities.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author zuoy1
 */
@Remote
public interface AccountOperationInterface {
    void persist(Account account);
    
    List<Account> getAllAccounts();
    
    List<Account> getAccountsByPerson(Customer c);
    
    Account getAccountById(int id);
    
    void deposit(Account account, double amount);
    
    void withdraw(Account account, double amount);
    
    void transfer(Account account1, Account account2, double amount);
}

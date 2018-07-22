/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author zuoy1
 */
@Entity
@Table(name = "Acc")
@NamedQueries({@NamedQuery(name = Account.GET_ALL_QUERY_NAME, query = "SELECT a FROM Account a")})
public class Account implements Serializable{
    public static final String GET_ALL_QUERY_NAME = "Account.getAll";
    
    private int accountNo;
    private double balance;
    private Set<Transaction> transactions;
    private Customer customer;
    
    public Account() {}
    public Account(int accountNo, double balance) {
        this.accountNo = accountNo;
        this.balance = balance;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    // some changes here, commented code are my original code but I am not sure
    // if it is correct so I merged with the solution
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    //@JoinColumn(name = "contact_person_id", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.accountNo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        return this.accountNo == other.accountNo;
    }
    
}

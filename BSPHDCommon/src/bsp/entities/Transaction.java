/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Zoe
 */
@Entity
// transaction cannot be used since it is a build-in name
@Table(name = "Transac")
@NamedQueries({@NamedQuery(name = Transaction.GET_ALL_QUERY_NAME, query = "SELECT t FROM Transaction t")})
public class Transaction implements Serializable{
    public static final String GET_ALL_QUERY_NAME = "Transaction.getAll";
    
    /*
    enum Type {
        Deposit, Withdraw
    }
*/
    
    private int no;
    private double amount;
    private String name;
 
    private Account account;
    private String type;

    public Transaction() {
        
    }

    public Transaction(int no, double amount, String name, Account account, String type) {
        this.no = no;
        this.amount = amount;
        this.name = name;
        this.account = account;
        this.type = type;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_no")
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
    
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    // some changes here, commented code are my original code but I am not sure
    // if it is correct so I merged with the solution
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    //@JoinColumn(name = "contact_person_id", nullable = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.no;
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
        final Transaction other = (Transaction) obj;
        return this.no == other.no;
    }
    

}
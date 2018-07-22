/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Zoe
 */
@Entity
@Table(name = "Customer")
@NamedQueries({@NamedQuery(name = Customer.GET_ALL_QUERY_NAME, query = "SELECT c FROM Customer c")})
public class Customer implements Serializable{
    public static final String GET_ALL_QUERY_NAME = "ContactPerson.getAll";
    
    private int customerId;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private Address address;
    private String password;
    private String type;
    private String email;
    
    private Set<Account> accounts;
    
    public Customer() {}

    public Customer(int customerId, String firstname, String lastname, String phoneNumber, 
            Address address, String password, String type, String email, Set<Account> accounts) {
        this.customerId = customerId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.type = type;
        this.email = email;
        this.accounts = accounts;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
    @Embedded
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.customerId;
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
        final Customer other = (Customer) obj;
        if (this.customerId != other.customerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + firstname + " " + lastname + ", Phone: " + 
                phoneNumber + ", Address: " + address.toString();
    }
    
}

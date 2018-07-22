/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.beans;

import bsp.entities.Account;
import bsp.entities.Customer;
import bsp.entities.Transaction;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author zuoy1
 */
@Stateless
public class AccountOperationImpl implements AccountOperationInterface{
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public void persist(Account account) {
        entityManager.merge(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return entityManager.createNamedQuery(Account.GET_ALL_QUERY_NAME).getResultList();
    }

    @Override
    public List<Account> getAccountsByPerson(Customer c) {
        if (c != null) {
            Query query = entityManager.createQuery("select a from Account a where a.customer.customerId = :param");
            query.setParameter("param",c.getCustomerId());
            List<Account> result = (List<Account>)query.getResultList();
            return result;
        }
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public Account getAccountById(int id) {
        return entityManager.find(Account.class, id);
    }

    @Override
    public void destroy(Customer c) {
        List<Account> accounts = getAccountsByPerson(c);
        for (int i = 0; i < accounts.size(); i++) {
            entityManager.remove(accounts.get(i));
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.beans;

import bsp.beans.TransactionSearcherInterface;
import bsp.entities.Account;
import bsp.entities.Transaction;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author zuoy
 */
@Stateless
public class TransactionSearcher implements TransactionSearcherInterface{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> searchTransactionByName(String name) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> q = b.createQuery(Transaction.class);
        Root<Transaction> p = q.from(Transaction.class);
        q.select(p);
        q.where(b.like(b.upper(p.get("name").as(String.class)), "%" + name.toUpperCase() + "%"));
        TypedQuery<Transaction> tq = entityManager.createQuery(q);
        List<Transaction> result = tq.getResultList();
        return result;
    }

    @Override
    public List<Transaction> searchTransactionByType(String type) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> q = b.createQuery(Transaction.class);
        Root<Transaction> p = q.from(Transaction.class);
        q.select(p);
        q.where(b.like(b.upper(p.get("type").as(String.class)), "%" + type.toUpperCase() + "%"));
        TypedQuery<Transaction> tq = entityManager.createQuery(q);
        List<Transaction> result = tq.getResultList();
        return result;
    }

    @Override
    public Transaction searchTransactionByNo(int id) {
        Transaction transaction = entityManager.find(Transaction.class, id);
        return transaction;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return entityManager.createNamedQuery(Transaction.GET_ALL_QUERY_NAME).getResultList();
    }

    @Override
    public void persist(Transaction transaction) {
        entityManager.merge(transaction);
    }

    @Override
    public void destroy(int accountNo) {
        List<Transaction> transactions = searchTransactionByAccountNo(accountNo);
        for (int i = 0; i < transactions.size(); i++) {
            entityManager.remove(transactions.get(i));
        }
    }

    @Override
    public List<Transaction> searchTransactionByAccountNo(int accountNo) {
        Query query = entityManager.createQuery("select t from Transaction t where t.account.accountNo = :param");
        query.setParameter("param",accountNo);
        List<Transaction> result = (List<Transaction>)query.getResultList();
        return result;
    }

}

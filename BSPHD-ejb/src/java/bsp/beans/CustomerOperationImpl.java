/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.beans;

import bsp.entities.Customer;
import bsp.entities.Transaction;
import com.sun.xml.wss.logging.LogStringsMessages;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Validation;

/**
 *
 * @author zuoy1
 */
@Stateless
public class CustomerOperationImpl implements CustomerOperationInterface{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Customer> getAllCustomers() {
        return entityManager.createNamedQuery(Customer.GET_ALL_QUERY_NAME).getResultList();
    }

    @Override
    public void persist(Customer customer) {
        customer.setPassword(getSHA256(customer.getPassword()));
        entityManager.persist(customer);
    }

    @Override
    public void merge(Customer customer) {
        entityManager.merge(customer);
    }

    @Override
    public void destory(Customer customer) {
        Customer c = searchCustomerByID(customer.getCustomerId());
        if (c != null) 
            entityManager.remove(c);
    }

    @Override
    public Customer searchCustomerByID(int id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public Customer searchCustomer(int userName, String password) {
        Customer c = searchCustomerByID(userName);
        if (c != null) {
            if (c.getPassword().equals(password)) 
                return c;
        }
        return null;
    }

    @Override
    public List<Customer> getCustomersByFirstName(String name) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> q = b.createQuery(Customer.class);
        Root<Customer> p = q.from(Customer.class);
        q.select(p);
        q.where(b.like(b.upper(p.get("firstname").as(String.class)), "%" + name.toUpperCase() + "%"));
        TypedQuery<Customer> tq = entityManager.createQuery(q);
        List<Customer> result = tq.getResultList();
        return result;
    }

    @Override
    public List<Customer> getCustomersByLastName(String name) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> q = b.createQuery(Customer.class);
        Root<Customer> p = q.from(Customer.class);
        q.select(p);
        q.where(b.like(b.upper(p.get("lastname").as(String.class)), "%" + name.toUpperCase() + "%"));
        TypedQuery<Customer> tq = entityManager.createQuery(q);
        List<Customer> result = tq.getResultList();
        return result;
    }

    @Override
    public List<Customer> getCustomersByEmail(String email) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> q = b.createQuery(Customer.class);
        Root<Customer> p = q.from(Customer.class);
        q.select(p);
        q.where(b.like(b.upper(p.get("email").as(String.class)), "%" + email.toUpperCase() + "%"));
        TypedQuery<Customer> tq = entityManager.createQuery(q);
        List<Customer> result = tq.getResultList();
        return result;
    }

    @Override
    public List<Customer> getCustomersByType(String type) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> q = b.createQuery(Customer.class);
        Root<Customer> p = q.from(Customer.class);
        q.select(p);
        q.where(b.like(b.upper(p.get("type").as(String.class)), "%" + type.toUpperCase() + "%"));
        TypedQuery<Customer> tq = entityManager.createQuery(q);
        List<Customer> result = tq.getResultList();
        return result;
    }
    
    private String getSHA256(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = getEncoded(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private String getEncoded(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
    
}

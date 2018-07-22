/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.beans;

import bsp.entities.Transaction;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Zoe
 */
@Remote
public interface TransactionSearcherInterface {
    List<Transaction> searchTransactionByName(String name);
    
    List<Transaction> searchTransactionByType(String type);
    
    Transaction searchTransactionByNo(int id);
    
    List<Transaction> getAllTransactions();
    
    void persist(Transaction transaction);
    
}

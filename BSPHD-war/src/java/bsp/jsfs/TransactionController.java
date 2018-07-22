package bsp.jsfs;

import bsp.beans.AccountOperationInterface;
import bsp.beans.CustomerOperationInterface;
import bsp.beans.TransactionFacade;
import bsp.beans.TransactionSearcherInterface;
import bsp.entities.Account;
import bsp.entities.Customer;
import bsp.entities.Transaction;
import bsp.entities.Transaction;
import bsp.jsfs.util.JsfUtil;
import bsp.jsfs.util.PaginationHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("transactionController")
@SessionScoped
public class TransactionController implements Serializable {
    private String selectedItem;
    private String[] options = new String[]{"Global Search","Search By Transaction Name","Search By Transaction Type","Search By Transaction ID"};
    private String[] transactionTypes = new String[]{"Deposit","Withdraw", "Transfer"};
    private String transactionType = "";
    private String searchText;
    private Transaction current;
    private DataModel items = null;
    @EJB
    private TransactionFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @EJB
    TransactionSearcherInterface search;
    @EJB
    AccountOperationInterface aoi;
    @EJB
    CustomerOperationInterface coi;
    private String message;

    public TransactionController() {
    }

    public Transaction getSelected() {
        if (current == null) {
            current = new Transaction();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TransactionFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Transaction) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Transaction();
        if (getCurrentUser() != null) {
            List<Account> accounts = new ArrayList<>();
            accounts.addAll(aoi.getAccountsByPerson(getCurrentUser()));
            accounts.sort((o1, o2) -> {
                return o1.getAccountNo() - o2.getAccountNo();
            });
            current.setAccount(accounts.get(0));
            current.setType(transactionTypes[0]);
        }
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            //getFacade().create(current);
            Account account = aoi.getAccountById(getSelected().getAccount().getAccountNo());
            current.setAccount(account);
            if (current.getType().equals(transactionTypes[0])){
                account.setBalance(account.getBalance() + getSelected().getAmount());
                search.persist(current);
            }
            else if (current.getType().equals(transactionTypes[1])) {
                if (getSelected().getAccount().getBalance() > current.getAmount()) {
                    current.setAmount(0 - current.getAmount());
                    account.setBalance(account.getBalance() - getSelected().getAmount());
                    search.persist(current);
                }
                else
                    throw new IllegalArgumentException("Amount exceeds balance");
            }
            else {
                List<Account> accounts = new ArrayList<>();
                accounts.addAll(aoi.getAccountsByPerson(getCurrentUser()));
                Account myAccount = accounts.get(0);
                if (myAccount.getBalance() > current.getAmount()) {
                    account.setBalance(account.getBalance() + getSelected().getAmount());
                    myAccount.setBalance(myAccount.getBalance() - getSelected().getAmount());
                    search.persist(current);
                    Transaction t = new Transaction();
                    t.setAccount(myAccount);
                    t.setAmount(0 - current.getAmount());
                    t.setName(current.getName());
                    t.setType(current.getType());
                    search.persist(t);
                }
                else
                    throw new IllegalArgumentException("Amount exceeds balance");
            }
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TransactionCreated"));
            return prepareCreate();
        }
        catch (IllegalArgumentException ie) {
            JsfUtil.addErrorMessage(ie, ie.getMessage());
            return null;
        }
        
        catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Transaction) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TransactionUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Transaction) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TransactionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        // 在这里改！！！把searcherApp的全部写过来
        if (items == null) {
            items = new ListDataModel();
            items.setWrappedData(search.getAllTransactions());
        }
        return items;
    }
    
    // new
    public void search(){
        
        if (selectedItem.equals(options[1])) {
            searchTransactionsByName(searchText);
        }
        else if (selectedItem.equals(options[2])){
            searchTransactionsByType(searchText);
        }
        else if (selectedItem.equals(options[0])) {
            globalSearch(searchText);
        }
        else {
            validation();
            searchTransactionsById(searchText);
        }
    }
    
    public void globalSearch(String text) {
        List<Transaction> result1 = search.searchTransactionByName(text);
        List<Transaction> result2 = search.searchTransactionByType(text);
        char[] textchars = text.toCharArray();
        boolean isdigit = true;
        if (textchars.length == 0)
            isdigit = false;
        for (Character c : textchars) {
            if(!Character.isDigit(c)) {
                isdigit = false;
                break;
            }
        }
        List<Transaction> result3 = new ArrayList<>();
        if (isdigit) {
            result3.add(search.searchTransactionByNo(Integer.parseInt(text)));
        }
        Set<Transaction> set = new HashSet<>();
        set.addAll(result1);
        set.addAll(result2);
        set.addAll(result3);
        List<Transaction> result = new ArrayList<>();
        result.addAll(set);
        result.sort((Transaction t1, Transaction t2) -> t1.getNo() - t2.getNo());
        items.setWrappedData(result);
    }
    
    public void searchTransactionsByName(String name) {
        List<Transaction> result = search.searchTransactionByName(name);
        items.setWrappedData(result);
    }
    
    public void searchTransactionsByType(String type) {
        items.setWrappedData(search.searchTransactionByType(type));
    }
    
    public void searchTransactionsById(String idStr) {
        
        if (message.equals("") && idStr.length() > 0) {
            int id = Integer.parseInt(idStr);
            List<Transaction> target = new ArrayList<>();
            target.add(search.searchTransactionByNo(id));
            items.setWrappedData(target);
        }
        if (message.equals("") && idStr.length() == 0) {
            items.setWrappedData(search.getAllTransactions());
        }
    }
    
    public void validation(){
        if (selectedItem.equals(options[3])) {
            boolean isdigit = true;
            char[] textChars = searchText.toCharArray();
            for (char c: textChars) {
                if (!Character.isDigit(c)) {
                    message = "Please enter only integers";
                    isdigit = false;
                    break;
                }
            }
            if (isdigit)
                message = "";
        }
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Transaction getTransaction(int id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Transaction.class)
    public static class TransactionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TransactionController controller = (TransactionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "transactionController");
            return controller.getTransaction(getKey(value));
        }

        int getKey(String value) {
            int key;
            key = Integer.parseInt(value);
            return key;
        }

        String getStringKey(int value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Transaction) {
                Transaction o = (Transaction) object;
                return getStringKey(o.getNo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Transaction.class.getName());
            }
        }

    }
    //
    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(String[] transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public List<Integer> listAvailableAccounts() {
        Customer c = getCurrentUser();
        int[] myresult;
        if (!getSelected().getType().equals(transactionTypes[2])) {
            List<Account> accounts = aoi.getAccountsByPerson(c);
            myresult = accounts.stream().mapToInt(a -> a.getAccountNo()).toArray();   
        }
        else {
            List<Account> allAccounts = aoi.getAllAccounts();
            List<Account> myAccounts = aoi.getAccountsByPerson(c);
            List<Account> result = new ArrayList<>();
            result.addAll(allAccounts);
            result.removeAll(myAccounts);
            excludeBankerAccounts(result);
            myresult = result.stream().mapToInt(a -> a.getAccountNo()).toArray();
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < myresult.length; i++)
            result.add(myresult[i]);
        Collections.sort(result);
        return result;
       
    }
    
    public void excludeBankerAccounts(List<Account> accounts) {
        List<Account> removeable = new ArrayList<>();
        for (Account account: accounts) {
            if (account.getCustomer().getType().equals("Banking Worker"))
                removeable.add(account);
        }
        accounts.removeAll(removeable);
    }
    


    
    public Customer getCurrentUser(){
    ELContext context
                    = FacesContext.getCurrentInstance().getELContext();
        UserController uc
                    = (UserController) FacesContext.getCurrentInstance()
                            .getApplication()
                            .getELResolver()
                            .getValue(context, null, "userController");
        return uc.getCustomer();
    }
    
    

}

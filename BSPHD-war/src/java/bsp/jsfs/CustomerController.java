package bsp.jsfs;

import bsp.beans.AccountOperationInterface;
import bsp.entities.Customer;
import bsp.jsfs.util.JsfUtil;
import bsp.jsfs.util.PaginationHelper;
import bsp.beans.CustomerFacade;
import bsp.beans.CustomerOperationInterface;
import bsp.beans.TransactionSearcherInterface;
import bsp.entities.Account;
import bsp.entities.Address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("customerController")
@SessionScoped
public class CustomerController implements Serializable {

    private Customer current;
    private DataModel items = null;
    @EJB
    private bsp.beans.CustomerFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    //
    private String message;
    private String streetNoStr;
    private String streetAddress;
    private String suburb;
    private String postcodeStr;
    private String state;
    private String firstname;
    private String lastname;
    private String phoneNumber; 
    private String password;
    private String type;
    private String email;
    private String[] types = new String[] {"Public", "Banking Worker"};
    private boolean isSuccess;
    
    private String searchId = "";
    private String searchFirstname = "";
    private String searchLastname = "";
    private String searchEmail = "";
    private String searchType = "";
    
    @EJB
    private CustomerOperationInterface coi;
    @EJB
    private AccountOperationInterface aoi;
    @EJB
    private TransactionSearcherInterface searcher;

    public CustomerController() {
    }

    public Customer getSelected() {
        if (current == null) {
            current = new Customer();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CustomerFacade getFacade() {
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
        current = (Customer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Customer();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            //getFacade().create(current);
            Address address = new Address(streetNoStr, streetAddress, suburb, postcodeStr, state);
            Customer customer = new Customer();
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setFirstname(firstname);
            customer.setLastname(lastname);
            customer.setPassword(password);
            customer.setPhoneNumber(phoneNumber);
            customer.setType(type);
            Account account = new Account();
            account.setBalance(1000.0d);
            account.setCustomer(customer);
            Set<Account> accounts = new HashSet<>();
            accounts.add(account);
            //aoi.persist(account);
            customer.setAccounts(accounts);
            coi.persist(customer);
            isSuccess = true;
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Customer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            //getFacade().edit(current);
            coi.merge(current);
            isSuccess = true;
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Customer) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //performDestroy();
        //recreatePagination();
        Set<Account> accounts = current.getAccounts();
        List<Account> accounts1 = new ArrayList<>();
        accounts1.addAll(accounts);
        Account account = accounts1.get(0);
        searcher.destroy(account.getAccountNo());
        aoi.destroy(current);
        coi.destory(current);
        isSuccess = true;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerDeleted"));
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
        if (items == null) {
            items = new ListDataModel();
            items.setWrappedData(coi.getAllCustomers());
        }
        return items;
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

    public Customer getCustomer(int id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Customer.class)
    public static class CustomerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerController controller = (CustomerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerController");
            return controller.getCustomer(getKey(value));
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
            if (object instanceof Customer) {
                Customer o = (Customer) object;
                return getStringKey(o.getCustomerId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Customer.class.getName());
            }
        }

    }

    public String getStreetNoStr() {
        return streetNoStr;
    }

    public void setStreetNoStr(String streetNoStr) {
        this.streetNoStr = streetNoStr;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostcodeStr() {
        return postcodeStr;
    }

    public void setPostcodeStr(String postcodeStr) {
        this.postcodeStr = postcodeStr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchFirstname() {
        return searchFirstname;
    }

    public void setSearchFirstname(String searchFirstname) {
        this.searchFirstname = searchFirstname;
    }

    public String getSearchLastname() {
        return searchLastname;
    }

    public void setSearchLastname(String searchLastname) {
        this.searchLastname = searchLastname;
    }

    public String getSearchEmail() {
        return searchEmail;
    }

    public void setSearchEmail(String searchEmail) {
        this.searchEmail = searchEmail;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    
    public void combinationSearch() {
        
        if(searchFirstname.trim().equals("")
                && searchLastname.trim().equals("")
                && searchId.trim().equals("")
                && searchEmail.trim().equals("")
                && searchType.trim().equals("")) {
            items.setWrappedData(coi.getAllCustomers());
            return;
        }

        List<Customer> result = new ArrayList<>();
        if (!searchFirstname.trim().equals("")) {
            result.addAll(coi.getCustomersByFirstName(searchFirstname));
        }
        if (!searchLastname.trim().equals("")) {
            if (result.isEmpty()) {
                result.addAll(coi.getCustomersByLastName(searchLastname));
            }
            else {
                result.retainAll(coi.getCustomersByLastName(searchLastname));
            }
        }
        if (!searchEmail.trim().equals("")) {
            if (result.isEmpty()) {
                result.addAll(coi.getCustomersByEmail(searchEmail));
            }
            else {
                result.retainAll(coi.getCustomersByEmail(searchEmail));
            }
        }
        if (!searchId.trim().equals("")) {
            validate();
            if (message.equals("")) {
                List<Customer> list = new ArrayList<>();
                list.add(coi.searchCustomerByID(Integer.parseInt(searchId)));
                items.setWrappedData(list);
                return;
            }
            else {
                items.setWrappedData(new ArrayList<Customer>());
                return;
            }
        }
        if (!searchType.trim().equals("")) {
            if (result.isEmpty()) {
                result.addAll(coi.getCustomersByType(searchType));
            }
            else {
                result.retainAll(coi.getCustomersByType(searchType));
            }
        }
        items.setWrappedData(result);
    }

    public void validate() {
        boolean isdigit = true;
        char[] textChars = searchId.toCharArray();
        for (char c: textChars) {
            if (!Character.isDigit(c)) {
                message = "Please enter only integers";
                isdigit = false;
                break;
            }
        }
        if (isdigit) {
            message = "";
        }
    }

}

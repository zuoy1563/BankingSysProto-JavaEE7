/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.webServices;

import bsp.beans.AccountOperationInterface;
import bsp.beans.CustomerOperationInterface;
import bsp.entities.Account;
import bsp.entities.Customer;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author zuoy1
 */
@Path("transfername")
public class TransfernameResource {
    @EJB
    private NameStorageBean nameStorage;

    @Context
    private UriInfo context;
    
    @EJB
    private CustomerOperationInterface coi;
    
    @EJB
    private AccountOperationInterface aoi;

    /**
     * Creates a new instance of TransfernameResource
     */
    public TransfernameResource() {
    }

    /**
     * Retrieves representation of an instance of bsp.webServices.TransfernameResource
     * @return an instance of java.lang.String
     */
   @GET
    public String getHtml() {
        //TODO return proper representation object
        return nameStorage.getName();
    }
    
     /**
     * POST method for updating an instance of HelloWorldResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("application/x-www-form-urlencoded") 
    public void postRequestName(@FormParam("id") String content) {
        int id = Integer.parseInt(content);
        Account account = aoi.getAccountById(id);
        Customer c = account.getCustomer();
        if (c != null) {
            nameStorage.setName(c.getFirstname() + " " + c.getLastname());
        }
//        return "<html><body><h1>Hello "+nameStorage.getName()+"!</h1></body></html>";
    }

    /**
     * PUT method for updating or creating an instance of TransfernameResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
}

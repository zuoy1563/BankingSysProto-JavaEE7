/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.webServices;

import bsp.entities.Customer;
import bsp.jsfs.UserController;
import javax.ejb.Singleton;
import javax.el.ELContext;
import javax.faces.context.FacesContext;

/** Singleton session bean used to store the name parameter for "/helloWorld" resource
 *
 * @author mkuchtiak
 */
@Singleton
public class NameStorageBean {

    // name field
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //getCurrentUserController().setTransferInAccountName(name);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.webServices;


import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;



/**
 *
 * @author Messom
 */
@Named(value = "webServiceBean")
@SessionScoped
public class webServiceBean implements Serializable {

    private String id;
    private String name;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    /**
     * Creates a new instance of webServiceBean
     */
    public webServiceBean() {
    }
    getNameWebService myWS;
    public void getNameWebservice(String id){
        
        myWS= new getNameWebService();
        this.id = id;
        myWS.postRequestName2(id);
       
    }
    public void getNameWebservice(){
        
        myWS= new getNameWebService();
        name = myWS.getHtml();
       
    }

    static class getNameWebService {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8080/BSPHD-war/webresources";

        public getNameWebService() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("transfername");
        }

        public void postRequestName() throws ClientErrorException {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED).post(null);
        }
        
        public void postRequestName2(String id) throws ClientErrorException {
               //create a form and add to this form information of a user
            Form form = new Form();
            form.param("id", id);

            webTarget.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
            //webTarget.queryParam("name", name).request(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED).post(null);
            
        }
       

        public String getHtml() throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.TEXT_HTML).get(String.class);
        }

        public void putHtml(Object requestEntity) throws ClientErrorException {
            webTarget.request(javax.ws.rs.core.MediaType.TEXT_HTML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_HTML));
        }

        public void close() {
            client.close();
        }
    }
    
}

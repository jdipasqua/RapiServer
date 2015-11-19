/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utiles;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author fcarou
 */
public class HttpHelper
{
    private String base;
    
    public HttpHelper (String base)
    {
        this.base = base;
    }
    
    public String doGet (String params)
    {
        try
        {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(base + "?" + params);

            HttpResponse res = client.execute(get);
            
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(res.getEntity());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
    public String doPost (String data, Header header)
    {
        try
        {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(base);
            
            StringEntity ent = new StringEntity(data);
            post.setEntity(ent);
            post.setHeader("Content-type", "application/json");
            post.setHeader(header);
            
            HttpResponse res = client.execute(post);
            
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(res.getEntity());
            
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

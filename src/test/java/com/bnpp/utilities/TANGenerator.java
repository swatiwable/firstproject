package com.bnpp.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class TANGenerator {

       //@Test
       public static String requestTan() throws ClientProtocolException, IOException {
             String endpoint = "http://10.207.132.203:8080/tanGenerator";
             HttpClient client = HttpClientBuilder.create().build();
             HttpGet req = new HttpGet(endpoint);
             HttpResponse rep = client.execute(req);
             BufferedReader br = new BufferedReader(new InputStreamReader(rep.getEntity().getContent()));

             String line = "";
             StringBuffer sb = new StringBuffer();
             String token = "000000";
             String newToken[]=null;
             while ((line = br.readLine()) != null) {
                    sb.append(line);
             }
             String jsn = sb.toString();
           //  token = jsn.substring(23, 31);
             String[] mytoken=jsn.split(":");
             System.out.println(mytoken[2]);
             newToken= mytoken[2].split("\"");
             token=newToken[1];
             System.out.println(jsn);
             System.out.println(token);
             return token;
       }

}


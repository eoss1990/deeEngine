package com.seeyon.resource;

import com.seeyon.v3x.dee.Document;
import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.context.EngineController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by yangyu on 17/1/4.
 */
@Path("deeResource")
public class DeeResource {

    @GET
    @Path("{flowId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String exec(@PathParam("flowId") String flowId){
        EngineController engineController = EngineController.getInstance(null);
        Document document = null;
        try {
             document = engineController.executeFlow(flowId);
        } catch (TransformException e) {
            e.printStackTrace();
        }
        return document==null?"document is null":document.toString();
    }
}

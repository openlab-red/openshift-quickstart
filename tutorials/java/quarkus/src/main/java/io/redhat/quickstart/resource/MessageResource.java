package io.redhat.quickstart.resource;

import java.util.List;

import io.redhat.quickstart.entity.Message;
import io.redhat.quickstart.service.MessageService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    @Inject
    MessageService messageService;

    @GET
    @Path("/messages")
    public List<Message> list() {
        return messageService.listMessages();
    }

    @POST
    @Path("/add/{message}")
    public Response add(@PathParam("message") String text) {
        messageService.addMessage(text);
        return Response.status(Response.Status.CREATED).entity("{\"message\":\"Message added successfully!\"}").build();
    }

    // Demo purpose
    @GET
    @Path("/add/{message}")
    public Response addGET(@PathParam("message") String text) {
        return add(text);
    }

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok().entity("{\"message\":\"pong\"}").build();
    }
}

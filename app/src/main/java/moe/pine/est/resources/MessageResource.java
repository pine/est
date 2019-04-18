package moe.pine.est.resources;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.filter.FilterGroup;
import moe.pine.est.mailgun.Mailgun;
import moe.pine.est.mailgun.MessageRequest;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Path("/api/messages")
@Dependent
@JBossLog
public class MessageResource {
    @Inject
    private Mailgun mailgun;

    @Inject
    private FilterGroup filterGroup;

    @POST
    public String receive(
        InputStream requestBody
    ) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        System.out.println(out.toString());   //Prints the string content read from input stream
        reader.close();

        /*
        log.infov(
            "Message received :: " +
                "recipient=\"{0}\", sender=\"{1}\", from=\"{2}\"",
            messageRequest.getRecipient(),
            messageRequest.getSender(),
            messageRequest.getFrom());

        log.infov(request.rea);
        */

//        mailgun.receive()

        // filterGroup.doFilter(Message.builder().build());

        return "OK";
    }
}

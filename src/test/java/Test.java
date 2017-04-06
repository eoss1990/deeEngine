import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by yangyu on 17/1/4.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:8080/myapp").path("deeResource");
        Invocation.Builder invocationBuilder =  webTarget.request();
        Response response = invocationBuilder.get();

        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }
}

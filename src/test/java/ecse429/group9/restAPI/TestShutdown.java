package ecse429.group9.restAPI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;

import static org.junit.Assert.assertEquals;

public class TestShutdown {

    @Before
    public void startInstance(){
        APIInstance.runApplication();
    }

    @After
    public void killInstance(){
        APIInstance.killInstance();
    }

    @Test
    public void testShutdownServer(){
        String shutdown_url = "/shutdown";
        boolean server_status = true;
        try {
            APIInstance.getStatusCode(shutdown_url);
        } catch (ConnectException ce){
            server_status = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(false, server_status);
    }
}

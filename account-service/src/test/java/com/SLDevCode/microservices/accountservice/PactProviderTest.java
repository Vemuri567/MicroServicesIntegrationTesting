package com.SLDevCode.microservices.accountservice;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.SLDevCode.microservices.accountservice.dto.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("UserService")
@PactFolder("D:\\VFC project\\Microservises\\account-service\\src\\main\\java\\pacts\\")
public class PactProviderTest {
    @LocalServerPort
    public int port;


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void pactVerificationTest(PactVerificationContext context)
    {
        context.verifyInteraction();

    }

    @BeforeEach
    public void setup(PactVerificationContext context)
    {

        context.setTarget(new HttpTestTarget("localhost",port));
    }

    @State(value= "user exist",action= StateChangeAction.SETUP)
    public void userExist()
    {
       /* UserRequest request=new UserRequest();

        request.setAge("20");
        request.setFullName("Naveen");*/
    }

    @State(value= "user exist",action= StateChangeAction.TEARDOWN)
    public void userExistTearDown()

    {

    }


}

package com.gw;

import com.gw.controller.SampleController;
import com.gw.entity.Gts2PermissionConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
@WebAppConfiguration
public class SampleControllerTest {
    private MockMvc mock;

    //@Autowired
    //@Qualifier(value = "secondaryMongoTemplate")
    //protected MongoTemplate secondaryMongoTemplate;

    @Before
    public void setUp() throws Exception {
        mock = MockMvcBuilders.standaloneSetup(new SampleController()).build();
    }

    @Test
    public void testHome() throws Exception {
        mock.perform(MockMvcRequestBuilders.get("/home").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello GW!")));
    }

    @Test
    public void testMongdb() throws Exception{
        Query query = new Query();
        Criteria cri =new Criteria();

        cri.where("user").is("test").and("pwd").is("test").and("collectionName").is("gts2_t_clone_trade_real");

        query.addCriteria(cri);
        //Gts2PermissionConfig obj = secondaryMongoTemplate.findOne(query, Gts2PermissionConfig.class);
        System.out.println(query.toString());





    }
}
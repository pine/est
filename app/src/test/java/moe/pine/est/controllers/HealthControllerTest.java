package moe.pine.est.controllers;

import lombok.SneakyThrows;
import moe.pine.est.properties.AppProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HealthControllerTest {
    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private HealthController healthController;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void homeTest() {
        when(appProperties.getSiteUrl()).thenReturn("https://www.example.com/");

        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("https://www.example.com/"));

        verify(appProperties).getSiteUrl();
    }


    @Test
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void homeTest_notFound() {
        when(appProperties.getSiteUrl()).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is(NOT_FOUND.value()));


        verify(appProperties).getSiteUrl();
    }

    @Test
    @SneakyThrows
    public void healthTest() {
        mvc.perform(MockMvcRequestBuilders.get("/health"))
            .andExpect(status().isOk());
    }
}

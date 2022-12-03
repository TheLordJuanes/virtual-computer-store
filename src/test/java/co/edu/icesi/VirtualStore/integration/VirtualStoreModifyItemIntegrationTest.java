package co.edu.icesi.VirtualStore.integration;

import co.edu.icesi.VirtualStore.dto.ItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;
import java.io.InputStreamReader;
import java.io.Reader;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class VirtualStoreModifyItemIntegrationTest {

    private static final String JSON_FILE = "createItem.json";

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper.findAndRegisterModules();
    }

    /*@Test
    @SneakyThrows
    public void testModifyItemSuccessfully() {
        ItemDTO baseItem = baseItem();
        String body = objectMapper.writeValueAsString(baseItem);
        MvcResult result = mockMvc.perform(post("/updateItem").param("name", "duke").param("number", "C0124").param("email", "duke@java.io").andExpect(status().isOk()).andExpect(header().string("Location", "/customers"));
        ItemDTO itemResult = objectMapper.readValue(result.getResponse().getContentAsString(), ItemDTO.class);
        assertThat(itemResult, hasProperty("name", is("Mini PC")));
        assertThat(itemResult, hasProperty("description", is("A mini pc.")));
        assertThat(itemResult, hasProperty("price", is(1000)));
        assertThat(itemResult, hasProperty("urlImage", is("https://www.img2link.com/images/2022/12/01/aaf04b258acfc1bf50e03b699b611fa7.jpg")));
    }*/

    @SneakyThrows
    private ItemDTO baseItem() {
        String body = parseResourceToString();
        return objectMapper.readValue(body, ItemDTO.class);
    }

    @SneakyThrows
    private String parseResourceToString() {
        Resource resource = new ClassPathResource(JSON_FILE);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}

package org.fpf.governance.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fpf.governance.brain.Brain;
import org.fpf.governance.web.dto.DeconstructResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FrameActivityController.class)
public class FrameActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Brain brain;

    @Test
    public void testShowFrameActivity() throws Exception {
        mockMvc.perform(get("/activity/frame"))
                .andExpect(status().isOk())
                .andExpect(view().name("activity_frame"));
    }

    @Test
    public void testSubmitFrame() throws Exception {
        String mockResponse = """
                Sure, here is the deconstruction:
                ```json
                {
                  "holons": [
                    { "id": "H.1", "name": "System1", "type": "System", "role": "Role1", "manifestation": "M1", "parentID": "root" }
                  ],
                  "reframedProblem": "Reframed problem statement",
                  "categoryErrors": [
                    { "description": "Error 1", "violation": "V1", "correction": "C1" }
                  ],
                  "ghostReinstantiations": [
                    { "from": "Ghost", "to": "Real" }
                  ]
                }
                ```
                I hope this helps!
                """;
        when(brain.generate(anyString())).thenReturn(mockResponse);

        mockMvc.perform(post("/activity/frame/submit")
                .param("description", "Test description"))
                .andExpect(status().isOk())
                .andExpect(view().name("activity_frame :: response-fragment"))
                .andExpect(model().attributeExists("response"))
                .andExpect(model().attribute("response", org.hamcrest.Matchers.instanceOf(DeconstructResponse.class)));
    }

    @Test
    public void testSubmitFrameError() throws Exception {
        when(brain.generate(anyString())).thenThrow(new RuntimeException("Brain failure"));

        mockMvc.perform(post("/activity/frame/submit")
                .param("description", "Test description"))
                .andExpect(status().isOk())
                .andExpect(view().name("activity_frame :: error-fragment"))
                .andExpect(model().attributeExists("error"));
    }
}

package ar.app.controllers.genre;

import ar.app.dtos.genre.GenreRequest;
import ar.app.models.genre.GenreModel;
import ar.app.repositories.genre.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreRepository repository;


    @Test
    void loadContext() {
        assertNotNull(mockMvc);
        assertNotNull(repository);

    }

    @DirtiesContext
    @Test
    void testGetGenres() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/genres")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }

    @DirtiesContext
    @Test
    void testGetGenresBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/genres")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }

    @DirtiesContext
    @Test
    void testGetGenresPageNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/genres")
                .param("page", "2")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

    }

    @DirtiesContext
    @Test
    void testCreateGenre() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(new GenreRequest("acción"));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/genres")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("acción"))
                .andReturn();
    }

    @DirtiesContext
    @Test
    void testCreateGenreByNameNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new GenreRequest(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/genres")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }

    @Nested
    class NestedTest {

        private static GenreModel model = GenreModel.builder()
                .id(1L)
                .name("acción")
                .build();

        @BeforeEach
        void persistGenre() {
            model = repository.saveAndFlush(model);
        }

        @AfterEach
        void removeGenre() {
            assertNotNull(model);
            repository.delete(model);
        }

        @DirtiesContext
        @Test
        void testCreateGenreExists() throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new GenreRequest(model.getName()));

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/genres")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn();

        }

        @DirtiesContext
        @Test
        void testGetGenreById() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/genres/" + model.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(model.getName()))
                    .andReturn();
        }

        @DirtiesContext
        @Test
        void testGenreNotFound() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/genres/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }

        @DirtiesContext
        @Test
        void testUpdateGenreById() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new GenreRequest("ficción"));

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/genres/" + model.getId())
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/genres/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ficción"))
                    .andReturn();
        }

        @DirtiesContext
        @Test
        void testUpdateGenreNotFound() throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new GenreRequest("ficción"));

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/genres/2")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }

        @DirtiesContext
        @Test
        void testUpdateGenreByNameNull() throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new GenreRequest(null));

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/genres/" + model.getId())
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn();
        }

        @DirtiesContext
        @Test
        void testDeleteGenreById() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/genres/" + model.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();
        }

        @DirtiesContext
        @Test
        void testDeleteGenreNotFound() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/genres/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }
    }
}
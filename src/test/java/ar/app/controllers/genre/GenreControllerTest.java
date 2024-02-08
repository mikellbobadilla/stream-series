package ar.app.controllers.genre;

import ar.app.dtos.genre.GenreResponse;
import ar.app.dtos.genre.PageGenre;
import ar.app.repositories.genre.GenreRepository;
import ar.app.services.genre.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GenreControllerTest {

    @Mock
    private GenreService service;
    @InjectMocks
    private GenreController controller;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void loadContext() {
        assertNotNull(service);
        assertNotNull(controller);
        assertNotNull(mockMvc);
    }

    @Test
    public void testGetGenres() throws Exception {
        PageGenre pageGenre = PageGenre.builder()
                .content(List.of(new GenreResponse(1L, "acción")))
                .pageNumber(1)
                .pageSize(10)
                .totalPages(1)
                .totalElements(1)
                .build();
        doReturn(pageGenre).when(service).getGenres(1, 10);

        var response = controller.genres(1, 10);

        verify(service, atLeastOnce()).getGenres(1, 10);

//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/genres")
//                        .param("page", "1")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn().getResponse();


    }
}
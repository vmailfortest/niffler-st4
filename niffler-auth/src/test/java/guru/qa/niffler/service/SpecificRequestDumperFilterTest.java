package guru.qa.niffler.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecificRequestDumperFilterTest {

    SpecificRequestDumperFilter specificRequestDumperFilter;

    @Mock
    private GenericFilter mockGenericFilter;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private ServletResponse mockResponse;

    @Mock
    private FilterChain mockChain;

    private String url = "testUrl";
    private String incorrectUrl = "otherUrl";

    @BeforeEach
    void beforeEach() {
        specificRequestDumperFilter = new SpecificRequestDumperFilter(mockGenericFilter, url);
    }

    @Test
    void doFilterNotHttpServletRequestTest(@Mock ServletRequestFake mockRequest) throws ServletException, IOException {

        lenient().when(mockRequest.getRequestURI()).thenReturn(url);

        specificRequestDumperFilter.doFilter(mockRequest, mockResponse, mockChain);

        verify(mockGenericFilter, never()).doFilter(mockRequest, mockResponse, mockChain);
        verify(mockChain, times(1)).doFilter(mockRequest, mockResponse);

    }

    @Test
    void doFilterUriMatchesTest() throws ServletException, IOException {

        lenient().when(mockRequest.getRequestURI()).thenReturn(url);

        specificRequestDumperFilter.doFilter(mockRequest, mockResponse, mockChain);

        verify(mockGenericFilter, times(1)).doFilter(mockRequest, mockResponse, mockChain);
        verify(mockChain, never()).doFilter(mockRequest, mockResponse);

    }

    @Test
    void doFilterUriNotMatchesTest() throws ServletException, IOException {

        lenient().when(mockRequest.getRequestURI()).thenReturn(incorrectUrl);

        specificRequestDumperFilter.doFilter(mockRequest, mockResponse, mockChain);

        verify(mockGenericFilter, never()).doFilter(mockRequest, mockResponse, mockChain);
        verify(mockChain, times(1)).doFilter(mockRequest, mockResponse);

    }

    @Test
    void destroyTest() {

        specificRequestDumperFilter.destroy();

        verify(mockGenericFilter, times(1)).destroy();

    }

}
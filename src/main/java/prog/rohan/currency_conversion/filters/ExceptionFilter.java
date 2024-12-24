package prog.rohan.currency_conversion.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import prog.rohan.currency_conversion.exceptions.DataExistException;
import prog.rohan.currency_conversion.exceptions.DataNotFoundException;
import prog.rohan.currency_conversion.exceptions.DatabaseException;
import prog.rohan.currency_conversion.exceptions.InvalidDataException;
import prog.rohan.currency_conversion.exceptions.NoExchangeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter(value = {
        "/currencies", "/currency/*", "/exchangeRates",
        "/exchangeRate/*", "/exchange"
})
public class ExceptionFilter extends HttpFilter {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try{
            super.doFilter(req, res, chain);
        }
        catch (DatabaseException e){
            writeError(e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res);
        }
        catch (DataExistException e){
            writeError(e, HttpServletResponse.SC_CONFLICT, res);
        }
        catch (DataNotFoundException e){
            writeError(e, HttpServletResponse.SC_NOT_FOUND, res);
        }catch (NoExchangeException e){
            writeError(e, HttpServletResponse.SC_NOT_FOUND, res);
        }catch(InvalidDataException e){
            writeError(e, HttpServletResponse.SC_BAD_REQUEST, res);
        }

    }

    private void writeError(RuntimeException e, int code, HttpServletResponse resp) throws IOException {
        Map<String, String> error= new HashMap<>();
        error.put("message", e.getMessage());
        resp.setStatus(code);
        objectMapper.writeValue(resp.getWriter(), error);
    }
}

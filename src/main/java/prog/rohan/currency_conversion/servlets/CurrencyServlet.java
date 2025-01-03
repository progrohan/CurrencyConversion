package prog.rohan.currency_conversion.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import prog.rohan.currency_conversion.dto.CurrencyRequestDto;
import prog.rohan.currency_conversion.dto.CurrencyResponseDTO;
import prog.rohan.currency_conversion.service.CurrencyService;
import prog.rohan.currency_conversion.utils.DataValidator;

import java.io.IOException;

@WebServlet(name="CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String code = pathInfo.substring(1);
        DataValidator.checkCode(code);
        CurrencyResponseDTO currency = CurrencyService.selectCurrencyByCode(
                new CurrencyRequestDto(code, null, null));
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currency);
    }
}

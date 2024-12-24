package prog.rohan.currency_conversion.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import prog.rohan.currency_conversion.dto.ExchangeDTO;
import prog.rohan.currency_conversion.service.ExchangeService;
import prog.rohan.currency_conversion.utils.DataValidator;

import java.io.IOException;

@WebServlet(name = "ExchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        DataValidator.checkCode(baseCurrencyCode);
        String targetCurrencyCode = req.getParameter("to");
        DataValidator.checkCode(targetCurrencyCode);
        String amount = req.getParameter("amount");
        DataValidator.checkAmount(amount);
        ExchangeDTO exchangeDTO = new ExchangeDTO(baseCurrencyCode,
                targetCurrencyCode,
                Integer.valueOf(amount));
        exchangeDTO = ExchangeService.makeExchange(exchangeDTO);
        objectMapper.writeValue(resp.getWriter(), exchangeDTO);
    }
}

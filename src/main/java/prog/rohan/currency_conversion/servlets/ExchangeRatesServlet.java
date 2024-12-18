package prog.rohan.currency_conversion.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import prog.rohan.currency_conversion.dto.ExchangeRateDTO;
import prog.rohan.currency_conversion.service.ExchangeRateService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ExchangeRatesServlet", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRateDTO> exchangeRateDTOS = ExchangeRateService.selectExchangeRates();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeRateDTOS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        Double rate = Double.valueOf(req.getParameter("rate"));
        ExchangeRateDTO exchangeRateReqDTO = new ExchangeRateDTO(
                baseCurrencyCode,
                targetCurrencyCode,
                rate
                );
        ExchangeRateDTO exchangeRateRespDTO = ExchangeRateService.insertExchangeRates(exchangeRateReqDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), exchangeRateRespDTO);
    }
}

package prog.rohan.currency_conversion.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import prog.rohan.currency_conversion.dto.ExchangeRateRequestDTO;
import prog.rohan.currency_conversion.dto.ExchangeRateResponseDTO;
import prog.rohan.currency_conversion.dto.ExchangeResponseDTO;
import prog.rohan.currency_conversion.service.ExchangeRateService;
import prog.rohan.currency_conversion.utils.DataValidator;

import java.io.IOException;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if("PATCH".equalsIgnoreCase(req.getMethod())){
            doPatch(req, resp);
        }else{
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String params = path.substring(1);
        String baseCurrencyCode = params.substring(0,3);
        DataValidator.checkCode(baseCurrencyCode);
        String targetCurrencyCode = params.substring(3,6);
        DataValidator.checkCode(targetCurrencyCode);
        ExchangeRateResponseDTO exchangeRatesDto = ExchangeRateService.selectByCode(
                new ExchangeRateRequestDTO(baseCurrencyCode,
                        targetCurrencyCode,
                        null)
        );
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeRatesDto);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String params = path.substring(1);
        String baseCurrencyCode = params.substring(0,3);
        DataValidator.checkCode(baseCurrencyCode);
        String targetCurrencyCode = params.substring(3,6);
        DataValidator.checkCode(targetCurrencyCode);
        String rate = req.getParameter("rate");
        DataValidator.checkRate(rate);
        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode,
                targetCurrencyCode,
                Double.valueOf(rate));
        ExchangeRateResponseDTO exchangeRateResponseDTO = ExchangeRateService.updateExchangeRate(exchangeRateRequestDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDTO);
    }
}

package prog.rohan.currency_conversion.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import prog.rohan.currency_conversion.dto.CurrencyDTO;
import prog.rohan.currency_conversion.service.CurrencyService;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import prog.rohan.currency_conversion.utils.DataValidator;

@WebServlet(name = "CurrenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyDTO> currencyDTOList = CurrencyService.selectCurrencies();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencyDTOList);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("Code");
        DataValidator.checkCode(code);
        String fullName = req.getParameter("FullName");
        DataValidator.checkName(fullName);
        String sign = req.getParameter("Sign");
        DataValidator.checkSign(sign);
        CurrencyDTO currencyReqDTO = new CurrencyDTO(
                null,
                code,
                fullName,
                sign
        );
        CurrencyDTO currencyRespDTO = CurrencyService.insertCurrency(currencyReqDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), currencyRespDTO);
    }
}


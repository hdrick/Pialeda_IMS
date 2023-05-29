package pialeda.app.Invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TerminateController {
    private final ApplicationContext applicationContext;

    @Autowired
    public TerminateController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/terminate", method = {RequestMethod.GET, RequestMethod.POST})
    public void terminateProgram() {
        // Gracefully shut down the application
        SpringApplication.exit(applicationContext, () -> 0);
    }
}

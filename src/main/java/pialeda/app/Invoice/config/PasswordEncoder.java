package pialeda.app.Invoice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration class for creating a BCryptPasswordEncoder bean.
 */
@Configuration
public class PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(PasswordEncoder.class);

    /**
     * Create and configure a BCryptPasswordEncoder bean with a strength factor of 5.
     *
     * @return A BCryptPasswordEncoder bean.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        logger.info("Creating BCryptPasswordEncoder bean with strength factor 5.");
        return new BCryptPasswordEncoder(5);
    }
}

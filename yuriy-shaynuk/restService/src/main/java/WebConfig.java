import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableCaching
@ComponentScan(basePackages = {"controllers","repository"})
public class WebConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("cities");
    }
}

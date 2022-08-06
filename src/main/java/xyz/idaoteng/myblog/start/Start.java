package xyz.idaoteng.myblog.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.idaoteng.auth.custom.url.UrlRecognizer;
import xyz.idaoteng.myblog.login.service.IpInterceptor;

@SpringBootApplication(scanBasePackages = {"xyz.idaoteng"})
@MapperScan(basePackages = {"xyz.idaoteng.**.dao"})
public class Start implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(Start.class);
    }

    @Bean
    UrlRecognizer urlRecognizer() {
        return new UrlRecognizer()
                .addRoleBoundUrl("/api/admin/**", new String[]{"root_user"})
                .allowTheRest(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IpInterceptor()).addPathPatterns("/api/register");
    }
}

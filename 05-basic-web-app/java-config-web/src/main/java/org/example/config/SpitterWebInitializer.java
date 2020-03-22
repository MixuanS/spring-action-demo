package org.example.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author hc
 * @version 1.0.0
 * @date 2020/3/19
 */
public class SpitterWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    // 指定配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig_2.class };
    }

    // 将DispacterServlet映射到“/”
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}

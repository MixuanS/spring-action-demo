package org.example.controller;

import org.example.pojo.Spitter;
import org.example.repository.SpitterRepository;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class SpitterControllerTest {
//------------------表单--------------------------

    /**
     * 这个测试方法与首页控制器的测试非常类似。
     * 它对“/spitter/register”发送GET请求，然后断言结果的视图名为registerForm。
     *
     * @throws Exception
     */
    @Test
    public void shouldShowRegistration() throws Exception {
        SpitterRepository mockRepository = mock(SpitterRepository.class);
        SpitterController controller = new SpitterController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/spitter/register"))
                .andExpect(view().name("registerForm"));
    }

    /**
     * 当处理注册表单的POST请求时，控制器需要接受表单数据并将表单数据保存为Spitter对象。
     * 最后，为了防止重复提交（用户点击浏览器的刷新按钮有可能会发生这种情况），应该将浏览器重定向到新创建用户的基本信息页面。
     * 这些行为通过下面的shouldProcessRegistration()进行了测试。
     * <p>
     * 显然，这个测试比展现注册表单的测试复杂得多。
     * 在构建完SpitterRepository的mock实现以及所要执行的控制器和MockMvc之后，shouldProcessRegistration()对“/spitter/ register”发起了一个POST请求。
     * 作为请求的一部分，用户信息以参数的形式放到request中，从而模拟提交的表单。
     * ​在处理POST类型的请求时，在请求处理完成后，最好进行一下重定向，这样浏览器的刷新就不会重复提交表单了。
     * 在这个测试中，预期请求会重定向到“/spitter/jbauer”，也就是新建用户的基本信息页面。
     * ​最后，测试会校验SpitterRepository的mock实现最终会真正用来保存表单上传入的数据。
     */
    @Test
    public void shouldProcessRegistration() throws Exception {
        SpitterRepository mockRepository = mock(SpitterRepository.class);
        Spitter unsaved = new Spitter("jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
        Spitter saved = new Spitter(24L, "jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
        when(mockRepository.save(unsaved)).thenReturn(saved);

        SpitterController controller = new SpitterController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/spitter/register")
                .param("firstName", "Jack")
                .param("lastName", "Bauer")
                .param("username", "jbauer")
                .param("password", "24hours")
                .param("email", "jbauer@ctu.gov"))
                .andExpect(redirectedUrl("/spitter/jbauer"));

        verify(mockRepository, atLeastOnce()).save(unsaved);
    }
}
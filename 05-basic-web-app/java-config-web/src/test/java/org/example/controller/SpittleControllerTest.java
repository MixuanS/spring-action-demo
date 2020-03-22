package org.example.controller;

import org.example.pojo.Spittle;
import org.example.repository.SpittleRepository;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


/**
 * @author hc
 * @version 1.0.0
 * @date 2020/3/21
 */
public class SpittleControllerTest {
    // -------------------- 接受请求的输入 ------------------------------------------
    // -------------------- 处理查询参数 start --------------------------------------
    // 这个测试方法与程序清单5.9中的测试方法关键区别在于它针对“/spittles”发送GET请求，同时还传入了max和count参数。
    // 它测试了这些参数存在时的处理器方法，而另一个测试方法则测试了没有这些参数时的情景。
    // 这两个测试就绪后，我们就能确保不管控制器发生什么样的变化，它都能够处理这两种类型的请求：
    @Test
    public void shouldShowRecentSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(20);
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findSpittles(Long.MAX_VALUE, 20))
                .thenReturn(expectedSpittles);

        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/WEB-INF/views/spittles.jsp"))
                .build();

        mockMvc.perform(get("/spittles"))
                .andExpect(view().name("spittles"))
                .andExpect(model().attributeExists("spittleList"))
                .andExpect(model().attribute("spittleList",
                        hasItems(expectedSpittles.toArray())));
    }

    // 这个测试方法与程序清单5.9中的测试方法关键区别在于它针对“/spittles”发送GET请求，同时还传入了max和count参数。
    // 它测试了这些参数存在时的处理器方法，而另一个测试方法则测试了没有这些参数时的情景。
    // 这两个测试就绪后，我们就能确保不管控制器发生什么样的变化，它都能够处理这两种类型的请求：

    /**
     * 在Spittr应用中，我们可能需要处理的一件事就是展现分页的Spittle列表。
     * 在现在的SpittleController中，它只能展现最新的Spittle，并没有办法向前翻页查看以前编写的Spittle历史记录。
     * 如果你想让用户每次都能查看某一页的Spittle历史，那么就需要提供一种方式让用户传递参数进来，进而确定要展现哪些Spittle集合。
     * ​	在确定该如何实现时，假设我们要查看某一页Spittle列表，这个列表会按照最新的Spittle在前的方式进行排序。
     * 因此，下一页中第一条的ID肯定会早于当前页最后一条的ID。
     * 所以，为了显示下一页的Spittle，我们需要将一个Spittle的ID传入进来，这个ID要恰好小于当前页最后一条Spittle的ID。
     * 另外，你还可以传入一个参数来确定要展现的Spittle数量。
     * 为了实现这个分页的功能，我们所编写的处理器方法要接受如下的参数：
     * <p>
     * - before参数（表明结果中所有Spittle的ID均应该在这个值之前）。
     * - count参数（表明在结果中要包含的Spittle数量）。
     * <p>
     * ​	为了实现这个功能，我们将程序中的spittles()方法替换为使用before和count参数的新spittles()方法。
     * 我们首先添加一个测试，这个测试反映了新spittles()方法的功能。
     *
     * @throws Exception
     */
    @Test
    public void shouldShowPagedSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(50);
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findSpittles(238900, 50))
                .thenReturn(expectedSpittles);

        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/WEB-INF/views/spittles.jsp"))
                .build();

        mockMvc.perform(get("/spittles?max=238900&count=50"))
                .andExpect(view().name("spittles"))
                .andExpect(model().attributeExists("spittleList"))
                .andExpect(model().attribute("spittleList",
                        hasItems(expectedSpittles.toArray())));
    }

    private List<Spittle> createSpittleList(int count) {
        List<Spittle> spittles = new ArrayList<Spittle>();
        for (int i = 0; i < count; i++) {
            spittles.add(new Spittle("Spittle " + i, new Date()));
        }
        return spittles;
    }
    // -------------------- 处理查询参数 end --------------------------------------

    // -------------------- 通过路径参数接受输入 start --------------------------------------

    /**
     * 前者能够识别出要查询的资源，而后者描述的是带有参数的一个操作——本质上是通过HTTP发起的RPC。
     * ​既然已经以面向资源的控制器作为目标，那我们将这个需求转换为一个测试。
     * 下面展现了一个新的测试方法，它会断言SpittleController中对面向资源请求的处理。
     *
     * 可以看到，这个测试构建了一个mock Repository、一个控制器和MockMvc，这与本章中我们所编写的其他测试很类似。
     * 这个测试中最重要的部分是最后几行，它对“/spittles/12345”发起GET请求，然后断言视图的名称是spittle，并且预期的Spittle对象放到了模型之中。
     * 因为我们还没有为这种请求实现处理器方法，因此这个请求将会失败。但是，我们可以通过为SpittleController添加新的方法来修正这个失败的测试。
     *
     * ​	到目前为止，在我们编写的控制器中，所有的方法都映射到了（通过@RequestMapping）静态定义好的路径上。
     * 但是，如果想让这个测试通过的话，我们编写的@RequestMapping要包含变量部分，这部分代表了Spittle ID。
     * 为了实现这种路径变量，Spring MVC允许我们在@RequestMapping路径中添加占位符。
     * 占位符的名称要用大括号（“{”和“}”）括起来。路径中的其他部分要与所处理的请求完全匹配，但是占位符部分可以是任意的值。
     */
    @Test
    public void testSpittle() throws Exception {
        Spittle expectedSpittle = new Spittle("Hello", new Date());
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findOne(12345)).thenReturn(expectedSpittle);

        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/spittles/12345"))
                .andExpect(view().name("spittle"))
                .andExpect(model().attributeExists("spittle"))
                .andExpect(model().attribute("spittle", expectedSpittle));
    }


}

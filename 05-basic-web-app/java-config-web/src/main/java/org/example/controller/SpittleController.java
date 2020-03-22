package org.example.controller;

import org.example.pojo.Spittle;
import org.example.repository.SpittleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hc
 * @version 1.0.0
 * @date 2020/3/21
 */
@Controller
@RequestMapping("/spittles")
public class SpittleController {

    private SpittleRepository spittleRepository;

    // Error: 元素值必须为常量表达式
    // private final static  String MAX_LONG_AS_STRING = Long.toString(Long.MAX_VALUE);

    private static final String MAX_LONG_AS_STRING = ""+ Long.MAX_VALUE ;
    /**
     * 	我们可以看到SpittleController有一个构造器，这个构造器使用了@Autowired注解，
     * 	用来注入SpittleRepository。这个SpittleRepository随后又用在spittles()方法中，用来获取最新的spittle列表。
     * @param spittleRepository
     */
    @Autowired
    public SpittleController(SpittleRepository spittleRepository) {
        this.spittleRepository = spittleRepository;
    }

    /**
     * 需要注意的是，我们在spittles()方法中给定了一个Model作为参数。
     * 这样，spittles()方法就能将Repository中获取到的Spittle列表填充到模型中。
     * Model实际上就是一个Map（也就是key-value对的集合），它会传递给视图，这样数据就能渲染到客户端了。
     * 当调用addAttribute()方法并且不指定key的时候，那么key会根据值的对象类型推断确定。
     * 在本例中，因为它是一个List<Spittle>，因此，键将会推断为spittleList。
     *
     * ​	spittles()方法所做的最后一件事是返回spittles作为视图的名字，这个视图会渲染模型。
     *
     * ​	如果你希望显式声明模型的key的话，那也尽可以进行指定。
     */
    // @RequestMapping(method = RequestMethod.GET)
    // public String spittles(Model model) {
    //     // 将spittle添加到模型中
    //     model.addAttribute(spittleRepository.findSpittles(Long.MAX_VALUE, 20));
    //     // 返回视图名
    //     return "spittles";
    // }

    /**
     *​这个版本与其他的版本有些差别。它并没有返回视图名称，也没有显式地设定模型，这个方法返回的是Spittle列表。
     * 当处理器方法像这样返回对象或集合时，这个值会放到模型中，模型的key会根据其类型推断得出（在本例中，也就是spittleList）。
     * ​而逻辑视图的名称将会根据请求路径推断得出。因为这个方法处理针对“spittles”的GET请求，因此视图的名称将会是spittles（去掉开头的斜线）。
     * ​不管你选择哪种方式来编写spittles()方法，所达成的结果都是相同的。
     * 模型中会存储一个Spittle列表，key为spittleList，然后这个列表会发送到名为spittles的视图中。按照我们配置InternalResourceViewResolver的方式，视图的JSP将会是“/WEB-INF/views/spittles.jsp”。
     */
    // @RequestMapping(method= RequestMethod.GET)
    // public List<Spittle> spittles(Model model) {
    //     return spittleRepository.findSpittles(Long.MAX_VALUE,20);
    // }

    /**
     * 接收两个参数来实现分页,缺陷是无法处理参数不存在的情况
     */
    // @RequestMapping(method = RequestMethod.GET)
    // public List<Spittle> spittles(
    //         @RequestParam("max") long max,
    //         @RequestParam("count") long count) {
    //     return spittleRepository.findSpittles(max, count);
    // }

    /**
     * 现在，如果max参数没有指定的话，它将会是Long类型的最大值
     * @param max
     * @param count
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public List<Spittle> spittleList(
            @RequestParam(value="max", defaultValue= MAX_LONG_AS_STRING) long max,
            @RequestParam(value="count", defaultValue="20") int count) {
        return spittleRepository.findSpittles(max,count);
    }

    /**
     * 假设我们的应用程序需要根据给定的ID来展现某一个Spittle记录。
     * 其中一种方案就是编写处理器方法，通过使用@RequestParam注解，让它接受ID作为查询参数：
     * 这个处理器方法将会处理形如“/spittles/show?spittle_id=12345”这样的请求。
     * 尽管这也可以正常工作，但是从面向资源的角度来看这并不理想。
     * 在理想情况下，要识别的资源（Spittle）应该通过URL路径进行标示，而不是通过查询参数。
     * 对“/spittles/12345”发起GET请求要优于对“/spittles/show?spittle_id=12345”发起请求。
     */
    @RequestMapping(value="/show", method=RequestMethod.GET)
    public String showSpittle(
            @RequestParam("spittle_id") long spittleId,
            Model model) {
        model.addAttribute(spittleRepository.findOne(spittleId));
        return "spittle";
    }

    /**
     * 下面的处理器方法使用了占位符，将Spittle ID作为路径的一部分：
     * 我们可以看到，spittle()方法的spittleId参数上添加了@PathVariable("spittleId")注解，
     * 这表明在请求路径中，不管占位符部分的值是什么都会传递到处理器方法的spittleId参数中。
     * 如果对“/spittles/54321”发送GET请求，那么将会把“54321”传递进来，作为spittleId的值。
     */
    // @RequestMapping(value="/{spittleId}",method=RequestMethod.GET)
    // public String spittle(
    //         @PathVariable("spittleId") long spittleId,
    //         Model model) {
    //     model.addAttribute(spittleRepository.findOne(spittleId));
    //     return "spittle";
    // }

    /**
     * 在样例中spittleId这个词出现了好几次：先是在@RequestMapping的路径中，然后作为@PathVariable属性的值，最后又作为方法的参数名称。
     * 因为方法的参数名碰巧与占位符的名称相同，因此我们可以去掉@PathVariable中的value属性：
     */
    @RequestMapping(value="/{spittleId}",method=RequestMethod.GET)
    public String spittle(@PathVariable long spittleId, Model model) {
        model.addAttribute(spittleRepository.findOne(spittleId));
        return "spittle";
    }
}

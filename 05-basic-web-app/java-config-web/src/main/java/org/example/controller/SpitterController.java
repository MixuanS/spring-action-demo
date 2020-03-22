package org.example.controller;

import org.example.pojo.Spitter;
import org.example.repository.SpitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *Web应用的功能通常并不局限于为用户推送内容。
 * 大多数的应用允许用户填充表单并将数据提交回应用中，通过这种方式实现与用户的交互。
 * 像提供内容一样，Spring MVC的控制器也为表单处理提供了良好的支持。
 *
 * ​使用表单分为两个方面：展现表单以及处理用户通过表单提交的数据。
 * 在Spittr应用中，我们需要有个表单让新用户进行注册。SpitterController是一个新的控制器，目前只有一个请求处理的方法来展现注册表单。
 */
@Controller
@RequestMapping("/spitter")
public class SpitterController {
    private SpitterRepository spitterRepository;

    @Autowired
    public SpitterController(SpitterRepository spitterRepository){
        this.spitterRepository = spitterRepository;
    }


    /**
     * 	showRegistrationForm()方法的@RequestMapping注解以及类级别上的@RequestMapping注解组合起来，
     * 	声明了这个方法要处理的是针对“/spitter/register”的GET请求。这是一个简单的方法，没有任何输入并且只是返回名为registerForm的逻辑视图。
     * 	按照我们配置InternalResourceViewResolver的方式，这意味着将会使用“/WEB-INF/views/registerForm.jsp”这个JSP来渲染注册表单。
     * 	尽管showRegistrationForm()方法非常简单，但测试依然需要覆盖到它。因为这个方法很简单，所以它的测试也比较简单。
     */
    @RequestMapping(value="/register", method=GET)
    public String showRegistrationForm() {
        return "registerForm";
    }

    /**
     *  接受一个Spitter对象作为参数。这个对象有firstName、lastName、username和password属性，这些属性将会使用请求中同名的参数进行填充。
     * ​	当使用Spitter对象调用processRegistration()方法时，它会进而调用SpitterRepository的save()方法，SpitterRepository是在SpitterController的构造器中注入进来的。
     * ​	processRegistration()方法做的最后一件事就是返回一个String类型，用来指定视图。但是这个视图格式和以前我们所看到的视图有所不同。这里不仅返回了视图的名称供视图解析器查找目标视图，而且返回的值还带有重定向的格式。
     * ​	当InternalResourceViewResolver看到视图格式中的“redirect:”前缀时，它就知道要将其解析为重定向的规则，而不是视图的名称。在本例中，它将会重定向到用户基本信息的页面。例如，如果Spitter.username属性的值为“jbauer”，那么视图将会重定向到“/spitter/jbauer”。
     * ​	需要注意的是，除了“redirect:”，InternalResourceViewResolver还能识别“forward:”前缀。当它发现视图格式中以“forward:”作为前缀时，请求将会前往（forward）指定的URL路径，而不再是重定向。
     */
    // @RequestMapping(value="/register", method=POST)
    // public String processRegistration(Spitter spitter) {
    //     // 保存Spitter
    //     spitterRepository.save(spitter);
    //     // 重定向到基本信息页
    //     return "redirect:/spitter/" + spitter.getUsername();
    // }

    /**
     * 与程序清单5.17中最初的processRegistration()方法相比，这里有了很大的变化。
     * Spitter参数添加了@Valid注解，这会告知Spring，需要确保这个对象满足校验限制。
     * 在Spitter属性上添加校验限制并不能阻止表单提交。
     * 即便用户没有填写某个域或者某个域所给定的值超出了最大长度，processRegistration()方法依然会被调用。
     * 这样，我们就需要处理校验的错误，就像在processRegistration()方法中所看到的那样。
     * 如果有校验出现错误的话，那么这些错误可以通过Errors对象进行访问，现在这个对象已作为processRegistration()方法的参数。
     * （很重要一点需要注意，Errors参数要紧跟在带有@Valid注解的参数后面，@Valid注解所标注的就是要检验的参数。）
     * processRegistration()方法所做的第一件事就是调用Errors.hasErrors()来检查是否有错误。
     * 如果有错误的话，Errors.hasErrors()将会返回到registerForm，也就是注册表单的视图。
     * 这能够让用户的浏览器重新回到注册表单页面，所以他们能够修正错误，然后重新尝试提交。
     * 现在，会显示空的表单，但是在下一章中，我们将在表单中显示最初提交的值并将校验错误反馈给用户。
     * 如果没有错误的话，Spitter对象将会通过Repository进行保存，控制器会像之前那样重定向到基本信息页面。
     */
    @RequestMapping(value="/register", method=POST)
    public String processRegistration(@Valid Spitter spitter, Errors errors) {
        if (errors.hasErrors()) {
            return "registerForm";
        }
        spitterRepository.save(spitter);
        return "redirect:/spitter/" + spitter.getUsername();
    }

    /**
     * 用来处理对基本信息页面的请求
     * SpitterRepository通过用户名获取一个Spitter对象，showSpitterProfile()得到这个对象并将其添加到模型中，
     * 然后返回profile，也就是基本信息页面的逻辑视图名。
     */
    @RequestMapping(value="/{username}", method=GET)
    public String showSpitterProfile(@PathVariable String username, Model model) {
        Spitter spitter = spitterRepository.findByUsername(username);
        model.addAttribute(spitter);
        return "profile";
    }
}

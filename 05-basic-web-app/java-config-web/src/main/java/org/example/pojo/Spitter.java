package org.example.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 如果用户在提交表单的时候，username或password文本域为空的话，那么将会导致在新建Spitter对象中，username或password是空的String。至少这是一种怪异的行为
 * 。如果这种现象不处理的话，这将会出现安全问题，因为不管是谁只要提交一个空的表单就能登录应用。
 * ​同时，我们还应该阻止用户提交空的firstName或lastName，使应用仅在一定程度上保持匿名性。
 * 有个好的办法就是限制这些输入域值的长度，保持它们的值在一个合理的长度范围，避免这些输入域的误用。
 * ​有种处理校验的方式非常初级，那就是在processRegistration()方法中添加代码来检查值的合法性，如果值不合法的话，就将注册表单重新显示给用户。
 * 这是一个很简短的方法，因此，添加一些额外的if语句也不是什么大问题，对吧？
 * ​与其让校验逻辑弄乱我们的处理器方法，还不如使用Spring对Java校验API（Java Validation API，又称JSR-303）的支持。
 * 从Spring 3.0开始，在Spring MVC中提供了对Java校验API的支持。在Spring MVC中要使用Java校验API的话，并不需要什么额外的配置。只要保证在类路径下包含这个Java API的实现即可，比如Hibernate Validator。
 */
public class Spitter {
    /**
     * @Null 所注解元素的值必须为 null
     * @Size 所注解的元素的值必须是 String、集合或数组，并且它的长度要符合给定的范围
     *
     * spitter的所有属性都添加了@NotNull注解，以确保它们的值不为null。
     * 类似地，属性上也添加了@Size注解以限制它们的长度在最大值和最小值之间。
     * 对Spittr应用来说，这意味着用户必须要填完注册表单，并且值的长度要在给定的范围内。
     */

    private Long id;

    @NotNull
    @Size(min = 5, max = 16)
    private String username;

    @NotNull
    @Size(min = 5, max = 25)
    private String password;

    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @NotNull
    private String email;

    public Spitter() {
    }

    public Spitter(String username, String password, String firstName, String lastName, String email) {
        this(null, username, password, firstName, lastName, email);
    }

    public Spitter(Long id, String username, String password, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, "firstName", "lastName", "username", "password", "email");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "firstName", "lastName", "username", "password", "email");
    }

}

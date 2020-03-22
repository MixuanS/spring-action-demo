package org.example.repository;

import org.example.pojo.Spitter;

/**
 * @author hc
 * @version 1.0.0
 * @date 2020/3/23
 */
public interface SpitterRepository {
    /**
     * 保存用户
     * @param spitter 用户
     * @return
     */
    Spitter save(Spitter spitter);

    /**
     * 通过用户名称查询用户
     * @param username
     * @return
     */
    Spitter findByUsername(String username);
}

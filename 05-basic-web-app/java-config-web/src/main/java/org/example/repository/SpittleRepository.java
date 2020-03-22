package org.example.repository;

import org.example.pojo.Spittle;

import java.util.List;

/**
 * @author hc
 * @version 1.0.0
 * @date 2020/3/21
 */
public interface SpittleRepository {

    List<Spittle> findSpittles(long max, long count);

    Spittle findOne(long id);
}
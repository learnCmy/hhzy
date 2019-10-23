package com.hhzy.crm.common.base;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 14:27
 * @Description:
 */
public interface BaseService<T> {
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    public T queryById(Long id);

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<T> queryAll();

    /**
     * 根据条件查询一条数据，如果该条件所查询的数据为多条会抛出异常
     *
     * @param record
     * @return
     */
    public T queryOne(T record) ;

    /**
     * 根据条件查询多条数据
     *
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record) ;

    /**
     * 根据条件分页查询数据
     *
     * @param record
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record, Integer page, Integer rows) ;


    /**
     * 新增数据
     *
     * @param t
     * @return
     */
    public Integer save(T t) ;

    /**
     * 选择不为null的字段作为插入数据的字段来插入数据
     *
     * @param t
     * @return
     */
    public Integer saveSelective(T t) ;

    /**
     * 更新数据
     *
     * @param t
     * @return
     */
    public Integer update(T t) ;

    /**
     * 选择不为null的字段作为更新的字段来更新数据
     *
     * @param t
     * @return
     */
    public Integer updateSelective(T t) ;

    /**
     * 根据主键id删除数据（物理删除）
     *
     * @param id
     * @return
     */
    public Integer deleteById(Long id) ;

    /**
     * 批量删除数据
     *
     * @param ids
     * @param clazz
     * @param property
     * @return
     */
    public Integer deleteByIds(List<Object> ids, Class<T> clazz, String property);


    /**
     * 根据条件删除数据
     *
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record) ;
}

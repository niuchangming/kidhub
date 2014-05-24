package interfaces;

import java.util.Collection;

public interface IDaoBase<T> {
 	/**
     * 不需确定新建实体是否已经存在
     */
    public T save(T t);
 
    /**
     * 不需确定新建实体是否已经存在
     */
    public Collection<T> save(Collection<T> ts);
 
    /**
     * 可确定为新建实体；返回处于托管状态的实例
     */
    public T persist(T t);
 
    public boolean persist(Collection<T> ts);
 
    /**
     * 若数据库中已有此记录，置为托管状态
     */
    public T merge(T t);
 
    /**
     * 若数据库中已有此记录，置为托管状态
     */
    public Collection<T> merge(Collection<T> t);
 
    /**
     * 删除
     */
    public void remove(T t);
 
    /**
     * 批量删除 传入集合
     */
    public void remove(Collection<T> ts);
    /**
     * 删除指定id、指定class的实例
     */
    public void remove(Class<T> clazz, String id);
 
    /**
49
     * 删除指定id、指定class的实例
50
     */
    public void remove(Class<T> clazz, Collection<String> ids);
 
    /**
     * 清除一级缓存
     */
    public void clear();
 
    /**
     * 将对象置为游离状态
     */
    public void detach(T t);
 
    /**
     * 将对象置为游离状态
     */
    public void detach(Collection<T> ts);
 
    /**
     * 检查实例是否处于托管状态
     */
    public boolean isManaged(T t);
 
    /**
     * 同步jpa容器和数据库
     */
    public void flush();

}

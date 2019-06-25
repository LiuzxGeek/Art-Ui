package com.art.uilibrary.base;

/**
 * @Author liuxian
 * @Date 2017/8/14 12:28
 * @Des
 */

public interface IAdapterListener {
    interface IOnChildClickListener<D> {//单数据返回

        void onChildClickListener(D data);

    }

    interface IOnItemClickListener<T, D> {//双数据返回

        void onItemClickListener(T type, D... data);

    }
}

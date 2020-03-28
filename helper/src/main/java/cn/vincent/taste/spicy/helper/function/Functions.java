/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.vincent.taste.spicy.helper.function;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author vincent
 * @version 1.0 2019/9/23 15:34
 */
public class Functions {

    public interface Callback0 {

        /**
         * 执行
         *
         * @throws Exception 异常
         */
        void invoke() throws Exception;
    }

    public interface Callback<A> {

        /**
         * 执行
         *
         * @param a 参数
         * @throws Exception 异常
         */
        void invoke(A a) throws Exception;
    }

    public interface Callback2<A, B> {

        /**
         * 执行
         *
         * @param a 参数
         * @param b 参数
         * @throws Exception 异常
         */
        void invoke(A a, B b) throws Exception;
    }

    public interface Callback3<A, B, C> {

        /**
         * 执行
         *
         * @param a 参数
         * @param b 参数
         * @param c 参数
         * @throws Exception 异常
         */
        void invoke(A a, B b, C c) throws Exception;
    }

    public interface Function0<R> {

        /**
         * 执行
         *
         * @return 返回
         * @throws Exception 异常
         */
        R apply() throws Exception;
    }

    public interface Function<A, R> {

        /**
         * 执行
         *
         * @param a 参数
         * @return 返回
         * @throws Exception 异常
         */
        R apply(A a) throws Exception;
    }

    public interface Function2<A, B, R> {

        /**
         * 执行
         *
         * @param a 参数
         * @param b 参数
         * @return 返回
         * @throws Exception 异常
         */
        R apply(A a, B b) throws Exception;
    }

    public interface Function3<A, B, C, R> {

        /**
         * 执行
         *
         * @param a 参数
         * @param b 参数
         * @param c 参数
         * @return 返回
         * @throws Exception 异常
         */
        R apply(A a, B b, C c) throws Exception;
    }

    public static abstract class Option<T> implements Collection<T> {

        /**
         * 是否定义
         *
         * @return true/false
         */
        public abstract boolean isDefined();

        @Override
        public boolean isEmpty() {
            return !isDefined();
        }

        /**
         * 获取
         *
         * @return 对象
         */
        public abstract T get();

        public T getOrElse(T defaultValue) {
            if (isDefined()) {
                return get();
            } else {
                return defaultValue;
            }
        }

        @SuppressWarnings("unchecked")
        public <A> Option<A> map(Function<T, A> function) {
            if (isDefined()) {
                try {
                    return some(function.apply(get()));
                } catch (RuntimeException e) {
                    throw e;
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            } else {
                return none();
            }
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException();
        }
    }

    public static class None<T> extends Option<T> {

        @Override
        public boolean isDefined() {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public T get() {
            throw new IllegalStateException("No value");
        }

        @SuppressWarnings("RedundantCollectionOperation")
        @Override
        public Iterator<T> iterator() {
            return Collections.<T>emptyList().iterator();
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.size() == 0;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <R> R[] toArray(R[] r) {
            Arrays.fill(r, null);
            return r;
        }

        @Override
        public String toString() {
            return "None";
        }

    }

    public static class Some<T> extends Option<T> {

        final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public boolean isDefined() {
            return true;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Iterator<T> iterator() {
            return Collections.singletonList(value).iterator();
        }

        @Override
        public boolean contains(Object o) {
            return o != null && o.equals(value);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return (c.size() == 1) && (c.toArray()[0].equals(value));
        }

        @Override
        public Object[] toArray() {
            Object[] result = new Object[1];
            result[0] = value;
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> R[] toArray(R[] r) {
            if (r.length == 0) {
                R[] array = Arrays.copyOf(r, 1);
                array[0] = (R) value;
                return array;
            } else {
                Arrays.fill(r, 1, r.length, null);
                r[0] = (R) value;
                return r;
            }
        }

        @Override
        public String toString() {
            return "Some(" + value + ")";
        }
    }

    public static <A> Some<A> some(A value) {
        return new Some<>(value);
    }

    public static None none() {
        return new None<>();
    }

    public static class Either<A, B> {

        final public Option<A> left;

        final public Option<B> right;

        private Either(Option<A> left, Option<B> right) {
            this.left = left;
            this.right = right;
        }

        @SuppressWarnings("unchecked")
        public static <A, B> Either<A, B> left(A value) {
            return new Either<A, B>(some(value), none());
        }

        @SuppressWarnings("unchecked")
        public static <A, B> Either<A, B> right(B value) {
            return new Either<A, B>(none(), some(value));
        }

        @Override
        public String toString() {
            return "Either(left: " + left + ", right: " + right + ")";
        }
    }

    public static class Tuple<A, B> {

        final public A _1;
        final public B _2;

        public Tuple(A _1, B _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public String toString() {
            return "Tuple2(_1: " + _1 + ", _2: " + _2 + ")";
        }
    }

    public static <A, B> Tuple<A, B> tuple(A a, B b) {
        return new Tuple<>(a, b);
    }

    public static class Tuple3<A, B, C> {

        final public A _1;
        final public B _2;
        final public C _3;

        public Tuple3(A _1, B _2, C _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public String toString() {
            return "Tuple3(_1: " + _1 + ", _2: " + _2 + ", _3:" + _3 + ")";
        }
    }

    public static <A, B, C> Tuple3<A, B, C> tuple3(A a, B b, C c) {
        return new Tuple3<>(a, b, c);
    }

    public static class Tuple4<A, B, C, D> {

        final public A _1;
        final public B _2;
        final public C _3;
        final public D _4;

        public Tuple4(A _1, B _2, C _3, D _4) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
        }

        @Override
        public String toString() {
            return "Tuple4(_1: " + _1 + ", _2: " + _2 + ", _3:" + _3 + ", _4:" + _4 + ")";
        }
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> tuple4(A a, B b, C c, D d) {
        return new Tuple4<>(a, b, c, d);
    }

    public static class Tuple5<A, B, C, D, E> {

        final public A _1;
        final public B _2;
        final public C _3;
        final public D _4;
        final public E _5;

        public Tuple5(A _1, B _2, C _3, D _4, E _5) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
        }

        @Override
        public String toString() {
            return "Tuple5(_1: " + _1 + ", _2: " + _2 + ", _3:" + _3 + ", _4:" + _4 + ", _5:" + _5 + ")";
        }
    }

    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> tuple5(A a, B b, C c, D d, E e) {
        return new Tuple5<>(a, b, c, d, e);
    }
}

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

package cn.vincent.taste.spicy.slog4j.aspect;

import cn.vincent.taste.spicy.helper.basic.LogHelper;
import cn.vincent.taste.spicy.helper.type.ValueSerializer;
import cn.vincent.taste.spicy.slog4j.annotation.Slog4j;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

/**
 * 记录入参和出参结果
 *
 * @author vincent
 * @version 1.0 2017/8/20 17:09
 */
@Slf4j
@Aspect
public class Slog4jAspect implements Ordered {

    private ValueSerializer serializer;

    @Getter
    @Setter
    private int order = -100;

    @Setter
    private List<String> filters;

    public Slog4jAspect(ValueSerializer serializer) {
        this.serializer = serializer;
    }

    @Pointcut("@within(cn.vincent.taste.spicy.slog4j.annotation.Slog4j) || @annotation(cn.vincent.taste.spicy.slog4j.annotation.Slog4j)")
    public void slog4jPointCut() {

    }

    @Around("slog4jPointCut()")
    public Object aroundLogAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Slog4j slog4j = this.getAnnotation(joinPoint);
        if (slog4j.disable()) {
            return joinPoint.proceed();
        }

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.info(LogHelper.getSignLogStr(slog4j.value(), "request args->null"));
        } else {
            int[] indexes = slog4j.filter();
            List<Integer> indexList = new ArrayList<>(indexes.length);
            for (int index : indexes) {
                indexList.add(index);
            }

            MethodSignature msig = (MethodSignature) joinPoint.getSignature();
            Class<?>[] parameterTypes = msig.getMethod().getParameterTypes();
            for (int i = 0; i < args.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                String className = parameterType.getName();
                boolean flag = (indexList.contains(i) || (filters != null && filters.contains(className)));
                if (flag) {
                    log.info(LogHelper.getSignLogStr(slog4j.value(), "request args" + i + "->" + parameterType.getName() + " has been filter, no log"));
                } else {
                    log.info(LogHelper.getSignLogStr(slog4j.value(), "request args" + i + "->" + parameterType.getName(), this.serializer.serialize(args[i])));
                }
            }
        }

        Object obj;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.info(LogHelper.getSignLogStr(slog4j.value(), "response result->error", throwable.getMessage()));
            throw throwable;
        }

        if (obj == null) {
            log.info(LogHelper.getSignLogStr(slog4j.value(), "response result->null"));
        } else {
            log.info(LogHelper.getSignLogStr(slog4j.value(), "response result->" + obj.getClass().getName(), this.serializer.serialize(obj)));
        }

        return obj;
    }

    private Slog4j getAnnotation(ProceedingJoinPoint joinPoint) throws SignatureException {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new SignatureException("The Signature's real type is not MethodSignature but " + signature.getClass().getName());
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Slog4j anno = methodSignature.getMethod().getAnnotation(Slog4j.class);
        if (null == anno) {
            anno = methodSignature.getMethod().getDeclaringClass().getAnnotation(Slog4j.class);
        }
        return anno;
    }
}

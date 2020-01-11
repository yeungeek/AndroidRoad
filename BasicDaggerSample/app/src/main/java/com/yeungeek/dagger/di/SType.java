package com.yeungeek.dagger.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author yangjian
 * @date 2018/03/03
 */

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SType {
}

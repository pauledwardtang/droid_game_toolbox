package com.tangly.framework;

public interface Builder<T>
{
    public Builder<T> getBuilder (T type);

    public T build ();
}

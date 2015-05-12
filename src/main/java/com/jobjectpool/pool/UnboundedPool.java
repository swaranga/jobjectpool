package com.jobjectpool.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class UnboundedPool<T> extends AbstractPool<T> {
    private static final int INITIAL_ELEMENTS = 16;

    private BlockingQueue<T> objects;

    private Validator<T> validator;
    private ObjectFactory<T> objectFactory;

    private volatile boolean shutdownCalled;

    public UnboundedPool(Validator<T> validator, ObjectFactory<T> objectFactory) {
        super();

        this.objectFactory = objectFactory;
        this.validator = validator;

        objects = new LinkedBlockingQueue<T>();

        initializeObjects();

        shutdownCalled = false;
    }

    public T get() {
        if (!shutdownCalled) {
            T t = null;

            try {
                t = objects.take();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            return t;
        }

        throw new IllegalStateException("Object pool is already shutdown");
    }

    public void shutdown() {
        shutdownCalled = true;

        clearResources();
    }

    private void clearResources() {
        for (T t : objects) {
            validator.invalidate(t);
        }
    }

    protected void returnToPool(T t) {
        if (validator.isValid(t)) {
            objects.add(t);
        }
    }

    @Override
    protected void handleInvalidReturn(T t) {

    }

    protected boolean isValid(T t) {
        return validator.isValid(t);
    }

    private void initializeObjects() {
        for (int i = 0; i < INITIAL_ELEMENTS; i++) {
            objects.add(objectFactory.createNew());
        }
    }
}

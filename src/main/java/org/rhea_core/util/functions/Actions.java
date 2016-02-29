package org.rhea_core.util.functions;

/**
 * @author Orestis Melkonian
 */
public final class Actions {
    public final static EmptyAction EMPTY = new EmptyAction<>();
    private static final class EmptyAction<T0, T1, T2, T3, T4, T5, T6, T7, T8> implements
            Action0,
            Action1<T0>,
            Action2<T0, T1>,
            Action3<T0, T1, T2>,
            Action4<T0, T1, T2, T3>,
            Action5<T0, T1, T2, T3, T4>,
            Action6<T0, T1, T2, T3, T4, T5>,
            Action7<T0, T1, T2, T3, T4, T5, T6>,
            Action8<T0, T1, T2, T3, T4, T5, T6, T7>,
            Action9<T0, T1, T2, T3, T4, T5, T6, T7, T8>,
            ActionN {

        public EmptyAction() {
        }

        @Override
        public void call() {
        }

        @Override
        public void call(T0 t1) {
        }

        @Override
        public void call(T0 t1, T1 t2) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3, T3 t4) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6, T6 t7) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6, T6 t7, T7 t8) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6, T6 t7, T7 t8, T8 t9) {
        }

        @Override
        public void call(Object... args) {
        }
    }
}


package org.rhea_core.internal.expressions.combining;

import org.rhea_core.internal.expressions.MultipleInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.creation.FromExpr;
import org.rhea_core.util.functions.*;

import java.util.Iterator;

/**
 * @author Orestis Melkonian
 */
public class ZipExpr<T1,T2,T3,T4,T5,T6,T7,T8,T9,R> extends MultipleInputExpr<Object> implements Transformer<R> {
    public int id;
    public String type;

    public Func2<? super T1, ? super T2, ? extends R> combiner2;
    public Func3<? super T1, ? super T2, ? super T3, ? extends R> combiner3;
    public Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner4;
    public Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner5;
    public Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner6;
    public Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner7;
    public Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner8;
    public Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner9;

    private ZipExpr(int id) {
        this.id = id;
    }

    public ZipExpr(int id, String type) {
        this(id);
        this.type = type;
    }
    public ZipExpr(int id, String type, Func2<? super T1, ? super T2, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner2 = combiner;
    }
    public ZipExpr(int id, String type, Func3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner3 = combiner;
    }
    public ZipExpr(int id, String type, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner4 = combiner;
    }
    public ZipExpr(int id, String type, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner5 = combiner;
    }
    public ZipExpr(int id, String type, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner6 = combiner;
    }
    public ZipExpr(int id, String type, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner7 = combiner;
    }
    public ZipExpr(int id, String type, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner8 = combiner;
    }
    public ZipExpr(int id, String type, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
        this(id);
        this.type = type;
        this.combiner9 = combiner;
    }

    public int getArgumentNo() {
        if (combiner9 != null)
            return 9;
        else if (combiner8 != null)
            return 8;
        else if (combiner7 != null)
            return 7;
        else if (combiner6 != null)
            return 6;
        else if (combiner5 != null)
            return 5;
        else if (combiner4 != null)
            return 4;
        else if (combiner3 != null)
            return 3;
        else
            return 2;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ZipExpr))
            return false;
        return id == ((ZipExpr) obj).id;
    }

    @Override
    public Transformer<R> clone() {
        if (combiner9 != null) return new ZipExpr<>(id, type, combiner9);
        if (combiner8 != null) return new ZipExpr<>(id, type, combiner8);
        if (combiner7 != null) return new ZipExpr<>(id, type, combiner7);
        if (combiner6 != null) return new ZipExpr<>(id, type, combiner6);
        if (combiner5 != null) return new ZipExpr<>(id, type, combiner5);
        if (combiner4 != null) return new ZipExpr<>(id, type, combiner4);
        if (combiner3 != null) return new ZipExpr<>(id, type, combiner3);
        return new ZipExpr<>(id, type, combiner2);
    }
}


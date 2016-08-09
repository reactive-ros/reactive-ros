package org.rhea_core.internal.expressions;

import org.rhea_core.util.IdMinter;

/**
 * Base class for expressions that hold a unique ID.
 * @author Orestis Melkonian
 */
public abstract class WithId {
    public int id = IdMinter.next();

    // For chain calling
    public WithId withID(int id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName().replaceFirst("Expr", "") + id;
    }

    @Override
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof WithId))
            return false;
        return id == ((WithId) other).id;
    }     
}

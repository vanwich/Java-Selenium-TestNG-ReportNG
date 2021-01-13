package help.ws.rest.model;

import help.data.TestData;
import help.ws.rest.comparison.ModelComparator;

public class Model implements IModel{
    public Model(TestData testData) {
        ((Adjustable) this).adjust(testData);
    }

    public Model() {
    }


    /**
     * Two models are equal in case if each field included for equality {@link Equality#include()}
     * have the same content
     * <p>
     * Throws IllegalStateException in case Model doesn't contain at least one field marked with {@link Equality}
     *
     * @param o Model to compare with this
     * @return true if models are equals and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Model that = (Model) o;
        return ModelComparator.compare(this, that).isEquals();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

import java.io.Serializable;

//edge class
public class GraphEdge implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object label = null;
    private Object value = null;

    // Constructor
    public GraphEdge(Object label, Object value) {
        this.label = label;
        this.value = value;
    }

    /**********************************************************************/
    // Accessors
    public Object getLabel() {
        return this.label;
    }

    public Object getValue() {
        return this.value;
    }

    public String toString() {
        return "Label: " + label + "Value: " + value;
    }

    // Mutators
    /**********************************************************************/
    public void setValue(Object value) {
        this.value = value;
    }
}
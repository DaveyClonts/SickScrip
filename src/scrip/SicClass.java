package scrip;

import java.util.List;
import java.util.Map;

/**
 * Stores class data and enables instances to be created.
 */
public class SicClass implements SicCallable {

    final String name;
    final SicClass superclass;
    final Map<String, SicFunction> methods;

    SicClass(String name, SicClass superclass, Map<String, SicFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }

    SicFunction findMethod(String name) {
        // Check if the method exists in this class.
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        // Check if the method exists in the super class.
        if (superclass != null) {
            return superclass.findMethod(name);
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        SicFunction initializer = findMethod("init");
        // No init method was defined.
        if (initializer == null) {
            return 0;
        }
        return initializer.arity();
    }

    /**
     * Creates a new instance of this class and invokes the init method if it
     * exists.
     */
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        // Create a new instance of this SicClass
        SicInstance instance = new SicInstance(this);
        SicFunction initializer = findMethod("init");
        if (initializer != null) {
            // Connect the initializer to the instance then call it.
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}

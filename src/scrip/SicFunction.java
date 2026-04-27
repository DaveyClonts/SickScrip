package scrip;

import java.util.List;

public class SicFunction implements SicCallable {

    private final Stmt.Function declaration;
    private final Environment closure;
    private final boolean isInitializer;

    public SicFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
    }

    /**
     * Creates an environment holding {@code instance} as 'this' and attaches it
     * to a copy of this function.
     */
    SicFunction bind(SicInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("dis", instance);
        return new SicFunction(declaration, environment, isInitializer);
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    /**
     * Creates an environment for the function then populates it with the
     * arguments of the function call before executing the function body.
     */
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);

        // Add arguments to the environment
        for (int i = 0; i < declaration.params.size(); i++) {
            String argumentName = declaration.params.get(i).lexeme;
            environment.define(argumentName, arguments.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            // Return was thrown to indicate that this function has terminated.

            // Returning early in an initialiser returns a reference to the 
            // class
            if (isInitializer) {
                return closure.getAt(0, "dis");
            }
            return returnValue.value;
        }
        // At the end of an initialiser, return a reference to the class.
        if (isInitializer) {
            return closure.getAt(0, "dis");
        }
        // The function reached the end of its body without a return statement.
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

}

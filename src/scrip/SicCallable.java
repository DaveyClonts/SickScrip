package scrip;

import java.util.List;

interface SicCallable {

    /**
     * @return the number of arguments of this callable.
     */
    int arity();

    /**
     * Calls this callable
     *
     * @param interpreter
     * @param arguments
     * @return
     */
    Object call(Interpreter interpreter, List<Object> arguments);

}

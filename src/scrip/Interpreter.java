package scrip;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Executes a list of statements.
 */
public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    Interpreter() {
        globals.define("clock", new SicCallable() {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }

        });

        globals.define("CheckIt", new SicCallable() {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                try {
                    return stdinReader.readLine();
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "<native fn>";
            }

        });

        globals.define("toNum", new SicCallable() {
            @Override public int arity() { return 1; }
            @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                try { return Double.parseDouble((String) arguments.get(0)); }
                catch (Exception e) { return null; }
            }
            @Override public String toString() { return "<native fn>"; }
        });

        globals.define("len", new SicCallable() {
            @Override public int arity() { return 1; }
            @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) ((String) arguments.get(0)).length();
            }
            @Override public String toString() { return "<native fn>"; }
        });

        globals.define("charAt", new SicCallable() {
            @Override public int arity() { return 2; }
            @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                String s = (String) arguments.get(0);
                int i = (int)(double)(Double) arguments.get(1);
                return String.valueOf(s.charAt(i));
            }
            @Override public String toString() { return "<native fn>"; }
        });

        globals.define("mod", new SicCallable() {
            @Override public int arity() { return 2; }
            @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                return (Double) arguments.get(0) % (Double) arguments.get(1);
            }
            @Override public String toString() { return "<native fn>"; }
        });
    }

    private final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));

    // Stores the scope depth of every local variable
    private final Map<Expr, Integer> locals = new HashMap<>();
    final Environment globals = new Environment();
    private Environment environment = globals;

    private static final Config defaultConfig = new Config(false);
    private Config config = defaultConfig;

    static record Config(boolean printExpressions) {
    }

    public void interpret(List<Stmt> statements) {
        interpret(statements, null);
    }

    public void interpret(List<Stmt> statements, Config config) {
        if (config == null) {
            config = defaultConfig;
        } else {
            this.config = config;
        }
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Sic.runtimeError(error);
        }
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double) right;
            default:
        }

        return null;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    /**
     * Verifies that {@code left} and {@code right} are numbers.
     * 
     * Throws a RuntimeError if the above statement is false.
     */
    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double) left * (double) right;
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }

                // if (left instanceof String || right instanceof String) {
                    // return stringify(left) + stringify(right);
                // }

                throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
            case SHIFT_LEFT:
                checkNumberOperands(expr.operator, left, right);
                return ((int) (double) left) << ((int) (double) right);
            case SHIFT_RIGHT:
                checkNumberOperands(expr.operator, left, right);
                return ((int) (double) left) >> ((int) (double) right);
            case TRIPLE_SHIFT:
                checkNumberOperands(expr.operator, left, right);
                return ((int) (double) left) >>> ((int) (double) right);
            default:
        }

        return null;
    }

    /**
     * 
     * @param object the value to evaluate
     * @return true if object == true or != false && != null
     */
    private boolean isTruthy(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        // Both are null
        if (a == null && b == null)
            return true;
        // a is null
        if (a == null)
            return false;

        return a.equals(b);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            var text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        // literals, strings etc
        return object.toString();
    }

    /**
     * 
     * @param expr the expression to evaluate
     * @return the result of evaluating the expression
     */
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    /**
     * 
     * @param stmt the statement to execute
     */
    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    public void resolve(Expr expr, int depth) {
        locals.put(expr, depth);
    }

    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;

            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            // Code in this finally block will always run even if an exception
            // (possibly unchecked) is thrown.

            // Restore the environment
            this.environment = previous;
        }
    }

    private void print(Object value) {
        System.out.println(stringify(value));
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        Object value = evaluate(stmt.expression);
        if (config.printExpressions) {
            print(value);
        }
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        // environment contains variables defined when this function was
        // declared.
        var function = new SicFunction(stmt, environment, false);
        environment.define(stmt.name.lexeme, function);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        print(value);
        return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }

        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);

        Integer distance = locals.get(expr);
        if (distance != null) {
            environment.assignAt(distance, expr.name, value);
        } else {
            globals.assign(expr.name, value);
        }

        return value;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return lookUpVariable(expr.name, expr);
    }

    private Object lookUpVariable(Token name, Expr expr) {
        Integer distance = locals.get(expr);
        if (distance != null) {
            return environment.getAt(distance, name.lexeme);
        } else {
            return globals.get(name);
        }
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitClassStmt(Stmt.Class stmt) {
        Object superclass = null;
        if (stmt.superclass != null) {
            superclass = evaluate(stmt.superclass);
            // Ensure that the superclass is actually a class not another type
            // of variable.
            if (!(superclass instanceof SicClass)) {
                throw new RuntimeError(stmt.superclass.name, "Superclass must be a class.");
            }
        }
        environment.define(stmt.name.lexeme, null);

        // Chain a child environment and add super to it. The methods stored
        // below will all have access to super.
        if (stmt.superclass != null) {
            environment = new Environment(environment);
            environment.define("Suppa", superclass);
        }

        // Store all methods in a map
        Map<String, SicFunction> methods = new HashMap<>();
        for (Stmt.Function method : stmt.methods) {
            SicFunction function = new SicFunction(method, environment, method.name.lexeme.equals("init"));
            methods.put(method.name.lexeme, function);
        }

        SicClass klass = new SicClass(stmt.name.lexeme, (SicClass) superclass, methods);

        if (superclass != null) {
            // Remove the environment holding "super" as it's been provided to
            // all methods of this class.
            environment = environment.enclosing;
        }
        environment.assign(stmt.name, klass);
        return null;

    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            // if condition is true.
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            // if condition is false and else statement is present.
            execute(stmt.elseBranch);
        }

        return null;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            // true OR expression = true so return a truthy expression
            if (isTruthy(left))
                return left;
        } else {
            // AND expression

            // false OR expression = false so return a falsey expression
            if (!isTruthy(left))
                return left;
        }

        return evaluate(expr.right);

    }

    @Override
    public Object visitSetExpr(Expr.Set expr) {
        Object object = evaluate(expr.object);

        if (!(object instanceof SicInstance)) {
            throw new RuntimeError(expr.name, "Only instances have fields.");
        }

        Object value = evaluate(expr.value);
        ((SicInstance) object).set(expr.name, value);
        return value;
    }

    @Override
    public Object visitSuperExpr(Expr.Super expr) {
        int distance = locals.get(expr);
        SicClass superclass = (SicClass) environment.getAt(distance, "Suppa");

        // The super class instance of the current instance.
        SicInstance object = (SicInstance) environment.getAt(distance - 1, "dis");

        SicFunction method = superclass.findMethod(expr.method.lexeme);

        if (method == null) {
            throw new RuntimeError(expr.method, "Undefined property '" + expr.method.lexeme + "'.");
        }
        return method.bind(object);
    }

    @Override
    public Object visitCallExpr(Expr.Call expr) {
        // The function/class being called
        Object callee = evaluate(expr.callee);

        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }

        // The callee must be a callable function/class
        if (!(callee instanceof SicCallable)) {
            throw new RuntimeError(expr.paren, "Can only call functions and classes.");
        }

        SicCallable function = (SicCallable) callee;

        // Ensure that the correct number of arguments are provided
        if (arguments.size() != function.arity()) {
            throw new RuntimeError(expr.paren,
                    String.format("Expected %d arguments but got %d.", function.arity(), arguments.size()));
        }

        return function.call(this, arguments);

    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        Object value = null;
        if (stmt.value != null)
            value = evaluate(stmt.value);

        //
        throw new Return(value);
    }

    @Override
    public Object visitGetExpr(Expr.Get expr) {
        Object object = evaluate(expr.object);
        if (object instanceof SicInstance) {
            return ((SicInstance) object).get(expr.name);
        }

        throw new RuntimeError(expr.name, "Only instances have properties.");
    }

    @Override
    public Object visitThisExpr(Expr.This expr) {
        return lookUpVariable(expr.keyword, expr);
    }

}
# SickScrip

**Author:** Davey Clonts

SickScrip was written using *Crafting Interpreters* as a reference. Features like dynamic typing were chosen based on what the book covers. The book walks you through writing Lox — SickScrip is a modified version of that foundation, expanded into its own language.

---

## Data Types

SickScrip is dynamically typed and supports the following types:

- **Booleans**
- **Numbers** — double-precision floating point
- **Strings** — enclosed in double quotes `""`
- **Null**

---

## Syntax

### Brackets

SickScrip does not use `{` and `}`. Instead:

| Symbol | Meaning        |
|--------|----------------|
| `xXx`  | Opening bracket |
| `XxX`  | Closing bracket |

### Statement Terminators

Instead of `;`, use one of the following:

- `no cap`
- `fr`
- `fr fr`

---

## Arithmetic

| Operation      | Operator |
|----------------|----------|
| Addition       | `PLUZ`   |
| Subtraction    | `MINUZ`  |
| Multiplication | `TIMEZ`  |
| Division       | `OUTTA`  |

---

## Comparison Operators

| Operator | Meaning                  |
|----------|--------------------------|
| `<`      | Less than                |
| `<=`     | Less than or equal to    |
| `>`      | Greater than             |
| `>=`     | Greater than or equal to |
| `==`     | Equal to                 |
| `!=`     | Not equal to             |

> **Note:** No implicit type conversions. `123 == "123"` evaluates to `Nope`.

---

## Logical Operators

| Value / Expression  | Result |
|---------------------|--------|
| `Yep`               | true   |
| `Nope`              | false  |
| `!Yep`              | `Nope` |
| `!Nope`             | `Yep`  |
| `Yep AnD Nope`      | `Nope` |
| `Yep OrR Nope`      | `Yep`  |

Operator precedence follows the same rules as C.

---

## Variables

Declare a variable with the `letta` keyword. After declaration, access it without `letta`:

```
letta myVar = "Adam"
HollaAtYaBoyz myVar   // Adam

myVar = "Todd"
HollaAtYaBoyz myVar   // Todd
```

---

## Control Flow

| Construct | Keyword    |
|-----------|------------|
| if        | `dat`      |
| while     | `loopyFR`  |
| for       | `loopyDat` |

---

## Functions

Declare functions with the `FUnKYY` keyword:

```
FUnKYY myFunction(a, b) xXx
    HollaAtYaBoyz a PLUZ b
XxX
```

---

## Classes

Declare classes with the `SCHOOLED` keyword. Methods inside a class do **not** use the `FUnKYY` keyword:

```
SCHOOLED MyClass xXx
    myFunc() xXx XxX
XxX
```

Create a new instance:

```
letta someVar = MyClass()
someFunction(MyClass)
```

---

## Built-in Functions

| Function      | Description                  |
|---------------|------------------------------|
| `HollaAtYaBoyz` | Print output to console    |
| `checkIt()`   | Read input from the user     |

---

## Running SickScrip

1. Open the project in File Explorer.
2. Run the `start` batch file.

For working code examples, see `ReadMeExample.sic`.
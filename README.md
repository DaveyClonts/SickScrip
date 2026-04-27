SickScrip

    Author: Davey Clonts
    I wrote this language using a book called Crafting Interpreters as a refrence
    This means that I choose features that the book looked at such as dynamic typing and what not
    Lox's code, which you are walked through writing, is the base for this project, and i modified the functionality of it into what is now SickScrip


    Data Types (SickScrip is dynamicaly typed):
    Booleans
    Numbers - double precision floating point
    Strings - ""
    Null

    Arithmetic:
    + is written as PLUZ
    - is written as MINUZ
    * is written as TIMEZ
    / is written as OUTTA

    Comparison:
    <
    <=
    >
    >=
    == - Note no implicit conversions across types, so 123 == "123" is false
    !=

    SickScrip doesnt use opening or closing brackets
    instead use these:
    xXx - open bracket
    XxX - closing bracket

    also instead of ; it is either
    no cap
    fr
    fr fr

    Logical Operators:
    true is Yep
    flase is Nope

    !Yep makes Nope and so on
    Yep AnD Nope is = Nope
    Yep OrR false is = Yep

    Precendence:
    Same as C

    Variables:
    Can be declared with letta myVar
    after declared can be accessed without letta, so:
    letta myVar = "Adam"
    HollaAtYaBoyz myVar //Adam
    myVar = "Todd"
    HollAtYaBoyz myVar //Todd

    Control Flow:
    if - dat
    while - loopyFR
    for - loopyDat

    Function:
    FUnKYY myFunction(a, b) xXx
        HollAtYaBoyz a PLUZ b;
    XxX

    Classes:
    SCHOOLED myClass xXx
        myFunc() xXx XxX
    XxX
    Functions inside of classes are not using the FUnKYY keyword

    create new instances like this:
    letta someVar = MyClass();
    someFunction(MyClass);

    ETC:
    print function is HollaAtYaBoyz
    input function is checkIt()

    please refer to ReadMeExample.sic if you need an example of how to write working code

    to run code, open up the project in file explorer and use the start batch file

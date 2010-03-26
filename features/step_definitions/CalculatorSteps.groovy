this.metaClass.mixin(cuke4duke.GroovyDsl)

//World {
//}

Before() {
    calc = new Calculator()
}

Given(~"I have entered (\\d+) into the calculator") { int n ->
    calc.push n
}

When(~"I press (\\w+)") {op ->
    result = calc.send(op)
}

Then(~"the result should be (.*) on the screen") { int r ->
    assert r == result
}

class Calculator {
    def numbers = []

    void push(number) {
        numbers << number
    }

    def send(operation) {
        def result
        switch (operation) {
            case "add":
                result = add()
        }
        numbers.clear()

        result
    }

    private def add() {
        numbers.sum()
    }
}
